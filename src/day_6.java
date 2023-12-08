import java.util.ArrayList;
import java.util.Arrays;
import java.util.stream.Collectors;

public class day_6 {
    public static void main(String[] args) {
        String[] lines = ReadFile.getFile("day_6_star_1.txt");
        ArrayList<Long> input = new ArrayList<>();

        for (String line : lines) {
            Long temp = 0L;
            String numbers = line.split(":")[1];
            for (char i : numbers.toCharArray()) {
                if (Character.isDigit(i)) temp = temp * 10 + Character.getNumericValue(i);
            }
            input.add(temp);
        }

        System.out.println("inputs:");
        for (long i : input) {
            System.out.println(i);
        }
        System.out.println();

        long rt = 0L;


        long wins = 0;
        long time = input.get(0);
        long distance = input.get(1);

        for (int j = 0; j < time; j++) {
            if (j * (time - j) > distance) wins++;
        }
        System.out.println(wins);
        rt = wins;

        System.out.println(rt);
    }
}
