package com.ecampus.Ecampus.entities;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum ItemCondition {
    NEW,
    USED,
    LIKE_NEW;

    @JsonCreator
    public static ItemCondition fromString(String value) {
        if (value == null) return null;

        // Normalize the value to match enum constants
        String normalized = value.replace(" ", "_").toUpperCase(); // Convert "like new" to "LIKE_NEW"
        try {
            return ItemCondition.valueOf(normalized);
        } catch (IllegalArgumentException e) {
            System.err.println("Invalid condition string: " + value);
            return null; // Handle invalid values gracefully
        }
    }

    @JsonValue
    public String toString() {
        // Customize how the enum is serialized
        if (this == LIKE_NEW) {
            return "like new";
        }
        return name().toLowerCase(); // Default to lowercase for other constants
    }
}
