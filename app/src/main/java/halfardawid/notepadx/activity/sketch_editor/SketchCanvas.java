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

package halfardawid.notepadx.activity.sketch_editor;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import java.util.concurrent.atomic.AtomicInteger;

import halfardawid.notepadx.activity.sketch_editor.colorpalette.ColorPaletteActivityTab;
import halfardawid.notepadx.activity.generic.layouts.Updatable;
import halfardawid.notepadx.activity.sketch_editor.brushes.Brush;
import halfardawid.notepadx.activity.sketch_editor.brushes.PAINTING_MODE;
import halfardawid.notepadx.activity.sketch_editor.brushes.types.SoftTipCircle;
import halfardawid.notepadx.activity.sketch_editor.finger_movement.Fingers;
import halfardawid.notepadx.util.vectors.Vector2i;


public class SketchCanvas extends View {

    private static final String TAG = "SKETCH_CANVAS";
    private static final String BRUSH_COLOR = "BRUSH_COLOR";

    private Fingers controller;
    private boolean move=false;
    private boolean erase=false;
    private Brush brush=new SoftTipCircle(20,40);
    private Updatable bar=null;
    private AtomicInteger brush_color=new AtomicInteger(ColorPaletteActivityTab.DEFAULT_COLOR);

    public SketchCanvas(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        image=new SmartBitmap();
        controller =new Fingers(this);
    }

    public void loadSettings(Bundle s) {
        brush_color.set(s.getInt(BRUSH_COLOR, ColorPaletteActivityTab.DEFAULT_COLOR));
        image.loadSettings(s);
    }
    public void saveSettings(Bundle s) {
        s.putInt(BRUSH_COLOR,brush_color.get());
        image.saveSettings(s);
    }

    public boolean isErasing(){
        return erase;
    }

    public boolean isMoveMode(){
        return move;
    }

    private SmartBitmap image;

    private boolean doOnce_onDraw_reset=true;
    @Override
    protected void onDraw(Canvas c){
        if(doOnce_onDraw_reset){
            doOnce_onDraw_reset=false;
            resetOffset();
        }
        c.save();
        c.drawColor(Color.TRANSPARENT);
        image.drawOnCanvas(c);
        c.restore();
    }
    @Override
    public boolean onTouchEvent(final MotionEvent me){
        controller.handleEvent(me);
        postInvalidate();
        return true;
    }

    @Override
    public void onMeasure(int x, int y){
        super.onMeasure(x,y);
    }

    public boolean toggleMove() {
        return move=!move;
    }

    public Bitmap getBitmap(){
        return image.getData();
    }

    public void setBitmap(Bitmap arg0){
        if(arg0==null)return;
        image.clone(arg0);
    }

    public SmartBitmap getSmartBitmap() {
        return image;
    }

    public Brush getBrush() {
        return brush;
    }

    public boolean toggleErase() {
        return erase=!erase;
    }


    public void setUpdatable(Updatable u){
        bar=u;
    }

    public Updatable getUpdatable(){
        return bar;
    }

    public void setBrushColor(int brushColor) {
        brush_color.set(brushColor);
    }

    public int getBrushColor() {
        return brush_color.get();
    }

    public void setErasing(boolean arg) {
        erase=arg;
    }

    public PAINTING_MODE getPaintMode() {
        if(erase)return PAINTING_MODE.ERASE;
        return PAINTING_MODE.NORMAL;
    }

    public void setMoveMode(boolean arg0) {
        move=arg0;
    }

    public void resetZoom() {
        getSmartBitmap().resetZoom();
        invalidate();
    }

    public void resetOffset() {
        getSmartBitmap().resetOffsetToCenter(this);
        invalidate();
    }

    public void clearCanvas() {
        image.clear();
        resetOffset();
    }

    public void autoCropCanvas() {
        image.autoCrop();
        resetOffset();
    }

    public void cropCanvas(Vector2i new_size, Vector2i cutout) {
        Log.d("Cropping to",new_size+" "+cutout);
        image.crop(new_size, cutout);
        resetOffset();
    }
}
