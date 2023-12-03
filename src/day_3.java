import javax.swing.plaf.IconUIResource;

public class day_3 {
    public static void main(String[] args) {
        String[] lines = ReadFile.getFile("day_3_star_1.txt");
        char[][] engine = new char[lines.length][lines[0].length()];
        int l = 0;
        for (String engineLine : lines) {
            engine[l] = engineLine.toCharArray();
            l++;
        }

        System.out.println(getGearRatio(engine));
    }

    public static int getEngineNumber(char[][] engine) {
        int rt = 0;
        int partNumber = 0;
        boolean adjecentSymbol = false;

        for (int i = 0; i < engine.length; i++) {
            for (int j = 0; j < engine[i].length; j++) {
                if (Character.isDigit(engine[i][j])) {
                    partNumber = partNumber * 10 + Character.getNumericValue(engine[i][j]);

                    if (!adjecentSymbol) {
                        int[] dX = {-1, 0, 1};
                        int[] dY = {-1, 0, 1};
                        for (int x : dX) {
                            for (int y : dY) {
                                int ix = i + x;
                                int jy = j + y;
                                if (!((ix < 0 || ix >= engine.length) || (jy < 0 || jy >= engine[j].length))) {
                                    char ch = engine[ix][jy];
                                    if (!Character.isDigit(ch) && (ch != '.')) {
                                        adjecentSymbol = true;
                                    }
                                }
                            }
                        }
                    }

                } else if (partNumber > 0) {
                    if (adjecentSymbol) rt += partNumber;
                    partNumber = 0;
                    adjecentSymbol = false;
                }
            }
        }

        return rt;
    }

    public static int getGearRatio(char[][] engine) {
        int rt = 0;

        for (int i = 0; i < engine.length; i++) {
            for (int j = 0; j < engine[i].length; j++) {
                char ch = engine[i][j];
                if (ch == '*') {
                    if (getNumberOfAdjecentNumbers(engine, i, j) == 2) {
                        rt += getSumOfAdjecentNumbers(engine, i, j);
                    }
                }
            }
        }
        return rt;
    }

    public static int getNumberOfAdjecentNumbers(char[][] engine, int row, int col) {
        int rt = 0;
        for (int i = row - 1; i <= row + 1; i++) {
            if (i >= 0 && i < engine.length) {
                if (Character.isDigit(engine[i][col])) {
                    rt++;
                    continue;
                }
                if (col-1 >= 0){
                    if (Character.isDigit(engine[i][col-1])) rt++;
                }
                if (col+1 < engine[i].length){
                    if (Character.isDigit(engine[i][col+1])) rt++;
                }
            }
        }
        return rt;
    }

    public static int getSumOfAdjecentNumbers(char[][] engine, int row, int col) {
        int rt = 1;
        for (int i = row - 1; i <= row + 1; i++) {
            if (i >= 0 && i < engine.length) {
                if (Character.isDigit(engine[i][col])) {
                    rt *= getPartNumber(engine, i, col);
                    continue;
                }
                if (col-1 >= 0){
                    if (Character.isDigit(engine[i][col-1])) rt *= getPartNumber(engine, i, col-1);
                }
                if (col+1 < engine[i].length){
                    if (Character.isDigit(engine[i][col+1])) rt *= getPartNumber(engine, i, col+1);
                }
            }
        }
        return rt;
    }

    public static int getPartNumber(char[][] engine, int row, int col){
        int rt = 0;
        int currentCol = col;

        // Go left to find the full number
        while (currentCol >= 0 && Character.isDigit(engine[row][currentCol])) {
            currentCol--;
        } // Reset the column index to go right from the original position
        currentCol++;

        // Go right to find the full number and multiply/add it
        while (currentCol < engine[row].length && Character.isDigit(engine[row][currentCol])) {
            rt = rt * 10 + Character.getNumericValue(engine[row][currentCol]);
            currentCol++;
        }

        return rt;
    }
}
