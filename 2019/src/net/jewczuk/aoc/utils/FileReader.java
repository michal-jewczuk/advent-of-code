package net.jewczuk.aoc.utils;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public enum FileReader {
    INSTANCE;

    public List<String> loadStringData(String filename) {
        List<String> output = new ArrayList<>();
        Path path = Paths.get("./data/" + filename);
        try (InputStream in = Files.newInputStream(path);
             BufferedReader reader = new BufferedReader(new InputStreamReader(in))
        ) {
            String line = null;
            while ((line = reader.readLine()) != null) {
                output.add(line.trim());
            }
        } catch (IOException ex) {
            System.out.println("Error reading file: " + filename);
        }

        return output;
    }

    public List<Integer> loadNumericData(String filename) {
        List<String> stringData = loadStringData(filename);
        return stringData.stream()
                .map(s -> Integer.parseInt(s))
                .collect(Collectors.toList());
    }
}


//    // Convert the string to a
//    // byte array.
//    String s = "Hello World! ";
//    byte data[] = s.getBytes();
//    Path p = Paths.get("./data/logfile.txt");
//
//        try (OutputStream out = new BufferedOutputStream(
//                Files.newOutputStream(p, CREATE, APPEND))) {
//                out.write(data, 0, data.length);
//                } catch (IOException x) {
//                System.err.println(x);
//                }
