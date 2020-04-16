package com.itheima.hchat.controller;

import com.itheima.hchat.pojo.TbChatRecord;
import com.itheima.hchat.service.ChatRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author GuoJunJiang
 * @version 1.0
 * @date 2020/4/9 10:36
 */
@RestController
@RequestMapping("chatrecord")
public class ChatRecordController {
    @Autowired
    private ChatRecordService chatRecordService;

    @RequestMapping("findByUserIdAndFriendId")
    public List<TbChatRecord> findByUserIdAndFriendId(String userid,String friendid){
        return chatRecordService.findByUserIdAndFriendId(userid,friendid);
    }

    @RequestMapping("findUnreadByUserid")
    public List<TbChatRecord> findUnreadByUserid(String userid){
        return chatRecordService.findUnreadByUserid(userid);
    }
}
