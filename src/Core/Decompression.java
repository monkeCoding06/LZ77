package Core;

import Utils.ProgressBar;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

public class Decompression {
    static void decompress(byte[] data, String outputPath) {
        System.out.println("Decompressing...");
        String compressedData = new String(data, StandardCharsets.UTF_8);
        StringBuilder decompressedOutput = new StringBuilder();

        int index = 0;
        int totalLength = compressedData.length();
        int lastPercentage = -1;

        while (index < totalLength) {
            char firstChar = compressedData.charAt(index);
            char secondChar = compressedData.charAt(index + 1);
            char nextChar = compressedData.charAt(index + 2);

            int matchDistance = (int) firstChar;
            int matchLength = (int) secondChar;

            int start = decompressedOutput.length() - matchDistance;
            for (int i = 0; i < matchLength; i++) {
                if (start + i >= 0) {
                    decompressedOutput.append(decompressedOutput.charAt(start + i));
                }
            }

            if (nextChar != '\0') {
                decompressedOutput.append(nextChar);
            }

            index += 3;

            int currentPercentage = (int) ((double) index / totalLength * 100);
            currentPercentage = Math.min(currentPercentage, 100);
            if (currentPercentage != lastPercentage) {
                ProgressBar.printProgressBar(currentPercentage);
                lastPercentage = currentPercentage;
            }
        }

        if (lastPercentage == 100) {
            System.out.println();
        }

        String outputFilePath = outputPath.endsWith(".txt") ? outputPath : outputPath + ".txt";
        try {
            Files.write(Paths.get(outputFilePath), decompressedOutput.toString().getBytes());
            System.out.println("Decompressed output written to: " + outputFilePath);
        } catch (IOException e) {
            System.err.println("Error writing decompressed file: " + e.getMessage());
        }
    }
}
