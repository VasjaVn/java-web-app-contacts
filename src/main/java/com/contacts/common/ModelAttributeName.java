package com.contacts.common;

public enum ModelAttributeName {
    MESSAGE("message"), CONTACT("contact");

    private final String attributeName;

    ModelAttributeName(String attributeName) {
        this.attributeName = attributeName;
    }

    @Override
    public String toString() {
        return this.attributeName;
    }
}