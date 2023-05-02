package edu.nuist.codehelper.exception;

public class RequiredPropertyNoFind  extends Exception{
    private String message;
    
    public RequiredPropertyNoFind(){
        this.message = "ERROR: the required property of this node isn't found!";
    }

    public String getMessage(){
        return message;
    }
}
