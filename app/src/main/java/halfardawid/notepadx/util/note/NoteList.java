package halfardawid.notepadx.util.note;

import android.content.Context;
import android.util.Log;

import org.json.JSONException;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;

import halfardawid.notepadx.util.exceptions.NoSuchNoteTypeException;

/**
 * Created by Dawid on 2017-09-24.
 */

public final class NoteList extends ArrayList<Note> {
    public static final String TAG="NOTE_LIST";
    public NoteList(Context context){
        loadAll(context);
    }
    private void loadAll(Context context){
        for(File f:context.getFilesDir().listFiles())
            try {
                add(Note.loadNote(f));
            }catch(JSONException |FileNotFoundException |NoSuchNoteTypeException e){
                Log.wtf(TAG,"Loading note went terribly wrong on "+f.getName(),e);
            }
    }
    public void reloadAll(Context con){
        this.clear();
        loadAll(con);
    }
    public void delAll(Context con){
        for(Note a:this){
            a.deleteFile(con);
        }
        this.clear();
    }
}
