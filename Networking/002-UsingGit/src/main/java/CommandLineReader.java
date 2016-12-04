/**
 * Created by blad on 1/21/15.
 */
public class CommandLineReader {
    public static void main(String[] args) {
        System.out.println(String.format("There are %d arguments", args.length));
        for (int i = 0; i < args.length; i++) {
            System.out.println(String.format("  %s", args[i]));
        }
    }
}
