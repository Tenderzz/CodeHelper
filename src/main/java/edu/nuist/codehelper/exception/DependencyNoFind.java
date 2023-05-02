package edu.nuist.codehelper.exception;

public class DependencyNoFind extends Exception{
    public DependencyNoFind(){
       super("ERROR: the dependency isn't provided by codehelper.");
    }
}
