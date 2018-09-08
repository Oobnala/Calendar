import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.GregorianCalendar;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Scanner;
import java.util.TreeMap;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;

/**
 * @author Boo, Alan
 * Copyright Version
 */
public class MainView extends JPanel {
	private GregorianCalendar cal;
	private ArrayList<JLabel> allDayLabels;
	private JPanel dayPanel;
	private JPanel dayTitlePanel;
	private JPanel buttonPanel;
	private JPanel westPanelTop;
	private JPanel westPanel;
	private JLabel calendarTitle;
	private MyCalendarController controller;
	private ArrayList<JLabel> dayTitle;

	private JButton createButton;
	private JButton quitButton;
	private JButton prevDayButton;
	private JButton nextDayButton;

	private JPanel eastPanel;
	private JLabel currentDayPanel;
	private JTable eventHoursTable;
	private JTable eventScheduledTable;

	/**
	 * MainView initializes all private JPanels, JButtons, JTables, ArrayLists, and Gregorian Calendar.
	 * A Controller object is used. The four buttons used in this application are Create, quit and "<", ">".
	 * Create button has actionlistener to call the createEvent method which displays a JOptionPane to get user input and schedule an event.
	 * The quit button exits the calendar and prints out events scheduled to event.txt.
	 * The "<" and ">" buttons change from previous and next days. The controller is notified about day, month, and calls monthView() and dayView() to display
	 * the JPanels for the application.
	 */
	public MainView() {

		// Initialize private global variables for calendar view
		allDayLabels = new ArrayList<>();
		dayTitle = new ArrayList<>();
		cal = new GregorianCalendar();
		dayTitlePanel = new JPanel();
		dayPanel = new JPanel();
		buttonPanel = new JPanel();
		westPanel = new JPanel();
		westPanelTop = new JPanel();
		controller = new MyCalendarController(this);
		calendarTitle = new JLabel();

		// Initialize private global variables for day view
		eastPanel = new JPanel();
		currentDayPanel = new JLabel("");

		// Set layoutManagers for panels.
		setLayout(new BorderLayout());
		westPanel.setLayout(new BorderLayout());
		westPanelTop.setLayout(new BorderLayout());
		eastPanel.setLayout(new BorderLayout());
		dayTitlePanel.setLayout(new GridLayout(0, 7));
		dayPanel.setLayout(new GridLayout(0, 7));
		buttonPanel.setLayout(new FlowLayout());

		// Create buttons for create, quit, previous day, and next day.
		createButton = new JButton("Create");
		createButton.setBackground(Color.WHITE);
		quitButton = new JButton("Quit");
		quitButton.setBackground(Color.RED);
		prevDayButton = new JButton("<");
		nextDayButton = new JButton(">");

		// Add buttons to the buttonPanel
		buttonPanel.add(prevDayButton);
		buttonPanel.add(nextDayButton);
		buttonPanel.add(createButton);
		buttonPanel.add(quitButton);

		// ActionListeners for button in ButtonPanel that use controller to
		// modify model.
		createButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				createEvent();
			}

		});

		prevDayButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				cal.add(Calendar.DAY_OF_MONTH, -1);
				controller.setDayOfWeek(cal);
				controller.setDayValue(cal);
				controller.setMonth(cal);
				controller.setYear(cal);
				monthView();
				dayView();

			}
		});

		nextDayButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				cal.add(Calendar.DAY_OF_MONTH, 1);
				controller.setDayOfWeek(cal);
				controller.setDayValue(cal);
				controller.setMonth(cal);
				controller.setYear(cal);
				monthView();
				dayView();
			}

		});

		quitButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				controller.quit();
				System.exit(0);
			}
		});

		// Sets the title of each day.
		dayTitle = new ArrayList<>();
		dayTitle.add(new JLabel("Su"));
		dayTitle.add(new JLabel("Mo"));
		dayTitle.add(new JLabel("Tu"));
		dayTitle.add(new JLabel("We"));
		dayTitle.add(new JLabel("Th"));
		dayTitle.add(new JLabel("Fr"));
		dayTitle.add(new JLabel("Sa"));

		// Add all dayTitle JLabels to the dayTitle panel.
		for (JLabel dayTitleLabel : dayTitle) {
			dayTitleLabel.setOpaque(true);
			dayTitleLabel.setBackground(Color.GRAY);
			dayTitlePanel.add(dayTitleLabel);
		}


		// Make the JTable cells for the eventScheduledTable uneditable.
		eventScheduledTable = new JTable(24, 1) {
			public boolean isCellEditable(int row, int columns) {
				return false;
			}
		};
		
		// sets the width of the eventScheduledTable
		eventScheduledTable.getColumnModel().getColumn(0).setPreferredWidth(320);	
		
		//Both methods display the hours of the left side of the eastPanel. The eastPanel Date displays the date of the current day.
		getDayViewHours();
		getEastPanelDate();
		
		// The westPanelTop adds buttonPanel, calendarTitle, and dayTitle.
		westPanelTop.add(buttonPanel, BorderLayout.NORTH);
		westPanelTop.add(calendarTitle, BorderLayout.CENTER);
		westPanelTop.add(dayTitlePanel, BorderLayout.SOUTH);

		// West panel adds the top panel and the dayPanel.
		westPanel.add(westPanelTop, BorderLayout.NORTH);
		westPanel.add(dayPanel, BorderLayout.CENTER);

		// East panel adds currentDay chosen and includes the JTables for hours and the events scheduled.
		eastPanel.add(currentDayPanel, BorderLayout.NORTH);
		eastPanel.add(eventHoursTable, BorderLayout.WEST);
		eastPanel.add(eventScheduledTable, BorderLayout.EAST);

		monthView();

		add(westPanel, BorderLayout.WEST);
		add(eastPanel, BorderLayout.EAST);
	}

	/**
	 * monthView() displays the month on the West side of the MainCalendarFrame.
	 * It consists of using the MONTHS enum in the tester for the calendar date JLabel.
	 * the day panel removes all and repaints when days change. The dayPanel consists of 
	 * All JLabels for days and are clickable. They have white background, but has a cyan color
	 * background that shows current day. The chosen day from using the mouselistener
	 * calls dayView() to change and repaint the east panel.
	 * 
	 */
	public void monthView() {

		MONTHS[] month = MONTHS.values();
		calendarTitle.setText("" + month[cal.get(Calendar.MONTH)] + " " + cal.get(Calendar.YEAR));
		allDayLabels.clear();
		dayPanel.removeAll();
		GregorianCalendar gc = new GregorianCalendar();
		gc.set(1, cal.get(Calendar.MONTH), cal.get(Calendar.YEAR));
		for (int i = 0; i < gc.get(Calendar.DAY_OF_WEEK); i++) {
			JLabel blank = new JLabel(" ");
			blank.setOpaque(true);
			blank.setBackground(Color.WHITE);
			dayPanel.add(blank);
		}

		for (int i = 1; i <= cal.getActualMaximum(Calendar.DAY_OF_MONTH); i++) {

			JLabel dayLabel = new JLabel(" " + i);
			dayLabel.setOpaque(true);
			dayLabel.setBackground(Color.WHITE);

			if (i == cal.get(Calendar.DAY_OF_MONTH)) {
				dayLabel.setBackground(Color.CYAN);
				allDayLabels.add(dayLabel);
				dayPanel.add(dayLabel);
			} else if (i < cal.get(Calendar.DAY_OF_MONTH)) {
				allDayLabels.add(dayLabel);
				dayPanel.add(dayLabel);
			} else if (i > cal.get(Calendar.DAY_OF_MONTH)) {
				allDayLabels.add(dayLabel);
				dayPanel.add(dayLabel);
			}
			dayLabel.addMouseListener(new MouseAdapter() {
				public void mouseClicked(MouseEvent e) {
					for (JLabel l : allDayLabels) {
						l.validate();
						l.setBackground(Color.WHITE);
						l.repaint();
					}
					int chosenDay = allDayLabels.indexOf(e.getSource());
					cal.set(Calendar.DAY_OF_MONTH, chosenDay + 1);
					controller.setDayOfWeek(cal);
					controller.setDayValue(cal);
					controller.setMonth(cal);
					controller.setYear(cal);
					dayView();
					dayLabel.setBackground(Color.CYAN);
				}
			});
		}
		dayPanel.validate();
		dayPanel.repaint();
	}

	/**
	 * The dayView() displays the eastern side of the MainCalendarFrame.
	 * It calls the getEastPanelDate() and getDayViewHours() methods to display 
	 * the date at the top and the hours on the west side of the east panel
	 * Interacts with controller to get current day events from model which is a treemap. 
	 * If the treemap is not empty and the events arraylist is not empty for that specific day,
	 * it will take the start time and end Time to put an "X" in the JTable. For every cell marked 
	 * with an "X" it will then use a JTable renderer to color it light gray. If it's not marked "X"
	 * or an event is not scheduled at that time, the color will be white.
	 */
	public void dayView() {
		eastPanel.removeAll();
		getEastPanelDate();
		getDayViewHours();
		TreeMap<GregorianCalendar, ArrayList<Event>> currentDayEvents = controller.getCurrentDayEvents();
		ArrayList<Event> theEvents = currentDayEvents.get(cal); 
		String[] time = new String[24];
		Object[][] eventData = new Object[24][1];
		Object[] dataName = {""}; 
		
		if (currentDayEvents != null) {
		for (GregorianCalendar gc: currentDayEvents.keySet() ) {
			System.out.println("hi");
			// checks to see if there are events in arraylist and on that day using the curretnDayEvents TreeMap.
			if (cal.get(Calendar.DAY_OF_MONTH) == gc.get(Calendar.DAY_OF_MONTH) && cal.get(Calendar.MONTH) == gc.get(Calendar.MONTH) && cal.get(Calendar.YEAR) == gc.get(Calendar.YEAR)) {

				System.out.println("test");
				for (Event e: theEvents) {
					int startingTime = e.getStartTime();
					int endingTime = e.getEndTime();
					int beginning = startingTime;
					int ending = endingTime - 1;
					
					// Event title is placed at the starting hour
					eventData[beginning][0] = e.getEventTitle();
					
					// Loops from the beginning of the event to the ending time of the event and 
					// places an "X" to show which cells to color.
					for (int i = beginning; i <= ending; i++) {
						time[i] = "X";
					}
				}
			}
			// If no event on the chosen day, displays empty table.
		}
		}
		
		// JTable renderer to color event scheduled marked with "X" light gray. Else colored white
		// if no event scheduled.
		eventScheduledTable = new JTable(eventData, dataName) {
            public Component prepareRenderer(TableCellRenderer tablerenderer, int row, int column) {
                Component comp = super.prepareRenderer(tablerenderer, row, column);
                
                if (time[row] == "X") {
                    comp.setBackground(Color.LIGHT_GRAY);
                } else {
                    comp.setBackground(Color.WHITE);
                }
                return comp;
            }
		};
		//eventScheduledTable = new JTable(24,1);
		eventScheduledTable.getColumnModel().getColumn(0).setPreferredWidth(320);
		eastPanel.add(eventScheduledTable, BorderLayout.EAST);		
		eastPanel.validate();
		eastPanel.repaint();

	}
	
	/**
	 * Displays the date of the current day at the top of the east panel. Uses the DAYS enum in calendar tester.
	 */
	public void getEastPanelDate() {
		DAYS[] days = DAYS.values();
		currentDayPanel.setText("" + days[controller.getDayOfWeek() - 1] + " " + (controller.getMonth() + 1) + "/"
				+ controller.getDayValue());
		eastPanel.add(currentDayPanel, BorderLayout.NORTH);
	}
	
	/**
	 * Displays the JTable of hours on the west side of the eastPanel.
	 * It is set to uneditable.
	 */
	public void getDayViewHours() {
		String[] hours = { "12am", "1am", "2am", "3am", "4am", "5am", "6am", "7am", "8am", "9am", "10am", "11am",
				"12pm", "1pm", "2pm", "3pm", "4pm", "5pm", "6pm", "7pm", "8pm", "9pm", "10pm", "11pm" };

		Object rowData[][] = new Object[24][1];
		for (int i = 0; i < hours.length; i++) {
			rowData[i][0] = hours[i];
		}
		Object[] columnNames = { "" };

		eventHoursTable = new JTable(rowData, columnNames) {
			public boolean isCellEditable(int row, int columns) {
				return false;
			}
		};
		eastPanel.add(eventHoursTable, BorderLayout.WEST);
	}

	/**
	 * createEvent() is a method opens a window using JOptionPane.
	 * It takes input from the user using JTextFields. The data is then parsed
	 * to obtain start time, end time, and the title of the event.
	 * This method will also check if there is an event conflict.
	 * If there is no conflict, sets the current day of events then calls dayView method()
	 * to display events on that day.
	 */
	public void createEvent() {
		JLabel date = new JLabel("" + (cal.get(Calendar.MONTH) + 1)+ "/" + cal.get(Calendar.DAY_OF_MONTH) + "/" + cal.get(Calendar.YEAR));
		JTextField eventTitle = new JTextField(20);
		JTextField startTime = new JTextField(20);
		JTextField endTime = new JTextField(20);

		JPanel createEventPanel = new JPanel();
		createEventPanel.setLayout(new GridLayout(0, 1));
		createEventPanel.add(date);
		createEventPanel.add(new JLabel("Please enter event title: "));
		createEventPanel.add(eventTitle);
		createEventPanel.add(new JLabel("Please enter start time (Example: 8:00)"));
		createEventPanel.add(startTime);
		createEventPanel.add(new JLabel("Please enter end time (Example: 13:00)"));
		createEventPanel.add(endTime);
		
		JOptionPane.showConfirmDialog(this, createEventPanel, "Create Event", JOptionPane.OK_CANCEL_OPTION);
		String title = eventTitle.getText();
		
		String sTime = startTime.getText();
		String eTime = endTime.getText();

		String[] st = sTime.split("[:-]");
		int sHour = Integer.parseInt(st[0]);
		int sMin = Integer.parseInt(st[1]);
		
		String[] et = eTime.split("[:-]");
		int eHour = Integer.parseInt(et[0]);
		int eMin = Integer.parseInt(et[1]);
		
		if(controller.checkConflict(sHour, eHour) == true){
			JOptionPane.showMessageDialog (null, "Sorry, there is an event already scheduled at that time. Please try again.", " Scheduling Conflict", JOptionPane.WARNING_MESSAGE);
		}
		else {
			controller.createEvent(title, sHour, eHour, sMin, eMin);
			int month = cal.get(Calendar.MONTH) ;
			int day = cal.get(Calendar.DAY_OF_MONTH);
			int year = cal.get(Calendar.YEAR);
			GregorianCalendar gc = new GregorianCalendar(month, day, year);
			controller.setCurrentDayEvents(gc, controller.getEvents());
			dayView();
		}

	}
	
}

