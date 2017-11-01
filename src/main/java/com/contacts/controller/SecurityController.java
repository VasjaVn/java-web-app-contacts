package com.contacts.controller;

import com.contacts.common.message.Message;
import com.contacts.common.ModelAttributeName;
import com.contacts.common.message.MessageSrcCode;
import com.contacts.common.util.MessageFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Locale;

@Controller
@RequestMapping("/security")
public class SecurityController {
    private final Logger logger = LoggerFactory.getLogger(SecurityController.class);

    private static final String MODEL_ATTR_NAME_MESSAGE = ModelAttributeName.MESSAGE.toString();

    private MessageFactory messageFactory;

    @Autowired
    public void setMessageFactory(MessageFactory messageFactory) {
        this.messageFactory = messageFactory;
    }

    @RequestMapping("/loginfail")
    public String loginFail(Model uiModel, Locale locale) {
        logger.info("Login failed detected");

        Message message = getMessage(MessageSrcCode.MESSAGE_LOGIN_FAIL, null, locale);
        uiModel.addAttribute(MODEL_ATTR_NAME_MESSAGE, message);

        return "contacts/list";
    }

    private Message getMessage(MessageSrcCode messageSrcCode, Object[] args, Locale locale) {
        return messageFactory.getMessage(messageSrcCode, args, locale);
    }
}
