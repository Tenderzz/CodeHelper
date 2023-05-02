package edu.nuist.codehelper.exception;

public class NodeTypeNoFind {
    private String message;
    
    public NodeTypeNoFind(String message){
        this.message = message;
        this.message = "WARNING: Node Type of the line: '" + message + "' is not found!";
    }

    public String getMessage(){
        return message;
    }
}
