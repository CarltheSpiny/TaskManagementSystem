package main.tasks;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

import main.helpers.TimeHelper;

public class RecurringTask extends Task {
	RecurringTaskType type;
	int endDate;
	float endTime;
	GregorianCalendar startGregorianDate;
	GregorianCalendar endGregorianDate;
	int frequency;
	boolean isDummyInstance;
	int dummyReps = 0;
	
	/**
	 * Default constructor for a recurring task using the TaskType Enum to ensure only the compatible
	 * types are set on this Recurring Task
	 * @param taskName a unique name
	 * @param taskType a type from {@link RecurringTaskType}
	 * @param startDate the date in YYYYMMDD format
	 * @param endDate the end date in YYYYMMDD format
	 * @param startTime the start time in 24-hour format as a decimal
	 * @param taskDuration the duration in a decimal rounded to the nearest 15 minutes
	 * @param taskFrequency the frequency of this task (either 1 for daily or 7 for weekly)
	 */
	public RecurringTask(String taskName, RecurringTaskType taskType, int startDate, int endDate, float startTime, float taskDuration, int taskFrequency) {
		super(taskName, taskType.getId(), startDate, startTime, taskDuration);
		this.type = taskType;
		this.endDate = endDate;
		this.frequency = taskFrequency;
		this.startGregorianDate = new GregorianCalendar(this.getYear(), this.getMonth() - 1, this.getDay());
		this.endGregorianDate = new GregorianCalendar(this.getEndYear(), this.getEndMonth() - 1, this.getEndDay());
	}
	
	public int getEndDate() {
		return endDate;
	}
	
	public void setEndDate(int endDate) {
		this.endDate = endDate;
	}
	
	/**
	 * The override for getting the task name from the TaskType enum. 
	 * This way, we can ensure the name is always synced to the type.
	 */
	@Override
	public String getType() {
		return type.getId();
	}
	
	/**
	 * This override parses the task type from a string and sets the new type
	 * if it exists. If it can't find a type from the string, it sets this type 
	 * to none.
	 */
	@Override
	public void setType(String type) {
		RecurringTaskType taskFromString = RecurringTaskType.fromString(type);
		if (taskFromString != null)
			super.setType(taskFromString.getId());
		else
			this.type = RecurringTaskType.NONE;
	}
	
	public void setType(RecurringTaskType newType) {
		this.type = newType;
	}
	
	public int getFrequency() {
		return frequency;
	}
	
	public void setFrequency(int frequency) {
		this.frequency = frequency;
	}
	
	/**
	 * Get the end time (in 24 hour format) for this task. If the duration would cause
	 * the time to go over 24, the time is wrapped to the right time (25 -> 1AM)
	 * @return
	 */
	public float getEndTime() {
		float endTime = this.getStartTime() + this.duration;
		return endTime > 24 ? endTime-24 : endTime;
	}

	/**
	 * Get the year from the end date. Assumes the date is valid
	 * @return
	 */
	public int getEndYear() {
		String dateAsString = Integer.toString(this.getEndDate());
		return Integer.parseInt(dateAsString.substring(0, 4));
	}
	
	/**
	 * Get the month from the end date. Assumes the date is valid
	 * @return
	 */
	public int getEndMonth() {
		String dateAsString = Integer.toString(this.getEndDate());
		return Integer.parseInt(dateAsString.substring(4, 6));
	}
	
	/**
	 * Get the day from the end date. Assumes the date is valid
	 * @return
	 */
	public int getEndDay() {
		String dateAsString = Integer.toString(this.getEndDate());
		return Integer.parseInt(dateAsString.substring(6, 8));
	}
	
	/**
	 * We have to return a copy of the date beacuse any destructive updates will mess up the date
	 * @return
	 */
	public GregorianCalendar getStartGregorianDate() {
		return (GregorianCalendar) startGregorianDate.clone();
	}
	
	public GregorianCalendar getEndGregorianDate() {
		return (GregorianCalendar)endGregorianDate.clone();
	}
	
	/**
	 * Override of the validation method that validates the EndDate and TaskType 
	 * for recurring tasks
	 */
	@Override
	public boolean isTaskValid() {
		if (!TimeHelper.isDateValid(this.getEndDate(), this.getEndMonth(), this.getEndDay())) {
			System.out.println("The end date is invalid");
			return false;
		}
		
		if (this.type.equals(RecurringTaskType.NONE)) {
			System.err.println("The task type is invalid");
			return false;
		}
		
		if (!(this.getFrequency() == 1 || this.getFrequency() == 7)) {
			System.err.println("The frequency is invalid");
			return false;
		}
		
		return super.isTaskValid();
	}

	/**
	 * returns wheter or not the passed task occurs during this task's occurances
	 * @param recurringTask
	 * @return false if the task doesn't overlap, true if there is an overlap
	 */
	public boolean overlapsWith(RecurringTask recurringTask) {
		boolean validating = true;
		RecurringTask nextInstance = this;
		
		//System.out.println("check against the future occurances of this task");
		while(validating) {
			//System.out.println("Checking if this task occurs on the same day");
			if (recurringTask.getStartGregorianDate().compareTo(nextInstance.getStartGregorianDate()) == 0) {
				// if the tasks happen on the same day, we should check if they occur at the same time
				//System.out.println("checking if the new task start time is during the time of the next occurance of a R! task");
				if (recurringTask.getStartTime() >= nextInstance.getStartTime() || (recurringTask.getEndTime()) <= (nextInstance.getEndTime())) {
					// first argument checks if the start time is greater than the start time of the nextInstance
					// second argument checks if the endTime of the comparator task is within the endTiem of the nextInstance
					System.err.println("The " + recurringTask.getName() + " task has an overlapping date with the "
							+ this.getName() + " task: " + this.getStartPrettyPrintDate());
					return true;
				} 
			}
			nextInstance = nextInstance.getNextOccurrance();
			if (nextInstance != null) {
				// if the next instance's date is the same as the end date, then we have checked all occurances
				if (nextInstance.getStartGregorianDate().compareTo(this.getEndGregorianDate()) >= 0)
					validating = false;
			} else
				return true;
		}
		System.out.println("No overlap found");
		return false;
	}
	
	public RecurringTask getNextOccurrance() {
		GregorianCalendar localStartDate = getStartGregorianDate();
		GregorianCalendar localEndDate = getEndGregorianDate();
		String dayString;
		String monthString;
		
;		// System.out.println("Set the time for this rec instance: " + startGregorianDate.getTime().toString());
		// System.out.println("Actual time for ref: " + this.getDate());
		switch (this.frequency) {
		case 1:
			// occurs daily
			// next instance is +1 day, so lets advance the calender
			localStartDate.add(Calendar.DAY_OF_MONTH, getFrequency());
			if (localStartDate.compareTo(localEndDate) > 0) {
				//System.err.println("Next requested occurance is past end date: " + localStartDate.getTime().toString());
				return null;
			}
			
			dayString = localStartDate.get(Calendar.DAY_OF_MONTH) < 10 ? "0" + localStartDate.get(Calendar.DAY_OF_MONTH) : "" + localStartDate.get(Calendar.DAY_OF_MONTH);
			monthString = localStartDate.get(Calendar.MONTH) < 10 ? "0" + (localStartDate.get(Calendar.MONTH) + 1) : "" + (localStartDate.get(Calendar.MONTH) + 1);
			String nextDailyDate = localStartDate.get(Calendar.YEAR) + monthString + dayString;
			//System.out.println("Next Daily Occurance: " +  nextDailyDate);
			RecurringTask nextDailyTask = new RecurringTask(getName(), type, Integer.parseInt(nextDailyDate), getEndDate(), startTime, duration, frequency);
			return nextDailyTask;
		case 7:
			// add 7 days to the start date
			localStartDate.add(Calendar.DAY_OF_MONTH, getFrequency());
			// if the new startDate is over the 
			if (localStartDate.compareTo(localEndDate) >= 0) {
				//System.err.println("Next requested occurance is past end date: " + localStartDate.getTime().toString());
				return null;
			}
			
			dayString = localStartDate.get(Calendar.DAY_OF_MONTH) < 10 ? "0" + localStartDate.get(Calendar.DAY_OF_MONTH) : "" + localStartDate.get(Calendar.DAY_OF_MONTH);
			monthString = localStartDate.get(Calendar.MONTH) < 10 ? "0" + (localStartDate.get(Calendar.MONTH) + 1) : "" + (localStartDate.get(Calendar.MONTH) + 1);
			String nextWeeklyDate = "" + localStartDate.get(Calendar.YEAR) + monthString + dayString;
			//System.out.println("Next Weekly Occurance: " +  nextWeeklyDate);
			RecurringTask nextWeeklyTask = new RecurringTask(getName(), type, Integer.parseInt(nextWeeklyDate), getEndDate(), startTime, duration, frequency);
			nextWeeklyTask.setDummyInstance(true);
			nextWeeklyTask.setDummyReps(nextWeeklyTask.getDummyReps()+1);
			return nextWeeklyTask;
		default:
			break;
		}
		return null;
	}
	
	public List<RecurringTask> getAllOccurances() {
		ArrayList<RecurringTask> allOccurances = new ArrayList<>();
		RecurringTask nextRecurringTask = this.getNextOccurrance();
		while (nextRecurringTask != null) {
			
			if (nextRecurringTask != null) {
				allOccurances.add(nextRecurringTask);
			}
			nextRecurringTask = nextRecurringTask.getNextOccurrance();
		}
		return allOccurances;
	}
	
	@Override
	public void printTask() {
		System.out.println(this.getName() + " | Type: " + this.getType());
		System.out.println("Start Date: " + this.getStartPrettyPrintDate() + " | Duration: " + this.getDuration());
		System.out.println("End Date: " + this.getEndPrettyPrintDate() + " | Repeats: " + (this.getFrequency() == 1 ? " daily" : "weekly"));
	}
	
	public String getStartPrettyPrintDate() {
		return TimeHelper.getMonthName(this.getMonth()) + " " + this.getDay() + ", " + this.getYear();
	}
	
	public String getEndPrettyPrintDate() {
		return TimeHelper.getMonthName(this.getEndMonth()) + " " + this.getEndDay() + ", " + this.getEndYear();
	}
	
	public void setDummyInstance(boolean isDummyInstance) {
		this.isDummyInstance = isDummyInstance;
	}
	
	public boolean isDummyInstance() {
		return isDummyInstance;
	}
	
	public int getDummyReps() {
		return dummyReps;
	}
	
	public void setDummyReps(int dummyReps) {
		this.dummyReps = dummyReps;
	}
	
	/**
	 * An enum that represnets the 7 types a recurring task can be. this ensures
	 * that only the compatible tasks can be added
	 * @author CTS
	 *
	 */
	public enum RecurringTaskType {
		CLASS("Class"),
		STUDY("Study"),
		SLEEP("Sleep"),
		EXERCISE("Exercise"),
		WORK("Work"),
		MEAL("Meal"),
		NONE("None");
		
		String id;
		
		private RecurringTaskType(String taskID) {
			this.id = taskID;
		}
		
		public String getId() {
			return id;
		}
		
		/**
		 * Get the type from the string, assuming it is spelled correctly
		 * @param type
		 * @return the type if any, else it will return null
		 */
		public static RecurringTaskType fromString(String type) {
	        for (RecurringTaskType tasktype : RecurringTaskType.values()) {
	            if (tasktype.getId().equals(type)) {
	                return tasktype;
	            }
	        }
	        return null;
	    }
	}
	
	
}
