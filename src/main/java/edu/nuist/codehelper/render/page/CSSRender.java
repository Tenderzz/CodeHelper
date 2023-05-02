package edu.nuist.codehelper.render.page;

import java.text.MessageFormat;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class CSSRender {
    private HashSet<String> cssIdents = new HashSet<>();
    private Map<String, String> componentCSS = new HashMap<>();
    private Set<String> importCss = new HashSet<>();

    public static final String CSS_MODE = "{0}'{'\r\n\r\n'}'";

    public void addIdents(String e){
        cssIdents.add(e);
    }

    public void addImportCss(String s){
        importCss.add(s);
    }

    public void addComponentCSS(String type, String css){
        componentCSS.put(type, css);
    }

    public String getImportCSS(){
        StringBuffer sb = new StringBuffer();
        for(String s: importCss){
            sb.append(s + "\r\n");
        }
        return sb.toString();
    }

    public String rend(){
        StringBuffer sb = new StringBuffer();
        for(String s: cssIdents){
            if( s.trim().equals("")) continue;
            String t = MessageFormat.format(CSS_MODE, s) + "\r\n";
            sb.append( '#'+t );
            sb.append( '.'+t );
        }

        for(String css: componentCSS.values()){
            sb.append(css + "\r\n");
        }

        return sb.toString();
    }
}
