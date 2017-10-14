package halfardawid.notepadx.util.note.types;

import android.content.Context;
import android.content.Intent;
import android.view.View;

import org.json.JSONException;

import java.util.UUID;

import halfardawid.notepadx.R;
import halfardawid.notepadx.activity.TextNoteActivity;
import halfardawid.notepadx.util.note.Note;

public class SketchNote extends Note {

    public final static String TYPE="sketch";
    public final static int NAME_TYPE= R.string.sketch_note;

    public SketchNote(UUID uuid, String data, String title) throws JSONException {
        super(uuid, data,title);
    }

    public SketchNote(){
        super();
    }

    @Override
    protected String getType() {
        return TYPE;
    }

    @Override
    public Intent getEditIntent(Context con) {
        Intent in=getNewIntent(con);
        if(this.uuid!=null)in.putExtra(Note.UUID_EXTRA,uuid.toString());
        return in;
    }

    public static Intent getNewIntent(Context con){
        return null;
        //return new Intent(con, TextNoteActivity.class);
    }


    @Override
    protected String getData() {
        return null;
    }

    @Override
    protected void setData(String arg) {

    }

    @Override
    protected View getMiniatureContent(Context con) {
        return null;
    }
}
