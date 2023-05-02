package edu.nuist.codehelper.component;

import java.util.LinkedList;
import java.util.List;

import edu.nuist.codehelper.entity.Node;
import edu.nuist.codehelper.entity.NodeWrapper;
import edu.nuist.codehelper.exception.NodeTypeNoFind;
import edu.nuist.codehelper.exception.SplitCharNotFind;

public class TreeConstructor {
    public Node ROOT = Node.builder().pid(-1).id(0).subNodes(new LinkedList<>()).type(null).raw(":").build();

    public boolean getTree(Node n,  List<NodeWrapper> wrappers ){  
           List<NodeWrapper> tmp = findSons(wrappers, n.getId() );
           //如果没有子节点，就退出当前递归
           if( tmp.size() == 0 ) return true;
           List<Node> subnodes = new LinkedList<>();
           for( NodeWrapper p : tmp){
                Node sub = convert(p);
                try{
                    sub.init();
                    if( sub.getType() == null ){
                        //如果当前的节点找不到对应的节点类型，则输出警告提示
                        System.out.println( new NodeTypeNoFind(sub.getRaw()).getMessage());
                    }
                }catch(SplitCharNotFind e){
                    System.out.println( e.getMessage() );
                    return false;
                }
                subnodes.add(sub) ;
           }
           wrappers.removeAll(tmp);

           for(Node sub: subnodes){
                boolean flag = getTree(sub, wrappers);
                //如果当前子节点建树出错，就退出
                if( !flag ) return false;
           }
            
           n.addSubNodes(subnodes);
           return true;
    }

    public Node exec(List<NodeWrapper> wrappers ){
        boolean flag = getTree(ROOT, wrappers);
        if( !flag ) return null; 
        return ROOT;
    }

    public Node convert( NodeWrapper p){
        Node n = Node.builder().raw(p.getContent())
                .id(p.getId())
                .pid(p.getPid())
                .subNodes(new LinkedList<>())
                .build();   
        n.addSubNodes(new LinkedList<>());
        return n; 
    }

    public List<NodeWrapper> findSons(List<NodeWrapper> wrappers, int pid){
        List<NodeWrapper> tmp = new LinkedList<>();
        for(NodeWrapper p : wrappers){
            if( p.getPid() == pid){
                tmp.add(p);
            }
        }
        return tmp;
    }
}
