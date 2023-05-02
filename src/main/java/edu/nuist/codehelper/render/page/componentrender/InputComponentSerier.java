package edu.nuist.codehelper.render.page.componentrender;

import java.io.File;

public class InputComponentSerier extends ComponentRenderInterface{

    public InputComponentSerier(String projDir, String pack) {
        super(projDir, pack);
        this.setDependencyCompJS(new String[]{
            "DataVerifier.js",
            "InputWidgets.js"
        });
        this.setDependencyThdJS(new String[]{});
    }

    @Override
    public boolean exist() {
        String popJs = this.getProjDir() + "/src/main/resources/static/js/components/DataVerifier.js";
        return new File(popJs).exists();
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
    
    public static final String COMP_JS = "    \"dataverifier\": '../components/DataVerifier',";
    public static final String WIDGETS_JS = "    \"inputwidgets\": '../components/InputWidgets',";
    @Override
    public void insertComponentForRequire() {
        this.insertRequireCompJS(ComponentRenderInterface.COMPONENT_PLACE, COMP_JS);
        this.insertRequireCompJS(ComponentRenderInterface.COMPONENT_PLACE, WIDGETS_JS);
        
    }

    @Override
    public void copyOtherFiles() {
        
    }
    
}
