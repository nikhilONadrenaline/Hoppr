package com.HopprBackend.backend.Service;

import com.HopprBackend.backend.Entity.Group;
import com.HopprBackend.backend.Enum.ConversationType;
import com.HopprBackend.backend.Repository.GroupRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class PrivateGroupService {

    @Autowired
    private GroupRepository groupRepository;
    @Autowired
    private RedisTemplate<String, String> groupRedisTemplate;

    public String createPrivateGroup(String adminId, String receiverId)
    {
        Group group=new Group();
        group.setName("Private Chat Room");

        List<String> members=new ArrayList<>();
        members.add(adminId);
        members.add(receiverId);

        group.setMemberIds(members);
        group.setAdminId(adminId);
        group.setLastActive(LocalDateTime.now());
        group.setConversationType(ConversationType.PRIVATE);

        groupRepository.save(group);
        groupRedisTemplate.opsForSet().add("group:"+group.getGroupId(),adminId);
        groupRedisTemplate.opsForSet().add("group:"+group.getGroupId(),receiverId);
        return group.getGroupId();
    }
}
