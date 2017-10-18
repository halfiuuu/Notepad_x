package halfardawid.notepadx.util.vectors;


import android.graphics.Bitmap;
import android.util.Log;

public class Vector2i {
    public int x;
    public int y;

    public Vector2i(Vector2i a){
        x=a.x;
        y=a.y;
    }
    public Vector2i(int ax, int ay){
        x=ax;
        y=ay;

    }

    public Vector2i(Bitmap bitmap) {
        x=bitmap.getWidth();
        y=bitmap.getHeight();
    }

    public void copy(Vector2i a){
        x=a.x;
        y=a.y;
    }
    public void add(int arg){
        x+=arg;
        y+=arg;
    }
    public void sub(int arg){
        x-=arg;
        y-=arg;
    }
    public void add(Vector2i arg){
        x+=arg.x;
        y+=arg.y;
    }
    public void sub(Vector2i arg){
        x-=arg.x;
        y-=arg.y;
    }

    public boolean isNone(){
        return x==0&&y==0;
    }

    public void abs(){
        if(x<0)x*=-1;
        if(y<0)y*=-1;
    }

    public void cutAllPositive(){
        if(x>0)x=0;
        if(y>0)y=0;
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

}

