package edu.nuist.codehelper.render.page;

import java.text.MessageFormat;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import edu.nuist.codehelper.entity.Node;
import edu.nuist.codehelper.exception.SplitCharNotFind;
import edu.nuist.codehelper.render.Render;
import edu.nuist.codehelper.render.page.componentrender.ComponentRender;
import lombok.Data;
import lombok.extern.log4j.Log4j2;

@Log4j2
@Data
public class DivRender extends Render {
    private String id;
    private String layout;
    private String redent;

    private HtmlRender hr;
    private CSSRender cr;
    private JSRender jr;
    private HashSet<String> dependThdJS;

    public static final String DIV_START_MODE = "{0}<div id=\"{1}\" class=\"{2}\">";
    public static final String FRAGMENT_INCLUDE_MODE = "<div th:include=\"{0}\"></div>";
    public static final String FRAGMENT_REPLACE_MODE = "<div th:replace=\"{0}\"></div>";
    public static final String FRAGMENT_INSERT_MODE = "<div th:insert=\"{0}\"></div>";
    public static final String DIV_END_MODE = "{0}</div>";

    public DivRender(String redent, HtmlRender hr, CSSRender cr, JSRender jr, HashSet<String> dependThdJS, Node n, Map<String, String> ctx) {
        super(n, ctx);
        this.redent = redent;
        this.hr = hr;
        this.cr = cr;
        this.jr = jr;
        this.dependThdJS = dependThdJS;
    }
    
    @Override
    public void exec() throws Exception {
        if( !check() ) return ;
        this.layout = getStyle();
        this.id = this.getN().getVale();

        //添加本节点的CSS定义，包括ID与CLASS属性
        this.cr.addIdents( this.id );
        this.cr.addIdents( this.layout );
        
        //DIV头
        this.hr.append( MessageFormat.format(DIV_START_MODE, this.redent, this.id, this.layout ));
        
        Collections.reverse(this.getN().getSubNodes());
        for(Node n: this.getN().getSubNodes()){
            String type = n.getKey();
            String nextRedent = this.getRedent() + "    ";
            switch( type ){
                case "div":
                    DivRender dr = new DivRender(nextRedent, hr, cr, jr, dependThdJS, n, this.getCTX());
                    dr.exec();
                    break;
                case "th-include":
                    this.hr.append(nextRedent + MessageFormat.format(FRAGMENT_INCLUDE_MODE, n.getVale()));
                    break;
                case "th-insert":
                    this.hr.append(nextRedent + MessageFormat.format(FRAGMENT_INSERT_MODE, n.getVale()));
                    break;
                case "th-replace":
                    this.hr.append(nextRedent + MessageFormat.format(FRAGMENT_REPLACE_MODE, n.getVale()));
                    break;
                case "components":
                    for(Node sub: getComponents(n)){
                        ComponentRender comp = new ComponentRender(this.getCTX(), sub, nextRedent, hr, cr, jr, dependThdJS, sub.getKey());
                        comp.exec();
                    }
                    break;
            }
        }

        //DIV尾
        this.hr.append( MessageFormat.format(DIV_END_MODE, this.redent));
    }

    public List<Node> getComponents(Node n) throws SplitCharNotFind{
        LinkedList<Node> comps = new LinkedList<>();
        for(Node sub: n.getSubNodes()){
            comps.add(sub);
        }
        Collections.reverse(comps);
        return comps;
    }
    
    public String getStyle() throws SplitCharNotFind{
        for(Node n: this.getN().getSubNodes()){
            String type = n.getKey();
            if( type.equals("class") ){
                return n.getVale();
            }
        }
        return "";
    }

    @Override
    public boolean check() {
        return true;
    }

}
