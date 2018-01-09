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

package halfardawid.notepadx.util.note;

import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.res.ResourcesCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.RemoteViews;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;
import java.util.UUID;

import halfardawid.notepadx.R;
import halfardawid.notepadx.util.ColorUtils;
import halfardawid.notepadx.util.exceptions.NoSuchNoteTypeException;
import halfardawid.notepadx.util.note.types.CheckListNote;
import halfardawid.notepadx.util.note.types.SketchNote;
import halfardawid.notepadx.util.note.types.TextNote;
import halfardawid.notepadx.widget.NoS_Receiver;

/**
 * note to self, i guess...
 *
 * Important stuff, a note class to be processable requires additional static components such as:

 public final static String TYPE="txt"; --------------------,_____________________________________,
                                                            for type recognition in I/O operations

 public final static int NAME_TYPE=R.string.text_note; -----,___________________________________________,
                                                            for automatic listing of possible note types

 public static Intent getNewIntent(Context con) ------------,___________________________________________________________________,
                                                            Simply for opening the editor for the note without any data included

 Also, a constructor with (UUID uuid,String data,String title) is a necessity for loading from file
 Obviously a constructor with () is also required, as a mean of creating blanks

 */

public abstract class Note {

    public static final java.lang.Class types[]={
            TextNote.class,
            SketchNote.class,
            CheckListNote.class
    };
    public static final String CLOSE_ON_RETURN = "close_on_return";
    private String md5;

    public static NoteType[] getPossibleNotes(Context con){
        List<NoteType> list=new ArrayList<>();
        for(Class cl:types){
            try {
                list.add(new NoteType(con,cl));
            } catch (NoSuchFieldException|IllegalAccessException|NoSuchMethodException e) {
                Log.wtf(TAG,"Omg what a noob wrote this code? Urghhh! (possible notes list fuckup)",e);
            }
        }
        return list.toArray(new NoteType[list.size()]);
    }

    public static final String TAG = "NOTE_CLASS";

    private static final String DATA = "d";
    private static final String TITLE = "t";
    private static final String TYPE = "tp";
    private static final String COLOR = "c";
    private static final String ORDER = "o";

    public static final String UUID_EXTRA="UUID";
    //public static final int FLAGS=Base64.NO_PADDING|Base64.NO_WRAP|Base64.NO_CLOSE;

    protected String title="";
    protected String color=null;
    protected long order=0;
    protected UUID uuid;

    abstract protected String getType();
    abstract public Intent getEditIntent(Context con);
    abstract protected String getData() throws JSONException;
    abstract protected void setData(String arg) throws JSONException;
    abstract protected View getMiniatureContent(Context con, ViewGroup parent);

    public long getOrder(){
        return order;
    }


    public final View getMiniature(final Context con, View rec){
        if(rec==null){
            final LayoutInflater layoutInflater=LayoutInflater.from(con);
            rec=layoutInflater.inflate(R.layout.activity_main_tile,null);
        }
        final Note t=this;
        applyColorsMiniature(rec);
        View top_bar=rec.findViewById(R.id.amt_top_bar);
        if(hasTitle()!=(top_bar.getVisibility()==View.VISIBLE))top_bar.setVisibility(hasTitle()?View.VISIBLE:View.GONE);
        ((TextView)rec.findViewById(R.id.amt_title)).setText(getTitle());
        FrameLayout rl=((FrameLayout)rec.findViewById(R.id.amt_content));
        View content=getMiniatureContent(con,rl);
        rl.removeAllViewsInLayout();
        if(content!=null)rl.addView(content);
        return rec;
    }

    private int applyColorsMiniature(View rec) {
        Context context = rec.getContext();
        int cid= ColorUtils.recognizeColorString(context,color);
        ColorUtils.applyColors(rec,R.id.amt_whole,cid,R.array.color_light);
        ColorUtils.applyColors(rec,R.id.amt_top_bar,cid,R.array.color_base);
        ((TextView)rec.findViewById(R.id.amt_title)).setTextColor(
                ResourcesCompat.getColor(rec.getResources(),ColorUtils.calcContrast(
                    ColorUtils.getColorSpecific(context,cid,R.array.color_base)),
                        context.getTheme()));
        return 0;
    }

    public final void saveToFile(Context context) throws IOException,JSONException {
        chkUUID(context);
        File file = getFile(context);
        try(FileWriter fw=new FileWriter(file)) {
            String s=getParsedFileData();
            fw.write(s);
            initializeMD5(s);
        }

        updateWidget(context);
    }

    private void updateWidget(Context context) {
        String found = checkIfHasWidget(context);
        if (found == null) return;
        int id;
        try {
            id = Integer.parseInt(found.replace(NoS_Receiver.KEY, ""));
        }catch(NumberFormatException e){
            Log.d(TAG,"widget update gone wrong",e);
            return;
        }
        Intent intent = new Intent(context, NoS_Receiver.class);
        intent.setAction(AppWidgetManager.ACTION_APPWIDGET_UPDATE);
        int[] ids = new int[]{id};
        intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, ids);
        context.sendBroadcast(intent);
    }

    @Nullable
    private String checkIfHasWidget(Context context) {
        String found=null;
        SharedPreferences sharedPreferences = NoS_Receiver.getSharedPreferences(context);
        Map<String, ?> stringMap = sharedPreferences.getAll();
        Set<String> keySet = stringMap.keySet();
        for(String key:keySet){
            String uuid = getUUID();
            if(stringMap.get(key).equals(uuid)){
                found=key;
                break;
            }
        }
        if(found==null) return null;
        return found;
    }

    @NonNull
    public final String getParsedFileData() throws JSONException{
        JSONObject obj=new JSONObject();
        obj.put(TITLE,getTitle());
        obj.put(TYPE,getType());
        obj.put(DATA,getData());
        //obj.put(ORDER,order);
        if(color!=null)obj.put(COLOR,color);

        //Log.d(TAG,((uuid!=null)?uuid.toString():"unsaved note")+":"+obj.toString());
        return obj.toString();
    }

    @NonNull
    private File getFile(Context context) {
        return new File(NoteList.getFilesDir(context), uuid.toString());
    }

    protected Note(){
        this.uuid=null;
    }

    protected Note(UUID uuid,String data,String title) throws JSONException {
        this.uuid=uuid;
        setData(data);
        setTitle(title);
    }

    public static Note loadNote(Context con, String uuid) throws FileNotFoundException, NoSuchNoteTypeException, JSONException {
        return loadNote(new File(NoteList.getFilesDir(con),uuid));
    }

    //IMPORTANT::BAE>BAY
    public static Note loadNote(File file) throws JSONException, FileNotFoundException,NoSuchNoteTypeException {
        String content=getContent(file);
        //Log.d(TAG,"loading "+file.getName()+", "+object.toString());
        return getNote(file.lastModified(),content,UUID.fromString(file.getName()));
    }

    @NonNull
    public static Note getNote(long modified, String content, UUID uuid) throws JSONException, NoSuchNoteTypeException {
        JSONObject object=new JSONObject(content);
        String title = object.has(TITLE)?object.getString(TITLE):"";
        String data = (object.has(DATA))?object.getString(DATA):null;
        String type = object.getString(TYPE);
        for(NoteType typePair:getPossibleNotes(null)) {
            try {
                if(!typePair.is(type))continue;
                Note n=typePair.build(uuid,data,title);
                if(object.has(COLOR))n.setColorIni(object.getString(COLOR));
                n.order=modified;
                n.initializeMD5(content);
                return n;
            } catch (NoSuchFieldException|IllegalAccessException|NoSuchMethodException|InvocationTargetException |InstantiationException e) {
                Log.wtf(TAG,"Yea, those errors are unacceptable, but still accounted for i guess...",e);
            }
        }
        throw new NoSuchNoteTypeException(type);
    }


    private static String getContent(File arg) throws JSONException, FileNotFoundException{
        try(Scanner sc=new Scanner(arg)){
            return sc.useDelimiter("\\A").next();
        }
    }

    private void chkUUID(Context con) {
        if(uuid==null)newUUID(con);
        if(order==0)order=NoteList.getPossibleLastOrder(con);
    }

    private void newUUID(Context con){
        File files[]=NoteList.getFilesDir(con).listFiles();
        do{
            uuid=UUID.randomUUID();
        }while(alreadyExistFile(files,uuid));
    }

    private boolean alreadyExistFile(File[] files, UUID uuid) {
        String test=uuid.toString();
        for(File f:files){
            if(f.getName().equals(test))return true;
        }
        return false;
    }

    public final void deleteFile(Context context){
        if(uuid==null)return;
        getFile(context).delete();
        updateWidget(context);
    }

    public final String getTitle(){
        return title;
    }

    public final boolean hasTitle(){
        return (title!=null)&&(title.length()!=0);
    }
    public final void setTitle(String arg){
        title=arg;
    }

    public String getUUID() {
        return (uuid!=null)?uuid.toString():null;
    }

    public void setColorIni(String color){
        this.color = color;
    }

    public void setColor(Context c,int id) {
        this.color = ColorUtils.recognizeColorId(c,id);
        //Log.w(TAG,"set color to "+color);
    }

    public boolean saveNeeded(){
        try {
            return !calculateCurrentMD5().equals(this.md5);
        } catch (JSONException|NoSuchAlgorithmException e) {
            Log.wtf(TAG,"Error when checking for reasons to save",e);
            return true;
        }
    }

    public String getColor(Context context) {
        if(null==color){
            color=ColorUtils.pickRandomColor(context);
        }
        return color;
    }

    public final void initializeMD5(String content) {
        try {
            this.md5 = calculateMD5(content);
        } catch (NoSuchAlgorithmException e) {
            Log.wtf(TAG,"Error during MD5 initialization... Whatever...",e);
        }
    }

    public final String calculateMD5(String content) throws NoSuchAlgorithmException{
        MessageDigest digest=MessageDigest.getInstance("MD5");
        byte[] md=digest.digest(content.getBytes());
        StringBuilder result=new StringBuilder();
        for (byte b:md)
            result.append(String.format("%02X",b));
        return result.toString();
    }

    private String calculateCurrentMD5() throws JSONException,NoSuchAlgorithmException {
        return calculateMD5(getParsedFileData());
    }

    public void saveAsNew(Context context) throws IOException, JSONException {
        uuid=null;
        saveToFile(context);
    }

    public abstract RemoteViews getMiniatureWidget(Context context);
}
