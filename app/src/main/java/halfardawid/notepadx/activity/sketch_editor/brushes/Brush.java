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

import android.graphics.Color;

import java.io.Serializable;
import java.lang.reflect.Field;

import halfardawid.notepadx.R;
import halfardawid.notepadx.activity.sketch_editor.SmartBitmap;
import halfardawid.notepadx.activity.sketch_editor.brushes.brush_pick.BrushDetailFragment;
import halfardawid.notepadx.activity.sketch_editor.brushes.types.SolidCircleBrush;
import halfardawid.notepadx.util.exceptions.ExpansionFailed;
import halfardawid.notepadx.util.vectors.Vector2i;


public abstract class Brush implements Serializable{
    private static final String TAG="BRUSH";

    @BrushParameter(name=R.string.brush_spacing, min=1, max=200)
    public Float spacing=1f;
    @BrushParameter(name=R.string.brush_radius, min=1,max=100)
    public Float radius=1f;

    public static Brush getDefaultBrush() {
        SolidCircleBrush solidCircleBrush = new SolidCircleBrush();
        solidCircleBrush.radius=5f;
        solidCircleBrush.spacing=3f;
        return solidCircleBrush;
    }

    /**
     * Returns a 0f-1f based on a distance. For circle-gradient types of brushes
     * @param position we'll see
     * @return [0f-1f]
     */
    abstract protected float smoothing(final Vector2i position);
    private transient static final int expand=3;

    public Brush(){}

    protected Brush(float spacing, float radius){
        this.spacing=spacing;
        this.radius=radius;
    }

    private int real_radius(){
        return (int) (expand+radius);
    }


    transient private int[] splat_pixel_map=null;
    transient private int splat_pixel_map_size=-1;
    transient private Integer splat_col;
    transient private Vector2i splat_pos_radius=null;
    public synchronized final void splat(SmartBitmap bitmap, Vector2i pos_arg, int color,PAINTING_MODE painting_mode) throws SplatFailed {
        if(splat_pos_radius==null)
            splat_pos_radius=new Vector2i(0);
        try {
            Vector2i radius = new Vector2i(real_radius());
            Vector2i real_position = bitmap.normalizeVector(pos_arg);

            int alpha = Color.alpha(color);
            int r = Color.red(color), g = Color.green(color), b = Color.blue(color);

            Vector2i n_pos = new Vector2i(0);

            secure(bitmap, real_position, radius);
            real_position = bitmap.normalizeVector(pos_arg);

            {
                int radius_multiplied = radius.radius_overlay();
                if (radius_multiplied != splat_pixel_map_size)
                    splat_pixel_map = new int[splat_pixel_map_size = radius_multiplied];
            }

            bitmap.getUnsafePixelsDirect(real_position, radius, splat_pixel_map);
            {
                final int rx = radius.x << 1;
                final int ry = radius.y << 1;
                splat_pos_radius.x = -radius.x;
                for (int x = 0; x < rx; x++, splat_pos_radius.x++) {
                    splat_pos_radius.y = -radius.y;
                    for (int y = 0; y < ry; y++, splat_pos_radius.y++) {
                        final int index = y * rx + x;
                        splat_col = painting_mode.mix(
                                smoothing(splat_pos_radius),
                                x, y, splat_pos_radius, splat_pixel_map,
                                index, alpha, r, g, b, color);
                        if (splat_col == null) continue;
                        splat_pixel_map[index] = splat_col;
                    }
                }
            }
            bitmap.drawPixelsNonSafeDirect(real_position, radius, splat_pixel_map);
        }catch(Exception e){
            throw new SplatFailed(e);
        }
    }

    private void secure(SmartBitmap bitmap, Vector2i pos, Vector2i radius) throws ExpansionFailed {
        Vector2i pp=new Vector2i(pos);
        pp.sub(radius);
        bitmap.securePositionDirect(pp);
        pp.add(radius);
        pp.add(radius);
        bitmap.securePositionDirect(pp);
    }

    public float splatLine(SmartBitmap bitmap, Vector2i lastPosition, Vector2i pos, int i, float last_dist, PAINTING_MODE painting_mode) throws SplatFailed {
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

    @Override
    public String toString() {
        StringBuilder sb=new StringBuilder();
        sb.append(getClass().getName());
        sb.append("[");
        Field[] fields = getClass().getFields();
        for (Field field:fields) {
            BrushParameter param = field.getAnnotation(BrushParameter.class);
            if(param==null)continue;
            sb.append(field.getName());
            sb.append(":");
            try {
                sb.append(field.get(this));
            } catch (IllegalAccessException e) {
                sb.append("Access Denied");
            }
            sb.append(";");
        }
        sb.append("](super:");
        sb.append(super.toString());
        sb.append(")");
        return sb.toString();
    }
}
