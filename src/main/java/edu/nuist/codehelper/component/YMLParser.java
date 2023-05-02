package edu.nuist.codehelper.component;

import java.util.LinkedList;
import java.util.List;

import edu.nuist.codehelper.entity.NodeWrapper;
import edu.nuist.codehelper.utils.ListStack;

public class YMLParser {
    private ListStack<NodeWrapper> stack = new ListStack<>();
    private int currentId = 1;
    public static final char SPACE = ' ';
    public static final char TAB = '	';

    private List<NodeWrapper> rst = new LinkedList<>();

    public List<NodeWrapper> get(){
        NodeWrapper p = stack.peek();
        while( p != null){
            rst.add(stack.pop());
            p = stack.peek();
        }

        return rst;
    }

    public void parser( String line ){
        
        if("".equals(line.trim())) return;
        
        int indent = getIndent(line);
        NodeWrapper n  =  NodeWrapper.builder()
                        .id(currentId++)
                        .indent(indent)
                        .content( getContent(line))
                        .build();
        NodeWrapper p = stack.peek();

        if( p == null ){
            n.setPid(0); //最高根节点
        }else{
            int pIndent = p.getIndent();
            if( indent > pIndent ) n.setPid( p.getId() );
            else if( indent == pIndent ) n.setPid( p.getPid() );
            else execPop( n );
        }

        stack.push(n);
    }


    public void execPop( NodeWrapper n ){
        NodeWrapper p = stack.peek();
        while( p != null && p.getIndent() > n.getIndent() ){
            rst.add( stack.pop() );
            p = stack.peek();
        }

        if( p == null ){
            n.setPid(0); //最高根节点
        }else{
            int pIndent = p.getIndent();
            if( n.getIndent() > pIndent ) n.setPid( p.getId() );
            else if( n.getIndent() == pIndent ) n.setPid( p.getPid() );
        }
    }

    public int getIndent( String line ){
        char preChar = 0;
        if( line.charAt(0) == SPACE ){
            preChar = SPACE;
        }else if( line.charAt(0) == TAB){
            preChar = TAB;
        }else return 0;
        int index = 0;
        while( line.charAt(index) == preChar ){
            index ++;
        }
        return index;
    }

    public String getContent(String line){
        return line.trim();
    }   
}
