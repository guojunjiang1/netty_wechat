package com.itheima.hchat.service;

import com.itheima.hchat.mapper.TbChatRecordMapper;
import com.itheima.hchat.pojo.TbChatRecord;
import com.itheima.hchat.pojo.TbChatRecordExample;
import com.itheima.hchat.util.IdWorker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

/**
 * @author GuoJunJiang
 * @version 1.0
 * @date 2020/4/9 9:59
 */
@Service
@Transactional
public class ChatRecordService {
    @Autowired
    private TbChatRecordMapper tbChatRecordMapper;
    @Autowired
    private IdWorker idWorker;

    //将聊天记录保存到数据库中
    public void insert(TbChatRecord chatRecord) {
        chatRecord.setId(idWorker.nextId());
        chatRecord.setHasRead(0);
        chatRecord.setCreatetime(new Date());
        chatRecord.setHasDelete(0);
        tbChatRecordMapper.insert(chatRecord);
    }

    //查询聊天记录
    public List<TbChatRecord> findByUserIdAndFriendId(String userid, String friendid) {
        TbChatRecordExample tbChatRecordExample=new TbChatRecordExample();
        TbChatRecordExample.Criteria criteria = tbChatRecordExample.createCriteria();
        TbChatRecordExample.Criteria criteria1 = tbChatRecordExample.createCriteria();
        criteria.andUseridEqualTo(userid);
        criteria.andFriendidEqualTo(friendid);
        criteria.andHasDeleteEqualTo(0);
        criteria1.andUseridEqualTo(friendid);
        criteria1.andFriendidEqualTo(userid);
        criteria1.andHasDeleteEqualTo(0);
        tbChatRecordExample.or(criteria);
        tbChatRecordExample.or(criteria1);

        TbChatRecordExample tbChatRecordExample1=new TbChatRecordExample();
        TbChatRecordExample.Criteria criteria2 = tbChatRecordExample1.createCriteria();
        criteria2.andUseridEqualTo(friendid);
        criteria2.andFriendidEqualTo(userid);
        criteria2.andHasDeleteEqualTo(0);
        criteria2.andHasReadEqualTo(0);
        List<TbChatRecord> list = tbChatRecordMapper.selectByExample(tbChatRecordExample1);
        for (TbChatRecord xx:list){
            xx.setHasRead(1);
            tbChatRecordMapper.updateByPrimaryKey(xx);
        }

        return tbChatRecordMapper.selectByExample(tbChatRecordExample);
    }

    //查询未读消息
    public List<TbChatRecord> findUnreadByUserid(String userid) {
        TbChatRecordExample tbChatRecordExample=new TbChatRecordExample();
        TbChatRecordExample.Criteria criteria = tbChatRecordExample.createCriteria();
        criteria.andFriendidEqualTo(userid);
        criteria.andHasReadEqualTo(0);
        return tbChatRecordMapper.selectByExample(tbChatRecordExample);
    }

    //设置消息为已读
    public void updateStatusHasRead(String id) {
        TbChatRecord tbChatRecord = tbChatRecordMapper.selectByPrimaryKey(id);
        tbChatRecord.setHasRead(1);
        tbChatRecordMapper.updateByPrimaryKey(tbChatRecord);
    }
}
