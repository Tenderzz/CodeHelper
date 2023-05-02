package edu.nuist.codehelper.exception;

public class ProjectCreatError  extends Exception{
    public static final String[] infos = new String[]{
        "ERROR: the project directory isn't exists Or isn't a directory.",
        "ERROR: the project directory should be empty.",
        "ERROR: the project package name is illegal.",
    };

    public ProjectCreatError(){
        super();
    }
    public ProjectCreatError(int i){
        super(infos[i]);
    }
    
}
