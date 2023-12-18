import java.util.ArrayList;

public class day_18 {
    public static void main(String[] args) {
        String[] input = ReadFile.getFile("day_18.txt");

        ArrayList<long[]> pointsp1 = new ArrayList<>();
        ArrayList<long[]> pointsp2 = new ArrayList<>();

        long[] currentLocationp1 = {0, 0};
        long[] currentLocationp2 = {0, 0};
        pointsp1.add(new long[]{currentLocationp1[0], currentLocationp1[1]});
        pointsp2.add(new long[]{currentLocationp2[0], currentLocationp2[1]});

        for (String line : input) {
            String[] lineArray = line.split(" ");
            String directionString = lineArray[0];
            long distance = Long.parseLong(lineArray[1]);
            String collor = lineArray[2].substring(2, lineArray[2].length() - 1);

            if (directionString.equals("U")) {
                currentLocationp1[0] -= distance;
                pointsp1.add(new long[]{currentLocationp1[0], currentLocationp1[1]});
            } else if (directionString.equals("D")) {
                currentLocationp1[0] += distance;
                pointsp1.add(new long[]{currentLocationp1[0], currentLocationp1[1]});
            } else if (directionString.equals("L")) {
                currentLocationp1[1] -= distance;
                pointsp1.add(new long[]{currentLocationp1[0], currentLocationp1[1]});
            } else if (directionString.equals("R")) {
                currentLocationp1[1] += distance;
                pointsp1.add(new long[]{currentLocationp1[0], currentLocationp1[1]});
            }
            System.out.println(collor);
            if (collor.charAt(collor.length() - 1) == '3') {
                currentLocationp2[0] -= Long.parseLong(collor.substring(0, collor.length()-1), 16);
                pointsp2.add(new long[]{currentLocationp2[0], currentLocationp2[1]});
            } else if (collor.charAt(collor.length() - 1) == '1') {
                currentLocationp2[0] += Long.parseLong(collor.substring(0, collor.length()-1), 16);
                pointsp2.add(new long[]{currentLocationp2[0], currentLocationp2[1]});
            } else if (collor.charAt(collor.length() - 1) == '2') {
                currentLocationp2[1] -= Long.parseLong(collor.substring(0, collor.length()-1), 16);
                pointsp2.add(new long[]{currentLocationp2[0], currentLocationp2[1]});
            } else if (collor.charAt(collor.length() - 1) == '0') {
                currentLocationp2[1] += Long.parseLong(collor.substring(0, collor.length()-1), 16);
                pointsp2.add(new long[]{currentLocationp2[0], currentLocationp2[1]});
            }
        }

        System.out.println("part 1: " + area(pointsp1));
        System.out.println("part 2: " + area(pointsp2));






    }

    public static long area(ArrayList<long[]> points) {
        long b = 0;
        long[] temp = {0, 0};
        for (long[] point : points) {
            b += Math.abs(point[0] - temp[0]) + Math.abs(point[1] - temp[1]);
            temp = point;
        }

        long a = 0;

        for (int i = 0; i < points.size(); i++) {
            //shoelace formula
            if (i < points.size() - 1) {
                a += points.get(i)[0] * points.get(i + 1)[1] - points.get(i + 1)[0] * points.get(i)[1];
            } else {
                a += points.get(i)[0] * points.get(0)[1] - points.get(0)[0] * points.get(i)[1];
            }
        }

        a = Math.abs(a / 2);

        long i = a - b / 2 + 1;
        return i+b;
    }


}
