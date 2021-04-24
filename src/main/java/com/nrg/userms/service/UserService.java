package com.nrg.userms.service;

import com.nrg.userms.model.User;

import java.util.List;
import java.util.Optional;

public interface UserService {
    List<User> getAllUsers();
    Optional<User> getSingleUser(Long userId);
    User createUser(User user);
    void partiallyUpdateUser(User user);
    void updateUser(User user);
    void deleteUser(Long userId);

}
