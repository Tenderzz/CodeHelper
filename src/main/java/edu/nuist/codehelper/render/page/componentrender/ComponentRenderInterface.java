package edu.nuist.codehelper.render.page.componentrender;

import edu.nuist.codehelper.component.FileTemplator;
import edu.nuist.codehelper.render.page.RequireJSRender;
import edu.nuist.codehelper.utils.FileUtil;
import lombok.extern.log4j.Log4j2;

@Log4j2
public abstract class ComponentRenderInterface {

    private String[] dependencyThdJS = null;
    private String[] dependencyCompJS = null;
    private String projDir ;
    private String pack;
    private RequireJSRender rqr = null;

    public ComponentRenderInterface(String projDir, String pack){
        this.projDir = projDir;
        this.pack = pack;
        rqr = new RequireJSRender( projDir  );
    }

    public void exec(){
        //探测下有没有做过这些，如果有就不用做了
        if( this.exist() ) return;
       
        //初始化REQUIRE.JS的环境
        rqr.initRequireJS();

        //调整配置文件
        this.insertYMLConfig(); 

        //在REQUIRE.CONFIG.JS文件中增加所需内容
        this.serialDenpdencyJS(); //拷贝所需要的第三方组件JS，自己的组件JS
        this.checkEventCenter();
        this.insertTHDjsForRequire(); //添加第三方依赖
        this.insertShimForRequire(); //添加SHIM
        this.insertComponentForRequire(); //添加组件依赖
        this.copyOtherFiles(); //拷贝其它所需的文件
    }

    //检查REQUIRE.CONFIG.JS的内容中有没有导入EVENTCETNER, 如果没有则添加
    //EVENTCENTER,是整个项目中必须的
    public void checkEventCenter(){
        String requireconfig = FileUtil.getFileContent(this.projDir + "/" + REQUIRECONFIG_JS);
        if(requireconfig.indexOf(UploaderComponentSerier.COMP_EVENTCENTER_JS) == -1){
            this.insertRequireCompJS(ComponentRenderInterface.COMPONENT_PLACE, UploaderComponentSerier.COMP_EVENTCENTER_JS);
        }
    }

    //rqr拷贝需要的第三方组件包，以及自身的组件JS文件
    public void serialDenpdencyJS(){
        rqr.copyDir(this.getDependencyCompJS(), true, false);
        rqr.copyDir(this.getDependencyThdJS(), false, false);
    }


    public abstract boolean exist();

    public abstract void insertYMLConfig();

    public abstract void insertTHDjsForRequire();

    public abstract void insertShimForRequire();

    public abstract void insertComponentForRequire();
    
    public abstract void copyOtherFiles();

    public static final String REQUIRECONFIG_JS = "src/main/resources/static/js/require.config.js";
    public static final String REQUIRECONFIG_TMP = "require.config.txt";

    public static final String THD_FILE_PLACE = "<!--third-party-->";
    public static final String COMPONENT_PLACE = "<!--component-->";
    public static final String SHIM_PLACE = "<!--shim-->";
    public static final String PAGE_INIT_PLACE = "<!--page init-->";
    
    public void insertRequireCompJS(String token, String c){
        String requireconfig = "";
        
        if( FileTemplator.isExist(this.projDir , REQUIRECONFIG_JS )){
            requireconfig = FileUtil.getFileContent(this.projDir + "/" + REQUIRECONFIG_JS);
        }else{
            requireconfig = FileUtil.getFileContent( FileTemplator.read(FileTemplator.DEFAULT_TEMP_DIR + REQUIRECONFIG_TMP));
        }
        
        //如果REQUIRES.JS中有的话，则直接返回
        if( requireconfig.indexOf(c) != -1) return ;
       
        requireconfig = requireconfig.replaceAll(token, token +"\r\n" +c);
        FileUtil.writeStringToFile(requireconfig, this.projDir + "/" + REQUIRECONFIG_JS);
    }

    public static final String YML_TEMP = "application-yml.txt";
    public static final String YML_PLACE = "<!--insert-->";
    public static final String YML_FILE = "src/main/resources/application.yml";

    public void insertYML( String c, String existToken){
        String yml = "";
        if( FileTemplator.isExist(this.projDir ,  YML_FILE)){
            yml = FileUtil.getFileContent(this.projDir + "/" + YML_FILE);
        }else{
            yml = FileUtil.getFileContent(FileTemplator.read(
                FileTemplator.DEFAULT_TEMP_DIR + YML_TEMP)
            );
        }
        if( yml.indexOf(existToken) != -1) return;
        yml = yml.replaceAll(YML_PLACE, YML_PLACE + "\r\n" + c);
        FileUtil.writeStringToFile(yml, this.projDir +"/"+ YML_FILE);
    }

    public static final String POM_FILE = "pom.xml";
    public static final String POM_PLACE = "<!-- dependency placeholder -->";
    public static final String POM_TEMP = "pom.xml";
    public void insertPOM( String c, String existToken){
        String pom = "";
        if( FileTemplator.isExist(this.projDir ,  POM_FILE)){
            pom = FileUtil.getFileContent(this.projDir + "/" + POM_FILE);
        }else{
            pom = FileUtil.getFileContent( FileTemplator.read( FileTemplator.DEFAULT_TEMP_DIR+POM_TEMP ));
        }
        //如果已经有了
        if( pom.indexOf(existToken) != -1) return ;
        pom = pom.replace(POM_PLACE, POM_PLACE + "\r\n" + c);
        FileUtil.writeStringToFile(pom, this.projDir +"/"+ POM_FILE);
    }

    public String[] getDependencyThdJS() {
        return dependencyThdJS;
    }
    public void setDependencyThdJS(String[] dependencyThdJS) {
        this.dependencyThdJS = dependencyThdJS;
    }
    public String[] getDependencyCompJS() {
        String[] tmp = new String[this.dependencyCompJS.length+1];
        boolean flag = false;
        int index = 0;
        for(String s: dependencyCompJS){
            tmp[ index++] = s;
            if( s.equals("EventCenter.js")){
                flag = true;
            }
        }
        if( !flag ){
            tmp[index] = "EventCenter.js";
            return tmp;
        }else
            return this.dependencyCompJS;
    }
    public void setDependencyCompJS(String[] dependencyCompJS) {
        this.dependencyCompJS = dependencyCompJS;
    }

    public String getPack(){
        return this.pack;
    }
    
    public String getProjDir(){
        return this.projDir;
    }
    

}
