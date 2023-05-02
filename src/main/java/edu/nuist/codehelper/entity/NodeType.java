package edu.nuist.codehelper.entity;

public enum NodeType {
    PAGE("page", 0),
    DIV("div",  1),
    STYPE("style",  2),

    PROJECT("project", 3),
    DEPENDENCY("dependenies", 4),
    BEAN("bean", 5),
    HTML("html", 6),
    USER("user", 6);

    private int index;
    private String name;

    private NodeType(String name, int index) {
        this.name = name;
        this.index = index;
    }
   
    public static String getName(int index) {  
        for (NodeType c : NodeType.values()) {  
            if (c.getIndex() == index) {  
                return c.name;  
            }  
        }  
        return null;  
    }

    
    public static NodeType getByIndex(int index) {  
        for (NodeType c : NodeType.values()) {  
            if (c.getIndex() == index) {  
                return c;
            }  
        }  
        return null;  
    }

    
    public static NodeType getByName(String name) {  
        for (NodeType c : NodeType.values()) {  
            if (c.getName().equals(name)) {  
                return c;
            }  
        }  
        return null;  
    }


    public String getName() {
        return name;
    }


    public void setName(String name) {
        this.name = name;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }
}
