package edu.nuist.codehelper.component;

import edu.nuist.codehelper.exception.ProjectCreatError;
import lombok.extern.log4j.Log4j2;

import java.io.File;

@Log4j2
public class BeanCreator {

    public void exec(String dir, String packagename, String beans, String beanReps, String beanServ, String beanName) throws ProjectCreatError {
        if (dir.endsWith("/")) dir = dir.substring(0, dir.length() - 1);
        createBeanPackage(dir,packagename);
        FileTemplator.newEntity(dir + "/src/main/java", beanName, beans, beanReps, beanServ, packagename);

    }
    
    public String createBeanPackage(String dir, String pack) {
        String packdir = pack.replaceAll("\\.", "\\/");
        new File(dir + "/src/main/java/" + packdir +"/entity").mkdirs();
        return dir + "/src/main/java" + packdir +"/entity";
    }

}
