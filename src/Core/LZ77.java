package Core;

import Utils.File;
import Utils.Messages;


public class LZ77 extends File {

    public void run(String[] args) {
        if (args.length == 0) {
            System.out.println(Messages.helpMessage);
            return;
        }

        if (args.length == 1) {
            System.out.println(Messages.specifyPath);
            return;
        }

        byte[] fileData = File.readFile(args[1]);
        if (fileData == null) {
            return;
        }


        switch (args[0]) {
            case "-c":
            case "--compress":
                Compression.compress(fileData,  args[args.length - 1].replace(".txt", ""));
                break;

            case "-d":
            case "--decompress":
                Decompression.decompress(fileData, args[args.length - 1].replace(".lz", "") + "unpacked");
                break;

            default:
                System.out.println(Messages.invalidOption);
                break;
        }
    }
}
