package com.android.nick.lab1;

import android.app.ActionBar;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

public class ReviewNoteActivity extends AppCompatActivity {

    final int neededLayout = R.layout.note_layout;
    Note noteSelected;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(neededLayout);

        Intent thisIntent = getIntent();
        noteSelected = (Note) thisIntent.getParcelableExtra("note");

        Intent intent = new Intent();
        intent.putExtra("result", "note reviewed");
        setResult(RESULT_OK, intent);

        prepareNoteLayout();
    }

    private void prepareNoteLayout() {

        LinearLayout noteBodyLayout = (LinearLayout) findViewById(R.id.note_body_layout);
        LinearLayout noteLayout = noteSelected.visualize(getApplicationContext());

        noteBodyLayout.addView(noteLayout);

        TextView description = new TextView(this);
        description.setText(noteSelected.getDescription());
        description.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);

        ActionBar.LayoutParams params = new ActionBar.LayoutParams(ActionBar.LayoutParams.FILL_PARENT,
                ActionBar.LayoutParams.WRAP_CONTENT);

        description.setLayoutParams(params);
        noteBodyLayout.addView(description);

        TextView additionHeight = new TextView(this);
        additionHeight.setHeight(140);
        noteBodyLayout.addView(additionHeight);

        Button backButton = (Button) findViewById(R.id.back_note_layout);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                noteSelected = null;
                finish();
            }
        });
    }
}
