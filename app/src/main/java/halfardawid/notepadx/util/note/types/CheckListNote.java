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

package halfardawid.notepadx.util.note.types;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

import halfardawid.notepadx.R;
import halfardawid.notepadx.activity.check_list_editor.CheckListActivity;
import halfardawid.notepadx.util.note.Note;

public class CheckListNote extends Note {
    public final static String TYPE="checklist";
    public final static int NAME_TYPE= R.string.list_note;
    public static final String DATAENTRIES = "DATA";

    private List<String> entries;

    public CheckListNote(){
        super();    //Inb4: I know it's redundant
        initializeEntries();
    }

    private void initializeEntries() {
        if(entries==null)
            entries=new LinkedList<>();
    }

    public CheckListNote(UUID uuid, String data, String title) throws JSONException {
        super(uuid,data,title);
        initializeEntries();
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

    @Override
    protected String getData() throws JSONException{
        initializeEntries();
        JSONObject jsonObject=new JSONObject();
        JSONArray jsonArray=new JSONArray();
        for(String entry:entries){
            jsonArray.put(entry);
        }
        jsonObject.put(DATAENTRIES,jsonArray);
        return jsonObject.toString();
    }

    @Override
    protected void setData(String arg) throws JSONException {
        initializeEntries();
        JSONObject jsonObject=new JSONObject(arg);
        JSONArray jsonArray=jsonObject.getJSONArray(DATAENTRIES);
        int length = jsonArray.length();
        entries.clear();
        for(int i = 0; i< length; i++){
            entries.add(jsonArray.getString(i));
        }
    }

    @Override
    protected View getMiniatureContent(Context con) {
        TextView textView = new TextView(con);
        textView.setText(R.string.placeholder);
        return textView;
    }

    public static Intent getNewIntent(Context con){
        return new Intent(con, CheckListActivity.class);
    }

    public int getEntriesCount() {
        return entries.size();
    }

    public String getEntry(int position) {
        return entries.get(position);
    }

    public void addEntry(String s) {
        entries.add(s);
    }

    public void setEntry(int index, String new_value) {
        entries.set(index,new_value);
    }
}
