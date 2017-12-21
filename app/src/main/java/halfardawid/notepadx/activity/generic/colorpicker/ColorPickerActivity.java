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

package halfardawid.notepadx.activity.generic.colorpicker;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import halfardawid.notepadx.R;
import halfardawid.notepadx.activity.generic.PopUpActivity;

public class ColorPickerActivity extends PopUpActivity implements ColorPickReaction {
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
