import java.awt.EventQueue;

import javax.swing.JFrame;
import java.awt.BorderLayout;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.JButton;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.Point;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import javax.swing.JCheckBox;

public class Test implements Runnable {

  private JFrame frmPathfindingTest;
	private JTextField txtUnitRadius;
	private JTextField txtWidth;
	private JTextField txtHeight;
	private JButton btnCreate;
	private MapDisplay mapPane;

	private int radius;
	private Map map;

	Point startNode;
	Point endNode;

	boolean enableFont;
	private JCheckBox chckbxEnableFont;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Test window = new Test();
					window.frmPathfindingTest.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public Test() {
		initialize();
		map = new Map(0, 0);
		enableFont = true;

		Thread th = new Thread(this);
		th.start();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frmPathfindingTest = new JFrame();
		frmPathfindingTest.setTitle("Pathfinding Test");
		frmPathfindingTest.setBounds(100, 100, 450, 300);
		frmPathfindingTest.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frmPathfindingTest.getContentPane().setLayout(new BorderLayout(0, 0));

		mapPane = new MapDisplay();
		frmPathfindingTest.getContentPane().add(mapPane, BorderLayout.CENTER);

		JPanel optionPane = new JPanel();
		frmPathfindingTest.getContentPane().add(optionPane, BorderLayout.EAST);
		optionPane.setLayout(new GridLayout(0, 1, 0, 0));

		txtUnitRadius = new JTextField();
		optionPane.add(txtUnitRadius);
		txtUnitRadius.setColumns(10);

		txtWidth = new JTextField();
		optionPane.add(txtWidth);
		txtWidth.setColumns(10);

		txtHeight = new JTextField();
		optionPane.add(txtHeight);
		txtHeight.setColumns(10);

		btnCreate = new JButton("Create");
		btnCreate.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				int tempRadius = 0, width, height;

				try {
					tempRadius = Integer.parseInt(txtUnitRadius.getText());
					width = Integer.parseInt(txtWidth.getText());
					height = Integer.parseInt(txtHeight.getText());
				} catch (Exception ex) {
					System.out.println("error");
					return;
				}

				radius = tempRadius;
				map = new Map(width, height);
			}
		});

		chckbxEnableFont = new JCheckBox("Enable Font");
		chckbxEnableFont.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				enableFont = chckbxEnableFont.isSelected();
			}
		});
		chckbxEnableFont.setSelected(true);

		optionPane.add(chckbxEnableFont);
		optionPane.add(btnCreate);
	}

	public void pathfind() {
		if (startNode == null || endNode == null || map.getWidth() == 0
				|| map.getHeight() == 0)
			return;

		Unit u = new Unit(startNode.x + 0.5, startNode.y + 0.5, 1, radius, 0);
		u.setDestination(map.findPath(startNode, endNode,
				u.getCollisionRadius()));
		u.nextPosition();
		map.addUnit(u);
	}

	public void printValues(Graphics g, int boxWidth, int boxHeight) {
		for (int y = 0; y < map.getHeight(); y++) {
			for (int x = 0; x < map.getWidth(); x++) {
				g.drawString("" + map.getNode(x, y).getMaxUnitRadius(), x
						* boxWidth, y * boxHeight + g.getFont().getSize());
				g.drawString("" + map.getNode(x, y).getF(), x * boxWidth, y
						* boxHeight + g.getFont().getSize() * 3);
				g.drawString("" + map.getNode(x, y).getG(), x * boxWidth, y
						* boxHeight + g.getFont().getSize() * 4);
				g.drawString("" + map.getNode(x, y).getH(), x * boxWidth, y
						* boxHeight + g.getFont().getSize() * 5);
			}
		}
	}

	public void run() {
		while (true) {
			long startTime = System.currentTimeMillis();

			map.update();
			mapPane.repaint();

//			System.out.println(map.getUnits().size());
//			System.out.println(100 - (System.currentTimeMillis() - startTime));
			
			try {
				Thread.sleep(100 - (System.currentTimeMillis() - startTime));
			} catch (IllegalArgumentException ex) {
				try {
					Thread.sleep((System.currentTimeMillis() - startTime) - 100);
				} catch (InterruptedException exe) {
				}
			} catch (InterruptedException ex) {
			}
		}
	}

	@SuppressWarnings("serial")
	private class MapDisplay extends JPanel implements MouseListener {
		public MapDisplay() {
			super();
			addMouseListener(this);
		}

		public void paintComponent(Graphics g) {
			super.paintComponent(g);

			if (map == null || map.getHeight() <= 0 || map.getWidth() <= 0)
				return;

			int boxWidth = (int) this.getWidth() / map.getWidth();
			int boxHeight = (int) this.getHeight() / map.getHeight();

			if (startNode != null) {
				// draw the starting square
				g.setColor(Color.green);
				g.fillRect(startNode.x * boxWidth, startNode.y * boxHeight,
						boxWidth, boxHeight);
			}

			g.setColor(Color.red);
			if (endNode != null) {
				// draw the end square
				g.fillRect(endNode.x * boxWidth, endNode.y * boxHeight,
						boxWidth, boxHeight);
			}

			g.setColor(Color.black);
			// draw the grid
			for (int y = 0; y < map.getHeight(); y++)
				for (int x = 0; x < map.getWidth(); x++)
					g.drawRect(x * boxWidth, y * boxHeight, boxWidth, boxHeight);

			// fill occupied areas of the grid
			for (int y = 0; y < map.getHeight(); y++)
				for (int x = 0; x < map.getWidth(); x++)
					if (map.getMaxUnitRadius(x, y) == 0)
						g.fillRect(x * boxWidth, y * boxHeight, boxWidth,
								boxHeight);

			if (enableFont)
				printValues(g, boxWidth, boxHeight);

			for(Unit u : map.getUnits())
				paintUnit(u, boxWidth, boxHeight, g);
		}

		public void paintUnit(Unit u, int boxWidth, int boxHeight, Graphics g) {
			g.setColor(Color.red);
			ArrayList<Node> path = u.getPath();
			if (path != null && path.size() > 0) {
				// draw the path
				for (int i = 0; i < path.size() - 1; i++)
					g.drawLine(
							(int) (path.get(i).getX() * boxWidth + (boxWidth / 2)),
							(int) (path.get(i).getY() * boxHeight + (boxHeight / 2)),
							(int) (path.get(i + 1).getX() * boxWidth + (boxWidth / 2)),
							(int) (path.get(i + 1).getY() * boxHeight + (boxHeight / 2)));
			}

			g.setColor(Color.blue);
			g.fillOval(
					(int) (boxWidth * (u.getX() - u.getCollisionRadius() / 10.0)),
					(int) (boxHeight * (u.getY() - u.getCollisionRadius() / 10.0)),
					(int) ((u.getCollisionRadius() / 5.0) * boxWidth),
					(int) ((u.getCollisionRadius() / 5.0) * boxHeight));
		}

		public void mousePressed(MouseEvent e) {
			if (map.getWidth() <= 0 || map.getHeight() <= 0)
				return;

			int x = e.getX() / (int) (this.getWidth() / map.getWidth());
			int y = e.getY() / (int) (this.getHeight() / map.getHeight());

			switch (e.getButton()) {
			case MouseEvent.BUTTON1:
				startNode = new Point(x, y);
				pathfind();
				break;
			case MouseEvent.BUTTON2:
				map.setValue(x, y, !map.getValue(x, y));
				pathfind();
				break;
			case MouseEvent.BUTTON3:
				endNode = new Point(x, y);
				pathfind();
				break;
			}
		}

		public void mouseClicked(MouseEvent arg0) {
		}

		public void mouseEntered(MouseEvent arg0) {
		}

		public void mouseReleased(MouseEvent arg0) {
		}

		public void mouseExited(MouseEvent arg0) {
		}
	}
}
