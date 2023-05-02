package edu.nuist.codehelper.render.page.componentrender;

import java.io.File;

public class FormButtonComponentSerier extends ComponentRenderInterface{

    public FormButtonComponentSerier(String projDir, String pack, String text){
        super(projDir, pack);
        this.setDependencyCompJS(new String[]{"FormButton.js", "EventCenter.js"} );
        this.setDependencyThdJS(new String[]{});
    }

    @Override
    public boolean exist() {
        String popJs = this.getProjDir() + "/src/main/resources/static/js/components/FormButton.js";
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

    public static final String COMP_JS = "    \"formbutton\": \"../components/FormButton\",";
    @Override
    public void insertComponentForRequire() {
        this.insertRequireCompJS(ComponentRenderInterface.COMPONENT_PLACE, COMP_JS );
    }

    @Override
    public void copyOtherFiles() {
        //序列化一下FONTAWSOME
        ButtonComponentSerier bcs = new ButtonComponentSerier(this.getProjDir(), this.getPack(), "", "");
        bcs.copyOtherFiles();
    }
    
}
