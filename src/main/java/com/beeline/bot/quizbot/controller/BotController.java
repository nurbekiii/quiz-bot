package com.beeline.bot.quizbot.controller;

import com.beeline.bot.quizbot.entity.User;
import com.beeline.bot.quizbot.service.UserService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author NIsaev on 17.09.2019
 */
@RestController
public class BotController {

    private static Logger logger = LogManager.getLogger(BotController.class);

    @Autowired
    private UserService userService;

    @RequestMapping(value = "/getuser", method = RequestMethod.GET)
    public String sendEmail(@RequestParam String subject, @RequestParam String body) {
        logger.info("Came subject: {}, body: {}", subject, body);

        User usr = userService.getUserById(10);
        System.out.println("User" + usr.toString());

        return "OK";

        //String ss = "940004167:AAH7DbyZ6H4bJDrp6jx8aMdpWcVXlg804nM";
    }

}
