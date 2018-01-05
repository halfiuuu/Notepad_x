/*
 * Copyright (c) 2018 anno Domini.
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

package halfardawid.notepadx.activity.generic.layouts;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.preference.PreferenceManager;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.ScrollView;

import halfardawid.notepadx.R;

public class AutoScrollView extends ScrollView {
    private boolean initiatedAnimations;

    public AutoScrollView(Context context) {
        super(context);
    }

    public AutoScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public AutoScrollView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if(!initiatedAnimations)
            initAnimations();
    }

    private void initAnimations(){
        initiatedAnimations=true;
        if(!PreferenceManager.getDefaultSharedPreferences(
                this.getContext()).getBoolean(
                this.getContext().getString(
                        R.string.pref_note_list_auto_scroll),true))
            return;
        if(getChildCount()!=1)return;
        int maxScrollAmount =getChildAt(0).getHeight()-getHeight();
        if(maxScrollAmount<0)return;
        //Log.d(getClass().getName(),"max scroll:"+maxScrollAmount);
        //pref_note_list_auto_scroll_speed
        try {
            final int scroll_speed = Integer.parseInt(
                    PreferenceManager.getDefaultSharedPreferences(//Now that's what i call a monster
                            this.getContext()).getString(
                            this.getContext().getString(
                                    R.string.pref_note_list_auto_scroll_speed),
                            this.getContext().getString(R.string.pref_note_list_auto_scroll_speed_default)));
            ObjectAnimator objectAnimator = ObjectAnimator.ofInt(this, "scrollY", 0, maxScrollAmount);
            objectAnimator.setRepeatMode(ValueAnimator.REVERSE);
            objectAnimator.setRepeatCount(ValueAnimator.INFINITE);
            objectAnimator.setDuration(maxScrollAmount * scroll_speed);
            objectAnimator.start();
        }catch (NumberFormatException e){
            Log.wtf("Good job!","How did that happen!",e);//Impossible unless I'm a bloody moron.
        }
    }

    @Override
    protected boolean onRequestFocusInDescendants(int d, Rect r) {
        return false;
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        return false;
    }
}
