# Changelog

Notable changes to the Areal V2 platform and this documentation.

## August 24, 2026 { id="2026-08-24" }

### Added

- **Tickets.** Create and track support tickets from the dashboard, linked to an upload session, document, CD balancer request, or Copilot task. Tickets have status, priority, assignee, tags, comments, and file attachments. Closed tickets can be reopened. The list filters by Active (open + in progress), assignee, opened-by, and organization. Reviewers can delete tickets.
- **Second-lien sessions.** Bond closings keep a paired primary and second-lien [upload session](sessions.md). The dashboard shows the linked loan on the session title bar. Second-lien sessions (loan class `Second`) do not push; filing happens from the primary.
- **Second-lien classification (OneDrive and other ingress).** Intermingled closing packages — including files dropped in OneDrive — classify onto the primary session. Children that belong to the paired second loan are assigned 2nd-lien templates (2nd Closing Disclosure, 2nd Loan Estimate, 2nd 1003, 2nd Security Instrument, Endorsement Allonge, First Payment Letter).
- **Second-lien document copy.** Completed documents already on the second-lien session can be copied onto the primary (`POST /sessions/{id}/copy_second_lien_documents/`), keeping their 2nd-lien templates so ordinary Encompass push files them on the primary loan. The document list exposes copy origin as `external_references.second_lien`.
- **2nd-lien templates.** New **2nd Mortgage – Loan Estimate** and **2nd Mortgage – Initial Closing Disclosure** templates, used by classification remap and document copy.
- **Byte LOS.** Byte is a first-class gateway: loan lookup, field get/update, document pull, and document push. OneDrive ingest can route files into a Byte session (also MeridianLink or Encompass) and trigger the matching LOS loan sync.
- **Byte Closing Disclosure.** [CD balancer](cdbalancer.md) pull and push for Byte, including fee line items and per-fee `los_extra` round-trip so LOS identifiers survive a balance.

### Changed

- **Extraction.** More accurate Closing Disclosure borrower and signing-party names; Deed of Trust borrower names, signing-party names, and loan number; 1003 name matching. Selected templates (CD buyer/seller, purchase contract, rate note, right to cancel, HOI, EO compliance) now extract with Sonnet 5. Appraisal Report gained additional components; Endorsement Allonge maturity date and city, flood hazard, and driver's license extraction also improved.
- **Copilot matching.** Company names treat DBA aliases as equivalent to the legal entity. Person-vs-entity records (trusts, LLCs, “Managing Member of …”) match without an LLM fallback. Addresses normalize USPS street suffixes and directionals. Rider types resolve through alias matching.
- **Copilot runs.** Never-run tasks return `results.status: null` instead of `"pending"` (`"pending"` is only for in-flight runs). Suppressed results include `suppressed_by`. The execution bench lists included documents and the best next action.

### Fixed

- Second-lien copies keep their source template and still file on the primary loan; Encompass push does not apply the doc-pull exclusion to those copies.
- CD balancer per-fee `los_extra` is deep-copied so Byte/Encompass CD push does not lose LOS identifiers.
- Copilot: DBA and entity-name mismatches that previously failed validation; non-admins can open a task they were linked to instead of hitting an error.

## August 17, 2026 { id="2026-08-17" }

### Added

- [Changelog](changelog.md) page for notable platform and documentation updates

