package neptune.commands;

public class MissingArgumentException extends Exception{
    public MissingArgumentException(String s){
        super(s);
    }
}
