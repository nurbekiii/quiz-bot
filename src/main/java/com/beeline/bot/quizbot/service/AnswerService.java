package com.beeline.bot.quizbot.service;


import com.beeline.bot.quizbot.entity.Answer;

import java.util.List;

/**
 * @author NIsaev on 03.10.2019
 */
public interface AnswerService {

    Answer save(Answer task);

    Answer create(Answer task);

    Answer update(Answer task);

    Answer getUserById(long id);

    List<Answer> getAll();
}
