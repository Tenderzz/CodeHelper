package edu.nuist.codehelper.render.page.componentrender;

import java.io.File;
import java.text.MessageFormat;

import edu.nuist.codehelper.component.FileTemplator;
import edu.nuist.codehelper.utils.FileUtil;
import lombok.extern.log4j.Log4j2;
@Log4j2
public class UploaderComponentSerier extends ComponentRenderInterface{
    public static final String[] shim = new String[]{
        "   'plupload': {",
        "       deps: ['jquery'],",
        "       exports: 'plupload'",       
        "   },"       
    };

    public static final String[] UPLOAD_FILE_SIZE_LIMIT = new String[]{
        "    servlet:",
        "       multipart:",
        "       # 最大支持文件大小",
        "           max-file-size: <!--size-->MB",
        "       # 最大支持请求大小",
        "           max-request-size: <!--size-->MB"
    };

    public static final String THD_JS = "    \"plupload\": 'plupload-2.1.2/js/plupload.full.min',";
    public static final String COMP_EVENTCENTER_JS = "    \"eventcenter\": \"../components/EventCenter\",";
    public static final String COMP_JS = "    \"uploader\": \"../components/Uploader\",";

    public static final String YML = "uploader:\r\n    local:\r\n        base: {0}\r\n";

    private String uploadDir;
    private String limit;

    public static final String ATTR_TAG_DIR_KEY = "upload-target-dir";
    public static final String FILE_SIZE_LIMIT = "file-size-limit";

    public UploaderComponentSerier(String projDir, String pack, String uploadDir , String limit){
        super(projDir, pack);

        this.uploadDir = uploadDir;
        this.limit = limit;
        //第三方组件，包括文件夹、文件
        this.setDependencyThdJS(PLUPLOAD);
        //自定义组件，包括文件夹、文件
        this.setDependencyCompJS(UPLOADER);
    }

    public static final String[] UPLOADER = new String[]{
        "Uploader.js",
        "EventCenter.js"
    };

    public static final String[] PLUPLOAD = new String[]{
        "plupload-2.1.2/",
        "plupload-2.1.2/js/",
        "plupload-2.1.2/js/moxie.js",
        "plupload-2.1.2/js/moxie.min.js",
        "plupload-2.1.2/js/Moxie.swf",
        "plupload-2.1.2/js/Moxie.xap",
        "plupload-2.1.2/js/plupload.dev.js",
        "plupload-2.1.2/js/plupload.full.min.js",
        "plupload-2.1.2/js/plupload.min.js",
        "plupload-2.1.2/license.txt",
        "plupload-2.1.2/readme.md"
    };

    @Override
    public void insertYMLConfig() {
        String yml = MessageFormat.format(YML, this.uploadDir);
        this.insertYML(yml, "uploader:");

        if( this.limit != null){
            String temp = FileUtil.getFileContent(this.getProjDir() + "/" + YML_FILE);
            if( temp.indexOf("max-file-size") != -1) return;
            String limitConfig = "";
            for(String s: UPLOAD_FILE_SIZE_LIMIT){
                limitConfig += s + "\r\n";
            }
            limitConfig = limitConfig.replaceAll("<!--size-->", this.limit);
            temp=temp.replaceAll("<!--upload file size limit-->", "<!--upload file size limit-->\r\n"+limitConfig);
            FileUtil.writeStringToFile(temp, this.getProjDir() + "/" + YML_FILE);
        }
        
    }

    @Override
    public void insertTHDjsForRequire() {
        this.insertRequireCompJS(ComponentRenderInterface.THD_FILE_PLACE, THD_JS);
    }

    @Override
    public void insertShimForRequire() {
        String tmp = "";
        for(String s: shim){
            tmp += s + "\r\n";
        }
        this.insertRequireCompJS(ComponentRenderInterface.SHIM_PLACE, tmp);
    }

    public void insertComponentForRequire() {
        this.insertRequireCompJS(ComponentRenderInterface.COMPONENT_PLACE, COMP_EVENTCENTER_JS);
        this.insertRequireCompJS(ComponentRenderInterface.COMPONENT_PLACE, COMP_JS);
    }

    //拷贝FileUploadController到CONTROLLER目录，还要查看POM依赖,拷贝LocalFileUploader.txt到COMPONENT目录
    @Override
    public void copyOtherFiles() {
        String packdir = this.getPack().replaceAll("\\.", "\\/");
        
        String controller = FileUtil.getFileContent(FileTemplator.read(
            FileTemplator.DEFAULT_TEMP_DIR + "js-components/uploader/FileUploadController.txt"
        ));

        String controllerFile = this.getProjDir() + "/src/main/java/" + packdir + "/controller/FileUploadController.java";
        if( !new File(controllerFile).exists()) {
            controller = controller.replaceAll("<!--pack-->", this.getPack());
            FileUtil.writeStringToFile(controller, controllerFile);
        }

        String localFile = this.getProjDir() + "/src/main/java/" + packdir + "/components/LocalFileUploader.java";
        if( !new File(localFile).exists()) {
            String local = FileUtil.getFileContent(FileTemplator.read(
                FileTemplator.DEFAULT_TEMP_DIR + "js-components/uploader/LocalFileUploader.txt"
            ));
            local = local.replaceAll("<!--pack-->", this.getPack());
            FileUtil.writeStringToFile(local, localFile);
        }     
        
        String uploadIfaceFile = this.getProjDir() + "/src/main/java/" + packdir + "/components/FileUploader.java";
        if( !new File(uploadIfaceFile).exists()) {
            String iFacea = FileUtil.getFileContent(FileTemplator.read(
                FileTemplator.DEFAULT_TEMP_DIR + "js-components/uploader/FileUploader.txt"
            ));
            iFacea = iFacea.replaceAll("<!--pack-->", this.getPack());
            FileUtil.writeStringToFile(iFacea, uploadIfaceFile);
        }  

        this.insertPOM(FileTemplator.getDependency("guava"), "guava");
        this.insertPOM(FileTemplator.getDependency("json"), "fastjson");

        //还有探测下上传的DIR目录是否存在，拷贝CSS所需要的图片
        if( !new File(this.uploadDir).exists()){
            new File(this.uploadDir).mkdirs();
        }

        //拷贝上传动图
        String imgPath = this.getProjDir() + "/src/main/resources/static/img/uploading.gif";
        FileUtil.copyBinFileToSub(
            FileTemplator.read(
                FileTemplator.DEFAULT_TEMP_DIR+"imgs/uploading.gif"), 
            imgPath); 
    }

    @Override
    public boolean exist() {
        //探测下有没有做过这些，如果有就不用做了
        String packdir = this.getPack().replaceAll("\\.", "\\/");
        if(FileTemplator.isExist( 
            this.getProjDir(), 
            this.getProjDir() + "/src/main/java/" + packdir + "/controller/FileUploadController.java") ){
                return true;
        }
        return false;
    }

    
}
