package com.example.clearsolutions.service;

import com.example.clearsolutions.model.User;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.Period;
import java.util.*;

@Service
public class UserServiceImpl {
    private static Map<Long, User> users = new HashMap<>();
    private static Long index = 2L;

    static {
        User user1 = new User (1L, "Jonh@gmail.com", "John", "Johnson", LocalDate.of(2000, 5, 14), "Kyiv");
        User user2 = new User (2L, "Alice@gmail.com", "Alice", "Stockton", LocalDate.of(1994, 7, 4), "Kyiv");
    }

    public static User addUser (User user) {
        index += 1;
        user.setId(index);
        users.put(index, user);
        return user;
    }

    public static List<User> getAllUsers () {
        return new ArrayList<>(users.values());
    }

    public User createUser(User user) {
        LocalDate birthDate = user.getBirthDate();
        if (!isUserOldEnough(birthDate)) {
            throw new IllegalArgumentException("User must be at least 18 years old.");
        }

        long nextId = getNextUserId();
        user.setId(nextId);
        users.put(nextId, user);
        return user;
    }

    private long getNextUserId() {
        return users.size() + 1L;
    }

    private static boolean isUserOldEnough(LocalDate birthDate) {
        LocalDate currentDate = LocalDate.now();
        int age = Period.between(birthDate, currentDate).getYears();
        return age >= 18;
    }

    public static User updateUser (Long userId, User user) {
        user.setId(userId);
        users.put(userId, user);
        return user;
    }

    public static User deleteUser (Long userId) {
        return users.remove(userId);
    }
}
