package edu.nuist.codehelper.entity;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import javax.transaction.Transactional;

@Service
public class UserService {
    @Autowired
    private UserReps reps;

    public User save(User user) {
        //MD5加密处理
        user.setPassword(DigestUtils.md5DigestAsHex(user.getPassword().getBytes()));
        return reps.save(user);
    }


    public User findByAccount(String account){
        return reps.findByAccount(account);
    }

    @Transactional
    public void delete(long id) {
        reps.deleteById(id);
    }

    @Transactional
    public void updateByAccount(String password,String account){
        reps.updateByAccount(password,account);
    }

}

