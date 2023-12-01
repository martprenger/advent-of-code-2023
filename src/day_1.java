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
        System.out.println(getCalibrationDocument("day_1.txt"));
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
        int r = line.length()-1;
        while (l<=r) {
            char i = line.charAt(l);
            if (Character.isDigit(i)) {
                rt += (Character.getNumericValue(i)*10);
                break;
            } else {
                l++;
            }
        }

        while (r>=l) {
            char i = line.charAt(r);
            if (Character.isDigit(i)) {
                rt += Character.getNumericValue(i);
                break;
            } else {
                r--;
            }
        }
        return rt;
    }
}
