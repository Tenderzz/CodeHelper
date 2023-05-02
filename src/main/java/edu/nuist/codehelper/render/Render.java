package edu.nuist.codehelper.render;

import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import edu.nuist.codehelper.entity.Node;
import lombok.NoArgsConstructor;

@NoArgsConstructor
public abstract class Render {
    private Node n;
    private Map<String, String> ctx;

    public Render(Node n, Map<String, String> ctx){
        this.ctx = ctx;
        this.n = n;
    }

    public Map<String, String> getCTX(){
        return this.ctx;
    }

    public boolean epsString(String s){
        return s == null || "".equals(s);
    }

    public abstract boolean check();

    public abstract void exec() throws Exception;

    public Node getN() {
        return n;
    }

    public void setN(Node n) {
        this.n = n;
    }

    public String erease(String src, String token){
        return src.replaceAll("(\\["+token+"([\\s\\S]*?)\\])", "");
    }

    public String took(String src, String token){
        Pattern pattern = Pattern.compile("(\\["+token+"([\\s\\S]*?)\\])");        
        Matcher matcher = pattern.matcher(src);
        
        while(matcher.find()) {
           String all =  matcher.group();
           String tmp = all.substring(all.indexOf(":")+1, all.lastIndexOf("]"));
           src = src.replace( all, tmp);
        }
        return src;
    }

    public String decorateByFlags( boolean flag, String s, String token){
        if(flag){
            return took(s, token);
        }else{
            return erease(s, token);
        }
    }
    
}
