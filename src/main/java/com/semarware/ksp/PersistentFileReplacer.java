package com.semarware.ksp;


import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;


public class PersistentFileReplacer {
    private final Scanner scanner;
    private final BufferedWriter bufferedWriter;
    private final List<Replacement> replacements;


    public PersistentFileReplacer(String inputFilePath, List<Replacement> replacements) {

        this.replacements = replacements;
        try {
            this.scanner = new Scanner(new File(inputFilePath), StandardCharsets.UTF_8);
            this.bufferedWriter = Files.newBufferedWriter(Path.of(inputFilePath + ".fixed"), StandardCharsets.UTF_8);
        } catch (IOException e) {
            throw new IllegalArgumentException("Failed to open file", e);
        }
    }

    public void fix() {
        while (scanner.hasNext()) {
            final String line = nextLine();
            final List<Replacement> relevantReplacemnts = replacements.stream().filter(r -> line.contains(r.getInvalidValue())).collect(Collectors.toList());
            String alteredLine = line;
            if (relevantReplacemnts.size() > 0) {
                System.out.printf("Found line to fix up: %s%n", line);

                for (Replacement r : relevantReplacemnts) {
                    alteredLine = alteredLine.replaceAll(r.getInvalidValue(), r.getCorrectValue());
                }
            }
            try {
                bufferedWriter.write(alteredLine);
                bufferedWriter.newLine();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        try {
            bufferedWriter.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    private String nextLine() {
        return scanner.nextLine();
    }

}