package gui;

import java.awt.Point;

public class ApfelWorker {

	double radius;
	int maxiter;
	double[][] values;

	public ApfelWorker(double radius, int maxiter) {
		this.radius = radius;
		this.maxiter = maxiter;
	}

	private double[] calculateSquare(double x, double y) {
		return new double[] { x * x - y * y, 2 * x * y };
	}

	private double[] calculatePoint(double x, double y, double xc, double yc) {
		double[] sq = calculateSquare(x, y);
		return new double[] { sq[0] + xc, sq[1] + yc };
	}

	private double calculateDistance(double xstart, double ystart, double xend,
			double yend) {
		return Math.sqrt(Math.pow(xstart - xend, 2)
				+ Math.pow(ystart - yend, 2));
	}

	private int iterateFunction(double x, double y) {
		double[] start = new double[] { x, y };
		double[] p = new double[] { x, y };

		int iter = 0;
		while (iter < maxiter
				&& radius >= calculateDistance(start[0], start[1], p[0], p[1])) {
			p = calculatePoint(p[0], p[1], start[0], start[1]);
			iter++;
		}

		return iter;
	}

	public static double[] calculateComplexFromScreenCoordinates(int sx,
			int sy, double xmin, double ymin, double xmax, double ymax,
			double swidth, int sheight) {
		return new double[] { (double) sx * (xmax - xmin) / swidth + (xmin),
				(double) (sy * (ymin - ymax) / sheight + (ymax)) };
	}

	public static Point calculateScreenCoordinatesFromComplex(double cx,
			double cy, double xmin, double ymin, double xmax, double ymax,
			int swidth, int sheight) {
		return new Point((int) Math.round((swidth / (xmax - xmin))
				* (cx - xmin)), (int) Math.round((sheight / (ymin - ymax))
				* (cy - xmax)));
	}

	public void calculateArray(int[][] screenPoints, double xmax, double ymax,
			double xmin, double ymin) {
		for (int x = 0; x < screenPoints.length; x++) {
			for (int y = 0; y < screenPoints[0].length; y++) {
				double[] ccoords = calculateComplexFromScreenCoordinates(x, y,
						xmin, ymin, xmax, ymax, screenPoints.length,
						screenPoints[0].length);

				screenPoints[x][y] = iterateFunction(ccoords[0], ccoords[1]);
			}

		}
	}
}