import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class day_11 {
    public static Set<Integer> galaxysx = new HashSet<>();
    public static Set<Integer> galaxysy = new HashSet<>();


    public static void main(String[] args) {
        String[] oldGalaxy = ReadFile.getFile("day_11.txt");
        char[][] map = expandSpace2(oldGalaxy);

        long rt = shortestPath(map);
        System.out.println(rt);
    }

    public static long shortestPath(char[][] map) {
        Set<int[]> galaxys = new HashSet<>();

        for (int i = 0; i < map.length; i++) {
            for (int j = 0; j < map[i].length; j++) {
                if (map[i][j] == '#') {
                    galaxys.add(new int[]{i, j});
                }
            }
        }

        long rt = 0;
        for (int[] i : galaxys) {
            for (int[] j : galaxys) {
                if (!(j == i)) {
                    if (i[0] < j[0]) {
                        for (int k = i[0]; k < j[0]; k++) {
                            if (galaxysx.contains(k)) rt += 1000000;
                            else rt++;
                        }
                    } else {
                        for (int k = i[0]; k > j[0]; k--) {
                            if (galaxysx.contains(k)) rt += 1000000;
                            else rt++;
                        }
                    }
                }

                if (i[1] < j[1]) {
                    for (int k = i[1]; k < j[1]; k++) {
                        if (galaxysy.contains(k)) rt += 1000000;
                        else rt++;
                    }
                } else {
                    for (int k = i[1]; k > j[1]; k--) {
                        if (galaxysy.contains(k)) rt += 1000000;
                        else rt++;
                    }
                }
            }
        }

        return rt / 2;
    }

    public static char[][] expandSpace2(String[] map) {
        char[][] charArray = new char[map.length][];
        for (int i = 0; i < map.length; i++) {
            charArray[i] = map[i].toCharArray();
        }

        for (int i = 0; i < charArray.length; i++) {
            boolean x = true;
            for (char c : charArray[i]) {
                if (c == '#') x = false;
            }
            if (x) galaxysx.add(i);
        }

        for (int i = 0; i < charArray[0].length; i++) {
            boolean y = true;
            for (int j = 0; j < charArray.length; j++) {
                if (charArray[j][i] == '#') y = false;
            }
            if (y) galaxysy.add(i);
        }

        return charArray;
    }

    public static char[][] expandSpace(String[] map) {
        ArrayList<char[]> temp = new ArrayList<>();

        for (String line : map) {
            temp.add(line.toCharArray());
            if (!line.contains("#")) {
                char[] charArray = new char[line.length()];
                Arrays.fill(charArray, '.');
                temp.add(charArray);
            }
        }
        int newLines = 0;

        int rows = temp.size();
        int maxLength = temp.get(0).length; // Assuming all rows have the same length

        ArrayList<ArrayList<Character>> temp2 = new ArrayList<>();
        for (int i = 0; i < rows; i++) {
            temp2.add(new ArrayList<>()); // Initialize inner ArrayLists for each row
        }

        for (int i = 0; i < temp.get(0).length + newLines; i++) {
            boolean noGalacy = true;
            for (int j = 0; j < temp.size(); j++) {
                char c = temp.get(j)[i];
                temp2.get(j).add(c);
                if (c == '#') noGalacy = false;
            }

            if (noGalacy) {
                for (int j = 0; j < temp.size(); j++) {
                    temp2.get(j).add('.');
                }
            }
        }

        int rows2 = temp2.size();
        int maxLength2 = 0;
        for (ArrayList<Character> list2 : temp2) {
            maxLength2 = Math.max(maxLength2, list2.size());
        }

        // Create the char[][] array
        char[][] charArray = new char[rows2][maxLength2];

        // Fill the char[][] array with the ArrayList contents
        for (int i = 0; i < rows; i++) {
            ArrayList<Character> row = temp2.get(i);
            for (int j = 0; j < row.size(); j++) {
                charArray[i][j] = row.get(j);
            }
        }
        return charArray;
    }
}
