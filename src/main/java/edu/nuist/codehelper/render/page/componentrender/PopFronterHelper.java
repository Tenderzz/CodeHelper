package edu.nuist.codehelper.render.page.componentrender;

import java.io.File;
import java.text.MessageFormat;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map.Entry;

import edu.nuist.codehelper.component.FileTemplator;
import edu.nuist.codehelper.component.NodeRenderFactory;
import edu.nuist.codehelper.entity.Node;
import edu.nuist.codehelper.exception.SplitCharNotFind;
import edu.nuist.codehelper.utils.FileUtil;

public class PopFronterHelper {
    private HashMap<String, String> attrs = null;

    private String dir;
    private String id;
    private String title;
    private String font;
    private List<Node> buttons;

    public PopFronterHelper(HashMap<String, String> attrs){
        this.attrs = attrs;
        this.dir = this.attrs.get(NodeRenderFactory.DIR_KEY);
        this.id = this.attrs.get(ComponentRender.ID);
        this.title =  this.attrs.get("title");
        this.font = this.attrs.get("font") == null ?"" : this.attrs.get("font");
        this.buttons = new LinkedList<>();
        
        for(Entry<String, String> attr: attrs.entrySet()){
            String key = attr.getKey();
            if( key.startsWith(ComponentRender.SUB_COMP_PREFIX + "pop-button" )){
                Node n = new NodeRenderFactory().getNodeFromString(attr.getValue());
                key = key.substring(key.indexOf(ComponentRender.SUB_COMP_PREFIX) + ComponentRender.SUB_COMP_PREFIX.length());
                n.setKey( key.split(",")[0].trim());
                n.setValue( key.split(",")[1].trim());
                buttons.add(n);
            }
        }   
    }
   
    public String js(String c){
        return c;
    }


    public String response(String c){
        return c;
    }

    public String html(){
        //拷贝文件模板
        String tmpHtmlFile = dir + "/src/main/resources/templates/popup/poptemplate.html"; 
        String windSlice = WIND_SLICE.replaceAll("<!--popwind-->",  id);
        windSlice = windSlice.replaceAll("<!--title-->", title);

        String buttonStr = getButtons(id, buttons);

        String iFacea = "";
        if( !new File(tmpHtmlFile).exists()) {
            iFacea = FileUtil.getFileContent(FileTemplator.read(
                FileTemplator.DEFAULT_TEMP_DIR + "js-components/popup/poptemplate.html"
            ));
        }else{
            iFacea = FileUtil.getFileContent(tmpHtmlFile);
        }
        iFacea = iFacea.replaceAll(PopComponentSerier.POPWIND_DFE, PopComponentSerier.POPWIND_DFE +"\r\n"+ windSlice);
        iFacea = iFacea.replaceAll(PopComponentSerier.POPBUT_DFE, PopComponentSerier.POPBUT_DFE +"\r\n"+ buttonStr);

        FileUtil.writeStringToFile(iFacea, tmpHtmlFile);

        //拷贝窗体内容HTML片段
        String htmlSliceFile = dir + "/src/main/resources/templates/popup/"+ id +".html"; 
        String cFacea = FileUtil.getFileContent(FileTemplator.read(
            FileTemplator.DEFAULT_TEMP_DIR + "js-components/popup/popfragment.txt"
        ));
        cFacea = cFacea.replaceAll("<!--title-->", title)
            .replaceAll("<!--popwind-->", id)
            .replaceAll("<!--font-->", font);
        FileUtil.writeStringToFile(cFacea, htmlSliceFile);
        return "";//POP组件没有PAGE页面的HTML
    }

    public static final String WIND_SLICE = 
        "            <div th:if=\"\\${<!--popwind-->}\"  class=\"popcontent\" title=\"<!--title-->\">\r\n" 
       +"                <div  th:include=\"/popup/<!--popwind-->\" id=\"head\" ></div>\r\n"
       +"            </div>\r\n";

    public static final String FOOT_BUTTONS = 
        "            <span th:if=\"\\${<!--popwind-->}\" id=\"<!--buttion id-->\" class=\"popbut cbutton\" >\r\n"
        +"               <i class=\"fa <!--font-->\" aria-hidden=\"true\"></i> \r\n"
        +"               <span><!--buttion text--></span>\r\n"
        +"           </span>\r\n";

    public static final String TEXT_KEY = "text";
    public static final String FONT_KEY = "font";

    public String getButtons(String id, List<Node> buttons){
        StringBuffer sb = new StringBuffer();
        //button的属性：id, text ,font（图标）, 
        for(Node n : buttons){
            String bid = "";
            String text = "";
            String font = "";
            try {
                bid = n.getVale();
                for(Node sub: n.getSubNodes()){
                    if(TEXT_KEY.equals(sub.getKey())){
                        text = sub.getVale();
                    };
                    if(FONT_KEY.equals(sub.getKey())){
                        font = sub.getVale();
                    };
                }
            } catch (SplitCharNotFind e1) {
                e1.printStackTrace();
            }
            
            
            String bt = FOOT_BUTTONS
                .replaceAll("<!--popwind-->", id)
                .replaceAll("<!--buttion id-->", bid)
                .replaceAll("<!--buttion text-->", text)
                .replaceAll("<!--font-->", font);

            if("".equals(font)){
                bt = bt.replaceAll("<i.*i>", "");
            }

            sb.append(bt);
        }

        return sb.toString();
    }
        
}
