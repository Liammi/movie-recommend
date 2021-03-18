package me.quinn.movie.service;

import me.quinn.movie.domain.User;

public interface UserService {
    User findByUserName(String userName);

    User save(User user);
}
