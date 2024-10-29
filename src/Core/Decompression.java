package Core;

import Utils.ProgressBar;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class Decompression {
    static void decompress(byte[] data, String outputPath) {
        System.out.println("\r"  + "Deccompressing...");
        int totalLength = data.length;
        List<Byte> decompressedOutput = new ArrayList<>();

        int index = 0;
        int lastPercentage = -1;

        while (index < totalLength) {
            int matchDistance = ((data[index] & 0xFF) << 8) | (data[index + 1] & 0xFF);
            int matchLength = ((data[index + 2] & 0xFF) << 8) | (data[index + 3] & 0xFF);
            byte nextByte = data[index + 4];

            if (matchDistance > 0) {
                int start = decompressedOutput.size() - matchDistance;
                for (int i = 0; i < matchLength; i++) {
                    decompressedOutput.add(decompressedOutput.get(start + i));
                }
            }

            decompressedOutput.add(nextByte);

            index += 5;

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