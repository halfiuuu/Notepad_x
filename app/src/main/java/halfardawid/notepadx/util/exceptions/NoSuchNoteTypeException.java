package halfardawid.notepadx.util.exceptions;


public class NoSuchNoteTypeException extends Exception{
    public NoSuchNoteTypeException(String a){
        super("Didn't found any note type of "+a);
    }
}