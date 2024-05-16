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
	GregorianCalendar endGregorianDate;
	int frequency;
	AntiTask cancelledOccurance;
	List<AntiTask> cancelledOccurances;
	boolean isCancelled = false;
	
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
		this.endTime = startTime + taskDuration >= 24 ? (startTime + taskDuration) - 24 : startTime + taskDuration;
		this.type = taskType;
		this.endDate = endDate;
		this.frequency = taskFrequency;
		this.endGregorianDate = new GregorianCalendar(this.getEndYear(), this.getEndMonth() - 1, this.getEndDay());
	}
	
	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return this.getName() + " | Type: " + this.getType() + this.getNumberOfCancelledSeries() + "\n" + "Start Date: " + this.getStartPrettyPrintDate() + " | Start Time: " + this.getStartTime() + " | Duration: " + this.getDuration()
		+ "\n" + "End Date: " + this.getEndPrettyPrintDate() + " | End Time: " + this.getEndTime() + " | Repeats: " + (this.getFrequency() == 1 ? " daily" : "weekly");
	}
	
	private String getNumberOfCancelledSeries() {
		if (this.getCancelledOccurances() != null && !this.getCancelledOccurances().isEmpty()) {
			return " | Cancelled Series: " + this.getCancelledOccurances().size() + "";
		} else if (this.getCancelledOccurance() != null) {
			return "| Cancelled Series: 1";
		}
		return "";
	}

	@Override
	public void printTask() {
		System.out.println(this.toString());
	}

	/**
	 * returns wheter or not the passed task occurs during this task's occurances
	 * @param taskToCheck
	 * @return false if the task doesn't overlap, true if there is an overlap
	 */
	public boolean overlapsWith(Task taskToCheck) {
		boolean validating = true;
		RecurringTask nextInstance = this;
		
		//System.out.println("check against the future occurances of this task");
		while(validating) {
			//System.out.println("Checking if this task occurs on the same day");
			if (nextInstance.isCancelled) {
				//System.out.println("The nextInstance is cancelled, so there is no overlaps");
			}
			else if (taskToCheck.getStartGregorianDate().compareTo(nextInstance.getStartGregorianDate()) == 0) {
				
				
				if (taskToCheck instanceof RecurringTask recurringTask) {
					if (recurringTask.isCancelled) {
						//System.out.println("This task is cancelled by an antitask, nextInstance will not overlap");
						return false;
					}
					
					
						
					// if the tasks happen on the same day, we should check if they occur at the same time
					if (recurringTask.getStartTime() == nextInstance.getStartTime()
							|| (recurringTask.getEndTime() > nextInstance.getStartTime() && (recurringTask.getEndTime()) <= (nextInstance.getEndTime()))) {
						// first argument checks if the start time is start time of the nextInstance
						// second argument checks if the endTime of the comparator task is within the endTime of the nextInstance
						System.err.println("The " + recurringTask.getName() + " recurring task has an overlapping date with the " + this.getName() + " task: " + nextInstance.getStartPrettyPrintDate());
						return true;
					} 
				} else if(taskToCheck instanceof AntiTask antiTask) {
					float endTime = (antiTask.getStartTime() + antiTask.duration);
					// anti-tasks indicate single cancelations of reaccuring tasks
					if (antiTask.getStartTime() == nextInstance.getStartTime()) {
						System.out.println("This anti task equals this recurring task's time exactly");
						return true;
					}
					
					if ((antiTask.getStartTime() + antiTask.duration) <= nextInstance.getEndTime()) {
						System.out.println("The end time of the antitask is within the time of the next reccurance");
						return true;
					}
					
					if ((antiTask.getStartTime() + (endTime / 2)) <= nextInstance.getEndTime()) {
						
						System.out.println("The " + antiTask.getName() + " antitask has an overlapping date with the " + this.getName() + " task: " + nextInstance.getStartPrettyPrintDate());
						return true;
					}
				} else {
					if (taskToCheck.getStartTime() == nextInstance.getStartTime()) {
						System.out.println("This task equals this recurring task's time exactly");
						return true;
					}
					
					if (taskToCheck.getStartTime() + taskToCheck.duration > nextInstance.getStartTime() && (taskToCheck.getStartTime() + taskToCheck.duration) <= nextInstance.getEndTime()) {
						System.err.println("The end time of the task is within the time of the next reccurance: \n" + nextInstance.toString());
						return true;
					}
					
					if ((taskToCheck.getStartTime() + (endTime / 2)) <= nextInstance.getEndTime()) {
						System.out.println("The " + taskToCheck.getName() + " task has an overlapping date with the "+ this.getName() + " task: " + nextInstance.getStartPrettyPrintDate());
						return true;
					}
				}
			}
			nextInstance = nextInstance.getNextOccurrance();
			if (nextInstance != null) {
				// if the next instance's date is the same as the end date, then we have checked all occurances
				if (nextInstance.getStartGregorianDate().compareTo(this.getEndGregorianDate()) >= 0)
					validating = false;
			} else
				validating = false;
		}
		// System.out.println("No overlap found with: " + this.getName());
		return false;
	}

	/**
	 * Override of the validation method that validates the EndDate and TaskType 
	 * for recurring tasks
	 */
	@Override
	public boolean isTaskValid() {
		if (!TimeHelper.isDateValid(this.getEndDate(), this.getEndMonth(), this.getEndDay())) {
			this.setInvalidReason("The end date is invalid");
			return false;
		}
		
		if (this.type.equals(RecurringTaskType.NONE)) {
			this.setInvalidReason("The task type is invalid");
			return false;
		}
		
		if (!(this.getFrequency() == 1 || this.getFrequency() == 7)) {
			this.setInvalidReason("The frequency is invalid");
			return false;
		}
		
		return super.isTaskValid();
	}
	
	/**
	 * Add an anti task to this recurring task. Handles the cases where there is already 
	 * a cancelation or multiple cancelations
	 * @param cancelationTask
	 */
	public void addAntiTask(AntiTask cancelationTask) {
		if (this.getCancelledOccurance() != null) {
			// there is already an antitask, move both to the list
			this.addAntiTaskToList(this.getCancelledOccurance());
			this.addAntiTaskToList(cancelationTask);
		} else if (this.getCancelledOccurances() != null && !this.getCancelledOccurances().isEmpty()) {
			this.addAntiTaskToList(cancelationTask);
		} else {
			this.setCancelledOccurance(cancelationTask);
		}
		
	}
	
	private void addAntiTaskToList(AntiTask antiTask) {
		this.getCancelledOccurances().add(antiTask);
	}

	public int getEndDate() {
		return endDate;
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
	@Override
	public GregorianCalendar getStartGregorianDate() {
		return (GregorianCalendar)startGregorianDate.clone();
	}

	public GregorianCalendar getEndGregorianDate() {
		return endGregorianDate;
	}

	public int getFrequency() {
		return frequency;
	}

	public RecurringTask getNextOccurrance() {
			GregorianCalendar localStartDate = this.getStartGregorianDate();
			GregorianCalendar localEndDate = this.getEndGregorianDate();
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
				nextDailyTask.setCancelledOccurance(this.getCancelledOccurance());
				nextDailyTask.setCancelledOccurances(this.getCancelledOccurances());
				if (this.getCancelledOccurance() != null) {
					if (nextDailyTask.getDate() == this.getCancelledOccurance().getDate())
						nextDailyTask.setCancelled(true);
					
				} else if (this.getCancelledOccurances() != null) {
					for (AntiTask antiTask : this.getCancelledOccurances()) {
						if (nextDailyTask.getDate() == antiTask.getDate())
							nextDailyTask.setCancelled(true);
					}
				}
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
				nextWeeklyTask.setCancelledOccurance(this.getCancelledOccurance());
				nextWeeklyTask.setCancelledOccurances(this.getCancelledOccurances());
				if (this.getCancelledOccurance() != null) {
					if (nextWeeklyTask.getDate() == this.getCancelledOccurance().getDate())
						nextWeeklyTask.setCancelled(true);
					
				} else if (this.getCancelledOccurances() != null) {
					for (AntiTask antiTask : this.getCancelledOccurances()) {
						if (nextWeeklyTask.getDate() == antiTask.getDate())
							nextWeeklyTask.setCancelled(true);
					}
				}
				return nextWeeklyTask;
			default:
				break;
			}
			return null;
		}

	public List<RecurringTask> getAllFutureOccurances() {
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
	
	public boolean isCancelled() {
		return isCancelled;
	}
	
	public void setCancelled(boolean isCancelled) {
		this.isCancelled = isCancelled;
	}
	
	/**
	public boolean isCancelled() {
		if (this.getCancelledOccurances() != null) {
			for (AntiTask cancelledInstance : this.getCancelledOccurances()) {
				if (this.overlapsWith(cancelledInstance))
					return true;
			}
			
		} else if (this.getCancelledOccurance() != null) {
			if (this.overlapsWith(this.getCancelledOccurance()))
				return true;
		}
		return false;
	}
	 **/

	public String getStartPrettyPrintDate() {
		return TimeHelper.getMonthName(this.getMonth()) + " " + this.getDay() + ", " + this.getYear();
	}

	public String getEndPrettyPrintDate() {
		return TimeHelper.getMonthName(this.getEndMonth()) + " " + this.getEndDay() + ", " + this.getEndYear();
	}
	
	/** 
	 * Get the next (or only) anti-task cancellation for this recurring series
	 * @return
	 */
	public AntiTask getCancelledOccurance() {
		return cancelledOccurance;
	}
	
	/**
	 * Get all the anti-task cancellation for this recurring task
	 * @return
	 */
	public List<AntiTask> getCancelledOccurances() {
		return cancelledOccurances;
	}
	
	public void setCancelledOccurance(AntiTask cancelledOccurance) {
		this.cancelledOccurance = cancelledOccurance;
	}
	
	public void setCancelledOccurances(List<AntiTask> cancelledOccurances) {
		this.cancelledOccurances = cancelledOccurances;
	}

	public void setEndDate(int endDate) {
		this.endDate = endDate;
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
	
	public void setFrequency(int frequency) {
		this.frequency = frequency;
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
