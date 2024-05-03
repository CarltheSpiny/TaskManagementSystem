package main.tasks;

public class TransientTask extends Task {
	TransientTasktype type;

	/**
	 * Default constructor to create a new transient task.
	 * @param taskName
	 * @param taskType the taskType from the list of valid types. Automatically turned into a string
	 * @param startDate
	 * @param startTime
	 * @param taskDuration
	 */
	public TransientTask(String taskName, TransientTasktype taskType, int startDate, float startTime, float taskDuration) {
		super(taskName, taskType.getId(), startDate, startTime, taskDuration);
		this.type = taskType;
	}
	
	@Override
	public String getType() {
		return this.type.getId();
	}
	
	@Override
	public void setType(String type) {
		TransientTasktype taskFromString = TransientTasktype.fromString(type);
		if (taskFromString != null)
			super.setType(taskFromString.getId());
		else
			this.type = TransientTasktype.NONE;
	}
	
	@Override
	public boolean isTaskValid() {
		if (this.type.equals(TransientTasktype.NONE))
			return false;
		else
			return super.isTaskValid();
	}
	
	/**
	 * An enum with constants representing the only valid types for transient
	 * tasks. 
	 * @author CTS
	 *
	 */
	public enum TransientTasktype {
		VISIT("Visit"),
		SHOPPING("Shopping"),
		APPOINTMENT("Appointment"),
		NONE("None");
		
		String id;
		
		private TransientTasktype(String taskID) {
			this.id = taskID;
		}
		
		public String getId() {
			return id;
		}
		
		public static TransientTasktype fromString(String type) {
	        for (TransientTasktype tasktype : TransientTasktype.values()) {
	            if (tasktype.id.toLowerCase() == type.toLowerCase()) {
	                return tasktype;
	            }
	        }
	        return null;
	    }
	}

}
