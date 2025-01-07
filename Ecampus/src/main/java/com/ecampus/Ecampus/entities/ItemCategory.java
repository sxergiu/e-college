package com.ecampus.Ecampus.entities;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum ItemCategory {
    BOOK, ELECTRONICS, CLOTHING, FURNITURE;

    @JsonCreator
    public static ItemCategory fromString(String value) {
        return value == null ? null : ItemCategory.valueOf(value.toUpperCase());
    }

    @JsonValue
    public String toString() {
        return name().toLowerCase();
    }
}
