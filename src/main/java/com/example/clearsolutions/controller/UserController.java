package com.example.clearsolutions.controller;

import com.example.clearsolutions.model.User;
import com.example.clearsolutions.service.UserServiceImpl;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLException;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserServiceImpl userService;

    public UserController(UserServiceImpl userService) {
        this.userService = userService;
    }
    @GetMapping("/all")
    public ResponseEntity<List<User>> getAllUsers() {
        List<User> users = userService.getAllUsers();
        return new ResponseEntity<>(users, HttpStatus.OK);
    }

    @PostMapping("/create")
    public User addUser(@RequestBody User user) {
        return userService.createUser(user);
    }
    @DeleteMapping("/delete/{userId}")
    public User deleteUser(@PathVariable Long userId) {
        return userService.deleteUser(userId);
    }
    @PatchMapping("/update/{userId}")
    public User updateUser(@PathVariable Long userId,String email) throws SQLException {
        return userService.update(userId, email);
    }
    @PutMapping("/updateAllFields/{userId}")
    public User updateAllFieldsUser(@PathVariable Long userId, User user) throws SQLException {
        return userService.updateAllFields(userId, user);
    }

    @GetMapping("/getInRange")
    public List<User> getUserByBirthday(@RequestParam Date from, @RequestParam Date to) {
        return userService.findByBirthdayBetween(from, to);
    }
}
//{
//        "id": 1,
//        "email": "user@example.com",
//        "firstName": "John",
//        "lastName": "Doe",
//        "birthDate": "1990-01-15",
//        "address": "123 Main St"
//        }

