package gui;

import java.awt.Point;
import java.util.ArrayList;

@SuppressWarnings("serial")
public class ApfelWorker {

	double radius;
	int maxiter;
	double[][] values;
	public Object runningLock = new Object();
	public boolean isRunning = false;
	private ArrayList<CalcObserver> observers = new ArrayList<CalcObserver>();

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
		ApfelWorkerThread t = new ApfelWorkerThread(xmin, xmax, ymin, ymax,
				screenPoints);
		t.start();
	}

	class ApfelWorkerThread extends Thread {
		double xmin, xmax, ymin, ymax;
		int[][] screenPoints;

		public ApfelWorkerThread(double xmin, double xmax, double ymin,
				double ymax, int[][] screenPoints) {
			super();
			this.xmin = xmin;
			this.xmax = xmax;
			this.ymin = ymin;
			this.ymax = ymax;
			this.screenPoints = screenPoints;
		}

		public void run() {
			isRunning = true;
			synchronized (runningLock) {
				for (int x = 0; x < screenPoints.length; x++) {
					new Thread(new LineWorker(x, xmin, xmax, ymin, ymax,
							screenPoints)).start();
					notifyObservers();

				}
			}
			isRunning = false;
		}
	}

	class LineWorker implements Runnable {
		int x;
		double xmin, xmax, ymin, ymax;
		int[][] screenPoints;

		public LineWorker(int x, double xmin, double xmax, double ymin,
				double ymax, int[][] screenPoints) {
			super();
			this.x = x;

			this.xmin = xmin;
			this.xmax = xmax;
			this.ymin = ymin;
			this.ymax = ymax;
			this.screenPoints = screenPoints;
		}

		@Override
		public void run() {
			for (int y = 0; y < screenPoints[0].length; y++) {
				double[] ccoords = calculateComplexFromScreenCoordinates(x, y,
						xmin, ymin, xmax, ymax, screenPoints.length,
						screenPoints[0].length);

				screenPoints[x][y] = iterateFunction(ccoords[0], ccoords[1]);
			}
		}
	}

	// ObserverPattern einbauen, damit das Panel neumalt, solange gerechnet wird
	public void registerCalcObserver(CalcObserver c) {
		observers.add(c);

	}

	public void notifyObservers() {
		for (CalcObserver c : observers) {
			c.notifyCalc();

		}

	}
}