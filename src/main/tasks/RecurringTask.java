package main.tasks;

import main.helpers.TimeHelper;

public class RecurringTask extends Task {
	RecurringTaskType type;
	int endDate;
	int frequency;
	
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
	
	@Override
	public void printTask() {
		System.out.println(this.getName() + " | Type: " + this.getType());
		System.out.println("Start Date: " + this.getDate() + " | Duration: " + this.getDuration());
		System.out.println("End Date: " + this.getEndDate() + " | Repeats: " + (this.getFrequency() == 1 ? " daily" : "weekly"));
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
