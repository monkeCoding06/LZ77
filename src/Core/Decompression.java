package Core;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import Utils.ProgressBar;

public class Decompression {
    static void decompress(byte[] data, String outputPath) {
        System.out.println("Decompresssing...");
        String compressedData = new String(data).replace("\\(", "(").replace("\\)", ")");
        StringBuilder decompressedOutput = new StringBuilder();

        int index = 0;
        int totalLength = compressedData.length();
        int lastPercentage = -1; // For tracking progress updates

        while (index < totalLength) {
            int openBracket = compressedData.indexOf('(', index);
            int closeBracket = compressedData.indexOf(')', openBracket);

            if (openBracket == -1 || closeBracket == -1) {
                break; // Exit if no more valid tokens
            }

            String token = compressedData.substring(openBracket + 1, closeBracket);
            String[] parts = token.split(", ");

            if (parts.length < 3) {
                System.err.println("Invalid token: " + token);
                break; // Handle invalid token
            }

            try {
                int matchDistance = Integer.parseInt(parts[0]);
                int matchLength = Integer.parseInt(parts[1]);
                char nextChar = parts[2].charAt(1);

                int start = decompressedOutput.length() - matchDistance;

                for (int i = 0; i < matchLength; i++) {
                    if (start + i >= 0) {
                        decompressedOutput.append(decompressedOutput.charAt(start + i));
                    }
                }

                if (nextChar != '\0') {
                    decompressedOutput.append(nextChar);
                }

                index = closeBracket + 1;

                int currentPercentage = (int) ((double) index / totalLength * 100);
                currentPercentage = Math.min(currentPercentage, 100); // Clamp to 100%
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
