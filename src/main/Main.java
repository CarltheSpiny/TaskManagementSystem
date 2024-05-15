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
	private static final String DEFAULT_SCHEDULE = "../TaskManagementSystem/src/resources/schedule.json";

	public static void main(String[] args) {
		testRecurringOverlap();
		System.out.println();
		//doScenario1();
		System.out.println();
		//doScenario2();
		
		
	}
	
	/**
	 * Do the tasks outline in Scenario 1 on Canvas
	 */
	public static void doScenario1() {
		System.out.println("*Scenario 1 Part 1: read contents of set1.json\n");
		// TODO add the scheduler version that also checks the validity of these tasks
		List<Task> scheduleFile = null;
		try {
			
			scheduleFile = JsonHelper.parseJsonContent(JsonHelper.readJsonFile(SET_1_FILE_PATH));
			for (Task task : scheduleFile) {
	            task.printTask();
	            System.out.println();
	        }
			
			Scheduler scheduler = new Scheduler(scheduleFile);
			
			System.out.println("Scenario 1 Part 2: Delete task 'Intern Interview' from the schedule");
			scheduler.deleteTask("Intern Interview");
			
			System.out.println("Scenario 1 Part 3: Add task 'Intern Interview' to the schedule");
			/**
			 * Name: "Intern Interview"
			 * Type: "Appointment"
			 * Date: 20200427
			 * StartTime: 17
			 * Duration: 2.5
			 */
			TransientTask transientTask = new TransientTask("Intern Interview", TransientTaskType.APPOINTMENT, 20200427, 17F, 2.5F);
			scheduler.addTask(transientTask);
			
			System.out.println("Scenario 1 Part 4: Add transient Task 'Watch a moive' and fail (No moive type)");
			/**
			 * Name: "Watch a movie"
			 * Type: "Movie"
			 * Date: 20200429
			 * StartTime: 21.5
			 * Duration: 2
			 */
			TransientTask transientTask2 = new TransientTask("Watch a moive", "Movie", 20200429, 21.5F, 2F);
			try {
				// handle here to avoid entire block stopping
				scheduler.addTask(transientTask2);
			} catch (Exception e) {
				e.printStackTrace();
			}
			System.out.println("Scenario 1 Part 5: Add transient Task 'Watch a moive' and fail due to time conflict");
			/**
			 * Name: "Watch a movie"
			 * Type: "Visit"
			 * Date: 20200430
			 * StartTime: 18.5
			 * Duration: 2
			 */
			TransientTask transientTask3 = new TransientTask("Watch a moive", "Visit", 20200430, 18.5F, 2f);
			try {
				scheduler.addTask(transientTask3);
			} catch (Exception e) {
				e.printStackTrace();
				System.err.println("Error caused by: ");
				scheduler.getOverlapTask().printTask();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
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
	
	public static void testRecurringOverlap() {
		RecurringTask test1 = new RecurringTask("Play Fire Emblem", RecurringTaskType.WORK, 20240514, 20240529, 10.00f, 1f, 7);
		RecurringTask test2 = new RecurringTask("Summer work", RecurringTaskType.WORK, 20240521, 20240619, 9.00f, 2f, 7);
		Scheduler scheduler = new Scheduler();
		try {
			scheduler.addTask(test1);
			List<RecurringTask> allOccurances = ((RecurringTask) scheduler.findTask("Play Fire Emblem")).getAllOccurances();
			System.out.println("Printing all occurances of the task and if it overlaps:");
			for (RecurringTask recurringTask$iterator : allOccurances) {
				recurringTask$iterator.overlapsWith(test2);
				recurringTask$iterator.printTask();
				
			}
			//scheduler.addTask(test2);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}


