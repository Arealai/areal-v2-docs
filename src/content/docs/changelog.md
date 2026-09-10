---
title: Changelog
description: Notable changes to the Areal V2 platform and this documentation.
---

Notable changes to the Areal V2 platform and this documentation.

## September 10, 2026

### Added

- When a [CD balancer](cdbalancer.md) run fails, the dashboard now shows a short explanation of why.
- Fee rename recommendations note how often that rename has been approved.

### Changed

- After-balancing summaries are easier to read.
- Locked fees import more reliably when amounts change.
- Fee mapping is smarter when names overlap or use common abbreviations.

### Fixed

- A few dashboard annoyances are gone: comparisons keep your edits when document links refresh, and leaving a page mid-load or after an app update no longer flashes an error.

## August 24, 2026

### Added

- **Tickets.** Open and track support tickets from the dashboard. Link a ticket to a loan, document, CD balance, or Copilot task. Set status, priority, assignee, and tags; add comments and file attachments. Filter the list, reopen a closed ticket, or delete one.
- **Second-lien loans.** Bond closings keep a paired primary and second-lien [loan](sessions.md). The dashboard shows the linked loan. Closing packages that mix both liens — including files dropped in OneDrive — land on the primary loan, with second-lien documents classified as such. Documents already processed on the second-lien loan can be copied to the primary so they file with the first-lien loan. Second-lien loans do not push on their own.
- **2nd-lien document types.** New 2nd Mortgage Loan Estimate and 2nd Mortgage Initial Closing Disclosure, alongside 2nd Closing Disclosure, 2nd 1003, 2nd Security Instrument, Endorsement Allonge, and First Payment Letter.
- **Byte.** Connect Byte as a loan origination system: look up loans, pull and push documents, and ingest OneDrive files onto the matching Byte loan. [CD balancer](cdbalancer.md) now pulls and pushes Closing Disclosures to Byte, including fees.

### Changed

- **Extraction.** More accurate borrower and signing-party names on Closing Disclosures and Deeds of Trust, plus loan number on Deeds of Trust and names on the 1003. Improvements also on Appraisal Report, Endorsement Allonge, flood hazard, and driver's license.
- **Copilot.** Company names match when one side uses a DBA. Trusts, LLCs, and capacity titles (for example “Managing Member”) match more reliably. Addresses follow USPS street abbreviations. Rider types are recognized more consistently. The run panel shows which documents were used and the recommended next action.

### Fixed

- Copilot tasks shared with non-admin users open correctly instead of showing an error.
- Copilot no longer treats a task that has never been run as if a run were already in progress.
