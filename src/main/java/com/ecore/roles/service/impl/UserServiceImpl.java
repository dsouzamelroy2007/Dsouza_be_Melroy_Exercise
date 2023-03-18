package com.ecore.roles.service.impl;

import com.ecore.roles.client.UserClient;
import com.ecore.roles.client.model.User;
import com.ecore.roles.exception.ResourceNotFoundException;
import com.ecore.roles.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class UserServiceImpl implements UserService {

    private final UserClient userClient;

    @Autowired
    public UserServiceImpl(UserClient userClient) {
        this.userClient = userClient;
    }

    public User getUser(UUID id) {
        User user = userClient.getUser(id).getBody();
        if (user == null) {
            throw new ResourceNotFoundException(User.class, id);
        }
        return user;
    }

    public List<User> getUsers() {
        return userClient.getUsers().getBody();
    }
}
