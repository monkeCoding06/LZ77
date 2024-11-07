package Core.LZ77;

import Core.COBS.COBS;
import Utils.File;
import Utils.Messages;

public class LZ77 extends File {

    private boolean isCompress = false;
    private boolean isStuff = false;
    private boolean isDecompress = false;
    private boolean isUnstuff = false;

    public void run(String[] args) {
        if (args.length == 0) {
            System.out.println(Messages.helpMessageLZ77);
            return;
        }

        if (args.length < 2) {
            System.out.println(Messages.specifyPath);
            return;
        }

        System.out.print("\r" + "Reading Data...");
        byte[] fileData = File.readFile(args[args.length - 1]);
        if (fileData == null || fileData.length == 0) {
            System.err.println("Error: Could not read file or file is empty.");
            return;
        }
        String outputFileName = args[args.length - 1];


        for (String arg : args) {
            switch (arg) {
                case "-c", "--compress" -> isCompress = true;
                case "-s", "--stuff" -> isStuff = true;
                case "-d", "--decompress" -> isDecompress = true;
                case "-u", "--unstuff" -> isUnstuff = true;
            }
        }


        if (isCompress && isStuff) {
            Compression.compress(fileData, args[args.length - 1].replace(".txt", ""));
            COBS.stuff(outputFileName);
        } else if (isDecompress) {
            Decompression.decompress(fileData, args[args.length - 1].replace(".lz", "") + "unpacked");
        } else if (isUnstuff) {
            COBS.unstuff(outputFileName);
        } else if (isCompress) {
            Compression.compress(fileData, args[args.length - 1].replace(".txt", ""));
        } else {
            System.out.println(Messages.invalidOption);
        }
    }
}
