import java.util.ArrayList;

public class day_11 {
    public static void main(String[] args) {
        String[] oldGalaxy = ReadFile.getFile("day_11.txt");
        char[][] map = expandSpace(oldGalaxy);

        for (char[] line : map) {
            for (char c : line) {
                System.out.print(c);
            }
            System.out.println();
        }
    }

    public static char[][] expandSpace(String[] map) {
        ArrayList<Integer[]> galaxys = new ArrayList<>();

    }
}
