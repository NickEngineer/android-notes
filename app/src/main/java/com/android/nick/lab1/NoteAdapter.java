package com.android.nick.lab1;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class NoteAdapter extends BaseAdapter {

    Context ctx;
    LayoutInflater lInflater;
    ArrayList<Note> objects;

    //priority colors
    private final int holo_green_light = 17170452;
    private final int holo_orange_light = 17170456;
    private final int holo_red_light = 17170454;


    private static Bitmap myBitmap = null;


    NoteAdapter(Context context, ArrayList<Note> products) {
        ctx = context;
        objects = products;
        lInflater = (LayoutInflater) ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return objects.size();
    }

    @Override
    public Object getItem(int position) {
        return objects.get(position);
    }

    @Override
    public long getItemId(int position) {
        return objects.get(position).getId();
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        //створення відображення, якщо не визначене
        View view = convertView;
        if (view == null) {
            view = lInflater.inflate(R.layout.note, parent, false);
        }

        Note note = (Note) getItem(position);

        //встановлення дати
        ((TextView) view.findViewById(R.id.note_title)).setText(note.getTitle());

        //встановлення зображення
        ImageView noteImage = (ImageView) view.findViewById(R.id.note_image);

        if (!note.getImage().equals("")) {
            Uri imageUri = Uri.parse(note.getImage());

            try {
                myBitmap = NoteImage.getResizedBitmap(MediaStore.Images.Media.getBitmap(view.getContext().getContentResolver(), imageUri),
                        350);
            } catch (IOException e) {
                e.printStackTrace();
            }

            noteImage.setImageBitmap(myBitmap);
        } else {
            noteImage.setImageResource(R.mipmap.ic_launcher);
        }

        //встановлення важливості
        ImageView notePriority = (ImageView) view.findViewById(R.id.note_priority);

        switch (note.getImportance()) {
            case "high":
                notePriority.setImageResource(holo_red_light);
                break;
            case "middle":
                notePriority.setImageResource(holo_orange_light);
                break;
            case "low":
                notePriority.setImageResource(holo_green_light);
                break;
        }


        //встановлення дати
        TextView noteDate = (TextView) view.findViewById(R.id.note_date);

        Date thisDate = note.getDate();

        Calendar cal = Calendar.getInstance();
        cal.setTime(thisDate);

        String dateAndTime = String.format(Locale.UK, "\n%1$s.%2$s.%3$s\n%4$tT",
                cal.get(Calendar.DAY_OF_MONTH),
                cal.get(Calendar.MONTH) + 1,
                cal.get(Calendar.YEAR),
                thisDate.getTime());
        noteDate.setText(ctx.getResources().getString(R.string.date_and_time)
                + dateAndTime);

        return view;
    }
}