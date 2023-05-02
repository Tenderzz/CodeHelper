package edu.nuist.codehelper.render.page.componentrender;

import edu.nuist.codehelper.component.FileTemplator;
import edu.nuist.codehelper.utils.FileUtil;

public class SelectorComponentSerier extends ComponentRenderInterface{

    public SelectorComponentSerier(String projDir, String pack) {
        super(projDir, pack);
        this.setDependencyCompJS(new String[]{"Selector.js"} );
        this.setDependencyThdJS(new String[]{});
    }

    @Override
    public boolean exist() {
        return false;
    }

    @Override
    public void insertYMLConfig() {
    }

    @Override
    public void insertTHDjsForRequire() {
    }

    @Override
    public void insertShimForRequire() {
        
    }

    public static final String COMP_JS = "    \"selector\": \"../components/Selector\",";
    @Override
    public void insertComponentForRequire() {
        // TODO Auto-generated method stub
        this.insertRequireCompJS(ComponentRenderInterface.COMPONENT_PLACE, COMP_JS );
    }

    @Override
    public void copyOtherFiles() {
        //拷贝上传动图
        String imgPath = this.getProjDir() + "/src/main/resources/static/img/s.png";
        FileUtil.copyBinFileToSub(
            FileTemplator.read(
                FileTemplator.DEFAULT_TEMP_DIR+"imgs/s.png"), 
            imgPath); 
        imgPath = this.getProjDir() + "/src/main/resources/static/img/check-s.png";
        FileUtil.copyBinFileToSub(
            FileTemplator.read(
                FileTemplator.DEFAULT_TEMP_DIR+"imgs/check-s.png"), 
            imgPath); 
    }
    
}
