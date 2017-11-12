package halfardawid.notepadx.activity.sketch_editor;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import halfardawid.notepadx.R;
import halfardawid.notepadx.activity.colorpalette.ColorPaletteActivity;
import halfardawid.notepadx.activity.colorpicker.ColorPickerActivity;
import halfardawid.notepadx.activity.generic.GenericNoteActivity;
import halfardawid.notepadx.activity.generic.layouts.SimpleProgressBar;
import halfardawid.notepadx.util.note.types.SketchNote;

public final class SketchActivity extends GenericNoteActivity<SketchNote> {
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
            case ColorPaletteActivity.CODE:
                if(intent.hasExtra(ColorPaletteActivity.EXTRA_COLOR))
                    sketch.setBrushColor(intent.getIntExtra(ColorPaletteActivity.EXTRA_COLOR,ColorPaletteActivity.DEFAULT_COLOR));
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
            case R.id.sem_move_mode:
                item.setChecked(sketch.toggleMove());
                return true;
            case R.id.sem_brush:
                return true;
            case R.id.sem_eraser:
                item.setChecked(sketch.toggleErase());
                return true;
            case R.id.sem_palette:
                startColorPalette();
                return true;
            default:
                return false;
        }
    }

    private void startColorPalette() {
        Intent i=new Intent(this,ColorPaletteActivity.class);
        i.putExtra(ColorPaletteActivity.EXTRA_COLOR,sketch.getBrushColor());
        startActivityForResult(i, ColorPaletteActivity.CODE);
    }

    @Override
    protected String getTag() {
        return TAG;
    }

}
