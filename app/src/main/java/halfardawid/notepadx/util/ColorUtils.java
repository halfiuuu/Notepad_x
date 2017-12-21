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

package halfardawid.notepadx.util;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.support.annotation.ArrayRes;
import android.support.annotation.ColorInt;
import android.support.annotation.ColorRes;
import android.support.annotation.IdRes;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import halfardawid.notepadx.R;

public final class ColorUtils {
    private ColorUtils(){}

    static public @ColorRes int calcContrast(@ColorInt int color) {
        return (calcContrastActivation(color))?android.R.color.white:android.R.color.black;
    }

    public static boolean calcContrastActivation(@ColorInt int color) {
        final int c[]=new int[]{Color.red(color),Color.green(color),Color.blue(color)};
        int activation=0;
        for(int i=0;i<c.length;i++)
            activation+=c[i]*c[i];
        activation/=1000;
        //Log.d("Activation",activation+"...");
        return activation<60;
    }

    public static int getColorSpecific(Context c, int id,@ArrayRes int array){
        return c.getResources().getIntArray(array)[id];
    }

    public static int recognizeColorString(Context c, String name) {
        if (name != null) {
            String[] cn = c.getResources().getStringArray(R.array.color_names);
            for (int o = 0; o < cn.length; o++) {
                if (name.equals(cn[o])) return o;
            }
        }
        return 0;//0 being default note color... I guess...
    }

    public static String recognizeColorId(Context c,int id) {
        return c.getResources().getStringArray(R.array.color_names)[id];
    }

    public static void applyColors(View v, @IdRes int id, int color_id, int col_array){
        v.findViewById(id).setBackgroundColor(getColorSpecific(v.getContext(),color_id,col_array));
    }

    static public void applyColors(AppCompatActivity activity, String color) {
        activity.getWindow().getDecorView().setBackgroundColor(getColorSpecific(activity,recognizeColorString(activity,color),R.array.color_light));
    }

    static public void applyColorsToBar(AppCompatActivity activity, String color) {
        final int cid= recognizeColorString(activity,color);
        ActionBar bar = activity.getSupportActionBar();
        if(bar==null)return;
        final int bgc=getColorSpecific(activity,cid,R.array.color_base);
        final boolean contrast= calcContrastActivation(bgc);
        bar.setBackgroundDrawable(new ColorDrawable(bgc));
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            activity.getWindow().setStatusBarColor(getColorSpecific(activity, cid, R.array.color_dark));
        }

    }
}
