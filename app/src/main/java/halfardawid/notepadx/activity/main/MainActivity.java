/*
 * Copyright (c) 2017 anno Domini.
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

package halfardawid.notepadx.activity.main;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.PopupMenu;
import android.widget.Toast;

import org.json.JSONException;

import java.io.IOException;
import java.util.prefs.PreferenceChangeEvent;
import java.util.prefs.PreferenceChangeListener;

import halfardawid.notepadx.activity.general_settings.SettingsActivity;
import halfardawid.notepadx.activity.generic.colorpicker.ColorPickerActivity;
import halfardawid.notepadx.util.note.Note;
import halfardawid.notepadx.util.note.NoteList;
import halfardawid.notepadx.R;
import halfardawid.notepadx.util.note.NoteType;

public final class MainActivity
        extends AppCompatActivity
        implements PopupMenu.OnMenuItemClickListener {

    public static final String TAG="MAIN_ACTIVITY";
    public static final int NOTE_EDITOR_RESULT = 6422;
    private NoteList notes;
    private NoteAdapter adapter;
    SharedPreferences sharedPref;
    private GridView gv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        sharedPref=PreferenceManager.getDefaultSharedPreferences(this);
        notes=new NoteList(getApplicationContext());
        initGridView(R.id.main_grid);
        applySettings();
    }

    private void applySettings() {
        findViewById(R.id.am_floating_add).setVisibility(
                sharedPref.getBoolean(getString(
                        R.string.pref_note_list_floating_add_button),true)?
                        View.VISIBLE:View.INVISIBLE);
        String tiles_count = sharedPref.getString(
                getString(R.string.pref_note_tile_per_row_key),
                getString(R.string.pref_note_tile_per_row_default));
        gv.setNumColumns(Integer.parseInt(tiles_count));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.mainactivity_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.mam_add:
                return add_new_note();
            case R.id.mam_delete:
                return delete_all_notes();
            case R.id.mam_settings:
                openSettings();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private boolean delete_all_notes() {
        AlertDialog.Builder b=new AlertDialog.Builder(this);
        b.setTitle(R.string.confirm);
        b.setMessage(R.string.are_you_sure_delete_all);
        b.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                notes.delAll(getApplicationContext());
                reload_list();
                dialog.dismiss();
            }
        });
        b.setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        b.create().show();
        return true;
    }

    public void onAddNewNoteButtonPressed(View v){
        add_new_note();
    }

    private boolean add_new_note() {
        try {
            final NoteType[] ntp = Note.getPossibleNotes(this);
            final MainActivity t=this;
            String[] strings = new String[ntp.length];
            for (int a = 0; a < ntp.length; a++) {
                strings[a] = ntp[a].get_name_type(this);
            }
            new AlertDialog.Builder(this)
                    .setTitle(R.string.choose_note_type)
                    .setItems(strings, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            try {
                                Intent i=ntp[which].get_editor_intent(getApplicationContext());
                                if(i==null) Toast.makeText(t, R.string.no_editor_what, Toast.LENGTH_SHORT).show();
                                else t.startActivityForResult(i,NOTE_EDITOR_RESULT);
                            } catch (Exception e) {
                                t.notify(e);
                            }
                        }
                    }).create().show();


            //startActivityForResult(TextNote.getNewIntent(this),NOTE_EDITOR_RESULT);
        }catch(Exception e){
            notify(e);
        }finally{
            return true;
        }
    }

    private void notify(Exception e) {
        Log.wtf(TAG,"build failure, or something...",e);
        Toast.makeText(this, R.string.sorry_error, Toast.LENGTH_LONG).show();
    }

    Note context_choice=null;
    private void initGridView(@IdRes int id){
        adapter=new NoteAdapter(this,notes);
        gv = (GridView) findViewById(id);
        gv.setAdapter(adapter);
        registerForContextMenu(gv);
        final MainActivity context=this;
        gv.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener(){
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View v,
                                    int position, long id) {
                context_choice=adapter.getNote(position);
                PopupMenu popup = new PopupMenu(context, v);
                popup.setOnMenuItemClickListener(context);
                MenuInflater inflater = popup.getMenuInflater();
                inflater.inflate(R.menu.context_note_tile_menu, popup.getMenu());
                popup.show();
                return true;
            }
        });
        gv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v,
                                    int position, long id) {
                open(adapter.getNote(position));
            }
        });
    }

    public void open(Note t) {
        startActivityForResult(t.getEditIntent(this), NOTE_EDITOR_RESULT);
    }

    @Override
    public void onActivityResult(int c,int r, Intent d){
        Log.d(TAG,"Activity ended, "+c+" "+r+" "+d);
        switch(c){
            case SettingsActivity.INTENT_CODE:
                applySettings();
                break;
            case NOTE_EDITOR_RESULT:
                reload_list();
                break;
            case ColorPickerActivity.CODE:
                handleColorPick(d);
                break;
        }
    }

    private void handleColorPick(Intent d) {
        if(d.hasExtra(ColorPickerActivity.COLOR_ID)) {
            context_choice.setColor(this,d.getIntExtra(ColorPickerActivity.COLOR_ID, 0));
            saveWithToast(context_choice);
        }
    }

    private void saveWithToast(Note note) {
        try {
            note.saveToFile(this);
        } catch (Exception e) {
            Toast.makeText(this,e.getMessage(),Toast.LENGTH_LONG).show();
        }
        reload_list();
    }

    private void reload_list() {
        adapter.reloadWhole(this);
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.cnt_delete:
                context_choice.deleteFile(this);
                reload_list();
                return true;
            case R.id.cnt_edit:
                open(context_choice);
                return true;
            case R.id.cnt_change_color:
                startActivityForResult(new Intent(this,ColorPickerActivity.class),ColorPickerActivity.CODE);
                return true;
            case R.id.cnt_change_title:
                changeTitleDialog();
                return true;
            case R.id.cnt_duplicate:
                duplicateNote();
            default:
                return false;
        }
    }

    private void duplicateNote() {
        Note note=context_choice;
        try {
            note.saveAsNew(this);
        } catch (Exception e) {
            Log.wtf(TAG,"Duplicating went terribly bad",e);
            Toast.makeText(this, R.string.sorry_error, Toast.LENGTH_SHORT).show();
        }
        reload_list();
    }

    protected void changeTitleDialog(){
        android.app.AlertDialog.Builder b = new android.app.AlertDialog.Builder(this);
        b.setMessage(R.string.input_new_title);
        //b.setTitle(R.string.change_title); Redundant...
        View v=getLayoutInflater().inflate(R.layout.activity_title_change,null);
        final EditText et=((EditText)v.findViewById(R.id.atch_title));
        final Note note=context_choice;
        et.setText(note.getTitle());
        b.setView(v);
        final Context context=this;
        b.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                note.setTitle(et.getText().toString());
                saveWithToast(note);
                dialog.dismiss();
            }
        });
        b.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.cancel();
            }
        });
        b.show();
    }

    private void openSettings(){
        startActivityForResult(new Intent(this,SettingsActivity.class),SettingsActivity.INTENT_CODE);
    }

}
