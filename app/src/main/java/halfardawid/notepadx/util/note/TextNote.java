package halfardawid.notepadx.util.note;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import org.json.JSONException;

import java.io.FileNotFoundException;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.util.UUID;
import java.util.zip.Inflater;

import halfardawid.notepadx.R;
import halfardawid.notepadx.activity.TextNoteActivity;
import halfardawid.notepadx.util.exceptions.NoSuchNoteTypeException;

public final class TextNote extends Note {

    public final static String TYPE="txt";
    String data;

    public TextNote(UUID uuid, String data,String title) throws JSONException{
        super(uuid, data,title);
    }

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
        setText(arg);
    }

    @Override
    public View getMiniatureContent(Context con) {
        final LayoutInflater layoutInflater=LayoutInflater.from(con);
        View v=layoutInflater.inflate(R.layout.content_textnote,null);
        TextView tv=(TextView)v.findViewById(R.id.c_tn_text);
        tv.setText(getText());
        return v;
    }

}
