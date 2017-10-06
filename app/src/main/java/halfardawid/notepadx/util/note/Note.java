package halfardawid.notepadx.util.note;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.util.Base64;
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
import java.util.Scanner;
import java.util.UUID;

import halfardawid.notepadx.R;
import halfardawid.notepadx.activity.MainActivity;
import halfardawid.notepadx.util.exceptions.NoSuchNoteTypeException;

public abstract class Note {

    public static final String TAG = "NOTE_CLASS";

    public static final String DATA = "d";
    public static final String TITLE = "t";
    public static final String TYPE = "tp";

    public static final String UUID_EXTRA="UUID";
    public static final int FLAGS=Base64.NO_PADDING|Base64.NO_WRAP|Base64.NO_CLOSE;

    protected String title="";
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

    public final void saveToFile(Context context) throws IOException,JSONException {
        chkUUID(context);
        File file = getFile(context);
        try(FileWriter fw=new FileWriter(file)) {
            fw.write(getParsedFileData());
        }
    }

    @NonNull
    private final String getParsedFileData() throws JSONException{
        JSONObject obj=new JSONObject();
        obj.put(TITLE,getTitle());
        obj.put(TYPE,getType());
        obj.put(DATA,getData());
        Log.d(TAG,uuid.toString()+":"+obj.toString());
        return obj.toString();
    }

    @NonNull
    private final File getFile(Context context) {
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

    public final static Note loadNote(Context con, String uuid) throws FileNotFoundException, NoSuchNoteTypeException, JSONException {
        return loadNote(new File(con.getFilesDir(),uuid));
    }

    //IMPORTANT::BAE>BAY
    public final static Note loadNote(File file) throws JSONException, FileNotFoundException,NoSuchNoteTypeException {
        JSONObject object = getContent(file);
        Log.d(TAG,"loading "+file.getName()+", "+object.toString());
        String title = object.has(TITLE)?object.getString(TITLE):"";
        String data = object.getString(DATA);
        String type = object.getString(TYPE);
        UUID uuid=UUID.fromString(file.getName());
        switch(type){
            case TextNote.TYPE:
                return new TextNote(uuid,data,title);
        }
        throw new NoSuchNoteTypeException(type);
    }


    private final static JSONObject getContent(File arg) throws JSONException, FileNotFoundException{
        try(Scanner sc=new Scanner(arg)){
            return new JSONObject(sc.useDelimiter("\\A").next());
        }
    }

    private final void chkUUID(Context con) {
        if(uuid==null)newUUID(con);
    }

    private final void newUUID(Context con){
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

}
