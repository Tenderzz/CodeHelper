package edu.nuist.codehelper.render.page.componentrender;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import edu.nuist.codehelper.component.FileTemplator;
import edu.nuist.codehelper.entity.Node;
import edu.nuist.codehelper.exception.ComponentNotFind;
import edu.nuist.codehelper.render.page.CSSRender;
import edu.nuist.codehelper.render.page.HtmlRender;
import edu.nuist.codehelper.render.page.JSRender;
import lombok.Data;
import lombok.extern.log4j.Log4j2;

/*
 * component, 包括：
 *  js, 有部分COMPONENT的JS代码可能合成一个JS文件
 *  css, 有组件的CSS，也有第三方模块的CSS
 *  html， 页面内容
 *  yml：配置项
 *  js：依赖第三方模块
 *    ：自己的组件
 *    ：还有一部分初始化的代码要插入到PAGE的JS中
 *    ：还有JS的依赖项，要汇总到PAGE前的依赖
 *  requires.js 
 *    : 要在其中插入COMPONENT的依赖第三方
 *    ：要在其中插入组件
 */
@Log4j2
@Data
public class ComponentRender {
    private HtmlRender hr;
    private CSSRender cr;
    private JSRender jr;
    private HashSet<String> dependThdJS;
    private Node n;
    private Map<String, String> ctx;

    private String type;
    private String redent;
    private String dir;
    private String pack;

    public static final String ID = "id";
    public static final String HTML_TOKEN = "html";
    public static final String CSS_IMPORT_TOKEN = "stylesheet";
    public static final String CSS_TOKEN = "css";
    public static final String JS_TOKEN = "js";
    public static final String DEPENDENCY_TOKEN = "dependency";
    public static final String COMP_EVENT_RESP_TOKEN = "event-resp";
    public static final String COMP_EVENT_REGIST_TOKEN = "event-regist";

    public static final Map<String, String> COMP_DEFINE = Map.ofEntries(
        Map.entry("uploader", "js-components/uploader/uploader.txt"),
        Map.entry("downloader", "js-components/downloader/downloader.txt"),
        Map.entry("button-small", "js-components/button/button.txt"),
        Map.entry("button", "js-components/button/button.txt"),
        Map.entry("button-big", "js-components/button/button.txt"),
        Map.entry("button-icon", "js-components/button/button.txt"),
        Map.entry("popup", "js-components/popup/popup.txt"),
        Map.entry("pop-button", ""),
        Map.entry("input", "js-components/input/input.txt"),
        Map.entry("downlist", "js-components/input/downlist.txt"),
        Map.entry("autocomplete", "js-components/input/downlist.txt"),
        Map.entry("datepicker", "js-components/input/datepicker.txt"),
        Map.entry("selector", "js-components/input/selector.txt"),
        Map.entry("multi-selector", "js-components/input/selector.txt"),
        Map.entry("sel-item", ""),
        Map.entry("editor", "js-components/editor/editor.txt"),
        Map.entry("button-form", "js-components/button/form-button.txt"),
        Map.entry("table", "js-components/table/table.txt"),
        Map.entry("pri-head", ""),
        Map.entry("head", ""),
        Map.entry("param", ""),
        Map.entry("page-table", "js-components/table/page-table.txt"),
        Map.entry("tab", "js-components/tab/tab.txt"),
        Map.entry("tab-li", "")
    );

    private ComponentFronterHelper cHelper = null;

    public ComponentRender(
        Map<String, String> ctx, 
        Node n, 
        String redent, 
        HtmlRender hr, 
        CSSRender cr, 
        JSRender jr, //组件本身初始化代码的存放 
        HashSet<String> dependThdJS, 
        String type
    ){
        this.hr = hr;
        this.cr = cr;
        this.jr = jr;
        this.type = type;
        this.redent = redent;
        this.dependThdJS = dependThdJS;
        this.n = n;
        this.ctx = ctx;
    }

    /**
     * 1. 取出组件的所有属性
     * 2. 取出组件模块
     * 3. 利用属性分别对取出HTML, CSS, JS进行渲染
     * 4. 探测是否JS组件已经拷贝过了，如果没有拷贝
     */
    public void exec(){
        exec( true ); //默认是要做组件的后端代码序列化的
    }

    public static final String SUB_COMP_PREFIX = "subcomp-";

    public void exec( boolean needSerizal ){
        try{
            //已有环境，加上本组件节点的属性
            HashMap<String, String> attrs = new HashMap<>(); 
            attrs.putAll(this.ctx);
            for(Node sub: n.getSubNodes()){
                //如果当前的节点属性是一个组件，则还原成文本，再交由相关逻辑处理
                if( COMP_DEFINE.get( sub.getKey()) != null){
                    attrs.put(SUB_COMP_PREFIX+ sub.getKey() +","+  sub.getVale(), Node.getNodeRaw(sub, ""));
                }else{
                    attrs.put(sub.getKey(), sub.getVale());
                }
            }
            //放入ID
            attrs.put(ID, this.n.getVale());

            ComponentFronterHelper cHelper = new ComponentFronterHelper(attrs, type);
            if( COMP_DEFINE.get(this.type) == null) throw new ComponentNotFind();
            String c = FileTemplator.getComponentParts( COMP_DEFINE.get(this.type) );
           
            String html = cHelper.exec(getSlice(HTML_TOKEN, c), ComponentFronterHelper.HTML) ;
            String css = cHelper.exec(getSlice(CSS_TOKEN, c), ComponentFronterHelper.CSS) ;
            String js = cHelper.exec(getSlice(JS_TOKEN, c), ComponentFronterHelper.JS) ;
            String[] dependencies = getSlice(DEPENDENCY_TOKEN, c).split(",");
            String eventResp =  cHelper.exec(getSlice(COMP_EVENT_RESP_TOKEN, c), ComponentFronterHelper.RESP);
            String eventRegist =  cHelper.exec(getSlice(COMP_EVENT_REGIST_TOKEN, c), ComponentFronterHelper.REGIST);


            hr.append(html);
            //因为一个页面中可能会出现多个同类的组件，会造成CSS定义重复，所以用MAP来设置，根据组件类型
            //放到MAP中，达到去重，可能不同类型的组件（如各种BUTTON）会共用CSS，所以在cHelper中转一下
            String css_import = getSlice(CSS_IMPORT_TOKEN, c);
            if( !"".equals(css_import) ) cr.addImportCss(css_import);
            cr.addComponentCSS(cHelper.getCssType(), css);
            /*
             * 这里第一，要把JS的组件、配置这些搞完，主要是使用COMPONENTRENDINTERFACE来弄
             * 第二，要把组件的配置TXT文中JS片段，渲染好之后，再添加到PAGE的JS文件中，组件的初始化
             */
            jr.append(JSRender.COMP_INIT, js);
            jr.append(JSRender.COMP_EVENT_REGIST, eventRegist);
            jr.append(JSRender.COMP_EVENT_RESPONSE, eventResp);
            //添加依赖组件
            for(String d: dependencies){
                dependThdJS.add( d.trim() );
            }

            //组件序列化, 标志控制是否需要序列化
            if(needSerizal){
                ComponentRenderInterface cif = ComponentInterfaceFactory.get(type, attrs);
                cif.exec();
            }

            //到此，组件渲染完成，接下来就是返回PAGE处理了
        }catch(Exception e){
            e.printStackTrace();
            log.error(e.getMessage());
        }        
    }

    private String getSlice(String token, String c){
        Pattern pattern = Pattern.compile("(\\["+token+"([\\s\\S]*?)\\])");        
        Matcher matcher = pattern.matcher(c);
        String tmp = "";
        if(matcher.find()) {
           String all =  matcher.group();
           tmp = all.substring(all.indexOf(":")+1, all.lastIndexOf("]"));
           String[] lines = tmp.split("\r\n");

           String redent = "    ";
           if( token.equals(HTML_TOKEN)) redent = this.redent; 
           if( token.equals(CSS_TOKEN)) redent = ""; 
           StringBuffer sb = new StringBuffer();
           for(String line: lines){
                sb.append(redent + line + "\r\n");
           }
           return sb.toString();
        }
        return "";
    }


}
