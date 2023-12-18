import java.util.*;

public class day_17 {
    public static int[][] map;

    public static void main(String[] args) {
        String[] input = ReadFile.getFile("day_17.txt");
        map = new int[input.length][input[0].length()];
        for (int i = 0; i < input.length; i++) {
            char[] line = input[i].toCharArray();
            for (int j = 0; j < line.length; j++) {
                map[i][j] = Character.getNumericValue(line[j]);
            }
        }

        char[][] charMap = new char[map.length][map[0].length];
        for (int i = 0; i < charMap.length; i++) {
            for (int j = 0; j < charMap[0].length; j++) {
                charMap[i][j] = (char) (map[i][j]+ '0');
            }
        }

        Node start = new UltraCrusibleNode(new int[] {0, 0}, new int[] {1, 0}, 0, 0, null);

        int[] end = new int[] {map.length-1, map[0].length-1};
        Node last = aStar(start, end);
        System.out.println("test");
        System.out.println(last.getTotalHeatloss()-map[0][0]);

        while (last != null) {
            if (Arrays.equals(last.dir, new int[] {1, 0})) {
                charMap[last.pos[0]][last.pos[1]] = 'v';
            } else if (Arrays.equals(last.dir, new int[] {-1, 0})) {
                charMap[last.pos[0]][last.pos[1]] = '^';
            } else if (Arrays.equals(last.dir, new int[] {0, 1})) {
                charMap[last.pos[0]][last.pos[1]] = '>';
            } else if (Arrays.equals(last.dir, new int[] {0, -1})) {
                charMap[last.pos[0]][last.pos[1]] = '<';
            }
            last = last.getParent();
        }

        for (char[] line : charMap) {
            System.out.println(line);
        }
    }


// tried to implement dijkstra but it was too slow so tried aStar but it was a difrent problem so both work.
    public static Node aStar(Node start, int[] end) {
        PriorityQueue<Node> pq = new PriorityQueue<>(Comparator.comparingInt(node -> node.getTotalHeatlossPlusHeuristic(end)));
        HashSet<String> visited = new HashSet<>();

        pq.offer(start);

        while (!pq.isEmpty()) {
            Node current = pq.poll();

            if ((Arrays.equals(current.getPos(), end)) && (current.straight >= 3)) {
                return current;
            }

            String currentPos = current.toString();
            if (visited.contains(currentPos)) {
                continue;
            }

            visited.add(currentPos);

            Node[] neighbors = current.neighbors();
            for (Node neighbor : neighbors) {
                if (!visited.contains(neighbor.toString())) {
                    pq.offer(neighbor);
                }
            }
        }
        return null;
    }


    public static Node dijkstra(Node start, int[] end) {
        PriorityQueue<Node> pq = new PriorityQueue<>(Comparator.comparingInt(Node::getTotalHeatloss));
        HashSet<String> visited = new HashSet<>();

        pq.offer(start);

        while (!pq.isEmpty()) {
            Node current = pq.poll();

            if ((Arrays.equals(current.getPos(), end)) && (current.straight < 3)) {
                return current;
            }

            String currentPos = current.toString();
            if (visited.contains(currentPos)) {
                continue;
            }

            visited.add(currentPos.toString());

            Node[] neighbors = current.neighbors();
            for (Node neighbor : neighbors) {
                if (!visited.contains(neighbor.toString())) {
                    pq.offer(neighbor);
                }
            }
        }
        return null;
    }

    public static class UltraCrusibleNode extends Node{
        public UltraCrusibleNode(int[] pos, int[] dir, int straight, int heatloss, Node parent) {
            super(pos, dir, straight, heatloss, parent);
        }

        @Override
        public Node[] neighbors() {
            ArrayList<int[]> excludedDirections = new ArrayList<>();
            excludedDirections.add(new int[] {-super.dir[0], -super.dir[1]});

            ArrayList<UltraCrusibleNode> neighbors = new ArrayList<>();
            for (int[] direction : this.directions) {
                boolean contains = false;
                for (int[] arr : excludedDirections) {
                    if (Arrays.equals(arr, direction)) {
                        contains = true;
                        break;
                    }
                }

                if (!contains) {
                    int straight = super.straight;
                    int[] newPos = super.move(super.pos, direction);
                    if (newPos == null) {
                        continue;
                    }

                    if (straight >= 10 || (straight < 3 & !Arrays.equals(direction, super.dir))) {
                        continue;
                    }

                    if (Arrays.equals(direction, super.dir)) {
                        straight++;
                    } else {
                        straight = 0;
                    }
                    neighbors.add(new UltraCrusibleNode(newPos, direction, straight, super.heatloss, this));
                }
            }
            return neighbors.toArray(new UltraCrusibleNode[neighbors.size()]);
        }
    }

    public static class Node {
        public int[][] directions = new int[][] {
            {1, 0}, {-1, 0}, {0, 1}, {0, -1}
        };
        private int[] pos;
        private int[] dir;
        private int straight;
        private int heatloss;
        private Node parent;

        public Node(int[] pos, int[] dir, int straight, int heatloss, Node parent) {
            this.pos = pos;
            this.dir = dir;
            this.straight = straight;
            this.heatloss = heatloss + map[pos[0]][pos[1]];
            this.parent = parent;
        }

        public Node getParent() {
            return this.parent;
        }

        public int[] getPos() {
            return this.pos;
        }

        public int[] getDir() {
            return this.dir;
        }

        public int getTotalHeatloss() {
            // Calculate the total heat loss from the start node to this node
            return heatloss;
        }

        public int getTotalHeatlossPlusHeuristic(int[] end) {
            // Calculate the total heat loss from the start node to this node
            int totalHeatloss = heatloss;

            // Heuristic estimate: Manhattan distance to the end node
            int heuristic = Math.abs(end[0] - pos[0]) + Math.abs(end[1] - pos[1]);

            return totalHeatloss + heuristic;
        }

        public Node[] neighbors() {
            ArrayList<int[]> excludedDirections = new ArrayList<>();
            excludedDirections.add(new int[] {-this.dir[0], -this.dir[1]});
            if (this.straight == 3) {
                excludedDirections.add(this.dir);
            }

            ArrayList<Node> neighbors = new ArrayList<>();
            for (int[] direction : this.directions) {
                boolean contains = false;
                for (int[] arr : excludedDirections) {
                    if (Arrays.equals(arr, direction)) {
                        contains = true;
                        break;
                    }
                }

                if (!contains) {
                    int straight = this.straight;
                    int[] newPos = this.move(this.pos, direction);
                    if (newPos == null) {
                        continue;
                    }

                    if (Arrays.equals(direction, this.dir)) {
                        straight++;
                    } else {
                        straight = 0;
                    }
                    if (straight >= 3) {
                        continue;
                    }

                    neighbors.add(new Node(newPos, direction, straight, this.heatloss, this));
                }
            }
            return neighbors.toArray(new Node[neighbors.size()]);
        }

        private int[] move(int[] pos, int[] dir) {
            int[] newPos = new int[] {pos[0] + dir[0], pos[1] + dir[1]};
            if (map[0].length <= newPos[1] || newPos[1] < 0 || map.length <= newPos[0] || newPos[0] < 0) {
                return null;
            }
            return newPos;
        }

        @Override
        public String toString() {
            return this.pos[0] + "," + this.pos[1] + this.dir[0] + "," + this.dir[1] + this.straight;
        }
    }
}
