import java.util.HashMap;
import java.util.Map;

public class day_1 {
    /*
    The newly-improved calibration document consists of lines of text; each line originally contained a specific calibration value that the Elves now need to recover. On each line, the calibration value can be found by combining the first digit and the last digit (in that order) to form a single two-digit number.

    For example:

    1abc2
    pqr3stu8vwx
    a1b2c3d4e5f
    treb7uchet

    In this example, the calibration values of these four lines are 12, 38, 15, and 77. Adding these together produces 142.

    Consider your entire calibration document. What is the sum of all of the calibration values?
     */
    public static void main(String[] args) {
        System.out.println(getCalibrationDocument("day_1_star_2.txt"));
    }


    public static int getCalibrationDocument(String file) {
        int rt = 0;
        String[] lines = ReadFile.getFile(file);
        for (String i : lines) {
            rt += getCalibrationValue(i);
        }
        return rt;
    }

    private static int getCalibrationValue(String line) {
        int rt = 0;
        int l = 0;
        int r = line.length() - 1;
        while (l <= r) {
            char i = line.charAt(l);
            if (Character.isDigit(i)) {
                rt += (Character.getNumericValue(i) * 10);
                break;
            } else {
                int temp = getWord(line, l);
                if (temp != 0) {
                    rt += temp * 10;
                    break;
                } else {
                    l++;
                }
            }
        }

        while (r >= l) {
            char i = line.charAt(r);
            if (Character.isDigit(i)) {
                rt += Character.getNumericValue(i);
                break;
            } else {
                int temp = getWord(line, r);
                if (temp != 0) {
                    rt += temp;
                    break;
                } else {
                    r--;
                }
            }
        }
        return rt;
    }

    private static int getWord(String line, int start) {
        Map<String, Integer> map = new HashMap<>();
        map.put("one", 1);
        map.put("two", 2);
        map.put("three", 3);
        map.put("four", 4);
        map.put("five", 5);
        map.put("six", 6);
        map.put("seven", 7);
        map.put("eight", 8);
        map.put("nine", 9);

        int rt = 0;
        for (Map.Entry<String, Integer> entry : map.entrySet()) {
            if (line.startsWith(entry.getKey(), start)) {
                rt = entry.getValue();
                break;
            }
        }
        return rt;
    }
}



