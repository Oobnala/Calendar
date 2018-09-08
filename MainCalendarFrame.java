import java.awt.BorderLayout;
import java.awt.Color;
import java.util.GregorianCalendar;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

/**
 * 
 * @author Boo, Alan
 * Copyright version
 */
public class MainCalendarFrame extends JFrame {

	public static final int WIDTH = 700;
	public static final int HEIGHT = 480;

	private final MyCalendarController controller;
	private final MainView mainPanel;
	
	/**
	 * The MainCalendarFrame constructs a frame that holds the main view of the calendar.
	 * It also creates a MyCalendarModel object to add a changelistener to repaint the mainpanel
	 * if a change occurs.
	 * A MyCalendarController is also created to load text from the events.text file.
	 */
	public MainCalendarFrame() {
		setTitle("Assignment 4: GUI Calendar");
		setSize(WIDTH, HEIGHT);		
		setLayout(new BorderLayout());
		

		
		final MyCalendarModel myModel = new MyCalendarModel();		
		mainPanel = new MainView();
		
		controller = new MyCalendarController(mainPanel);		
		controller.load();
		
		add(mainPanel);
		
		ChangeListener listener = new 
				ChangeListener() {
					public void stateChanged(ChangeEvent e) {
						mainPanel.removeAll();
						add(mainPanel);
						mainPanel.validate();
						mainPanel.repaint();
						repaint();
					}	
		};
		myModel.addChangeListener(listener);
	}
}
