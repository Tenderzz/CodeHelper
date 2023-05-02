package edu.nuist.codehelper.render.project;

import java.util.Map;

import edu.nuist.codehelper.component.NodeRenderFactory;
import edu.nuist.codehelper.component.ProjectCreator;
import edu.nuist.codehelper.entity.Node;
import edu.nuist.codehelper.exception.DependencyNoFind;
import edu.nuist.codehelper.exception.ProjectCreatError;
import edu.nuist.codehelper.exception.RequiredPropertyNoFind;
import edu.nuist.codehelper.exception.SplitCharNotFind;
import edu.nuist.codehelper.render.Render;
import lombok.Data;
import lombok.extern.log4j.Log4j2;

@Data
@Log4j2
public class ProjectRender extends Render{
    private String dir;
    private String port;
    private String packageName;
    private String dbName;
    private String dbUser;
    private String dbPassword;

    private Node dependencies;

    @Override
    public void exec() throws RequiredPropertyNoFind, DependencyNoFind, ProjectCreatError {
        if( !this.check() ) throw new RequiredPropertyNoFind();

        ProjectCreator pc = new ProjectCreator(); 
        String dependencies = null;
            //渲染依赖项
        if( this.dependencies != null){
            ProjectDependencyRender pd = new ProjectDependencyRender(this.dependencies, this.getCTX());
            pd.exec();
            dependencies = pd.getRendRst();
        }   

        this.getCTX().put(NodeRenderFactory.PACKAGE_KEY, this.packageName);
        this.getCTX().put(NodeRenderFactory.DIR_KEY, this.dir);
        
        pc.exec(
            this.dir, 
            this.packageName, 
            this.port, 
            dbName, 
            dbUser, 
            dbPassword,
            dependencies
        );

    }

    public ProjectRender(Node n, Map<String, String> ctx){
        super(n, ctx);
        try{
            for( Node sub: n.getSubNodes()){
                if("dir".equals(sub.getKey())){ this.dir = sub.getVale(); }
                if("port".equals(sub.getKey())){ this.port = sub.getVale(); }
                if("package-name".equals(sub.getKey())){ this.packageName = sub.getVale(); }
                if("db-name".equals(sub.getKey())){ this.dbName = sub.getVale(); }
                if("db-user".equals(sub.getKey())){ this.dbUser = sub.getVale(); }
                if("db-password".equals(sub.getKey())){ this.dbPassword = sub.getVale(); }
                if("dependencies".equals(sub.getKey())){ this.dependencies = sub; }

                //收集系统配置信息到CONTEXT中
                ctx.put(n.getKey(), n.getVale());
            };
        }catch(SplitCharNotFind e){
            log.error(e.getMessage());
        }
    }

    @Override
    public boolean check() {
        return !epsString(dir) && !epsString(packageName) &&  !epsString(dbName)
                &&  !epsString(dbUser) &&  !epsString(dbPassword);
    }

    public static final String[] checkPoints = new String[]{
        "dir",
        "package",
        "db",
        "dbuser",
        "dbpassword"
    };


}
