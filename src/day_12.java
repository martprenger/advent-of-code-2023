import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class day_12 {
    public static void main(String[] args) {
        String[] input = ReadFile.getFile("day_12.txt");

//        System.out.println(getCombinationsp2(input[0]));
//        System.out.println(getCombinationsp2("??????##??????.? 1,3,1,1,1"));

        long rt = 0;
        for (String line : input){
            rt += getCombinationsp2(line);
        }
        System.out.println(rt);
    }

    public static long countWays(String s, int[] targetRuns) {
        int maxRun = 0;
        for (int run : targetRuns) {
            maxRun = Math.max(maxRun, run);
        }

        int[] targetRunsCopy = new int[targetRuns.length + 1];
        System.arraycopy(targetRuns, 0, targetRunsCopy, 0, targetRuns.length);
        targetRunsCopy[targetRuns.length] = 0;

        maxRun = Math.max(maxRun, targetRunsCopy[targetRunsCopy.length - 1]);
        s = s + ".";

        int n = s.length();
        int m = targetRunsCopy.length;
        long[][][] dp = new long[n][m][maxRun + 1];

        for (int i = 0; i < n; i++) {
            char x = s.charAt(i);
            for (int j = 0; j < m; j++) {
                for (int k = 0; k <= targetRunsCopy[j]; k++) {
                    // Base case
                    if (i == 0) {
                        if (j != 0) {
                            dp[i][j][k] = 0;
                            continue;
                        }

                        if (x == '#') {
                            if (k != 1) {
                                dp[i][j][k] = 0;
                                continue;
                            }
                            dp[i][j][k] = 1;
                            continue;
                        }

                        if (x == '.') {
                            if (k != 0) {
                                dp[i][j][k] = 0;
                                continue;
                            }
                            dp[i][j][k] = 1;
                            continue;
                        }

                        if (x == '?') {
                            if (k != 0 && k != 1) {
                                dp[i][j][k] = 0;
                                continue;
                            }
                            dp[i][j][k] = 1;
                            continue;
                        }
                    }

                    // Process answer if current char is .
                    long ansDot;
                    if (k != 0) {
                        ansDot = 0;
                    } else if (j > 0) {
                        assert k == 0;
                        ansDot = dp[i - 1][j - 1][targetRunsCopy[j - 1]];
                        ansDot += dp[i - 1][j][0];
                    } else {
                        ansDot = s.substring(0, i).chars().filter(c -> c == '#').count() == 0 ? 1 : 0;
                    }

                    // Process answer if current char is #
                    long ansPound;
                    if (k == 0) {
                        ansPound = 0;
                    } else {
                        ansPound = dp[i - 1][j][k - 1];
                    }

                    if (x == '.') {
                        dp[i][j][k] = ansDot;
                    } else if (x == '#') {
                        dp[i][j][k] = ansPound;
                    } else {
                        dp[i][j][k] = ansDot + ansPound;
                    }
                }
            }
        }

        return dp[n - 1][m - 1][0];
    }

    public static long getCombinationsp2(String lines) {
        String[] line = lines.split(" ");

        StringBuilder springBuilder = new StringBuilder();
        for (int i = 0; i < 5; i++) {
            springBuilder.append(line[0]);
            if (i != 4) { // To avoid adding '?' after the last repetition
                springBuilder.append("?");
            }
        }

        StringBuilder lengthBuilder = new StringBuilder();
        for (int i = 0; i < 5; i++) {
            lengthBuilder.append(line[1]);
            if (i != 4) { // To avoid adding '?' after the last repetition
                lengthBuilder.append(",");
            }
        }
        int[] lengths = Arrays.stream(lengthBuilder.toString().split(",")).mapToInt(Integer::parseInt).toArray();

        return countWays(springBuilder.toString(), lengths);
    }

    public static int getCombinations(String line) {
        char[] spring = line.split(" ")[0].toCharArray();

        int[] lengths = Arrays.stream(line.split(" ")[1].split(","))
                        .mapToInt(Integer::parseInt)
                        .toArray();

        int rt = 0;
        int n = spring.length;
        int totalCombinations = 1 << new String(spring).chars().filter(ch -> ch == '?').count();

        for (int i = 0; i < totalCombinations; i++) {
            char[] temp = spring.clone(); // Create a copy of the original array
            int combinationIndex = 0;

            for (int j = 0; j < n; j++) {
                if (temp[j] == '?') {
                    if (((i >> combinationIndex) & 1) == 1) {
                        temp[j] = '#';
                    } else {
                        temp[j] = '.';
                    }
                    combinationIndex++;
                }
            }

            //System.out.println(temp);
            if (posibleLine(temp, lengths)) {
                rt++;
                //System.out.println("true");
            }
        }

        return rt;
    }

    public static boolean posibleLine(char[] line, int[] lengths) {
        int pointer = 0;
        boolean spring = false;
        int counter = 0;
        int[] temp = new int[lengths.length];

        for (char c : line) {
            if (!spring && c == '#') {
                counter++;
                spring = true;
            } else if (c == '#') {
                counter++;
            } else if (spring){
                spring = false;
                if (pointer >= lengths.length) {
                    return false;
                }
                temp[pointer] = counter;
                pointer++;
                counter = 0;
            }
        }
        if (spring) {
            if (pointer >= lengths.length) {
                return false;
            }
            temp[pointer] = counter;
        }

        for (int i = 0; i < temp.length; i++) {
            if (temp[i] != lengths[i]) {
                return false;
            }
        }

        return true;

    }
}
