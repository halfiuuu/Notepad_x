package halfardawid.notepadx.util.vectors;


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

    public OUT_OF_BOUNDS checkInBounds(Vector2i size) {
        if(x>=size.x)return OUT_OF_BOUNDS.RIGHT;
        if(x<0)return OUT_OF_BOUNDS.LEFT;
        if(y>=size.y)return OUT_OF_BOUNDS.BOTTOM;
        if(y<0)return OUT_OF_BOUNDS.TOP;
        return OUT_OF_BOUNDS.NONE;
    }

    public enum OUT_OF_BOUNDS{
        TOP,BOTTOM,LEFT,RIGHT,NONE
    }

    @Override public String toString(){
        return "V2I["+x+"/"+y+"]";
    }

}

