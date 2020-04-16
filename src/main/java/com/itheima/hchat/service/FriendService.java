package com.itheima.hchat.service;

import com.itheima.hchat.mapper.TbChatRecordMapper;
import com.itheima.hchat.mapper.TbFriendMapper;
import com.itheima.hchat.mapper.TbFriendReqMapper;
import com.itheima.hchat.mapper.TbUserMapper;
import com.itheima.hchat.pojo.*;
import com.itheima.hchat.pojo.vo.FriendReq;
import com.itheima.hchat.pojo.vo.Result;
import com.itheima.hchat.pojo.vo.User;
import com.itheima.hchat.util.IdWorker;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author GuoJunJiang
 * @version 1.0
 * @date 2020/4/8 18:42
 */
@Service
@Transactional
public class FriendService {
    @Autowired
    private TbUserMapper tbUserMapper;
    @Autowired
    private IdWorker idWorker;
    @Autowired
    private TbFriendReqMapper tbFriendReqMapper;
    @Autowired
    private TbFriendMapper tbFriendMapper;
    @Autowired
    private TbChatRecordMapper tbChatRecordMapper;

    //添加好友
    public Result sendRequest(TbFriendReq tbFriendReq) {
        String userId = tbFriendReq.getFromUserid();
        String toUserid = tbFriendReq.getToUserid();

        //不能添加自己为好友
        if (userId.equals(toUserid)){
            return new Result(false,"不能添加自己为好友");
        }

        //不能重复添加好友
        TbFriendExample tbFriendReqExample=new TbFriendExample();
        TbFriendExample.Criteria criteria1 = tbFriendReqExample.createCriteria();
        criteria1.andUseridEqualTo(userId);
        criteria1.andFriendsIdEqualTo(toUserid);
        List<TbFriend> list1 = tbFriendMapper.selectByExample(tbFriendReqExample);
        if (!CollectionUtils.isEmpty(list1)&&list1.size()==1){
            return new Result(false,"不能重复添加好友");
        }

        //如果之前发送过请求则更新添加时间
        TbFriendReqExample tbFriendReqExample1=new TbFriendReqExample();
        TbFriendReqExample.Criteria criteria2 = tbFriendReqExample1.createCriteria();
        criteria2.andFromUseridEqualTo(userId);
        criteria2.andToUseridEqualTo(toUserid);
        List<TbFriendReq> tbFriendReqs = tbFriendReqMapper.selectByExample(tbFriendReqExample1);
        if (!CollectionUtils.isEmpty(tbFriendReqs)&&tbFriendReqs.size()==1){
            TbFriendReq tbFriendReq1 = tbFriendReqs.get(0);
            tbFriendReq1.setCreatetime(new Date());
            return new Result(false,"不可重复申请" );
        }

        //添加
        tbFriendReq.setId(idWorker.nextId());
        tbFriendReq.setCreatetime(new Date());
        tbFriendReq.setStatus(0);
        tbFriendReqMapper.insert(tbFriendReq);
        return new Result(true,"已申请" );
    }

    //查询谁添加自己
    public List<FriendReq> findFriendReqByUserid(String userid) {
        TbFriendReqExample tbFriendReqExample=new TbFriendReqExample();
        TbFriendReqExample.Criteria criteria = tbFriendReqExample.createCriteria();
        criteria.andToUseridEqualTo(userid);
        List<TbFriendReq> list = tbFriendReqMapper.selectByExample(tbFriendReqExample);
        List<FriendReq> list1=new ArrayList<>();
        for (TbFriendReq xx:list){
            TbUser tbUser = tbUserMapper.selectByPrimaryKey(xx.getFromUserid());
            FriendReq user=new FriendReq();
            BeanUtils.copyProperties(tbUser,user);
            user.setId(xx.getId());
            list1.add(user);
        }
        return list1;
    }

    //接受好友请求
    public Result acceptFriendReq(String reqid) {
        TbFriendReq tbFriendReq = tbFriendReqMapper.selectByPrimaryKey(reqid);
        String fromUserid = tbFriendReq.getFromUserid();
        String toUserid = tbFriendReq.getToUserid();
        TbFriend tbFriend=new TbFriend();
        tbFriend.setId(idWorker.nextId());
        tbFriend.setUserid(fromUserid);
        tbFriend.setFriendsId(toUserid);
        tbFriend.setCreatetime(new Date());
        tbFriendMapper.insert(tbFriend);
        tbFriend.setId(idWorker.nextId());
        tbFriend.setUserid(toUserid);
        tbFriend.setFriendsId(fromUserid);
        tbFriendMapper.insert(tbFriend);
        tbFriendReqMapper.deleteByPrimaryKey(reqid);
        return new Result(true,"添加成功");
    }

    //拒绝好友请求
    public Result ignoreFriendReq(String reqid) {
        tbFriendReqMapper.deleteByPrimaryKey(reqid);
        return new Result(true,"已拒绝");
    }

    //查询所有好友
    public List<User> findFriendByUserId(String userid) {
        TbFriendExample tbFriendExample=new TbFriendExample();
        TbFriendExample.Criteria criteria = tbFriendExample.createCriteria();
        criteria.andUseridEqualTo(userid);
        List<TbFriend> list = tbFriendMapper.selectByExample(tbFriendExample);
        List<User> users=new ArrayList<>();
        for (TbFriend xx:list){
            TbUser tbUser = tbUserMapper.selectByPrimaryKey(xx.getFriendsId());
            User user=new User();
            BeanUtils.copyProperties(tbUser,user);
            users.add(user);
        }
        return users;
    }
}
