package edu.nuist.codehelper.render.page.componentrender;

import java.io.File;
import java.text.MessageFormat;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import edu.nuist.codehelper.component.FileTemplator;
import edu.nuist.codehelper.component.NodeRenderFactory;
import edu.nuist.codehelper.render.page.PageRender;
import edu.nuist.codehelper.utils.FileUtil;

public class TableFronterHelper {
    private HashMap<String, String> attrs = null;

    private String type;
    private String dir;
    private String id;
    private String url;
    private String pageName;
    private String priKey;
    private String size = "";
    private String pack;
    private Map<String, String> params = new HashMap<>();
    private Map<String, String> heads  = new HashMap<>();

    public TableFronterHelper(String type, HashMap<String, String> attrs){
        this.type = type;
        this.attrs = attrs;
        this.dir = this.attrs.get(NodeRenderFactory.DIR_KEY);
        this.pack = this.attrs.get(NodeRenderFactory.PACKAGE_KEY);
        this.id = this.attrs.get(ComponentRender.ID);
        this.url =  this.attrs.get("url");
        this.pageName = this.attrs.get(PageRender.PAGENAME);
        
        for(Entry<String, String> attr: attrs.entrySet()){
            String key = attr.getKey();
            //发现参数定义
            if( key.startsWith(ComponentRender.SUB_COMP_PREFIX + "param" )){
                key = attr.getValue();
                key = key.substring(key.indexOf(":")+1).trim();
                params.put( key.split(",")[0].trim(), key.split(",")[1].trim());
            }

            //发现字段定义
            if( key.startsWith(ComponentRender.SUB_COMP_PREFIX + "head" ) || key.startsWith(ComponentRender.SUB_COMP_PREFIX + "pri-head" ) ){
                if( key.startsWith(ComponentRender.SUB_COMP_PREFIX + "pri-head" ) ){
                    this.priKey = attr.getValue().split(",")[1].trim();
                }
                key = attr.getValue();
                key = key.substring(key.indexOf(":")+1).trim();
                heads.put( key.split(",")[0].trim(), key.split(",")[1].trim());
                
            }
        }   
    }


    public String html(String c){
        return c.replaceAll("<!--id-->", id)
                .replaceAll("<!--heads-->", getHeads())
                .replaceAll("<!--url-->", url)
                .replaceAll("<!--prikey-->", priKey);

    }
   
    public String js(String c){
        return c;
    }


    public String response(String c){
        return c;
    }

    public static final String[] IMPORT = { 
        "import org.springframework.web.bind.annotation.ResponseBody;",
        "import com.alibaba.fastjson.JSONObject;",
        "import com.alibaba.fastjson.JSONArray;",
        "import org.springframework.web.bind.annotation.RequestParam;"
    };
    
    //修改对应CONTROLLER接口
    public void serizal(){
        String packdir = this.pack.replaceAll("\\.", "\\/");
        String controllerFile = this.dir + "/src/main/java/" + packdir + "/controller/";
        controllerFile += (this.pageName.charAt(0) + "").toUpperCase() + this.pageName.substring(1) + "Controller.java";
        
        String file = FileUtil.getFileContent(controllerFile);

        String methodFile = this.type.equals("table") ? "table.txt":"pagetable.txt";

        if( new File(controllerFile).exists()) {
            String cFacea = FileUtil.getFileContent(FileTemplator.read(
                FileTemplator.DEFAULT_TEMP_DIR + "controller-method/" + methodFile
            ));
            cFacea = cFacea.replaceAll("<!--url-->", this.url)
                     .replaceAll("<!--params-->", getParams())
                     .replaceAll("<!--table-->", this.id);

            file = file.replaceAll("<!--methods-->", "<!--methods-->\r\n    "+cFacea + "\r\n");
            if( file.indexOf("annotation.ResponseBody") == -1){
                file = file.replaceAll("//<!-- imports -->", "//<!-- imports -->\r\n"+IMPORT[0]);
            }
            if( file.indexOf("fastjson.JSONObject") == -1){
                file = file.replaceAll("//<!-- imports -->", "//<!-- imports -->\r\n"+IMPORT[1]);
            }

            if( file.indexOf("fastjson.JSONArray") == -1){
                file = file.replaceAll("//<!-- imports -->", "//<!-- imports -->\r\n"+IMPORT[2]);
            }

            if( !type.equals("table") && file.indexOf("annotation.RequestParam") == -1){
                file = file.replaceAll("//<!-- imports -->", "//<!-- imports -->\r\n"+IMPORT[3]);
            }
            FileUtil.writeStringToFile(file, controllerFile);
        }  
    }

    public static final String HEAD = "<div class=\"cell\" data=\"{1}\">{0}</div>";
    public String getHeads(){
        StringBuffer sb = new StringBuffer();
        for(Entry<String, String> e: heads.entrySet()){
            sb.append( MessageFormat.format(HEAD, e.getKey(), e.getValue())+ "\r\n");
        }
        return sb.toString();
    }

    public String getParams(){
        StringBuffer sb = new StringBuffer();
        for(Entry<String, String> e: params.entrySet()){
            sb.append(  e.getKey() + " " + e.getValue() + ", ");
        }
        String t = sb.toString();
        return t.substring(0, t.length()-2);
    }
        
}
