package org.rugal.util;

import lombok.experimental.UtilityClass;

import java.util.Map;
import java.util.Objects;

@UtilityClass
public class ErrorMap {
    private final String START_DIRECTORY_PATH = "start_directory_path";

    private final Map<String, String> MAP = Map.of("Configuration error: You must configure at least one set of arguments for this @ParameterizedTest", "Didn't found any target files in start_directory_path=%s");
    public String substituteError(String error) {
        String value = MAP.get(error);
        if(Objects.nonNull(value)) {
            return value.contains(START_DIRECTORY_PATH) ? String.format(value, ConfigurationValues.CONTAINER.getStartDirectoryPath()) : value;
        }
        return error;
    }
}
