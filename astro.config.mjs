// @ts-check
import { defineConfig } from 'astro/config';
import starlight from '@astrojs/starlight';
import mermaid from 'astro-mermaid';

// https://astro.build/config
export default defineConfig({
  site: 'https://docs.v2.areal.ai',
  outDir: 'site',
  trailingSlash: 'always',
  integrations: [
    mermaid({
      theme: 'dark',
      autoTheme: true,
    }),
    starlight({
      title: 'Areal V2 Docs',
      description: 'Documentation for the Areal V2 platform and API.',
      logo: {
        src: './src/assets/logo.png',
        alt: 'Areal',
      },
      favicon: '/favicon.png',
      customCss: ['./src/styles/custom.css'],
      social: [
        {
          icon: 'github',
          label: 'GitHub',
          href: 'https://github.com/Arealai/areal-v2-docs',
        },
      ],
      sidebar: [
        { label: 'Home', link: '/' },
        { label: 'Accounts', slug: 'auth' },
        {
          label: 'Processing',
          items: [
            { label: 'Document Processing', slug: 'processing/processing' },
            { label: 'Status Tracking', slug: 'processing/status' },
            { label: 'Webhooks', slug: 'processing/webhooks' },
            { label: 'Email', slug: 'processing/email' },
            { label: 'Annotations', slug: 'processing/annotations' },
            { label: 'Indexing', slug: 'processing/indexing' },
          ],
        },
        {
          label: 'Session',
          items: [
            { label: 'Upload Sessions', slug: 'sessions' },
            { label: 'LoanInfo', slug: 'loan_info' },
          ],
        },
        { label: 'CDBalancer', slug: 'cdbalancer' },
        {
          label: 'Migration Guide',
          items: [
            { label: 'Overview', slug: 'migration' },
            { label: 'Code Examples', slug: 'migration/examples' },
          ],
        },
        { label: 'Changelog', slug: 'changelog' },
      ],
    }),
  ],
});
