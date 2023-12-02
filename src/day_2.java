public class day_2 {
    public static void main(String[] args) {
        System.out.println(makeGamsePosible("day_2_star_2.txt"));

    }

    public static int makeGamsePosible(String fileName) {
        int rt = 0;
        String[] lines = ReadFile.getFile(fileName);
        for (String i : lines) {
            rt += makeGamePosible(i);
        }
        return rt;
    }

    public static int getPosibleGames(String fileName) {
        int rt = 0;
        String[] lines = ReadFile.getFile(fileName);
        for (String i : lines) {
            rt += checkGame(i);
        }
        return rt;
    }

    public static int checkGame(String game) {
        int rt = 0;
        String[] gameLine = game.split(": ");
        String[] subGames = gameLine[1].split("; ");
        int red = 12;
        int green = 13;
        int blue = 14;
        boolean posible = true;

        for (String i : subGames) {
            if (!posible) break;

            String[] subGame = i.split(", ");

            for (String j : subGame) {
                if (!posible) break;
                String[] cube = j.split(" ");
                switch (cube[1]) {
                    case "red":
                        if (!(Integer.parseInt(cube[0]) <= red)) {
                            posible = false;
                        }
                        break;
                    case "green":
                        if (!(Integer.parseInt(cube[0]) <= green)) {
                            posible = false;
                        }
                        break;
                    case "blue":
                        if (!(Integer.parseInt(cube[0]) <= blue)) {
                            posible = false;
                        }
                        break;
                }
            }
        }
        if (posible) rt = Integer.parseInt(gameLine[0].split(" ")[1]);
        return rt;
    }

    public static int makeGamePosible(String game) {
        String[] gameLine = game.split(": ");
        String[] subGames = gameLine[1].split("; ");
        int red = 0;
        int green = 0;
        int blue = 0;
        boolean posible = true;

        for (String i : subGames) {
            if (!posible) break;

            String[] subGame = i.split(", ");

            for (String j : subGame) {
                if (!posible) break;
                String[] cube = j.split(" ");
                int temp = Integer.parseInt(cube[0]);
                switch (cube[1]) {
                    case "red":
                        if (!(temp <= red)) {
                            red = temp;
                        }
                        break;
                    case "green":
                        if (!(temp <= green)) {
                            green = temp;
                        }
                        break;
                    case "blue":
                        if (!(temp <= blue)) {
                            blue = temp;
                        }
                        break;
                }
            }
        }
        return red*green*blue;

    }
}
