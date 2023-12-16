import java.util.ArrayList;

public class day_13 {
    public static void main(String[] args) {
        String[] input = ReadFile.getFile("day_13.txt");

        ArrayList<ArrayList<String>> maps = new ArrayList<>();
        ArrayList<String> map = new ArrayList<>();
        for (String line : input) {
            if (line.isEmpty()) {
                maps.add(map);
                map = new ArrayList<>();
            } else {
                map.add(line);
            }
        }
        maps.add(map);

        long part1 = 0;
        long part2 = 0;

        for (ArrayList<String> map1 : maps) {
            System.out.println("Map: ");
            int verticalReflection = findVerticalReflection(map1);
            int horizontalReflection = findHorzontalReflection(map1);
            System.out.println("Vertical Reflection: " + verticalReflection);
            System.out.println("Horizontal Reflection: " + horizontalReflection);

            if (verticalReflection != 0) {
                part1 += verticalReflection;
            } else if (horizontalReflection != 0) {
                part1 += horizontalReflection * 100L;
            }

            int newVerticalReflection = findNewVerticalReflection(map1);
            int newHorizontalReflection = findNewHorzontalReflection(map1);
            System.out.println("New Vertical Reflection: " + newVerticalReflection);
            System.out.println("New Horizontal Reflection: " + newHorizontalReflection);
            if (newHorizontalReflection != 0) {
                part2 += newHorizontalReflection * 100L;
            } else if (newVerticalReflection != 0) {
                part2 += newVerticalReflection;
            }

        }

        System.out.println("Part 1: " + part1);
        System.out.println("Part 2: " + part2);
    }

    public static int findHorzontalReflection(ArrayList<String> map) {
        ArrayList<Integer> posibleReflections = new ArrayList<>();

        for (int i = 1; i < map.size(); i++) {
            boolean isReflection = true;
            for (int j = 0; j < map.get(i).length(); j++) {
                if (!(map.get(i - 1).charAt(j) == map.get(i).charAt(j))) {
                    isReflection = false;
                    break;
                }
            }
            if (isReflection) {
                posibleReflections.add(i - 1);
            }
        }

        for (int reflection : posibleReflections) {
            boolean isReflection = true;
            int up = reflection;
            int down = reflection + 1;
            while (up >= 0 && down < map.size()) {
                for (int j = 0; j < map.get(up).length(); j++) {
                    if (!(map.get(up).charAt(j) == map.get(down).charAt(j))) {
                        isReflection = false;
                        break;
                    }
                }
                if (isReflection) {
                    up--;
                    down++;
                } else {
                    break;
                }
            }
            if (isReflection) {
                return reflection + 1;
            }
        }

        return 0;
    }

    public static int findVerticalReflection(ArrayList<String> map) {
        ArrayList<Integer> posibleReflections = new ArrayList<>();

        for (int i = 1; i < map.get(0).length(); i++) {
            boolean isReflection = true;
            for (int j = 0; j < map.size(); j++) {
                if (!(map.get(j).charAt(i - 1) == map.get(j).charAt(i))) {
                    isReflection = false;
                    break;

                }
            }
            if (isReflection) {
                posibleReflections.add(i - 1);
            }
        }

        for (int reflection : posibleReflections) {
            boolean isReflection = true;
            int left = reflection;
            int right = reflection + 1;
            while (left >= 0 && right < map.get(0).length()) {
                for (int i = 0; i < map.size(); i++) {
                    if (!(map.get(i).charAt(left) == map.get(i).charAt(right))) {
                        isReflection = false;
                        break;
                    }
                }
                if (!isReflection) {
                    break;
                } else {
                    left--;
                    right++;
                }
            }
            if (isReflection) {
                return reflection + 1;
            }
        }

        return 0;
    }

    public static int findNewHorzontalReflection(ArrayList<String> map) {
        ArrayList<Integer> posibleReflections = new ArrayList<>();
        boolean fixedSmudge = false;

        for (int i = 1; i < map.size(); i++) {
            fixedSmudge = false;
            boolean isReflection = true;
            for (int j = 0; j < map.get(i).length(); j++) {
                if (!(map.get(i - 1).charAt(j) == map.get(i).charAt(j))) {
                    if (!fixedSmudge) {
                        fixedSmudge = true;
                    } else {
                        isReflection = false;
                        break;
                    }
                }
            }
            if (isReflection) {
                posibleReflections.add(i - 1);
            }
        }

        for (int reflection : posibleReflections) {
            fixedSmudge = false;
            boolean isReflection = true;
            int up = reflection;
            int down = reflection + 1;
            while (up >= 0 && down < map.size()) {
                for (int j = 0; j < map.get(up).length(); j++) {
                    if (!(map.get(up).charAt(j) == map.get(down).charAt(j))) {
                        if (!fixedSmudge) {
                            fixedSmudge = true;
                        } else {
                            isReflection = false;
                            break;
                        }
                    }
                }
                if (isReflection) {
                    up--;
                    down++;
                } else {
                    break;
                }
            }
            if (isReflection && fixedSmudge) {
                return reflection + 1;
            }
        }

        return 0;
    }

    public static int findNewVerticalReflection(ArrayList<String> map) {
        ArrayList<Integer> posibleReflections = new ArrayList<>();
        boolean fixedSmudge = false;

        for (int i = 1; i < map.get(0).length(); i++) {
            fixedSmudge = false;
            boolean isReflection = true;
            for (int j = 0; j < map.size(); j++) {
                if (!(map.get(j).charAt(i - 1) == map.get(j).charAt(i))) {
                    if (!fixedSmudge) {
                        fixedSmudge = true;
                    } else {
                        isReflection = false;
                        break;
                    }
                }
            }
            if (isReflection) {
                posibleReflections.add(i - 1);
            }
        }

        for (int reflection : posibleReflections) {
            fixedSmudge = false;
            boolean isReflection = true;
            int left = reflection;
            int right = reflection + 1;
            while (left >= 0 && right < map.get(0).length()) {
                for (int j = 0; j < map.size(); j++) {
                    if (!(map.get(j).charAt(left) == map.get(j).charAt(right))) {
                        if (!fixedSmudge) {
                            fixedSmudge = true;
                        } else {
                            isReflection = false;
                            break;
                        }
                    }
                }
                if (isReflection) {
                    left--;
                    right++;
                } else {
                    break;
                }
            }
            if (isReflection && fixedSmudge) {
                return reflection + 1;
            }
        }

        return 0;
    }
}
