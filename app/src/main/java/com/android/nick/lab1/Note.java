package com.android.nick.lab1;

import android.app.ActionBar;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;
import android.provider.MediaStore;
import android.support.annotation.DrawableRes;
import android.support.annotation.IdRes;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.io.IOException;
import java.io.InputStream;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import static android.view.ViewGroup.LayoutParams.MATCH_PARENT;

public class Note implements Parcelable {

    private static Bitmap myBitmap = null;

    private String title;
    private String description;
    private String importance;
    private Date date;
    private String image;
    private int id;

    //priority colors
    private final int holo_green_light = 17170452;
    private final int holo_orange_light = 17170456;
    private final int holo_red_light = 17170454;

    public Note(){
        this.title = "";
    }

    public Note(String title, String description, String importance, Date date, String image) {
        this.title = title;
        this.description = description;
        this.importance = importance;
        this.date = date;
        this.image = image;
    }

    public Note(Note note) {
        this.title = note.getTitle();
        this.description = note.getDescription();
        this.importance = note.getImportance();
        this.date = note.getDate();
        this.image = note.getImage();
    }

    protected Note(Parcel in) {
        title = in.readString();
        description = in.readString();
        importance = in.readString();
        image = in.readString();
        id = in.readInt();
        date = (Date) in.readSerializable();
    }

    public static final Creator<Note> CREATOR = new Creator<Note>() {
        @Override
        public Note createFromParcel(Parcel in) {
            return new Note(in);
        }

        @Override
        public Note[] newArray(int size) {
            return new Note[size];
        }
    };

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImportance() {
        return importance;
    }

    public void setImportance(String importance) {
        this.importance = importance;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(title);
        dest.writeString(description);
        dest.writeString(importance);
        dest.writeString(image);
        dest.writeInt(id);
        dest.writeSerializable(date);
    }

    public void generateId(Context context) {
        LinearLayout noteLayout = new LinearLayout(context);

    }

    public LinearLayout visualize(Context context) {

        //general element layout
        LinearLayout noteLayout = new LinearLayout(context);
        id = noteLayout.generateViewId();
        noteLayout.setId(id);
        noteLayout.setOrientation(LinearLayout.VERTICAL);

        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
        layoutParams.setMargins(30, 30, 30, 30);
        noteLayout.setBackgroundResource(R.drawable.my_custom_background);
        noteLayout.setLayoutParams(layoutParams);

        //title textView
        TextView title = new TextView(context);
        title.setTextSize(18);
        title.setText(this.title);
        LinearLayout.LayoutParams titleLayoutParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        titleLayoutParams.weight = 1;
        title.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        title.setLayoutParams(titleLayoutParams);
        noteLayout.addView(title);

        //horizontal layout for content
        LinearLayout noteContentLayout = new LinearLayout(context);
        noteContentLayout.setOrientation(LinearLayout.HORIZONTAL);
        LinearLayout.LayoutParams noteContentLayoutParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        noteContentLayoutParams.weight = 1;
        noteContentLayout.setLayoutParams(noteContentLayoutParams);

        //image
        ImageView image = new ImageView(context);

        LinearLayout.LayoutParams imageLayoutParams = new LinearLayout.LayoutParams(
                350, 350);
        imageLayoutParams.weight = 1;
        image.setVisibility(View.VISIBLE);

        if (!this.image.equals("")) {
            Uri imageUri = Uri.parse(this.image);

            try {
                //InputStream in = MainActivity.parentContext.getContentResolver().openInputStream(imageUri);



                myBitmap = NoteImage.getResizedBitmap(BitmapFactory.decodeStream(context.getContentResolver().openInputStream(imageUri)),
                350);


               /* myBitmap = NoteImage.getResizedBitmap(MediaStore.Images.Media.getBitmap(context.getContentResolver(), imageUri),
                        350);*/
            } catch (IOException e) {
                e.printStackTrace();
            }

            image.setImageBitmap(myBitmap);
        } else {
            image.setImageResource(R.mipmap.ic_launcher);
        }


        image.setLayoutParams(imageLayoutParams);

        noteContentLayout.addView(image);

        myBitmap = null;
        System.gc();

        //priority
        ImageView priority = new ImageView(context);
        LinearLayout.LayoutParams priorityLayoutParams = new LinearLayout.LayoutParams(
                50, 250);
        priorityLayoutParams.weight = 1;
        priority.setLayoutParams(priorityLayoutParams);
        priority.setMinimumWidth(50);
        priority.setPaddingRelative(5, 45, 5, 5);
        priority.setVisibility(View.VISIBLE);
        switch (this.importance) {
            case "high":
                priority.setImageResource(holo_red_light);
                break;
            case "middle":
                priority.setImageResource(holo_orange_light);
                break;
            case "low":
                priority.setImageResource(holo_green_light);
                break;
        }

        noteContentLayout.addView(priority);

        //date of creation
        TextView date = new TextView(context);
        date.setTextSize(14);

        Date thisDate = this.date;

        Calendar cal = Calendar.getInstance();
        cal.setTime(thisDate);

        String dateAndTime = String.format(Locale.UK,"\n%1$s.%2$s.%3$s\n%4$tT",
                cal.get(Calendar.DAY_OF_MONTH),
                cal.get(Calendar.MONTH) + 1,
                cal.get(Calendar.YEAR),
                thisDate.getTime());
        date.setText(context.getResources().getString(R.string.date_and_time) + dateAndTime);

        LinearLayout.LayoutParams dateLayoutParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.MATCH_PARENT);
        dateLayoutParams.weight = 1;
        date.setPadding(0, 20, 0, 0);
        date.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        date.setLayoutParams(dateLayoutParams);
        noteContentLayout.addView(date);

        //add noteContentLayout to the Note Layout
        noteLayout.addView(noteContentLayout);

        return noteLayout;
    }

    public void editNote(Note editedNote) {
        this.title = editedNote.getTitle();
        this.description = editedNote.getDescription();
        this.importance = editedNote.getImportance();
        this.date = editedNote.getDate();
        this.image = editedNote.getImage();
    }
}
