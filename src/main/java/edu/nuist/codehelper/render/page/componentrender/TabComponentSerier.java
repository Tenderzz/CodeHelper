package edu.nuist.codehelper.render.page.componentrender;

public class TabComponentSerier extends ComponentRenderInterface{

    public TabComponentSerier(String projDir, String pack) {
        super(projDir, pack);
        this.setDependencyCompJS(new String[]{"Tab.js"} );
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

    public static final String CMP_JS = "    \"tab\": \"../components/Tab\",";
    @Override
    public void insertComponentForRequire() {
        this.insertRequireCompJS(ComponentRenderInterface.COMPONENT_PLACE, CMP_JS);
    }

    @Override
    public void copyOtherFiles() {

    }
    
}
