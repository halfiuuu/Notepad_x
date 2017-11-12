package halfardawid.notepadx.activity.generic;

import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;


public class PopUpActivity extends AppCompatActivity {
    @Override
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        final View view = getWindow().getDecorView();
        final WindowManager.LayoutParams lp = (WindowManager.LayoutParams) view.getLayoutParams();
        final DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);

        lp.gravity = Gravity.CENTER;
        lp.width = (int) (metrics.widthPixels*.8f);
        lp.height = (int) (metrics.heightPixels*.8f);


        getWindowManager().updateViewLayout(view, lp);
    }
}
