# approval-maven-plugin
The plugin for Apache Maven project builder, that helps tests and approve generated files.

## Requirements
* Java 11

## When Do You Need This Plugin
If you have generated files, that are sensitive to changes. For example a .proto files that sensitives to changes ordering of its fields.

## Getting Started

Add to your pom.xml file an **_approval-maven-plugin_** plugin
```xml
<project>
    ...
    <build>
        <plugins>
            <plugin>
                <groupId>org.rugal</groupId>
                <artifactId>approval-maven-plugin</artifactId>
                <version>0.0.1</version>
                <executions>
                    <execution>
                        <phase>test</phase>
                        <goals>
                            <goal>approval-files</goal>
                        </goals>
                    </execution>
                </executions>
            </plugin>
        ...
        </plugins>
        ...
    </build>
    ...
</project>
```

## Plugin Configuration
startDirectoryPath - the path to the folder where the search for files to check starts. By default, the start directory path is a root of a project, that use the plugin.
Example:
```xml
<project>
    ...
    <build>
        <plugins>
            <plugin>
                <groupId>org.rugal</groupId>
                <artifactId>approval-maven-plugin</artifactId>
                <version>0.0.1</version>
                <configuration>
                    <startDirectoryPath>project_path_name</startDirectoryPath>
                </configuration>
                <executions>
                    <execution>
                        <phase>test</phase>
                        <goals>
                            <goal>approval-files</goal>
                        </goals>
                    </execution>
                </executions>
            </plugin>
        ...
        </plugins>
        ...
    </build>
    ...
</project>
```

srcTestResourcesFixtures - the path to the folder for saving an approved files. By default, an approved files will save in "prj_path/src/test/resources/fixtures", where prj_path is a root of a project, that use the plugin.
Example:
```xml
<project>
    ...
    <build>
        <plugins>
            <plugin>
                <groupId>org.rugal</groupId>
                <artifactId>approval-maven-plugin</artifactId>
                <version>0.0.1</version>
                <configuration>
                    <srcTestResourcesFixtures>/src/test/approved</srcTestResourcesFixtures>
                </configuration>
                <executions>
                    <execution>
                        <phase>test</phase>
                        <goals>
                            <goal>approval-files</goal>
                        </goals>
                    </execution>
                </executions>
            </plugin>
        ...
        </plugins>
        ...
    </build>
    ...
</project>
```

filterFileExtension - the file extension by which files will be filtered for an approval testing. By default, an extension is ".proto".
Example:
```xml
<project>
    ...
    <build>
        <plugins>
            <plugin>
                <groupId>org.rugal</groupId>
                <artifactId>approval-maven-plugin</artifactId>
                <version>0.0.1</version>
                <configuration>
                    <srcTestResourcesFixtures>/src/test/approved</srcTestResourcesFixtures>
                </configuration>
                <executions>
                    <execution>
                        <phase>test</phase>
                        <goals>
                            <goal>approval-files</goal>
                        </goals>
                    </execution>
                </executions>
            </plugin>
        ...
        </plugins>
        ...
    </build>
    ...
</project>
```