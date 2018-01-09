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

package halfardawid.notepadx.util.note.types;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RemoteViews;
import android.widget.TextView;

import org.json.JSONException;

import java.util.UUID;

import halfardawid.notepadx.R;
import halfardawid.notepadx.activity.text_editor.TextNoteActivity;
import halfardawid.notepadx.util.note.Note;


public final class TextNote extends Note {

    public final static String TYPE="txt";
    @SuppressWarnings("unused")
    public final static int NAME_TYPE=R.string.text_note;
    String data;

    //Those are used, but Android Studio isn't smart enough to see it coming.
    @SuppressWarnings("unused")
    public TextNote(UUID uuid, String data,String title) throws JSONException{
        super(uuid, data,title);
    }

    @Override
    public RemoteViews getMiniatureWidget(Context context) {
        RemoteViews views = new RemoteViews(context.getPackageName(),
                R.layout.content_textnote_widget);
        views.setTextViewText(R.id.ctw_text,getText());
        return views;
    }

    @SuppressWarnings("unused")
    public TextNote(){
        super();
    }

    public void setText(String a){
        data=a;
    }

    public String getText(){
        return data;
    }

    @Override
    public String getType() {
        return TYPE;
    }

    public Intent getEditIntent(Context con) {
        Intent in=getNewIntent(con);
        if(this.uuid!=null)in.putExtra(Note.UUID_EXTRA,uuid.toString());
        return in;
    }

    public static Intent getNewIntent(Context con){
        return new Intent(con, TextNoteActivity.class);
    }

    @Override
    public String getData() {
        return getText();
    }

    @Override
    public void setData(String arg) {
        setText((arg!=null)?arg:"");
    }

    @Override
    public View getMiniatureContent(Context con, ViewGroup parent) {
        final LayoutInflater layoutInflater=LayoutInflater.from(con);
        View v=layoutInflater.inflate(R.layout.content_textnote,parent,false);
        TextView tv=(TextView)v.findViewById(R.id.c_tn_text);
        tv.setText(getText());
        return v;
    }

}
