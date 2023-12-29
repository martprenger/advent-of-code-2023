import java.awt.*;
import java.util.*;

public class day_23 {
    static int[][] dirs = {{0, 1}, {1, 0}, {0, -1}, {-1, 0}};
    static char[] slides = {'v', '>', '^', '<'};

    public static void main(String[] args) {
        String[] input = ReadFile.getFile("day_23.txt");
        char[][] map = Arrays.stream(input).map(String::toCharArray).toArray(char[][]::new);

        Point start = new Point(1, 0);
        int[] startdir = {0, 0};
        System.out.println("part 1: " + getLongestPathP1(new Node(start, startdir), map).getSteps());
        Node part2 = getLongestPathP2(new Point(1, 0), map);
        System.out.println("part 2: " + part2.getSteps());

    }

    public static Node getLongestPathP1(Node prev, char[][] map) {
        boolean[][] visited = new boolean[map.length][map[0].length];
        ArrayList<Node> paths = new ArrayList<>();

        for (int i = 0; i < dirs.length; i++) {
            int[] dir = dirs[i];
            if (prev.getDir()[0] == -dir[0] && prev.getDir()[1] == -dir[1]) continue; // Don't go back (180 degrees
            Point nextPos = new Point(prev.getPos().x + dir[0], prev.getPos().y + dir[1]);
            if (nextPos.y < 0 || nextPos.y >= map.length || nextPos.x < 0 || nextPos.x >= map[0].length) continue;
            if (map[nextPos.y][nextPos.x] == '.') {
                Node next = new Node(nextPos, dir, prev);
                paths.add(next);
            } else if (map[nextPos.y][nextPos.x] == slides[i]) {
                Node next = new Node(nextPos, dir, prev);
                paths.add(next);
            }
        }

        if (paths.isEmpty()) {
            return prev;
        }

        Node longest = new Node(new Point(0, 0), new int[]{0, 0});
        for (Node path : paths) {
            Node temp = getLongestPathP1(path, map);
            if (temp.getSteps() > longest.getSteps()) longest = temp;
        }

        return longest;
    }

    public static Node getLongestPathP2(Point start, char[][] map) {
        Stack<Node> stack = new Stack<>();
        Node initialNode = new Node(start, new int[]{0, 0});
        stack.push(initialNode);
        Point end = new Point(map[0].length - 2, map.length - 1);

        Node longestPath = initialNode;

        while (!stack.isEmpty()) {
            Node current = stack.pop();
            if (current.dir[0] == -2 && current.dir[1] == -2) {
                map[current.getPos().y][current.getPos().x] = '.';
                continue;
            }

            if (current.getPos().equals(end)) {
                if  (current.getSteps() >= longestPath.getSteps()){
                    System.out.println("found path with " + current.getSteps() + " steps");
                    longestPath = current;
                }
                continue;
            }

            map[current.getPos().y][current.getPos().x] = 'O';
            if (current.getPrev() != null) {
                stack.push(new Node(current.getPos(), new int[] {-2, -2}, current.getPrev()));
            } else {
                stack.push(new Node(current.getPos(), new int[] {-2, -2}));
            }

            for (int[] dir : dirs) {
                Point nextPos = new Point(current.getPos().x + dir[0], current.getPos().y + dir[1]);
                if (nextPos.y < 0 || nextPos.y >= map.length || nextPos.x < 0 || nextPos.x >= map[0].length) continue;
                if (map[nextPos.y][nextPos.x] != '#' && map[nextPos.y][nextPos.x] != 'O') {
                        Node next = new Node(nextPos, dir, current);
                        stack.push(next);

                }
            }
        }

        return longestPath;
    }

    public static class Node {
        Node prev;
        public Point pos;
        public int[] dir;
        public int steps;
        public Set<Point> visited;

        public Node(Point pos, int[] dir, Node prev) {
            this.pos = pos;
            this.dir = dir;
            this.prev = prev;
            this.steps = prev.getSteps() + 1;
        }

        public Node(Point pos, int[] dir) {
            this.pos = pos;
            this.dir = dir;
            this.prev = null;
            this.steps = 0;
        }

        public int getSteps() {
            return this.steps;
        }

        public Node getPrev() {
            return this.prev;
        }

        public Point getPos() {
            return this.pos;
        }

        public int[] getDir() {
            return this.dir;
        }
    }
}
