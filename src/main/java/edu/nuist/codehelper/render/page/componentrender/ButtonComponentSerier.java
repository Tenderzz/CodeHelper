package edu.nuist.codehelper.render.page.componentrender;

import java.io.File;

import edu.nuist.codehelper.component.FileTemplator;
import edu.nuist.codehelper.utils.FileUtil;

public class ButtonComponentSerier extends ComponentRenderInterface{
    public static final String TEXT = "text";
    public static final String ICON = "icon";
    public static final String FONT = "font";
    public static final String TIP = "tip";

    public static final String[] FONTAWSOME = new String[]{
        "css/font-awesome/css/",
        "css/font-awesome/css/font-awesome.css",
        "css/font-awesome/css/font-awesome.min.css",
        "css/font-awesome/fonts/",
        "css/font-awesome/fonts/fontawesome-webfont.eot",
        "css/font-awesome/fonts/fontawesome-webfont.svg",
        "css/font-awesome/fonts/fontawesome-webfont.ttf",
        "css/font-awesome/fonts/fontawesome-webfont.woff",
        "css/font-awesome/fonts/fontawesome-webfont.woff2",
        "css/font-awesome/fonts/FontAwesome.otf",
        "css/font-awesome/HELP-US-OUT.txt",
        "css/font-awesome/less/",
        "css/font-awesome/less/animated.less",
        "css/font-awesome/less/bordered-pulled.less",
        "css/font-awesome/less/core.less",
        "css/font-awesome/less/fixed-width.less",
        "css/font-awesome/less/font-awesome.less",
        "css/font-awesome/less/icons.less",
        "css/font-awesome/less/larger.less",
        "css/font-awesome/less/list.less",
        "css/font-awesome/less/mixins.less",
        "css/font-awesome/less/path.less",
        "css/font-awesome/less/rotated-flipped.less",
        "css/font-awesome/less/screen-reader.less",
        "css/font-awesome/less/stacked.less",
        "css/font-awesome/less/variables.less",
        "css/font-awesome/scss/",
        "css/font-awesome/scss/font-awesome.scss",
        "css/font-awesome/scss/_animated.scss",
        "css/font-awesome/scss/_bordered-pulled.scss",
        "css/font-awesome/scss/_core.scss",
        "css/font-awesome/scss/_fixed-width.scss",
        "css/font-awesome/scss/_icons.scss",
        "css/font-awesome/scss/_larger.scss",
        "css/font-awesome/scss/_list.scss",
        "css/font-awesome/scss/_mixins.scss",
        "css/font-awesome/scss/_path.scss",
        "css/font-awesome/scss/_rotated-flipped.scss",
        "css/font-awesome/scss/_screen-reader.scss",
        "css/font-awesome/scss/_stacked.scss",
        "css/font-awesome/scss/_variables.scss"
    };


    public ButtonComponentSerier(String projDir, String pack, String type, String text){
        super(projDir, pack);
        this.setDependencyCompJS(new String[]{"Button.js", "EventCenter.js"} );
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


    public static final String COMP_JS = "    \"button\": \"../components/Button\",";
    @Override
    public void insertComponentForRequire() {
        this.insertRequireCompJS(ComponentRenderInterface.COMPONENT_PLACE, COMP_JS );
    }

    @Override
    public void copyOtherFiles() {
        String fontcssFile = this.getProjDir() + "/src/main/resources/static/css/font-awesome/css/font-awesome.css";
        if( !new File(fontcssFile).exists()){
            for(String f : FONTAWSOME){
                if( f.endsWith("/")) continue;
                String src = FileTemplator.DEFAULT_TEMP_DIR + "/" + f;
                String dst = this.getProjDir() + "/src/main/resources/static/" + f;
                FileUtil.copyBinFileToSub( FileTemplator.read(src), dst);
            }
        }
        
    }

    
}
