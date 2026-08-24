import fs from 'node:fs';
import path from 'node:path';
import { fileURLToPath } from 'node:url';

const repoRoot = path.resolve(path.dirname(fileURLToPath(import.meta.url)), '..');
const docsRoot = path.join(repoRoot, 'docs');
const outRoot = path.join(repoRoot, 'src/content/docs');

const asideMap = {
  warning: 'caution',
  info: 'note',
  note: 'note',
  tip: 'tip',
  danger: 'danger',
  failure: 'danger',
};

const descriptions = {
  'auth.md': 'Authenticate with JWT cookies, bearer tokens, API keys, and MFA.',
  'sessions.md': 'Group documents into an upload session for each loan.',
  'loan_info.md': 'Get a consolidated LoanInfo view of a processed mortgage loan.',
  'cdbalancer.md': 'Balance two processed Closing Disclosure documents.',
  'changelog.md': 'Notable changes to the Areal V2 platform and this documentation.',
  'processing/processing.md': 'Upload PDFs and extract classified, structured mortgage data.',
  'processing/status.md': 'Track asynchronous processing with WebSockets or polling.',
  'processing/webhooks.md': 'Receive event-based notifications for processing and CDBalancer.',
  'processing/email.md': 'Configure email notifications and process documents over email.',
  'processing/annotations.md': 'Detect signers, notaries, and witnesses on documents.',
  'processing/indexing.md': 'Group, duplicate, and download indexed documents.',
  'migration/index.md': 'Migrate an Areal V1 API integration to Areal V2.',
  'migration/examples.md': 'Authentication and document processing examples for V1 to V2 migration.',
};

const linkFixes = [
  ['(processing/status.md)', '(status.md)'],
  ['(websocket.md)', '(#websocket-api)'],
  ['(../accounts/auth.md)', '(../auth.md)'],
  ['(../accounts/profiles.md)', '(../auth.md)'],
  ['(../processing/index.md)', '(../processing/processing.md)'],
];

function toImportName(relFile) {
  return relFile
    .replace(/\.[^.]+$/, '')
    .replaceAll(/[^a-zA-Z0-9]+/g, '_')
    .replace(/^(\d)/, '_$1');
}

function importPathFor(sourceRel, snippetRel) {
  const depth = sourceRel.split('/').length;
  const prefix = '../'.repeat(depth + 1);
  return `${prefix}code_samples/${snippetRel}?raw`;
}

function convertAdmonitions(markdown) {
  return markdown.replace(
    /^!!!\s+(\w+)(?:\s+"([^"]*)")?\n((?:[ \t]*\n|[ \t]{4,}.*\n)*)/gm,
    (_match, type, title, body) => {
      const kind = asideMap[type] ?? 'note';
      const heading = title ? `[${title}]` : '';
      const content = body
        .split('\n')
        .map((line) => (line.startsWith('    ') ? line.slice(4) : line.trimEnd()))
        .join('\n')
        .replace(/^\n+/, '')
        .replace(/\n+$/, '');
      return `:::${kind}${heading}\n${content}\n:::\n\n`;
    },
  );
}

function convertTabs(markdown, sourceRel) {
  const tabBlock =
    /(?:^=== "[^"]+"\n\n[ \t]*```[\s\S]*?--8<--[\s\S]*?```\n(?:\n)?)+/gm;

  const imports = new Map();

  const next = markdown.replace(tabBlock, (block) => {
    const samples = [];
    const itemRe =
      /^=== "([^"]+)"\n\n[ \t]*```(\S+)[^\n]*\n[ \t]*--8<-- "([^"]+)"\n[ \t]*```/gm;
    let item;
    while ((item = itemRe.exec(block))) {
      const label = item[1];
      const fenceLang = item[2];
      const snippet = item[3].replace(/^code_samples\//, '').replace('/c#/', '/csharp/');
      const titleMatch = block.slice(item.index, item.index + item[0].length).match(/title="([^"]+)"/);
      const lang =
        fenceLang === 'py' || fenceLang === 'python'
          ? 'python'
          : fenceLang === 'csharp' || fenceLang === 'cs'
            ? 'csharp'
            : fenceLang;
      const name = toImportName(snippet);
      imports.set(name, importPathFor(sourceRel, snippet));
      samples.push({ label, name, lang, title: titleMatch?.[1] ?? label });
    }

    if (samples.length === 0) return block;

    const tabs = samples
      .map(
        (sample) => `  <TabItem label="${sample.label}">
    <Code code={${sample.name}} lang="${sample.lang}" title="${sample.title}" />
  </TabItem>`,
      )
      .join('\n');

    return `<Tabs>\n${tabs}\n</Tabs>\n\n`;
  });

  return { markdown: next, imports };
}

function extractTitle(markdown) {
  const match = markdown.match(/^#\s+(.+)\n/);
  return {
    title: match ? match[1].replaceAll('\u00a0', ' ').trim() : 'Untitled',
    body: match ? markdown.slice(match[0].length).replace(/^\n+/, '') : markdown,
  };
}

function yamlEscape(value) {
  return value.replaceAll('"', '\\"');
}

function convertFile(sourceRel) {
  const source = path.join(docsRoot, sourceRel);
  let markdown = fs.readFileSync(source, 'utf8').replaceAll('\r\n', '\n');
  const { title, body } = extractTitle(markdown);
  markdown = body;
  markdown = convertAdmonitions(markdown);
  const converted = convertTabs(markdown, sourceRel);
  markdown = converted.markdown;
  for (const [from, to] of linkFixes) {
    if (sourceRel === 'processing/processing.md' || sourceRel === 'processing/status.md') {
      markdown = markdown.replaceAll(from, to);
    }
  }
  if (sourceRel === 'migration/index.md') {
    markdown = markdown.replaceAll('(../accounts/auth.md)', '(../auth.md)');
    markdown = markdown.replaceAll('(../accounts/profiles.md)', '(../auth.md)');
    markdown = markdown.replaceAll('(../processing/index.md)', '(../processing/processing.md)');
  }
  markdown = markdown.replace(/^## (.+) \{ id="([^"]+)" \}$/gm, '## $1\n');
  markdown = markdown.replace(/^## (.+) \{#([^}]+)\}$/gm, '## $1');

  const needsMdx = converted.imports.size > 0;
  const importLines = needsMdx
    ? [
        "import { Code, TabItem, Tabs } from '@astrojs/starlight/components';",
        ...[...converted.imports.entries()].map(
          ([name, importPath]) => `import ${name} from '${importPath}';`,
        ),
        '',
      ]
    : [];

  const description = descriptions[sourceRel] ?? title;
  const frontmatter = [
    '---',
    `title: ${yamlEscape(title)}`,
    `description: ${yamlEscape(description)}`,
    '---',
    '',
  ];

  const output = [...frontmatter, ...importLines, markdown.replace(/\n+$/, ''), ''].join('\n');
  const destRel = sourceRel.replace(/\.md$/, needsMdx ? '.mdx' : '.md');
  const dest = path.join(outRoot, destRel);
  fs.mkdirSync(path.dirname(dest), { recursive: true });
  fs.writeFileSync(dest, output);
  console.log(`wrote ${path.relative('.', dest)}`);
}

const files = [
  'auth.md',
  'sessions.md',
  'loan_info.md',
  'cdbalancer.md',
  'changelog.md',
  'processing/processing.md',
  'processing/status.md',
  'processing/webhooks.md',
  'processing/email.md',
  'processing/annotations.md',
  'processing/indexing.md',
  'migration/index.md',
  'migration/examples.md',
];

for (const file of files) convertFile(file);
