package com.android.nick.lab1;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;

import java.io.IOException;

public class EditNoteActivity extends AddNoteActivity {

    private Note noteEdited;

    ImageView noteSelectedImage;
    EditText note_name;
    EditText descriptionBody;

    Button addNoteActionButton;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        prepareEditNote();
    }

    private void prepareEditNote() {

        Intent thisIntent = getIntent();
        noteEdited = (Note) thisIntent.getParcelableExtra("note");

        note_name = findViewById(R.id.note_name);
        descriptionBody = findViewById(R.id.description_body);

        note_name.setText(noteEdited.getTitle());
        descriptionBody.setText(noteEdited.getDescription());

        if (!"".equals(noteEdited.getImage())) {
            Uri imageUri = Uri.parse(noteEdited.getImage());
            try {
                noteSelectedImageBitmap = NoteImage.getResizedBitmap(MediaStore.Images.Media.getBitmap(getApplicationContext().getContentResolver(), imageUri),
                        350);
            } catch (IOException e) {
                e.printStackTrace();
            }

            noteSelectedImage = (ImageView) findViewById(R.id.noteSelectedImage);
            noteSelectedImage.setImageBitmap(noteSelectedImageBitmap);
        }

        noteSelectedImageBitmap = null;

        switch (noteEdited.getImportance()) {
            case "high":
                ((RadioButton) findViewById(R.id.highPr)).setChecked(true);
                break;
            case "middle":
                ((RadioButton) findViewById(R.id.middlePr)).setChecked(true);
                break;
            case "low":
                ((RadioButton) findViewById(R.id.lowPr)).setChecked(true);
                break;
        }

        addNoteActionButton = findViewById(R.id.addNoteAction);

        addNoteActionButton.setText(R.string.edit_note);

        if (!"".equals(noteEdited.getImage())) {
            selectedImageUri = Uri.parse(noteEdited.getImage());
        }


    }


}
