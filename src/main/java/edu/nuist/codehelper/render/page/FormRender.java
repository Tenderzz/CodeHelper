package edu.nuist.codehelper.render.page;

import java.util.HashSet;
import java.util.Map;

import edu.nuist.codehelper.entity.Node;
import edu.nuist.codehelper.exception.SplitCharNotFind;
import lombok.extern.log4j.Log4j2;

@Log4j2
public class FormRender extends DivRender {
    
    public FormRender(
        String redent, HtmlRender hr, CSSRender cr, 
        JSRender jr, HashSet<String> dependThdJS, 
        Node n, Map<String, String> ctx) {
        super(redent, hr, cr, jr, dependThdJS, n, ctx);
    }

    @Override
    public void exec() throws Exception {
       super.exec();
       //将其添加URL属性，打完搞定
        String html = super.getHr().getHtml();
        String divHead = "<div id=\""+ super.getId() +"\" class=\""+ super.getLayout() +"\">";
        String replaced = "<div id=\""+ super.getId() +"\" class=\"form "+ super.getLayout() +"\">";
        html = html.replaceAll(divHead, replaced );
        super.getHr().reset(html);
    }

    //检查有没有COMPONENTS，如果有，有没有提交按键，检查有没有URL,提交接口
    @Override
    public boolean check() {
        boolean hasComponents = false;
        boolean hasSubmitButton = false;
        try {
            for(Node sub: this.getN().getSubNodes()){
                if( sub.getKey().equals("components")){
                    hasComponents = true;
                    for(Node component: sub.getSubNodes()){
                        if( component.getKey().equals("button-form")){
                            hasSubmitButton = true;
                        }
                    }
                }
            } 
        }catch (SplitCharNotFind e) {
            e.printStackTrace();
        }
        
        return hasComponents && hasSubmitButton ;
    }
}
