import javax.swing.JFrame;

enum MONTHS {
	January, February, March, April, May, June, July, August, September, October, November, December;
}

enum DAYS {
	Sunday, Monday, Tuesday, Wednesday, Thursday, Friday, Saturday
}

/**
 * @author Boo, Alan
 * copyright version
 */
public class SimpleCalendar {
	/**
	 * The tester creates a MainCalendarFrame objects that has-a MainView JPanel. 
	 * frame is set to be visible and close on exit.
	 * @param args
	 */
	public static void main(String[] args) {
		JFrame frame = new MainCalendarFrame();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);
	}
}
