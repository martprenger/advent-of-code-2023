import java.util.ArrayList;

public class day_9 {
    public static void main(String[] args) {
        String[] input = ReadFile.getFile("day_9.txt");
        ArrayList<ArrayList<Integer>> readings = new ArrayList<>();

        for (String i : input) {
            ArrayList<Integer> reading = new ArrayList<>();
            String[] temp = i.split(" ");
            for (String j : temp) {
                reading.add(Integer.parseInt(j));
            }
            readings.add(reading);
        }

        int rt = 0;

        for (ArrayList<Integer> reading : readings) {
            rt += getPreviousValue(reading);
        }

        System.out.println(rt);

    }

    public static int getNextValue(ArrayList<Integer> reading) {
        if (reading.stream().allMatch(item -> item == 0)) {
            return 0;
        }
        ArrayList<Integer> temp = new ArrayList<>();

        for (int i = 0; i < reading.size()-1; i++) {
            temp.add(reading.get(i+1)-reading.get(i));
        }

        return reading.get(reading.size()-1) + getNextValue(temp);
    }

    public static int getPreviousValue(ArrayList<Integer> reading) {
        if (reading.stream().allMatch(item -> item == 0)) {
            return 0;
        }
        ArrayList<Integer> temp = new ArrayList<>();

        for (int i = 0; i < reading.size()-1; i++) {
            temp.add(reading.get(i+1)-reading.get(i));
        }

        return reading.get(0) - getPreviousValue(temp);
    }
}
