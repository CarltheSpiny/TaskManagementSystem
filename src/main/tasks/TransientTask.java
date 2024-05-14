package main.tasks;

public class TransientTask extends Task {
	TransientTaskType type;

	/**
	 * Default constructor to create a new transient task.
	 * @param taskName a unique name
	 * @param taskType the taskType from the list of valid types. Automatically turned into a string
	 * @param startDate the start date in YYYYMMDD format
	 * @param startTime the start time in 24-hour format
	 * @param taskDuration the duration, rounded to the nearest 15 minutes, in decimal from
	 */
	public TransientTask(String taskName, TransientTaskType taskType, int startDate, float startTime, float taskDuration) {
		super(taskName, taskType.getId(), startDate, startTime, taskDuration);
		this.type = taskType;
	}
	
	/**
	 * Alternate constructor that takes a string instead of a TaskType. You should ensure the string matches the enum
	 * name correctly or refrences to this.task will break
	 * @param taskName a unique name
	 * @param taskType the taskType as a string. the {@link TransientTask#type} will be set to the matching enum
	 * @param startDate the start date in YYYYMMDD format
	 * @param startTime the start time in 24-hour format
	 * @param taskDuration the duration, rounded to the nearest 15 minutes, in decimal from
	 */
	public TransientTask(String taskName, String taskType, int startDate, float startTime, float taskDuration) {
		super(taskName, taskType, startDate, startTime, taskDuration);
		this.type = TransientTaskType.fromString(taskType);
	}
	
	/**
	 * Override of getType that returns the string representation of the enum
	 */
	@Override
	public String getType() {
		return this.type.getId();
	}
	
	/**
	 * The override of setType that uses a string to set the tasktype to a new one
	 */
	@Override
	public void setType(String type) {
		TransientTaskType taskFromString = TransientTaskType.fromString(type);
		if (taskFromString != null)
			super.setType(taskFromString.getId());
		else
			this.type = TransientTaskType.NONE;
	}
	
	public TransientTaskType getTransientType() {
		return this.type;
	}
	
	public void setType(TransientTaskType taskType) {
		this.type = taskType;
	}
	
	/**
	 * The override for isTaskValid that also checks that the taskType
	 * is a type that isn't none, which indicates the type is erroneous.
	 */
	@Override
	public boolean isTaskValid() {
		if (this.type.equals(TransientTaskType.NONE)) {
			System.err.println("Task Type is invalid!");
			return false;
		}
		else
			return super.isTaskValid();
	}
	
	/**
	 * An enum with constants representing the only valid types for transient
	 * tasks. 
	 * @author CTS
	 *
	 */
	public enum TransientTaskType {
		VISIT("Visit"),
		SHOPPING("Shopping"),
		APPOINTMENT("Appointment"),
		NONE("None");
		
		String id;
		
		private TransientTaskType(String taskID) {
			this.id = taskID;
		}
		
		public String getId() {
			return id;
		}
		
		/**
		 * Get the transientTaskType from a string, assuming the string is spelled exactly like
		 * the id in the enum constant
		 * @param type
		 * @return
		 */
		public static TransientTaskType fromString(String type) {
	        for (TransientTaskType tasktype : TransientTaskType.values()) {
	        	 if (tasktype.getId().equals(type)) {
		                return tasktype;
		            }
	        }
	        return TransientTaskType.NONE;
	    }
	}

}
