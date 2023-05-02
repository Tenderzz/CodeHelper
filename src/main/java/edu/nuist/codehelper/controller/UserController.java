package edu.nuist.codehelper.controller;


import edu.nuist.codehelper.common.R;
import edu.nuist.codehelper.entity.User;
import edu.nuist.codehelper.entity.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/user")
public class UserController {
    @Autowired
    private UserService userService;

    @RequestMapping("/regist")
    public R<String> userRegist(User user) {
        if (userService.findByAccount(user.getAccount()) != null) {
            return R.error("账户已存在，请重新输入");
        }
        userService.save(user);
        return R.success("注册成功！");
    }

    @RequestMapping("/login")
    public R<User> userLogin(HttpServletRequest request, User user) {
        String password = user.getPassword();
        password = DigestUtils.md5DigestAsHex(password.getBytes());
        User u = userService.findByAccount(user.getAccount());
        if (u == null) {
            return R.error("登录失败：账户未注册");
        }
        if (!u.getPassword().equals(password)) {
            return R.error("登录失败：密码错误");
        }
        if (u.getStatus() == 0) {
            return R.error("登录失败：账户被禁用！请联系管理员");
        }
        request.getSession().setAttribute("user", u);
        return R.success(u);
    }

    @RequestMapping("/logout")
    public R<String> logout(HttpServletRequest request) {
        request.getSession().removeAttribute("user");
        return R.success("退出成功！");
    }

    @RequestMapping("/forget")
    public R<String> forgetpwd(@RequestParam("account") String account
            , @RequestParam("email") String email
            , @RequestParam("password") String password) {
        User user = userService.findByAccount(account);
        password = DigestUtils.md5DigestAsHex(password.getBytes());

        if (user == null) {
            return R.error("该账户不存在！请重新输入");
        }
        if (!user.getEmail().equals(email)) {
            return R.error("账户与邮箱不符！请重新输入");
        }
        if (user.getPassword().equals(password)) {
            return R.error("修改的密码不能与之前相同！请重新输入");
        }
        userService.updateByAccount(password, account);
        return R.success("修改成功");
    }
}
