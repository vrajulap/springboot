package com.edureka.userms.service;

import com.edureka.userms.model.User;
import com.edureka.userms.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {
    private static final Logger LOGGER = LoggerFactory.getLogger(UserServiceImpl.class);
    private final UserRepository userRepository;
    private final RestTemplate restTemplate;

    public UserServiceImpl(UserRepository userRepository, RestTemplate restTemplate) {
        this.userRepository = userRepository;
        this.restTemplate = restTemplate;
    }

    @Override
    public List<User> getAllUsers() {
        LOGGER.info("************");
        LOGGER.info("getting all users");
        return userRepository.findAll();
    }

    @Override
    public Optional<User> getSingleUser(Long userId) {
        LOGGER.info("************");
        LOGGER.info("getting single user");
        return userRepository.findById(userId);
    }

    @Override
    public User createUser(User user) {
        User userCreated = userRepository.save(user);
        return userCreated;
    }

    @Override
    public void partiallyUpdateUser(User user) {

    }

    @Override
    public void updateUser(User user) {

    }

    @Override
    public void deleteUser(Long userId) {

    }

    @Override
    public Object getAllOrders() {
        return restTemplate.getForObject("http://orderms/orders", Object.class);
    }
}
