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
package com.vaadin.flow.server.frontend;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.IOUtils;

import static java.nio.charset.StandardCharsets.UTF_8;

/**
 * Generate <code>tsconfig.json</code> if it is missing in project folder.
 * <p>
 * For internal use only. May be renamed or removed in a future release.
 *
 * @since 3.0
 */
public class TaskGenerateTsConfig extends AbstractTaskClientGenerator {

    static final String TSCONFIG_JSON = "tsconfig.json";
    private final File npmFolder;

    /**
     * Create a task to generate <code>tsconfig.json</code> file.
     *
     * @param npmFolder
     *            project folder where the file will be generated.
     */
    TaskGenerateTsConfig(File npmFolder) {
        this.npmFolder = npmFolder;
    }

    @Override
    protected String getFileContent() throws IOException {
        return IOUtils.toString(getClass().getResourceAsStream(TSCONFIG_JSON),
                UTF_8);
    }

    @Override
    protected File getGeneratedFile() {
        return new File(npmFolder, TSCONFIG_JSON);
    }

    @Override
    protected boolean shouldGenerate() {
        return !new File(npmFolder, TSCONFIG_JSON).exists();
    }
}
