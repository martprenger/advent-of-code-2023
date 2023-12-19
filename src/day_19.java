import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.math.BigInteger;

public class day_19 {
    public static void main(String[] args) {
        String[] input = ReadFile.getFile("day_19.txt");

        ArrayList<String> rules = new ArrayList<>();
        ArrayList<HashMap<String, Integer>> parts = new ArrayList<>();
        int counter = 0;
        while (!input[counter].equals("")) {
            rules.add(input[counter]);
            counter++;
        }
        counter++;
        while (counter < input.length) {
            String[] pairs = input[counter].substring(1, input[counter].length() - 1).split(",");

            // Create a map to store the values
            HashMap<String, Integer> valuesMap = new HashMap<>();

            // Iterate through the key-value pairs and add them to the map
            for (String pair : pairs) {
                String[] keyValue = pair.split("=");
                String key = keyValue[0];
                int value = Integer.parseInt(keyValue[1]);
                valuesMap.put(key, value);
            }
            parts.add(valuesMap);
            counter++;
        }

        HashMap<String, String[]> ruleMap = getRuleMap(rules);
        long part1 = 0;

        for (HashMap<String, Integer> part : parts) {
            if (followRules("in", part, ruleMap).equals("A")) {
                for (Map.Entry<String, Integer> entry : part.entrySet()) {
                    part1 += entry.getValue();
                }
            }
        }

        // part 1
        System.out.println("part 1: " + part1);

        // part 2
        HashMap<String, int[]> values = new HashMap<>();
        values.put("x", new int[] {1, 4000});
        values.put("m", new int[] {1, 4000});
        values.put("a", new int[] {1, 4000});
        values.put("s", new int[] {1, 4000});


        // part 2 is not done
        
        System.out.println(getPossibleCombinations("in", ruleMap, values));
        System.out.println("167409079868000");


    }

    public static BigInteger getPossibleCombinations(String name, HashMap<String, String[]> rules, HashMap<String, int[]> range) {
        if (name.equals("A")) {
            BigInteger rt = BigInteger.ONE;
            for (Map.Entry<String, int[]> entry : range.entrySet()) {
                BigInteger test = BigInteger.valueOf(entry.getValue()[1] - entry.getValue()[0] + 1);
                if (test.compareTo(BigInteger.ZERO) < 0) {
                    test = BigInteger.ZERO;
                    break;
                }
                rt = rt.multiply(test);
            }
            return rt;
        } else if (name.equals("R")) {
            return BigInteger.ZERO;
        }

        BigInteger rt = BigInteger.ZERO;

        String[] rule = rules.get(name);

        for (int i = 0; i < rule.length - 1; i++) {
            String condition = rule[i];
            String[] split = condition.split(":");
            String newRt = split[1];
            String newRule = split[0];

            if (newRule.contains("<")) {
                String[] split2 = newRule.split("<");
                String key = split2[0];
                int value = Integer.parseInt(split2[1]);
                int[] newRange = range.get(key).clone();
                newRange[1] = Math.min(newRange[1], value - 1);

                int[] oldRange = range.get(key);
                oldRange[1] = newRange[1] + 1;

                HashMap<String, int[]> newRangeMap = new HashMap<>();
                for (Map.Entry<String, int[]> entry : range.entrySet()) {
                    int[] clonedArray = entry.getValue().clone(); // Deep clone of the int array
                    newRangeMap.put(entry.getKey(), clonedArray);
                }

                newRangeMap.put(key, newRange);

                rt = rt.add(getPossibleCombinations(newRt, rules, newRangeMap));
            } else if (newRule.contains(">")) {
                String[] split2 = newRule.split(">");
                String key = split2[0];
                int value = Integer.parseInt(split2[1]);
                int[] newRange = range.get(key).clone();
                newRange[0] = Math.max(newRange[0], value + 1);

                int[] oldRange = range.get(key);
                oldRange[0] = newRange[0] - 1;

                HashMap<String, int[]> newRangeMap = new HashMap<>();
                for (Map.Entry<String, int[]> entry : range.entrySet()) {
                    int[] clonedArray = entry.getValue().clone(); // Deep clone of the int array
                    newRangeMap.put(entry.getKey(), clonedArray);
                }

                newRangeMap.put(key, newRange);

                rt = rt.add(getPossibleCombinations(newRt, rules, newRangeMap));
            } else {
                System.out.println(newRule);
                throw new IllegalArgumentException("Invalid rule");
            }
        }

        rt = rt.add(getPossibleCombinations(rule[rule.length - 1], rules, range));

        return rt;
    }
    public static String followRules(String name, HashMap<String, Integer> values, HashMap<String, String[]> rules) {
        String rt = name;

        while (!rt.equals("R") && !rt.equals("A")) {
            String[] rule = rules.get(rt);
            for (int i = 0; i < rule.length - 1; i++) {
                String[] split = rule[i].split(":");
                String newRt = split[1];
                String newRule = split[0];

                if (newRule.contains("<")) {
                    String[] split2 = newRule.split("<");

                    String key = split2[0];
                    int value = Integer.parseInt(split2[1]);
                    //System.out.println("key: " + values.get(key) + " value: " + value + " booelen: " + (values.get(key) < value) + " next: " + newRt);
                    if (values.get(key) < value) {
                        rt = newRt;
                        break;
                    }
                } else if (newRule.contains(">")) {
                    String[] split2 = newRule.split(">");

                    String key = split2[0];
                    int value = Integer.parseInt(split2[1]);
                    //System.out.println("key: " + values.get(key) + " value: " + value + " booelen: " + (values.get(key) > value) + " next: " + newRt);
                    if (values.get(key) > value) {
                        rt = newRt;
                        break;
                    }
                } else {
                    System.out.println(newRule);
                    throw new IllegalArgumentException("Invalid rule");
                }

                rt = rule[rule.length - 1];
            }
        }

        return rt;
    }

    public static HashMap<String, String[]> getRuleMap(ArrayList<String> rules) {
        HashMap<String, String[]> ruleMap = new HashMap<>();
        for (String rule : rules) {
            String[] split = rule.split("\\{");
            String name = split[0];
            String[] nRules = split[1].substring(0, split[1].length() - 1).split(",");
            ruleMap.put(name, nRules);
        }
        return ruleMap;
    }
}
