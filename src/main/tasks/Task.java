package main.tasks;

import main.helpers.TimeHelper;

public abstract class Task {

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
	
	/**
	 * Checks if the date, startTime, and duration are valid
	 * @return true if all three are valid
	 */
	public boolean isTaskValid() {
		if (!TimeHelper.isDateValid(this.getDate(), this.getMonth(), this.getDay())) {
			System.err.println("Start Date is invalid!");
			return false;
		}
		
		// validate start time: is a float rounded to the nearest 15 minutes
		if (!TimeHelper.isRounded(this.startTime) ) {
			System.err.println("The start time is invalid!");
			return false;
		}
		
		// validate duration: is a float rounded to the neaest 15 minutes
		if (!TimeHelper.isRounded(this.duration)) {
			System.err.println("The duration is invalid!");
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
	
	public void prettyPrintDate() {
		 System.out.println(TimeHelper.getMonthName(this.getMonth()) + " " + this.getDay() + ", " + this.getYear());
	}
	
	public void printTask() {
		System.out.println(this.getName() + " | Type: " + this.getType());
		System.out.println("Start Date: " + this.getDate() + " | Duration: " + this.getDuration());
	}
	
	public enum Tasktype {
		NONE;
	}
}
