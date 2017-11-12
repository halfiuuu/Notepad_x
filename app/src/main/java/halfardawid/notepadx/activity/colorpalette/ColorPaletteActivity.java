package halfardawid.notepadx.activity.colorpalette;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import halfardawid.notepadx.R;

public class ColorPaletteActivity extends AppCompatActivity {

    static private final String TAG="COLOR_PALETTE";
    static public final int CODE=514;
    static public final String EXTRA_COLOR="COLOR";
    public static final int DEFAULT_COLOR = Color.BLACK;

    private final Intent result=new Intent();
    private AtomicInteger color=new AtomicInteger(DEFAULT_COLOR);

    List<View> refresher=new ArrayList<>();

    public int getColor(){
        return color.get();
    }

    public void setColor(int val){
        color.set(val);
        refreshAll();
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
        loadColorIntent();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_color_palette);
        setResult(CODE,result);
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

    public void addToRefresher(View view) {
        synchronized (refresher) {
            refresher.add(view);
        }
    }
}
