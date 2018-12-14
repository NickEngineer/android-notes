package com.android.nick.lab1;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import java.io.IOException;
import java.util.Date;
import java.util.List;


import static com.android.nick.lab1.NoteImage.getResizedBitmap;

public class AddNoteActivity extends AppCompatActivity {

    final int neededLayout = R.layout.add_new_note;
    final int REQUEST_CODE_EXTERNAL_IMAGE = 2000;

    Note newNote;

    Uri selectedImageUri;
    ImageView noteSelectedImage;
    static Bitmap noteSelectedImageBitmap;

    Button addNoteActionButton;
    Button addImageActionButton;

    EditText note_name;

    RadioGroup priorityGroup;

    int checkedPriorityId = R.id.middlePr;
    String titleOfNewNote = "";
    EditText descriptionBody;
    String description = "";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(neededLayout);

        newNote = new Note();

        Intent intentToMainActivity = new Intent();
        intentToMainActivity.putExtra("newNote", newNote);
        setResult(RESULT_OK, intentToMainActivity);

        prepareLayoutAddNewNote();
    }

    //handle save information
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putParcelable("noteSelectedImageBitmap", noteSelectedImageBitmap);
        outState.putInt("checkedPriorityId", checkedPriorityId);
        outState.putString("titleOfNewNote", titleOfNewNote);

        outState.putString("description", description);

        outState.putParcelable("selectedImageUri", selectedImageUri);

    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        noteSelectedImageBitmap = savedInstanceState.getParcelable("noteSelectedImageBitmap");
        checkedPriorityId = savedInstanceState.getInt("checkedPriorityId");
        titleOfNewNote = savedInstanceState.getString("titleOfNewNote");

        description = savedInstanceState.getString("description");

        selectedImageUri = savedInstanceState.getParcelable("selectedImageUri");

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            // get image from external storage
            case REQUEST_CODE_EXTERNAL_IMAGE:
                if (resultCode == Activity.RESULT_OK) {

                    Uri imageUri = data.getData();
                    selectedImageUri = imageUri;
                    try {
                        this.grantUriPermission("com.android.nick.lab1", imageUri, Intent.FLAG_GRANT_READ_URI_PERMISSION);

                        noteSelectedImageBitmap = NoteImage.getResizedBitmap(MediaStore.Images.Media.getBitmap(getApplicationContext().getContentResolver(), imageUri),
                                350);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    noteSelectedImage = (ImageView) findViewById(R.id.noteSelectedImage);
                    noteSelectedImage.setImageBitmap(noteSelectedImageBitmap);

                    System.gc();

                }
                break;
            default:
                break;
        }
    }

    private void prepareLayoutAddNewNote() {

        priorityGroup = (RadioGroup) findViewById(R.id.priority);
        addNoteActionButton = (Button) findViewById(R.id.addNoteAction);
        addImageActionButton = (Button) findViewById(R.id.addImageAction);

        descriptionBody = (EditText) findViewById(R.id.description_body);

        descriptionBody.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                description = String.valueOf(descriptionBody.getText());
            }
        });

        note_name = (EditText) findViewById(R.id.note_name);

        note_name.addTextChangedListener(new TextWatcher() {

            public void afterTextChanged(Editable s) {
                titleOfNewNote = String.valueOf(note_name.getText());
            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }
        });

        //add new note handler
        addNoteActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addNoteHandler();
            }
        });

        //select picture from external storage
        addImageActionButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                // choose picture from gallery
                Intent intent = new Intent(
                        Intent.ACTION_PICK,
                        android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

                startActivityForResult(intent,
                        REQUEST_CODE_EXTERNAL_IMAGE);
            }
        });


        priorityGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {

                // find which radio button is selected
                switch (checkedId) {
                    case R.id.highPr:
                        checkedPriorityId = R.id.highPr;
                        break;
                    case R.id.middlePr:
                        checkedPriorityId = R.id.middlePr;
                        break;
                    case R.id.lowPr:
                        checkedPriorityId = R.id.lowPr;
                        break;
                }
            }
        });

        // handle changing display orientation
        if (noteSelectedImageBitmap != null) {
            noteSelectedImage = (ImageView) findViewById(R.id.noteSelectedImage);
            noteSelectedImage.setImageBitmap(noteSelectedImageBitmap);
        }

        ((RadioButton) findViewById(checkedPriorityId)).setChecked(true);

        if (!"".equals(titleOfNewNote)) {
            ((EditText) findViewById(R.id.note_name)).setText(titleOfNewNote);
        }

        if (!"".equals(description)) {
            ((EditText) findViewById(R.id.description_body)).setText(description);
        }

    }

    private void addNoteHandler() {
        String noteName = note_name.getText().toString();
        String importance = "middle";
        String imageUri = "";

        descriptionBody = (EditText) findViewById(R.id.description_body);
        String desc = String.valueOf(descriptionBody.getText());


        noteSelectedImage = ((ImageView) findViewById(R.id.noteSelectedImage));


        if (selectedImageUri != null) {
            imageUri = selectedImageUri.toString();
        }


        //get the selected priority
        int radioButtonID = priorityGroup.getCheckedRadioButtonId();
        View radioButton = priorityGroup.findViewById(radioButtonID);
        int idx = priorityGroup.indexOfChild(radioButton);
        RadioButton r = (RadioButton) priorityGroup.getChildAt(idx);
        String selectedtext = r.getText().toString();

        switch (idx) {
            case 0:
                importance = "high";
                break;
            case 1:
                importance = "middle";
                break;
            case 2:
                importance = "low";
                break;
        }

        titleOfNewNote = "";
        description = "";
        selectedImageUri = null;
        if (noteSelectedImageBitmap != null) {
            noteSelectedImageBitmap.recycle();
            noteSelectedImageBitmap = null;
        }

        System.gc();

        Note newNote = new Note(noteName, desc, importance, new Date(), imageUri);

        Intent intentToMainActivity = new Intent();
        intentToMainActivity.putExtra("newNote", newNote);
        setResult(RESULT_OK, intentToMainActivity);

        ConstraintLayout addNoteLayout = (ConstraintLayout) findViewById(R.id.add_note);
        addNoteLayout.clearAnimation();
        addNoteLayout.removeAllViews();

        finish();
    }
}
