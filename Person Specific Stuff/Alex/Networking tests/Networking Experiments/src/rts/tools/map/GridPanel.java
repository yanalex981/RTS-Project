package rts.tools.map;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.io.File;
import java.io.IOException;

import javax.swing.JPanel;

import rts.networking.Map;

public class GridPanel extends JPanel implements MouseListener, MouseMotionListener {
	private static final long serialVersionUID = -2739997346291402006L;
	
	private int w = 5;
	private int h = 5;

	private byte drawMode = Map.MINERAL;
	
	private byte[][] grid = new byte[h][w];
	
	public GridPanel() {
		setBackground(Color.WHITE);
		setPreferredSize(new Dimension(w * 8, h * 8));
		addMouseListener(this);
		addMouseMotionListener(this);
		
		for (int i = 0; i < h; ++i) {
			for (int j = 0; j < w; ++j) {
				grid[i][j] = 0;
			}
		}
	}
	
	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		g.setColor(Color.BLACK);
		
		for (int i = 0; i <= h; ++i) {
			g.drawLine(0, getHeight() / h * i, getWidth() / w * w, getHeight() / h * i);
		}
		
		for (int i = 0; i <= w; ++i) {
			g.drawLine(getWidth() / w * i, 0, getWidth() / w * i, getHeight() / h * h);
		}
		
		for (int i = 0; i < h; ++i) {
			for (int j = 0; j < w; ++j) {
				switch (grid[i][j]) {
				
				case Map.BLOCKED:
					g.setColor(Color.BLACK);
					g.fillRect(i * (getWidth() / w), j * (getHeight() / h), getWidth() / w, getHeight() / h);
					break;
				case Map.MINERAL:
					g.setColor(Color.BLUE);
					g.fillRect(i * (getWidth() / w), j * (getHeight() / h), getWidth() / w, getHeight() / h);
					break;
				case Map.SPAWN:
					g.setColor(Color.GREEN);
					g.fillRect(i * (getWidth() / w), j * (getHeight() / h), getWidth() / w, getHeight() / h);
				}
			}
		}
	}
	
	public int getH() {
		return h;
	}
	
	public void setH(int h) {
		this.h = h;
	}
	
	public int getW() {
		return w;
	}
	
	public void setW(int w) {
		this.w = w;
	}
	
	public void setDrawMode(byte mode) {
		if (mode >= 0 && mode < 4) {
			drawMode = mode;
		}
	}
	
	public void setGrid(byte[][] grid) {
		this.grid = grid;
		w = grid[0].length;
		h = grid.length;
		repaint();
	}
	
	public void newGrid(int w, int h) {
		this.w = w;
		this.h = h;
		
		byte[][] grid = new byte[h][w];
		
		for (int i = 0; i < grid.length; ++i) {
			for (int j = 0; j < grid[i].length; ++j) {
				grid[i][j] = 0;
			}
		}
		
		this.grid = grid;
		repaint();
	}
	
	public void openGridFile(File file) throws IOException {
		setGrid(Map.readMap(file));
	}
	
	public void saveGridFile(File file) throws IOException {
		int spawns = 0;
		
		for (int i = 0; i < grid.length; ++i) {
			for (int j = 0; j < grid[i].length; ++j) {
				if (grid[i][j] == Map.SPAWN) {
					++spawns;
				}
			}
		}
		
		System.out.println(spawns);
		
		Map.writeMap(grid, file, spawns);
	}

	@Override
	public void mouseClicked(MouseEvent e) {}

	@Override
	public void mousePressed(MouseEvent e) {}

	@Override
	public void mouseReleased(MouseEvent e) {
		int x = e.getX() / (getWidth() / w);
		int y = e.getY() / (getHeight() / h);
		
		if (x < w && x >= 0 && y < h && y >= 0) {
			grid[x][y] = drawMode;
			
		}
		
		repaint();
	}

	@Override
	public void mouseEntered(MouseEvent e) {}

	@Override
	public void mouseExited(MouseEvent e) {}

	@Override
	public void mouseDragged(MouseEvent e) {
		mouseReleased(e);
	}

	@Override
	public void mouseMoved(MouseEvent e) {}
}
