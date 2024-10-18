package org.rugal.util;

import lombok.Getter;
import lombok.Setter;

public enum ConfigurationValues {
    CONTAINER;

    @Getter
    @Setter
    private String startDirectoryPath;
    @Getter
    @Setter
    private String srcTestResourcesFixtures;
    @Getter
    @Setter
    private String filterFileExtension;
}
