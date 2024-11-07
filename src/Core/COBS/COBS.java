package Core.COBS;

import Utils.File;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class COBS extends File {
    public static void stuff(String fileName) {
        int nextNUL;
        int overHeadByte = 1;
        System.out.print("\r" + "Reading Data...");
        final byte[] fileData = File.readFile(fileName.replace(".txt", ".lz"));
        List<Byte> output = new ArrayList<Byte>();

        if (fileData == null || fileData.length == 0) {
            System.err.println("Error: Could not read file or file is empty.");
            return;
        }
        for (int i = 0; i < fileData.length; i++) {
            if (fileData[i] != (byte) 0) {
                overHeadByte++;
            } else {
                break;
            }
        }

        System.out.println(Arrays.toString(fileData));
        output.add((byte) overHeadByte);
        for (int i = 0; i < fileData.length; i++) {
            nextNUL = 1;
            if (fileData[i] == (byte) 0) {
                if (fileData[i + 1] == (byte) 0) {
                    output.add((byte) nextNUL);
                    continue;
                } else {
                    nextNUL++;
                }
                output.add((byte) nextNUL);
            } else {
                output.add(fileData[i]);
            }
        }
        output.add((byte) 0);

        System.out.println(Arrays.toString(output.toArray()));

    }


    public static void unstuff(String fileName) {
        System.out.println("no work yet");
    }
}
