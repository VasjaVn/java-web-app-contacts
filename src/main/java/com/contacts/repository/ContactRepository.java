package com.contacts.repository;

import com.contacts.entity.Contact;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ContactRepository
        extends PagingAndSortingRepository<Contact, Long> {
}