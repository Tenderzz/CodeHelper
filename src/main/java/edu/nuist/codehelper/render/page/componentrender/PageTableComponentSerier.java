package edu.nuist.codehelper.render.page.componentrender;

import java.io.File;

import edu.nuist.codehelper.component.FileTemplator;
import edu.nuist.codehelper.utils.FileUtil;

public class PageTableComponentSerier extends ComponentRenderInterface{
    public PageTableComponentSerier(String projDir, String pack) {
        super(projDir, pack);
        this.setDependencyCompJS(new String[]{"PageTable.js"} );
        this.setDependencyThdJS(new String[]{"template.js","pagization.js"});
    }

    @Override
    public boolean exist() {
        return false;
    }

    @Override
    public void insertYMLConfig() {
    }

    public static final String THD_JS = "    \"template\": 'template',";
    public static final String THD_JS1 = "    \"pagization\": 'pagization',";
    @Override
    public void insertTHDjsForRequire() {
        this.insertRequireCompJS(ComponentRenderInterface.THD_FILE_PLACE, THD_JS);
        this.insertRequireCompJS(ComponentRenderInterface.THD_FILE_PLACE, THD_JS1);
    }

    @Override
    public void insertShimForRequire() {
    }

    public static final String CMP_JS = "    \"pagetable\": \"../components/PageTable\",";
    @Override
    public void insertComponentForRequire() {
        this.insertRequireCompJS(ComponentRenderInterface.COMPONENT_PLACE, CMP_JS);
    }

    @Override
    public void copyOtherFiles() {
        String fontcssFile = this.getProjDir() + "/src/main/resources/static/css/font-awesome/css/font-awesome.css";
        if( !new File(fontcssFile).exists()){
            for(String f : ButtonComponentSerier.FONTAWSOME){
                if( f.endsWith("/")) continue;
                String src = FileTemplator.DEFAULT_TEMP_DIR + "/" + f;
                String dst = this.getProjDir() + "/src/main/resources/static/" + f;
                FileUtil.copyBinFileToSub( FileTemplator.read(src), dst);
            }
        }
    }
}
