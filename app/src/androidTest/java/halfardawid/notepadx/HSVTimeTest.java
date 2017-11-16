package halfardawid.notepadx;

import android.graphics.Color;
import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;
import junit.framework.TestCase;

import org.junit.Test;
import org.junit.runner.RunWith;


@RunWith(AndroidJUnit4.class)
public class HSVTimeTest{
    private static long times=100*100;
    private static long width=100;
    private static long designated_x=500;
    private static long designated_y=500;

    @Test
    public void test_color_hsv_to_color() {
        float[] hsv=new float[3];
        Color.colorToHSV(Color.RED,hsv);
        for(int r=0;r<times;r++){
            hsv[0]=r%360;
            Color.HSVToColor(128,hsv);
        }
    }

    @Test
    public void test_color_hsv_to_color_with_array(){
        float[] hsv=new float[3];
        int[] bitmap=new int[(int)times];
        Color.colorToHSV(Color.RED,hsv);
        for(int r=0;r<times;r++){
            bitmap[r]=Color.HSVToColor(128,hsv);
        }
    }
}