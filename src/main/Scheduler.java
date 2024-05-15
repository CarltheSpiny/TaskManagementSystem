package main;

import java.io.IOException;
import java.util.*;

import main.helpers.JsonHelper;
import main.tasks.*;
import main.tasks.RecurringTask.RecurringTaskType;
import main.tasks.TransientTask.TransientTaskType;

public class Scheduler {
	private List<Task> tasks;
	private Task overlapConflict;
	
	public Scheduler() {
		this.tasks = new LinkedList<Task>();
		this.overlapConflict = null;
	}
	
	public Scheduler(List<Task> taskList) {
		this.tasks = taskList;
		this.overlapConflict = null;
	}
	
	/**
	 * Checks for overlap
	 * Note: does not check for overlap if task extends into next day
	 * Note2: prof said that this is fine if we force all tasks to never
	 * go past midnight
	 * @param task
	 * @return
	 */
	private boolean overlap(Task task) {
		float endTime = task.getStartTime() + task.getDuration();
		for(Task t: tasks) {
			if(t.getDate() == task.getDate()) {
				if(endTime > t.getStartTime()) {
					this.overlapConflict = t;
					return true;
				}
			}
			
			if (t instanceof RecurringTask reccuringTask) {
				reccuringTask.getNextOccurrance();
			}
		}
		
		return false;
	}
	
	/**
	 * returns the task that caused the overlap fail in
	 * {@link Scheduler#overlap(Task)}. Resets the field 
	 * to null until called again
	 * @return
	 */
	public Task getOverlapTask() {
		return this.overlapConflict;
	}
	
	/**
	 * Add a task using the CLI. The only check here is TaskType
	 * and a corresponding reccurring task for Anti-Task
	 * @return
	 * @throws Exception
	 */
	public boolean addTask() throws Exception {
		System.out.println("Adding a Task Interface");
		System.out.println("Please select what kind of task you will create:");
		Scanner scnr = new Scanner(System.in);
		System.out.println("T for Transient Task, R for Recurring Task, A for Anti-Task:");
		String usertasktype = scnr.nextLine();
		
		String name;
		String taskType;
		int startDate;
		float startTime;
		int endDate;
		float duration;
		
		switch(usertasktype.toLowerCase()) {
		case "t":
			System.out.println("Transient Task selected");
			System.out.println("Please provide the following details:");
			System.out.println("Name for task:");
			name = scnr.nextLine();
			System.out.println("Task type (valid types: " + Arrays.toString(TransientTaskType.values()) + "):");
			taskType = scnr.nextLine();
			TransientTask.TransientTaskType type = TransientTaskType.fromString(taskType);
			if (type.equals(TransientTaskType.NONE))
				throw new Exception("Task type is invalid");
			System.out.println("The start date in YYYYMMDD:");
			startDate = scnr.nextInt();
			System.out.println("The start time in 24-hour format:");
			startTime = scnr.nextFloat();
			System.out.println("The duration of the task: ");
			duration = scnr.nextFloat();
			TransientTask transientTask = new TransientTask(name, type, startDate, startTime, duration);
			this.addTask(transientTask);
			break;
		case "r":
			System.out.println("Reccuring Task selected");
			System.out.println("Please provide the following details:");
			System.out.println("Name for task:");
			name = scnr.nextLine();
			System.out.println("Task type (valid types: " + Arrays.toString(RecurringTaskType.values()) + "):");
			taskType = scnr.nextLine();
			RecurringTaskType recurringType = RecurringTaskType.fromString(taskType);
			if (recurringType.equals(RecurringTaskType.NONE))
				throw new Exception("Task type is invalid");
			System.out.println("The start date in YYYYMMDD:");
			startDate = scnr.nextInt();
			System.out.println("The end date in YYYYMMDD:");
			endDate = scnr.nextInt();
			System.out.println("The start time in 24-hour format:");
			startTime = scnr.nextFloat();
			System.out.println("The duration of the task: ");
			duration = scnr.nextFloat();
			System.out.println("The frequency of the task (1 for daily, 7 for weekly):");
			int frequency = scnr.nextInt();
			RecurringTask recurringTask = new RecurringTask(name, recurringType, startDate, endDate, startTime, duration, frequency);
			this.addTask(recurringTask);
			break;
		case "a":
			break;
		default:
			break;
		}
		return true;
	}
	
	public void addTask(Task newTask) throws Exception {
		// Check for overlap
		if(overlap(newTask)) {
			System.err.println("Error: Task conflicts with existing tasks. Could not be added.");
			return;
		}
		
		int i = 0;
		for(Task existingTask : tasks){
			/**
				if(newTask.getStartTime() < existingTask.getStartTime()) {
				// validation: is task startTime less than currentTask
				System.err.println("");
				break;
			}
		 	**/
			System.out.println("Validating task");
			// Catch any malformed task
			if (!newTask.isTaskValid()) {
				throw new Exception("Task is invalid");
			}
			
			if (existingTask instanceof RecurringTask recurring$iterator)
				if (newTask instanceof RecurringTask reccuringTask) {
					if (reccuringTask.overlapsWith(recurring$iterator)) {
						throw new Exception("New Task overlaps with an Existing recurring task instance");
					}
				}
			
			
			i++;
		}
		
		tasks.add(i, newTask);
		System.out.println("Added new task: " + newTask.getName());
	}
	
	public void addAllTasks(List<Task> taskList) throws Exception {
		for (Task task$iterator : taskList) {
			this.addTask(task$iterator);
		}
	}
	
	public Task findTask(String taskName) {
		for(Task t: tasks) {
			if(t.getName().equals(taskName)) {
				return t;
			}
		}
		
		System.out.println("Error: Task not found.");
		return null;
	}
	
	public void viewTask(String taskName) {
		findTask(taskName).printTask();
	}
	
	public boolean deleteTask(Task task) {
		return tasks.remove(task);
	}
	
	public void deleteTask(String taskName) {
		if (deleteTask(findTask(taskName)))
			System.out.println("Successfuly deleted the task");
			else
				System.out.println("Error in deleting task");
	}
	
	public void editTask(Task task) {
		Scanner sc = new Scanner(System.in);
		boolean cont = true;
		int choice;
		
		while(cont) {
			System.out.println("Select aspect of task to edit:");
			System.out.println("1. Name");
			//System.out.println("2. Type");
			System.out.println("3. Date");
			System.out.println("4. Time and Duration");
			System.out.println("5. Exit");
			System.out.println("Enter choice: ");
			choice = sc.nextInt();
			sc.nextLine();
			
			switch(choice) {
			//edit name
			case 1:
				System.out.print("Enter new name: ");
				task.setName(sc.nextLine());
				break;
				
			/*
			//edit type. 
			case 2:
				System.out.println("Enter new type")
			*/
				
			case 3:
				System.out.print("Enter new date (YYYYMMDD): ");
				task.setDate(sc.nextInt());
				break;
				
			case 4: 
				
				float oldStart = task.getStartTime();
				float oldDuration = task.getDuration();
				System.out.print("Enter new start time: ");
				task.setStartTime(sc.nextFloat());
				System.out.print("Enter new duration: ");
				task.setDuration(sc.nextFloat());
				
				if(overlap(task)) {
					task.setStartTime(oldStart);
					task.setDuration(oldDuration);
					System.out.println("Error: New time causes overlap. New times not saved.");
				}
				
				break;
				
			case 5:
				cont = false;
				break;
				
			default:
				System.out.println("Invalid choice");
			}
		}
	}
	
	public void editTask(String taskName) {
		editTask(findTask(taskName));
	}
	
	public void printTaskList() {
		for (Task task$iterator : this.tasks) {
			if (task$iterator instanceof TransientTask transienttask) {
				transienttask.printTask();
			}
			
			if (task$iterator instanceof RecurringTask recurringTask) {
				recurringTask.printTask();
			}
			
		}
	}
	
	/**
	 * write the current task list to a json file. You can specify
	 * the name with this method.
	 * @param outputFileName
	 */
	public void writeSchedule(String outputFileName) {
		try {
			JsonHelper.writeToJson(outputFileName, tasks);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Write the current task list to a json file. The default
	 * file name will be 'schedule.json'.
	 */
	public void writeSchedule() {
		writeSchedule("schedule");
	}
}
