import java.time.LocalDate;
import java.util.Calendar;
import java.util.GregorianCalendar;

/**
 * @author Boo, Alan
 * copyright version
 */

public class Event {
	private String eventTitle;
	private int startHour;
	private int endHour;
	private int startMin;
	private int endMin;
	
	private GregorianCalendar date;
	/**
	 * Event constructor
	 */
	public Event () {
		eventTitle = "";
		startHour = 0;
		endHour = 0;
		
		date = new GregorianCalendar();
	}

	/**
	 * @return event title
	 */
	public String getEventTitle() {
		return eventTitle;
	}

	/**
	 * @param eventTitle set event title
	 */
	public void setEventTitle(String eventTitle) {
		this.eventTitle = eventTitle;
	}

	
	/**
	 * @return starting hour of event.
	 */
	public int getStartTime() {
		return startHour;
	}

	
	/**
	 * set start hour of event.
	 * @param startTime pass in start hour.
	 */
	public void setStartTime(int startTime) {
		this.startHour = startTime;
	}


	/**
	 * get end hour of event.
	 * @return
	 */
	public int getEndTime() {
		return endHour;
	}
	
	/**
	 * @return get starting minute of event.
	 */
	public int getStartMin() {
		return startMin;
	}

	/**
	 * @param startMin return start mins of event
	 */
	public void setStartMin(int startMin) {
		this.startMin = startMin;
	}

	/**
	 * @return return ending mins of event.
	 */
	public int getEndMin() {
		return endMin;
	}

	/**
	 * set the ending minute of event.
	 * @param endMin 
	 */
	public void setEndMin(int endMin) {
		this.endMin = endMin;
	}

	/**
	 * sets ending hour of event.
	 * @param endTime
	 */
	public void setEndTime(int endTime) {
		this.endHour = endTime;
	}

	/**
	 * @return date of calendar.
	 */
	public GregorianCalendar getDate() {
		return date;
	}

	/**
	 * set date of the calendar.
	 * @param date
	 */
	public void setDate(GregorianCalendar date) {
		this.date = date;
	}
	
	/** 
	 * Converts arraylist of events to a string. Checks to see if min for start and end time are < than 10 
	 * to add a 0 for formatting when printing to event.txt when quit is called.
	 */
	public String toString() {
		
		if (startMin < 10 && endMin < 10) {
			return startHour + ":" + startMin + "0" +  "-" +  endHour + ":" + endMin +  "0" +" " + eventTitle;
		}

		else if (startMin < 10) {
			return startHour + ":" + startMin + "0" + "-" +  endHour + ":" + endMin + " " + eventTitle;
		}
		
		else if (endMin < 10) {
			return startHour + ":" + startMin + "-" +  endHour + ":" + endMin + "0" + " " + eventTitle;
		}
		
		return startHour + ":" + startMin + "-" +  endHour + ":" + endMin + " " + eventTitle;
		
	}
}
