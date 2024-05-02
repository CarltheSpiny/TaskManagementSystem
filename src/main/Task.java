package main;

public class Task {

	String name;
	String type;
	int date;
	float startTime;
	float duration;
	
	public Task(String taskName, String taskType, int startDate, float startTime, float taskDuration) {
		this.name = taskName;
		this.type = taskType;
		this.date = startDate;
		this.startTime = startTime;
		this.duration = taskDuration;
	}
	
	public int getDate() {
		return date;
	}
	public float getDuration() {
		return duration;
	}
	
	public String getName() {
		return name;
	}
	
	public float getStartTime() {
		return startTime;
	}
	
	public String getType() {
		return type;
	}
	
	public void setDate(int date) {
		this.date = date;
	}
	
	public void setDuration(int duration) {
		this.duration = duration;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public void setStartTime(float startTime) {
		this.startTime = startTime;
	}
	
	public void setType(String type) {
		this.type = type;
	}
	
	public boolean equals(Task task) {
		return this.name == task.name ? true : false;
	}
	
	private void validate() {
		// validate date: has the form YYYYMMDD as an integer
		if (this.date >= 10000000) {
			// has 8 digits
			if (this.getMonth() > 0 && this.getMonth() < 13) {
				if (this.getDay() > 0 && this.getDay() < 31) {
					//return true;
				}
				//return false;
			}
			//return false;
		}
		// validate start time: a float rounded to the nearest 15 minutes
		
		// validate duration: 
	}
	
	public boolean isTaskValid() {
		return false;
	}
	
	// <------------------ Output Utils -------------------------->
	
	public int getYear() {
		String dateAsString = Integer.toString(getDate());
		return Integer.parseInt(dateAsString.substring(0, 4));
	}
	
	public int getMonth() {
		String dateAsString = Integer.toString(getDate());
		return Integer.parseInt(dateAsString.substring(4, 6));
	}

	/**
	 * Get month name from the integer representing the month
	 * @param month an integer from 1 to 12
	 * @return
	 */
	private String getMonthName(int month) {
		switch(month) {
		case 1:
			return "January";
		case 2:
			return "February";
		case 3:
			return "March";
		case 4:
			return "Apirl";
		case 5:
			return "May";
		case 6:
			return "June";
		case 7:
			return "July";
		case 8:
			return "August";
		case 9:
			return "September";
		case 10:
			return "October";
		case 11:
			return "November";
		case 12:
			return "December ";
		default:
			return "Error";
		}
	}

	public int getDay() {
		String dateAsString = Integer.toString(getDate());
		return Integer.parseInt(dateAsString.substring(6, 8));
	}
	
	public void prettyPrintDate() {
		 System.out.println(this.getMonthName(this.getMonth()) + " " + this.getDay() + ", " + this.getYear());
	}
	
	public void printTask() {
		System.out.println(this.getName() + " | Type: " + this.getType());
		System.out.println("Start Date: " + this.getDate() + " | Duration: " + this.getDuration());
	}
}
