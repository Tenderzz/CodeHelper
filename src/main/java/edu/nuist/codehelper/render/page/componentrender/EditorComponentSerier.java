package edu.nuist.codehelper.render.page.componentrender;

import java.io.File;

public class EditorComponentSerier extends ComponentRenderInterface{

    public EditorComponentSerier(String projDir, String pack) {
        super(projDir, pack);
        this.setDependencyCompJS(new String[]{"Editor.js"} );
        this.setDependencyThdJS(new String[]{"wangEditor.min.js", "xss.min.js", "i18next.min.js"});
    }

    @Override
    public boolean exist() {
        String editor = this.getProjDir() + "/src/main/resources/static/js/components/Editor.js";
        return new File(editor).exists();
    }

    @Override
    public void insertYMLConfig() {
    }

    

    @Override
    public void insertTHDjsForRequire() {
        return;
    }

    @Override
    public void insertShimForRequire() {
        
    }


    public static final String COMP_JS = "    \"editor\": '../components/Editor',";
    @Override
    public void insertComponentForRequire() {
        this.insertRequireCompJS(ComponentRenderInterface.COMPONENT_PLACE, COMP_JS);
    }

    @Override
    public void copyOtherFiles() {
        
    }
    
}
