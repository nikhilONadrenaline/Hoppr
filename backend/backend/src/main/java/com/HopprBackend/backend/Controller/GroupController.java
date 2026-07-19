package com.HopprBackend.backend.Controller;

import com.HopprBackend.backend.Service.GroupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/group")
public class GroupController {
    @Autowired
    private GroupService groupService;

    @PostMapping("/addMember/{adminId}/{groupId}/{userId}")
    public void addMember(@PathVariable String adminId, @PathVariable String groupId, @PathVariable String userId)
    {
        groupService.addMembers(groupId,adminId,userId);
    }

    @PostMapping("/addRequest/{groupId}/{userId}")
    public void addRequest(@PathVariable String groupId, @PathVariable String userId)
    {
        groupService.addRequest(groupId,userId);
    }

    @PostMapping("/leaveGroup/{groupId}/{userId}")
    public void leaveGroup(String groupId,String userId)
    {
        groupService.leaveGroup(groupId, userId);
    }



}
