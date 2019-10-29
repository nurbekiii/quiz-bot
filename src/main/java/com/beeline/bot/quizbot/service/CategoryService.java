package com.beeline.bot.quizbot.service;


import com.beeline.bot.quizbot.entity.Category;

import java.util.List;

/**
 * @author NIsaev on 18.10.2019
 */
public interface CategoryService {

    Category save(Category category);

    Category create(Category category);

    Category update(Category category);

    Category getCategoryById(long id);

    List<Category> getAll();
}
