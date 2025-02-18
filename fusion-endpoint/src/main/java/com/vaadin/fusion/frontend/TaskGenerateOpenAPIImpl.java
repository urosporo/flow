/*
 * Copyright 2000-2021 Vaadin Ltd.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package com.vaadin.fusion.frontend;

import java.io.File;
import java.util.Collections;
import java.util.Objects;

import com.vaadin.flow.server.ExecutionFailedException;
import com.vaadin.flow.server.frontend.TaskGenerateOpenAPI;
import com.vaadin.fusion.Endpoint;
import com.vaadin.fusion.generator.OpenAPISpecGenerator;

/**
 * Generate OpenAPI json file for Vaadin Endpoints.
 */
public class TaskGenerateOpenAPIImpl extends AbstractTaskFusionGenerator
        implements TaskGenerateOpenAPI {

    private final File javaSourceFolder;
    private final ClassLoader classLoader;
    private final File output;

    /**
     * Create a task for generating OpenAPI spec.
     *
     * @param javaSourceFolder
     *            source paths of the project containing {@link Endpoint}
     * @param classLoader
     *            The class loader which should be used to resolved types in the
     *            source paths.
     * @param output
     *            the output path of the generated json file.
     */
    TaskGenerateOpenAPIImpl(File properties, File javaSourceFolder,
            ClassLoader classLoader, File output) {
        super(properties);
        Objects.requireNonNull(javaSourceFolder,
                "Source paths should not be null.");
        Objects.requireNonNull(output,
                "OpenAPI output file should not be null.");
        Objects.requireNonNull(classLoader, "ClassLoader should not be null.");
        this.javaSourceFolder = javaSourceFolder;
        this.classLoader = classLoader;
        this.output = output;
    }

    @Override
    public void execute() throws ExecutionFailedException {
        OpenAPISpecGenerator openApiSpecGenerator = new OpenAPISpecGenerator(
                readApplicationProperties());
        openApiSpecGenerator.generateOpenApiSpec(
                Collections.singletonList(javaSourceFolder.toPath()),
                classLoader, output.toPath());
    }
}
