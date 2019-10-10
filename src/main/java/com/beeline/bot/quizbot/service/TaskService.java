package com.beeline.bot.quizbot.service;


import com.beeline.bot.quizbot.entity.Task;

import java.util.List;

/**
 * @author NIsaev on 02.10.2019
 */
public interface TaskService {

    Task save(Task task);

    Task create(Task task);

    Task update(Task task);

    Task getUserById(long id);

    List<Task> getAll();
}
