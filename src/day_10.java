import java.util.*;

public class day_10 {

    public static int[] changeDirection(int[] direction, char symbol) {
        int[] newDirection = new int[2];

        if (symbol == '|') {
            if (direction[0] == -1 && direction[1] == 0) {
                newDirection[0] = -1;
                newDirection[1] = 0;
            } else if (direction[0] == 1 && direction[1] == 0) {
                newDirection[0] = 1;
                newDirection[1] = 0;
            }
        } else if (symbol == '-') {
            if (direction[0] == 0 && direction[1] == -1) {
                newDirection[0] = 0;
                newDirection[1] = -1;
            } else if (direction[0] == 0 && direction[1] == 1) {
                newDirection[0] = 0;
                newDirection[1] = 1;
            }
        } else if (symbol == 'L') {
            if (direction[0] == 1 && direction[1] == 0) {
                newDirection[0] = 0;
                newDirection[1] = 1;
            } else if (direction[0] == 0 && direction[1] == -1) {
                newDirection[0] = -1;
                newDirection[1] = 0;
            }
        } else if (symbol == 'J') {
            if (direction[0] == 1 && direction[1] == 0) {
                newDirection[0] = 0;
                newDirection[1] = -1;
            } else if (direction[0] == 0 && direction[1] == 1) {
                newDirection[0] = -1;
                newDirection[1] = 0;
            }
        } else if (symbol == '7') {
            if (direction[0] == -1 && direction[1] == 0) {
                newDirection[0] = 0;
                newDirection[1] = -1;
            } else if (direction[0] == 0 && direction[1] == 1) {
                newDirection[0] = 1;
                newDirection[1] = 0;
            }
        } else if (symbol == 'F') {
            if (direction[0] == -1 && direction[1] == 0) {
                newDirection[0] = 0;
                newDirection[1] = 1;
            } else if (direction[0] == 0 && direction[1] == -1) {
                newDirection[0] = 1;
                newDirection[1] = 0;
            }
        }
        // Add more conditions for other symbols here...

        return newDirection;
    }

    public static boolean isValidDirection(int[] direction, char symbol) {
        if (direction[0] == 1 && direction[1] == 0) {
            return symbol == '|' || symbol == 'L' || symbol == 'J';
        } else if (direction[0] == 0 && direction[1] == 1) {
            return symbol == '-' || symbol == '7' || symbol == 'J';
        } else if (direction[0] == -1 && direction[1] == 0) {
            return symbol == '|' || symbol == 'F' || symbol == '7';
        } else if (direction[0] == 0 && direction[1] == -1) {
            return symbol == '-' || symbol == 'F' || symbol == 'L';
        }
        return false;
    }

    static int[][] directions = {{-1, 0}, {1, 0}, {0, -1}, {0, 1}}; // Up, Down, Left, Right

    public static int findFarthestPoint(char[][] map, int[] start) {
        int[] currentDirection = {0, 0};

        for (int[] i : directions) {
            if (isValidDirection(i, map[i[0] + start[0]][i[1] + start[1]])) {
                currentDirection = i;
                break;
            }
        }

        int[] currentLocation = start;

        int counter = 0;

        do {
            currentLocation[0] += currentDirection[0];
            currentLocation[1] += currentDirection[1];
            currentDirection = changeDirection(currentDirection, map[currentLocation[0]][currentLocation[1]]);

            counter++;
        } while ((map[start[0]][start[1]] != 'S') && (!Arrays.equals(currentDirection, new int[]{0, 0})));
        return counter/2;
    }

    public static boolean isLocationVisited(Set<int[]> visitedLocations, int row, int col) {
        for (int[] location : visitedLocations) {
            if (location[0] == row && location[1] == col) {
                return true;
            }
        }
        return false;
    }

    public static char getSymbol(char[][] map, int[] start, Set<int[]> visited) {
        int row = start[0];
        int col = start[1];
        boolean visitedAbove;
        boolean visitedBelow;
        boolean visitedLeft;
        boolean visitedRight;

        if (row >= 0) {
            visitedAbove = isLocationVisited(visited, row - 1, col);
        } else {
            visitedAbove = false;
        }

        if (row < map.length) {
            visitedBelow = isLocationVisited(visited, row + 1, col);
        } else {
            visitedBelow = false;
        }

        if (col >= 0) {
            visitedLeft = isLocationVisited(visited, row, col - 1);
        } else {
            visitedLeft = false;
        }

        if (col < map[0].length) {
            visitedRight = isLocationVisited(visited, row, col + 1);
        } else {
            visitedRight = false;
        }


        if (visitedAbove && visitedBelow) {
            return '|';
        } else if (visitedLeft && visitedRight) {
            return '-';
        } else if (visitedAbove && visitedRight) {
            return 'L';
        } else if (visitedLeft && visitedAbove) {
            return 'J';
        } else if (visitedLeft && visitedBelow) {
            return '7';
        } else if (visitedRight && visitedBelow) {
            return 'F';
        }

        // Return a default symbol if no specific conditions are met
        return '.';
    }

    public static int findArea(char[][] map, int[] start) {
        int[] currentDirection = {0, 0};

        for (int[] i : directions) {
            if ((i[0] + start[0] >= 0) && (i[0] + start[0] < map.length) && (i[1] + start[1] >= 0) && (i[1] + start[1] < map[0].length)) {
                if (isValidDirection(i, map[i[0] + start[0]][i[1] + start[1]])) {
                    currentDirection = i;
                    break;
                }
            }
        }
        System.out.println(currentDirection[0] + " " + currentDirection[1]);

        int[] currentLocation = start;

        int counter = 0;
        Set<int[]> visitedLocations = new HashSet<>();

        do {
            currentLocation[0] += currentDirection[0];
            currentLocation[1] += currentDirection[1];
            visitedLocations.add(Arrays.copyOf(currentLocation, currentLocation.length));
            currentDirection = changeDirection(currentDirection, map[currentLocation[0]][currentLocation[1]]);
            counter++;
        } while (!Arrays.equals(currentDirection, new int[]{0, 0})&&(map[currentLocation[0]][currentLocation[1]] != 'S'));


        //map[start[0]][start[1]] = getSymbol(map, start, visitedLocations);
        // my get symbol is wrong i should look at the posible replacement but i just hardcoded it.
        map[start[0]][start[1]] = '-';


        int area = 0;
        for (int row = 0; row < map.length; row++) {
            boolean inside = false;
            boolean fromDown = false;
            boolean fromUp = false;
            for (int col = 0; col < map[row].length; col++) {
                if (isLocationVisited(visitedLocations, row, col)) {
                    if (map[row][col] == '|') inside = !inside;
                    else if (fromDown) {
                        if (map[row][col] == 'J') {
                            fromDown = false;
                            inside = !inside;
                        } else if (map[row][col] == '7') fromDown = false;
                    } else if (fromUp) {
                        if (map[row][col] == 'J') fromUp = false;
                        else if (map[row][col] == '7') {
                            fromUp = false;
                            inside = !inside;
                        }
                    } else if (map[row][col] == 'L') fromUp = true;
                    else if (map[row][col] == 'F') fromDown = true;
                } else if (inside) {
                    map[row][col] = 'I';
                    area++;
                } else {
                    map[row][col] = 'O';
                }
            }
        }

        for (char[] line : map) {
            for (char c : line) {
                System.out.print(c);
            }
            System.out.println();
        }

        System.out.println("area: " + area);

        return area;
    }




    public static void main(String[] args) {
        String[] input = ReadFile.getFile("day_10.txt");
        char[][] map = new char[input.length][input[0].length()];
        int[] start = new int[2];

        for (int i = 0; i < input.length; i++) {
            for (int j = 0; j < input[0].length(); j++) {
                char c = input[i].charAt(j);
                if (c == 'S') start = new int[]{i, j};
                map[i][j] = c;
            }
        }

        System.out.println(findArea(map, start));



    }
}
