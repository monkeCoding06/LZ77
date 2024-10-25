package Utils;

public class ProgressBar {
     public static void printProgressBar(int percentage) {
        int width = 50;
        int progress = (int) (percentage / 100.0 * width);

        StringBuilder bar = new StringBuilder("[");
        for (int i = 0; i < width; i++) {
            if (i < progress) bar.append("=");
            else bar.append(" ");
        }
        bar.append("] ").append(percentage).append("%");
        System.out.print("\r" + bar.toString());
    }
}
