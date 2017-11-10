package halfardawid.notepadx.activity.colorpicker;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import halfardawid.notepadx.R;

public class ColorPickerActivity extends AppCompatActivity implements ColorPickReaction {
    public final static int CODE=513;
    public static final String COLOR_ID = "COLOR_ID";
    private final Intent result=new Intent();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_color_picker);
        setResult(CODE,result);
        findViewById(R.id.acp_cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    public void applyColorPick(int id) {
        result.putExtra(COLOR_ID,id);
        finish();
    }
}
