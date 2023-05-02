package edu.nuist.codehelper.component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import edu.nuist.codehelper.entity.Node;
import edu.nuist.codehelper.entity.NodeWrapper;
import edu.nuist.codehelper.render.bean.BeanRender;
import edu.nuist.codehelper.render.page.PageRender;
import edu.nuist.codehelper.render.project.ProjectRender;
import lombok.extern.log4j.Log4j2;

@Log4j2
public class NodeRenderFactory {
    private Map<String, String> context = new HashMap<>();
    public static final String PACKAGE_KEY = "pack";
    public static final String DIR_KEY = "dir";

    public Node getNodeFromStrings(String[] lines){
        YMLParser parser = new YMLParser();
        TreeConstructor tree = new TreeConstructor();
        for (String s : lines) {
            parser.parser(s);
        }
        List<NodeWrapper> rst = parser.get();
        return tree.exec(rst).getSubNodes().get(0);
    }

    public Node getNodeFromString(String line){
        return getNodeFromStrings(line.split("\r\n"));
    }

    public void rend( Node n ) throws Exception{
        switch(n.getType()){
            case PROJECT:
                log.info("Project Node has found!");
                new ProjectRender(n, context).exec();
                break;
            case BEAN:
                log.info("Bean Node has found!");
                new BeanRender(n, context).exec();
                break;
            case PAGE:
                log.info("Page Node has found!");
                new PageRender(n, context).exec();
                break;
            case USER:
                log.info("User Node has found!");
                new UserRender(n, context).exec();
                break;
            default:

        }
    }
}
