package com.beeline.bot.quizbot.service;

import com.beeline.bot.quizbot.entity.QuizText;
import com.beeline.bot.quizbot.entity.QuizText;

import java.util.List;

/**
 * @author NIsaev on 26.09.2019
 */
public interface QuizTextService {

    QuizText save(QuizText quiz);

    QuizText create(QuizText quiz);

    QuizText update(QuizText quiz);

    QuizText getUserById(long id);

    List<QuizText> getAll();
}
