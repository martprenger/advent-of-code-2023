import java.util.Arrays;

public class day_12 {
    public static void main(String[] args) {
        String[] input = ReadFile.getFile("day_12.txt");

        System.out.println(getCombinations(input[0]));
    }

    public static boolean isBitSet(int num, int bit) {
        return (num & (1 << bit)) != 0;
    }

    public static int getCombinations(String line) {
        char[] spring = line.split(" ")[0].toCharArray();

        int[] lengths = Arrays.stream(line.split(" ")[1].split(","))
                        .mapToInt(Integer::parseInt)
                        .toArray();

        int rt = 0;
        int n = spring.length;
        int totalCombinations = 1 << n;

        for (int i = 0; i < totalCombinations; i++) {
            char[] temp = spring.clone(); // Create a copy of the original array

            for (int j = 0; j < n; j++) {
                if (spring[j] == '?') {
                    if (isBitSet(i, j)) {
                        temp[j] = '#';
                    } else {
                        temp[j] = '.';
                    }
                }
            }
            if (posibleLine(temp, lengths)) rt++;
        }


        return rt;
    }

    public static boolean posibleLine(char[] line, int[] lengths) {
        int pointer = 0;
        boolean spring = false;

        for (char c : line) {

        }

        return false;

    }
}
