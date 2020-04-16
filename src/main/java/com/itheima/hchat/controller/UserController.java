package com.itheima.hchat.controller;

import com.itheima.hchat.pojo.TbUser;
import com.itheima.hchat.pojo.vo.Result;
import com.itheima.hchat.pojo.vo.User;
import com.itheima.hchat.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * @author GuoJunJiang
 * @version 1.0
 * @date 2020/4/8 11:18
 */
@RestController
@RequestMapping("user")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping("findAll")
    public List<TbUser> findAll(){
        return userService.findAll();
    }

    @RequestMapping("login")
    public Result login(@RequestBody TbUser user){
        return userService.login(user);
    }

    @RequestMapping("register")
    public Result register(@RequestBody TbUser tbUser){
        return userService.register(tbUser);
    }

    @RequestMapping("upload")
    public Result upload(MultipartFile file,String userid){
        return userService.upload(file,userid);
    }

    @RequestMapping("updateNickname")
    public Result updateNickname(@RequestBody TbUser tbUser){
        return userService.updateNickname(tbUser);
    }

    @RequestMapping("findById")
    public User findById(String userid){
        return userService.findById(userid);
    }

    @RequestMapping("findByUsername")
    public Result search(String userid,String friendUsername){
        return userService.search(userid,friendUsername);
    }
}
