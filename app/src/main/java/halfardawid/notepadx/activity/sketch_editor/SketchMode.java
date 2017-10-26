package halfardawid.notepadx.activity.sketch_editor;


import android.os.Bundle;

import halfardawid.notepadx.activity.sketch_editor.brushes.Brush;
import halfardawid.notepadx.activity.sketch_editor.brushes.SolidCircleBrush;

/**
 * Sketch settings done properly (or at least better)
 */

final class SketchMode {
    private MODE mode;
    private static final MODE def=MODE.DRAW;
    private Brush currentBrush=new SolidCircleBrush(10,5);
    public SketchMode(){
        mode=MODE.DRAW;
    }

    public SketchMode(final SketchMode original){
        synchronized (original) {
            mode = original.mode;
        }
    }

    final static private String BUNDLE_MODE="SMS_MODE"; //Sms stands for sketch mode settings, writing just in case...

    public SketchMode(Bundle s){
        mode=MODE.values()[s.getInt(BUNDLE_MODE,0)];
    }

    public synchronized void saveSetting(Bundle s){
        s.putInt(BUNDLE_MODE,mode.ordinal());
    }

    public void toggleEraser() {
        toggle(MODE.ERASE);
    }

    private synchronized void toggle(MODE m) {
        if(mode==m)mode=def;
        else mode=m;
    }

    public void toggleMove(){
        toggle(MODE.MOVE);
    }

    public synchronized boolean isEraser(){
        return mode==MODE.ERASE;
    }

    public synchronized boolean isMove() {
        return mode==MODE.MOVE;
    }

    public synchronized boolean isDraw(){
        return mode==MODE.DRAW;
    }

    public synchronized Brush getBrush() {
        return currentBrush;
    }

    private enum MODE{
        DRAW,MOVE,ERASE
    }
}
