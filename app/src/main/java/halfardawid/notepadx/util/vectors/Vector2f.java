package halfardawid.notepadx.util.vectors;

public class Vector2f {
    public float x;
    public float y;
    public Vector2f(float ax, float ay){
        x=ax;
        y=ay;

    }
    public void add(float arg){
        x+=arg;
        y+=arg;
    }
    public void sub(float arg){
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
}
