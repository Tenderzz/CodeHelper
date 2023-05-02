package edu.nuist.codehelper.component;

import java.io.File;
import java.util.Map;

import edu.nuist.codehelper.utils.FileUtil;
import lombok.Data;

@Data
public class PageFileHelper {
    private String dir;
    private String packagename;

    private String controllerPackPath;
    private String controllerFile;
    private String cssFilePath; //page的JS路径
    private String cssFile;//page的CSS
    private String jsFilePath; //page的JS路径
    private String jsFile; //page的JS路径
    private String widgetPath;
    private String requireJS;
    private String htmlFilePath;
    private String htmlFile;
    
    public PageFileHelper(String dir, String packagename){
        this.dir = dir.endsWith("/") ? dir.substring(0, dir.length()-1): dir;
        this.packagename = packagename;

        this.controllerPackPath = dir + "/" + getPack();
        this.controllerFile = this.controllerPackPath + "/" + getControllerClassName() + ".java";
        this.cssFilePath = dir + "/src/main/resources/static/css";
        this.cssFile = this.getCssFile();
        this.jsFilePath = this.getJSFilePath();
        this.jsFile = this.getJSFilePath() + this.getControllerClassName() + ".js";
        //组件JS文件所在目录
        this.widgetPath = dir + "/src/main/resources/static/js/components";

        //require.js
        this.requireJS = dir + "/src/main/resources/static/js/require.config.js";
        this.htmlFile = this.getHtmlFile();
        this.htmlFilePath = this.getHtmlFilePath();

        this.initPaths();
    }

    public void serizalHtml(String html){
        FileUtil.writeStringToFile(html, this.htmlFile);
    }

    public void initPaths(){
        String[] paths = new String[]{
            this.cssFilePath,
            this.htmlFilePath,
            this.jsFilePath
        };
        
        for(String p : paths){
            if( !FileTemplator.isExist(p, "")){
                new File(p).mkdirs();
            };
        }
    }

    public String getPack(){
        return this.packagename.replaceAll("\\.", "\\/");
    }

    public String getCssFile(){
        return dir + "/src/main/resources/static/css/" + getControllerClassName().toLowerCase() + ".css";
    }

    public String getJSFilePath(){
        return dir + "/src/main/resources/static/js/pages" ;
    }

    public String getControllerClassName(){
        String className = this.packagename.substring(this.packagename.lastIndexOf("\\.")+1);
        return className.toUpperCase().charAt(0) + className.substring(1);
    }

    public String getHtmlFilePath(){
        return dir + "/src/main/resources/templates/pages";
    }

    public String getHtmlFile(){
        return dir + "/src/main/resources/templates/pages/" + getControllerClassName().toLowerCase() +".html";
    }
    
}
