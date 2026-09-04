package com.divyam.ankisheetsyncpublic;

import android.animation.*;
import android.app.*;
import android.app.Activity;
import android.app.DialogFragment;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.*;
import android.content.res.*;
import android.graphics.*;
import android.graphics.drawable.*;
import android.media.*;
import android.net.*;
import android.os.*;
import android.text.*;
import android.text.style.*;
import android.util.*;
import android.view.*;
import android.view.View;
import android.view.View.*;
import android.view.animation.*;
import android.webkit.*;
import android.widget.*;
import com.divyam.ankisheetsyncpublic.databinding.*;
import com.ichi2.anki.api.*;
import java.io.*;
import java.text.*;
import java.util.*;
import java.util.regex.*;
import org.json.*;

public class MainActivity extends Activity {
	
	private MainBinding binding;
	private String csvData = "";
	private java.util.ArrayList<String> frontList = new java.util.ArrayList<String>();
	private java.util.ArrayList<String> backList = new java.util.ArrayList<String>();
	
	@Override
	protected void onCreate(Bundle _savedInstanceState) {
		super.onCreate(_savedInstanceState);
		binding = MainBinding.inflate(getLayoutInflater());
		setContentView(binding.getRoot());
		initialize(_savedInstanceState);
		initializeLogic();
	}
	
	private void initialize(Bundle _savedInstanceState) {
		
		binding.buttonPermission.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View _view) {
				if (android.os.Build.VERSION.SDK_INT >= 23) {
					
					requestPermissions(
					new String[]{"com.ichi2.anki.permission.READ_WRITE_DATABASE"},
					1001
					);
					
				} else {
					
					((android.widget.EditText) findViewById(R.id.edittext_sheet_url))
					.setEnabled(true);
					
					((android.widget.Button) findViewById(R.id.button_sync))
					.setEnabled(true);
					
					((android.widget.TextView) findViewById(R.id.text_status))
					.setText("Anki permission granted");
					
				}
			}
		});
		
		binding.buttonSync.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View _view) {
				if (_validateInput()) {
					android.widget.Button syncButton =
					(android.widget.Button) findViewById(
					R.id.button_sync
					);
					
					android.widget.ProgressBar progressBar =
					(android.widget.ProgressBar) findViewById(
					R.id.progressbar_sync
					);
					
					// Disable Sync while syncing
					syncButton.setEnabled(false);
					
					// Show progress bar
					progressBar.setVisibility(
					android.view.View.VISIBLE
					);
					
					// Use real 0-100% progress
					progressBar.setIndeterminate(false);
					progressBar.setMax(100);
					progressBar.setProgress(0);
					_downloadSheet();
				}
			}
		});
	}
	
	private void initializeLogic() {
		((android.widget.EditText) findViewById(R.id.edittext_sheet_url))
		.setEnabled(false);
		
		((android.widget.EditText) findViewById(R.id.edittext_deck_name))
		.setEnabled(false);
		
		((android.widget.Button) findViewById(R.id.button_sync))
		.setEnabled(false);
		
		((android.widget.TextView) findViewById(R.id.text_status))
		.setText("Anki permission required");
		
		((android.widget.ProgressBar) findViewById(
		R.id.progressbar_sync
		)).setVisibility(
		android.view.View.GONE
		);        
	}
	
	@Override
	public void onResume() {
		super.onResume();
		if (android.os.Build.VERSION.SDK_INT >= 23) {
			
			if (checkSelfPermission("com.ichi2.anki.permission.READ_WRITE_DATABASE")
			== android.content.pm.PackageManager.PERMISSION_GRANTED) {
				
				// Permission granted
				((android.widget.EditText) findViewById(R.id.edittext_sheet_url))
				.setEnabled(true);
				
				((android.widget.EditText) findViewById(R.id.edittext_deck_name))
				.setEnabled(true);
				
				((android.widget.Button) findViewById(R.id.button_sync))
				.setEnabled(true);
				
				// Hide permission button and status
				((android.widget.Button) findViewById(R.id.button_permission))
				.setVisibility(android.view.View.GONE);
				
				((android.widget.TextView) findViewById(R.id.text_status))
				.setVisibility(android.view.View.GONE);
				
			} else {
				
				// Permission not granted
				((android.widget.EditText) findViewById(R.id.edittext_sheet_url))
				.setEnabled(false);
				
				((android.widget.EditText) findViewById(R.id.edittext_deck_name))
				.setEnabled(false);
				
				((android.widget.Button) findViewById(R.id.button_sync))
				.setEnabled(false);
				
				// Show permission button and status
				((android.widget.Button) findViewById(R.id.button_permission))
				.setVisibility(android.view.View.VISIBLE);
				
				((android.widget.Button) findViewById(R.id.button_permission))
				.setText("Grant Anki Permission");
				
				((android.widget.TextView) findViewById(R.id.text_status))
				.setVisibility(android.view.View.VISIBLE);
				
				((android.widget.TextView) findViewById(R.id.text_status))
				.setText("Anki permission required");
			}
			
		} else {
			
			// Android versions below 6.0
			((android.widget.EditText) findViewById(R.id.edittext_sheet_url))
			.setEnabled(true);
			
			((android.widget.EditText) findViewById(R.id.edittext_deck_name))
			.setEnabled(true);
			
			((android.widget.Button) findViewById(R.id.button_sync))
			.setEnabled(true);
			
			((android.widget.Button) findViewById(R.id.button_permission))
			.setVisibility(android.view.View.GONE);
			
			((android.widget.TextView) findViewById(R.id.text_status))
			.setVisibility(android.view.View.GONE);
		}
	}
	public boolean _validateInput() {
		final android.widget.EditText urlBox =
		(android.widget.EditText) findViewById(
		R.id.edittext_sheet_url
		);
		
		final android.widget.EditText deckBox =
		(android.widget.EditText) findViewById(
		R.id.edittext_deck_name
		);
		
		final String sheetUrl =
		urlBox.getText().toString().trim();
		
		final String deckName =
		deckBox.getText().toString().trim();
		
		if (android.os.Build.VERSION.SDK_INT >= 23) {
			
			if (checkSelfPermission(
			"com.ichi2.anki.permission.READ_WRITE_DATABASE")
			!= android.content.pm.PackageManager.PERMISSION_GRANTED) {
				
				android.widget.Toast.makeText(
				getApplicationContext(),
				"Anki permission is required",
				android.widget.Toast.LENGTH_SHORT
				).show();
				
				return false;
			}
		}
		
		if (sheetUrl.length() == 0) {
			
			android.widget.Toast.makeText(
			getApplicationContext(),
			"Please enter Google Sheet URL",
			android.widget.Toast.LENGTH_SHORT
			).show();
			
			return false;
		}
		
		if (deckName.length() == 0) {
			
			android.widget.Toast.makeText(
			getApplicationContext(),
			"Please enter Anki deck name",
			android.widget.Toast.LENGTH_SHORT
			).show();
			
			return false;
		}
		
		return true;
	}
	
	
	public void _downloadSheet() {
		new Thread(new Runnable() {
			@Override
			public void run() {
				
				// Progress: download starting
				runOnUiThread(new Runnable() {
					@Override
					public void run() {
						
						android.widget.ProgressBar progressBar =
						(android.widget.ProgressBar) findViewById(
						R.id.progressbar_sync
						);
						
						progressBar.setIndeterminate(false);
						progressBar.setMax(100);
						progressBar.setProgress(10);
					}
				});
				
				try {
					
					// Get Google Sheet URL
					final String sheetUrl =
					((android.widget.EditText) findViewById(
					R.id.edittext_sheet_url
					))
					.getText()
					.toString()
					.trim();
					
					// Extract Spreadsheet ID
					java.util.regex.Matcher matcher =
					java.util.regex.Pattern.compile(
					"/spreadsheets/d/([a-zA-Z0-9-_]+)"
					).matcher(sheetUrl);
					
					if (!matcher.find()) {
						
						runOnUiThread(new Runnable() {
							@Override
							public void run() {
								
								android.widget.Toast.makeText(
								getApplicationContext(),
								"Invalid Google Sheet URL",
								android.widget.Toast.LENGTH_LONG
								).show();
								
							}
						});
						
						return;
					}
					
					final String spreadsheetId =
					matcher.group(1);
					
					// Create CSV export URL
					String csvUrl =
					"https://docs.google.com/spreadsheets/d/"
					+ spreadsheetId
					+ "/export?format=csv";
					
					// Open connection
					java.net.URL url =
					new java.net.URL(csvUrl);
					
					java.net.HttpURLConnection connection =
					(java.net.HttpURLConnection)
					url.openConnection();
					
					connection.setRequestMethod("GET");
					connection.setConnectTimeout(15000);
					connection.setReadTimeout(15000);
					
					// Check server response
					int responseCode =
					connection.getResponseCode();
					
					if (responseCode != 200) {
						
						connection.disconnect();
						
						runOnUiThread(new Runnable() {
							@Override
							public void run() {
								
								android.widget.Toast.makeText(
								getApplicationContext(),
								"Could not download Google Sheet",
								android.widget.Toast.LENGTH_LONG
								).show();
								
							}
						});
						
						return;
					}
					
					// Read CSV
					java.io.InputStream inputStream =
					connection.getInputStream();
					
					java.io.BufferedReader reader =
					new java.io.BufferedReader(
					new java.io.InputStreamReader(
					inputStream,
					"UTF-8"
					)
					);
					
					StringBuilder data =
					new StringBuilder();
					
					String line;
					
					while ((line = reader.readLine()) != null) {
						
						data.append(line);
						data.append("\n");
					}
					
					reader.close();
					inputStream.close();
					connection.disconnect();
					
					// Save downloaded CSV
					csvData = data.toString();
					
					// Progress: download complete
					runOnUiThread(new Runnable() {
						@Override
						public void run() {
							
							android.widget.ProgressBar progressBar =
							(android.widget.ProgressBar) findViewById(
							R.id.progressbar_sync
							);
							
							progressBar.setProgress(25);
						}
					});
					
					// Continue to CSV parsing
					_parseCsv();
					
				} catch (final Exception e) {
					
					runOnUiThread(new Runnable() {
						@Override
						public void run() {
							
							android.widget.Toast.makeText(
							getApplicationContext(),
							"Sheet download error: "
							+ e.getMessage(),
							android.widget.Toast.LENGTH_LONG
							).show();
							
						}
					});
				}
			}
		}).start();
	}
	
	
	public void _parseCsv() {
		// Clear old data
		frontList.clear();
		backList.clear();
		
		// Progress: parsing started
		runOnUiThread(new Runnable() {
			@Override
			public void run() {
				
				android.widget.ProgressBar progressBar =
				(android.widget.ProgressBar) findViewById(
				R.id.progressbar_sync
				);
				
				progressBar.setProgress(30);
			}
		});
		
		// Make sure CSV data exists
		if (csvData == null || csvData.trim().length() == 0) {
			
			runOnUiThread(new Runnable() {
				@Override
				public void run() {
					
					android.widget.Toast.makeText(
					getApplicationContext(),
					"Google Sheet contains no data",
					android.widget.Toast.LENGTH_LONG
					).show();
					
				}
			});
			
			return;
		}
		
		// Split CSV into lines
		String[] lines =
		csvData.split("\\r?\\n");
		
		// Need header + at least one card
		if (lines.length < 2) {
			
			runOnUiThread(new Runnable() {
				@Override
				public void run() {
					
					android.widget.Toast.makeText(
					getApplicationContext(),
					"No cards found in Google Sheet",
					android.widget.Toast.LENGTH_LONG
					).show();
					
				}
			});
			
			return;
		}
		
		// Start after header
		for (int i = 1; i < lines.length; i++) {
			
			String line =
			lines[i].trim();
			
			// Skip empty rows
			if (line.length() == 0) {
				continue;
			}
			
			// Split columns
			String[] columns =
			line.split(",", -1);
			
			// Need ID + Front + Back
			if (columns.length < 3) {
				continue;
			}
			
			String id =
			columns[0].trim();
			
			String front =
			columns[1].trim();
			
			String back =
			columns[2].trim();
			
			// Skip incomplete rows
			if (id.length() == 0 ||
			front.length() == 0 ||
			back.length() == 0) {
				
				continue;
			}
			
			// Store ID + Front
			frontList.add(
			id + "\t" + front
			);
			
			// Store Back
			backList.add(back);
		}
		
		// Check result
		if (frontList.size() == 0) {
			
			runOnUiThread(new Runnable() {
				@Override
				public void run() {
					
					android.widget.Toast.makeText(
					getApplicationContext(),
					"No valid cards found in Google Sheet",
					android.widget.Toast.LENGTH_LONG
					).show();
					
				}
			});
			
			return;
		}
		
		// Progress: parsing complete
		runOnUiThread(new Runnable() {
			@Override
			public void run() {
				
				android.widget.ProgressBar progressBar =
				(android.widget.ProgressBar) findViewById(
				R.id.progressbar_sync
				);
				
				progressBar.setProgress(45);
			}
		});
		
		// Continue to Anki preparation
		_prepareAnki();
	}
	
	
	public void _prepareAnki() {
		try {
			
			// Progress: Anki preparation started
			runOnUiThread(new Runnable() {
				@Override
				public void run() {
					
					android.widget.ProgressBar progressBar =
					(android.widget.ProgressBar) findViewById(
					R.id.progressbar_sync
					);
					
					progressBar.setProgress(50);
				}
			});
			
			// Connect to AnkiDroid
			com.ichi2.anki.api.AddContentApi api =
			new com.ichi2.anki.api.AddContentApi(
			MainActivity.this
			);
			
			// Check AnkiDroid API permission
			if (android.os.Build.VERSION.SDK_INT >= 23) {
				
				if (checkSelfPermission(
				"com.ichi2.anki.permission.READ_WRITE_DATABASE")
				!= android.content.pm.PackageManager.PERMISSION_GRANTED) {
					
					runOnUiThread(new Runnable() {
						@Override
						public void run() {
							
							android.widget.Toast.makeText(
							getApplicationContext(),
							"AnkiDroid permission is required",
							android.widget.Toast.LENGTH_LONG
							).show();
							
						}
					});
					
					return;
				}
			}
			
			// Get requested deck name
			final String deckName =
			((android.widget.EditText) findViewById(
			R.id.edittext_deck_name
			))
			.getText()
			.toString()
			.trim();
			
			// Find deck
			java.util.Map<Long, String> decks =
			api.getDeckList();
			
			Long deckId = null;
			
			if (decks != null) {
				
				for (java.util.Map.Entry<Long, String> entry :
				decks.entrySet()) {
					
					if (deckName.equals(entry.getValue())) {
						
						deckId = entry.getKey();
						break;
					}
				}
			}
			
			// Create deck if it doesn't exist
			if (deckId == null) {
				
				deckId = api.addNewDeck(deckName);
				
				if (deckId == null) {
					
					runOnUiThread(new Runnable() {
						@Override
						public void run() {
							
							android.widget.Toast.makeText(
							getApplicationContext(),
							"Could not create Anki deck",
							android.widget.Toast.LENGTH_LONG
							).show();
							
						}
					});
					
					return;
				}
			}
			
			// Our ID-based note type
			String modelName =
			"Anki Sheet Sync ID";
			
			// Find existing model
			java.util.Map<Long, String> models =
			api.getModelList();
			
			Long modelId = null;
			
			if (models != null) {
				
				for (java.util.Map.Entry<Long, String> entry :
				models.entrySet()) {
					
					if (modelName.equals(entry.getValue())) {
						
						modelId = entry.getKey();
						break;
					}
				}
			}
			
			// Create model if it doesn't exist
			if (modelId == null) {
				
				String[] fields = new String[] {
					"ID",
					"Front",
					"Back"
				};
				
				String[] cards = new String[] {
					"Card 1"
				};
				
				// ID is hidden from the question
				String[] questionFormats = new String[] {
					"{{Front}}"
				};
				
				// ID is hidden from the answer
				String[] answerFormats = new String[] {
					"{{FrontSide}}<hr id=answer>{{Back}}"
				};
				
				modelId =
				api.addNewCustomModel(
				modelName,
				fields,
				cards,
				questionFormats,
				answerFormats,
				null,
				deckId,
				null
				);
				
				if (modelId == null) {
					
					runOnUiThread(new Runnable() {
						@Override
						public void run() {
							
							android.widget.Toast.makeText(
							getApplicationContext(),
							"Could not create Anki note type",
							android.widget.Toast.LENGTH_LONG
							).show();
							
						}
					});
					
					return;
				}
			}
			
			// Progress: Anki preparation complete
			runOnUiThread(new Runnable() {
				@Override
				public void run() {
					
					android.widget.ProgressBar progressBar =
					(android.widget.ProgressBar) findViewById(
					R.id.progressbar_sync
					);
					
					progressBar.setProgress(55);
				}
			});
			
			// Continue to card synchronization
			_addCards();
			
		} catch (final Exception e) {
			
			runOnUiThread(new Runnable() {
				@Override
				public void run() {
					
					android.widget.Toast.makeText(
					getApplicationContext(),
					"Anki preparation error: "
					+ e.getMessage(),
					android.widget.Toast.LENGTH_LONG
					).show();
					
				}
			});
		}
	}
	
	
	public void _addCards() {
		try {
			
			// ------------------------------------------------
			// CONNECT TO ANKI
			// ------------------------------------------------
			
			com.ichi2.anki.api.AddContentApi api =
			new com.ichi2.anki.api.AddContentApi(
			MainActivity.this
			);
			
			// ------------------------------------------------
			// GET SELECTED DECK NAME
			// ------------------------------------------------
			
			final String deckName =
			((android.widget.EditText) findViewById(
			R.id.edittext_deck_name
			))
			.getText()
			.toString()
			.trim();
			
			// ------------------------------------------------
			// FIND DECK ID
			// ------------------------------------------------
			
			java.util.Map<Long, String> decks =
			api.getDeckList();
			
			Long deckId = null;
			
			if (decks != null) {
				
				for (java.util.Map.Entry<Long, String> entry :
				decks.entrySet()) {
					
					if (deckName.equals(entry.getValue())) {
						
						deckId = entry.getKey();
						break;
					}
				}
			}
			
			if (deckId == null) {
				
				runOnUiThread(new Runnable() {
					@Override
					public void run() {
						
						android.widget.Toast.makeText(
						getApplicationContext(),
						"Anki deck not found",
						android.widget.Toast.LENGTH_LONG
						).show();
						
					}
				});
				
				return;
			}
			
			// ------------------------------------------------
			// FIND OUR MODEL
			// ------------------------------------------------
			
			String modelName =
			"Anki Sheet Sync ID";
			
			java.util.Map<Long, String> models =
			api.getModelList();
			
			Long modelId = null;
			
			if (models != null) {
				
				for (java.util.Map.Entry<Long, String> entry :
				models.entrySet()) {
					
					if (modelName.equals(entry.getValue())) {
						
						modelId = entry.getKey();
						break;
					}
				}
			}
			
			if (modelId == null) {
				
				runOnUiThread(new Runnable() {
					@Override
					public void run() {
						
						android.widget.Toast.makeText(
						getApplicationContext(),
						"Anki note type not found",
						android.widget.Toast.LENGTH_LONG
						).show();
						
					}
				});
				
				return;
			}
			
			// ------------------------------------------------
			// LOAD ONLY NOTES FROM THE SELECTED DECK
			// ------------------------------------------------
			
			/*
     * Anki search:
     *
     * deck:"banana"
     *
     * This prevents cards in another deck such as
     * "apple" from being treated as duplicates.
     */
			
			String deckSearch =
			"deck:\"" + deckName + "\"";
			
			java.util.HashMap<String, Long> existingIdMap =
			new java.util.HashMap<String, Long>();
			
			java.util.HashSet<String> existingContentSet =
			new java.util.HashSet<String>();
			
			android.database.Cursor noteCursor =
			null;
			
			try {
				
				noteCursor =
				getContentResolver().query(
				com.ichi2.anki.FlashCardsContract.Note.CONTENT_URI,
				new String[] {
					com.ichi2.anki.FlashCardsContract.Note._ID,
					com.ichi2.anki.FlashCardsContract.Note.MID,
					com.ichi2.anki.FlashCardsContract.Note.FLDS
				},
				deckSearch,
				null,
				null
				);
				
				if (noteCursor != null) {
					
					int idColumn =
					noteCursor.getColumnIndex(
					com.ichi2.anki.FlashCardsContract.Note._ID
					);
					
					int modelColumn =
					noteCursor.getColumnIndex(
					com.ichi2.anki.FlashCardsContract.Note.MID
					);
					
					int fieldsColumn =
					noteCursor.getColumnIndex(
					com.ichi2.anki.FlashCardsContract.Note.FLDS
					);
					
					while (noteCursor.moveToNext()) {
						
						if (idColumn < 0 ||
						modelColumn < 0 ||
						fieldsColumn < 0) {
							
							continue;
						}
						
						long noteId =
						noteCursor.getLong(
						idColumn
						);
						
						long noteModelId =
						noteCursor.getLong(
						modelColumn
						);
						
						// Only use our model
						if (noteModelId != modelId) {
							continue;
						}
						
						String fieldsText =
						noteCursor.getString(
						fieldsColumn
						);
						
						if (fieldsText == null) {
							continue;
						}
						
						String[] fields =
						fieldsText.split(
						"\u001f",
						-1
						);
						
						if (fields.length < 3) {
							continue;
						}
						
						String existingId =
						fields[0];
						
						String existingFront =
						fields[1];
						
						String existingBack =
						fields[2];
						
						// Store ID -> note ID
						existingIdMap.put(
						existingId,
						noteId
						);
						
						// Store Front + Back combination
						String contentKey =
						existingFront
						+ "\u0000"
						+ existingBack;
						
						existingContentSet.add(
						contentKey
						);
					}
				}
				
			} finally {
				
				if (noteCursor != null) {
					noteCursor.close();
				}
			}
			
			// ------------------------------------------------
			// PROCESS GOOGLE SHEET ROWS
			// ------------------------------------------------
			
			int added = 0;
			int updated = 0;
			int duplicateSkipped = 0;
			int invalidSkipped = 0;
			
			for (int i = 0; i < frontList.size(); i++) {
				
				// frontList contains:
				// ID + TAB + Front
				
				String stored =
				frontList.get(i);
				
				int tabPosition =
				stored.indexOf("\t");
				
				if (tabPosition < 0) {
					
					invalidSkipped++;
					continue;
				}
				
				String id =
				stored.substring(
				0,
				tabPosition
				).trim();
				
				String front =
				stored.substring(
				tabPosition + 1
				).trim();
				
				String back =
				backList.get(i).trim();
				
				// ------------------------------------------------
				// VALIDATE ROW
				// ------------------------------------------------
				
				if (id.length() == 0 ||
				front.length() == 0 ||
				back.length() == 0) {
					
					invalidSkipped++;
					continue;
				}
				
				// ------------------------------------------------
				// CREATE CONTENT KEY
				// ------------------------------------------------
				
				String contentKey =
				front
				+ "\u0000"
				+ back;
				
				// ------------------------------------------------
				// STEP 1
				// SAME ID IN SELECTED DECK?
				// ------------------------------------------------
				
				Long existingNoteId =
				existingIdMap.get(id);
				
				if (existingNoteId != null) {
					
					/*
             * Same ID already exists in the selected deck.
             *
             * We need to read the current note so we can
             * determine whether the content actually changed.
             */
					
					com.ichi2.anki.api.NoteInfo note =
					api.getNote(existingNoteId);
					
					if (note == null) {
						
						invalidSkipped++;
						
					} else {
						
						String[] existingFields =
						note.getFields();
						
						String existingFront = "";
						String existingBack = "";
						
						if (existingFields != null &&
						existingFields.length >= 3) {
							
							existingFront =
							existingFields[1];
							
							existingBack =
							existingFields[2];
						}
						
						// ------------------------------------------------
						// SAME ID + SAME CONTENT
						// ------------------------------------------------
						
						if (front.equals(existingFront) &&
						back.equals(existingBack)) {
							
							duplicateSkipped++;
							
						} else {
							
							// ------------------------------------------------
							// SAME ID + CHANGED CONTENT
							//
							// But first make sure another note in the
							// selected deck doesn't already contain the
							// new Front + Back.
							// ------------------------------------------------
							
							boolean duplicateContent =
							false;
							
							Long contentOwnerId = null;
							
							/*
                     * Search the currently loaded content map.
                     *
                     * If this content belongs to another note,
                     * don't create/update a duplicate.
                     */
							
							if (existingContentSet.contains(
							contentKey)) {
								
								// Find whether the content belongs
								// to this exact note or another note.
								//
								// We need to scan the selected-deck notes
								// to distinguish the two cases.
								
								android.database.Cursor checkCursor =
								null;
								
								try {
									
									checkCursor =
									getContentResolver().query(
									com.ichi2.anki.FlashCardsContract.Note.CONTENT_URI,
									new String[] {
										com.ichi2.anki.FlashCardsContract.Note._ID,
										com.ichi2.anki.FlashCardsContract.Note.MID,
										com.ichi2.anki.FlashCardsContract.Note.FLDS
									},
									deckSearch,
									null,
									null
									);
									
									if (checkCursor != null) {
										
										int checkIdColumn =
										checkCursor.getColumnIndex(
										com.ichi2.anki.FlashCardsContract.Note._ID
										);
										
										int checkModelColumn =
										checkCursor.getColumnIndex(
										com.ichi2.anki.FlashCardsContract.Note.MID
										);
										
										int checkFieldsColumn =
										checkCursor.getColumnIndex(
										com.ichi2.anki.FlashCardsContract.Note.FLDS
										);
										
										while (
										checkCursor.moveToNext()
										) {
											
											if (checkIdColumn < 0 ||
											checkModelColumn < 0 ||
											checkFieldsColumn < 0) {
												
												continue;
											}
											
											long checkNoteId =
											checkCursor.getLong(
											checkIdColumn
											);
											
											long checkModelId =
											checkCursor.getLong(
											checkModelColumn
											);
											
											if (checkModelId != modelId) {
												continue;
											}
											
											if (checkNoteId ==
											existingNoteId) {
												
												continue;
											}
											
											String checkFieldsText =
											checkCursor.getString(
											checkFieldsColumn
											);
											
											if (checkFieldsText == null) {
												continue;
											}
											
											String[] checkFields =
											checkFieldsText.split(
											"\u001f",
											-1
											);
											
											if (checkFields.length >= 3) {
												
												String checkFront =
												checkFields[1];
												
												String checkBack =
												checkFields[2];
												
												if (front.equals(
												checkFront
												) &&
												back.equals(
												checkBack
												)) {
													
													duplicateContent =
													true;
													
													contentOwnerId =
													checkNoteId;
													
													break;
												}
											}
										}
									}
									
								} finally {
									
									if (checkCursor != null) {
										checkCursor.close();
									}
								}
							}
							
							// ------------------------------------------------
							// CONTENT ALREADY EXISTS IN ANOTHER NOTE
							// ------------------------------------------------
							
							if (duplicateContent) {
								
								duplicateSkipped++;
								
							} else {
								
								// ------------------------------------------------
								// UPDATE EXISTING NOTE
								// ------------------------------------------------
								
								boolean success =
								api.updateNoteFields(
								existingNoteId,
								new String[] {
									id,
									front,
									back
								}
								);
								
								if (success) {
									
									updated++;
									
									// Update our local content tracking
									existingContentSet.remove(
									existingFront
									+ "\u0000"
									+ existingBack
									);
									
									existingContentSet.add(
									contentKey
									);
									
								} else {
									
									invalidSkipped++;
								}
							}
						}
					}
					
				} else {
					
					// ------------------------------------------------
					// STEP 2
					// NO SAME ID IN SELECTED DECK
					//
					// CHECK FRONT + BACK DUPLICATE
					// ------------------------------------------------
					
					if (existingContentSet.contains(
					contentKey)) {
						
						// Same Front + Back already exists
						// in the selected deck.
						
						duplicateSkipped++;
						
					} else {
						
						// ------------------------------------------------
						// STEP 3
						// COMPLETELY NEW CARD
						// ------------------------------------------------
						
						Long newNoteId =
						api.addNote(
						modelId,
						deckId,
						new String[] {
							id,
							front,
							back
						},
						null
						);
						
						if (newNoteId != null) {
							
							added++;
							
							// Add to our local maps so another row
							// in the same Sheet cannot create a
							// duplicate.
							
							existingIdMap.put(
							id,
							newNoteId
							);
							
							existingContentSet.add(
							contentKey
							);
							
						} else {
							
							invalidSkipped++;
						}
					}
				}
				
				// ------------------------------------------------
				// UPDATE PROGRESS
				// ------------------------------------------------
				
				final int currentProgress =
				55 + (int)(
				((i + 1) * 45.0)
				/ frontList.size()
				);
				
				runOnUiThread(new Runnable() {
					@Override
					public void run() {
						
						android.widget.ProgressBar progressBar =
						(android.widget.ProgressBar) findViewById(
						R.id.progressbar_sync
						);
						
						progressBar.setIndeterminate(false);
						progressBar.setMax(100);
						progressBar.setProgress(
						currentProgress
						);
					}
				});
			}
			
			// ------------------------------------------------
			// FINAL RESULT
			// ------------------------------------------------
			
			final int finalAdded =
			added;
			
			final int finalUpdated =
			updated;
			
			final int finalDuplicates =
			duplicateSkipped;
			
			final int finalInvalid =
			invalidSkipped;
			
			runOnUiThread(new Runnable() {
				@Override
				public void run() {
					
					android.widget.Toast.makeText(
					getApplicationContext(),
					"Added = " + finalAdded
					+ "\nUpdated = " + finalUpdated
					+ "\nDuplicates = " + finalDuplicates
					+ "\nInvalid = " + finalInvalid,
					android.widget.Toast.LENGTH_LONG
					).show();
					
				}
			});
			
			_showResult();
			
		} catch (final Exception e) {
			
			runOnUiThread(new Runnable() {
				@Override
				public void run() {
					
					android.widget.Toast.makeText(
					getApplicationContext(),
					"Card sync error: "
					+ e.getMessage(),
					android.widget.Toast.LENGTH_LONG
					).show();
					
				}
			});
		}
	}
	
	
	public void _showResult() {
		runOnUiThread(new Runnable() {
			@Override
			public void run() {
				
				// Get Sync button
				android.widget.Button syncButton =
				(android.widget.Button) findViewById(
				R.id.button_sync
				);
				
				// Get ProgressBar
				android.widget.ProgressBar progressBar =
				(android.widget.ProgressBar) findViewById(
				R.id.progressbar_sync
				);
				
				// Make sure progress reaches 100%
				progressBar.setIndeterminate(false);
				progressBar.setMax(100);
				progressBar.setProgress(100);
				
				// Re-enable Sync button
				syncButton.setEnabled(true);
				
				// Hide ProgressBar after completion
				progressBar.setVisibility(
				android.view.View.GONE
				);
				
			}
		});
	}
	
}