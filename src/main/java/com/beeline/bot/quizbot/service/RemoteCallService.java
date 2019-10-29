package com.beeline.bot.quizbot.service;

import java.util.List;

/**
 * @author NIsaev on 26.09.2019
 */
public interface RemoteCallService<T> {
    T sendPostRequest(String url, Object req, Class<T> class1) throws Exception;

    T sendPutRequest(String url, Object req, Class<T> class1) throws Exception;

    T sendGetRequest(String url, Object req, Class<T> class1) throws Exception;

    T getSimpleObject(String url, Class<T> class1) throws Exception;

    List<T> getAll(String url, Class<T> class2) throws Exception;
}
