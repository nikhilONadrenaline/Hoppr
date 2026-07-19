package com.HopprBackend.backend.Service;

import com.HopprBackend.backend.Entity.ApproveUser;
import com.HopprBackend.backend.Entity.Group;
import com.HopprBackend.backend.Enum.ConversationType;
import com.HopprBackend.backend.Repository.ApproveRepository;
import com.HopprBackend.backend.Repository.GroupRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Service
public class GroupService {

    @Autowired
    private GroupRepository groupRepository;
    @Autowired
    private ApproveRepository approveRepository;
    @Autowired
    private  RedisTemplate<String, String> groupRedisTemplate;


    public List<String> getMembers(String groupId) {
        Set<String> memberSet=groupRedisTemplate.opsForSet().members("group:"+groupId);
        if(memberSet!=null && !memberSet.isEmpty()) return new ArrayList<>(memberSet);

        Group group=groupRepository.findById(groupId).orElseThrow(()->new RuntimeException("No group exists"));  //WORKS FOR PRIVATE AND GROUP BOTH
        List<String> members = group.getMemberIds();


        if (members != null && !members.isEmpty()) {
            groupRedisTemplate.opsForSet().add("group:"+groupId, members.toArray(new String[0]));
        }

        return members;
    }

    public void addMembers(String groupId, String userId, String memberId)
    {
        Group group=groupRepository.findById(groupId).orElseThrow(()->new RuntimeException("No group exists"));
        if(!userId.equals(group.getAdminId()))
        {
            throw new RuntimeException("Only admin can add a member");
        }
        List<String> list=group.getMemberIds();
        list.add(memberId);
        group.setMemberIds(list);
        groupRepository.save(group);
        groupRedisTemplate.opsForSet().add("group:"+groupId,memberId);
    }

    public void addRequest(String groupId, String userId)
    {
        ApproveUser user=new ApproveUser();
        user.setUserId(userId);
        user.setGroupId(groupId);
        approveRepository.save(user);
    }


    public void leaveGroup(String groupId,String userId)
    {
        Group group=groupRepository.findById(groupId).orElseThrow(()->new RuntimeException("No group exists"));
        if(group.getAdminId().equals(userId)) throw new RuntimeException("Admin can't leave the group");

        List<String> list=group.getMemberIds();

        List<String> newList=new ArrayList<>();
        for(String str: list)
        {
            if(str.equals(userId)) continue;
            newList.add(str);
        }

        group.setMemberIds(newList);
        groupRepository.save(group);
        groupRedisTemplate.opsForSet().remove("group:"+groupId,userId);
    }

    public String createGroup(String name, String adminId)
    {
        Group group=new Group();
        group.setName(name);

        List<String> members=new ArrayList<>();
        members.add(adminId);

        group.setMemberIds(members);
        group.setAdminId(adminId);
        group.setLastActive(LocalDateTime.now());
        group.setConversationType(ConversationType.GROUP);

        groupRepository.save(group);
        groupRedisTemplate.opsForSet().add("group:"+group.getGroupId(),adminId);
        return group.getGroupId();
    }
    public void deleteGroup(String groupId, String userId)
    {
        Group group=groupRepository.findById(groupId).orElseThrow(()->new RuntimeException("No group exists"));
        if(!userId.equals(group.getAdminId()))
        {
            throw new RuntimeException("Only admin can delete this group");
        }

        groupRepository.deleteById(groupId);
        groupRedisTemplate.delete("group:" + groupId);
    }

}
