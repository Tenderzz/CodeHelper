package edu.nuist.codehelper.render.page;

import edu.nuist.codehelper.component.FileTemplator;
import edu.nuist.codehelper.component.NodeRenderFactory;
import edu.nuist.codehelper.component.PageCreator;
import edu.nuist.codehelper.entity.Node;
import edu.nuist.codehelper.exception.SplitCharNotFind;
import edu.nuist.codehelper.render.Render;
import edu.nuist.codehelper.utils.FileUtil;
import lombok.Data;
import lombok.extern.log4j.Log4j2;

import java.io.File;
import java.util.Collections;
import java.util.HashSet;
import java.util.Map;

import com.google.common.io.Files;


@Data
@Log4j2
public class PageRender extends Render {
    private String pageName;
    private String title;
    
    private HtmlRender hr = new HtmlRender();
    private CSSRender sr = new CSSRender();
    private JSRender jr = new JSRender();

    private HashSet<String> dependThdJS = new HashSet<>();

    public static final String PAGENAME = "page_name";

    public PageRender(Node n, Map<String, String> ctx) {
        super(n, ctx);
        try {
            this.pageName = n.getVale();
            for( Node sub: n.getSubNodes()){
                if( sub.getKey().equals("title")){
                    this.title = sub.getVale();
                }
            }
            this.getCTX().put(PAGENAME,this.pageName);
        } catch (SplitCharNotFind splitCharNotFind) {
            splitCharNotFind.printStackTrace();
        }
    }

    /*
     * 创建PAGE对应的CONTROLLER
     * 渲染前端的HTML， CSS
     * 渲染前端的JS，初始化各个组件
     * 收获HTML, CSS, PAGE对应的JS
     * 序列化上述文件到指定的目录
    */
    @Override
    public void exec() throws Exception {
        String dir = this.getCTX().get(NodeRenderFactory.DIR_KEY);
        String pack = this.getCTX().get(NodeRenderFactory.PACKAGE_KEY);
        //初始化CONTROLLER目录，并创建CONTROLLER代码文件
        PageCreator cc = new PageCreator( dir, pack );
        cc.exec(pageName);
        Collections.reverse(this.getN().getSubNodes());
        for(Node sub : this.getN().getSubNodes()){
            if( sub.getKey().equals("div") ) {
                DivRender dr = new DivRender("    ", hr, sr, jr, dependThdJS, sub, getCTX());
                dr.exec();
            }else if( sub.getKey().equals("form") ) {
                FormRender fr = new FormRender("    ", hr, sr, jr, dependThdJS, sub, getCTX());
                fr.exec();
            }
        }

        String html = hr.getHtml();
        String css =  sr.rend();
        String comp_init_js =  jr.get(JSRender.COMP_INIT);
        String comp_regist_js = jr.get(JSRender.COMP_EVENT_REGIST);
        String comp_resp_js = jr.get(JSRender.COMP_EVENT_RESPONSE);
        
        serizalHTML(dir, pageName, html, !"".equals(comp_resp_js));
        serizalCSS(dir, pageName, css);
        serizalCSSImport(dir, pageName, sr.getImportCSS());
        serizalJS(dir, pageName, comp_init_js, comp_regist_js, comp_resp_js, dependThdJS, !"".equals(comp_resp_js));
        
        //最后要在REQUIRE.CONFIG.JS中添加页面JS初始化代码
        addPageInit( dir, pageName );
    }

    public static final String[] PAGE_INIT = new String[]{
        "     case '<!--page-->':",
        "       require(['<!--page-->'], function (<!--page-->) {",
        "           new <!--page-->( \\$('body') ).init();",
        "       });",
        "       break;"
    };

    public static final String PAGE_INIT_TOKEN = "<!--page init-->";
    public static final String PAGE_IMPORT_TOKEN = "<!--pages-->";
    public static final String PAGE_IMPORT_JS = "\"<!--page-->\": \"../pages/<!--page-->\", ";
    public void addPageInit(String dir, String pageName ){
        StringBuffer sb = new StringBuffer();
        for(String s : PAGE_INIT){
            sb.append(s + "\r\n");
        }

        String init = sb.toString().replaceAll(PAGE_TOKEN, pageName);
       
        String js = dir + "/src/main/resources/static/js/require.config.js";
        String temp = FileUtil.getFileContent( js );

        temp = temp.replaceAll(PAGE_INIT_TOKEN, PAGE_INIT_TOKEN + "\r\n" + init + "\r\n");
        temp = temp.replaceAll(PAGE_IMPORT_TOKEN, PAGE_IMPORT_TOKEN + "\r\n    " + PAGE_IMPORT_JS.replaceAll(PAGE_TOKEN, pageName) + "\r\n");
       
        FileUtil.writeStringToFile(temp, js);
    }

    public static final String PAGE_TOKEN = "<!--page-->";
    public static final String PAGE_TITLE_TOKEN = "<!--title-->";
    public static final String PAGE_CONTENT_TOKEN = "<!-- div -->";
    public void serizalHTML(String dir,String pageName, String html, boolean hasComp ){
        String temp = FileUtil.getFileContent(
            FileTemplator.read(FileTemplator.DEFAULT_TEMP_DIR + "html.txt")
        );

        temp = temp.replaceAll(PAGE_CONTENT_TOKEN, html);
        temp = temp.replaceAll(PAGE_TITLE_TOKEN, this.title);
        temp = temp.replaceAll(PAGE_TOKEN, pageName);

        if(hasComp){
            temp = took(temp, "hasComponent");
        }else{
            erease(temp, "hasComponent");
        }

        String htmlFile = dir + "/src/main/resources/templates/pages/" + pageName +".html";
        FileUtil.writeStringToFile(temp, htmlFile);
    }

    public void serizalCSSImport(String dir, String pageName,  String importCss){
        String htmlFile = dir + "/src/main/resources/templates/pages/" + pageName +".html";
        String temp = FileUtil.getFileContent(htmlFile);
        temp = temp.replaceAll("<!--css files-->", "<!--css files-->\r\n"+importCss);
        FileUtil.writeStringToFile(temp, htmlFile);
    }

    public void serizalCSS(String dir, String pageName,  String css ){
        String cssFile = dir + "/src/main/resources/static/css/" + pageName +".css";
        FileUtil.writeStringToFile(css, cssFile);
    }


    public static final String IMPORT_COMP_TOKEN = "<!--import js-->";
    public static final String IMPORT_COMP_OBJ_TOKEN = "<!--import js obj-->";
    public static final String COMP_INIT_TOKEN = "<!--init components-->";
    public static final String COMP_REGIST_TOKEN = "<!--components regist-->";
    public static final String COMP_RESPONSE_TOKEN = "<!--components response-->";
    public void serizalJS( 
        String dir, 
        String packName, 
        String initJs, 
        String registJs, 
        String respJs, 
        HashSet<String> dependThdJS,
        boolean hasEvent 
    ){
        String temp = FileUtil.getFileContent(
            FileTemplator.read(FileTemplator.DEFAULT_TEMP_DIR + "page-js.txt")
        );
        temp = temp.replaceAll(PAGE_TOKEN, packName);
        temp = temp.replaceAll(COMP_INIT_TOKEN, initJs);
        temp = temp.replaceAll(COMP_REGIST_TOKEN, registJs);
        temp = temp.replaceAll(COMP_RESPONSE_TOKEN, respJs);
        //需要不需要事件注册中心
        if(hasEvent){
            temp = took(temp, "hasevent");
        }else{
            erease(temp, "hasevent");
        }

        String de = "";
        String obj = "";
        if( dependThdJS != null && dependThdJS.size() > 0){
            for(String s: dependThdJS){
                if(!"".equals(s)){
                    de += "\"" + s + "\", ";
                    obj += s + ", ";
                }
            }
        }

        if(!"".equals(de)) de = de.substring(0, de.length()-2);
        if(!"".equals(obj)) obj = obj.substring(0, obj.length()-2);

        temp = temp.replaceAll(IMPORT_COMP_TOKEN, de);
        temp = temp.replaceAll(IMPORT_COMP_OBJ_TOKEN, obj);

        String htmlFile = dir + "/src/main/resources/static/js/pages/" + pageName +".js";
        FileUtil.writeStringToFile(temp, htmlFile);
    }


    @Override
    public boolean check() {
        return true;
    }
}
