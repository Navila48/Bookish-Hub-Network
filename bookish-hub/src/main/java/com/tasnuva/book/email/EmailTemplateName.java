package com.tasnuva.book.email;

import lombok.Getter;

@Getter
public enum EmailTemplateName {
    ACTIVATE_ACCOUNT("activate-account");
    private final String name;
    private EmailTemplateName(String name) {
        this.name = name;
    }
}
