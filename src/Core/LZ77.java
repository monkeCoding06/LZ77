package Core;

import Utils.File;
import Utils.Messages;

import java.util.ArrayList;
import java.util.List;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class LZ77 extends File {
    Messages messages = new Messages();

    public void run(String[] args) {
        if (args.length == 0) {
            System.out.println(messages.helpMessage);
            return;
        }

        if (args.length == 1) {
            System.out.println(messages.specifyPath);
            return;
        }

        byte[] fileData = File.readFile(args[1]);
        if (fileData == null) {
            return;
        }

        File.printFileContent(fileData);

        switch (args[0]) {
            case "-c":
            case "--compress":
                compress(fileData, "Files/output");
                break;

            case "-d":
            case "--decompress":
                decompress(fileData, "Files/decompressed.txt");
                break;

            default:
                System.out.println(messages.invalidOption);
                break;
        }
    }



    private void compress(byte[] data, String outputPath) {
        String input = new String(data);
        int inputLength = input.length();
        List<String> output = new ArrayList<>();
        System.out.println(data.length + "bytes");
        int windowSize = data.length;
        int lookaheadBufferSize = data.length ;

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
        }

        String compressedOutput = String.join("", output);

        String outputFilePath = outputPath.endsWith(".lz") ? outputPath : outputPath + ".lz";
        try {
            Files.write(Paths.get(outputFilePath), compressedOutput.getBytes());
            System.out.println("Compressed output written to: " + outputFilePath);
        } catch (IOException e) {
            System.err.println("Error writing to file: " + e.getMessage());
        }
    }


    private void decompress(byte[] data, String outputPath) {
        String compressedData = new String(data);
        StringBuilder decompressedOutput = new StringBuilder();


        int index = 0;
        while (index < compressedData.length()) {
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
