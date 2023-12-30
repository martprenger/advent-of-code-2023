import java.awt.*;
import java.math.BigDecimal;
import java.math.MathContext;
import java.util.ArrayList;

public class day_24 {
    private static final MathContext CONTEXT = MathContext.DECIMAL128;
    public static void main(String[] args) {
        String[] input = ReadFile.getFile("day_24.txt");
        ArrayList<HailStone> hail = new ArrayList<>();
        for (String line : input) {
            String[] split = line.split(" @ ");
            String[] pos = split[0].split(", ");
            String[] vel = split[1].split(", ");
            hail.add(new HailStone(
                    Double.parseDouble(pos[0]),
                    Double.parseDouble(pos[1]),
                    Double.parseDouble(pos[2]),
                    Double.parseDouble(vel[0]),
                    Double.parseDouble(vel[1]),
                    Double.parseDouble(vel[2])
            ));
        }

        double min = 200000000000000d;
        double max = 400000000000000d;

        int count = 0;
        for (int i = 0; i < hail.size(); i++) {
            HailStone first = hail.get(i);
            for (int j = i + 1; j < hail.size(); j++) {
                HailStone second = hail.get(j);
                double x = (second.b() - first.b()) / (first.a() - second.a());
                double y = first.a() * x + first.b();
                boolean inRange = x >= min && x <= max && y >= min && y <= max;
                boolean possibleFirst = Math.signum(x - first.x) == Math.signum(first.vx);
                boolean possibleSecond = Math.signum(x - second.x) == Math.signum(second.vx);
                if (inRange && possibleFirst && possibleSecond) {
//                    System.out.println("Collision at " + x + ", " + y);
                    count++;
                }

            }
        }
        System.out.println(count);

        HailStone one = hail.get(0);
        BigDecimal[][] matrix = new BigDecimal[4][5];
        for (int i = 0; i < 4; i++) {
            HailStone two = hail.get(i + 1);
            matrix[i][0] = bd(one.y).subtract(bd(two.y));
            matrix[i][1] = bd(two.x).subtract(bd(one.x));
            matrix[i][2] = bd(two.vy).subtract(bd(one.vy));
            matrix[i][3] = bd(one.vx).subtract(bd(two.vx));
            matrix[i][4] = bd(one.y).multiply(bd(one.vx))
                    .subtract(bd(one.x).multiply(bd(one.vy)))
                    .subtract(bd(two.y).multiply(bd(two.vx)))
                    .add(bd(two.x).multiply(bd(two.vy)));
        }

        gauss(matrix);

        BigDecimal rsy = matrix[3][4].divide(matrix[3][3], CONTEXT);
        BigDecimal rsx = (matrix[2][4].subtract(matrix[2][3].multiply(rsy))).divide(matrix[2][2], CONTEXT);
        BigDecimal rvy = (matrix[1][4].subtract(matrix[1][3].multiply(rsy)).subtract(matrix[1][2].multiply(rsx))).divide(matrix[1][1], CONTEXT);
        BigDecimal rvx = (matrix[0][4].subtract(matrix[0][3].multiply(rsy)).subtract(matrix[0][2].multiply(rsx)).subtract(matrix[0][1].multiply(rvy)).divide(matrix[0][0], CONTEXT));

        BigDecimal t1 = (bd(one.x).subtract(rsx)).divide(rvx.subtract(bd(one.vx)), CONTEXT);
        BigDecimal z1 = bd(one.z).add(t1.multiply(bd(one.vz)));
        HailStone two = hail.get(1);
        BigDecimal t2 = (bd(two.x).subtract(rsx)).divide(rvx.subtract(bd(two.vx)), CONTEXT);
        BigDecimal z2 = bd(two.z).add(t2.multiply(bd(two.vz)));
        BigDecimal rvz = (z2.subtract(z1)).divide(t2.subtract(t1), CONTEXT);
        BigDecimal rsz = z1.subtract(rvz.multiply(t1));

        System.out.println(rsx.add(rsy).add(rsz).longValue());
    }

    static BigDecimal bd(double in) {
        return BigDecimal.valueOf(in);
    }

    static void gauss(BigDecimal[][] matrix) {
        // See https://en.wikipedia.org/wiki/Gaussian_elimination
        int pivotRow = 0;
        int pivotCol = 0;
        int nRows = matrix.length;
        int nCols = matrix[0].length;
        while (pivotRow < nRows && pivotCol < nCols) {
            BigDecimal max = BigDecimal.ZERO;
            int idxMax = -1;
            for (int i = pivotRow; i < nRows; i++) {
                BigDecimal cand = matrix[i][pivotCol].abs();
                if (cand.compareTo(max) > 0) {
                    max = cand;
                    idxMax = i;
                }
            }
            if (matrix[idxMax][pivotCol].equals(BigDecimal.ZERO)) {
                // nothing to pivot in this column
                pivotCol++;
            } else {
                // swap rows idxMax and pivotRow
                BigDecimal[] tmp = matrix[pivotRow];
                matrix[pivotRow] = matrix[idxMax];
                matrix[idxMax] = tmp;
                for (int i = pivotRow + 1; i < nRows; i++) {
                    // for all lower rows, subtract so that matrix[i][pivotCol] becomes 0
                    BigDecimal factor = matrix[i][pivotCol].divide(matrix[pivotRow][pivotCol], CONTEXT);
                    matrix[i][pivotCol] = BigDecimal.ZERO;
                    for (int j = pivotCol + 1; j < nCols; j++) {
                        // only need to go right, to the left it's all zeros anyway
                        matrix[i][j] = matrix[i][j].subtract(factor.multiply(matrix[pivotRow][j]));
                    }
                }
            }
            pivotCol++;
            pivotRow++;
        }
    }

    public static class HailStone {
        double x, y , z;
        double vx, vy, vz;

        public HailStone(double x, double y, double z, double vx, double vy, double vz) {
            this.x = x;
            this.y = y;
            this.z = z;
            this.vx = vx;
            this.vy = vy;
            this.vz = vz;
        }

        @Override
        public String toString() {
            return "HailStone{" +
                    "x=" + x +
                    ", y=" + y +
                    ", z=" + z +
                    ", vx=" + vx +
                    ", vy=" + vy +
                    ", vz=" + vz +
                    '}';
        }

        private double b() {
            return y - x * (vy / vx);
        }

        private double a() {
            return vy / vx;
        }

    }
}
