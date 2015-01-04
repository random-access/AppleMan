package gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;

import javax.swing.JPanel;

@SuppressWarnings("serial")
public class PaintPanel extends JPanel {
	int[][] points;

	Point mouseCoordinates = new Point(0, 0);
	Point dragCoordinates = new Point(0, 0);
	int width = 800;
	int height = 600;
	ApfelWorker worker;
	protected boolean isDragged = false;
	protected double xmax = 2;
	protected double xmin = -2;
	protected double ymax = 2;
	protected double ymin = -2;

	public PaintPanel() {
		worker = new ApfelWorker(10.0, 500);
		points = new int[width][height];
		worker.calculateArray(points, xmax, ymax, xmin, xmin);

		setPreferredSize(new Dimension(width, height));
		setBackground(Color.WHITE);
		setOpaque(true);
		// MouseListener hinzufügen
		addMouseMotionListener(new MouseMotionAdapter() {
			public void mouseMoved(MouseEvent e) {
				System.out.println("Mouse Moved...");
				mouseCoordinates.x = e.getX();
				mouseCoordinates.y = e.getY();
				repaint();

			}

			public void mouseDragged(MouseEvent e) {
				System.out.println("Mouse dragging...");
				dragCoordinates.x = e.getX();
				dragCoordinates.y = e.getY();
				isDragged = true;
				repaint();

			}

		});
		addMouseListener(new MouseAdapter() {

			public void mouseReleased(MouseEvent e) {
				System.out.println("Mouse released...");
				if (isDragged) {
					isDragged = false;
					// calculate coordinates to be displayed

					int sxmax = Math.max(mouseCoordinates.x, dragCoordinates.x);
					int sxmin = Math.min(mouseCoordinates.x, dragCoordinates.x);
					int symax = Math.min(mouseCoordinates.y, dragCoordinates.y);
					int symin = Math.max(mouseCoordinates.y, dragCoordinates.y);

					// getting the complex coordinates by helper method
					double[] cmin = ApfelWorker
							.calculateComplexFromScreenCoordinates(sxmin,
									symin, xmin, ymin, xmax, ymax, width,
									height);
					double[] cmax = ApfelWorker
							.calculateComplexFromScreenCoordinates(sxmax,
									symax, xmin, ymin, xmax, ymax, width,
									height);
					worker.calculateArray(points, cmax[0], cmax[1], cmin[0],
							cmin[1]);
					// set these coords as new member coords xmin,xmax...
					xmax = cmax[0];
					ymax = cmax[1];
					xmin = cmin[0];
					ymin = cmin[1];
				}
				System.out.println(e.getButton());
				if (e.getButton() == 3) {
					// reset window
					xmax = 2;
					xmin = -2;
					ymin = -2;
					ymax = 2;
					worker.calculateArray(points, 2, 2, -2, -2);

				}

			}

		});

	}

	protected void paintComponent(Graphics g) {
		System.out.println("in paintComponent");
		super.paintComponent(g);
		Graphics2D g2d = (Graphics2D) g;
		drawPoints(g2d);

	}

	private void drawPoints(Graphics2D g2d) {
		// draw calculated Array
		for (int x = 0; x < points.length; x++) {
			for (int y = 0; y < points[0].length; y++) {
				g2d.setColor(new Color((points[x][y] * 10) % 125, 0, 0));
				g2d.drawLine(x, y, x, y);
			}
		}
		g2d.setColor(new Color(255, 255, 255));

		// draw cursor (coordinates)
		g2d.drawLine(mouseCoordinates.x, 0, mouseCoordinates.x,
				points[0].length);
		g2d.drawLine(0, mouseCoordinates.y, points.length, mouseCoordinates.y);
		// draw box if dragged
		if (isDragged) {

			g2d.drawLine(dragCoordinates.x, 0, dragCoordinates.x,
					points[0].length);
			g2d.drawLine(0, dragCoordinates.y, points.length, dragCoordinates.y);
			g2d.setColor(new Color(79, 79, 79));
			g2d.drawRect(dragCoordinates.x, dragCoordinates.y,
					mouseCoordinates.x - dragCoordinates.x, mouseCoordinates.y
							- dragCoordinates.y);
		}
	}
}
