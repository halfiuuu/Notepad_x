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
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import org.json.JSONException;

import java.io.ByteArrayOutputStream;
import java.util.List;
import java.util.UUID;

import halfardawid.notepadx.R;
import halfardawid.notepadx.activity.sketch_editor.SketchActivity;
import halfardawid.notepadx.util.note.Note;
import halfardawid.notepadx.util.vectors.Vector2i;

public class SketchNote extends Note {

    public final static String TYPE="sketch";
    @SuppressWarnings("unused") //Used in generic calls, but Android Studio keeps whining
    public final static int NAME_TYPE= R.string.sketch_note;

    private Bitmap bitmap;

    //Those are used, but Android Studio isn't smart enough to see it coming.
    @SuppressWarnings("unused")
    public SketchNote(UUID uuid, String data, String title) throws JSONException {
        super(uuid, data,title);
    }

    @SuppressWarnings("unused")
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
        return new Intent(con, SketchActivity.class);
    }


    @Override
    protected String getData() {
        if(bitmap==null)return null;
        ByteArrayOutputStream os=new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG,100,os);
        return Base64.encodeToString(os.toByteArray(),Base64.DEFAULT);
    }


    @Override
    protected void setData(String arg) {
        if(arg==null)return;
        try{
            byte[] b=Base64.decode(arg,Base64.DEFAULT);
            bitmap= BitmapFactory.decodeByteArray(b,0,b.length);
        }catch(Exception e){
            Log.wtf(TAG,"Yea, corrupted data or something?",e);
        }
    }

    @Override
    protected View getMiniatureContent(Context con) {
        Bitmap b=getBitmap();
        if(b==null)return null;
        ImageView v=new ImageView(con);
        v.setImageBitmap(getBitmap());
        return v;
    }

    public void setBitmap(Bitmap bitmap) {
        this.bitmap = Bitmap.createBitmap(bitmap);
    }

    public Bitmap getBitmap() {
        return bitmap!=null?Bitmap.createBitmap(bitmap):null;
    }
}
