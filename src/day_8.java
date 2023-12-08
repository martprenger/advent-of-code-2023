import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

//760362547

public class day_8 {
    public static void main(String[] args) {
        String[] lines = ReadFile.getFile("day_8.txt");

        ArrayList<String> starts = new ArrayList<>();
        char[] instructions = lines[0].toCharArray();

        HashMap<String, String[]> map = new HashMap<>();

        for (int i = 2; i < lines.length; i++) {
            String[] temp = lines[i].split(" = ");
            String location = temp[0];
            String[] directions = temp[1].substring(1, temp[1].length()-1).split(", ");
            map.put(location, directions);

            if (temp[0].endsWith("A")) starts.add(temp[0]);
        }

        ArrayList<Long> total = new ArrayList<>();

        for (String i :starts) {
            System.out.println(i);
            long steps = getSteps(i, instructions, map);
            total.add(steps);
        }

        System.out.println(lcmOfArrayList(total));


    }

    public static long getSteps(String start, char[] instructions, HashMap<String, String[]> map) {
        int counter = 0;
        long steps = 0;
        while (!start.endsWith("Z")) {
            String[] lr = map.get(start);
            if (instructions[counter] == 'L') start = lr[0];
            else start = lr[1];

            if (++counter >= instructions.length) counter = 0;
            steps++;
        }
        return steps;
    }

    public static long gcd(long a, long b) {
        while (b != 0) {
            long temp = b;
            b = a % b;
            a = temp;
        }
        return a;
    }

    // Method to calculate LCM using GCD for an array list
    public static long lcmOfArrayList(ArrayList<Long> numbers) {
        long lcm = numbers.get(0);
        for (int i = 1; i < numbers.size(); i++) {
            long currentNumber = numbers.get(i);
            lcm = (lcm * currentNumber) / gcd(lcm, currentNumber);
        }
        return lcm;
    }
}
