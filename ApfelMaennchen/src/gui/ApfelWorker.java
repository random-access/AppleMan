package gui;

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

	public void calculateArray(int[][] screenPoints, double xmax, double ymax,
			double xmin, double ymin) {
		for (int x = 0; x < screenPoints.length; x++) {
			for (int y = 0; y < screenPoints[0].length; y++) {
				screenPoints[x][y] = iterateFunction((double) x * (xmax - xmin)
						/ screenPoints.length + (xmin), (double) (-1) * y
						* (ymax - ymin) / screenPoints[0].length + (ymin));
			}
		}
	}
}