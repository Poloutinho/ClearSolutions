package com.example.clearsolutions.service;

import com.example.clearsolutions.model.User;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;

import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.Period;
import java.time.ZoneId;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl {


    private static final String EMAIL_PATTERN = "^[a-z0-9._%+-]+@[a-z0-9.-]+\\.[a-z]{2,4}$";
    private static Map<Long, User> users = new HashMap<>();

    private static Long index = 0L;

    public static List<User> getAllUsers () {
        return new ArrayList<>(users.values());
    }

    public User createUser(User user) {
        Date birthDate = user.getBirthDate();
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

    private static boolean isUserOldEnough(Date birthDate) {
        LocalDate currentDate = LocalDate.now();
        LocalDate birthLocalDate = birthDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        int age = Period.between(birthLocalDate, currentDate).getYears();
        return age >= 18;
    }

    public static User deleteUser (Long userId) {
        return users.remove(userId);
    }
    public static User getUserById(Long userId) throws SQLException {
        return users.get(userId);
    }

    public User update(Long userId, String email) throws SQLException {
        Optional<User> userToUpdate = Optional.ofNullable(getUserById(userId));
        if (userToUpdate.isPresent()) {
                userToUpdate.get().setEmail(email);
                users.put(userToUpdate.get().getId(), userToUpdate.get());
                return userToUpdate.get();
        } else {
            throw new NoSuchElementException("User not found");
        }
    }

    public User updateAllFields(@PathVariable Long userId, User user) throws SQLException {
        Optional<User> userFullUpdate = Optional.ofNullable(getUserById(userId));
        if (userFullUpdate.isPresent()) {
            if ((LocalDate.now().minusYears(user.getBirthDate().getYear()).getYear()) >= 18 &&
                    user.getEmail().matches(EMAIL_PATTERN)) {
                userFullUpdate.get().setEmail(user.getEmail());
                userFullUpdate.get().setFirstName(user.getFirstName());
                userFullUpdate.get().setLastName(user.getLastName());
                userFullUpdate.get().setBirthDate(user.getBirthDate());
                userFullUpdate.get().setAddress(user.getAddress());
                users.put(userFullUpdate.get().getId(), userFullUpdate.get());
                return userFullUpdate.get();
            } else {
                throw new IllegalArgumentException("Illegal argument for user");
            }
        } else {
            throw new NoSuchElementException("User not found");
        }
    }
    public List<User> findByBirthdayRange(Date from, Date to) {
        List<User> result = new ArrayList<>();

        for (User user : users.values()) {
            if (user.getBirthDate().after(from) && user.getBirthDate().before(to)) {
                result.add(user);
            }
        }
        return result;
    }
}