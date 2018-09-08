import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.TreeMap;

import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

/**
 * @author Alan
 * Copyright version
 */
public class MyCalendarController {

	private MyCalendarModel cModel;
	private MainView mainview;
	private GregorianCalendar gc;
	
	/**
	 * @param view is the the MainView of the calendar.
	 */
	public MyCalendarController(MainView view) {
		this.cModel = new MyCalendarModel();
		this.gc = new GregorianCalendar();
	}
	
	/**
	 * @return month of calendar
	 */
	public int getMonth() {
		return gc.get(Calendar.MONTH);
	}
	
	/**
	 * @param cal sets month of calendar passing in a GregorianCalendar from Main View
	 */
	public void setMonth(GregorianCalendar cal) {
		gc.set(Calendar.MONTH, cal.get(Calendar.MONTH));
	}
	
	/**
	 * @return returns the day of the month.
	 */
	public int getDayValue() {
		return gc.get(Calendar.DAY_OF_MONTH);
	}
	
	/**
	 * @param cal sets the day of month from view class by passing in a Gregorian Calendar.
	 */
	public void setDayValue(GregorianCalendar cal) {
		gc.set(Calendar.DAY_OF_MONTH, cal.get(Calendar.DAY_OF_MONTH));
	}

	/**
	 * @return returns year of calendar.
	 */
	public int getYear() {
		return gc.get(Calendar.YEAR);
	}
	
	/**
	 * @param cal sets the year from the Gregorian Calendar passed in.
	 */
	public void setYear(GregorianCalendar cal) {
		gc.set(Calendar.YEAR, cal.get(Calendar.YEAR));
	}
	
	/**
	 * @return day of the wek
	 */
	public int getDayOfWeek() {
		return gc.get(Calendar.DAY_OF_WEEK);
	}
	
	/**
	 * @param cal sets the day of the week from Gregorian Calendar passed from View.
	 */
	public void setDayOfWeek(GregorianCalendar cal) {
		gc.set(Calendar.DAY_OF_WEEK, cal.get(Calendar.DAY_OF_WEEK));
	}
	
	/**
	 * When a user clicks the quit button in view, this method is called
	 * and calls the quit method in model.
	 */
	public void quit() {
		cModel.quit();
	}
	
	/**
	 * 
	 * creates the event from user. Controller is being used in view which then calls this method
	 * to create an event in the model. It should notify the views when an event is added. 
	 * @param title title of event
	 * @param startHour starting hour of event
	 * @param endHour ending hour of event.
	 * @param startMin starting minute of event
	 * @param endMin ending minute of event.
	 */
	public void createEvent(String title, int startHour, int endHour, int startMin, int endMin) {
		cModel.createEvent(title, startHour, endHour, startMin, endMin);
	}
	
	/**
	 * @return an arraylist of all events from the model.
	 */
	public ArrayList<Event> getEvents() {
		return cModel.getEvents();
	}
	
	/**
	 * @return returns a treemap of currentday events from model to view.
	 */
	public TreeMap<GregorianCalendar, ArrayList<Event>> getCurrentDayEvents() {
		return cModel.getCurrentDayEvents();
	}
	
	/**
	 * Sets the current day of events from view to model using the controller object in view.
	 * @param gc Calendar
	 * @param events Arraylist of events.
	 */
	public void setCurrentDayEvents(GregorianCalendar gc, ArrayList<Event> events) {
		cModel.setCurrentDayEvents(gc, events);
	}
	
	/**
	 * 
	 * Checks to see if conflict when creating event. Controller object is used in view in the createEvent()
	 * method then calls checkEventConflict method in model.
	 * @param startHour Starting hour of event created.
	 * @param endHour ending hour of event created.
	 * @return returns true if there is a conflict, else false.
	 */
	public boolean checkConflict(int startHour, int endHour) {
		return cModel.checkEventConflict(startHour, endHour);
	}
	
	/**
	 * load method to load events from event.txt
	 */
	public void load() {
		try {
			cModel.load();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	

}
