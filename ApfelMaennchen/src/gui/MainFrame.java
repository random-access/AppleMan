package gui;

import java.awt.BorderLayout;

import javax.swing.*;

public class MainFrame extends JFrame {
	
	public MainFrame (JComponent c) {
		setTitle("JComponent Testklasse");
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException e) {
			e.printStackTrace();
		}
		
		add(c, BorderLayout.CENTER);
		pack();
		setLocationRelativeTo(null);
		setVisible(true);
	}
	
	public static void main(String[] args) {
		
		PaintPanel p = new PaintPanel();
		new MainFrame(p);
		
	}

}
