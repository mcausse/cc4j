package org.homs.cc4j.util;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class FileUtils {

    public static String loadFile(String filePath) throws IOException {
        Path path = Paths.get(filePath);
        Stream<String> lines = Files.lines(path);
        String data = lines.collect(Collectors.joining("\n"));
        lines.close();
        return data;
    }

    public static List<File> processDirectory(File folder, Predicate<String> fileNamePredicate) {
        List<File> r = new ArrayList<>();
        for (final File fileEntry : Objects.requireNonNull(folder.listFiles())) {
            if (fileEntry.isDirectory()) {
                r.addAll(processDirectory(fileEntry, fileNamePredicate));
            } else {
                if (fileNamePredicate.test(fileEntry.getAbsoluteFile().toString())) {
                    System.out.println(fileEntry.getName());
                    r.add(fileEntry);
                }
            }
        }
        return r;
    }
}
