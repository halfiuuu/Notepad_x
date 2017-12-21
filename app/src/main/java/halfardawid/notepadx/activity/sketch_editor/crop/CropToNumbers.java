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

package halfardawid.notepadx.activity.sketch_editor.crop;

import android.content.Intent;
import android.support.annotation.IdRes;
import android.support.annotation.StringRes;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import halfardawid.notepadx.R;

public final class CropToNumbers extends AppCompatActivity {

    public final static int CODE=522;
    public static final String CUT_X = "CUT_X";
    public static final String CUT_Y = "CUT_Y";
    public static final String SIZE_X = "SIZE_X";
    public static final String SIZE_Y = "SIZE_Y";

    private static class KeyPair{
        public final String key;
        @IdRes public final int value;
        private KeyPair(String key, @IdRes int value) {
            this.key = key;
            this.value = value;
        }
    }

    public static final KeyPair[] FORM_EXTRACT_PATTERN={
            new KeyPair(CUT_X, R.id.acn_offset_x),
            new KeyPair(CUT_Y, R.id.acn_offset_y),
            new KeyPair(SIZE_X, R.id.acn_size_x),
            new KeyPair(SIZE_Y, R.id.acn_size_y)
    };

    private boolean validate(Intent i){
        boolean valid=true;
        /*if(i.getIntExtra(CUT_X,0)<0){
            setError(R.id.acn_offset_x,R.string.must_be_greater_than_0_or_equal);
            valid=false;
        }
        if(i.getIntExtra(CUT_Y,0)<0){
            setError(R.id.acn_offset_y,R.string.must_be_greater_than_0_or_equal);
            valid=false;
        }*/
        if(i.getIntExtra(SIZE_X,0)<=0){
            setError(R.id.acn_size_x,R.string.must_be_greater_than_0);
            valid=false;
        }
        if(i.getIntExtra(SIZE_Y,0)<=0){
            setError(R.id.acn_size_y,R.string.must_be_greater_than_0);
            valid=false;
        }
        return valid;
    }

    private void setError(@IdRes int id,@StringRes int error){
        ((EditText)findViewById(id)).setError(getString(error));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crop_numbers);
    }

    @Override
    public final boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public final void onConfirm(View v){
        Intent i=new Intent();
        for(KeyPair kp:FORM_EXTRACT_PATTERN)
            i.putExtra(kp.key,getValue(kp.value));
        if(!validate(i))return;
        setResult(RESULT_OK,i);
        finish();
    }

    private int getValue(@IdRes int id) {
        String s=((EditText)findViewById(id)).getText().toString();
        return s.isEmpty()?0:Integer.parseInt(s);
    }

    public final void onCancel(View v){
        setResult(RESULT_CANCELED,null);
        finish();
    }
}
