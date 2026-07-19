package com.HopprBackend.backend.Controller;

import com.HopprBackend.backend.Dto.UserResponseDto;
import com.HopprBackend.backend.Service.GroupService;
import com.HopprBackend.backend.Service.PrivateGroupService;
import com.HopprBackend.backend.Service.SessionService;
import com.HopprBackend.backend.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/user")
public class UserController {
    @Autowired
    private SessionService sessionService;
    @Autowired
    private UserService userService;
    @Autowired
    private PrivateGroupService privateGroupService;
    @Autowired
    private GroupService groupService;

    @GetMapping("/onlineUsers")
    public List<UserResponseDto> onlineUsers(){
        List<String> userIds =sessionService.getUsers();

        if(userIds.isEmpty()) throw new RuntimeException("No user is online");

        List<UserResponseDto> users=new ArrayList<>();

        for(String id: userIds)
        {
            users.add(userService.fetchUser(id));
        }

        return users;
    }

    @PostMapping("/connect/{adminId}/{receiverId}")
    public String connect(@PathVariable String receiverId, @PathVariable String adminId)
    {
           return privateGroupService.createPrivateGroup(receiverId,adminId); //Group Id mil gya
    }

    @PostMapping("/createGroup/{adminId}")
    public String createGroup(@PathVariable String adminId, @RequestBody String name)
    {
        return groupService.createGroup(name, adminId);
    }

    @DeleteMapping("/deleteGroup/{adminId}/{groupId}")
    public void deleteGroup(@PathVariable String groupId,@PathVariable String adminId )
    {
        groupService.deleteGroup(groupId,adminId);
    }


}
