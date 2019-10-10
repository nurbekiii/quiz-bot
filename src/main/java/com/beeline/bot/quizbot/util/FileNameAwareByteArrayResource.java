package com.beeline.bot.quizbot.util;

import org.springframework.core.io.ByteArrayResource;

/**
 * @author NIsaev on 02.10.2019
 */
public class FileNameAwareByteArrayResource extends ByteArrayResource {

    private String fileName;

    public FileNameAwareByteArrayResource(String fileName, byte[] byteArray, String description) {
        super(byteArray, description);
        this.fileName = fileName;
    }

    @Override
    public String getFilename() {
        return fileName;
    }
}
