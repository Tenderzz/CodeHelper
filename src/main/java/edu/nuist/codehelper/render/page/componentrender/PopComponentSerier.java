package edu.nuist.codehelper.render.page.componentrender;

import java.io.File;

import edu.nuist.codehelper.component.FileTemplator;
import edu.nuist.codehelper.utils.FileUtil;

public class PopComponentSerier extends ComponentRenderInterface{

    public PopComponentSerier(String projDir, String pack){
        super(projDir, pack);
        this.setDependencyCompJS(new String[]{
            "Popup.js",
            "EventCenter.js"
        });
        this.setDependencyThdJS(LAYER);
    }

    @Override
    public boolean exist() {
        String popJs = this.getProjDir() + "/src/main/resources/static/js/components/Popup.js";
        return new File(popJs).exists();
    }

    @Override
    public void insertYMLConfig() {
        return;
    }

    @Override
    public void insertTHDjsForRequire() {
        this.insertRequireCompJS(ComponentRenderInterface.THD_FILE_PLACE, THD_JS);
    }

    @Override
    public void insertShimForRequire() {
        String tmp = "";
        for(String s: shim){
            tmp += s + "\r\n";
        }
        this.insertRequireCompJS(ComponentRenderInterface.SHIM_PLACE, tmp);
        
    }

    @Override
    public void insertComponentForRequire() {
        this.insertRequireCompJS(ComponentRenderInterface.COMPONENT_PLACE, COMP_JS);
    }

    public static final String POPWIND_DFE = "<!--pop winds-->";
    public static final String POPBUT_DFE = "<!--footer buttons-->";   
    @Override
    public void copyOtherFiles() {
        //拷贝响应的CONTROLLER
        String packdir = this.getPack().replaceAll("\\.", "\\/");
        String controllerFile = this.getProjDir() + "/src/main/java/" + packdir + "/controller/PopController.java";
        if( !new File(controllerFile).exists()) {
            String cFacea = FileUtil.getFileContent(FileTemplator.read(
                FileTemplator.DEFAULT_TEMP_DIR + "js-components/popup/PopController.java"
            ));
            cFacea = cFacea.replaceAll("<!--pack-->", this.getPack());
            FileUtil.writeStringToFile(cFacea, controllerFile);
        }  
    }


    public static final String THD_JS = "    \"layui\": \"layui/layui.all\",";
    public static final String COMP_JS = "    \"pop\": '../components/Popup',";

    public static final String[] shim = new String[]{
        "    'layui': {",
        "        deps: ['jquery'],",
        "        exports: 'layui'",       
        "    },"       
    };

    public static final String[] LAYER = new String[]{
        "layui/css/",
        "layui/css/layui.css",
        "layui/css/layui.mobile.css",
        "layui/css/modules/",
        "layui/css/modules/code.css",
        "layui/css/modules/laydate/",
        "layui/css/modules/laydate/default/",
        "layui/css/modules/laydate/default/laydate.css",
        "layui/css/modules/layer/",
        "layui/css/modules/layer/default/",
        "layui/css/modules/layer/default/icon-ext.png",
        "layui/css/modules/layer/default/icon.png",
        "layui/css/modules/layer/default/layer.css",
        "layui/css/modules/layer/default/loading-0.gif",
        "layui/css/modules/layer/default/loading-1.gif",
        "layui/css/modules/layer/default/loading-2.gif",
        "layui/font/",
        "layui/font/iconfont.eot",
        "layui/font/iconfont.svg",
        "layui/font/iconfont.ttf",
        "layui/font/iconfont.woff",
        "layui/font/iconfont.woff2",
        "layui/images/",
        "layui/images/face/",
        "layui/images/face/0.gif",
        "layui/images/face/1.gif",
        "layui/images/face/10.gif",
        "layui/images/face/11.gif",
        "layui/images/face/12.gif",
        "layui/images/face/13.gif",
        "layui/images/face/14.gif",
        "layui/images/face/15.gif",
        "layui/images/face/16.gif",
        "layui/images/face/17.gif",
        "layui/images/face/18.gif",
        "layui/images/face/19.gif",
        "layui/images/face/2.gif",
        "layui/images/face/20.gif",
        "layui/images/face/21.gif",
        "layui/images/face/22.gif",
        "layui/images/face/23.gif",
        "layui/images/face/24.gif",
        "layui/images/face/25.gif",
        "layui/images/face/26.gif",
        "layui/images/face/27.gif",
        "layui/images/face/28.gif",
        "layui/images/face/29.gif",
        "layui/images/face/3.gif",
        "layui/images/face/30.gif",
        "layui/images/face/31.gif",
        "layui/images/face/32.gif",
        "layui/images/face/33.gif",
        "layui/images/face/34.gif",
        "layui/images/face/35.gif",
        "layui/images/face/36.gif",
        "layui/images/face/37.gif",
        "layui/images/face/38.gif",
        "layui/images/face/39.gif",
        "layui/images/face/4.gif",
        "layui/images/face/40.gif",
        "layui/images/face/41.gif",
        "layui/images/face/42.gif",
        "layui/images/face/43.gif",
        "layui/images/face/44.gif",
        "layui/images/face/45.gif",
        "layui/images/face/46.gif",
        "layui/images/face/47.gif",
        "layui/images/face/48.gif",
        "layui/images/face/49.gif",
        "layui/images/face/5.gif",
        "layui/images/face/50.gif",
        "layui/images/face/51.gif",
        "layui/images/face/52.gif",
        "layui/images/face/53.gif",
        "layui/images/face/54.gif",
        "layui/images/face/55.gif",
        "layui/images/face/56.gif",
        "layui/images/face/57.gif",
        "layui/images/face/58.gif",
        "layui/images/face/59.gif",
        "layui/images/face/6.gif",
        "layui/images/face/60.gif",
        "layui/images/face/61.gif",
        "layui/images/face/62.gif",
        "layui/images/face/63.gif",
        "layui/images/face/64.gif",
        "layui/images/face/65.gif",
        "layui/images/face/66.gif",
        "layui/images/face/67.gif",
        "layui/images/face/68.gif",
        "layui/images/face/69.gif",
        "layui/images/face/7.gif",
        "layui/images/face/70.gif",
        "layui/images/face/71.gif",
        "layui/images/face/8.gif",
        "layui/images/face/9.gif",
        "layui/lay/",
        "layui/lay/modules/",
        "layui/lay/modules/carousel.js",
        "layui/lay/modules/code.js",
        "layui/lay/modules/colorpicker.js",
        "layui/lay/modules/element.js",
        "layui/lay/modules/flow.js",
        "layui/lay/modules/form.js",
        "layui/lay/modules/jquery.js",
        "layui/lay/modules/laydate.js",
        "layui/lay/modules/layedit.js",
        "layui/lay/modules/layer.js",
        "layui/lay/modules/laypage.js",
        "layui/lay/modules/laytpl.js",
        "layui/lay/modules/mobile.js",
        "layui/lay/modules/rate.js",
        "layui/lay/modules/slider.js",
        "layui/lay/modules/table.js",
        "layui/lay/modules/transfer.js",
        "layui/lay/modules/tree.js",
        "layui/lay/modules/upload.js",
        "layui/lay/modules/util.js",
        "layui/layui.all.js",
        "layui/layui.js"
    };

  
  
}
