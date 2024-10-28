package Core;

import Utils.ProgressBar;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class Compression {
    static void compress(byte[] data, String outputPath) {
        System.out.println("Compressing...");
        String input = new String(data);
        int inputLength = input.length();
        List<String> output = new ArrayList<>();

        int windowSize = data.length;
        int lookaheadBufferSize = data.length;

        int lastPercentage = -1;

        for (int i = 0; i < inputLength; ) {
            int matchLength = 0;
            int matchDistance = 0;

            int start = Math.max(0, i - windowSize);
            int end = Math.min(inputLength, i + lookaheadBufferSize);

            for (int j = start; j < i; j++) {
                int length = 0;
                while (i + length < end && input.charAt(j + length) == input.charAt(i + length)) {
                    length++;
                }
                if (length > matchLength) {
                    matchLength = length;
                    matchDistance = i - j;
                }
            }

            if (matchLength > 0) {
                int nextCharIndex = i + matchLength;
                char nextChar = nextCharIndex < inputLength ? input.charAt(nextCharIndex) : '\0';
                output.add("(" + matchDistance + ", " + matchLength + ", '" + nextChar + "')");
                i += matchLength + 1;
            } else {
                output.add("(0, 0, '" + input.charAt(i) + "')");
                i++;
            }

            int currentPercentage = (int) ((double) i / inputLength * 100);
            currentPercentage = Math.min(currentPercentage, 100); // Clamp to 100%
            if (currentPercentage != lastPercentage) {
                ProgressBar.printProgressBar(currentPercentage);
                lastPercentage = currentPercentage;
            }
        }

        String compressedOutput = String.join("", output);

        String outputFilePath = outputPath.endsWith(".lz") ? outputPath : outputPath + ".lz";
        try {
            Files.write(Paths.get(outputFilePath), compressedOutput.getBytes());
            System.out.println("\nCompressed output written to: " + outputFilePath);
        } catch (IOException e) {
            System.err.println("Error writing to file: " + e.getMessage());
        }
    }
}
