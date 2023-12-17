import java.util.ArrayList;

public class day_16 {
    static int[][] intensity;
    static char[][] map;
    static ArrayList<String> visited;

    public static void main(String[] args) {
        String[] input = ReadFile.getFile("day_16.txt");
        map = new char[input.length][input[0].length()];
        for (int i = 0; i < input.length; i++) {
            map[i] = input[i].toCharArray();
        }

        long rt = 0;

        System.out.println("direction 1");

        for (int i = 0; i < map[0].length; i++) {
            System.out.println(i);
            long temp = startMove(new int[] {0, i}, new int[] {1, 0});
            if (temp > rt) {
                rt = temp;
            }
        }

        System.out.println("direction 2");

        for (int i = 0; i < map[0].length; i++) {
            System.out.println(i);
            long temp = startMove(new int[] {map.length-1, i}, new int[] {-1, 0});
            if (temp > rt) {
                rt = temp;
            }
        }

        System.out.println("direction 3");

        for (int i = 0; i < map.length; i++) {
            System.out.println(i);
            long temp = startMove(new int[] {i, 0}, new int[] {0, 1});
            if (temp > rt) {
                rt = temp;
            }
        }

        System.out.println("direction 4");

        for (int i = 0; i < map.length; i++) {
            System.out.println(i);
            long temp = startMove(new int[] {i, map[0].length-1}, new int[] {0, -1});
            if (temp > rt) {
                rt = temp;
            }
        }

        System.out.println(rt);
    }

    public static long startMove(int[] pos, int[] dir) {
        intensity = new int[map.length][map[0].length];
        visited = new ArrayList<>();

        ArrayList<int[]> beams = new ArrayList<>();

        int [] firstMove = move(pos, dir);

        if (firstMove.length == 4) {
            beams.add(firstMove);
        } else if (firstMove.length == 8){
            beams.add(new int[] {firstMove[0], firstMove[1], firstMove[2], firstMove[3]});
            beams.add(new int[] {firstMove[4], firstMove[5], firstMove[6], firstMove[7]});
        }

        while (beams.size() > 0) {
            int[] beam = beams.get(0);
            beams.remove(0);
            int[] newBeam = move(new int[] {beam[0], beam[1]}, new int[] {beam[2], beam[3]});
            if (newBeam != null) {
                if (newBeam.length == 4) {
                    beams.add(newBeam);
                } else if (newBeam.length == 8){
                    beams.add(new int[] {newBeam[0], newBeam[1], newBeam[2], newBeam[3]});
                    beams.add(new int[] {newBeam[4], newBeam[5], newBeam[6], newBeam[7]});
                }
            }
        }

        long count = 0;
        for (int[] row : intensity) {
            for (int c : row) {
                if (c > 0) {
                    count++;
//                    System.out.print('#');
                } else {
//                    System.out.print('.');
                }
//                System.out.print(c);
            }
        }
        return count;
    }

    public static int[] move(int[] pos, int[] dir) {
        intensity[pos[0]][pos[1]]++;

        String move = pos[0] + "," + pos[1] + " " + dir[0] + "," + dir[1];

        if (visited.contains(move)) {
            visited.remove(move);
            visited.add(move+"2");
        } else if (visited.contains(move+"2")) {
            return null;
        } else {
            visited.add(move);
        }

        int[] newPos = new int[2];
        newPos[0] = pos[0] + dir[0];
        newPos[1] = pos[1] + dir[1];

        int[] newDir = new int[2];
        newDir[0] = 0;
        newDir[1] = 0;

        if (newPos[0] < 0 || newPos[0] >= map.length || newPos[1] < 0 || newPos[1] >= map[0].length) {
            return null;
        }
        if (map[newPos[0]][newPos[1]] == '/') {
            if (dir[1] == 0) {
                if (dir[0] == 1) {
                    newDir[1] = -1;
                } else {
                    newDir[1] = 1;
                }
            } else {
                if (dir[1] == 1) {
                    newDir[0] = -1;
                } else {
                    newDir[0] = 1;
                }
            }
            return new int [] {newPos[0], newPos[1], newDir[0], newDir[1]};
//            move(newPos, newDir);
        } else if (map[newPos[0]][newPos[1]] == '\\') {
            if (dir[0] == 0) {
                if (dir[1] == 1) {
                    newDir[0] = 1;
                } else {
                    newDir[0] = -1;
                }
            } else {
                if (dir[0] == 1) {
                    newDir[1] = 1;
                } else {
                    newDir[1] = -1;
                }
            }
            return new int [] {newPos[0], newPos[1], newDir[0], newDir[1]};
//            move(newPos, newDir);
        } else if (map[newPos[0]][newPos[1]] == '-') {
            if (dir[0] != 0) {
                return new int[] {newPos[0], newPos[1], 0, -1, newPos[0], newPos[1], 0, 1};
//                move(newPos, new int[] {0, -1});
//                move(newPos, new int[] {0, 1});
            } else {
                newDir[1] = dir[1];
                return new int[] {newPos[0], newPos[1], newDir[0], newDir[1]};
//                move(newPos, newDir);
            }
        } else if (map[newPos[0]][newPos[1]] == '|') {
            if (dir[1] != 0) {
                return new int[] {newPos[0], newPos[1], -1, 0, newPos[0], newPos[1], 1, 0};
//                move(newPos, new int[] {-1, 0});
//                move(newPos, new int[] {1, 0});
            } else {
                newDir[0] = dir[0];
                return new int[] {newPos[0], newPos[1], newDir[0], newDir[1]};
//                move(newPos, newDir);
            }
        } else {
            newDir[0] = dir[0];
            newDir[1] = dir[1];
            return new int[] {newPos[0], newPos[1], newDir[0], newDir[1]};
//            move(newPos, newDir);
        }
    }
}
