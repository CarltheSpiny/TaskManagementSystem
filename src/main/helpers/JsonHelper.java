package main.helpers;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import main.tasks.RecurringTask;
import main.tasks.RecurringTask.RecurringTaskType;
import main.tasks.Task;
import main.tasks.TransientTask;
import main.tasks.TransientTask.TransientTaskType;

public class JsonHelper {
	
	public static final String DEFAULT_NAME = "schedule.json";
	public static final String DEFAULT_LOC = "";

	/**
	 * Parse JSON file without the help of a library. Does not validate tasks or input data.
	 * @param jsonContent the contents of the json file.
	 * @return
	 * @throws Exception When a task type could not be determined
	 */
	public static List<Task> parseJsonContent(String jsonContent) throws Exception {
	    String[] objects = jsonContent.split("\\},\\s*\\{");
	    List<Task> taskList = new ArrayList<>(objects.length);
	    for (String obj : objects) {
	        // Trim leading and trailing curly braces
	        obj = obj.replaceAll("^\\{|\\}$", "");
	        obj = obj.replace("[  {", "");
	        obj = obj.replace("}]", "");
	        obj = obj.trim();
	        String[] keyValuePairs = obj.split(",\\s*");
	        
	        TaskType taskType = TaskType.NONE;
            String name = "", type = "";
        	int date = 0, endDate = 0, frequency = 0;
        	float startTime = 0F, duration = 0F;
        	
	        for (String pair : keyValuePairs) {
	            // Split the key-value pair by colon
	            String[] keyValue = pair.split("\\s*:\\s*");
	            String key = keyValue[0].replaceAll("^\"|\"$", "");
	            key = key.substring(key.indexOf('"')+1);
	            String value = keyValue[1].replaceAll("^\"|\"$", "");
	            value = value.trim();
	            
	            // We switch on the key and can decide the task type with "type"
	            // We can also guarantee that the values will be read in this order
	            switch(key) {
		            case "Name":
		            	name = value;
		            	break;
		            case "Type":
		            	type = value;
		            	if (RecurringTaskType.fromString(value) != null) {
		            		taskType = TaskType.RECURRING;
		            	} else if(TransientTask.TransientTaskType.fromString(value) != null) {
		            		taskType = TaskType.TRANSIENT;
		            	} else if(value.equals("Cancellation")) {
		            		taskType = TaskType.ANTI_TASK;
		            	} else {
		            		// Not any of the 3 task types
		            	}
		            	break;
		            case "StartDate":
		            	date = Integer.parseInt(value);
		            	break;
		            case "StartTime":
		            	startTime = Float.parseFloat(value);
		            	break;
		            case "Duration":
		            	duration = Float.parseFloat(value);
		            	break;
		            case "EndDate":
		            	endDate = Integer.parseInt(value);
		            	break;
		            case "Frequency":
		            	frequency = Integer.parseInt(value);
		            	break;
	            	default:
	            		break;
	            }
	        }
	        if (taskType.equals(TaskType.NONE))
            	throw new Exception("Task type not defined");
            
            else if (taskType.equals(TaskType.TRANSIENT)) {
            	TransientTask transientTask = new TransientTask(name, TransientTaskType.fromString(type), date, startTime, duration);
            	taskList.add(transientTask);
            } else if (taskType.equals(TaskType.RECURRING)) {
            	RecurringTask recTask = new RecurringTask(name, RecurringTaskType.fromString(type), date, endDate, startTime, duration, frequency);
            	taskList.add(recTask);
            } else if (taskType.equals(TaskType.ANTI_TASK)) {
            	
            }
	    }
	    return taskList;
	}

	/**
	 * Read the contents of a JSON file all at once. Note: Set 1 and 2 have 
	 * 'tab' characters that may affect string replacements
	 * @param filePath
	 */
	public static String readJsonFile(String filePath) {
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            StringBuilder jsonContent = new StringBuilder();
            while ((line = reader.readLine()) != null) {
                jsonContent.append(line);
            }
            return jsonContent.toString();
        } catch (IOException e) {
            e.printStackTrace();
            return "Error";
        }
    }

	/**
	 * Constants that are used to determine what kind of Task to make in the
	 * {@link JsonHelper#parseJsonContent(String)} method. {@link TaskType#NONE} is used to show 
	 * a task from a file is wrong.
	 * @author CTS
	 *
	 */
    enum TaskType {
    	TRANSIENT("transient"),
    	RECURRING("recurring"),
    	ANTI_TASK("anti_task"),
    	NONE("none");
    	
    	String id;
    	
    	private TaskType(String taskId) {
    		this.id = taskId;
		}
    }
		
}
