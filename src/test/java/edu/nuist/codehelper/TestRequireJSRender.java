package edu.nuist.codehelper;

import java.io.File;
import java.util.HashMap;

import org.junit.Test;

import edu.nuist.codehelper.component.FileTemplator;
import edu.nuist.codehelper.component.NodeRenderFactory;
import edu.nuist.codehelper.render.page.RequireJSRender;
import edu.nuist.codehelper.render.page.componentrender.ComponentInterfaceFactory;
import edu.nuist.codehelper.render.page.componentrender.PopComponentSerier;
import edu.nuist.codehelper.render.page.componentrender.UploaderComponentSerier;
import edu.nuist.codehelper.utils.FileUtil;

public class TestRequireJSRender {
    @Test
    public void t(){
        RequireJSRender rjr = new RequireJSRender("d:/codetest");
        rjr.initRequireJS();
    }

    @Test
    public void k(){
        RequireJSRender rjr = new RequireJSRender("d:/codetest");
        rjr.copyDir(PopComponentSerier.LAYER, false,false);
    }

    @Test
    public void testComponentFactory(){
        String type = "uploader";
        HashMap<String, String> attrs = new HashMap<>();
        attrs.put(NodeRenderFactory.DIR_KEY, "d:/codetest");
        attrs.put(NodeRenderFactory.PACKAGE_KEY, "com.nuist.codehelper");
        attrs.put(UploaderComponentSerier.ATTR_TAG_DIR_KEY, "d:/codetest/upload");
        ComponentInterfaceFactory .get(type, attrs).exec();
    } 
    
    @Test
    public void s(){
        //String l = FileUtil.getFileContent(FileTemplator.read(FileTemplator.DEFAULT_TEMP_DIR+ "require.config.txt"));
        String l = FileUtil.getFileContent("d:/t.txt");
        System.out.println(l);
        FileUtil.writeStringToFile(l, "d:/t.txt");
    }


    @Test
    public void dir(){
        //String dir = "D:/work/samples/common/src/main/resources/static/js/third-party/plupload-2.1.2";
        String dir = "D:/work/codehelper/src/main/resources/templates/default-templates/css/font-awesome";
        visitAllDirsAndFiles(new File(dir));
    }

    public void visitAllDirsAndFiles(File dir) {
        if (dir.isDirectory()) {
            String[] children = dir.list();
            for (int i = 0; i < children.length; i++) {
                System.out.println(dir + "/" + children[i]);
                visitAllDirsAndFiles(new File(dir, children[i]));
            }
        }
    }

    @Test
    public void tk(){
        String t = "<i class=\"fa fa-times\" aria-hidden=\"true\"></i> ";
        System.out.println( "fff" + t.replaceAll("<i.*i>", ""));
    }
}
