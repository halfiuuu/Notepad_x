package halfardawid.notepadx.activity.sketch_editor;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.res.ResourcesCompat;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;
import android.widget.ToggleButton;

import halfardawid.notepadx.R;
import halfardawid.notepadx.activity.colorpalette.ColorPaletteActivityTab;
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
            case ColorPaletteActivityTab.CODE:
                if(intent.hasExtra(ColorPaletteActivityTab.EXTRA_COLOR))
                    sketch.setBrushColor(intent.getIntExtra(ColorPaletteActivityTab.EXTRA_COLOR,ColorPaletteActivityTab.DEFAULT_COLOR));
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
        inflater.inflate(R.menu.generic_note_editor_menu, menu);
        return true;
    }

    @Override
    protected boolean menuButtonPressed(MenuItem item) {
        return false;//Floating buttons seems more accessible.
        /*switch(item.getItemId()) {
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
        }*/
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

    @Override
    protected String getTag() {
        return TAG;
    }

}
