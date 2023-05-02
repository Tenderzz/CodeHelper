package edu.nuist.codehelper.render.project;

import java.util.HashSet;
import java.util.Map;

import edu.nuist.codehelper.component.FileTemplator;
import edu.nuist.codehelper.entity.Node;
import edu.nuist.codehelper.exception.DependencyNoFind;
import edu.nuist.codehelper.exception.SplitCharNotFind;
import edu.nuist.codehelper.render.Render;
import lombok.extern.log4j.Log4j2;

@Log4j2
public class ProjectDependencyRender extends Render{
    private HashSet<String> dependencies = new HashSet<>();
    private String rendRst = "";
    
    public ProjectDependencyRender(Node n, Map<String, String> ctx){
        super(n,ctx);
        try{
            for(Node sub: n.getSubNodes()){
                String type = sub.getKey();
                dependencies.add(type);
            }
        }catch(SplitCharNotFind e ){
            log.error(e.getMessage());
        }        
    }

    @Override
    public boolean check() {
        for(String  e : this.dependencies){
            boolean flag = false;
            for(String s : checkPoints){
                if( s.equals(e)){
                    flag = true;
                    break;
                }
            }
            if( !flag ) return false;
        }
        return true;
    }

    @Override
    public void exec() throws DependencyNoFind  {
        if( !check() ) throw new DependencyNoFind();
        else{
            StringBuffer sb = new StringBuffer();
            for(String d: this.dependencies ){
                sb.append( FileTemplator.getDependency(d.trim()) + "\r\n");
            }
            this.rendRst = sb.toString();
        }
        
    }

    public static final String[] checkPoints = new String[]{
        "hutools",
        "json",
        "html2pdf",
        "stripe",
        "redis",
        "jsoup",
        "guava"
    };

    public String getRendRst() {
        return rendRst;
    }

    public void setRendRst(String rendRst) {
        this.rendRst = rendRst;
    }

    
    
}
