package halfardawid.notepadx.activity.main;

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
import android.widget.Toast;

import halfardawid.notepadx.util.note.Note;
import halfardawid.notepadx.util.note.NoteList;
import halfardawid.notepadx.R;
import halfardawid.notepadx.util.note.NoteType;

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
