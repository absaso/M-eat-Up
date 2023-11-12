package com.example.server.user;

import java.util.Optional;

import com.example.server.Event.SavingUser;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@CrossOrigin
@RestController
@RequestMapping(value = "/Users")
public class UserController {
    public final UserService userService;
    public UserController(UserService userService) {
        this.userService = userService;

    }
    @RequestMapping(method = RequestMethod.POST,value="/connect")
    private User getByLogin(@RequestBody UserDTO userDTO) {
        Optional<User> user = userService.getUserByUsernameAndPassword(userDTO.getLogin(), userDTO.getPwd());
        if (user.isPresent()){
            return user.get();
        }
       else{ return null;}
    }
    @RequestMapping(method = RequestMethod.POST,value="/add")
    private boolean addUser(@RequestBody UserDTO userDTO) {

        Optional<User> user = userService.getUserByUsername(userDTO.getLogin());
        if (user.isPresent()){
            return false;
        }
        else{
            User user1 = new User(userDTO);
            userService.AddUser(user1);
            System.out.println("adding User: " + userDTO);
            return true;}

    }
    @RequestMapping(method = RequestMethod.POST, value = "/history/add")
    private boolean addHistory(@RequestBody SavingUser history) {
        System.out.println(history.toString());

        Optional<User> user = userService.getUserById(history.getIdUser());
        if (user.isPresent()) {
            userService.AddHistory(user.get(), history.getNameResto());
            userService.AddHistoryId(user.get(), history.getIdResto());
            return true;
        } else {
            return false;
        }
    }
    @RequestMapping(method = RequestMethod.POST,value="/modify")
    private User modify(@RequestBody UserDTO userDTO) {
        Optional<User> user = userService.getUserByUsername(userDTO.getLogin());
        if (user.isPresent()) {
            userService.modifyPreference(user.get(), userDTO.getPreferences());
            return user.get();
        }
        else{
            return null;
    }
}
    @RequestMapping(method = RequestMethod.POST, value = "/history/like")
    private User like_history(@RequestBody UserDTO userDTO) {
        Optional<User> user = userService.getUserByUsername(userDTO.getLogin());
        if (user.isPresent()) {
            userService.modifyLikedHistory(user.get(), userDTO.getLiked_restaurants());
            return user.get();
        } else {
            return null;
        }

    }
}
