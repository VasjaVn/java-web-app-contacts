package com.contacts.service;

import com.contacts.entity.Contact;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ContactService {
    List<Contact> findAll();
    Contact findById(Long id);
    Page<Contact> findAllByPage(Pageable pageable);
    Contact save(Contact contact);
    void delete(Long id);
}
