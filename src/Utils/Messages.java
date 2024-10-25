package Utils;

public class Messages {
    public static String helpMessage = "Usage: lz77 [options] <file_path>\n"
            + "Options:\n"
            + "  -c, --compress      Compress the specified file.\n"
            + "  -d, --decompress    Decompress the specified file.\n"
            + "\n"
            + "Examples:\n"
            + "  lz77 -c somefile.txt          Compress 'somefile.txt'.\n"
            + "  lz77 --decompress somefile.lz  Decompress 'somefile.lz'.\n"
            + "\n"
            + "Note: Ensure that the specified file path is correct and that the file exists.";

    public static String specifyPath = "Specify the path after -c or -d\nex.: lz76 -c somefile.txt";
    public static String invalidOption = "Invalid option. Use -c/--compress or -d/--decompress.";

}
