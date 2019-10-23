package com.beeline.bot.quizbot.service;


import com.beeline.bot.quizbot.entity.Answer;
import com.beeline.bot.quizbot.entity.TaskFilter;

import java.util.List;

/**
 * @author NIsaev on 03.10.2019
 */
public interface AnswerService {

    Answer save(Answer task);

    Answer create(Answer task);

    Answer update(Answer task);

    Answer getAnswerById(long id);

    List<Answer> getAll();

    List<Answer> getAnswersByFilter(TaskFilter filter);
}
