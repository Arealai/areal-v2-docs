# areal-v2-docs

The public docs site for customers who call the Areal API v2 (`https://docs.areal.ai/`):
hand-written guides to authentication, processing, status tracking, webhooks, loans, CD
Balancer and copilot, built with Astro and Starlight. The endpoint reference is not built
here: the home page links to api-v2's own docs (`/api/v2/docs`, OpenAPI at
`/api/v2/openapi.json`). Workspace-wide context is in `../AGENTS.md`.

## Glossary

- The pages use the dashboard's words and give the API name next to them: a **loan** is an
  Upload Session (`UploadSession`), an **agent** is a copilot task group.

## Layout

- `src/content/docs/`: the pages (`.mdx`, `.md`). The sidebar is listed by hand in
  `astro.config.mjs`, so a new page needs an entry there to show up in it.
- `src/code_samples/<topic>/<language>/`: the code shown in the pages (Python, C#, Java),
  imported into the MDX with `?raw`.
- `src/components/`, `src/styles/custom.css`, `src/assets/`, `public/`: site components,
  styles, images and static files.
- `partner-guides/`: standalone files, not part of the site build. `copilot-agents-api.html` is
  the Copilot agents integration guide for partners.
  `build_copilot_openapi_subset.py` generates its API reference, `copilot-agents-openapi.json`
  and the Scalar page `copilot-agents-openapi.html`, from the UAT OpenAPI spec (usage in its
  docstring). Every endpoint the guide mentions must be in the script's `OPERATIONS`.

## Workflows

- **Setup**: Node 22 (`.nvmrc`), `npm install`.
- **Run**: `npm run dev`, then open `http://localhost:4321/`.
- **Build**: `npm run build` writes the static site to `site/` (`outDir` in
  `astro.config.mjs`, ignored by git); `npm run preview` serves it.
- **Deploy**: branch off `development` and open a PR into it; it's the only deploy branch. A
  push to it runs `.github/workflows/deploy.yml`: `npm ci`, `npm run build`,
  `aws s3 sync ./site s3://docs.areal.ai` and a CloudFront invalidation, so a merge reaches
  customers right away.

## Test gaps

- Nothing builds the site before a merge: a page that breaks the build fails `deploy.yml` on
  `development` and the old site stays live. Run `npm run build` before you merge.
