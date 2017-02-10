package com.example.praveenkn.ilovezappos.custom;

import android.content.Context;
import android.database.Cursor;
import android.support.v4.widget.SimpleCursorAdapter;
import android.widget.AdapterView;

/**
 * Created by Praveen Kn on 02-01-2017.
 */

public class CustomSimpleCursorAdapter extends SimpleCursorAdapter {
    public CustomSimpleCursorAdapter(Context context, int layout, Cursor c, String[] from, int[] to, int flags) {
        super(context, layout, c, from, to, flags);
    }

    @Override
    public CharSequence convertToString(Cursor cursor) {
        int indexColumnSuggestion = cursor.getColumnIndex(CustomDatabase.SUGGESTION_VAL);
        return cursor.getString(indexColumnSuggestion);
    }

}
