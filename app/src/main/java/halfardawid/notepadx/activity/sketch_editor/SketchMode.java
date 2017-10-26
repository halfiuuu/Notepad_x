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

    final static private String BUNDLE_MODE="SMS_MODE"; //Sms stands for sketch mode settings, writing just in case...

    public synchronized void saveSetting(Bundle s){
        s.putInt(BUNDLE_MODE,mode.ordinal());
    }
    public synchronized void loadSettings(Bundle s) {
        mode=MODE.values()[s.getInt(BUNDLE_MODE,0)];
    }

    public void toggleEraser() {
        toggle(MODE.ERASE);
    }
    public void toggleMove(){
        toggle(MODE.MOVE);
    }

    private void toggle(final MODE m) {
        final SketchMode this_t=this;
        new Thread(new Runnable() {
            @Override
            public void run() {
                synchronized (this_t){
                    if(mode==m)mode=def;
                    else mode=m;
                }
            }
        }).start();
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
