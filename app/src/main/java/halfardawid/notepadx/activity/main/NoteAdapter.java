package halfardawid.notepadx.activity.main;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.List;

import halfardawid.notepadx.util.note.Note;

public class NoteAdapter extends BaseAdapter{
    public static final String TAG="NoteAdapter";
    private MainActivity con;
    private List<Note> notes;

    public NoteAdapter(MainActivity con, List<Note> notes){
        this.con=con;
        this.notes=notes;
    }

    @Override public int getCount(){
        return notes.size();
    }

    @Override public long getItemId(int pos){
        return 0;
    }

    @Override public Object getItem(int position){
        return null;
    }

    @Override public View getView(int pos, View conv, ViewGroup par){
        return notes.get(pos).getMiniature(con,conv);
    }
}
