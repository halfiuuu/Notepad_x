/*
 * Copyright (c) 2018 anno Domini.
 *
 * Code below is a part of
 * https://github.com/halfiuuu/Notepad_x
 * available for use under the
 * GNU Affero General Public License v3.0
 * as stated in
 * https://github.com/halfiuuu/Notepad_x/blob/master/LICENSE
 *
 * Created by Dawid Halfar
 * contact possible via halfardawid@gmail.com
 *
 * I'm not sure what else this thing should state... Whatever.
 */

package halfardawid.notepadx.widget;

import android.appwidget.AppWidgetManager;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.RemoteViews;

import halfardawid.notepadx.R;
import halfardawid.notepadx.util.note.Note;
import halfardawid.notepadx.util.note.NoteAdapter;
import halfardawid.notepadx.util.note.NoteList;

public class NoS_Configure extends AppCompatActivity{

    public static final String UUID = "UUID";
    private int widgetId;

    private NoteList noteList;
    private SharedPreferences preferences;
    private GridView gridView;
    private NoteAdapter noteAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.widget_note_on_screen_configure);
        preferences = PreferenceManager.getDefaultSharedPreferences(this);
        extractId();
        setupNoteList();
        setResult(RESULT_CANCELED);
    }

    private void setupNoteList() {
        noteList =new NoteList(getApplicationContext());
        String tiles_count = preferences.getString(
                getString(R.string.pref_note_tile_per_row_key),
                getString(R.string.pref_note_tile_per_row_default));
        gridView = (GridView) findViewById(R.id.wnosc_list);
        gridView.setNumColumns(Integer.parseInt(tiles_count));
        noteAdapter = new NoteAdapter(this, noteList);
        gridView.setAdapter(noteAdapter);
        gridView.setOnItemClickListener(new CustomListener());
    }

    private void done(Note note) {
        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(this);
        RemoteViews views = getRemoteViews();
        appWidgetManager.updateAppWidget(widgetId, views);
        Intent resultValue = new Intent();
        resultValue.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, widgetId);
        resultValue.putExtra(UUID,note.getUUID());
        SharedPreferences.Editor editor =NoS_Receiver.getSharedPreferences(this).edit();
        editor.putString(NoS_Receiver.KEY+widgetId,note.getUUID());
        editor.apply();
        setResult(RESULT_OK, resultValue);
        NoS_Receiver.broadcastUpdate(this,widgetId);
        finish();
    }

    @NonNull
    private RemoteViews getRemoteViews() {
        return new RemoteViews(getPackageName(),
                    R.layout.widget_note_on_screen);
    }

    private void extractId() {
        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        if (extras != null) {
            widgetId = extras.getInt(
                    AppWidgetManager.EXTRA_APPWIDGET_ID,
                    AppWidgetManager.INVALID_APPWIDGET_ID);
        }
    }

    private class CustomListener implements android.widget.AdapterView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            done(noteAdapter.getNote(position));
        }
    }
}
