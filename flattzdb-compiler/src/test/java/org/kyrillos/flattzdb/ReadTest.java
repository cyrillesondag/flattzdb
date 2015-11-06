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
import java.nio.ByteBuffer;
import java.nio.file.Files;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;

/**
 * Project : flattzdb-parent. Created by Cyrille Sondag on 05/11/2015.
 */
public class ReadTest {

    private static File testDir;
    private static File dbFile;

    @BeforeClass
    public static void setUp() throws IOException {
        String samples = TestCompilerRaw.class.getResource("/samples/").getPath();
        testDir = Files.createTempDirectory(".temp").toFile();

        String[] args = {"-srcdir", samples, "-dstdir", testDir.toString(), "-verbose"};
        long start = System.currentTimeMillis();
        Compiler.main(args);
        System.out.println("Compile take " + (System.currentTimeMillis() - start) + " ms");

        File[] files = testDir.listFiles();
        Assert.assertNotNull(files);
        Assert.assertTrue(files.length == 1);
        dbFile = files[0];
    }

    @AfterClass
    public static void tearDown() {
        if (!testDir.delete()){
            throw new IllegalStateException("failed to delete temp directory");
        }
    }

    public Tzdb getRootAsTzdb() throws IOException {
        byte[] bytes = Files.readAllBytes(dbFile.toPath());
        return Tzdb.getRootAsTzdb(ByteBuffer.wrap(bytes));
    }
}
