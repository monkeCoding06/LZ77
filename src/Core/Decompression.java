package Core;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import Utils.ProgressBar;

public class Decompression {
    static void decompress(byte[] data, String outputPath) {
        System.out.println("Decompressing...");
        String compressedData = new String(data, StandardCharsets.UTF_8);
        StringBuilder decompressedOutput = new StringBuilder();

        int index = 0;
        int totalLength = compressedData.length();
        int lastPercentage = -1;

        while (index < totalLength) {
            int nextComma = compressedData.indexOf(',', index);
            if (nextComma == -1) break;

            int secondComma = compressedData.indexOf(',', nextComma + 1);
            if (secondComma == -1) break;

            try {
                int matchDistance = Integer.parseInt(compressedData.substring(index, nextComma));

                int matchLength = Integer.parseInt(compressedData.substring(nextComma + 1, secondComma));

                char nextChar = compressedData.charAt(secondComma + 1);

                int start = decompressedOutput.length() - matchDistance;
                for (int i = 0; i < matchLength; i++) {
                    if (start + i >= 0) {
                        decompressedOutput.append(decompressedOutput.charAt(start + i));
                    }
                }

                if (nextChar != '\0') {
                    decompressedOutput.append(nextChar);
                }

                index = secondComma + 2;

                int currentPercentage = (int) ((double) index / totalLength * 100);
                currentPercentage = Math.min(currentPercentage, 100);
                if (currentPercentage != lastPercentage) {
                    ProgressBar.printProgressBar(currentPercentage);
                    lastPercentage = currentPercentage;
                }

            } catch (NumberFormatException e) {
                System.err.println("Error parsing token parts: " + e.getMessage());
                break;
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
