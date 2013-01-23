package rts.tools.map;

import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import javax.swing.filechooser.FileNameExtensionFilter;

import rts.networking.Map;

public class GridMaker extends JFrame {
	private static final long serialVersionUID = 807206923751068976L;
	
	JFileChooser fileChooser = new JFileChooser();
	
	JFrame newMapFrame;
	JSpinner w;
	JSpinner h;
	JButton create;
	
	JPanel dim = new JPanel(new FlowLayout());
	JLabel lblDim = new JLabel("Dimensions: w, h");
	
	JFrame paintFrame;
	JButton spawn;
	JButton mineral;
	JButton blocked;
	JButton unblocked;
	
	JMenuBar mainBar;
		JMenu file;
			JMenuItem newMap;
			JMenuItem open;
			JMenuItem save;
			JMenuItem quit;
		JMenu view;
			JMenuItem gridType;
	
	GridPanel gridPanel;
	
	public GridMaker() {
		initComponents();
		initNewMapWindow();
		initPaintWindow();
		initMenu();
		
		fileChooser.setAcceptAllFileFilterUsed(false);
		fileChooser.addChoosableFileFilter(new FileNameExtensionFilter("Map Grid File (.map)", "map"));
		
		add(gridPanel);
		
		setTitle("Map Grid Editor");
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		pack();
		setVisible(true);
	}
	
	private void initComponents() {
		newMapFrame = new JFrame("New Map");
		
		w = new JSpinner();
		h = new JSpinner();
		create = new JButton("Create New Map");
		
		paintFrame = new JFrame();
		
		spawn = new JButton("Spawn");
		mineral = new JButton("Resource");
		blocked = new JButton("Blocked");
		unblocked = new JButton("Unblocked");
		
		mainBar = new JMenuBar();
		file = new JMenu("File");
			newMap = new JMenuItem("New Map");
			open = new JMenuItem("Open Map");
			save = new JMenuItem("Save");
			quit = new JMenuItem("Quit");
		view = new JMenu("View");
			gridType = new JMenuItem("Grid Types");
	
		gridPanel = new GridPanel();
	}
	
	private void initNewMapWindow() {
		newMapFrame.setLayout(new BoxLayout(newMapFrame.getContentPane(), BoxLayout.Y_AXIS));
		
		w.setModel(new SpinnerNumberModel(0,0,Integer.MAX_VALUE,1));
		h.setModel(new SpinnerNumberModel(0,0,Integer.MAX_VALUE,1));
		
		dim.add(lblDim);
		dim.add(w);
		dim.add(h);
		
		newMapFrame.add(dim);
		newMapFrame.add(create);
		
		create.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				gridPanel.newGrid((Integer)w.getValue(), (Integer)h.getValue());
				pack();
			}
		});
		
		newMapFrame.setDefaultCloseOperation(HIDE_ON_CLOSE);
		newMapFrame.pack();
		newMapFrame.setResizable(false);
	}
	
	private void initPaintWindow() {
		paintFrame.setLayout(new BoxLayout(paintFrame.getContentPane(), BoxLayout.Y_AXIS));
		
		paintFrame.add(spawn);
		paintFrame.add(mineral);
		paintFrame.add(blocked);
		paintFrame.add(unblocked);
		
		spawn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				gridPanel.setDrawMode(Map.SPAWN);
			}
		});
		
		mineral.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				gridPanel.setDrawMode(Map.MINERAL);
			}
		});
		
		blocked.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				gridPanel.setDrawMode(Map.BLOCKED);
			}
		});
		
		unblocked.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				gridPanel.setDrawMode(Map.UNBLOCKED);
			}
		});
		
		paintFrame.setDefaultCloseOperation(HIDE_ON_CLOSE);
		paintFrame.pack();
		paintFrame.setResizable(false);
	}
	
	private void initMenu() {
		setJMenuBar(mainBar);
			mainBar.add(file);
				file.add(newMap);
				file.add(open);
				file.add(save);
				file.add(quit);
			mainBar.add(view);
				view.add(gridType);
				
		newMap.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				newMapFrame.setVisible(true);
			}
		});
		
		open.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (fileChooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
					try {
						if (!Map.validateMapFile(fileChooser.getSelectedFile(), false)) {
							JOptionPane.showMessageDialog(null, "Map file is bad", "Bad map file", JOptionPane.ERROR_MESSAGE);
						}
						else {
							gridPanel.openGridFile(fileChooser.getSelectedFile());
						}
					}
					catch (IOException e1) {
						e1.printStackTrace();
					}
				}
			}
		});
		
		save.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (fileChooser.showSaveDialog(null) == JFileChooser.APPROVE_OPTION) {
					try {
						gridPanel.saveGridFile(fileChooser.getSelectedFile());
					}
					catch (IOException e1) {
						e1.printStackTrace();
					}
				}
			}
		});
		
		quit.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				System.exit(0);
			}
		});
		
		gridType.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				paintFrame.setVisible(true);
			}
		});
	}
	
	public static void main(String[] args) {
		new GridMaker();
	}
}
