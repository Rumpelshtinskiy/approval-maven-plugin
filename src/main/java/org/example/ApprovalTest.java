package org.example;

import com.spun.util.tests.TestUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.approvaltests.Approvals;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.FileVisitOption;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Objects;
import java.util.stream.Stream;

public class ApprovalTest {
    public static final String SRC_TEST_RESOURCES_FIXTURES = "src/test/resources/fixtures";
    public static final String PROJECT_PATH = "";

    @ParameterizedTest
    @MethodSource("getFiles")
    void testProtoFiles(String protoFileName, String value) {
        //Init
        Approvals.settings().allowMultipleVerifyCallsForThisMethod();
        File fixturesPath = new File(SRC_TEST_RESOURCES_FIXTURES);
        try(TestUtils.SourceDirectoryRestorer sdr = TestUtils.registerSourceDirectoryFinder((clazz, fileName) -> fixturesPath)) {
            //Act
            //Assert
            Approvals.verify(value, Approvals.NAMES.withParameters(protoFileName));
        }
    }

    static Stream<Arguments> getFiles() {
        File prjPath = new File(PROJECT_PATH);
        return Stream.of(prjPath)
                .flatMap(ApprovalTest::walkToDirectory)
                .filter(Objects::nonNull)
                .map(path -> Pair.of(path.toFile().getName(), path.toFile()))
                .filter(pair -> pair.getRight().isFile())
                .filter(pair -> protoFileFilter(pair.getRight()))
                .map(pair -> Pair.of(pair.getLeft(), fileInputStream(pair.getRight())))
                .map(pair -> Pair.of(pair.getLeft(), read(pair.getRight())))
                .map(pair -> Arguments.of(pair.getLeft(), pair.getRight()));
    }

    static Stream<Path> walkToDirectory(File file) {
        try {
            return Files.walk(file.getAbsoluteFile().toPath(), FileVisitOption.FOLLOW_LINKS);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    static boolean protoFileFilter(File file) {
        return file.getName().endsWith(".proto");
    }

    static InputStream fileInputStream(File file) {
        try {
            return new FileInputStream(file);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    static String read(InputStream inputStream) {
        StringBuilder resultStringBuilder = new StringBuilder();
        try (BufferedReader br = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8))) {
            String line;
            while ((line = br.readLine()) != null) {
                resultStringBuilder.append(line).append("\n");
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return resultStringBuilder.toString();
    }
}
