package edu.nuist.codehelper.exception;

public class SplitCharNotFind extends Exception{
    public SplitCharNotFind(){
        super();
    }
    public SplitCharNotFind(String message){
        super("ERROR: the key and  value of the line: '" + message + "'' should be split by ':' ");
    }
}
