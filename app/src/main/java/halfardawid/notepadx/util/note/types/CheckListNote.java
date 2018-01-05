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
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.LinkedList;
import java.util.List;
import java.util.UUID;
import java.util.zip.Inflater;

import halfardawid.notepadx.R;
import halfardawid.notepadx.activity.check_list_editor.CheckListActivity;
import halfardawid.notepadx.util.note.Note;

public class CheckListNote extends Note {
    public final static String TYPE="checklist";
    public final static int NAME_TYPE= R.string.list_note;
    public static final String DATAENTRIES = "DATA";
    public static final String KEY_CHECK = "checked";
    public static final String KEY_TEXT = "text";

    private List<CheckListEntry> entries;

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
        for(CheckListEntry entry:entries){
            JSONObject entryObject=new JSONObject();
            entryObject.put(KEY_CHECK,entry.checked);
            entryObject.put(KEY_TEXT,entry.text);
            jsonArray.put(entryObject);
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
            JSONObject entryObject=jsonArray.getJSONObject(i);
            entries.add(
                    new CheckListEntry(
                            entryObject.getString(KEY_TEXT),
                            entryObject.getBoolean(KEY_CHECK)));
        }
    }

    @Override
    protected View getMiniatureContent(Context con,ViewGroup parent) {
        LayoutInflater from = LayoutInflater.from(con);
        View v=from.inflate(R.layout.content_checklistnote,parent,false);
        ViewGroup vg=(ViewGroup) v.findViewById(R.id.ccln_layout);
        for (CheckListEntry entry:entries) {
//            Log.d(TAG, String.valueOf(entry));
//            TextView child = new TextView(con);
//            child.setText(entry.text);
//            vg.addView(child);
            View detail=from.inflate(R.layout.content_checklistnote_detail,vg,false);
            ((CheckBox) detail.findViewById(R.id.cclnd_check_box)).setChecked(entry.checked);
            ((TextView) detail.findViewById(R.id.cclnd_text_view)).setText(entry.text);
            Log.d(TAG, entry+"\n"+entry.text+"\n"+entry.checked+"\n"+((TextView) detail.findViewById(R.id.cclnd_text_view)).getText());
            vg.addView(detail);
        }
        return v;
    }

    public static Intent getNewIntent(Context con){
        return new Intent(con, CheckListActivity.class);
    }

    public int getEntriesCount() {
        return entries.size();
    }

    public CheckListEntry getEntry(int position) {
        return entries.get(position);
    }

    public void addEntry(CheckListEntry s) {
        entries.add(s);
    }

    public void setEntry(int index, CheckListEntry new_value) {
        entries.set(index,new_value);
    }

    public void removeEntry(int index) {
        entries.remove(index);
    }

    public boolean hasEntry(int index) {
        return index<entries.size();
    }

    public static class CheckListEntry {
        public String text;
        public boolean checked;

        public CheckListEntry(String text, boolean checked) {
            this.text = text;
            this.checked = checked;
        }

        public CheckListEntry() {
            this.text="";
            this.checked=false;
        }

        @Override
        public String toString() {
            return "["+checked+"]("+text+")";
        }
    }
}
