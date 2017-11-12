package halfardawid.notepadx.activity.colorpalette;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import java.util.concurrent.atomic.AtomicInteger;

import halfardawid.notepadx.R;

public class ColorPaletteActivity extends AppCompatActivity {

    static private final String TAG="COLOR_PALETTE";
    static public final int CODE=514;
    static public final String EXTRA_COLOR="COLOR";
    public static final int DEFAULT_COLOR = Color.BLACK;

    private AtomicInteger color=null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        loadColorIntent();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_color_palette);
    }

    private void loadColorIntent() {
        int t=getIntent().getIntExtra(EXTRA_COLOR,-1);
        color.set((t<0)?DEFAULT_COLOR:t);
    }

    @Override
    protected void onRestoreInstanceState(Bundle s) {
        color.set(s.getInt(EXTRA_COLOR,DEFAULT_COLOR));
    }

    @Override
    protected void onSaveInstanceState(Bundle s) {
        s.putInt(EXTRA_COLOR,color.get());
    }

}
