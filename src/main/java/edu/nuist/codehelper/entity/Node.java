package edu.nuist.codehelper.entity;

import java.util.LinkedList;
import java.util.List;

import edu.nuist.codehelper.exception.SplitCharNotFind;
import lombok.Builder;
@Builder
public class Node {
    private int id;
    private int pid;

    private NodeType type;
    private String raw;

    private String key;
    private String value;
    
    @Builder.Default
    private List<Node> subNodes = new LinkedList<>();

    public void addSubNode( Node node ){
        subNodes.add(node);
    }

    public void addSubNodes( List<Node> node ){
        subNodes.addAll(node);
    }

    public List<Node> getSubNodes(){
        return this.subNodes;
    }

    public NodeType getType() throws SplitCharNotFind{
        if( this.type != null) return this.type;
        this.type = NodeType.getByName(this.key);
        return this.type;
    }

    public String getKey() throws SplitCharNotFind{
        try{
            if( key != null ) return key;
            this.key = this.raw.substring(0, this.raw.indexOf(":")).trim().toLowerCase();
            
        }catch(Exception e){
            throw new SplitCharNotFind(this.raw);
        }
        return this.key;
    }

    public String getVale() throws SplitCharNotFind{
        try{
            if( value != null ) return value;
            this.value = this.raw.substring( this.raw.indexOf(":")+1 ).trim();;
        }catch(Exception e){
            throw new SplitCharNotFind(this.raw);
        }
        return this.value;
    }

    public void init() throws SplitCharNotFind {
        this.getKey();
        this.getVale();
        this.getType();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getPid() {
        return pid;
    }

    public void setPid(int pid) {
        this.pid = pid;
    }

    public void setType(NodeType type) {
        this.type = type;
    }

    public String getRaw() {
        return raw;
    }

    public void setRaw(String raw) {
        this.raw = raw;
    }

    public static String getNodeRaw(Node n, String indent){
        StringBuffer sb = new StringBuffer();
        sb.append(indent + n.getRaw() +"\r\n");
        indent += "  ";
        for(Node sub: n.getSubNodes()){
            sb.append(getNodeRaw(sub, indent) );
        }
        return sb.toString();
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
    
}
