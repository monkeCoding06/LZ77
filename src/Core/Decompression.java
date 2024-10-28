package Core;

import Utils.ProgressBar;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class Decompression {
    static void decompress(byte[] data, String outputPath) {
        System.out.println("Decompressing...");
        int totalLength = data.length;
        List<Byte> decompressedOutput = new ArrayList<>();

        int index = 0;
        int lastPercentage = -1;

        while (index < totalLength) {
            int matchDistance = Byte.toUnsignedInt(data[index]);
            int matchLength = Byte.toUnsignedInt(data[index + 1]);
            byte nextByte = data[index + 2];

            if (matchDistance <= decompressedOutput.size()) {
                int start = decompressedOutput.size() - matchDistance;
                for (int i = 0; i < matchLength; i++) {
                    if (start + i >= 0 && start + i < decompressedOutput.size()) {
                        decompressedOutput.add(decompressedOutput.get(start + i));
                    }
                }
            } else {
                System.err.println("Warning: matchDistance exceeds decompressed data length. Skipping match.");
            }

            if (nextByte != 0) {
                decompressedOutput.add(nextByte);
            }

            index += 3;

            int currentPercentage = (int) ((double) index / totalLength * 100);
            currentPercentage = Math.min(currentPercentage, 100);
            if (currentPercentage != lastPercentage) {
                ProgressBar.printProgressBar(currentPercentage);
                lastPercentage = currentPercentage;
            }
            if (currentPercentage == 100) {
                System.out.println();
            }
        }

        byte[] outputBytes = new byte[decompressedOutput.size()];
        for (int i = 0; i < decompressedOutput.size(); i++) {
            outputBytes[i] = decompressedOutput.get(i);
        }

        String outputFilePath = outputPath.endsWith(".txt") ? outputPath : outputPath + ".txt";
        try {
            Files.write(Paths.get(outputFilePath), outputBytes);
            System.out.println("Decompressed output written to: " + outputFilePath);
        } catch (IOException e) {
            System.err.println("Error writing decompressed file: " + e.getMessage());
        }
    }
}
