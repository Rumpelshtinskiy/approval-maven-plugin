package org.rugal;

/*
 * Copyright 2001-2005 The Apache Software Foundation.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.project.MavenProject;
import org.junit.platform.launcher.Launcher;
import org.junit.platform.launcher.LauncherDiscoveryRequest;
import org.junit.platform.launcher.core.LauncherDiscoveryRequestBuilder;
import org.junit.platform.launcher.core.LauncherFactory;
import org.junit.platform.launcher.listeners.SummaryGeneratingListener;
import org.junit.platform.launcher.listeners.TestExecutionSummary;
import org.rugal.test.ApprovalTest;
import org.rugal.util.ConfigurationValues;
import org.rugal.util.ErrorMap;

import java.util.List;

import static org.junit.platform.engine.discovery.DiscoverySelectors.selectClass;

@Mojo( name = "approval-files")
public class ProtoApprovalMojo extends AbstractMojo
{
    @Parameter(defaultValue = "${project}", required = true, readonly = true)
    private MavenProject project;
    @Parameter(property = "start_directory_path")
    private String startDirectoryPath = ""; //Default project path
    @Parameter(property = "src_test_resources_fixtures", defaultValue = "src/test/resources/fixtures") //Default path for saving fixtures
    private String srcTestResourcesFixtures;
    @Parameter(property = "filter_file_extension", defaultValue = ".proto") //Default filter file extension
    private String filterFileExtension;
    private final SummaryGeneratingListener listener = new SummaryGeneratingListener();

    @Override
    public void execute() throws MojoExecutionException {
        getLog().info("Approval test running.");
        ConfigurationValues.CONTAINER.setStartDirectoryPath(startDirectoryPath);
        ConfigurationValues.CONTAINER.setSrcTestResourcesFixtures(project.getBasedir().getAbsolutePath() + srcTestResourcesFixtures);
        ConfigurationValues.CONTAINER.setFilterFileExtension(filterFileExtension);

        LauncherDiscoveryRequest request = LauncherDiscoveryRequestBuilder.request()
                .selectors(selectClass(ApprovalTest.class))
                .build();
        Launcher launcher = LauncherFactory.create();
        launcher.discover(request);
        launcher.registerTestExecutionListeners(listener);
        launcher.execute(request);

        List<TestExecutionSummary.Failure> failures = listener.getSummary().getFailures();
        failures.stream()
                .map(TestExecutionSummary.Failure::getException)
                .map(Throwable::getMessage)
                .map(ErrorMap::substituteError)
                .forEach(message -> getLog().error(message));

        if(!failures.isEmpty()) {
            throw new MojoExecutionException("Approval test failed!");
        }
        getLog().info("Approval test finished without errors.");
    }
}
