package edu.nuist.codehelper.render.page.componentrender;

import java.io.File;

import edu.nuist.codehelper.component.FileTemplator;
import edu.nuist.codehelper.utils.FileUtil;

public class DownloaderComponentSerier extends ComponentRenderInterface{

    private String url = "";

    public static final String DOWNLOAD_URL = "url";

    public DownloaderComponentSerier(String projDir, String pack, String url){
        super(projDir, pack);
        this.setDependencyCompJS(DOWNLOADER);
        this.setDependencyThdJS(new String[]{});
        this.url = url;
    }

    public static final String[] DOWNLOADER = new String[]{
        "Downloader.js"
    };

    @Override
    public boolean exist() {
        String js = this.getProjDir() + "/src/main/resources/static/js/components/Downloader.js"; 
        return new File(js).exists();
    }

    public static final String COMP_JS = "    \"downloader\": \"../components/Downloader\",";
    @Override
    public void insertComponentForRequire() {
        this.insertRequireCompJS(ComponentRenderInterface.COMPONENT_PLACE, COMP_JS);
    }

    @Override
    public void insertYMLConfig() {
        return ;
    }
    @Override
    public void insertTHDjsForRequire() {
        return ;
    }
    @Override
    public void insertShimForRequire() {   
        return ;
        
    }
    @Override
    public void copyOtherFiles() {
        String packdir = this.getPack().replaceAll("\\.", "\\/");
        
        String controllerFile = this.getProjDir() + "/src/main/java/" + packdir + "/controller/FileDownloadController.java";
        if( !new File(controllerFile).exists()) {
            String controller = FileUtil.getFileContent(FileTemplator.read(
                FileTemplator.DEFAULT_TEMP_DIR + "js-components/downloader/FileDownloadController.txt"
            ));
            controller = controller.replaceAll("<!--pack-->", this.getPack());
            controller = controller.replaceAll("<!--url-->", this.url);
            FileUtil.writeStringToFile(controller, controllerFile);
        } 
        return ;
    }
    
}
