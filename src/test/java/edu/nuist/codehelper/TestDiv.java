package edu.nuist.codehelper;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import org.junit.Test;

import edu.nuist.codehelper.component.NodeRenderFactory;
import edu.nuist.codehelper.component.TreeConstructor;
import edu.nuist.codehelper.component.YMLParser;
import edu.nuist.codehelper.entity.Node;
import edu.nuist.codehelper.entity.NodeWrapper;
import edu.nuist.codehelper.render.page.CSSRender;
import edu.nuist.codehelper.render.page.DivRender;
import edu.nuist.codehelper.render.page.HtmlRender;
import edu.nuist.codehelper.render.page.JSRender;
import edu.nuist.codehelper.render.page.componentrender.ButtonComponentSerier;
import edu.nuist.codehelper.render.page.componentrender.UploaderComponentSerier;

public class TestDiv {
    @Test
    public void testTable(){ 
        String[] page = new String[]{
            "project: ",
            "  dir: d:/codetest9", //7
            "  package-name: edu.nuist.code",
            "  db-name: test",
            "  db-user: root",
            "  db-password: Ly20020622.123",
            "  port: 8080",
            "  dependencies: ",
            "    hutools: ",
            "    json: ",    
            "page: inputtest",
            "   title: 测试输入控件",
            "   div: zzz",
            "       components: ",
            "           tab: testtab",
            "               tab-li: 第一页, first",     // 卡片页的文字与对应DIV的ID 
            "               tab-li: 第二页, sec",     //     
            "               tab-li: 第三页, thrd",     // 
            "       div: first", //卡片页对应的DIV
            "       div: sec",        //卡片页对应的DIV
            "       div: thrd",        //卡片页对应的DIV
            "   div: container",
            "        components: ",
            "           table: firsttable",//普通表格
            "               url: /test/firsttable",    //定义的数据接口，必须要有
            "               param: String, key",    //定义表的提交参数，可以没有,分成二部分，前为参数类型，后参数名
            "               param: long, id",    //定义表的提交参数，可以没有           
            "               pri-head: 序号, id",    //这个字段是表中各行的唯一标识，这个必须要有, 将会把这部分的值放到ROW的DIV属性中
            "               head: 字段1, name",    //定义的表头，必须至少有一个，分成二部分，前为表头文字，后面表头对应返回JSON数据的KEY
            "               head: 字段2, frt",    //
            "               head: 字段3, sec",    //
            "               head: 字段4, thr",    //
            "           page-table: pagetable",//分页表
            "               url: /test/pagetable",    //定义的数据接口，必须要有
            "               param: String, key",    //定义表的提交参数，可以没有,分成二部分，前为参数类型，后参数名
            "               param: long, id",    //定义表的提交参数，可以没有           
            "               pri-head: 序号, id",    //这个字段是表中各行的唯一标识，这个必须要有, 将会把这部分的值放到ROW的DIV属性中
            "               head: 字段1, name",    //定义的表头，必须至少有一个，分成二部分，前为表头文字，后面表头对应返回JSON数据的KEY
            "               head: 字段2, frt",    //
            "               head: 字段3, sec",    //
            "               head: 字段4, thr",    //
        };
        try {
            NodeRenderFactory nf = new NodeRenderFactory();
            YMLParser parser = new YMLParser();
            TreeConstructor tree = new TreeConstructor();
            for (String s : page) {
                parser.parser(s);
            }
            List<NodeWrapper> rst = parser.get();
            Node d = tree.exec(rst);
            Collections.reverse(d.getSubNodes());
            for(Node sub : d.getSubNodes()){
                //System.out.println( Node.getNodeRaw(sub, "")); //测试将节点序列化
                nf.rend(sub);
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(e.getMessage());
        }
    }

    @Test
    public void test1Page(){ 
        String[] page = new String[]{
            "project: ",
            "  dir: d:/codetest",
            "  package-name: edu.nuist.code",
            "  db-name: qdb",
            "  db-user: root",
            "  db-password: hanjinhao",
            "  port: 8080",
            "  dependencies: ",
            "    hutools: ",
            "    json: ",    
            "page: inputtest",
            "   title: 测试输入控件",
            "   div: container",
            "        components: ",
            "           datepicker: firstdate",//下拉列表
            "               placeholder: 第一个日期选择",    //可以没有
            "           datepicker: secdate",//下拉列表
            "               placeholder: 第二个日期选择",    //可以没有
            "           selector: s2",//多选框
            "               sel-item: select1, 1",//选项：显示文字，值 
            "               sel-item: select2, 2",//选项
            "               sel-item: select3, 3",//选项
            "           multi-selector: s1",//下拉列表
            "               sel-item: select1, 1",//选项：显示文字，值 
            "               sel-item: select2, 2",//选项
            "               sel-item: select3, 3",//选项
            "           editor: first",//文本框,
            "               height: 240",//高度，整数
            "   form: testform",
            "        url: /test/testform",
            "        class: testform",
            "        components: ",
            "           input: firstinput",
            "               placeholder: 只能输入整数",    //可以没有
            "               label: 输入小数的时候会出错",//可以没有
            "               value: 10", //可以没有
            "               verifier: int",        //可以没有,校验种类：email、'nonull' 'int' 'double': 'date' 'year':       
            "           input: secinput",           
            "           downlist: thrinput",//下拉列表
            "               url: /inputtest/testdown",    //获取列表内容的接口地址，返回JSON数组，每个元素包括{item: , value:},ITEM是选项显示内容，VALUE是选项的值，这个选项可以是ITEMS， URL，如果二个都有，则会混合在一起
            "               placeholder: 使用下拉列表",    //可以没有
            "           downlist: firinput",
            "               items: [第一个选项, 第二个选项, 第三个选项]",    //获取列表内容的接口地址，返回数组，每个元素包括{item: , value:},ITEM是选项显示内容，VALUE是选项的值，这个选项可以是ITEMS， URL，如果二个都有，则会混合在一起
            "               placeholder: 使用下拉列表",    //可以没有
            "           autocomplete: fiveinput",
            "               url: /inputtest/autocomplete",    //只接收从URL获取，返回JSON数组，每个元素包括{item: , value:},ITEM是选项显示内容，VALUE是选项的值
            "               placeholder: 使用AutoComplete",    //可以没有
            "           button-form: submit", //form的提交按键，必须要有 
            "               text: submit",      
            "               font: fa-paper-plane",  //font属性可以不设置   
        };
            try {
                NodeRenderFactory nf = new NodeRenderFactory();
                YMLParser parser = new YMLParser();
                TreeConstructor tree = new TreeConstructor();
                for (String s : page) {
                    parser.parser(s);
                }
                List<NodeWrapper> rst = parser.get();
                Node d = tree.exec(rst);
                Collections.reverse(d.getSubNodes());
                for(Node sub : d.getSubNodes()){
                    //System.out.println( Node.getNodeRaw(sub, "")); //测试将节点序列化
                    nf.rend(sub);
                }
            } catch (Exception e) {
                e.printStackTrace();
                System.out.println(e.getMessage());
            }
        }
    
    @Test
    public void testPage(){ 
        String[] page = new String[]{
            "project: ",
            "  dir: d:/codetest",
            "  package-name: edu.nuist.code",
            "  db-name: qdb",
            "  db-user: root",
            "  db-password: hanjinhao",
            "  port: 8080",
            "  dependencies: ",
            "    hutools: ",
            "    json: ",    
            "page: inputtest",
            "   title: 测试输入控件",
            "   div: container",
            "        components: ",
            "           input: firstinput",
            "               placeholder: 只能输入整数",    //可以没有
            "               label: 输入小数的时候会出错",//可以没有
            "               value: 10", //可以没有
            "               verifier: int",        //可以没有       
            "           input: secinput",           
            "           downlist: thrinput",//下拉列表
            "               url: /inputtest/testdown",    //获取列表内容的接口地址，返回JSON数组，每个元素包括{item: , value:},ITEM是选项显示内容，VALUE是选项的值，这个选项可以是ITEMS， URL，如果二个都有，则会混合在一起
            "               placeholder: 使用下拉列表",    //可以没有
            "           downlist: firinput",
            "               items: [第一个选项, 第二个选项, 第三个选项]",    //获取列表内容的接口地址，返回数组，每个元素包括{item: , value:},ITEM是选项显示内容，VALUE是选项的值，这个选项可以是ITEMS， URL，如果二个都有，则会混合在一起
            "               placeholder: 使用下拉列表",    //可以没有
            "           autocomplete: fiveinput",
            "               url: /inputtest/autocomplete",    //只接收从URL获取，返回JSON数组，每个元素包括{item: , value:},ITEM是选项显示内容，VALUE是选项的值
            "               placeholder: 使用AutoComplete",    //可以没有
            "page: test2",
            "   title: 测试输入控件",
            "   div: container",
            "        components: ",
            "           datepicker: firstdate",//下拉列表
            "               placeholder: 第一个日期选择",    //可以没有
            "           datepicker: secdate",//下拉列表
            "               placeholder: 第二个日期选择",    //可以没有            "page: poptest",
            "   div: pops",
            "        components: ",
            "           popup: firstpop",
            "              title: 我的第一个弹窗",
            "              pop-button: confirm",   
            "                  font: fa-check",  
            "                  text: 确认",            
            "              pop-button: cancel",     
            "                  text: 取消",      
            "                  font: fa-times",
            "           popup: secpop",
            "              font: fa-check",       //窗口标题的图标，可以不设置       
            "              title: 我的第二个弹窗",
            "              pop-button: confirm",     
            "                  text: 确认",
            "           button-small: button1",
            "              text: button1",      
            "              font: fa-eercast",  //font属性可以不设置     
            "page: test",
            "   title: 第一张网页", 
            "   div: buttons",
            "        components: ",
            "          button-small: button1",
            "              text: button1",      
            "              font: fa-eercast",  //font属性可以不设置     
            "          button: button2",
            "              text: button2",      
            "              font: fa-envelope-open",       
            "              tip: 鼠标移入后会有提示",    //tip属性可以不设置     
            "          button-big: button3",
            "              text: button3",      
            "              font: fa-grav",      
            "          button-icon: button4",
            "              text: button4",      
            "              font: fa-id-card-o",     
            "   div: nav",
            "       th-include: nav",
            "   div: container",
            "     class: hor",
            "     div: top",
            "        components: ",
            "          uploader: testupload",     //值为upload容器的ID属性
            "              upload-target-dir: d:/codetest/upload",//上传文件夹，这个参数必须要在PROJECT范围内值一致，也就是每个项目就这一个文件夹，定义在PROJECT中也行
            "              file-size-limit: 100",//上传文件大小限制,值为整数MB单位，这个属性也可以没有
            "          downloader: ", //下载组件，要求有一个下载的CONTROLLER链接
            "              url: /download/{file}",//下载文件
            "     div: middle",
            "        th-insert: context",  //三种导入页面，分别是in
            "     div: foot",
            "        th-replace: context",     
        } ;
    
        try {
            NodeRenderFactory nf = new NodeRenderFactory();
            YMLParser parser = new YMLParser();
            TreeConstructor tree = new TreeConstructor();
            for (String s : page) {
                parser.parser(s);
            }
            List<NodeWrapper> rst = parser.get();
            Node d = tree.exec(rst);
            Collections.reverse(d.getSubNodes());
            for(Node sub : d.getSubNodes()){
                //System.out.println( Node.getNodeRaw(sub, "")); //测试将节点序列化
                nf.rend(sub);
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    @Test
    public void testnestedPage(){
        String[] page = new String[]{
                "project: ",
                "  dir: d:/codetest11",
                "  package-name: edu.nuist.code",
                "  db-name: qdb",
                "  db-user: root",
                "  db-password: hanjinhao",
                "  port: 8080",
                "  dependencies: ",
                "    hutools: ",
                "    json: ",
                "page: inputtest",
                "   title: 测试输入控件",
                "   div: firstdiv",
                "       div: nested",
                "           div: container",
                "                components: ",
                "                   datepicker: firstdate",//下拉列表
                "                       placeholder: 第一个日期选择",    //可以没有
                "                   datepicker: secdate",//下拉列表
                "                       placeholder: 第二个日期选择",    //可以没有
                "                   selector: s2",//多选框
                "                       sel-item: select1, 1",//选项：显示文字，值
                "                       sel-item: select2, 2",//选项
                "                       sel-item: select3, 3",//选项
                "                   multi-selector: s1",//下拉列表
                "                       sel-item: select1, 1",//选项：显示文字，值
                "                       sel-item: select2, 2",//选项
                "                       sel-item: select3, 3",//选项
                "                   editor: first",//文本框,
                "                       height: 240",//高度，整数
                "   form: testform",
                "        url: /test/testform",
                "        class: testform",
                "        components: ",
                "           input: firstinput",
                "               placeholder: 只能输入整数",    //可以没有
                "               label: 输入小数的时候会出错",//可以没有
                "               value: 10", //可以没有
                "               verifier: int",        //可以没有,校验种类：email、'nonull' 'int' 'double': 'date' 'year':
                "           input: secinput",
                "           downlist: thrinput",//下拉列表
                "               url: /inputtest/testdown",    //获取列表内容的接口地址，返回JSON数组，每个元素包括{item: , value:},ITEM是选项显示内容，VALUE是选项的值，这个选项可以是ITEMS， URL，如果二个都有，则会混合在一起
                "               placeholder: 使用下拉列表",    //可以没有
                "           downlist: firinput",
                "               items: [第一个选项, 第二个选项, 第三个选项]",    //获取列表内容的接口地址，返回数组，每个元素包括{item: , value:},ITEM是选项显示内容，VALUE是选项的值，这个选项可以是ITEMS， URL，如果二个都有，则会混合在一起
                "               placeholder: 使用下拉列表",    //可以没有
                "           autocomplete: fiveinput",
                "               url: /inputtest/autocomplete",    //只接收从URL获取，返回JSON数组，每个元素包括{item: , value:},ITEM是选项显示内容，VALUE是选项的值
                "               placeholder: 使用AutoComplete",    //可以没有
                "           button-form: submit", //form的提交按键，必须要有
                "               text: submit",
                "               font: fa-paper-plane",  //font属性可以不设置
        };
        try {
            NodeRenderFactory nf = new NodeRenderFactory();
            YMLParser parser = new YMLParser();
            TreeConstructor tree = new TreeConstructor();
            for (String s : page) {
                parser.parser(s);
            }
            List<NodeWrapper> rst = parser.get();
            Node d = tree.exec(rst);
            Collections.reverse(d.getSubNodes());
            for(Node sub : d.getSubNodes()){
                //System.out.println( Node.getNodeRaw(sub, "")); //测试将节点序列化
                nf.rend(sub);
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(e.getMessage());
        }
    }
    
    @Test
    public void t(){
        ButtonComponentSerier bcs = new ButtonComponentSerier("d:/codetest", "edu.nuist.code", "","");
        bcs.copyOtherFiles();
    }


    @Test
    public void k(){
        String[] div = new String[]{
            "div: container",
            "     class: hor",
            "     div: top",
            "        components: ",
            "          uploader: ",     //upload包括ID属性
            "              id: testupload",     
            "              upload-tmp-dir: d:/codetest/upload/tmp",//上传的临时文件夹
            "              upload-target-dir: d:/codetest/upload",//上传文件夹，这二个参数必须要在PROJECT范围内值一致，也就是每个项目就这二个文件夹
        };

        try {
            Map<String, String> attrs = new HashMap<>();
            attrs.put(NodeRenderFactory.DIR_KEY, "d:/codetest");
            attrs.put(NodeRenderFactory.PACKAGE_KEY, "com.nuist.codehelper");
            attrs.put(UploaderComponentSerier.ATTR_TAG_DIR_KEY, "d:/codetest/upload");
            
            YMLParser parser = new YMLParser();
            TreeConstructor tree = new TreeConstructor();
            for (String s : div) {
                parser.parser(s);
            }
            List<NodeWrapper> rst = parser.get();
            Node d = tree.exec(rst);
            String redent = "";
            HtmlRender hr = new HtmlRender();
            CSSRender cr = new CSSRender(); 
            JSRender jr = new JSRender(); 
            HashSet<String> dependThdJS = new HashSet<String>(); 

            DivRender dr = new DivRender(redent, hr, cr, jr, dependThdJS, d.getSubNodes().get(0), attrs);
           
            dr.exec();

            System.out.println( hr.getHtml() );
            System.out.println( cr.rend());
            System.out.println( dependThdJS );
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        


       
        
    }
}

