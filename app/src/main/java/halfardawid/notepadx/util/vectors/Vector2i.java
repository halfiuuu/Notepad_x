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

package halfardawid.notepadx.util.vectors;


import android.graphics.Bitmap;
import android.util.Log;
import android.view.View;

import halfardawid.notepadx.activity.sketch_editor.SketchCanvas;

public class Vector2i {
    public int x;
    public int y;

    public Vector2i(Vector2i a){
        x=a.x;
        y=a.y;
    }

    public Vector2i(int both){
        x=both;
        y=both;
    }

    public Vector2i(int ax, int ay){
        x=ax;
        y=ay;

    }

    public Vector2i(Bitmap bitmap) {
        x=bitmap.getWidth();
        y=bitmap.getHeight();
    }

    public Vector2i(View v) {
        x=v.getWidth();
        y=v.getHeight();
    }

    public Vector2i copy(Vector2i a){
        x=a.x;
        y=a.y;
        return this;
    }
    public Vector2i add(int arg){
        x+=arg;
        y+=arg;
        return this;
    }
    public Vector2i sub(int arg){
        x-=arg;
        y-=arg;
        return this;
    }
    public Vector2i add(Vector2i arg){
        x+=arg.x;
        y+=arg.y;
        return this;
    }
    public Vector2i sub(Vector2i arg){
        x-=arg.x;
        y-=arg.y;
        return this;
    }

    public boolean isNone(){
        return x==0&&y==0;
    }

    public Vector2i abs(){
        if(x<0)x*=-1;
        if(y<0)y*=-1;
        return this;
    }

    public int added(){
        return x+y;
    }

    public Vector2i cutAllPositive(){
        if(x>0)x=0;
        if(y>0)y=0;
        return this;
    }


    public Vector2i cutAllNegative() {
        if(x<0)x=0;
        if(y<0)y=0;
        return this;
    }

    public Vector2i checkInBounds(Vector2i size,int steps) {
        Vector2i r=new Vector2i(0,0);
        if(x>=size.x){
            int f=(x-size.x);
            int o=f%steps;
            int t=(((f/steps))+((o!=0)?1:0));
            if(t==0)t++;
            r.x=t*steps;
        }else if(x<0){
            int f=(-x);
            int o=f%steps;
            int t=(((f/steps))+(o!=0?1:0));
            if(t==0)t++;
            r.x=t*-steps;
        }
        if(y>=size.y){
            int f=(y-size.y);
            int o=f%steps;
            int t=(((f/steps))+((o!=0)?1:0));
            if(t==0)t++;
            r.y=t*steps;
        }else if(y<0){
            int f=(-y);
            int o=f%steps;
            int t=(((f/steps))+(o!=0?1:0));
            if(t==0)t++;
            r.y=t*-steps;
        }
        //Log.d("VECTOR2I","Check in bounds ready, "+r+" on "+this+" for "+size);
        return r;
    }


    @Override public String toString(){
        return "V2I["+x+"/"+y+"]";
    }

    public Vector2i multiply(float multipler) {
        x*=multipler;
        y*=multipler;
        return this;
    }

    public Vector2i divide(float scale) {
        x/=scale;
        y/=scale;
        return this;
    }

    public float pythagoras() {
        return (float) Math.sqrt(x*x+y*y);
    }

    public boolean inside(Vector2i vector2i) {
        return x>=0&&x<vector2i.x&&y>=0&&y<vector2i.y;
    }

    public void set(int xx, int yy) {
        x=xx;
        y=yy;
    }

    public int radius_overlay() {
        return (x*2)*(y*2);
    }

    public Vector2i copy() {
        return new Vector2i(this);
    }

    public int length() {
        return x*y;
    }

}

