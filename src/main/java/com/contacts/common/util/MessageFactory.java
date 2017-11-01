package com.contacts.common.util;

import com.contacts.common.message.Message;
import com.contacts.common.message.MessageType;
import com.contacts.common.message.MessageSrcCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;

import java.util.Locale;

@Component
public class MessageFactory {

    private MessageSource messageSource;

    @Autowired
    public void setMessageSource(MessageSource messageSource) {
        this.messageSource = messageSource;
    }

    public Message getMessage(MessageSrcCode messageSrcCode, Object[] args, Locale locale) {
        String messageType = MessageType.ERROR.toString();
        String messageText = "";

        switch (messageSrcCode) {
            case MESSAGE_LOGIN_FAIL: {
                messageText = getMessageForMsgSrcCode(MessageSrcCode.MESSAGE_LOGIN_FAIL, args, locale );
                break;
            }

            case CONTACT_SAVE_FAIL: {
                messageText = getMessageForMsgSrcCode(MessageSrcCode.CONTACT_SAVE_FAIL, args, locale );
                break;
            }

            case CONTACT_SAVE_SUCCESS: {
                messageType = MessageType.SUCCESS.toString();
                messageText = getMessageForMsgSrcCode(MessageSrcCode.CONTACT_SAVE_SUCCESS, args, locale );
                break;
            }

            case CONTACT_DELETE_SUCCESS: {
                messageType = MessageType.SUCCESS.toString();
                messageText = getMessageForMsgSrcCode(MessageSrcCode.CONTACT_DELETE_SUCCESS, args, locale );
                break;
            }
        }

        return new Message(messageType, messageText);
    }

    private String getMessageForMsgSrcCode(MessageSrcCode messageSrcCode, Object[] args, Locale locale) {
        return messageSource.getMessage(messageSrcCode.toString(), args, locale);
    }
}