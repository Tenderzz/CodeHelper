package edu.nuist.codehelper.exception;

public class LayoutDivHasOtherNode extends Exception{
    public LayoutDivHasOtherNode(){
       super("ERROR: the layout div node should not contain sub nodes are not div .");
    }
    
}
