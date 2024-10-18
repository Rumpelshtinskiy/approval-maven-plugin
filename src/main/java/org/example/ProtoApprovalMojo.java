package org.example;

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
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.project.MavenProject;
import org.junit.platform.launcher.Launcher;
import org.junit.platform.launcher.LauncherDiscoveryRequest;
import org.junit.platform.launcher.TestPlan;
import org.junit.platform.launcher.core.LauncherDiscoveryRequestBuilder;
import org.junit.platform.launcher.core.LauncherFactory;
import org.junit.platform.launcher.listeners.SummaryGeneratingListener;
import org.junit.platform.launcher.listeners.TestExecutionSummary;

import java.io.File;
import java.util.List;

import static org.junit.platform.engine.discovery.DiscoverySelectors.selectClass;

@Mojo( name = "approval-files")
public class ProtoApprovalMojo extends AbstractMojo
{
    @Parameter( defaultValue = "${project.build.directory}", property = "outputDir", required = true )
    private File outputDirectory;
    @Parameter(defaultValue = "${project}", required = true, readonly = true)
    private MavenProject project;
    private final SummaryGeneratingListener listener = new SummaryGeneratingListener();

    @Override
    public void execute() throws MojoExecutionException, MojoFailureException {
        getLog().info("Approval test running.");
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
                .forEach(getLog()::error);
        if(!failures.isEmpty()) {
            throw new MojoExecutionException("Approval test failed!");
        }
        getLog().info("Approval test finished.");
    }
}
