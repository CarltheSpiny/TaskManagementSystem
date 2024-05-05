package main;

import java.util.List;

import main.helpers.JsonHelper;
import main.tasks.RecurringTask;
import main.tasks.RecurringTask.RecurringTaskType;
import main.tasks.Task;
import main.tasks.TransientTask;
import main.tasks.TransientTask.TransientTaskType;

public class Main {
	
	public static final String SET_1_FILE_PATH = "../TaskManagementSystem/src/main/set1.json";
	public static final String SET_2_FILE_PATH = "../TaskManagementSystem/src/main/set2.json";

	public static void main(String[] args) {
		Task testTask = new TransientTask("Dummy Task", TransientTaskType.APPOINTMENT, 20240501, 12.0F, 1.0F);
		testTask.printTask();
		System.out.println(RecurringTask.RecurringTaskType.fromString("Class"));
		// testTask.prettyPrintDate();
		System.out.println("Task valid? " + testTask.isTaskValid());
		
		RecurringTask testRecTask = new RecurringTask("Dummy Rec Task", RecurringTaskType.CLASS, 20240501, 20240507, 10F, 2.0F, 7);
		testRecTask.printTask();
		System.out.println("Task valid? " + testRecTask.isTaskValid());
		
		doScenario1();
		System.out.println();
		doScenario2();
		
		
	}
	
	/**
	 * Do the tasks outline in Scenario 1 on Canvas
	 */
	public static void doScenario1() {
		System.out.println("*Scenario 1 Part 1: read contents of set1.json\n");
		// TODO add the scheduler version that also checks the validity of these tasks
		try {
			List<Task> scheduleFile;
			scheduleFile = JsonHelper.parseJsonContent(JsonHelper.readJsonFile(SET_1_FILE_PATH));
			for (Task task : scheduleFile) {
	            task.printTask();
	            System.out.println();
	        }
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		
		
		System.out.println("Scenario 1 Part 2: Delete task 'Intern Interview' from the schedule");
		
		System.out.println("Scenario 1 Part 3: Add task 'Intern Interview' to the schedule");
		
		System.out.println("Scenario 1 Part 4: Add transient Task 'Watch a moive' and fail (No moive type)");
		
		System.out.println("Scenario 1 Part 5: Add transient Task 'Watch a moive' and fail due to time conflict");
	}
	
	public static void doScenario2() {
		System.out.println(" 1. Read the file Set2.json. This should work.");
		try {
			List<Task> scheduleFile;
			scheduleFile = JsonHelper.parseJsonContent(JsonHelper.readJsonFile(SET_2_FILE_PATH));
			for (Task task : scheduleFile) {
	            task.printTask();
	            System.out.println();
	        }
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println("2. Add an anti-task");
		/**
		 * Name: "Skip-out"
		 * Type: "Cancellation"
		 * Date: 20200430
	     * StartTime: 19.25
	     * Duration: .75
	     * This should fail, it does not exactly match a recurring task.
		 */
		System.out.println("3. Add an anti-task");
		/**
		 * Name: "Skip a meal"
	     * Type: "Cancellation"
		 * Date: 20200428
		 * StartTime: 17
		 * Duration: 1
		 * This should work
		 */
		System.out.println("4. Read the file Set1.json. This should work.");
	}
}
