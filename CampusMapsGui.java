package hw8;

import java.awt.AWTException;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Container;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GraphicsEnvironment;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.Robot;
import java.awt.Shape;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;
import java.util.ArrayList;
import java.util.HashMap;

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.ScrollPaneConstants;
import javax.swing.JLabel;
import javax.swing.ToolTipManager;
import javax.swing.WindowConstants;
import javax.swing.border.Border;

import hw4.Edge;
import hw7.BldgData;
import hw7.PathData;
import hw7.CampusData;


/*
 * Java Swing application for visualizing campus maps
 * This is an attempt to program the V & C aspects of the MVC methodology
 * 
 */

	public class CampusMapsGui  {

	// Screen Width
	private static double W;	
	// Screen Height
	private static double H;	

	// Node Circle Diameter = C
	private static int C = 20;

	//Xoffset as a function of W for scaling to different screen sizes
	private static double XOffset;

	//Yoffset as a function of H for scaling to different screen sizes
	private static double YOffset;

	// min and max values of X and Y in the data (needed for scaling)
	private static double maxX;
	private static double maxY;
	private static double minX;
	private static double minY;

	// string from node & to node - used for calls to calculate distance
	private static String strFrom;
	private static String strTo;


	// some local object variables, and the various components on the screen
	private static CampusMapsGui cmg;
	private static JFrame frame;
	private static CampusData cd;
	private static HashMap<String, BldgData> b;
	private static HashMap<String, ArrayList<PathData>> p;
	private static LegendNodesEdges LNE;
	private static JComboBox<String> JCFrom;
	private static JComboBox<String> JCTo;
	private static JButton jbFindPath;
	private static JButton jbClear;
	private static JButton jbExit;
	private static JLabel jLTitle;
	private static JLabel jLSubTitle;
	private static JTextArea jTAResult;
	private static JLabel jcfl;
	private static JLabel jclt;
	private static JScrollPane scrollV;
	private static Robot rbt;
	private static HashMap<Shape, String> shapes; //store shapes for mouseover
	private static ButtonGroup bg;
	private static JRadioButton rb1;
	private static JRadioButton rb2;
	private static JRadioButton rb3;
	private static JRadioButton rb4;

	// timer pause duration for mouse movement in milli-secondds
	private static int pauseTimems = 50;

	// ticks per edge for simulated movement
	private static int ticks = 10;

	// attempt to create rainbow colors VIBGYOR (for drawing the path)
	private static Color VIOLET = new Color(238,130,238);
	private static Color INDIGO = new Color(75,0,130);
	private static Color[] rainbow = {VIOLET, INDIGO, Color.BLUE, Color.GREEN, Color.YELLOW, Color.ORANGE, Color.RED}; //, Color.MAGENTA, Color.CYAN, Color.PINK};
	private static int colorCount = 0;
	
	private CampusMapsGui() {

		// get our campus data
		LoadCampusData();
	}

	private static void LoadCampusData() {
		// load campus data
		cd = new CampusData("data/RPI_map_data_Nodes.csv", "data/RPI_map_data_Edges.csv");

		// get building and path data
		b = cd.getbData();
		p = cd.getpData();

		// get min/max X and min/max Y (need it for scaling)
		maxX = 0;
		maxY = 0;
		minX = Double.POSITIVE_INFINITY;
		minY = Double.POSITIVE_INFINITY;

		for (BldgData bd : b.values()) {
			double x = bd.getX();
			double y = bd.getY();
			if (x > maxX)
				maxX = x;
			if (y > maxY)
				maxY = y;

			if (x < minX)
				minX = x;
			if (y < minY)
				minY = y;
		}

		// looks like we can get away with half the border size  
		minX = minX/2;
		minY = minY/2;
	}

	// GUIMain objects and Constructor
	private void CreateGui() {

		// get our current Graphics Environment (screen size)
		GraphicsEnvironment env = GraphicsEnvironment.getLocalGraphicsEnvironment();
		Rectangle bounds = env.getMaximumWindowBounds();
		W = (double) bounds.width;
		H = (double) bounds.height;
		XOffset = W/200;
		YOffset = H/200;
		

		// create gui components
		CreateTitle();
		CreateSubTitle();
		CreateRadioButtons();
		CreateJCFrom();
		CreateJCTo();
		CreateJBFindPath();
		CreateJBClear();
		CreateJBExit();
		CreateJLabelResults();

		// add all components to the JPanel LNE
		LNE.add(jLTitle);
		LNE.add(jLSubTitle);
		LNE.add(rb1);
		LNE.add(rb2);
		LNE.add(rb3);
		LNE.add(rb4);
		LNE.add(jcfl);
		LNE.add(JCFrom);
		LNE.add(jclt);
		LNE.add(JCTo);
		LNE.add(jbFindPath);
		LNE.add(jbClear);
		LNE.add(jbExit);
		LNE.add(scrollV);

		strFrom = "";
		strTo = "";

		shapes = new HashMap<Shape, String>();

	}

	// create title
	private static void CreateTitle() {

		// write text box for path results.. .with vertical scrollbar
		jLTitle = new JLabel("");
		jLTitle.setText("<html><h3>RPI Campus Map (Buildings & Intersections)</h3>");

		double left = 2*XOffset;
		double top = 2*YOffset;
		jLTitle.setBounds((int) left, (int) top, jLTitle.getPreferredSize().width, jLTitle.getPreferredSize().height);
	}

	// create subtitle
	private static void CreateSubTitle() {

		// write text box for path results.. .with vertical scrollbar
		jLSubTitle = new JLabel("");
		jLSubTitle.setText("<html><h4>Find Paths with</h4>");

		double left = 2*XOffset;
		double top = jLTitle.getY() + jLTitle.getPreferredSize().height;

		jLSubTitle.setBounds((int) left, (int) top, jLSubTitle.getPreferredSize().width, jLSubTitle.getPreferredSize().height);
	}

	// create radio buttons
	private static void CreateRadioButtons() {

		// write text box for path results.. .with vertical scrollbar
		rb1 = new JRadioButton("Least Distance     ", true);
		rb2 = new JRadioButton("Least Hops         ", false);
		rb3 = new JRadioButton("Avoid Buildings    ", false);
		rb4 = new JRadioButton("Avoid Intersections", false);

		bg = new ButtonGroup();
		bg.add(rb1);
		bg.add(rb2);
		bg.add(rb3);
		bg.add(rb4);
		
		double left = 8*XOffset;
		double top = jLTitle.getPreferredSize().height + jLSubTitle.getPreferredSize().height;
		rb1.setBounds((int) left, (int) top, rb1.getPreferredSize().width, rb1.getPreferredSize().height);


		left = 8*XOffset + rb1.getPreferredSize().width;
		top = jLTitle.getPreferredSize().height + jLSubTitle.getPreferredSize().height;
		rb2.setBounds((int)left, (int)top, rb2.getPreferredSize().width, rb2.getPreferredSize().height);

		left = 8*XOffset;
		top = jLTitle.getPreferredSize().height + jLSubTitle.getPreferredSize().height + rb1.getPreferredSize().height;
		rb3.setBounds((int)left, (int)top,rb3.getPreferredSize().width, rb3.getPreferredSize().height);

		left = 8*XOffset+ rb1.getPreferredSize().width;
		top = jLTitle.getPreferredSize().height + jLSubTitle.getPreferredSize().height + rb1.getPreferredSize().height;
		rb4.setBounds((int)left, (int)top, rb4.getPreferredSize().width, rb4.getPreferredSize().height);

	}


	// create the "From" combobox
	private static void CreateJCFrom() {

		// write drop down box for From Buidling

		jcfl = new JLabel();
		jcfl.setText("From:");
		
		double left = 2*XOffset;
		double top = jLTitle.getPreferredSize().height + jLSubTitle.getPreferredSize().height + rb1.getPreferredSize().height + + rb3.getPreferredSize().height;
		jcfl.setBounds((int) left, (int)top, jcfl.getPreferredSize().width, jcfl.getPreferredSize().height);

		JCFrom = new JComboBox<String>();
		JCFrom.addItem("<Select a From Location>");
		for (String id : cd.listBldgNames()) {
			if (!id.contains("Intersection "))
				JCFrom.addItem(id);
		}

		left = 3*XOffset + jcfl.getPreferredSize().width;
		top = jLTitle.getPreferredSize().height + jLSubTitle.getPreferredSize().height + rb1.getPreferredSize().height + + rb3.getPreferredSize().height;
		JCFrom.setBounds((int)left, (int) top, JCFrom.getPreferredSize().width, JCFrom.getPreferredSize().height);
		JCFrom.setSelectedItem(-1);

		// Create an ActionListener for the JCFrom combobox
		JCFrom.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent event) {
				// Print the selected items and the action command.
				String command = event.getActionCommand();
				if ("comboBoxChanged".equals(command)) {
					strFrom = JCFrom.getSelectedItem().toString();
					if (strFrom.indexOf(",") != -1) {
						strFrom = strFrom.substring(0, strFrom.indexOf(","));
					}

				}
			}
		});
	}

	
	// create "To" combobox
	private static void CreateJCTo() {

		jclt = new JLabel();
		jclt.setText("To:");

		double left = 3*XOffset;
		double top = 2*YOffset + jLTitle.getPreferredSize().height + jLSubTitle.getPreferredSize().height + 
				rb1.getPreferredSize().height + + rb3.getPreferredSize().height +
				JCFrom.getPreferredSize().height;

		jclt.setBounds((int) left, (int) top, jcfl.getPreferredSize().width, jcfl.getPreferredSize().height);

		// write drop down box for To Buidling
		JCTo = new JComboBox<String>();
		// write drop down box for TO Buidling
		JCTo.addItem("<Select a To Location>");
		for (String id : cd.listBldgNames()) {
			if (!id.contains("Intersection "))
				JCTo.addItem(id);
		}

		left = 3*XOffset + jcfl.getPreferredSize().width;
		top = 2*YOffset + jLTitle.getPreferredSize().height + jLSubTitle.getPreferredSize().height + 
				rb1.getPreferredSize().height + + rb3.getPreferredSize().height +
				JCFrom.getPreferredSize().height;

		JCTo.setBounds((int) left, (int) top, JCFrom.getPreferredSize().width, JCFrom.getPreferredSize().height);
		JCTo.setSelectedItem(-1);

		// Create an ActionListener for the JCTo combobox
		JCTo.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent event) {
				// Print the selected items and the action command.
				String command = event.getActionCommand();
				if ("comboBoxChanged".equals(command)) {
					strTo = JCTo.getSelectedItem().toString();
					if (strTo.indexOf(",") != -1)
						strTo = strTo.substring(0, strTo.indexOf(","));
				}
			}
		});
	}

	// create button "Find Path"
	private static void CreateJBFindPath() {

		// write Button to click for findPath
		jbFindPath = new JButton("Find Path");
		jbFindPath.setMnemonic('F');

		double left = 3*XOffset + jcfl.getPreferredSize().width;
		double top = 4*YOffset + jLTitle.getPreferredSize().height + jLSubTitle.getPreferredSize().height + 
				rb1.getPreferredSize().height + + rb3.getPreferredSize().height +
				JCFrom.getPreferredSize().height+
				JCTo.getPreferredSize().height;
		

		jbFindPath.setBounds((int) left, (int) top, jbFindPath.getPreferredSize().width, jbFindPath.getPreferredSize().height);

		// Create an ActionListener for button jbFindPath
		jbFindPath.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent event) {
				// Print the selected items and the action command.
				String command = event.getActionCommand();
				if ("Find Path".equals(command)) {
					String jts = "";
					if (strFrom == null)
						jts = jts + "Please select a \"From\" location\n";
					else if (strFrom.length() == 0)
						jts = jts + "Please select a \"From\" location\n";
					else if (strFrom.equals("<Select a From Location>"))
						jts = jts + "Please select a \"From\" location\n";

					if (strTo == null)
						jts = jts + "Please select a \"To\" location\n";
					else if (strTo.length() == 0)
						jts = jts + "Please select a \"To\" location\n";
					else if (strTo.equals("<Select a To Location>"))
						jts = jts + "Please select a \"To\" location\n";

					if (jts.equals("")) {
						jTAResult.setText(jts);
						ArrayList<Edge<String, Double, Double>> AL = new ArrayList<Edge<String, Double, Double>>();
						String searchType = "";
						if (rb1.isSelected())
							searchType = "least_distance";
						else if (rb2.isSelected())
							searchType = "least_hops";
						else if (rb3.isSelected())
							searchType = "avoid_buildings";
						else if (rb4.isSelected())
							searchType = "avoid_intersections";

						AL = cd.findPathAL(strFrom, strTo, searchType);

						cmg.DrawShortestPath(AL);
					} else
						jTAResult.setText(jts);
				}
			}
		});
	}


	// create button "Clear"
	private static void CreateJBClear() {

		// write Button to clear paths
		jbClear = new JButton("Clear");
		jbClear.setMnemonic('C');
		double left = 5*XOffset + jcfl.getPreferredSize().width + jbFindPath.getPreferredSize().width; 
		double top = 4*YOffset + jLTitle.getPreferredSize().height + jLSubTitle.getPreferredSize().height + 
				rb1.getPreferredSize().height + + rb3.getPreferredSize().height +
				JCFrom.getPreferredSize().height+
				JCTo.getPreferredSize().height;
		
		jbClear.setBounds((int) left, (int) top, jbFindPath.getPreferredSize().width, jbFindPath.getPreferredSize().height);

		// Create an ActionListener for button jb
		jbClear.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent event) {
				// Print the selected items and the action command.
				String command = event.getActionCommand();
				if ("Clear".equals(command)) {

					JCFrom.setSelectedIndex(0);
					JCTo.setSelectedIndex(0);
					jTAResult.setText("Results Here");
					rb1.setSelected(true);

					LNE.repaint();
					strFrom = null;
					strTo = null;
					colorCount = 0;
				}
			}
		});
	}


	// create button "Exit"
	private static void CreateJBExit() {

		// write Button to exit window
		jbExit = new JButton("Exit");
		jbExit.setMnemonic('E');

		double left = 7*XOffset + jcfl.getPreferredSize().width + 
					jbFindPath.getPreferredSize().width +
					jbFindPath.getPreferredSize().width;
		
		double top = 4*YOffset + jLTitle.getPreferredSize().height + jLSubTitle.getPreferredSize().height + 
				rb1.getPreferredSize().height + + rb3.getPreferredSize().height +
				JCFrom.getPreferredSize().height+
				JCTo.getPreferredSize().height;
		
		jbExit.setBounds((int) left, (int) top, jbFindPath.getPreferredSize().width, jbFindPath.getPreferredSize().height);

		// Create an ActionListener for button jb
		jbExit.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent event) {
				// Print the selected items and the action command.
				String command = event.getActionCommand();
				if ("Exit".equals(command)) {
					System.exit(0);
				}
			}
		});
	}

	// Create text area to show results
	private static void CreateJLabelResults() {

		// write text box for path results.. .with vertical scrollbar
		jTAResult = new JTextArea("Results Here");

		double left = 5*XOffset + jcfl.getPreferredSize().width + 
				JCFrom.getPreferredSize().width;
	
		double top = 4*YOffset;
	
		jTAResult.setRows((int) XOffset );
		jTAResult.setColumns(8*(int) YOffset );
		jTAResult.setBounds((int) left, (int) top, jTAResult.getPreferredSize().width, jTAResult.getPreferredSize().height);
		jTAResult.setEditable(false);
		jTAResult.setForeground(Color.black);
		jTAResult.setBackground(new Color(238, 238, 238));
		jTAResult.setBorder(null);

		scrollV = new JScrollPane(jTAResult);
		scrollV.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
		scrollV.setBounds((int) left, (int) top, 
				jTAResult.getPreferredSize().width+ (int) XOffset, 
				jTAResult.getPreferredSize().height+(int) YOffset);
		Border border = BorderFactory.createLineBorder(Color.BLUE, 1);
		scrollV.setBorder(border);
	}


	// sub class for adding graphics elements from nodes/edges legend
	private static class LegendNodesEdges extends JPanel  implements MouseListener {

		private static final long serialVersionUID = 1L;
		public LegendNodesEdges() {
			addMouseListener(this);
		}

		@Override
		public void paintComponent(Graphics g) {

			super.paintComponent(g);

			Graphics2D g2d = (Graphics2D) g;
			g2d.setRenderingHint( RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);	// for prettier drawing

			// Write Legend for building
			// red circles for buildings, blue circles for intersections
			g2d.setColor(new Color(196, 0, 0)); // set color to dark red

			double left = 5*XOffset + jcfl.getPreferredSize().width;
			double top = YOffset + jLTitle.getPreferredSize().height + jLSubTitle.getPreferredSize().height + 
					rb1.getPreferredSize().height + + rb3.getPreferredSize().height +
					JCFrom.getPreferredSize().height +
					JCTo.getPreferredSize().height+
					2*jbFindPath.getPreferredSize().height;

			Ellipse2D.Double lcircle = new Ellipse2D.Double(left, top, C, C);
			g2d.fill(lcircle);
			g2d.setColor(Color.black); // set color to black
			g2d.draw(lcircle);

			left = 8*XOffset + jcfl.getPreferredSize().width;
			top = 2*YOffset + jLTitle.getPreferredSize().height + jLSubTitle.getPreferredSize().height + 
					rb1.getPreferredSize().height + + rb3.getPreferredSize().height +
					JCFrom.getPreferredSize().height+
					JCTo.getPreferredSize().height+
					2*jbFindPath.getPreferredSize().height + (int) (2*YOffset);

			g2d.setColor(Color.black); // set color to black
			g2d.drawString(" = Buildings", (int) left, (int) top);

			// Write Legend for intersection
			g2d.setColor(new Color(128, 128, 255)); // set color to blue
			// red circles for buildings, blue small circles for intersections
			
			left = 20*XOffset + jcfl.getPreferredSize().width;
			top = 2*YOffset + jLTitle.getPreferredSize().height + jLSubTitle.getPreferredSize().height + 
					rb1.getPreferredSize().height + + rb3.getPreferredSize().height +
					JCFrom.getPreferredSize().height +
					JCTo.getPreferredSize().height+
					2*jbFindPath.getPreferredSize().height;

			lcircle = new Ellipse2D.Double(left, top, C/2, C/2);
			g2d.fill(lcircle);
			g2d.setColor(Color.black); // set color to black
			g2d.draw(lcircle);

			left = 22*XOffset + jcfl.getPreferredSize().width;
			top = 4*YOffset + jLTitle.getPreferredSize().height + jLSubTitle.getPreferredSize().height + 
					rb1.getPreferredSize().height + + rb3.getPreferredSize().height +
					JCFrom.getPreferredSize().height +
					JCTo.getPreferredSize().height+
					2*jbFindPath.getPreferredSize().height;

			g2d.drawString(" = Intersections", (int) left, (int) top);


			left = 5*XOffset + jcfl.getPreferredSize().width;
			top = 5*YOffset + 
					jLTitle.getPreferredSize().height + 
					jLSubTitle.getPreferredSize().height + 
					rb1.getPreferredSize().height + 
					rb3.getPreferredSize().height +
					JCFrom.getPreferredSize().height +
					JCTo.getPreferredSize().height+
					2*jbFindPath.getPreferredSize().height;

			// give ourselves some screen border
			double SW = W-(W/16);
			double SH = H-(H/14);

			// draw our edges - grey lines
			for (ArrayList<PathData> pd : p.values()) {

				for (PathData e : pd) {
					String FID = e.getFromID();
					String TID = e.getToID();

					double x1 = b.get(FID).getX();
					double y1 = b.get(FID).getY();

					x1 = left - minX + x1 *(SW-left+minX)/ (maxX);
					y1 = top - minY +  y1 *(SH-top+minY)/ (maxY);

					
					double x2 = b.get(TID).getX();
					double y2 = b.get(TID).getY();

					x2 = left -minX + x2 *(SW-left+minX)/ (maxX);
					y2 = top  -minY +  y2 *(SH-top+minY)/ (maxY);

					// set color to grey (for the edges we are going to draw)
					g2d.setColor(new Color(128, 128, 128)); // set color to grey
					//					g2d.setColor(new Color(0,0,0)); // set color to black
					g2d.setStroke(new BasicStroke((float) 0.50));
					Line2D.Double line = new Line2D.Double(x1 + (C / 2), y1 + (C / 2), x2 + (C / 2), y2 + (C / 2));
					shapes.put(line, String.format("%.3f", e.getDist()));
					g2d.draw(line);


				}
			}

			// create buildings (red circles) and intersections (blue circles)
			for (BldgData bd : b.values()) {

				double x = left -minX + bd.getX() *(SW-left+minX)/ (maxX);
				double y = top  -minY + bd.getY() *(SH-top+minY )/ (maxY);

				if (bd.getBldgName().length() > 0 && !bd.getBldgName().contains("Intersection ")) {
					g2d.setColor(new Color(196, 0, 0)); // set color to dark red
					// red circles for buildings, blue small circles for intersections
					Ellipse2D.Double circle = new Ellipse2D.Double(x, y, C, C);
					g2d.fill(circle);
					g2d.setColor(Color.black); // set color to black
					g2d.draw(circle);
					// save this circle to our our arraylist of shapes. we need this later on for
					// mouse events
					shapes.put(circle, bd.getBldgName());


				} else {
					g2d.setColor(new Color(128, 128, 255)); // set color to blue
					Ellipse2D.Double circle = new Ellipse2D.Double(x+(C/4), y+(C/4), C/2, C/2);
					g2d.fill(circle);
					g2d.setColor(Color.black); // set color to black
					g2d.draw(circle);
					// save this circle to our our arraylist of shapes
					// we need this later on for mouse events
					shapes.put(circle, bd.getBldgName());
				}

			}

		}
		@Override

		// show tooltip, when Building circle is clicked - to show building name, intersection id or 
		// translated distance in miles
		public void mouseClicked(MouseEvent e) {
			Ellipse2D.Double t1 = new Ellipse2D.Double();
			Line2D.Double t2 = new Line2D.Double();

			for (Shape s : shapes.keySet()) {

				if (s.getClass().equals(t1.getClass())) {
					if (s.contains(e.getPoint()) && shapes.get(s).length() > 0) {
						setToolTipText(shapes.get(s));
						ToolTipManager.sharedInstance().mouseMoved(e);
						break;
					}
				}

				else if (s.getClass().equals(t2.getClass())) {
					if (Math.abs(((Line2D) s).ptSegDist(e.getPoint())) <= 3) {
						setToolTipText(String.format("%.3f", Double.valueOf(shapes.get(s)) / 700.0) + " miles");
						ToolTipManager.sharedInstance().mouseMoved(e);
						break;
					}
				}
			}
		}

		@Override
		public void mouseEntered(MouseEvent e) {
			// TODO Auto-generated method stub

		}

		@Override
		public void mouseExited(MouseEvent e) {
			// TODO Auto-generated method stub

		}

		@Override
		public void mousePressed(MouseEvent e) {
			// TODO Auto-generated method stub

		}

		@Override
		public void mouseReleased(MouseEvent e) {
			// TODO Auto-generated method stub

		}
	}




	// The shortest path is calculated and stored in AL
	// now graphically draw the path on the screen From -> To
	private void DrawShortestPath(ArrayList<Edge<String, Double, Double>> AL) {

		scrollV.setEnabled(false);
		if (AL == null || AL.size() == 0) {
			jTAResult.setText("No Path Found");
			return;
		}

		jTAResult.setText("");

		Graphics g = LNE.getGraphics();
		Graphics2D g2d = (Graphics2D) g;
		g2d.setRenderingHint( RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);	// for prettier drawing
		g2d.setColor(Color.black);

		// give ourselves some screen border
		double SW = W-(W/16);
		double SH = H-(H/14);

		double left = 5*XOffset + jcfl.getPreferredSize().width;
		double top = 5*YOffset + 
				jLTitle.getPreferredSize().height + 
				jLSubTitle.getPreferredSize().height + 
				rb1.getPreferredSize().height + 
				rb3.getPreferredSize().height +
				JCFrom.getPreferredSize().height +
				JCTo.getPreferredSize().height+
				2*jbFindPath.getPreferredSize().height;

		double totDist = 0.0;

		// change cursor effect
		Cursor cursor = new Cursor(Cursor.HAND_CURSOR);
		LNE.setCursor(cursor);

		String FID = AL.get(0).getPnode().getName();
		String TID = AL.get(AL.size() - 1).getCnode().getName();

		String jts = "";
		if (rb1.isSelected())
			jts = "Shortest Distance from " + cd.getbData().get(FID).getBldgName() + ":\n";
		else if (rb2.isSelected())
			jts = "Least Hop Path from " + cd.getbData().get(FID).getBldgName() + ":\n";
		else if (rb3.isSelected())
			jts = "Path Avoiding Buildings from " + cd.getbData().get(FID).getBldgName() + ":\n";
		else if (rb4.isSelected())
			jts = "Path Avoiding Intersections from " + cd.getbData().get(FID).getBldgName() + ":\n";

		jTAResult.append(jts);
		
		double x0 = left -minX + (b.get(FID).getX() *(SW-left+minX)/ (maxX));
		double y0 = top  -minY + (b.get(FID).getY() *(SH-top+minY )/ (maxY));
		
		
		g2d.setColor(new Color(0, 0, 128, 96)); // overlay start color to dark translucent blue
		Ellipse2D.Double start = new Ellipse2D.Double(x0, y0, C, C);
		g2d.fill(start);
		g2d.setColor(Color.black); // set color to black
		g2d.draw(start);

		x0 = left -minX + (int) (b.get(TID).getX() *(SW-left+minX)/ (maxX));
		y0 = top -minY +  (int) (b.get(TID).getY() *(SH-top+minY )/ (maxY));

		g2d.setColor(new Color(0, 128, 0, 96)); // overlay end color to translucent dark green
		Ellipse2D.Double end = new Ellipse2D.Double(x0, y0, C, C);
		g2d.fill(end);
		g2d.setColor(Color.black); // set color to black
		g2d.draw(end);

		int nhops = 0;
		int isect = 0;
		int bldgs = 0;

		colorCount = colorCount % rainbow.length;
		Color c = rainbow[colorCount];
		c = new Color(c.getRed(), c.getGreen(), c.getBlue(), 96);
		colorCount = colorCount+1;

		//		g2d.setColor(rainbow[colorCount]); // set color to dark green
		g2d.setColor(c); // set color to a different shade, cycled 8 times


		// loop to show movement from one building to another along the calculated AL path
		for (Edge<String, Double, Double> e : AL) {
			nhops = nhops + 1;

			FID = e.getPnode().getName();
			TID = e.getCnode().getName();

			String dist = String.format("%.3f", e.getWeight() / 700.0);
			totDist = totDist + e.getWeight() / 700.0;

			String dir = CampusData.calcDirection(cd.getbData().get(FID).getBldgID(), cd.getbData().get(TID).getBldgID());

			String FN = cd.getbData().get(FID).getBldgName();
			String TN = cd.getbData().get(TID).getBldgName();

			if (FN.contains("Intersection ") && !FN.equals(AL.get(0).getPnode().getName())
					&& !TN.equals(AL.get(AL.size() - 1).getPnode().getName()))
				isect = isect + 1;
			else if (!FN.contains("Intersection ") && !FN.equals(AL.get(0).getPnode().getName())
					&& !TN.equals(AL.get(AL.size() - 1).getPnode().getName()))
				bldgs = bldgs + 1;

			double x1 = b.get(FID).getX();
			double y1 = b.get(FID).getY();

			x1 = left -minX + x1 *(SW-left+minX)/ (maxX);
			y1 = top -minY +  y1 *(SH-top+minY)/ (maxY);

			double x2 = b.get(TID).getX();
			double y2 = b.get(TID).getY();

			x2 = left -minX + x2 *(SW-left+minX)/ (maxX);
			y2 = top -minY+  y2 *(SH-top+minY)/ (maxY);

			Line2D.Double line = new Line2D.Double();

			for (int i = 0; i < ticks; i++) {
				double dx = (x2 - x1) / ticks;
				double dy = (y2 - y1) / ticks;
				line = new Line2D.Double(x1 + (C / 2) + (i * dx), y1 + (C / 2) + (i * dy),
						x1 + (C / 2) + ((i + 1) * dx), y1 + (C / 2) + ((i + 1) * dy));
				try {
					rbt = new Robot();
					rbt.mouseMove((int) (x1 + ((i + 1) * dx) + ticks),
							(int) (y1 + ((i + 1) * dy) + ticks*(YOffset/2)));
				} catch (AWTException e2) {
					e2.printStackTrace();
				}

				try {
					Thread.sleep(pauseTimems);
				} catch (InterruptedException e1) {
					e1.printStackTrace();
				}
				g2d.setStroke(new BasicStroke((float) 5));
				g2d.draw(line);
			}


			jts = "  Walk " + dist + " miles " + dir + ", to " + TN + "\n";
			jTAResult.append(jts);

		}
		jts = "  Total distance: " + String.format("%.3f", totDist) + " miles.\n";
		jTAResult.append(jts);

		jts = "  # of Hops: " + String.format("%3d", nhops) + ", " + " Intersections: " + String.format("%3d", isect)
		+ ", " + " Buildings: " + String.format("%3d", bldgs - 1) + ".";
		jTAResult.append(jts);

		cursor = new Cursor(Cursor.DEFAULT_CURSOR);
		LNE.setCursor(cursor);
		scrollV.setEnabled(true);

	}


	public static void main(String[] args) {

		// get frame
		frame = new JFrame("RPI Campus Map");

		// get container
		Container Pane1 = frame.getContentPane();

		// create new JPanel (extension)
		LNE = new LegendNodesEdges();
		LNE.setLayout(null);
		LNE.setOpaque(false);
		LNE.setPreferredSize(new Dimension((int)W, (int) H));

		// add LNE JPanel to contentPane
		Pane1.add(LNE);
		
		cmg = new CampusMapsGui();
		cmg.CreateGui();

		//finalize frame for viewing
		frame.setExtendedState(Frame.MAXIMIZED_BOTH);
		frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		frame.setResizable(true);
		frame.setVisible(true);
	}


}
