package com.itheima.hchat.controller;

import com.itheima.hchat.pojo.TbFriendReq;
import com.itheima.hchat.pojo.vo.FriendReq;
import com.itheima.hchat.pojo.vo.Result;
import com.itheima.hchat.pojo.vo.User;
import com.itheima.hchat.service.FriendService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author GuoJunJiang
 * @version 1.0
 * @date 2020/4/8 18:41
 */
@RestController
@RequestMapping("friend")
public class FriendController {

    @Autowired
    private FriendService friendService;

    @RequestMapping("sendRequest")
    public Result sendRequest(@RequestBody TbFriendReq tbFriendReq){
        return friendService.sendRequest(tbFriendReq);
    }

    @RequestMapping("findFriendReqByUserid")
    public List<FriendReq> findFriendReqByUserid(String userid){
        return friendService.findFriendReqByUserid(userid);
    }

    @RequestMapping("acceptFriendReq")
    public Result acceptFriendReq(String reqid){
        return friendService.acceptFriendReq(reqid);
    }

    @RequestMapping("ignoreFriendReq")
    public Result ignoreFriendReq(String reqid){
        return friendService.ignoreFriendReq(reqid);
    }

    @RequestMapping("findFriendByUserid")
    public List<User> findFriendByUserId(String userid){
        return friendService.findFriendByUserId(userid);
    }
}
