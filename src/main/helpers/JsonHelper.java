package main.helpers;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InvalidObjectException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import main.tasks.AntiTask;
import main.tasks.RecurringTask;
import main.tasks.RecurringTask.RecurringTaskType;
import main.tasks.Task;
import main.tasks.TransientTask;
import main.tasks.TransientTask.TransientTaskType;

public class JsonHelper {
	
	public static final String DEFAULT_NAME = "schedule.json";
	private static final String DEFAULT_TASK_OUT = "../TaskManagementSystem/src/resources/";
	private static List<AntiTask> antiTaskHolder;

	/**
	 * Parse JSON file without the help of a library. Does not validate tasks or input data.
	 * @param jsonContent the contents of the json file.
	 * @return
	 * @throws Exception When a task type could not be determined
	 */
	public static List<Task> parseJsonContent(String jsonContent) throws Exception {
		antiTaskHolder = new ArrayList<>();
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
		            	} else if(TransientTask.TransientTaskType.fromString(value) != TransientTaskType.NONE) {
		            		taskType = TaskType.TRANSIENT;
		            	} else if(value.equals("Cancellation")) {
		            		taskType = TaskType.ANTI_TASK;
		            	} else {
		            		// Not any of the 3 task types
		            	}
		            	break;
		            case "StartDate":
		            case "Date":
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
            	if (!antiTaskHolder.isEmpty()) {
            		for (int i = 0; i < antiTaskHolder.size(); i++) {
            			AntiTask antiTask = antiTaskHolder.get(i);
            			if (recTask.overlapsWith(antiTask)) {
            				recTask.addAntiTask(antiTask);
            				antiTaskHolder.remove(i);
            			}
            		}
            	}
            	taskList.add(recTask);
            } else if (taskType.equals(TaskType.ANTI_TASK)) {
            	AntiTask antiTask = new AntiTask(name, "Cancellation", date, startTime, duration);
            	// anti-tasks are linked to existing reccuring tasks but are technically not a task
            	// when reading from a file, the anti-task may come before a recurring task
            	// we should hold on to it before adding it so we can attach it to the right task
            	if (!taskList.isEmpty()) {
            		// there are tasks in this list
            		for (Task task$iterator : taskList) {
            			if (task$iterator instanceof RecurringTask reccurringTask$iterator) {
            				if (reccurringTask$iterator.overlapsWith(antiTask)) {
            					reccurringTask$iterator.addAntiTask(antiTask);
            					break;
            				}
            					
            			} else {
            				addAntiTask(antiTask);
            			}
            		}
            		
            	} else {
            		// no tasks exisit yet, we should wait until a task is added
            		addAntiTask(antiTask);
            	}
            }
	    }
	    return taskList;
	}
	
	/**
	 * Write the schedule taskList into a file. This method does not need the outputPath 
	 * and defaults to {@link JsonHelper#DEFAULT_TASK_OUT}.
	 * @param fileOutput the name of the file (without extension)
	 * @param taskList the taskList from the scheduler
	 * @return true if the tasks were witten
	 * @throws IOException
	 */
	public static boolean writeToJson(String fileOutput, List<Task> taskList) throws IOException {
		return writeToJson(fileOutput, DEFAULT_TASK_OUT, taskList);
	}
	
	/**
	 * Write the schedule into a file. You can specify the output path with this method.
	 * @param fileOutName the name of the file (without extension)
	 * @param fileOutputPath the output path where the file will be stored
	 * @param taskList the task list from the schedule
	 * @return true if the task list was written to the file
	 */
	public static boolean writeToJson(String fileOutName, String fileOutputPath, List<Task> taskList) {
		// we assume the task is validated, lets start printing out
		 createOutDirectory(fileOutputPath);
		 
		 try (FileWriter fw = new FileWriter(fileOutputPath + "/" + fileOutName + ".json")) {
			 fw.write("[\n");
			 int count = taskList.size();
			 int iterCount = 0;
			 for (Task task$iteator : taskList) {
				 String taskString = "\t" + convertTaskToJson(task$iteator);
				 if (iterCount < count - 1) {
					 taskString += ",\n";
				 }
				 fw.write(taskString);
				 iterCount++;
			 }
			 fw.write("\n]");
			 System.out.println("Schedule has been written to " + fileOutputPath + "/" + fileOutName + ".json");
			 return true;
		 } catch (Exception e) {
			 e.printStackTrace();
			 return false;
		 }
		
	}
	
	/**
	 *  Create the passed output directory if the path does not already exist
	 * @param outPath
	 * @return
	 */
	private static boolean createOutDirectory(String outPath) {
		try {
			Files.createDirectories(Paths.get(outPath));
			return true;
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
	}
	
	/**
	 * Converts the passed task into a JSON object. No need to cast the Task as it's actual type
	 * @param task the task that will be turned into a sting JSON object
	 * @return the string with the '{' and the '}'
	 * @throws InvalidObjectException
	 */
	private static String convertTaskToJson(Task task) throws InvalidObjectException {
		if (task instanceof TransientTask transientTask) {
			String taskName = "\t\t\"Name\":" + "\"" + transientTask.getName() + "\",\n";
			String taskType = "\t\t\"Type\":" + "\"" + transientTask.getType() + "\",\n";
			String startDate = "\t\t\"Date\":" + "\"" + transientTask.getDate() + "\",\n";
			String startTime = "\t\t\"StartTime\":" + "\"" + transientTask.getStartTime() + "\",\n";
			String duration = "\t\t\"Duration\":" + "\"" + transientTask.getDuration() + "\"\n";
			return "{\n" + taskName + taskType + startDate + startTime + duration + "\t}";
			
		} else if (task instanceof RecurringTask recurringTask) {
			String taskName = "\t\t\"Name\":" + "\"" + recurringTask.getName() + "\",\n";
			String taskType = "\t\t\"Type\":" + "\"" + recurringTask.getType() + "\",\n";
			String startDate = "\t\t\"StartDate\":" + "\"" + recurringTask.getDate() + "\",\n";
			String startTime = "\t\t\"StartTime\":" + "\"" + recurringTask.getStartTime() + "\",\n";
			String duration = "\t\t\"Duration\":" + "\"" + recurringTask.getDuration() + "\",\n";
			String endDate = "\t\t\"EndDate\":" + "\"" + recurringTask.getEndDate() + "\",\n";
			String frequency = "\t\t\"Frequency\":" + "\"" + recurringTask.getFrequency() + "\"\n";
			return "{\n" + taskName + taskType + startDate + startTime + duration + endDate + frequency + "\t}";
			
		} else {
			throw new InvalidObjectException("Task could not be added");
		}
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
	
	public static boolean addAntiTask(AntiTask antiTask) {
		System.out.println("Anti-Task did not have matching recurring task, adding to holder");
		return antiTaskHolder.add(antiTask);
	}
	
	public static void clearAntiTaskList() {
		antiTaskHolder.clear();
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
