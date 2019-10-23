package com.beeline.bot.quizbot.service;


import com.beeline.bot.quizbot.entity.UserComment;

import java.util.List;

/**
 * @author NIsaev on 11.10.2019
 */
public interface UserCommentService {

    UserComment save(UserComment userComment);

    UserComment create(UserComment userComment);

    UserComment update(UserComment userComment);

    UserComment getUserCommentById(long id);

    List<UserComment> getAll();
}
