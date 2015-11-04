/*
 * Copyright 2015 Cyrille Sondag.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.kyrillos.flattzdb;

import java.io.File;
import java.io.IOException;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

/**
 * Project : flattzdb-parent.
 * Created by Cyrille Sondag on 04/11/2015.
 */
public class CompilerTest {

    @Rule
    public TemporaryFolder folder= new TemporaryFolder();

    @Test
    public void compileTest() throws IOException {
        String samples = CompilerTest.class.getResource("/integration/").getPath();
        File destFolder = folder.newFolder();
        String[] args = {"-srcdir", samples, "-dstdir", destFolder.getPath()};
        Compiler.main(args);

        File[] files = destFolder.listFiles();
        Assert.assertNotNull(files);
        Assert.assertTrue(files.length == 1);
        System.out.println("size = " + files[0].length() / 1024 + " kb");
    }

}
