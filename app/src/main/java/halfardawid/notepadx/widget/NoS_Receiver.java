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
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.SharedPreferences;
import android.support.v4.content.SharedPreferencesCompat;
import android.util.Log;
import android.widget.RemoteViews;

import org.json.JSONException;

import java.io.FileNotFoundException;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import halfardawid.notepadx.R;
import halfardawid.notepadx.util.exceptions.NoSuchNoteTypeException;
import halfardawid.notepadx.util.note.Note;

public class NoS_Receiver extends AppWidgetProvider {

    public static final String WIDGET_PREFS = "halfardawid.notepad.WIDGET_PREFS";
    public static final String KEY="UUID;";

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(WIDGET_PREFS, Context.MODE_PRIVATE);
        Map<String, ?> preferencesMap = sharedPreferences.getAll();
        Set<String> used=new HashSet<>();
        for(int appWidgetId:appWidgetIds) {
            updateWidget(preferencesMap,used , context, appWidgetManager, appWidgetId);
        }
        clearUnused(sharedPreferences,used);
        super.onUpdate(context,appWidgetManager,appWidgetIds);
    }

    private void clearUnused(SharedPreferences sharedPreferences, Set<String> usedList) {
        Set<String> keys = sharedPreferences.getAll().keySet();
        keys.removeAll(usedList);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        for(String key:keys)
            editor.remove(key);
        editor.apply();
    }

    private void updateWidget(Map<String, ?> preferences, Set<String> used,
                              Context context, AppWidgetManager appWidgetManager, int appWidgetId) {
        String key = KEY + appWidgetId;
        String uuid = (String) preferences.get(key);
        Note note;
        try {
            note = Note.loadNote(context, uuid);
        } catch (Exception e) {
            Log.d("UPDATE_WIDGET","loading went wrong",e);
            return;
        }
        getViews(context, appWidgetManager, appWidgetId, note);
        used.add(key);
    }

    public static void getViews(Context context, AppWidgetManager appWidgetManager, int appWidgetId, Note note) {
        RemoteViews views = new RemoteViews(context.getPackageName(),
                R.layout.widget_note_on_screen);
        views.setTextViewText(R.id.wnos_title,note.getTitle());
        appWidgetManager.updateAppWidget(appWidgetId, views);
    }


}
