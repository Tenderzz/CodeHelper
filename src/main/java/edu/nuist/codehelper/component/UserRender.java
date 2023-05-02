package edu.nuist.codehelper.component;

import java.text.MessageFormat;
import java.util.HashMap;
import java.util.Map;
import edu.nuist.codehelper.entity.Node;
import edu.nuist.codehelper.exception.RequiredPropertyNoFind;
import edu.nuist.codehelper.exception.SplitCharNotFind;
import edu.nuist.codehelper.render.bean.BeanRender;
import edu.nuist.codehelper.utils.FileUtil;
import lombok.Data;
import lombok.extern.log4j.Log4j2;

@Data
@Log4j2
public class UserRender {
    private boolean hasroles = false;

    private Node n;

    //作为普通的BEAN来参与
    private Node prop;
    private Node role;
    private String pack;
    private Map<String, String> ctx = new HashMap<String, String>();



    public void wrapUserBean(){
        int id = this.prop.getId();
        Node username =  Node.builder()
                        .id(0)
                        .pid(id)
                        .key("username")
                        .raw("username: String")
                        .value("String")
                        .build(); 
        Node password = Node.builder()
                        .id(0)
                        .pid(id)
                        .raw("password: String")
                        .key("password")
                        .value("String")
                        .build(); 
        this.prop.addSubNode(username); this.prop.addSubNode(password);
        if( this.role != null){
            Node role = Node.builder()
                .id(0)
                .pid(id)
                .raw("role: int")
                .key("role")
                .value("int")
                .build(); 
            this.prop.addSubNode(role);
        }
        
    }

    public UserRender(Node n, Map<String, String> ctx) {
        this.ctx = ctx;
        this.n = n;
        this.pack = this.ctx.get(NodeRenderFactory.PACKAGE_KEY);
        try {
            for (Node sub : n.getSubNodes()) {
                if (sub.getKey().equals("prop")) {
                    this.prop = sub;
                } 
                if (sub.getKey().equals("roles")) {
                    this.role = sub;
                }   
            }
        } catch (SplitCharNotFind e) {
            log.error(e.getMessage());
        }
    }

    public boolean check() {
        return this.prop != null;
    }
    public static final String roleMode = "    {0}(\"{1}\",{2}),";
    public void exec() throws Exception {
        if (!this.check()) throw new RequiredPropertyNoFind();
        this.wrapUserBean();
        this.n.setKey("bean");
        this.n.setValue("User");
        BeanRender br = new BeanRender(this.n, this.ctx);
        br.exec();  //序列化BEAN;

        //覆盖掉REPS和SERV
        String reps = FileUtil.getFileContent( FileTemplator.read("templates/user/UserReps.txt"));
        reps = reps.replaceAll("<!--pack-->", this.pack);
        FileUtil.writeStringToFile(reps, this.ctx.get(NodeRenderFactory.DIR_KEY) + "/src/main/java/" + pack.replaceAll("\\.", "\\/") + "/" + "entity" + "/UserReps.java");
        
        String serv = FileUtil.getFileContent( FileTemplator.read("templates/user/UserService.txt"));
        serv = serv.replaceAll("<!--pack-->", this.pack);
        FileUtil.writeStringToFile(serv, this.ctx.get(NodeRenderFactory.DIR_KEY) + "/src/main/java/" + pack.replaceAll("\\.", "\\/") + "/" + "entity" + "/UserService.java");
        
        //序列化Controller
        String contr = FileUtil.getFileContent( FileTemplator.read("templates/user/UserController.txt"));
        if( this.role == null ){
            contr = br.erease(contr, "role");
        }else{
            contr = contr.replaceAll("(\\[role([\\s\\S]*?)\\])", "import <!--pack-->.entity.UserRole");
        }

        contr = contr.replaceAll("<!--pack-->", this.pack);
        
        FileUtil.writeStringToFile(contr, this.ctx.get(NodeRenderFactory.DIR_KEY) + "/src/main/java/" + pack.replaceAll("\\.", "\\/") + "/" + "controller" + "/UserController.java");
        
        //如果有ROLE，则生成ROLE的枚举类
        if( this.role != null){
            StringBuffer sb = new StringBuffer();
            int i=0; 
            for(Node n : this.role.getSubNodes()){
                sb.append(MessageFormat.format(roleMode, n.getKey().toUpperCase(), n.getVale(), (i++))+"\r\n");
            }

            String t = sb.toString();
            t = t.substring(0, t.length()-3) + ";\r\n";
            String roleStr = FileUtil.getFileContent( FileTemplator.read("templates/user/rolesEnum.txt"));
            
            roleStr = roleStr.replaceAll("<!--roles-->",t).replaceAll("<!--pack-->", this.pack);;
            FileUtil.writeStringToFile(roleStr,  this.ctx.get(NodeRenderFactory.DIR_KEY) + "/src/main/java/" + pack.replaceAll("\\.", "\\/") + "/" + "entity" + "/UserRole.java");
        
        
            
        }
    }

    

}
