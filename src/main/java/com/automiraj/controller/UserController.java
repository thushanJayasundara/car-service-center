package com.automiraj.controller;

import com.automiraj.dto.UserDTO;
import com.automiraj.service.UserService;
import com.automiraj.util.CommonResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping ("v1/user-management-api")
public class UserController {
    
    private UserService userService;

    @Autowired
    public UserController (UserService userService){this.userService = userService;}

    @PostMapping("/")
    public CommonResponse save(@RequestBody UserDTO userDTO) {return userService.save(userDTO);}

    @PutMapping("/")
    public CommonResponse update(@RequestBody UserDTO userDTO) {return userService.update(userDTO);}

    @DeleteMapping("/{id}")
    public CommonResponse delete(@PathVariable Long id) {return userService.delete(id);}

    @GetMapping("/")
    public CommonResponse getAll(){return userService.getAll();}

    @GetMapping("/{id}")
    public CommonResponse findById(@PathVariable String id){return userService.findById(id);}

    @PutMapping("/updatePassword")
    public CommonResponse updatePassword(@RequestBody UserDTO userDTO){return userService.updatePassword(userDTO); }

    @GetMapping("/get-by-username/{userName}")
    public CommonResponse findByUserName(@PathVariable String userName){return userService.findByUserName(userName);}
}
