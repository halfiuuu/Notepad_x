package halfardawid.notepadx.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.IdRes;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.GridView;

import java.util.UUID;

import halfardawid.notepadx.util.note.Note;
import halfardawid.notepadx.util.note.NoteAdapter;
import halfardawid.notepadx.util.note.NoteList;
import halfardawid.notepadx.R;
import halfardawid.notepadx.util.note.TextNote;

public final class MainActivity extends AppCompatActivity {

    public static final String TAG="MAIN_ACTIVITY";
    public static final int NOTE_EDITOR_RESULT = 6422;
    private NoteList notes;
    private NoteAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        notes=new NoteList(getApplicationContext());
        initGridView(R.id.main_grid);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.mainactivity, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.mam_add:
                return add_new_note();
            case R.id.mam_delete:
                return delete_all_notes();
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

    private boolean add_new_note() {
        startActivityForResult(TextNote.getNewIntent(this),NOTE_EDITOR_RESULT);
        return true;
    }

    private void initGridView(@IdRes int id){
        adapter=new NoteAdapter(this,notes);
        ((GridView)findViewById(id)).setAdapter(adapter);
    }

    public void open(Note t) {
        startActivityForResult(t.getEditIntent(this), NOTE_EDITOR_RESULT);
    }

    @Override
    public void onActivityResult(int c,int r, Intent d){
        switch(c){
            case NOTE_EDITOR_RESULT:
                reload_list();
                break;
        }
    }

    private void reload_list() {
        adapter.notifyDataSetChanged();
    }
}
