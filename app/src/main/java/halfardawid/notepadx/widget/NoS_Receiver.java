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

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v4.content.res.ResourcesCompat;
import android.view.View;
import android.widget.RemoteViews;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import halfardawid.notepadx.R;
import halfardawid.notepadx.receiver.WidgetEditorOpen;
import halfardawid.notepadx.util.ColorUtils;
import halfardawid.notepadx.util.note.Note;

public class NoS_Receiver extends AppWidgetProvider {

    public static final String WIDGET_PREFS = "halfardawid.notepad.WIDGET_PREFS";
    public static final String KEY="UUID;";

    public static void broadcastUpdate(Context context, int... ids) {
        Intent intent = new Intent(context, NoS_Receiver.class);
        intent.setAction(AppWidgetManager.ACTION_APPWIDGET_UPDATE);
        intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, ids);
        context.sendBroadcast(intent);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        SharedPreferences sharedPreferences = getSharedPreferences(context);
        Map<String, ?> preferencesMap = sharedPreferences.getAll();
        for(int appWidgetId:appWidgetIds) {
            updateWidget(preferencesMap , context, appWidgetManager, appWidgetId);
        }
        super.onUpdate(context,appWidgetManager,appWidgetIds);
    }

    public static SharedPreferences getSharedPreferences(Context context) {
        return context.getSharedPreferences(WIDGET_PREFS, Context.MODE_PRIVATE);
    }

    private void clearKeys(SharedPreferences sharedPreferences, Set<String> unusedIds) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        for(String key:unusedIds)
            editor.remove(key);
        editor.apply();
    }

    @Override
    public void onDeleted(Context context, int[] appWidgetIds) {
        Set<String> unused=new HashSet<>();
        for(int id:appWidgetIds){
            unused.add(KEY+id);
        }
        clearKeys(getSharedPreferences(context),unused);
        super.onDeleted(context, appWidgetIds);
    }

    private void updateWidget(Map<String, ?> preferences,
                              Context context, AppWidgetManager appWidgetManager, int appWidgetId) {
        String key = KEY + appWidgetId;
        String uuid = (String) preferences.get(key);

        //Forces widget to drop all views instead of recycling and causing unwanted,
        //erased data from showing up instead of being erased
        setDeleted(context, appWidgetManager, appWidgetId);

        Note note;
        try {
            note = Note.loadNote(context, uuid);
        } catch (Exception e) {
            return;
        }
        getViews(context, appWidgetManager, appWidgetId, note);
    }

    private void setDeleted(Context context, AppWidgetManager appWidgetManager, int appWidgetId) {
        RemoteViews views = new RemoteViews(context.getPackageName(),
                R.layout.widget_note_on_screen_deleted);
        applyConfigIntent(context, appWidgetId, views);
        appWidgetManager.updateAppWidget(appWidgetId, views);
    }

    private void applyConfigIntent(Context context, int appWidgetId, RemoteViews views) {
        Intent intent=new Intent(context,WidgetEditorOpen.class);
        intent.setAction(WidgetEditorOpen.CONFIGURE);
        intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID,appWidgetId);
        PendingIntent pendingIntent =
                PendingIntent.getBroadcast(context, appWidgetId, intent,PendingIntent.FLAG_UPDATE_CURRENT);
        views.setOnClickPendingIntent(R.id.wnosd_root, pendingIntent);
    }

    public static void getViews(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId, Note note) {
        RemoteViews views = new RemoteViews(context.getPackageName(),
                R.layout.widget_note_on_screen);
        setTitle(note, views);
        addContent(context, note, views);
        applyColors(context, note, views);
        applyIntent(context, note, views,appWidgetId);
        appWidgetManager.updateAppWidget(appWidgetId, views);
    }

    private static void applyIntent(Context context, Note note, RemoteViews views,int appWidgetId) {
        Intent intent=new Intent(context,WidgetEditorOpen.class);
        intent.setAction(WidgetEditorOpen.EDIT_NOTE);
        intent.putExtra(Note.UUID_EXTRA,note.getUUID());
        PendingIntent pendingIntent =
                PendingIntent.getBroadcast(context, appWidgetId, intent,PendingIntent.FLAG_UPDATE_CURRENT);
        views.setOnClickPendingIntent(R.id.wnos_root, pendingIntent);
    }

    private static void setTitle(Note note, RemoteViews views) {
        String title = note.getTitle();
        if(title.equals(""))
            views.setViewVisibility(R.id.wnos_top_bar, View.GONE);
        else {
            views.setTextViewText(R.id.wnos_title, title);
        }
    }

    private static void addContent(Context context, Note note, RemoteViews views) {
        views.addView(R.id.wnos_content,note.getMiniatureWidget(context));
    }

    private static void applyColors(Context context, Note note, RemoteViews views) {
        int cid= ColorUtils.recognizeColorString(context,note.getColor(context));
        views.setInt(R.id.wnos_root, "setBackgroundColor", ColorUtils.getColorSpecific(context,cid,R.array.color_light));
        int colorSpecific = ColorUtils.getColorSpecific(context, cid, R.array.color_base);
        views.setInt(R.id.wnos_top_bar, "setBackgroundColor", colorSpecific);
        views.setTextColor(R.id.wnos_title,
                ResourcesCompat.getColor(
                        context.getResources(),
                        ColorUtils.calcContrast(colorSpecific),
                        context.getTheme()));
    }


}
