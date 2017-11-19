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
    public synchronized final void splat(SmartBitmap bitmap, Vector2i pos_arg, int color) {
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
                    splat_col = mixColors(x,y,splat_pos_radius, splat_pixel_map,index, alpha, r,g,b, color);
                    if (splat_col == null) continue;
                    splat_pixel_map[index]=splat_col;
                }
            }
        }
        bitmap.drawPixelsNonSafeDirect(real_position,radius,splat_pixel_map);
    }

    @Nullable
    private Integer mixColors(final int x, final int y, final Vector2i pos, int[] bitmap, int index, int a, int r, int g, int b, int color){
        float smoothing=smoothing(pos.pythagoras());
        if(smoothing ==1&&a>254)
            return color;
        if(smoothing==0)
            return null;

        int alpha=(int)(a*smoothing);
        int base_color=bitmap[index];
        int base_alpha=Color.alpha(base_color);
        if(base_alpha+alpha==0)return null;
        int base_r=Color.red(base_color),base_g=Color.green(base_color),base_b=Color.blue(base_color);
        int inverted_base_alpha = 255 - base_alpha;
        return Color.argb(
                Math.min(base_alpha+((inverted_base_alpha*alpha)>>8),255),
                ((r*alpha)+(base_r*base_alpha))/(alpha+base_alpha),
                ((g*alpha)+(base_g*base_alpha))/(alpha+base_alpha),
                ((b*alpha)+(base_b*base_alpha))/(alpha+base_alpha)

        );
    }

    private void secure(SmartBitmap bitmap, Vector2i pos, Vector2i radius) {
        Vector2i pp=new Vector2i(pos);
        pp.sub(radius);
        bitmap.securePositionDirect(pp);
        pp.copy(pos);
        pp.add(radius);
        bitmap.securePositionDirect(pp);
    }

    public float splatLine(SmartBitmap bitmap, Vector2i lastPosition, Vector2i pos, int i, float last_dist) {
        Vector2i distance=new Vector2i(pos);
        distance.sub(lastPosition);
        float whole_distance=distance.pythagoras();
        float dist_i=last_dist;
        for(;dist_i<whole_distance;dist_i+=getSpacing(bitmap)){
            float multiplier=dist_i/whole_distance;
            Vector2i between=new Vector2i(distance);
            between.multiply(multiplier);
            between.add(lastPosition);
            splat(bitmap,between,i);
        }
        return dist_i-whole_distance;
    }

    public float getSpacing(SmartBitmap bitmap) {
        return spacing*bitmap.getScale();
    }
}
