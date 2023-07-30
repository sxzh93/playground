import edu.princeton.cs.algs4.Picture;

public class SeamCarver {
    private static final int COLOR_MASK = 0x000000ff;
    private int[][] colors;

    // Create a seam carver object based on the given picture.
    public SeamCarver(Picture picture) {
        if (picture == null) {
            throw new IllegalArgumentException("Input must not be null.");
        }
        Picture myPicture = new Picture(picture);
        colors = new int[myPicture.width()][myPicture.height()];
        // Initialize colors array from picture.
        for (int i = 0; i < myPicture.width(); i++) {
            for (int j = 0; j < myPicture.height(); j++) {
                colors[i][j] = myPicture.getRGB(i, j);
            }
        }
    }

    // Current picture.
    public Picture picture() {
        Picture myPicture = new Picture(width(), height());
        for (int i = 0; i < width(); i++) {
            for (int j = 0; j < height(); j++) {
                myPicture.setRGB(i, j, colors[i][j]);
            }
        }
        return new Picture(myPicture);
    }

    // Width of current picture.
    public int width() {
        return colors.length;
    }

    // Height of current picture.
    public int height() {
        return colors[0].length;
    }

    // Energy of pixel at column x and row y.
    public double energy(int x, int y) {
        if (!inbound(x, y)) {
            throw new IllegalArgumentException("Pixel index is out of boundary.");
        }
        if (x == 0 || y == 0 || x == this.width() - 1 || y == this.height() - 1) {
            return 1000;
        }
        int colorLeft = colors[x - 1][y];
        int colorRight = colors[x + 1][y];
        int colorUp = colors[x][y - 1];
        int colorDown = colors[x][y + 1];
        return Math.sqrt(
                getSquareOfColorGradient(colorLeft, colorRight) + getSquareOfColorGradient(colorUp,
                                                                                           colorDown));
    }

    // Sequence of indices for horizontal seam.
    public int[] findHorizontalSeam() {
        transpose();
        int[] result = findVerticalSeam();
        transpose();
        return result;
    }

    // Sequence of indices for vertical seam.
    public int[] findVerticalSeam() {
        double[][] energies = new double[width()][height()];
        double[][] distTo = new double[width()][height()];
        int[][] edgeTo = new int[width()][height()];
        for (int i = 0; i < width(); i++) {
            for (int j = 0; j < height(); j++) {
                energies[i][j] = energy(i, j);
            }
        }
        for (int i = 0; i < width(); i++) {
            for (int j = 0; j < height(); j++) {
                distTo[i][j] = Double.POSITIVE_INFINITY;
            }
        }
        for (int i = 0; i < width(); i++) {
            for (int j = 0; j < height(); j++) {
                edgeTo[i][j] = -1;
            }
        }

        // Special topological sort as the sequence can only go from top to down, i.e. dynamic
        // programming.
        for (int i = 0; i < width(); i++) {
            distTo[i][0] = energies[i][0];
        }
        for (int j = 0; j < height() - 1; j++) {
            for (int i = 0; i < width(); i++) {
                // Relax next three edges.
                int nextJ = j + 1;
                for (int k = -1; k <= 1; k++) {
                    int nextI = i + k;
                    if (!inbound(nextI, nextJ)) {
                        continue;
                    }
                    if (distTo[nextI][nextJ] > distTo[i][j] + energies[nextI][nextJ]) {
                        distTo[nextI][nextJ] = distTo[i][j] + energies[nextI][nextJ];
                        edgeTo[nextI][nextJ] = i;
                    }
                }
            }
        }

        int[] result = new int[height()];
        double minEnery = Double.POSITIVE_INFINITY;
        int cur = -1;
        for (int i = 0; i < width(); i++) {
            if (distTo[i][height() - 1] < minEnery) {
                minEnery = distTo[i][height() - 1];
                cur = i;
            }
        }
        result[height() - 1] = cur;
        for (int j = height() - 1; j > 0; j--) {
            result[j - 1] = edgeTo[result[j]][j];
        }

        return result;
    }

    // Remove horizontal seam from current picture.
    public void removeHorizontalSeam(int[] seam) {
        if (seam == null) {
            throw new IllegalArgumentException("Input must not be null.");
        }
        if (seam.length != width()) {
            throw new IllegalArgumentException(
                    "Horizontal seam must have length equals to width of the picture.");
        }
        for (int i = 0; i < width(); i++) {
            if (seam[i] < 0 || seam[i] >= height()) {
                throw new IllegalArgumentException("Invalid seam index.");
            }
            if (i > 0 && Math.abs(seam[i] - seam[i - 1]) > 1) {
                throw new IllegalArgumentException(
                        "Successive entries in seam[] must differ by -1, 0, or +1.");
            }
        }
        int[][] newColors = new int[width()][height() - 1];
        for (int i = 0; i < width(); i++) {
            for (int j = 0; j < seam[i]; j++) {
                newColors[i][j] = colors[i][j];
            }
            for (int j = seam[i] + 1; j < height(); j++) {
                newColors[i][j - 1] = colors[i][j];
            }
        }
        colors = newColors;
    }

    // Remove vertical seam from current picture.
    public void removeVerticalSeam(int[] seam) {
        if (seam == null) {
            throw new IllegalArgumentException("Input must not be null.");
        }
        if (seam.length != height()) {
            throw new IllegalArgumentException(
                    "Vertical seam must have length equals to height of the picture.");
        }
        for (int i = 0; i < height(); i++) {
            if (seam[i] < 0 || seam[i] >= width()) {
                throw new IllegalArgumentException("Invalid seam index.");
            }
            if (i > 0 && Math.abs(seam[i] - seam[i - 1]) > 1) {
                throw new IllegalArgumentException(
                        "Successive entries in seam[] must differ by -1, 0, or +1.");
            }
        }
        int[][] newColors = new int[width() - 1][height()];
        for (int j = 0; j < height(); j++) {
            for (int i = 0; i < seam[j]; i++) {
                newColors[i][j] = colors[i][j];
            }
            for (int i = seam[j] + 1; i < width(); i++) {
                newColors[i - 1][j] = colors[i][j];
            }
        }
        colors = newColors;
    }

    private double getSquareOfColorGradient(int firstColor, int secondColor) {
        return Math.pow(getRed(secondColor) - getRed(firstColor), 2) + Math.pow(
                getGreen(secondColor) - getGreen(firstColor), 2) + Math.pow(
                getBlue(secondColor) - getBlue(firstColor), 2);
    }

    private int getRed(int color) {
        return (color >> 16) & COLOR_MASK;
    }

    private int getGreen(int color) {
        return (color >> 8) & COLOR_MASK;
    }

    private int getBlue(int color) {
        return color & COLOR_MASK;
    }

    private void transpose() {
        int[][] newColors = new int[height()][width()];
        for (int i = 0; i < width(); i++) {
            for (int j = 0; j < height(); j++) {
                newColors[j][i] = colors[i][j];
            }
        }
        colors = newColors;
    }

    private boolean inbound(int row, int col) {
        return row >= 0 && row < width() && col >= 0 && col < height();
    }
}
