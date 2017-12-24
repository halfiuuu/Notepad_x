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
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;
import android.widget.ToggleButton;

import halfardawid.notepadx.R;
import halfardawid.notepadx.activity.sketch_editor.colorpalette.ColorPaletteActivityTab;
import halfardawid.notepadx.activity.generic.GenericNoteActivity;
import halfardawid.notepadx.activity.generic.layouts.SimpleProgressBar;
import halfardawid.notepadx.activity.sketch_editor.crop.CropToNumbers;
import halfardawid.notepadx.util.exceptions.CropFailed;
import halfardawid.notepadx.util.exceptions.ErrorReportable;
import halfardawid.notepadx.util.note.types.SketchNote;
import halfardawid.notepadx.util.vectors.Vector2i;

public final class SketchActivity extends GenericNoteActivity<SketchNote> implements ErrorReportable{
    public static final String TAG="SKETCH_EDITOR";

    private SketchCanvas sketch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        loadIntentData(SketchNote.class);
        setContentView(R.layout.activity_sketch);
        sketch=(SketchCanvas)findViewById(R.id.as_sketchcanvas);
        sketch.setUpdatable((SimpleProgressBar)findViewById(R.id.progress_bar));
        refreshDataToView();
    }


    @Override
    public void inherentRefresh() {
        Bitmap b;
        if(!(sketch!=null&&note!=null&&(b=note.getBitmap())!=null))return;
        sketch.setBitmap(b);
    }

    @Override
    public void onActivityResult(int code,int r, Intent intent){
        super.onActivityResult(code,r,intent);
        switch(code){
            case ColorPaletteActivityTab.CODE:
                if(intent.hasExtra(ColorPaletteActivityTab.EXTRA_COLOR))
                    sketch.setBrushColor(
                            intent.getIntExtra(ColorPaletteActivityTab.EXTRA_COLOR,
                                    ColorPaletteActivityTab.DEFAULT_COLOR));
                break;
            case CropToNumbers.CODE:
                if(intent!=null){
                    Vector2i new_size=new Vector2i(
                            intent.getIntExtra(CropToNumbers.SIZE_X,0),
                            intent.getIntExtra(CropToNumbers.SIZE_Y,0));
                    Vector2i cutout=new Vector2i(
                            intent.getIntExtra(CropToNumbers.CUT_X,0),
                            intent.getIntExtra(CropToNumbers.CUT_Y,0));
                    try {
                        sketch.cropCanvas(new_size,cutout);
                    } catch (CropFailed cropFailed) {
                        reportError(cropFailed);
                    }
                }
                break;
        }
    }

    @Override
    protected void loadSettings(Bundle s) {
        sketch.loadSettings(s);
    }

    @Override
    protected void saveSettings(Bundle s) {
        sketch.saveSettings(s);
    }

    @Override
    public void prepareForSave() {
        if(sketch==null)return;
        Bitmap b=sketch.getBitmap();
        if(b==null)return;
        note.setBitmap(b);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.sketch_editor_menu, menu);
        return true;
    }

    @Override
    protected boolean menuButtonPressed(MenuItem item) {
        switch(item.getItemId()) {
            case R.id.sem_reset_zoom:
                sketch.resetZoom();
                return true;
            case R.id.sem_reset_position:
                sketch.resetOffset();
                return true;
            case R.id.sem_clear_canvas:
                sketch.clearCanvas();
                return true;
            case R.id.sem_auto_crop_canvas:
                try {
                    sketch.autoCropCanvas();
                } catch (CropFailed cropFailed) {
                    reportError(cropFailed);
                }
                return true;
            case R.id.sem_numbers_crop_canvas:
                startManualCrop();
                return true;
            default:
                return false;
        }
    }

    public void reportError(final Exception e) {
        final Context t=this;
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(t,"Action ended up as a failure...\n"+e.getMessage(),Toast.LENGTH_LONG).show();
            }
        });
        Log.wtf(TAG,"Some failure happened...",e);
    }

    public void onMoveModeToggleClicked(View v){
        sketch.setMoveMode(((ToggleButton)(v)).isChecked());
    }

    public void onEraserModeToggleClicked(View v){
        sketch.setErasing(((ToggleButton)(v)).isChecked());
    }

    public void onBrushEditorClicked(View v){
        Toast.makeText(this,"Wowser!",Toast.LENGTH_SHORT);
    }

    public void onColorPaletteClicked(View v){
        startColorPalette();
    }

    private void startColorPalette() {
        Intent i=new Intent(this,ColorPaletteActivityTab.class);
        i.putExtra(ColorPaletteActivityTab.EXTRA_COLOR,sketch.getBrushColor());
        startActivityForResult(i, ColorPaletteActivityTab.CODE);
    }

    private void startManualCrop() {
        Intent i=new Intent(this,CropToNumbers.class);
        startActivityForResult(i, CropToNumbers.CODE);
    }

    @Override
    protected String getTag() {
        return TAG;
    }

}
