package com.beeline.bot.quizbot.service;

import com.beeline.bot.quizbot.entity.PhotoSmile;

import java.util.List;

/**
 * @author NIsaev on 01.10.2019
 */
public interface PhotoSmileService {
    PhotoSmile save(PhotoSmile smile);

    PhotoSmile create(PhotoSmile smile);

    PhotoSmile update(PhotoSmile smile);

    PhotoSmile getPhotoSmileById(int id);

    List<PhotoSmile> getAllPhotoSmiles();
}
