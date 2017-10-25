package halfardawid.notepadx.util.note;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.UUID;

import halfardawid.notepadx.R;
import halfardawid.notepadx.activity.main.MainActivity;
import halfardawid.notepadx.util.exceptions.NoSuchNoteTypeException;
import halfardawid.notepadx.util.note.types.SketchNote;
import halfardawid.notepadx.util.note.types.TextNote;

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

    public static final java.lang.Class types[]={TextNote.class, SketchNote.class};

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

    public static final String UUID_EXTRA="UUID";
    //public static final int FLAGS=Base64.NO_PADDING|Base64.NO_WRAP|Base64.NO_CLOSE;

    protected String title="";
    protected String color=null;
    protected UUID uuid;

    abstract protected String getType();
    abstract public Intent getEditIntent(Context con);
    abstract protected String getData();
    abstract protected void setData(String arg);
    abstract protected View getMiniatureContent(Context con);


    public View getMiniature(final MainActivity con, View rec){
        if(rec==null){
            final LayoutInflater layoutInflater=LayoutInflater.from(con);
            rec=layoutInflater.inflate(R.layout.activity_main_tile,null);
        }
        final Note t=this;
        applyColors(rec);
        rec.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                con.open(t);
            }
        });
        View top_bar=rec.findViewById(R.id.amt_top_bar);
        if(hasTitle()!=(top_bar.getVisibility()==View.VISIBLE))top_bar.setVisibility(hasTitle()?View.VISIBLE:View.GONE);
        ((TextView)rec.findViewById(R.id.amt_title)).setText(getTitle());
        View content=getMiniatureContent(con);
        RelativeLayout rl=((RelativeLayout)rec.findViewById(R.id.amt_content));
        rl.removeAllViewsInLayout();
        if(content!=null)rl.addView(content);
        return rec;
    }

    private int applyColors(View rec) {
        int cid= recognizeColorString(rec.getContext(),color);
        applyColors(rec,R.id.amt_whole,cid,R.array.color_light);
        applyColors(rec,R.id.amt_top_bar,cid,R.array.color_base);
        return 0;
    }

    static private void applyColors(View v, @IdRes int id, int color_id, int col_array){
        v.findViewById(id).setBackgroundColor(getColorSpecific(v.getContext(),color_id,col_array));
    }

    static private int getColorSpecific(Context c,int id, int array){
        return c.getResources().getIntArray(array)[id];
    }

    public static int recognizeColorString(Context c, String name) {
        if(name!=null){
            String[] cn=c.getResources().getStringArray(R.array.color_names);
            for(int o=0;o<cn.length;o++)
            {
                if(name.equals(cn[o]))return o;
            }
        }
        return 0;//0 being default note color... I guess...
    }

    public static String recognizeColorId(Context c,int id) {
        return c.getResources().getStringArray(R.array.color_names)[id];
    }

    public final void saveToFile(Context context) throws IOException,JSONException {
        chkUUID(context);
        File file = getFile(context);
        try(FileWriter fw=new FileWriter(file)) {
            fw.write(getParsedFileData());
        }
    }

    @NonNull
    public final String getParsedFileData() throws JSONException{
        JSONObject obj=new JSONObject();
        obj.put(TITLE,getTitle());
        obj.put(TYPE,getType());
        obj.put(DATA,getData());
        if(color!=null)obj.put(COLOR,color);

        Log.d(TAG,((uuid!=null)?uuid.toString():"unsaved note")+":"+obj.toString());
        return obj.toString();
    }

    @NonNull
    private File getFile(Context context) {
        return new File(context.getFilesDir(), uuid.toString());
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
        return loadNote(new File(con.getFilesDir(),uuid));
    }

    //IMPORTANT::BAE>BAY
    public static Note loadNote(File file) throws JSONException, FileNotFoundException,NoSuchNoteTypeException {
        JSONObject object = getContent(file);
        Log.d(TAG,"loading "+file.getName()+", "+object.toString());
        return getNote(object,UUID.fromString(file.getName()));
    }

    @NonNull
    public static Note getNote(JSONObject object,UUID uuid) throws JSONException, NoSuchNoteTypeException {
        String title = object.has(TITLE)?object.getString(TITLE):"";
        String data = (object.has(DATA))?object.getString(DATA):null;
        String type = object.getString(TYPE);
        for(NoteType typePair:getPossibleNotes(null)) {
            try {
                if(!typePair.is(type))continue;
                Note n=typePair.build(uuid,data,title);
                if(object.has(COLOR))n.setColor(object.getString(COLOR));
                return n;
            } catch (NoSuchFieldException|IllegalAccessException|NoSuchMethodException|InvocationTargetException |InstantiationException e) {
                Log.wtf(TAG,"Yea, those errors are unacceptable, but still accounted for i guess...",e);
            }
        }
        throw new NoSuchNoteTypeException(type);
    }


    private static JSONObject getContent(File arg) throws JSONException, FileNotFoundException{
        try(Scanner sc=new Scanner(arg)){
            return new JSONObject(sc.useDelimiter("\\A").next());
        }
    }

    private void chkUUID(Context con) {
        if(uuid==null)newUUID(con);
    }

    private void newUUID(Context con){
        File files[]=con.getFilesDir().listFiles();
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

    public void setColor(String color) {
        this.color = color;
    }
    public void setColor(Context c,int id) {
        this.color = Note.recognizeColorId(c,id);
        Log.w(TAG,"set color to "+color);
    }

    public void applyColors(AppCompatActivity activity) {
        final int cid= recognizeColorString(activity,color);
        Log.d(TAG,"applying colors to activity with color marked with id of ["+cid+"]");
        activity.getWindow().getDecorView().setBackgroundColor(getColorSpecific(activity,cid,R.array.color_light));
        ActionBar bar = activity.getSupportActionBar();
        Log.d(TAG,"action bar..."+ bar);
        bar.setBackgroundDrawable(new ColorDrawable(getColorSpecific(activity,cid,R.array.color_base)));
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
            activity.getWindow().setStatusBarColor(getColorSpecific(activity,cid,R.array.color_dark));

    }
}
