package edu.nuist.codehelper.render.page;

import edu.nuist.codehelper.component.FileTemplator;
import lombok.Data;
import lombok.extern.log4j.Log4j2;

/*
 * require.js
 * 要看有没有REQUIRES.JS，如果有，再看有没有这个UPLOADER依赖的第三方JS
 * 如果有第三方JS，再看REUQIERS.JS中有没有引入第三方JS
 * 如果引入，再看有没有自定义的J
 * 
 */
@Log4j2
@Data
public class RequireJSRender {
    private String projDir;
    public static final String THD_JS_PATH = "src/main/resources/static/js/third-party";
    public static final String COMP_JS_PATH = "src/main/resources/static/js/components";
    public static final String JQUERY_JS = "src/main/resources/static/js/third-party/jquery.js";
    public static final String REQUIRE_JS = "src/main/resources/static/js/third-party/require.js";
    public static final String REQUIRECONFIG_JS = "src/main/resources/static/js/require.config.js";

    public static final String JSTEMP_THD_DIR = FileTemplator.DEFAULT_TEMP_DIR + "js/third-party";
    public static final String JSTEMP_COMPONENT_DIR = FileTemplator.DEFAULT_TEMP_DIR + "js/components";

    public static final String REQUIRECONFIG_TMP = "require.config.txt";

    public RequireJSRender(String dir){
        this.projDir = dir;
    }

    //初始化REQUIREJS的必备环境
    public void initRequireJS(){
        FileTemplator.copyFile(JSTEMP_THD_DIR+"/jquery.js",  this.projDir + "/" + JQUERY_JS, false);
        FileTemplator.copyFile(JSTEMP_THD_DIR+"/require.js",  this.projDir + "/" + REQUIRE_JS, false);
        FileTemplator.copyFile(FileTemplator.DEFAULT_TEMP_DIR +  REQUIRECONFIG_TMP,  this.projDir + "/" + REQUIRECONFIG_JS, false);
    }

    //复制第三方组件文件包,是不是自定义组件
    public void copyDir(String[] dirFileList, boolean isComp, boolean flag){
        String targetDir = this.projDir + "/" + THD_JS_PATH ;
        if( isComp )  targetDir = this.projDir + "/" + COMP_JS_PATH ;
        if( dirFileList == null || dirFileList.length == 0) return;
        for(String file: dirFileList){
            String srcDir = JSTEMP_THD_DIR;
            if( isComp ) srcDir = JSTEMP_COMPONENT_DIR;
            FileTemplator.copyFile(srcDir+"/"+file, targetDir + "/" + file , flag);
        }
    }

    
}
