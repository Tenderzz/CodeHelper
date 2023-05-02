package edu.nuist.codehelper.component;

import lombok.Data;
import lombok.extern.log4j.Log4j2;

import java.io.File;

@Log4j2
@Data
public class PageCreator {
    private String dir;
    private String packagename;

    private String cssFileStr;
    private String jsFileStr;
    private String htmlStr;

    private PageFileHelper cFileHelper;

    public PageCreator(String dir, String packagename){
        this.dir = dir;
        this.packagename = packagename;
        this.cFileHelper = new PageFileHelper(dir, packagename);
    }

    public void exec(String controllername) {
        String packdir = packagename.replaceAll("\\.", "\\/");
        if (!FileTemplator.isExist( packdir, "")) {
            createControllerPackage(dir, packagename);
        }
        //创建一个CONTROLLER
        FileTemplator.newController(dir + "/src/main/java", packagename, controllername);
 
    }

    public void createHtml(String title, String page){
        String html = FileTemplator.getHtml();
        html.replaceAll("<!--title-->", title);
        html.replaceAll("<!--page-->", page);
        this.cFileHelper.serizalHtml( html );
    }   

    public String createControllerPackage(String dir, String pack) {
        String packdir = pack.replaceAll("\\.", "\\/");
        new File(dir + "/src/main/java/" + packdir + "/controller").mkdirs();
        new File(dir + "/src/main/java/" + packdir + "/components").mkdirs();
        return dir + "/src/main/java/" + packdir + "/controller";
    }
}
