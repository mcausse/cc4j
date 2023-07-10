package org.homs.cc4j;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

public class TestUtils {

    public static String loadFromClasspath(String resourceName) throws URISyntaxException, IOException {
        final URL resource = Thread.currentThread().getContextClassLoader().getResource(resourceName);
        final URI uri = resource.toURI();
        final Path path = Paths.get(uri);
        final List<String> elements = Files.readAllLines(path);
        return String.join("\n", elements);
    }

    public static String getFile(String classPathResourceName) {
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        URL resource = classLoader.getResource(classPathResourceName);

        if (resource == null) {
            throw new IllegalArgumentException("file not found: " + classPathResourceName);
        }

        return new File(resource.getFile()).toString();
    }
}
