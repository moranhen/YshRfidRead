package com.ysh.rfid.controller;

import cn.hutool.core.lang.Dict;

import com.ysh.rfid.entity.User;
import com.ysh.rfid.exception.ResponseObject;
import com.ysh.rfid.service.IUserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@Slf4j
public class UserController {
    private final IUserService userService;

    @Autowired
    public UserController(IUserService userService) {
        this.userService = userService;
    }

    @PostMapping("/user")
    public Dict save(@RequestBody User user) {
        Boolean save = userService.save(user);
        if (save){
            return Dict.create().set("code", save ? 200 : 500).set("msg", save ? "成功" : "失败").set("data", save ? user : null);
        }else {
            return Dict.create().set("code",ResponseObject.fail().getCode()).set("msg",ResponseObject.fail().getMsg()).set("data",ResponseObject.fail().getData());
        }
    }

    @DeleteMapping("/user/{uid}")
    public Dict delete(@PathVariable String uid) {
        Boolean delete = userService.delete(uid);
        return Dict.create().set("code", delete ? 200 : 500).set("msg", delete ? "成功" : "失败").set("data",null);

//        if(delete){
//
//        }else {
//            return Dict.create().set("code",ResponseObject.fail().getCode()).set("msg",ResponseObject.fail().getMsg()).set("data",ResponseObject.fail().getData());
//        }
    }

    @PutMapping("/user/{uid}")
    public Dict update(@RequestBody User user, @PathVariable String uid) {
        Boolean update = userService.update(user, uid);
        if(update){
            return Dict.create().set("code", update ? 200 : 500).set("msg", update ? "成功" : "失败").set("data", update ? user : null);
        }else {
            return Dict.create().set("code",ResponseObject.fail().getCode()).set("msg",ResponseObject.fail().getMsg()).set("data",ResponseObject.fail().getData());
        }
    }

    @GetMapping("/user/{uid}")
    public Dict getUser(@PathVariable String id) {
        User user = userService.getUser(id);
        return Dict.create().set("code", 200).set("msg", "成功").set("data", user);
    }

    @GetMapping("/user")
    public Dict getUser(User user) {
        List<User> userList = userService.getUser(user);
        return Dict.create().set("code", 200).set("msg", "成功").set("data", userList);
    }
    @GetMapping("/user/{kind}")
    public Dict getKind(User user,@PathVariable String kind){
        List<User> userList = userService.getKind(user,kind);
        return Dict.create().set("code", 200).set("msg", "成功").set("data", userList);
    }
    @GetMapping("/user/{name}")
    public Dict getName(@PathVariable String name){
        User user = userService.getName(name);
        return Dict.create().set("code", 200).set("msg", "成功").set("data", user);
    }
}
