import java.util.ArrayList;
import java.util.Arrays;

public class day_14 {
    public static void main(String[] args) {
        String[] input = ReadFile.getFile("day_14.txt");

        char[][] map = new char[input.length][input[0].length()];
        for (int i = 0; i < input.length; i++) {
            map[i] = input[i].toCharArray();
        }

        ArrayList<char[][]> maps = new ArrayList<>();

        int cycle = 0;

        while (cycle < 1000000000) {
            map = cycle(map);
            cycle++;

            int count = 0;

            for (char[][] map1 : maps) {
                //this doesn't work but don't know why
                if (compareMaps(map1, map)) {
                    System.out.println("Found a match at cycle " + cycle);

                    int c = cycle - count;
                    System.out.println("Cycle difference: " + c);
                    int index = (1000000000 - count) % c + count;
                    System.out.println("Index: " + index);
                    char[][] testMap = maps.get(index-3);
                    System.out.println("final cycle: " + index + " Map load: " + getMapLoad(testMap));
                    System.exit(0);
                }

                count++;
            }
            System.out.println("Cycle: " + cycle + " Map load: " + getMapLoad(map));
            maps.add(deepCopy(map));
        }
    }

    public static char[][] deepCopy(char[][] original) {
        if (original == null) return null;

        int length = original.length;
        char[][] copy = new char[length][];

        for (int i = 0; i < length; ++i) {
            copy[i] = Arrays.copyOf(original[i], original[i].length);
        }

        return copy;
    }

    public static boolean compareMaps(char[][] map1, char[][] map2) {
        for (int i = 0; i < map1.length; i++) {
            for (int j = 0; j < map2[i].length; j++) {
                if (map1[i][j] != map2[i][j]) {
                    return false;
                }
            }
        }
        return true;
    }

    public static char[][] cycle(char[][] map) {
        map = updateMapNorth(map);
        map = updateMapWest(map);
        map = updateMapSouth(map);
        map = updateMapEast(map);

        return map;
    }

    public static int getMapLoad(char[][] map) {
        int load = 0;
        int weight = map.length;
        for (int i = 0; i < map.length; i++) {
            for (char aChar : map[i]) {
                if (aChar == 'O') {
                    load += weight;
                }
            }
            weight--;
        }
        return load;
    }

    public static char[][] updateMapNorth(char[][] map) {
        for (int i = 0; i < map[0].length; i++) {
            int pointer = 0;

            while (pointer < map.length) {
                if (map[pointer][i] == 'O') {
                    int temp = pointer;
                    while (0 < temp && map[temp - 1][i] == '.') {
                        temp--;
                    }
                    char tempChar = map[temp][i];
                    map[temp][i] = map[pointer][i];
                    map[pointer][i] = tempChar;
                }
                pointer++;
            }
        }
        return map;
    }

    public static char[][] updateMapSouth(char[][] map) {
        for (int i = 0; i < map[0].length; i++) {
            int pointer = map.length - 1; // Start from the bottom

            while (pointer >= 0) { // Loop from bottom to top
                if (map[pointer][i] == 'O') {
                    int temp = pointer;
                    while (temp < map.length - 1 && map[temp + 1][i] == '.') {
                        temp++;
                    }
                    char tempChar = map[temp][i];
                    map[temp][i] = map[pointer][i];
                    map[pointer][i] = tempChar;
                }
                pointer--;
            }
        }
        return map;
    }

    public static char[][] updateMapWest(char[][] map) {
        for (int i = 0; i < map.length; i++) {
            int pointer = 0; // Start from the left

            while (pointer < map[i].length) { // Loop from left to right
                if (map[i][pointer] == 'O') {
                    int temp = pointer;
                    while (temp > 0 && map[i][temp - 1] == '.') {
                        temp--;
                    }
                    char tempChar = map[i][temp];
                    map[i][temp] = map[i][pointer];
                    map[i][pointer] = tempChar;
                }
                pointer++;
            }
        }
        return map;
    }

    public static char[][] updateMapEast(char[][] map) {
        for (int i = 0; i < map.length; i++) {
            int pointer = map[i].length - 1; // Start from the left

            while (pointer >= 0) { // Loop from left to right
                if (map[i][pointer] == 'O') {
                    int temp = pointer;
                    while (temp < map[i].length - 1 && map[i][temp + 1] == '.') {
                        temp++;
                    }
                    char tempChar = map[i][temp];
                    map[i][temp] = map[i][pointer];
                    map[i][pointer] = tempChar;
                }
                pointer--;
            }
        }
        return map;
    }
}
