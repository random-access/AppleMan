package gui;

import java.awt.*;

import javax.swing.JPanel;

@SuppressWarnings("serial")
public class PaintPanel extends JPanel {
	int [][] points;
	
	
	int width = 800;	
	int height = 600;

	
	public PaintPanel() {
		ApfelWorker worker = new ApfelWorker(10.0, 500);
		points = new int[width][height];
		worker.calculateArray(points);
		
		setPreferredSize(new Dimension(width, height));
		setBackground(Color.WHITE);
		setOpaque(true);
		
	}
	
	protected void paintComponent(Graphics g) {
		System.out.println("in paintComponent");
		super.paintComponent(g);		
		Graphics2D g2d = (Graphics2D)g;
		drawPoints(g2d);

	}
	
	private void drawPoints(Graphics2D g2d) {
		for (int x = 0; x < points.length; x++) {
			for(int y = 0; y < points[0].length; y++) {
				g2d.setColor(new Color((points[x][y]*10) % 125, 0, 0));
				g2d.drawLine(x, y, x, y);
			}
		}
	}
	
	
}
