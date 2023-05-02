package edu.nuist.codehelper;

import edu.nuist.codehelper.entity.ProjService;
import edu.nuist.codehelper.entity.User;
import edu.nuist.codehelper.entity.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.UUID;

@SpringBootTest
class loginTests {
    @Autowired
    UserService userService;

    @Autowired
    ProjService projService;
    @Test
    void contextLoads() {
        User user= new User();
        user.setPassword("liyang");
        user.setAccount("liyang");
        userService.save(user);
    }

    @Test
    void update(){
        userService.updateByAccount("liyang1","111");
    }

    @Test
    void findall(){
        System.out.println( userService.findByAccount("123"));
    }

    @Test
    void updateProj(){
        projService.update("123","123","a6749126-c9b0-4684-a9f8-71480364a12a");
    }
}
