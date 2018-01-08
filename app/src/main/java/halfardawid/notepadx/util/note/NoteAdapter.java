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

package halfardawid.notepadx.util.note;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.List;

import halfardawid.notepadx.activity.main.MainActivity;
import halfardawid.notepadx.util.note.Note;
import halfardawid.notepadx.util.note.NoteList;

public class NoteAdapter extends BaseAdapter{
    public static final String TAG="NoteAdapter";
    private Context con;
    private NoteList notes;

    public NoteAdapter(Context con, NoteList notes){
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

    public void reloadWhole(Context con) {
        notes.reloadAll(con);
        notifyDataSetChanged();
    }

    public Note getNote(int position) {
        return notes.get(position);
    }
}
