package edu.nuist.codehelper.component;

import java.io.File;
import java.util.regex.Pattern;

import edu.nuist.codehelper.exception.ProjectCreatError;
import lombok.extern.log4j.Log4j2;

@Log4j2
public class ProjectCreator {
    public static final String PACKAGE_PATTERN = "[a-z]+\\.[a-z]+";
    public static final String[] PROJECT_STRUCTURE = new String[]{
        "src",
        "src/main",
        "src/main/java",
        "src/main/resources",
        "src/main/resources/static",
        "src/main/resources/static/css",
        "src/main/resources/static/js",
        "src/main/resources/static/js/pages",
        "src/main/resources/static/js/third-party",
        "src/main/resources/static/js/components",
        "src/main/resources/static/font",
        "src/main/resources/static/img",
        "src/main/resources/templates",
        "src/test",
        "src/test/java"
    };

    /*
     * dir: 项目所在目录
     * packagename: 项目主包，项目名以主包最后一个字段为名
     * 
     */
    public void exec( String dir, String packagename, String port, String db, String dbuser, String dbpw, String dependencies ) throws ProjectCreatError{ 
        if( dir.endsWith("/") ) dir = dir.substring(0, dir.length()-1);

        if( isDirectory( dir) ){
            if( !isEmptyDirectory(dir) ){
                throw new ProjectCreatError(1);
            }else if( !isLegalPackageName(packagename)){
                throw new ProjectCreatError(2);
            }else{
                for(String s : PROJECT_STRUCTURE){
                    makeProjectDir( dir + "/" + s);
                }

                createPackage(dir, packagename);
                FileTemplator.newPom(dir, dependencies);
                FileTemplator.newAppClass(dir + "/src/main/java", packagename);
                FileTemplator.newApplicationYml(dir + "/src/main/resources", port, db, dbuser, dbpw);
                FileTemplator.newTest(dir+"/src/test/java",packagename);
            
            }
        }else{
            throw new ProjectCreatError(0);
        }
    }

    public void createPackage( String dir, String pack ){
        String packdir = pack.replaceAll("\\.", "\\/");
        new File( dir + "/src/main/java/" + packdir).mkdirs();
        new File( dir + "/src/test/java/" + packdir).mkdirs();
    }

    public boolean isDirectory( String dir ){
        return new File(dir).isDirectory() && new File(dir).exists();
    }

    public boolean isEmptyDirectory(String dir){
        return new File(dir).list() != null && new File(dir).list().length == 0;
    }

    public boolean isLegalPackageName(String pake) {
        String PACKAGE_PATTERN = "([a-z]+\\.)+[a-z]+";
        return Pattern.matches(PACKAGE_PATTERN, pake);
    }

    public boolean makeProjectDir(String dir){
        return new File(dir).mkdir();
    }

    

}
