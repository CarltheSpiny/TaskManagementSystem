package main.tasks;

import java.util.GregorianCalendar;

import main.helpers.TimeHelper;

public abstract class Task {

	String name;
	String type;
	int date;
	GregorianCalendar startGregorianDate;
	float startTime;
	float duration;
	String invalidReason;
	
	public Task(String taskName, String taskType, int startDate, float startTime, float taskDuration) {
		this.name = taskName;
		this.type = taskType;
		this.date = startDate;
		this.startTime = startTime;
		this.duration = taskDuration;
		this.startGregorianDate = new GregorianCalendar(this.getYear(), this.getMonth() - 1, this.getDay());
	}
	
	public int getDate() {
		return date;
	}
	
	public GregorianCalendar getStartGregorianDate() {
		return startGregorianDate;
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
	
	public void setStartGregorianDate(GregorianCalendar startGregorianDate) {
		this.startGregorianDate = startGregorianDate;
	}
	
	public void setDuration(float duration) {
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
	
	public void setInvalidReason(String invalidReason) {
		this.invalidReason = invalidReason;
	}
	
	public String getInvalidReason() {
		return invalidReason;
	}
	
	/**
	 * Checks if the date, startTime, and duration are valid
	 * @return true if all three are valid
	 */
	public boolean isTaskValid() {
		if (!TimeHelper.isDateValid(this.getDate(), this.getMonth(), this.getDay())) {
			this.setInvalidReason("Start Date is invalid!");
			return false;
		}
		
		// validate start time: is a float rounded to the nearest 15 minutes
		if (!TimeHelper.isRounded(this.startTime) ) {
			this.setInvalidReason("The start time is invalid!");
			return false;
		}
		
		// validate duration: is a float rounded to the neaest 15 minutes
		if (!TimeHelper.isRounded(this.duration)) {
			this.setInvalidReason("The duration is invalid!");
			return false;
		}
		
		return true;
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

	public int getDay() {
		String dateAsString = Integer.toString(getDate());
		return Integer.parseInt(dateAsString.substring(6, 8));
	}
	
	public String getPrettyPrintDate() {
		return TimeHelper.getMonthName(this.getMonth()) + " " + this.getDay() + ", " + this.getYear();
	}
	
	public void prettyPrintDate() {
		 System.out.println(getPrettyPrintDate());
	}
	
	public String toString() {
		return this.getName() + " | Type: " + this.getType() + "\n" + "Start Date: " + this.getPrettyPrintDate() + " | Start Time: " + this.getStartTime() + " | Duration: " + this.getDuration();
	}
	
	public void printTask() {
		System.out.println(this.toString());
	}
	
	public enum Tasktype {
		NONE;
	}
}
