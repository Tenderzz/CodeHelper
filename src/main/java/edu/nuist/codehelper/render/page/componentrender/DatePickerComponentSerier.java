package edu.nuist.codehelper.render.page.componentrender;

import java.io.File;

import edu.nuist.codehelper.component.FileTemplator;
import edu.nuist.codehelper.utils.FileUtil;

public class DatePickerComponentSerier extends ComponentRenderInterface{

    public DatePickerComponentSerier(String projDir, String pack){
        super(projDir, pack);
        this.setDependencyCompJS(new String[]{
            "DatePicker.js",
            "EventCenter.js"
        });
        this.setDependencyThdJS(PopComponentSerier.LAYER);
    }

    @Override
    public boolean exist() {
        String DatePicker = this.getProjDir() + "/src/main/resources/static/js/components/DatePicker.js";
        return new File(DatePicker).exists();
    }

    @Override
    public void insertYMLConfig() {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void insertTHDjsForRequire() {
        this.insertRequireCompJS(ComponentRenderInterface.THD_FILE_PLACE, PopComponentSerier.THD_JS);
        // TODO Auto-generated method stub
        
    }

    @Override
    public void insertShimForRequire() {
        String tmp = "";
        for(String s: PopComponentSerier.shim){
            tmp += s + "\r\n";
        }
        this.insertRequireCompJS(ComponentRenderInterface.SHIM_PLACE, tmp);
    }

    public static final String COMP_JS = "    \"datepicker\": '../components/DatePicker',";
    @Override
    public void insertComponentForRequire() {
        this.insertRequireCompJS(ComponentRenderInterface.COMPONENT_PLACE, COMP_JS);
    }

    @Override
    public void copyOtherFiles() {
        // TODO Auto-generated method stub
        String layui = this.getProjDir() + "/src/main/resources/static/css/layui.css";
        if( !new File(layui).exists()){
            String src = FileTemplator.DEFAULT_TEMP_DIR + "/css/layui.css"; 
            FileUtil.copyBinFileToSub( FileTemplator.read(src), layui);
        }

        String fontcssFile = this.getProjDir() + "/src/main/resources/static/font/iconfont.ttf";
        if( !new File(fontcssFile).exists()){
            for(String f : FONTAWSOME){
                if( f.endsWith("/")) continue;
                String src = FileTemplator.DEFAULT_TEMP_DIR + "/" + f;
                String dst = this.getProjDir() + "/src/main/resources/static/" + f;
                FileUtil.copyBinFileToSub( FileTemplator.read(src), dst);
            }
        }
    }


    String[] FONTAWSOME = new String[]{
        "font/iconfont.eot",
        "font/iconfont.svg",
        "font/iconfont.ttf",
        "font/iconfont.woff",
        "font/iconfont.woff2"
    };



}
