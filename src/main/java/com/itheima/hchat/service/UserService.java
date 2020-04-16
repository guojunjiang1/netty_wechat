package com.itheima.hchat.service;

import com.itheima.hchat.mapper.TbChatRecordMapper;
import com.itheima.hchat.mapper.TbFriendMapper;
import com.itheima.hchat.mapper.TbFriendReqMapper;
import com.itheima.hchat.mapper.TbUserMapper;
import com.itheima.hchat.pojo.*;
import com.itheima.hchat.pojo.vo.Result;
import com.itheima.hchat.pojo.vo.User;
import com.itheima.hchat.util.IdWorker;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.DigestUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.Date;
import java.util.List;

/**
 * @author GuoJunJiang
 * @version 1.0
 * @date 2020/4/8 11:19
 */
@Service
@Transactional
public class UserService {
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

    public List<TbUser> findAll() {
        return tbUserMapper.selectByExample(null);
    }

    //用户登录
    public Result login(TbUser user) {
        String username = user.getUsername();
        String password = user.getPassword();
        if (StringUtils.isEmpty(username)||StringUtils.isEmpty(password)){
            return new Result(false,"参数错误",null);
        }
        TbUserExample tbUserExample = new TbUserExample();
        TbUserExample.Criteria criteria = tbUserExample.createCriteria();
        criteria.andUsernameEqualTo(username);
        List<TbUser> list = tbUserMapper.selectByExample(tbUserExample);
        if (CollectionUtils.isEmpty(list)||list.size()!=1){
            return new Result(false,"账号不存在",null);
        }
        String newPassword = list.get(0).getPassword();
        String s = DigestUtils.md5DigestAsHex(password.getBytes());
        if (!s.equals(newPassword)){
            return new Result(false,"密码错误",null);
        }
        User user1=new User();
        BeanUtils.copyProperties(list.get(0),user1);
        return new Result(true,"登录成功",user1);
    }

    //用户注册
    public Result register(TbUser user) {
        String username = user.getUsername();
        String password = user.getPassword();
        if (StringUtils.isEmpty(username)||StringUtils.isEmpty(password)){
            return new Result(false,"参数错误",null);
        }
        TbUserExample tbUserExample=new TbUserExample();
        TbUserExample.Criteria criteria = tbUserExample.createCriteria();
        criteria.andUsernameEqualTo(username);
        List<TbUser> list = tbUserMapper.selectByExample(tbUserExample);
        if (!CollectionUtils.isEmpty(list)||list.size()==1){
            return new Result(false,"账号已存在",null);
        }
        user.setId(idWorker.nextId());
        user.setPassword(DigestUtils.md5DigestAsHex(password.getBytes()));
        user.setNickname(username);
        user.setCreatetime(new Date());
        tbUserMapper.insert(user);
        return new Result(true,"注册成功");
    }

    //上传头像
    public Result upload(MultipartFile file, String userid) {
        String originalFilename = file.getOriginalFilename();
        try {
            file.transferTo(new File("E:\\leyouimages\\" + originalFilename));
            TbUser user = tbUserMapper.selectByPrimaryKey(userid);
            user.setPicNormal("E:\\leyouimages\\" + originalFilename);
            user.setPicSmall("E:\\leyouimages\\" + originalFilename);
            tbUserMapper.updateByPrimaryKey(user);
            User user1=new User();
            BeanUtils.copyProperties(user,user1);
            return new Result(true,"上传成功",user1);
        }catch (Exception e){
            return new Result(false,"上传失败");
        }
    }

    //修改昵称
    public Result updateNickname(TbUser tbUser) {
        String id = tbUser.getId();
        String nickname = tbUser.getNickname();
        if (StringUtils.isEmpty(id)||StringUtils.isEmpty(nickname)){
            return new Result(false,"参数错误");
        }
        TbUser tbUser1 = tbUserMapper.selectByPrimaryKey(id);
        tbUser1.setNickname(nickname);
        tbUserMapper.updateByPrimaryKey(tbUser1);
        return new Result(true,"修改成功");
    }

    //根据用户id查找用户
    public User findById(String userid) {
        TbUser tbUser = tbUserMapper.selectByPrimaryKey(userid);
        User user=new User();
        BeanUtils.copyProperties(tbUser,user);
        return user;
    }

    //搜索好友
    public Result search(String userId, String friendUsername) {
        TbUserExample tbUserExample=new TbUserExample();
        TbUserExample.Criteria criteria = tbUserExample.createCriteria();
        criteria.andUsernameEqualTo(friendUsername);
        List<TbUser> list = tbUserMapper.selectByExample(tbUserExample);
        if (CollectionUtils.isEmpty(list)||list.size()!=1){
            return new Result(false,"用户不存在");
        }

        User friendUser=new User();
        BeanUtils.copyProperties(list.get(0),friendUser);
        return new Result(true,"搜索成功",friendUser);
    }


}
