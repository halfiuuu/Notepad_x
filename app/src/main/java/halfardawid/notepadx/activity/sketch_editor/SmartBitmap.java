package halfardawid.notepadx.activity.sketch_editor;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;

import halfardawid.notepadx.util.vectors.Vector2i;

import static android.R.attr.bitmap;

/**
 * Created by Dawid on 2017-10-26.
 */

class SmartBitmap {
    public static final String TAG="SMART_BITMAP";

    private Bitmap bitmap;
    private Vector2i offset=new Vector2i(100,100);
    private final static int steps=250;

    public SmartBitmap(){
        bitmap=makeClearBitmap(new Vector2i(500,500));
    }

    public synchronized void clone(Bitmap b){
        bitmap=b.copy(Bitmap.Config.ARGB_8888,true);
    }

    public synchronized void drawOnCanvas(Canvas c) {
        c.drawBitmap(bitmap,offset.x,offset.y,null);
        Log.d(TAG,"Drawing the bitmap");
    }

    public synchronized void drawPixel(Vector2i pos, int c) {
        Vector2i np=normalizeVector(pos);
        expandIfNeeded(np);
        np=normalizeVector(pos);
        bitmap.setPixel(np.x, np.y, c);
    }

    private synchronized Vector2i normalizeVector(Vector2i arg0){
        Vector2i var=new Vector2i(arg0);
        var.sub(offset);
        return var;
    }

    @NonNull
    private synchronized void expandIfNeeded(Vector2i pos) {
        Vector2i changes = pos.checkInBounds(new Vector2i(bitmap),steps);
        if(changes.isNone())return;
        Log.d(TAG,"Changes needed for "+pos+" as "+changes);
        Vector2i addSize=new Vector2i(changes);
        addSize.abs();
        addSize.add(new Vector2i(bitmap));
        Log.d(TAG,"sum size "+addSize);
        Vector2i f=new Vector2i(changes);
        f.cutAllPositive();
        f.abs();
        Log.d(TAG,"new offset "+f);
        bitmap = copyOnBitmap(addSize,f);
        Log.d(TAG,"Bitmap expanded!");
        offset.sub(f);
    }


    private synchronized Bitmap copyOnBitmap(Vector2i new_size,Vector2i o) {
        Bitmap b = makeClearBitmap(new_size);
        Log.d(TAG, b.getWidth()+"/"+b.getHeight()+" new bitmap");
        Log.d(TAG, bitmap.getWidth()+"/"+bitmap.getHeight()+" old bitmap");
        for(int x=0;x<bitmap.getWidth();x++)
            for(int y=0;y<bitmap.getHeight();y++) {
                //Log.d(TAG,"copy "+x+"/"+y+" to "+(x+o.x)+"/"+(y+o.y));
                b.setPixel(x + o.x, y + o.y, bitmap.getPixel(x, y));
            }
        Log.d(TAG,"Whole bitmap copied!");
        return b;
    }

    public synchronized Bitmap getData() {
        return bitmap;
    }

    private static Bitmap makeClearBitmap(Vector2i s){
        return Bitmap.createBitmap(s.x,s.y, Bitmap.Config.ARGB_8888);
    }

    public synchronized void clear(){
        bitmap=makeClearBitmap(new Vector2i(bitmap));
    }

    public synchronized void move(Vector2i v) {
        offset.add(v);
    }

    private static final String OFFSET_X="SMARTBITMAP_OFFSET_X";
    private static final String OFFSET_Y="SMARTBITMAP_OFFSET_Y";

    public void loadSettings(Bundle s) {
        offset.x=s.getInt(OFFSET_X,offset.x);
        offset.y=s.getInt(OFFSET_Y,offset.y);
    }

    public void saveSettings(Bundle s) {
        s.putInt(OFFSET_X,offset.x);
        s.putInt(OFFSET_Y,offset.y);
    }
}
