package com.contacts.controller;

import com.contacts.dto.outer.ContactInfo;
import com.contacts.entity.Contact;
import com.contacts.dto.outer.response.Response;
import com.contacts.dto.outer.response.ResponseContacts;
import com.contacts.service.ContactService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.List;


@org.springframework.web.bind.annotation.RestController
@RequestMapping(value = "/rest")
public class RestController {
    private final Logger logger = LoggerFactory.getLogger(RestController.class);

    private ContactService contactService;

    @Autowired
    public void setContactService(ContactService contactService) {
        this.contactService = contactService;
    }

    @RequestMapping(value = "/test", method = RequestMethod.GET)
    @ResponseBody
    public String test() {
        logger.info("rest");
        return "__test__";
    }

    @RequestMapping(value = "/contacts", method = RequestMethod.GET, produces = "application/json")
    public ResponseContacts contacts() {
        ResponseContacts response = new ResponseContacts();

        try {
            List<Contact> entityContacts = contactService.findAll();
            if ( !entityContacts.isEmpty() ) {
                List<ContactInfo> contacts = new ArrayList<>( entityContacts.size() );
                for ( Contact entityContact : entityContacts ) {
                    ContactInfo contactInfo = new ContactInfo();
                    contactInfo.setId( entityContact.getId() );
                    contactInfo.setFirstName( entityContact.getFirstName() );
                    contactInfo.setLastName( entityContact.getLastName() );
                    contactInfo.setDescription( entityContact.getDescription() );
                    contactInfo.setBirthDate( entityContact.getBirthDateString() );
                    contacts.add( contactInfo );
                }

                response.setContacts( contacts );
                response.setStatus( Response.STATUS_SUCCESS );
            }
        } catch (Exception ex) {
            response.setStatus( Response.STATUS_FAILED );
        }

        return response;
    }
}