package com.beeline.bot.quizbot.service;

import com.beeline.bot.quizbot.entity.User;

import java.util.List;

/**
 * @author NIsaev on 26.09.2019
 */
public interface UserService {

    User save(User user);

    User create(User user);

    User update(User user);

    User getUserById(int id);

    List<User> getAllUsers();

    User getUserByTlgId(long tlgId);
}
