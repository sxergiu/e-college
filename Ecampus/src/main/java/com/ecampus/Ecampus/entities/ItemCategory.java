package com.ecampus.Ecampus.entities;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum ItemCategory {
    BOOK, ELECTRONIC, CLOTHING, FURNITURE;

    @JsonCreator
    public static ItemCategory fromString(String value) {
        // Converts incoming string to enum, handles case-insensitivity
        return value == null ? null : ItemCategory.valueOf(value.toUpperCase());
    }

    @JsonValue
    public String toString() {
        // Converts enum to string for serialization (returns in lowercase)
        return name().toLowerCase();
    }
}
