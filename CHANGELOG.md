# Changelog

All notable changes to this project are documented in this file.

## [1.0.0] - 2026-09-04

### Added

- Initial public release of Anki Sheet Sync Public.
- Sync cards from publicly accessible Google Sheets.
- AnkiDroid database permission handling.
- Automatic Anki deck creation.
- Automatic Anki note type creation.
- Card creation through the AnkiDroid API.
- Existing card updates when Sheet content changes.
- Duplicate detection based on ID and card content.
- Duplicate detection scoped to the selected Anki deck.
- Sync progress indicator.
- Sync result summary showing added, updated, duplicate, and invalid cards.
- Sketchware Pro project backup.

### Known Limitations

- Google Sheet CSV parsing currently expects three comma-separated columns: ID, Front, and Back.
- Complex CSV values containing commas inside quoted fields are not yet supported.
- Duplicate IDs within the same Sheet are not yet fully validated before synchronization.

[1.0.0]: https://github.com/divyam-gawde/anki-sheet-sync-public/releases/tag/v1.0.0
