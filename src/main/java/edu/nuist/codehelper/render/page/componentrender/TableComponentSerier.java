package edu.nuist.codehelper.render.page.componentrender;

import java.util.HashMap;


public class TableComponentSerier  extends ComponentRenderInterface{

    public TableComponentSerier(String projDir, String pack) {
        super(projDir, pack);
        this.setDependencyCompJS(new String[]{"Table.js"} );
        this.setDependencyThdJS(new String[]{"template.js"});
    }

    @Override
    public boolean exist() {
        return false;
    }

    @Override
    public void insertYMLConfig() {
    }

    public static final String THD_JS = "    \"template\": 'template',";
    @Override
    public void insertTHDjsForRequire() {
        this.insertRequireCompJS(ComponentRenderInterface.THD_FILE_PLACE, THD_JS);
    }

    @Override
    public void insertShimForRequire() {
    }

    public static final String CMP_JS = "    \"table\": \"../components/Table\",";
    @Override
    public void insertComponentForRequire() {
        this.insertRequireCompJS(ComponentRenderInterface.COMPONENT_PLACE, CMP_JS);
    }

    @Override
    public void copyOtherFiles() {

    }
    
}
