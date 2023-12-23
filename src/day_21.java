import java.awt.*;
import java.math.BigInteger;
import java.util.*;

public class day_21 {
    public static char[][] map;
    public static int[][] directions = new int[][]{{-1, 0}, {1, 0}, {0, -1}, {0, 1}};

    public static void main(String[] args) {

        HashMap<Point, Integer> visited = visited();

        long part1 = visited.entrySet()
                .stream()
                .filter(i -> i.getValue() <= 64 && i.getValue() % 2 == 0)
                .count();
        System.out.println(part1);


        // got part 2 by following https://github.com/villuna/aoc23/blob/main/rust/src/day21.rs alogorithm

        long even_corners = visited.entrySet()
                .stream()
                .filter(i -> i.getValue() % 2 == 0 && i.getValue() > 65)
                .count();
        long odd_corners = visited.entrySet()
                .stream()
                .filter(i -> i.getValue() % 2 == 1 && i.getValue() > 65)
                .count();

        long even_full = visited.entrySet()
                .stream()
                .filter(i -> i.getValue() % 2 == 0)
                .count();
        long odd_full = visited.entrySet()
                .stream()
                .filter(i -> i.getValue() % 2 == 1)
                .count();

        long n = (26501365 - (131 / 2)) / (131);

        //must be 202300
        assert (n == 202300);

        long even = n*n;
        long odd = (n+1)*(n+1);

        long p2 = odd * visited.entrySet()
                .stream().filter(i -> i.getValue() % 2 == 1).count()
                + even * visited.entrySet()
                .stream().filter(i -> i.getValue() % 2 == 0).count()
                - ((n + 1) * odd_corners)
                + (n * even_corners);

        System.out.println(p2);

    }

    public static HashMap<Point, Integer>  visited() {
        String[] input = ReadFile.getFile("day_21.txt");
        map = new char[input.length][input[0].length()];
        for (int i = 0; i < input.length; i++) {
            for (int j = 0; j < input[0].length(); j++) {
                if (input[i].charAt(j) != '#') {
                    map[i][j] = '.';
                    //add distance from start to this point
                } else {
                    map[i][j] = '#';
                }
            }
        }

        int[] start = new int[] {65, 65, 0};

        Queue<int[]> queue = new java.util.LinkedList<>();
        queue.add(start);
        HashMap<Point, Integer> visited = new HashMap<>();

        while (!queue.isEmpty()) {
            int[] current = queue.poll();
            Point currentPoint = new Point(current[0], current[1]);
            if (visited.containsKey(currentPoint)) {
                continue;
            }
            visited.put(currentPoint, current[2]);

            for (int[] dir : directions) {
                int newX = current[0] + dir[0];
                int newY = current[1] + dir[1];
                if (newX >= 0 && newX < map.length && newY >= 0 && newY < map[0].length && map[newX][newY] == '.') {
                    queue.add(new int[]{newX, newY, current[2] + 1});
                }
            }

        }

        return visited;
    }

    public static long part1(int iterations) {
        String[] input = ReadFile.getFile("day_21.txt");
        map = new char[input.length][input[0].length()];
        int[] start = new int[2];

        for (int i = 0; i < input.length; i++) {
            for (int j = 0; j < input[0].length(); j++) {
                if (input[i].charAt(j) == 'S') {
                    map[i][j] = '.';
                    start = new int[]{i, j};
                } else {
                    map[i][j] = input[i].charAt(j);
                }
            }
        }

        HashSet<Point> visited = new HashSet<>();
        visited.add(new Point(start[0], start[1]));

        for (int i = 0; i < iterations; i++) {
            HashSet<Point> newVisited = new HashSet<>();
            for (Point pos : visited) {
                for (int[] dir : directions) {
                    int newX = pos.x + dir[0];
                    int newY = pos.y + dir[1];
                    if (newX >= 0 && newX < map.length && newY >= 0 && newY < map[0].length && map[newX][newY] == '.') {
                        Point newPos = new Point(newX, newY);
                        newVisited.add(newPos);
                    }
                }
            }
            visited = newVisited;
        }

        for (Point pos : visited) {
            map[pos.x][pos.y] = 'O';
        }
        return visited.size();
    }
}
