package com.android.nick.lab1;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class MainActivity extends AppCompatActivity {

    public Context parentContext;

    final int REQUEST_CODE_REVIEW_NOTE = 2001;
    final int REQUEST_CODE_ADD_NOTE = 2002;
    final int REQUEST_EDIT_NOTE = 2004;
    Context context = this;
    boolean highFilter = true;
    boolean middleFilter = true;
    boolean lowFilter = true;


    //main layout
    int neededLayout = R.layout.activity_main;
    ArrayList<Note> notes = new ArrayList<>();
    NoteAdapter noteAdapter;
    FloatingActionButton buttonAddNote;
    TextView title;
    LinearLayout notesLayout;
    CheckBox highFilterBox;
    CheckBox middleFilterBox;
    CheckBox lowFilterBox;
    Note noteSelected;
    int indexSelectedNote;
    String searchLine = "";
    boolean isSearch = false;
    int editedNoteNumber = -1;

    String description = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(neededLayout);

        parentContext = getApplicationContext();

        noteAdapter = new NoteAdapter(this, notes);

        prepareLayoutActivityMain();

    }

    private void outputNotes() {
        LinearLayout noteLayout = null;

        notesLayout = (LinearLayout) findViewById(R.id.note_linear_layout);

        //ListView notesList = (ListView) findViewById(R.id.notes_list);
        //LinkedList<Integer> numbersOfNotesSelected = new LinkedList<>();

        for (int i = notes.size() - 1; i >= 0; --i) {
            if (searchLine.length() != 0) {
                notes.get(i).getTitle();
                notes.get(i).getDescription();
                if (!(notes.get(i).getTitle().contains(searchLine)
                        || notes.get(i).getDescription().contains(searchLine))) {
                    continue;
                }
            }
            switch (notes.get(i).getImportance()) {
                case "high":
                    if (highFilter) {
                        printNote(noteLayout, i);
                        //numbersOfNotesSelected.add(i);
                    }
                    break;
                case "middle":
                    if (middleFilter) {
                        printNote(noteLayout, i);
                        //numbersOfNotesSelected.add(i);
                    }
                    break;
                case "low":
                    if (lowFilter) {
                        printNote(noteLayout, i);
                        //numbersOfNotesSelected.add(i);
                    }
                    break;
            }
        }

       /* Iterator selectedNotesIterator = numbersOfNotesSelected.iterator();

        // package data
        ArrayList<HashMap<String, Object>> data = new ArrayList<>(
                numbersOfNotesSelected.size());
        HashMap<String, Object> map;
        int selectedNoteId;

        String time;
        Date thisDate;
        Calendar cal;

        //priority colors
        final int holo_green_light = 17170452;
        final int holo_orange_light = 17170456;
        final int holo_red_light = 17170454;

        for (int i = 0; i < numbersOfNotesSelected.size(); i++) {
            selectedNoteId = (int) selectedNotesIterator.next();
            map = new HashMap<>();
            map.put("title", notes.get(selectedNoteId).getTitle());
            map.put("description", notes.get(selectedNoteId).getDescription());

            switch (notes.get(selectedNoteId).getImportance()) {
                case "high":
                    map.put("importance", holo_red_light);
                    break;
                case "middle":
                    map.put("importance", holo_orange_light);
                    break;
                case "low":
                    map.put("importance", holo_green_light);
                    break;
            }


            thisDate = notes.get(selectedNoteId).getDate();

            cal = Calendar.getInstance();
            cal.setTime(thisDate);

            time = String.format("Date&Time\n%1$s.%2$s.%3$s\n%4$tT",
                    cal.get(Calendar.DAY_OF_MONTH),
                    cal.get(Calendar.MONTH + 1),
                    cal.get(Calendar.YEAR),
                    thisDate.getTime());

            map.put("date", time);

            if (!"".equals(notes.get(selectedNoteId).getImage())) {
                map.put("image", Uri.parse(notes.get(selectedNoteId).getImage()));
            } else {
                map.put("image", R.mipmap.ic_launcher);
            }

            data.add(map);
        }



        String[] from = {"title", "importance", "date", "image"};


        int[] to = {R.id.note_title, R.id.note_priority, R.id.note_date, R.id.note_image};


        SimpleAdapter adapter = new SimpleAdapter(this, data, R.layout.note,
                from, to);

        notesList.setAdapter(adapter);
*/
    }

    private void printNote(LinearLayout noteLayout, int i) {
        //використовую створений метод для нотатки
        //noteLayout = notes.get(i).visualize(i, this);

        View addedNote;
        //використовую створений адаптер
        addedNote = noteAdapter.getView(i, null, notesLayout);

        addedNote.setId(i);
        notes.get(i).setId(addedNote.getId());

        addedNote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int id = v.getId();

                int indexInArray = getNoteIndexById(id);
                noteSelected = notes.get(indexInArray);

                Intent intent = new Intent(getApplicationContext(), ReviewNoteActivity.class);
                intent.putExtra("note", noteSelected);

                startActivityForResult(intent, REQUEST_CODE_REVIEW_NOTE);
            }
        });

        addedNote.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {

                int id = v.getId();
                final int indexInArray = getNoteIndexById(id);

                indexSelectedNote = indexInArray;
                editedNoteNumber = indexInArray;

                AlertDialog.Builder builder1 = new AlertDialog.Builder(context);
                builder1.setMessage(R.string.what_to_do);
                builder1.setCancelable(true);

                builder1.setPositiveButton(
                        R.string.edit,
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                noteSelected = notes.get(editedNoteNumber);

                                Intent intent = new Intent(getApplicationContext(), EditNoteActivity.class);
                                intent.putExtra("note", noteSelected);

                                startActivityForResult(intent, REQUEST_EDIT_NOTE);
                            }
                        });

                builder1.setNegativeButton(
                        R.string.delete,
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                deleteNote(indexInArray);
                                neededLayout = R.layout.activity_main;
                                setContentView(neededLayout);
                                prepareLayoutActivityMain();
                            }
                        });

                builder1.setNeutralButton(
                        R.string.cancle,
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });

                AlertDialog alert11 = builder1.create();
                alert11.show();
                return false;
            }


        });

        notesLayout.addView(addedNote);
    }


    //handle save information
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("neededLayout", neededLayout);
        outState.putParcelableArrayList("notes", notes);
        outState.putString("searchLine", searchLine);
        outState.putBoolean("isSearch", isSearch);

        outState.putBoolean("highFilter", highFilter);
        outState.putBoolean("middleFilter", middleFilter);
        outState.putBoolean("lowFilter", lowFilter);

        outState.putParcelable("noteSelected", noteSelected);
        outState.putString("description", description);
        outState.putInt("indexSelectedNote", indexSelectedNote);
        outState.putInt("editedNoteNumber", editedNoteNumber);

    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        neededLayout = savedInstanceState.getInt("neededLayout");
        notes = savedInstanceState.getParcelableArrayList("notes");
        searchLine = savedInstanceState.getString("searchLine");
        isSearch = savedInstanceState.getBoolean("isSearch");

        highFilter = savedInstanceState.getBoolean("highFilter");
        middleFilter = savedInstanceState.getBoolean("middleFilter");
        lowFilter = savedInstanceState.getBoolean("lowFilter");

        noteSelected = savedInstanceState.getParcelable("noteSelected");
        description = savedInstanceState.getString("description");
        indexSelectedNote = savedInstanceState.getInt("indexSelectedNote");
        editedNoteNumber = savedInstanceState.getInt("editedNoteNumber");

        setContentView(neededLayout);

        prepareLayoutActivityMain();

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            //get a result of review
            case REQUEST_CODE_REVIEW_NOTE:
                if (!"note reviewed".equals(data.getStringExtra("result"))) {
                    // to do
                    System.out.println("error REQUEST_CODE_REVIEW_NOTE");
                }
                break;
            //get a result of adding a note
            case REQUEST_CODE_ADD_NOTE:
                Note newNote = (Note) data.getParcelableExtra("newNote");

                if (!"".equals(newNote.getTitle())) {
                    notes.add(newNote);
                } else {
                    // to do
                    System.out.println("error REQUEST_CODE_ADD_NOTE");
                }
                setContentView(neededLayout);
                prepareLayoutActivityMain();
                break;
            //get a result of editing a note
            case REQUEST_EDIT_NOTE:
                Note editedNote = (Note) data.getParcelableExtra("newNote");

                if (!"".equals(editedNote.getTitle())) {
                    notes.get(editedNoteNumber).editNote(editedNote);

                    sortNotes();
                } else {
                    // to do
                    System.out.println("error REQUEST_EDIT_NOTE");
                }

                editedNoteNumber = -1;
                setContentView(neededLayout);
                prepareLayoutActivityMain();
                break;
            default:
                break;
        }

    }

    private void sortNotes() {
        Collections.sort(notes, new Comparator<Note>() {
            @Override
            public int compare(Note o1, Note o2) {
                return (o1.getDate().compareTo(o2.getDate()));
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Runtime.getRuntime().gc();
    }

    private void prepareLayoutActivityMain() {
        buttonAddNote = (FloatingActionButton) findViewById(R.id.addNote);
        FloatingActionButton buttonSearchNote = (FloatingActionButton) findViewById(R.id.searchButton);

        buttonAddNote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ConstraintLayout main = (ConstraintLayout) findViewById(R.id.main);
                main.removeAllViews();


                Intent intent = new Intent(getApplicationContext(), AddNoteActivity.class);
                intent.putExtra("test", "test");

                startActivityForResult(intent, REQUEST_CODE_ADD_NOTE);
            }
        });

        final EditText search = (EditText) findViewById(R.id.editSearch);

        buttonSearchNote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //adjust search button
                if (search.getVisibility() == View.INVISIBLE) {
                    isSearch = true;
                    search.setVisibility(View.VISIBLE);
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.showSoftInput(search, InputMethodManager.SHOW_IMPLICIT);
                } else {
                    isSearch = false;
                    search.setVisibility(View.INVISIBLE);
                }


            }
        });

        search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                searchLine = s.toString();
            }
        });

        search.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                notesLayout = (LinearLayout) findViewById(R.id.note_linear_layout);
                notesLayout.removeAllViews();
                outputNotes();
                return false;
            }
        });

        if (isSearch) {
            search.setVisibility(View.VISIBLE);
            search.setText(searchLine);
        }


        highFilterBox = (CheckBox) findViewById(R.id.high_filter);
        middleFilterBox = (CheckBox) findViewById(R.id.middle_filter);
        lowFilterBox = (CheckBox) findViewById(R.id.low_filter);

        highFilterBox.setChecked(highFilter);
        middleFilterBox.setChecked(middleFilter);
        lowFilterBox.setChecked(lowFilter);

        highFilterBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                highFilter = isChecked;
                setContentView(neededLayout);
                prepareLayoutActivityMain();
            }
        });

        middleFilterBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                middleFilter = isChecked;
                setContentView(neededLayout);
                prepareLayoutActivityMain();
            }
        });

        lowFilterBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                lowFilter = isChecked;
                setContentView(neededLayout);
                prepareLayoutActivityMain();
            }
        });


        noteAdapter = new NoteAdapter(this, notes);

        outputNotes();
    }

    public int getNoteIndexById(int id) {
        int noteIndex = -1;
        for (int i = 0; i < notes.size(); ++i) {
            if (notes.get(i).getId() == id) {
                noteIndex = i;
            }
        }
        return noteIndex;
    }

    private void deleteNote(int index) {
        notes.remove(index);
    }
}
