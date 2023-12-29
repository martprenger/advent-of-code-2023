import java.lang.reflect.Array;
import java.util.*;

public class day_22 {
    public static void main(String[] args) {
        String[] input = ReadFile.getFile("day_22.txt");
        int[] maxValues = new int[]{0, 0, 0};

        Brick[] bricks = Arrays.stream(input)
                .map(line -> {
                    String[] split = line.split("~");
                    int[] start = Arrays.stream(split[0].split(",")).mapToInt(Integer::parseInt).toArray();
                    int[] end = Arrays.stream(split[1].split(",")).mapToInt(Integer::parseInt).toArray();
                    maxValues[0] = Math.max(maxValues[0], Math.max(start[0], end[0]));
                    maxValues[1] = Math.max(maxValues[1], Math.max(start[1], end[1]));
                    maxValues[2] = Math.max(maxValues[2], Math.max(start[2], end[2]));

                    return new Brick(start, end);
                })
                .toArray(Brick[]::new);

        Arrays.sort(bricks, Comparator.comparingInt(a -> a.lowestZ));

        makeBricksFall(bricks);

        Arrays.sort(bricks, Comparator.comparingInt(a -> a.lowestZ));

        // Printing updated coordinates

        char[][][] grid = new char[maxValues[0] + 1][maxValues[1] + 1][maxValues[2] + 1];

        for (Brick brick : bricks) {
            for (int x = brick.start[0]; x <= brick.end[0]; x++) {
                for (int y = brick.start[1]; y <= brick.end[1]; y++) {
                    for (int z = brick.start[2]; z <= brick.end[2]; z++) {
                        grid[x][y][z] = '#';
                    }
                }
            }
        }

        // Printing grid z x
        System.out.println(" x     y ");
        System.out.println("123   123");

        for (int z = grid[0][0].length-1; z > 0; z--) {
            for (int x = 0; x < grid.length; x++) {
                char c = '.';
                for (int y = 0; y < grid[0].length; y++) {
                    if (grid[x][y][z] == '#') {
                        c = '#';
                        break;
                    }
                }
                System.out.print(c);
            }

            System.out.print("   ");

            for (int y = 0; y < grid.length; y++) {
                char c = '.';
                for (int x = 0; x < grid[0].length; x++) {
                    if (grid[x][y][z] == '#') {
                        c = '#';
                        break;
                    }
                }
                System.out.print(c);
            }
            System.out.println(" " + (z));
        }

        int count = 0;

        for(int i = 0; i < bricks.length; i++) {
            Brick brick = bricks[i];
            for (int j = i + 1; j < bricks.length; j++) {
                Brick other = bricks[j];
                if (brick.checkIfSuporting(other)) {
                    other.increaseSupportedBy();
                }
            }
        }


        for(int i = 0; i < bricks.length; i++) {
            Brick brick = bricks[i];
            boolean supporting = false;
            for (int j = i + 1; j < bricks.length; j++) {
                Brick other = bricks[j];
                if (brick.checkIfSuporting(other)) {
                    if (other.getSupportedBy() < 2) {
                        supporting = true;
                        break;
                    }
                }
            }
            if (!supporting) {
                count++;
            }
        }
        System.out.println("part 1:  " + count);

        //it is ineficient but works
        int part2 = 0;

        for (int i = 0; i < bricks.length; i++) {
            part2 += count(i, bricks);
        }
        System.out.println("part 2:  " + part2);
    }


    public static int count(int idx, Brick[] bricks) {
        int n = bricks.length;
        int[] indeg = new int[n];

        // Calculate in-degrees of bricks
        for (int j = 0; j < n; j++) {
            for (int i = 0; i < n; i++) {
                if (bricks[i].checkIfSuporting(bricks[j])) {
                    indeg[j]++;
                }
            }
        }

        Queue<Integer> queue = new ArrayDeque<>();
        int count = -1;

        // Find the starting brick and add it to the queue
        queue.offer(idx);

        while (!queue.isEmpty()) {
            count++;
            int x = queue.poll();

            // Decrement in-degrees of connected bricks
            for (int i = 0; i < n; i++) {
                if (bricks[x].checkIfSuporting(bricks[i])) {
                    indeg[i]--;
                    if (indeg[i] == 0) {
                        queue.offer(i);
                    }
                }
            }
        }

        return count;
    }


    public static void makeBricksFall(Brick[] bricks) {

        for (int i = 0; i < bricks.length; i++) {
            Brick brick = bricks[i];
            while (brick.getLowestZ() > 1) {
                boolean colliding = false;
                int[] start = brick.getStart().clone();
                start[2]--;
                int[] end = brick.getEnd().clone();
                end[2]--;
                Brick tempBrick = new Brick(start, end);
                for (int j = i - 1; j >= 0; j--) {
                    Brick other = bricks[j];
                    if (tempBrick.checkIfColliding(other)) {
                        colliding = true;
                        break;
                    }
                }
                if (!colliding) {
                    brick.lowerZ();
                } else {
                    break;
                }
            }
        }
    }


    public static class Brick {
        private int[] start;
        private int[] end;
        private int lowestZ;
        private int highestZ;
        private int supportedBy = 0;

        public Brick(int[] start, int[] end) {
            this.start = start;
            this.end = end;
            this.lowestZ = Math.min(start[2], end[2]);
            this.highestZ = Math.max(start[2], end[2]);
        }

        public boolean checkIfColliding(Brick other) {
            // Check for overlap along X-axis
            boolean xOverlap = this.start[0] <= other.end[0] && this.end[0] >= other.start[0];

            // Check for overlap along Y-axis
            boolean yOverlap = this.start[1] <= other.end[1] && this.end[1] >= other.start[1];

            // Check for overlap along Z-axis
            boolean zOverlap = this.start[2] <= other.end[2] && this.end[2] >= other.start[2];

            // If all three axes have overlap, bricks are colliding
            return xOverlap && yOverlap && zOverlap;
        }

        public boolean checkIfSuporting(Brick other) {
            // Check for overlap along X-axis
            boolean xOverlap = this.start[0] <= other.end[0] && this.end[0] >= other.start[0];

            // Check for overlap along Y-axis
            boolean yOverlap = this.start[1] <= other.end[1] && this.end[1] >= other.start[1];

            // Check for overlap along Z-axis
            boolean zOverlap = this.highestZ+1 == other.getLowestZ();

            // If all three axes have overlap, bricks are colliding
            return xOverlap && yOverlap && zOverlap;
        }

        public int getLowestZ() {
            return lowestZ;
        }
        public int getHighestZ() {
            return highestZ;
        }
        public int[] getStart() {
            return start;
        }
        public int[] getEnd() {
            return end;
        }
        public void increaseSupportedBy() {
            this.supportedBy++;
        }
        public int getSupportedBy() {
            return supportedBy;
        }

        public void lowerZ() {
            this.start[2]--;
            this.end[2]--;
            this.lowestZ--;
            this.highestZ--;
        }

        @Override
        public String toString() {
            return "start: " + Arrays.toString(start) + " end: " + Arrays.toString(end);
        }
    }
}
