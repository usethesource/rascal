/*
 * Copyright (c) 2025, Swat.engineering All rights reserved. Redistribution and use in source and
 * binary forms, with or without modification, are permitted provided that the following conditions
 * are met: 1. Redistributions of source code must retain the above copyright notice, this list of
 * conditions and the following disclaimer. 2. Redistributions in binary form must reproduce the
 * above copyright notice, this list of conditions and the following disclaimer in the documentation
 * and/or other materials provided with the distribution. THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT
 * HOLDERS AND CONTRIBUTORS "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT
 * LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT,
 * INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED
 * TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT
 * LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package org.rascalmpl.util.maven;

import java.io.File;
import java.io.IOException;
import java.net.http.HttpClient;
import java.net.http.HttpClient.Version;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Duration;
import java.util.Comparator;
import java.util.stream.Stream;

import org.apache.maven.model.Repository;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

public class SimpleRepositoryDownloaderTest {
    private static Path tempRepo;
    private static SimpleRepositoryDownloader downloader;

    @BeforeClass
    public static void setupRepo() throws IOException {
        tempRepo = Files.createTempDirectory("m2-test-repo");
        Repository remoteRepo = new Repository();
        remoteRepo.setUrl("https://repo.maven.apache.org/maven2/");
        var httpClient = HttpClient.newBuilder()
            .version(Version.HTTP_2) // upgrade where possible
            .connectTimeout(Duration.ofSeconds(10)) // don't wait longer than 10s to connect to a repo
            .build();
        downloader = new SimpleRepositoryDownloader(new Repo(remoteRepo), httpClient);
    }

    @AfterClass
    public static void cleanupRepo() throws IOException {
         try (Stream<Path> pathStream = Files.walk(tempRepo)) {
            pathStream.sorted(Comparator.reverseOrder())
            .map(Path::toFile)
            .forEach(File::delete);
        }
    }

    @Test
    public void testPomDownload() throws IOException {
        String url = "com/google/api/api-common/2.9.0/api-common-2.9.0.pom";
        Path target = tempRepo.resolve("test.pom"); // No need to go any deeper for this test
        String pomContent = downloader.downloadAndRead(url, target, true);
        Assert.assertTrue(pomContent.startsWith("<?xml version='1.0' encoding='UTF-8'?>"));    
        Assert.assertEquals(pomContent, Files.readString(target));
    }

    @Test
    public void testDownloadNoForce() throws IOException {
        String url = "com/google/api/api-common/2.9.0/api-common-2.9.0.pom";
        Path target = tempRepo.resolve("test.pom"); // No need to go any deeper for this test
        String hello = "Hello World!";
        Files.writeString(target, hello);
        downloader.downloadAndRead(url, target, false);
        // File should not have been overwritten because force is false
        Assert.assertEquals(hello, Files.readString(target));
    }

    @Test
    public void testDownloadBlob() throws IOException {
        String url = "com/google/api/api-common/2.9.0/api-common-2.9.0-tests.jar";
        Path target = tempRepo.resolve("tests.jar"); // No need to go any deeper for this test
        Assert.assertTrue(downloader.download(url, target, true));
        Assert.assertTrue(Files.exists(target));
    }

}
