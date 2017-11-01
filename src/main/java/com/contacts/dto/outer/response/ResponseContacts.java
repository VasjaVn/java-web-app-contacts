package com.contacts.dto.outer.response;

import com.contacts.dto.outer.ContactInfo;

import java.util.List;

public class ResponseContacts extends Response {
    private List<ContactInfo> contacts;

    public List<ContactInfo> getContacts() {
        return contacts;
    }
    public void setContacts(List<ContactInfo> contacts) {
        this.contacts = contacts;
    }
}
