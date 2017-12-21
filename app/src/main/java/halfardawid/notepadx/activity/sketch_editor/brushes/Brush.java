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

package halfardawid.notepadx.activity.sketch_editor.brushes;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.support.annotation.Nullable;
import android.util.Log;

import halfardawid.notepadx.activity.sketch_editor.SmartBitmap;
import halfardawid.notepadx.util.vectors.Vector2i;


/**
 * Created by Dawid on 2017-10-17.
 */

public abstract class Brush {
    private static final String TAG="BRUSH";

    protected float spacing;
    protected float radius;

    /**
     * Returns a 0f-1f based on a distance. For circle-gradient types of brushes
     * @param distance sqrt((x^2)+(y^2))
     * @return [0f-1f]
     */
    abstract protected float smoothing(float distance);
    protected static final int expand=3;
    protected Brush(float spacing, float radius){
        this.spacing=spacing;
        this.radius=radius;
    }

    private int real_radius(){
        return (int) (expand+radius);
    }


    private int[] splat_pixel_map=null;
    private int splat_pixel_map_size=-1;
    private Integer splat_col;
    private Vector2i splat_pos_radius=new Vector2i(0);
    public synchronized final void splat(SmartBitmap bitmap, Vector2i pos_arg, int color,PAINTING_MODE painting_mode) {
        Vector2i radius=new Vector2i(real_radius());
        Vector2i real_position=bitmap.normalizeVector(pos_arg);

        int alpha= Color.alpha(color);
        int r=Color.red(color),g=Color.green(color),b=Color.blue(color);

        Vector2i n_pos=new Vector2i(0);

        secure(bitmap, real_position, radius);
        real_position=bitmap.normalizeVector(pos_arg);

        {
            int radius_multiplied=radius.radius_overlay();
            if (radius_multiplied != splat_pixel_map_size)
                splat_pixel_map=new int[splat_pixel_map_size=radius_multiplied];
        }

        bitmap.getUnsafePixelsDirect(real_position,radius,splat_pixel_map);
        {
            final int rx = radius.x <<1;
            final int ry = radius.y <<1;
            splat_pos_radius.x=-radius.x;
            for (int x=0; x < rx; x++,splat_pos_radius.x++){
                splat_pos_radius.y=-radius.y;
                for (int y = 0; y < ry; y++,splat_pos_radius.y++) {
                    final int index=y*rx+x;
                    splat_col=painting_mode.mix(
                            smoothing(splat_pos_radius.pythagoras()),
                            x,y,splat_pos_radius, splat_pixel_map,
                            index, alpha, r,g,b, color);
                    if (splat_col == null) continue;
                    splat_pixel_map[index]=splat_col;
                }
            }
        }
        bitmap.drawPixelsNonSafeDirect(real_position,radius,splat_pixel_map);
    }

    private void secure(SmartBitmap bitmap, Vector2i pos, Vector2i radius) {
        Vector2i pp=new Vector2i(pos);
        pp.sub(radius);
        bitmap.securePositionDirect(pp);
        pp.add(radius);
        pp.add(radius);
        bitmap.securePositionDirect(pp);
    }

    public float splatLine(SmartBitmap bitmap, Vector2i lastPosition, Vector2i pos, int i, float last_dist, PAINTING_MODE painting_mode) {
        Vector2i distance=new Vector2i(pos);
        distance.sub(lastPosition);
        float whole_distance=distance.pythagoras();
        float dist_i=last_dist;
        for(;dist_i<whole_distance;dist_i+=getSpacing(bitmap)){
            float multiplier=dist_i/whole_distance;
            Vector2i between=new Vector2i(distance);
            between.multiply(multiplier);
            between.add(lastPosition);
            splat(bitmap,between,i,painting_mode);
        }
        return dist_i-whole_distance;
    }

    public float getSpacing(SmartBitmap bitmap) {
        return spacing*bitmap.getScale();
    }
}
