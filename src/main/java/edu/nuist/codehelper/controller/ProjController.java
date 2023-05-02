package edu.nuist.codehelper.controller;

import edu.nuist.codehelper.common.R;
import edu.nuist.codehelper.component.NodeRenderFactory;
import edu.nuist.codehelper.component.TreeConstructor;
import edu.nuist.codehelper.component.YMLParser;
import edu.nuist.codehelper.entity.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

@RestController
@RequestMapping("/proj")
public class ProjController {
    @Autowired
    private ProjService projService;

    @RequestMapping("/")
    public List<Proj> showproj(String account) {
        List<Proj> list = projService.findAllByAccount(account);

        return list;
    }

    @RequestMapping("/addproj")
    public R<Proj> addproj(Proj proj) {
        projService.save(proj);
        return R.success(proj);
    }

    @RequestMapping("/delproj")
    public R<String> delproj(String projid) {
        projService.delete(projid);
        System.out.println(projid);
        return R.success("删除成功");
    }

    @RequestMapping("/updateproj")
    public R<String> updateproj(String projname, String content, String projid) {
        projService.update(projname, content, projid);
/*        System.out.println(projname);
        System.out.println(content);
        System.out.println(projid);*/
        return R.success("更新成功");
    }

    @RequestMapping("/findprojcontent")
    public R<Proj> findprojcontent(String projid) {
        Proj p = projService.findByProjid(projid);
        System.out.println(p);
        if (p != null) {
            return R.success(p);
        }
        return R.error("");
    }

    @RequestMapping("/coderender")
    public R<String> coderender(@RequestParam("code[]") List<String> list){
        try {
            NodeRenderFactory nf = new NodeRenderFactory();
            YMLParser parser = new YMLParser();
            TreeConstructor tree = new TreeConstructor();
            String[] page=list.toArray(new String[list.size()]);
            System.out.println(page);
            for (String s : page) {
                System.out.println(s);
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
            return R.error(e.getMessage());
        }
        return R.success("生成成功");
    }
}

