package edu.nuist.codehelper.render.page;

import java.util.HashMap;
import java.util.HashSet;

public class JSRender {
    private HashMap<String,  HashSet<String>> jsSildes = new HashMap<>();
    public static final String COMP_INIT = "init";
    public static final String COMP_EVENT_REGIST = "regist";
    public static final String COMP_EVENT_RESPONSE = "response";

    public JSRender(){
        jsSildes.put(COMP_INIT,  new HashSet<String>());
        jsSildes.put(COMP_EVENT_REGIST, new HashSet<String>());
        jsSildes.put(COMP_EVENT_RESPONSE, new HashSet<String>());
    }

    public void append(String token ,String t){
        this.jsSildes.get(token).add(t);
    }

    public String get(String token){
        StringBuffer sb = new StringBuffer();
        for(String s: this.jsSildes.get(token)){
            sb.append(s + "\r\n");
        }
        return sb.toString();
    }
}
