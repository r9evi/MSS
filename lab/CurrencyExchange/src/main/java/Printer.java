public class Printer {
    public static final StringBuilder sb = new StringBuilder();

    public static void print() {
        System.out.println(sb);
        sb.setLength(0);
    }
}
