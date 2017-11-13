package halfardawid.notepadx.activity.colorpalette;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import halfardawid.notepadx.R;
import halfardawid.notepadx.activity.generic.PopUpActivity;

public class ColorPaletteActivity extends PopUpActivity implements ColorSliderInterface{

    static private final String TAG="COLOR_PALETTE";
    static public final int CODE=514;
    static public final String EXTRA_COLOR="COLOR";
    public static final int DEFAULT_COLOR = Color.BLACK;

    private ColorPreview colorPreview;
    private final Intent result=new Intent();
    private AtomicInteger color=new AtomicInteger(DEFAULT_COLOR);

    List<View> refresher=new ArrayList<>();

    @Override
    public int getColor(){
        return color.get();
    }

    @Override
    public void setColor(int val){
        color.set(val);
        refreshAll();
        colorPreview.setColor(val);
        result.putExtra(EXTRA_COLOR,val);
    }

    private void refreshAll() {
        synchronized (refresher) {
            for (View v : refresher) {
                v.invalidate();
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_color_palette);
        setResult(CODE,result);

        colorPreview= (ColorPreview) findViewById(R.id.acp_preview);
        loadColorIntent();
    }

    private void loadColorIntent() {
        setColor(getIntent().getIntExtra(EXTRA_COLOR,DEFAULT_COLOR));
    }

    @Override
    protected void onRestoreInstanceState(Bundle s) {
        setColor(s.getInt(EXTRA_COLOR,DEFAULT_COLOR));
    }

    @Override
    protected void onSaveInstanceState(Bundle s) {
        s.putInt(EXTRA_COLOR,color.get());
    }

    @Override
    public void addToRefresher(View view) {
        synchronized (refresher) {
            refresher.add(view);
        }
    }
}
