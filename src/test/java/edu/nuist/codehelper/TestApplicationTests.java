package edu.nuist.codehelper;

import edu.nuist.codehelper.component.NodeRenderFactory;
import edu.nuist.codehelper.component.TreeConstructor;
import edu.nuist.codehelper.component.YMLParser;
import edu.nuist.codehelper.entity.Node;
import edu.nuist.codehelper.entity.NodeWrapper;
import edu.nuist.codehelper.render.page.PageRender;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
class TestApplicationTests {
/**
         * 实现对这个Page节点的解析
         * 1. 发现Page节点，
         *     1.1 在项目构建的包下检测有没有Controller子包，如果没有建立一个包
         *     1.2 在项目下渲染创建一个Controller类，以page节点的值为类名，大写首字符+Controller
         *     1.3 在Controller类中创建一个方法，映射URL路径为"/"+page节点的值，然后将page节点的值返回作为页面
         * 2. 渲染html
         *     2.1 遍历PAGE子节点
         *     2.2 只允许DIV类型
         *     2.3 渲染DIV
         *     2.4 如果DIV中有layout属性，则不允许有components属性，反之依然，做一个判断，如果判断为否，也就是二个都有，则抛出DivSubnodeError异常
         *     2.5 如果没有异常，有LAYOUT属性，则将DIV的class属性设置为layout节点值，如果子节点是DIV，则生成渲染DIV，将返回的结果插入到当前DIV的渲染结果中
         *     2.6 如果子节点是html，是SPRING BOOT的Themleaf片段，生成include形式导入这个片段的代码，插入到当前DIV的渲染结果中
               重新设定一下，PAGE ，DIV下面不控制，随便都行
               1. title: 是PAGE网页的标题
               2. page, div, 节点的值分别是page的html文件名，CONTROLLER名, DIV的ID号
               3. class， 是DIV的属性，CLASS属性
               4. components, 是DIV包含的插件定义
               5. th-include, th-insert, th-replace, 是DIV的页面THEMLEAF的FRAGMENT节点地址， 分别对应三种不同的导入片段方式
         
         */

    @Test
    public void formCreate() throws  Exception {
        String[] page = {
                "project: ",
                "  dir: d:/codetest10",
                "  package-name: edu.nuist.code",
                "  db-name: ojs",
                "  db-user: root",
                "  db-password: Ly20020622.123",
                "  port: 8080",
                "  dependencies: ",
                "    hutools: ",
                "    json: ",
                "page: test",
                "   title: 第一张网页",
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
            YMLParser parser = new YMLParser();
            TreeConstructor tree = new TreeConstructor();
            for (String s : page) {
                parser.parser(s);
            }
            List<NodeWrapper> rst = parser.get();
            Node d = tree.exec(rst);
            NodeRenderFactory nodeRenderFactory = new NodeRenderFactory();
            for (int i=d.getSubNodes().size()-1;i>=0;i--){
                nodeRenderFactory.rend(d.getSubNodes().get(i));
            }

            System.out.println("创建成功");
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
    @Test
    public void pageCreate() throws Exception {
        
        String[] page = {
            "project: ",
            "  dir: d:/codetest",
            "  package-name: edu.nuist.code",
            "  db-name: ojs",
            "  db-user: root",
            "  db-password: Ly20020622.123",
            "  port: 8080",
            "  dependencies: ",
            "    hutools: ",
            "    json: ",    
            "page: test",
            "   title: 第一张网页", 
            "   div: nav",
            "       th-include: nav",
            "   div: container",
            "     class: hor",
            "     div: top",
            "        components: ",
            "          uploader: ",     //upload包括ID属性
            "              id: testupload",     
            "              upload-tmp-dir: d:/codetest/upload/tmp",//上传的临时文件夹
            "              upload-target-dir: d:/codetest/upload",//上传文件夹，这二个参数必须要在PROJECT范围内值一致，也就是每个项目就这二个文件夹
            "     div: middle",
            "        th-insert: context", 
            "     div: foot",
            "        th-replace: context",                                       
        };

        try {
            YMLParser parser = new YMLParser();
            TreeConstructor tree = new TreeConstructor();
            for (String s : page) {
                parser.parser(s);
            }
            List<NodeWrapper> rst = parser.get();
            Node d = tree.exec(rst);
            
            PageRender pr = new PageRender(d.getSubNodes().get(0), null);
            pr.exec();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    @Test
    public void beanCreate() throws Exception {
        String[] proj = {
                "project: ",
                "  dir: d:/codetest",
                "  package-name: edu.nuist.code",
                "  db-name: ojs",
                "  db-user: root",
                "  db-password: Ly20020622.123",
                "  port: 8080",
                "   dependencies: ",
                "      hutools: ",
                "      json: ",
                "bean: Student",
                "  table: student",
                "  prop: ",
                "     name: String",
                "     code: int",
                "        col-name: student_code",
                "     introduction: String",
                "        col-name: intro",
                "        col-type: TEXT",
                "     createTime: Timestamp ",
                "     file: List<String>",
                "        col-not:",
                "  dao: ", //分为PAGED和不分页的方法，返回是list还是单个
                "     page-fun: testp",
                "        &id: ",
                "        &name: ",
                "        param: String",
                "        start: int",
                "        end: int",
                "        sql: select * from student",
                "     list-fun: testl",
                "        &name: ",
                "        param: String",
                "        start: int",
                "        end: int",
                "        sql: select * from student",
                "     update-fun: testu",
                "        &name: ",
                "        param: String",
                "        start: int",
                "        end: int",
                "        sql: select * from student",
                "     fun: testf",
                "        &name: ",
                "        param: String",
                "        start: int",
                "        end: int",
                "        sql: select * from student",
                "bean: People",
                "  table: people",
                "  prop: ",
                "     name: String",
                "     code: int",
                "        col-name: student_code",
                "     introduction: String",
                "        col-name: intro",
                "        col-type: TEXT",
                "     createTime: Timestamp ",
                "     file: List<String>",
                "        col-not:",
                "  dao: ", //分为PAGED和不分页的方法，返回是list还是单个
                "     page-fun: testp",
                "        &id: ",
                "        &name: ",
                "        param: String",
                "        start: int",
                "        end: int",
                "        sql: select * from student",
                "     list-fun: testl",
                "        &name: ",
                "        param: String",
                "        start: int",
                "        end: int",
                "        sql: select * from student",
                "     update-fun: testu",
                "        &name: ",
                "        param: String",
                "        start: int",
                "        end: int",
                "        sql: select * from student",
                "     fun: testf",
                "        &name: ",
                "        param: String",
                "        start: int",
                "        end: int",
                "        sql: select * from student"
        };

        try {
            YMLParser parser = new YMLParser();
            TreeConstructor tree = new TreeConstructor();
            for (String s : proj) {
                parser.parser(s);
            }
            List<NodeWrapper> rst = parser.get();
            Node d = tree.exec(rst);
            NodeRenderFactory nodeRenderFactory = new NodeRenderFactory();
            for (int i=d.getSubNodes().size()-1;i>=0;i--){
                nodeRenderFactory.rend(d.getSubNodes().get(i));
            }

            System.out.println("创建成功");
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

    }

    public void bean1Create() throws Exception {
        String[] proj = {
                "project: ",
                "  dir: d:/codetest",
                "  package-name: edu.nuist.code",
                "  db-name: ojs",
                "  db-user: root",
                "  db-password: Ly20020622.123",
                "  port: 8080",
                "   dependencies: ",
                "      hutools: ",
                "      json: ",
                "bean: Student",
                "  table: student",
                "  prop: ",
                "     name: String",
                "     code: int",
                "        col-name: student_code",
                "     introduction: String",
                "        col-name: intro",
                "        col-type: TEXT",
                "     createTime: Timestamp ",
                "     file: List<String>",
                "        col-not:",
                "  dao: ", //分为PAGED和不分页的方法，返回是list还是单个
                "     page-fun: testp",
                "        &id: ",
                "        &name: ",
                "        param: String",
                "        start: int",
                "        end: int",
                "        sql: select * from student",
                "     list-fun: testl",
                "        &name: ",
                "        param: String",
                "        start: int",
                "        end: int",
                "        sql: select * from student",
                "     update-fun: testu",
                "        &name: ",
                "        param: String",
                "        start: int",
                "        end: int",
                "        sql: select * from student",
                "     fun: testf",
                "        &name: ",
                "        param: String",
                "        start: int",
                "        end: int",
                "        sql: select * from student",
                "bean: People",
                "  table: people",
                "  prop: ",
                "     name: String",
                "     code: int",
                "        col-name: student_code",
                "     introduction: String",
                "        col-name: intro",
                "        col-type: TEXT",
                "     createTime: Timestamp ",
                "     file: List<String>",
                "        col-not:",
                "  dao: ", //分为PAGED和不分页的方法，返回是list还是单个
                "     page-fun: testp",
                "        &id: ",
                "        &name: ",
                "        param: String",
                "        start: int",
                "        end: int",
                "        sql: select * from student",
                "     list-fun: testl",
                "        &name: ",
                "        param: String",
                "        start: int",
                "        end: int",
                "        sql: select * from student",
                "     update-fun: testu",
                "        &name: ",
                "        param: String",
                "        start: int",
                "        end: int",
                "        sql: select * from student",
                "     fun: testf",
                "        &name: ",
                "        param: String",
                "        start: int",
                "        end: int",
                "        sql: select * from student"
        };

        try {
            YMLParser parser = new YMLParser();
            TreeConstructor tree = new TreeConstructor();
            for (String s : proj) {
                parser.parser(s);
            }
            List<NodeWrapper> rst = parser.get();
            Node d = tree.exec(rst);
            NodeRenderFactory nodeRenderFactory = new NodeRenderFactory();
            for (int i=d.getSubNodes().size()-1;i>=0;i--){
                nodeRenderFactory.rend(d.getSubNodes().get(i));
            }

            System.out.println("创建成功");
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

    }


}
