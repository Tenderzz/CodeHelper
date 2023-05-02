package edu.nuist.codehelper.component;

import java.io.File;
import java.io.InputStream;
import java.util.Map;

import org.springframework.core.io.ClassPathResource;


import edu.nuist.codehelper.utils.FileUtil;
import lombok.extern.log4j.Log4j2;

@Log4j2
public class FileTemplator {
    public static final String DEFAULT_TEMP_DIR = "templates/default-templates/";

    public static final Map<String, String> placholders = Map.of(
            "dependencies", "<!-- dependency placeholder -->",
            "pack", "<!--packagename-->",
            "app", "<!--mainclass-->",
            "port", "<!--port-->",
            "db", "<!--db-->",
            "dbuser", "<!--dbuser-->",
            "dbpw", "<!--dbpw-->"
    );

    public static Map<String, String> templates = Map.ofEntries(
        Map.entry("pom", "pom.xml"),
        Map.entry("appclass", "appclass.txt"),
        Map.entry("application-yml", "application-yml.txt"),
        Map.entry("html", "html.txt"),
        Map.entry("dependencies", "dependencies.txt"),
        Map.entry("bean", "bean.txt"),
        Map.entry("repo", "repository.txt"),
        Map.entry("serv", "service.txt"),
        Map.entry("test", "test.txt"),
        Map.entry("controller", "controller.txt"),
        Map.entry("page-js","page-js.txt")
    );

    public static final Map<String, String> othertemplates = Map.of(
        "html", "plainhtml.txt",
        "div", "div.txt"
    );
    public static void newDiv(String dir,String div){
        String c = FileUtil.getFileContent(read(
                DEFAULT_TEMP_DIR + othertemplates.get("div"))
        );
        c=c.replace("<!-- div -->",div);
        FileUtil.writeStringToFile(c,dir + "/src/main/resources/templates/page.html");

    }

    //得到HTML模板
    public static void newHtml(String dir, String fragmentname) {
        String c = FileUtil.getFileContent(read(
                DEFAULT_TEMP_DIR + othertemplates.get("html"))
        );
        c = c.replace("<!-- fragmentname -->", fragmentname);
        FileUtil.writeStringToFile(c, dir + "/src/main/resources/templates/" + fragmentname + ".html");
    }

    //得到COMPONENT模板
    public static String getComponentParts( String compFile ) {
        String c = FileUtil.getFileContent(read(
                DEFAULT_TEMP_DIR + "/"+  compFile)
        );
        return c;
    }

    public static void newPom(String path,  String dependencies) {
        String c = getPom();
        if (dependencies != null) {
            String ph = placholders.get("dependencies");
            c = c.replace(ph, ph + "\r\n" + dependencies);
        }
        FileUtil.writeStringToFile(c, path + "/pom.xml");
    }

    public static void newAppClass(String dir, String pack) {
        String mainClass = pack.substring(pack.lastIndexOf(".") + 1);
        mainClass = mainClass.substring(0, 1).toUpperCase() + mainClass.substring(1) + "App";

        String c = FileUtil.getFileContent(read(
                DEFAULT_TEMP_DIR + templates.get("appclass"))
        );

        c = c.replace(placholders.get("pack"), pack);
        c = c.replace(placholders.get("app"), mainClass);
        //log.info( dir + "/" + pack.replaceAll("\\.", "\\/") +"/"+ mainClass + "App.java");
        FileUtil.writeStringToFile(c, dir + "/" + pack.replaceAll("\\.", "\\/") + "/" + mainClass + ".java");
    }

    public static void newTest(String dir, String pack) {
        String mainClass = pack.substring(pack.lastIndexOf(".") + 1);
        mainClass = mainClass.substring(0, 1).toUpperCase() + mainClass.substring(1) + "App";
        String t = FileUtil.getFileContent(read(
                DEFAULT_TEMP_DIR + templates.get("test"))
        );
        t = t.replace(placholders.get("pack"), pack);
        t = t.replace(placholders.get("app"), mainClass);
        FileUtil.writeStringToFile(t, dir + "/" + pack.replaceAll("\\.", "\\/") + "/" + mainClass + "Test.java");

    }
    public static void newPageJS(String dir, String controllername) {
        String c = FileUtil.getFileContent(read(
            DEFAULT_TEMP_DIR + templates.get("page"))
        );
    }

    public static void newController(String dir, String pack, String controllername) {
        String c = getController();
        c = c.replace("<!-- package -->", "package " + pack + ".controller;");
        c = c.replace("<!-- Pagename -->", controllername.substring(0, 1).toUpperCase() + controllername.substring(1));
        c = c.replace("<!-- pagename -->", controllername);
        controllername = controllername.substring(0, 1).toUpperCase() + controllername.substring(1) + "Controller";
        FileUtil.writeStringToFile(c, dir + "/" + pack.replaceAll("\\.", "\\/") + "/" + "controller" + "/" + controllername + ".java");
    }


    public static void newEntity(String dir, String beanName, String bean, String reps, String serv, String pack) {
        FileUtil.writeStringToFile(bean, dir + "/" + pack.replaceAll("\\.", "\\/") + "/" + "entity" + "/" + beanName + ".java");
        FileUtil.writeStringToFile(reps, dir + "/" + pack.replaceAll("\\.", "\\/") + "/" + "entity" + "/" + beanName + "Reps.java");
        FileUtil.writeStringToFile(serv, dir + "/" + pack.replaceAll("\\.", "\\/") + "/" + "entity" + "/" + beanName + "Service.java");
    }

    public static void newApplicationYml(
            String dir,
            String port,
            String db,
            String dbuser,
            String dbpass) {
        String c = FileUtil.getFileContent(read(
                DEFAULT_TEMP_DIR + templates.get("application-yml"))
        );

        c = c.replace(placholders.get("port"), port == null ? "80" : port);
        c = c.replace(placholders.get("db"), db);
        c = c.replace(placholders.get("dbuser"), dbuser);
        c = c.replace(placholders.get("dbpw"), dbpass);
        FileUtil.writeStringToFile(c, dir + "/" + "application.yml");
    }


    private static String getPom() {
        return FileUtil.getFileContent(read(
                DEFAULT_TEMP_DIR + templates.get("pom"))
        );
    }

    public static String getHtml() {
        return FileUtil.getFileContent(read(
                DEFAULT_TEMP_DIR + templates.get("html"))
        );
    }

    public static String getController() {
        return FileUtil.getFileContent(read(
                DEFAULT_TEMP_DIR + templates.get("controller")
        ));
    }

    public static String getBean() {
        return FileUtil.getFileContent(read(
                DEFAULT_TEMP_DIR + templates.get("bean"))
        );
    }

    public static String getRepo() {
        return FileUtil.getFileContent(read(
                DEFAULT_TEMP_DIR + templates.get("repo"))
        );
    }

    public static String getServ() {
        return FileUtil.getFileContent(read(
                DEFAULT_TEMP_DIR + templates.get("serv"))
        );
    }


    public static InputStream read(String template) {
        try {
            InputStream in = new ClassPathResource(template).getInputStream();
            return in;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    public static String getDependency(String token) {
        String d = FileUtil.getFileContent(read(
                DEFAULT_TEMP_DIR + templates.get("dependencies"))
        );

        d = d.substring(d.indexOf(token + ":") + token.length() + 1);
        return d.substring(0, d.indexOf("]"));
    }

    public static void insert(String file, String placeholder, String c) {
        String f = FileUtil.getFileContent(read(file));
        String ph = placholders.get(placeholder);
        f.replace(ph, ph + "\r\n" + c);
        FileUtil.writeStringToFile(f, file);
    }

    

    public static boolean isExist(String dir, String file) {
        if (dir.endsWith("/")) dir = dir.substring(0, dir.length() - 1);
        return new File(dir + "/" + file).exists();
    }

    
    public static void copyFile(String resource, String dst, boolean isCover){
        //如果不覆盖，而且已经有了目标文件
        if( !isCover && isExist(
            dst.substring(0, dst.lastIndexOf("/")),
            dst.substring(dst.lastIndexOf("/")+1)
        ))  
        return;

        //如果是目录就创建
        if( dst.endsWith("/") ){
            new File(dst).mkdirs();
            return;
        }
        String content = FileUtil.getFileContent(read(resource));
        FileUtil.writeStringToFile( content, dst );
    }

    public static void copyBinFile(String resource, String dst, boolean isCover){
        if( !isCover && isExist(
            dst.substring(0, dst.lastIndexOf("/")),
            dst.substring(dst.lastIndexOf("/")+1)
        ))  
        return;
        
    }

}
