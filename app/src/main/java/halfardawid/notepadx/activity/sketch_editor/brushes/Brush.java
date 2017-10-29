package halfardawid.notepadx.activity.sketch_editor.brushes;

import android.graphics.Bitmap;
import android.graphics.Color;

import halfardawid.notepadx.activity.sketch_editor.SmartBitmap;
import halfardawid.notepadx.util.vectors.Vector2i;

/**
 * Created by Dawid on 2017-10-17.
 */

public abstract class Brush {
    protected float spacing;
    protected float radius;
    abstract protected float smoothing(float distance);
    protected static final int expand=3;
    protected Brush(float spacing, float radius){
        this.spacing=spacing;
        this.radius=radius;
    }

    private int real_radius(){
        return (int) (expand+radius);
    }

    public final void splat(SmartBitmap bitmap, Vector2i pos_arg, int color) {
        Vector2i radius=new Vector2i(real_radius());
        Vector2i real_position=bitmap.normalizeVector(pos_arg);

        int alpha= Color.alpha(color);
        int r=Color.red(color),g=Color.green(color),b=Color.blue(color);
        Vector2i n_pos=new Vector2i(0);

        secure(bitmap, real_position, radius);

        //float scale_smoothing_factor=bitmap.getScale()/2;

        for(int x=-radius.x;x<radius.x;x++)
            for(int y=-radius.y;y<radius.y;y++) {
                n_pos.copy(real_position);
                n_pos.add(new Vector2i(x,y));
                int col=0;
                float smoothing = smoothing(dist(x, y));
                if(smoothing ==1){
                    col=color;
                }else if(smoothing==0){
                    continue;
                }else{
                    int base_color=bitmap.getUnsafePixelDirect(n_pos);
                    float invert=1F-smoothing;
                    col=Color.argb(
                            (int)((alpha*smoothing)+(Color.alpha(base_color)*invert)),
                            (int)((r*smoothing)+(Color.red(base_color)*invert)),
                            (int)((g*smoothing)+(Color.green(base_color)*invert)),
                            (int)((b*smoothing)+(Color.blue(base_color)*invert))
                    );
                }
                bitmap.drawPixelNonSafeDirect(n_pos,col);
            }
    }

    private void secure(SmartBitmap bitmap, Vector2i pos, Vector2i radius) {
        Vector2i pp=new Vector2i(pos);
        pp.sub(radius);
        bitmap.securePositionDirect(pp);
        pp.copy(pos);
        pp.add(radius);
        bitmap.securePositionDirect(pp);
    }

    private float dist(int x, int y) {
        return (float) Math.sqrt((float)((x*x)+(y*y)));
    }

    public float splatLine(SmartBitmap bitmap, Vector2i lastPosition, Vector2i pos, int i, float last_dist) {
        Vector2i distance=new Vector2i(pos);
        distance.sub(lastPosition);
        float whole_distance=dist(distance);
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

    private float getSpacing(SmartBitmap bitmap) {
        return spacing*bitmap.getScale();
    }

    private float dist(Vector2i distance) {
        return dist(distance.x,distance.y);
    }
}
