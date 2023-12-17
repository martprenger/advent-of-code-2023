import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class day_15 {
    public static void main(String[] args) {
        String[] input = ReadFile.getFile("day_15.txt");
        String[] strings = input[0].split(",");

        long pat1 = 0;

        for (String i : strings) {
            pat1 += hash(i);
        }
        System.out.println("Part 1: " + pat1);

        long part2 = 0;

        part2 += hashmap(input[0]);


        System.out.println("Part 2: " + part2);
    }

    public static long hashmap(String s) {
        HashMap<Integer, ArrayList<String>> boxes = new HashMap<>();
        long rt = 0L;

        String[] strings = s.split(",");

        for (String i : strings) {
            StringBuilder label = new StringBuilder();
            StringBuilder operation = new StringBuilder();
            StringBuilder strength = new StringBuilder();
            for (char j : i.toCharArray()) {
                if (Character.isLetter(j)) {
                    label.append(j);
                } else if (Character.isDigit(j)) {
                    strength.append(j);
                } else if (j == '-' || j == '=') {
                    operation.append(j);
                }
            }

            if (operation.toString().equals("=")) {
                if (boxes.containsKey(hash(label.toString()))) {
                    ArrayList<String> temp = boxes.get(hash(label.toString()));
                    boolean labelExists = false;
                    String newElement = label + "," + strength;
                    for (int j = 0; j < temp.size(); j++) {
                        String element = temp.get(j);
                        if (element.startsWith(label + ",")) {
                            // Replace the element with the new one
                            temp.set(j, newElement);
                            labelExists = true;
                            break;
                        }
                    }
                    if (!labelExists) {
                        temp.add(newElement);
                    }
                } else {
                    ArrayList<String> temp = new ArrayList<>();
                    temp.add(label + "," + strength);
                    boxes.put(hash(label.toString()), temp);
                }
            } else if (operation.toString().equals("-")) {
                if (boxes.containsKey(hash(label.toString()))) {
                    ArrayList<String> temp = boxes.get(hash(label.toString()));
                    temp.removeIf(j -> j.startsWith(label.toString()+","));
                    boxes.put(hash(label.toString()), temp);
                }
            }
        }

        for (Map.Entry<Integer, ArrayList<String>> entry : boxes.entrySet()) {
            Integer key = entry.getKey();
            long slot = 0;
            for (String i : entry.getValue()) {
                slot++;
                rt += (key + 1) * slot * Integer.parseInt(i.split(",")[1]);
            }
        }
        return rt;
    }

    public static int hash(String s) {
        int rt = 0;
        for (char i : s.toCharArray()) {
            rt += i;
            rt *= 17;
            rt = rt % 256;
        }
        return rt;
    }
}
