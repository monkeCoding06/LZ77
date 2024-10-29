package Core;

import Utils.ProgressBar;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class Compression {
    static void compress(byte[] data, String outputPath) {
        System.out.println("\r" + "Compressing...");
        int inputLength = data.length;
        List<Byte> output = new ArrayList<>();
        final int windowSize = 4080;
        final int lookaheadBufferSize = 255;
        int lastPercentage = -1;

        for (int i = 0; i < inputLength; ) {
            int matchLength = 0;
            int matchDistance = 0;

            final int start = Math.max(0, i - windowSize);
            final int end = Math.min(inputLength, i + lookaheadBufferSize);

            for (int j = start; j < i; j++) {
                int length = 0;
                while (i + length < end && data[j + length] == data[i + length]) {
                    length++;
                }
                if (length > matchLength) {
                    matchLength = length;
                    matchDistance = i - j;
                }
            }

            final int nextByteIndex = i + matchLength;
            byte nextByte = nextByteIndex < inputLength ? data[nextByteIndex] : 0;

            output.add((byte) (matchDistance >> 8));
            output.add((byte) matchDistance);
            output.add((byte) (matchLength >> 8));
            output.add((byte) matchLength);
            output.add(nextByte);

            i += matchLength + 1;

            int currentPercentage = (int) ((double) i / inputLength * 100);
            currentPercentage = Math.min(currentPercentage, 100);
            if (currentPercentage != lastPercentage) {
                ProgressBar.printProgressBar(currentPercentage);
                lastPercentage = currentPercentage;
            }
        }

        final byte[] compressedOutput = new byte[output.size()];
        for (int k = 0; k < output.size(); k++) {
            compressedOutput[k] = output.get(k);
        }

        String outputFilePath = outputPath.endsWith(".lz") ? outputPath : outputPath + ".lz";
        try {
            Files.write(Paths.get(outputFilePath), compressedOutput);
            System.out.println("\nCompressed output written to: " + outputFilePath);
        } catch (IOException e) {
            System.err.println("Error writing to file: " + e.getMessage());
        }
    }
}