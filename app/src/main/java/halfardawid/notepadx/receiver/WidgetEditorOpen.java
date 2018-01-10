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

package halfardawid.notepadx.receiver;

import android.appwidget.AppWidgetManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import halfardawid.notepadx.R;
import halfardawid.notepadx.util.note.Note;
import halfardawid.notepadx.widget.NoS_Configure;

import static android.content.Intent.FLAG_ACTIVITY_CLEAR_TASK;
import static android.content.Intent.FLAG_ACTIVITY_CLEAR_TOP;
import static android.content.Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS;
import static android.content.Intent.FLAG_ACTIVITY_NEW_TASK;
import static android.content.Intent.FLAG_ACTIVITY_NO_HISTORY;

public class WidgetEditorOpen extends BroadcastReceiver {

    public static final int FLAGS =
                    FLAG_ACTIVITY_NO_HISTORY|
                    FLAG_ACTIVITY_NEW_TASK|
                    FLAG_ACTIVITY_CLEAR_TASK|
                    FLAG_ACTIVITY_CLEAR_TOP|
                    FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS;
    public static final String EDIT_NOTE = "edit_note";
    public static final String CONFIGURE = "configure";

    @Override
    public void onReceive(Context context, Intent income) {
        if (income == null) return;
        switch (income.getAction()) {
            case EDIT_NOTE:
                actionEdit(context, income);
                break;
            case CONFIGURE:
                actionConfigure(context, income);
                break;
        }
    }

    private void actionConfigure(Context context, Intent income) {
        Intent intent=new Intent(context,NoS_Configure.class);
        intent.putExtra(
                AppWidgetManager.EXTRA_APPWIDGET_ID,
                income.getIntExtra(
                        AppWidgetManager.EXTRA_APPWIDGET_ID,
                        AppWidgetManager.INVALID_APPWIDGET_ID));
        intent.setFlags(FLAGS);
        context.startActivity(intent);
    }

    private void actionEdit(Context context, Intent income) {
        Note note;
        try {
            note = Note.loadNote(context, income.getStringExtra(Note.UUID_EXTRA));
        } catch (Exception e) {
            Log.wtf("WidgetService", "failed", e);
            Toast.makeText(context, R.string.sorry_error, Toast.LENGTH_SHORT).show();
            return;
        }
        Intent intent = note.getEditIntent(context);
        intent.setFlags(FLAGS);
        context.startActivity(intent);
    }
}
