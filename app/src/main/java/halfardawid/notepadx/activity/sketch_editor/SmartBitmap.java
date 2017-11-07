package halfardawid.notepadx.activity.sketch_editor;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;

import halfardawid.notepadx.util.vectors.Vector2i;

import static android.R.attr.bitmap;
import static android.R.attr.offset;

/**
 * Created by Dawid on 2017-10-26.
 */

public class SmartBitmap {
    public static final String TAG="SMART_BITMAP";

    private Bitmap bitmap;
    private Vector2i offset=new Vector2i(100,100);
    private final static int steps=250;

    private float scale=1f;
    private float scale_min=.1f,scale_max=3f;

    public SmartBitmap(){
        bitmap=makeClearBitmap(new Vector2i(500,500));
    }

    public void clone(Bitmap b){
        bitmap=b.copy(Bitmap.Config.ARGB_8888,true);
    }

    public void drawOnCanvas(Canvas c) {
        /*Bitmap bitmap;
        float scale;
        synchronized (this){
            bitmap=this.bitmap;
            scale=this.scale;
        }*/
        drawOutlined(
                c,
                (scale==1)?
                        bitmap:
                        Bitmap.createScaledBitmap(bitmap,(int)(bitmap.getWidth()*scale),(int)(bitmap.getHeight()*scale),true)
        );

        //Log.d(TAG,"Drawing the bitmap");
    }

    private void drawOutlined(Canvas c,Bitmap img) {
        int x0=offset.x,x1=x0+img.getWidth(),y0=offset.y,y1=y0+img.getHeight();
        emptyRect(c, x0, x1, y0, y1,new Paint(Color.RED));
        c.drawBitmap(img,offset.x,offset.y,null);
    }

    private void emptyRect(Canvas c, int x0, int x1, int y0, int y1,Paint p) {
        c.drawLines(new float[]{x0,y0,x1,y0},p);
        c.drawLines(new float[]{x1,y0,x1,y1},p);
        c.drawLines(new float[]{x1,y1,x0,y1},p);
        c.drawLines(new float[]{x0,y1,x0,y0},p);
    }

    @Deprecated
    public void drawPixel(Vector2i pos, int c) {
        Vector2i np=normalizeVector(pos);
        expandIfNeeded(np);
        np=normalizeVector(pos);
        bitmap.setPixel(np.x, np.y, c);
    }

    @Deprecated
    public void securePosition(Vector2i pos){
        expandIfNeeded(normalizeVector(pos));
    }

    public void securePositionDirect(Vector2i pos){
        expandIfNeeded(pos);
    }


    @Deprecated
    public int getUnsafePixel(Vector2i arg0) {
        Vector2i vector2i=normalizeVector(arg0);
        return bitmap.getPixel(vector2i.x,vector2i.y);
    }

    public int getUnsafePixelDirect(Vector2i arg0) {
        return bitmap.getPixel(arg0.x,arg0.y);
    }

    @Deprecated
    public void drawPixelNonSafe(Vector2i pos, int c) {
        Vector2i np=normalizeVector(pos);
        bitmap.setPixel(np.x, np.y, c);
    }

    public void drawPixelNonSafeDirect(Vector2i pos, int c) {
        if(!pos.inside(new Vector2i(bitmap))){
            Log.wtf(TAG,"Yea, good luck painting "+pos+" when whole bitmap has "+new Vector2i(bitmap));
        }
        else
            bitmap.setPixel(pos.x, pos.y, c);
    }

    public Vector2i normalizeVector(Vector2i arg0){
        Vector2i var=new Vector2i(arg0);
        var.sub(offset);
        if(scale!=1)var.divide(scale);
        return var;
    }

    public void resetZoom(){
        zoom(1);
    }

    public synchronized void zoom(float change){
        float new_scale=getNormalizedScale(change);
        offset.add(new Vector2i(bitmap).multiply(scale).sub(new Vector2i(bitmap).multiply(new_scale)).divide(2));
        scale=new_scale;
    }

    private float getNormalizedScale(float change) {
        float v=this.scale+change;
        if(v>scale_max)v=scale_max;
        if(v<scale_min)v=scale_min;
        return v;
    }

    @NonNull
    private void expandIfNeeded(Vector2i pos) {
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
        if(scale!=1)f.multiply(scale);
        synchronized (this) {
            bitmap = copyOnBitmap(addSize, f);
            offset.sub(f);
        }
        Log.d(TAG,"Bitmap expanded!");

    }


    private Bitmap copyOnBitmap(Vector2i new_size,Vector2i o) {
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

    public Bitmap getData() {
        return bitmap;
    }

    private static Bitmap makeClearBitmap(Vector2i s){
        return Bitmap.createBitmap(s.x,s.y, Bitmap.Config.ARGB_8888);
    }

    public void clear(){
        bitmap=makeClearBitmap(new Vector2i(bitmap));
    }

    public void move(Vector2i v) {
        offset.add(v);
    }

    private static final String OFFSET_X="SMARTBITMAP_OFFSET_X";
    private static final String OFFSET_Y="SMARTBITMAP_OFFSET_Y";
    private static final String SCALE="SMARTBITMAP_SCALE";

    public synchronized void loadSettings(Bundle s) {
        offset.x=s.getInt(OFFSET_X,offset.x);
        offset.y=s.getInt(OFFSET_Y,offset.y);
        scale=s.getFloat(SCALE,scale);
    }

    public synchronized void saveSettings(Bundle s) {
        s.putInt(OFFSET_X,offset.x);
        s.putInt(OFFSET_Y,offset.y);
        s.putFloat(SCALE,scale);
    }

    public synchronized float getScale() {
        return scale;
    }
}
