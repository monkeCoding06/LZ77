package Core;

import Utils.ProgressBar;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class Decompression {

    static void decompress(byte[] data, String outputPath) {
        String compressedData = new String(data);
        StringBuilder decompressedOutput = new StringBuilder();

        int index = 0;
        int totalLength = compressedData.length();
        int lastPercentage = -1;

        while (index < totalLength) {
            int openBracket = compressedData.indexOf('(', index);
            int closeBracket = compressedData.indexOf(')', openBracket);

            if (openBracket == -1 || closeBracket == -1) {
                break;
            }

            String token = compressedData.substring(openBracket + 1, closeBracket);
            String[] parts = token.split(", ");

            int matchDistance = Integer.parseInt(parts[0]);
            int matchLength = Integer.parseInt(parts[1]);
            char nextChar = parts[2].charAt(1);

            int start = decompressedOutput.length() - matchDistance;


            for (int i = 0; i < matchLength; i++) {
                decompressedOutput.append(decompressedOutput.charAt(start + i));
            }

            decompressedOutput.append(nextChar);
            index = closeBracket + 1;


            int currentPercentage = (int) ((double) index / totalLength * 100);
            if (currentPercentage != lastPercentage) {
                ProgressBar.printProgressBar(currentPercentage);
                lastPercentage = currentPercentage;
            }
        }

        String outputFilePath = outputPath.endsWith(".txt") ? outputPath : outputPath + ".txt";
        try {
            Files.write(Paths.get(outputFilePath), decompressedOutput.toString().getBytes());
            System.out.println("\nDecompressed output written to: " + outputFilePath);
        } catch (IOException e) {
            System.err.println("Error writing decompressed file: " + e.getMessage());
        }
    }

}
