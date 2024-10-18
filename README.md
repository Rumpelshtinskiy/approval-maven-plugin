# approval-maven-plugin
The plugin for Apache Maven project builder, that helps tests and approve generated files.

## Requirements
* Java 11

## When do you need this plugin
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