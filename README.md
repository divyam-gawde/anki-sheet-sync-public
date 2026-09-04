# Anki Sheet Sync Public

An Android app that syncs cards from a public Google Sheet directly into AnkiDroid.

## Features

- Sync cards from a public Google Sheet
- No Google account or sign-in required
- Works with AnkiDroid
- Creates the selected Anki deck automatically
- Updates cards when the same Sheet ID has changed
- Skips duplicate cards
- Prevents duplicate content within the selected deck
- Shows sync progress
- Uses the AnkiDroid API for database operations

## Google Sheet Format

Your Google Sheet should contain these columns:

| ID | Front | Back |
| --- | --- | --- |
| 1 | What is the capital of France? | Paris |
| 2 | 2 + 2 | 4 |
| 3 | Largest planet? | Jupiter |

The spreadsheet must be publicly accessible.

## Requirements

- Android 5.0 (API 21) or higher
- AnkiDroid installed
- AnkiDroid database permission granted
- A publicly accessible Google Sheet

## How to Use

1. Install Anki Sheet Sync Public.
2. Open the app.
3. Grant AnkiDroid database permission.
4. Paste your public Google Sheet URL.
5. Enter the Anki deck name.
6. Tap **Sync Deck**.
7. The app downloads the Sheet and synchronizes the cards with AnkiDroid.

## Duplicate Handling

The sync engine uses the following rules:

1. Same ID + same Front/Back → skipped.
2. Same ID + changed Front or Back → updated.
3. Different ID + same Front/Back → skipped.
4. Different ID + different Front/Back → added.
5. Duplicate IDs in the Sheet should be treated as invalid data.

Duplicate detection is scoped to the selected Anki deck.

## Download

APK releases are published through the GitHub Releases page.

## Source

This project is built with Sketchware Pro and exported as an Android Studio-compatible Gradle project.

The original Sketchware project backup is also included in this repository.

## License

This project is licensed under the MIT License. See [LICENSE](LICENSE).

## Contributing

Contributions, bug reports, and suggestions are welcome.

See [CONTRIBUTING.md](CONTRIBUTING.md) for contribution guidelines.
