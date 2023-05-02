package edu.nuist.codehelper.exception;

public class ReferenceAttrNoFind extends Exception{
    public ReferenceAttrNoFind(){
        super("ERROR: the reference attribute isn't found in context.");
     }
}
