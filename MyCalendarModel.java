import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.Month;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.GregorianCalendar;
import java.util.Iterator;
import java.util.Scanner;
import java.util.TreeMap;
import java.util.TreeSet;

import javax.swing.JPanel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

/**
 * @author Boo, Alan
 * Copyright version
 */
public class MyCalendarModel {
	private GregorianCalendar c;
	private ArrayList<Event> events;
	private ArrayList<ChangeListener> listeners;	
	private TreeMap<Integer, ArrayList<Event>> treeEvents;
	private TreeMap<GregorianCalendar, ArrayList<Event>> currentDayEvents;
	/**
	 * My Calendar constructor to initialize private variables.
	 */
	public MyCalendarModel() {
		this.c = new GregorianCalendar();
		this.events = new ArrayList<>();
		this.treeEvents = new TreeMap<>();
		this.currentDayEvents = new TreeMap<>();
		this.listeners = new ArrayList<>();
	}
	

	/**
	 * This method loads the event.txt file to populate the calendar.
	 * This load() includes the use of BufferedReader and FileReader to read the txt file.
	 * Two ArrayLists are used to store time and title of each event read from the txt.
	 * The txt file is parsed for time and title.
	 * The ArrayLists are then changed to string arrays to parse through them to to set the start time and end time of an event. 
	 * They events are added to the ArrayList by calling createEvent method in this class.
	 * @throws IOException
	 */
	public void load() throws IOException {
		System.out.println("Loading");
		try {
			FileReader fr = new FileReader("src/event.txt");
			BufferedReader br = new BufferedReader(fr);

			String[] timeEvent, titleEvent;

			String startHour, startMin;
			String endHour, endMin;

			ArrayList<String> time = new ArrayList<>();
			ArrayList<String> title = new ArrayList<>();

			String str = br.readLine();

			while (str != null) {
				String[] eventStr = str.split(" ");
				String eventTitle = "";
				str = br.readLine();
				for (int i = 0; i < eventStr.length; i++) {
					if (i == 0) {
						time.add(eventStr[0]);
					}

					else if (i == 1) {
						eventTitle += eventStr[1];
					}
				}
				title.add(eventTitle);
			}

			timeEvent = time.toArray(new String[time.size()]);
			titleEvent = title.toArray(new String[title.size()]);

			int eventTotal = timeEvent.length;
			for (int i = 0; i < eventTotal; i++) {

				GregorianCalendar startTime = new GregorianCalendar();
				GregorianCalendar endTime = new GregorianCalendar();

				String[] timeSplit = timeEvent[i].split("[:-]");
				startHour = timeSplit[0];
				int sH = Integer.parseInt(startHour);
				startMin = timeSplit[1];
				int sM = Integer.parseInt(startMin);
				endHour = timeSplit[2];
				int eH = Integer.parseInt(endHour);
				endMin = timeSplit[3];
				int eM = Integer.parseInt(endMin);

				startTime.set(Calendar.HOUR, sH);
				startTime.set(Calendar.MINUTE, sM);
				endTime.set(Calendar.HOUR, eH);
				endTime.set(Calendar.MINUTE, eM);

				createEvent(titleEvent[i], sH, eH, sM, eM);
			}

			br.close();
			fr.close();

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}


	/**
	 * 
	 * This method initializes an Event object to set the title, start time and end time of an event.
	 * Adds to ArrayList by calling add, which will notify a state change.
	 * @param eTitle Event title
	 * @param sHour Starting hour
	 * @param eHour Ending hour
	 * @param sMin Starting minute
	 * @param eMin Ending minute
	 */
	public void createEvent(String eTitle, int sHour, int eHour, int sMin, int eMin) {
		Event e = new Event();
		
		e.setEventTitle(eTitle);
		e.setStartTime(sHour);
		e.setEndTime(eHour);
		e.setStartMin(sMin);
		e.setEndMin(eMin);

		add(e);
	}
	
	/**
	 * Returns the arraylist of events.
	 * @return
	 */
	public ArrayList<Event> getEvents() {
		return events;
	}
	
	/**
	 * A treemap is returned for current day of events.
	 * @return
	 */
	public TreeMap<GregorianCalendar, ArrayList<Event>> getCurrentDayEvents() {
		return currentDayEvents;
	}
	
	/**
	 * Adds changeListener from MainCalendarFrame
	 * @param cListener
	 */
	public void addChangeListener(ChangeListener cListener) {
		listeners.add(cListener);
	}
	
	
	/**
	 * Sets the current day of events
	 * @param gc is a GregorianCalendar object
	 * @param events Arraylist of events that were added.
	 */
	public void setCurrentDayEvents(GregorianCalendar gc, ArrayList<Event> events) {
		this.currentDayEvents.put(gc, events);
	}

	/**
	 * @param event events are added to the ArrayList from create event and load.
	 * This method should notify the listener of a changed event. 
	 * @return
	 */
	public void add(Event event) {
		this.events.add(event);
		ChangeEvent cEvent = new ChangeEvent(this);
		for(ChangeListener listener: listeners) {
			listener.stateChanged(cEvent);
		}
	}
	
	/**
	 * This method checks to see if there is a conflict between two events.
	 * It uses the starthour and endhour parameters to determine the conflict.
	 * @param startHour
	 * @param endHour
	 * @return
	 */
	public Boolean checkEventConflict(int startHour, int endHour) {
		for (Event e: events) {
			if(startHour == e.getStartTime()) {
				return true;
			}			
			else if(endHour == e.getEndTime()) {
				return true;
			}
			else if (startHour > endHour) {
				return true;
			}
			else if(startHour < e.getStartTime() && endHour > e.getEndTime()) {
				return true;
			}
			else if(startHour > e.getStartTime() && endHour < e.getEndTime()) {
				return true;
			}

		}
		
		return false;
	}


	/**
	 * The quit() method incorporates FileWriter and PrintWriter for event.txt.
	 * Uses an Arraylist and TreeMap to write to event.txt. The writers are then closed.
	 */
	public void quit() {
		System.out.println("Quitting");
		try {
			FileWriter fw = new FileWriter("src/event.txt");
			PrintWriter pw = new PrintWriter(fw);

			Event e = new Event();
			ArrayList<Event> temp = new ArrayList<>(this.events);
			this.treeEvents.put(e.getStartTime(), temp);
			
			for(Integer sT: this.treeEvents.keySet()) {
				temp = this.treeEvents.get(sT);	
			}
			
			for (Event ev: temp) {
				pw.println(ev.toString());
			}

			pw.close();
			fw.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

}
