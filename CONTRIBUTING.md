# Contributing

Thanks for your interest in contributing to Anki Sheet Sync Public.

## Reporting Bugs

Before opening an issue:

1. Make sure you are using the latest release.
2. Check whether the issue has already been reported.
3. Include clear steps to reproduce the problem.
4. Include relevant error messages or screenshots.
5. Do not share private Google Sheets, Anki databases, passwords, API keys, or other sensitive information.

## Suggesting Features

Feature requests are welcome.

Please explain:

- What problem the feature would solve.
- How you expect it to work.
- Why it would be useful to other users.

## Pull Requests

1. Fork the repository.
2. Create a new branch for your change.
3. Keep changes focused and easy to review.
4. Test the application before submitting the pull request.
5. Update documentation when necessary.
6. Provide a clear description of your changes.

## Development

The project is developed with Sketchware Pro and exported as an Android Studio-compatible Gradle project.

The application uses the AnkiDroid API to communicate with AnkiDroid.

## Code Changes

Please avoid unrelated changes in pull requests.

When modifying synchronization behavior, test at least:

- New cards.
- Existing cards with unchanged content.
- Existing cards with changed content.
- Duplicate content.
- Multiple Anki decks.
- Invalid Sheet rows.

## License

By contributing to this project, you agree that your contributions will be licensed under the MIT License.
