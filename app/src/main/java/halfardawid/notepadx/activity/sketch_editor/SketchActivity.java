package halfardawid.notepadx.activity.sketch_editor;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import halfardawid.notepadx.R;
import halfardawid.notepadx.activity.generic.GenericNoteActivity;
import halfardawid.notepadx.util.note.types.SketchNote;

public final class SketchActivity extends GenericNoteActivity<SketchNote> {
    public static final String TAG="SKETCH_EDITOR";

    private SketchCanvas sketch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sketch);
        loadIntentData(SketchNote.class);
        sketch=(SketchCanvas)findViewById(R.id.as_sketchcanvas);
        refreshDataToView();
    }


    @Override
    public void refreshDataToView() {
        Bitmap b=null;
        if((sketch!=null&&note!=null&&(b=note.getBitmap())!=null)==false)return;
        sketch.setBitmap(b);
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
    public boolean onOptionsItemSelected(MenuItem item) {
        if(!handleGenericTasks(item))
            switch(item.getItemId()){
                case R.id.sem_brush:
                    return true;
                case R.id.sem_eraser:
                    return true;
                case R.id.sem_palette:
                    return true;
            }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected String getTag() {
        return TAG;
    }

}
