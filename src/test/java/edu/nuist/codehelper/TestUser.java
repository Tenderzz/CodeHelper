package edu.nuist.codehelper;

import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import edu.nuist.codehelper.component.NodeRenderFactory;
import edu.nuist.codehelper.component.TreeConstructor;
import edu.nuist.codehelper.component.YMLParser;
import edu.nuist.codehelper.entity.Node;
import edu.nuist.codehelper.entity.NodeWrapper;

@SpringBootTest
public class TestUser {
    @Test
    public void testrole(){
        String[] page = new String[]{
            "project: ",
            "  dir: d:/codetest1",
            "  package-name: edu.nuist.code",
            "  db-name: qdb",
            "  db-user: root",
            "  db-password: hanjinhao",
            "  port: 8080",
            "  dependencies: ",
            "    hutools: ",
            "    json: ",    
            "user: user", //user属于一种特殊的BEAN，创建完成后有自己的username, password默认属性,有其默认的DAO
            "  table: user",
            "  prop: ",
            "     grade: int",
            "     code: int",
            "        col-name: student_code",
            "     introduction: String",
            "        col-name: intro",
            "        col-type: TEXT",
            "     createTime: Timestamp ",
            "  roles: ", //用户的角色配置，可以有可以没有，如果有的话，会增加一个属性int role；
            "     normal: 普通用户",      
            "     admin: 管理员",   
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
}
