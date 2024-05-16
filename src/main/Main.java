package main;

import java.util.List;

import main.helpers.JsonHelper;
import main.tasks.AntiTask;
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
		System.out.println();
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
		List<Task> tasksFromFile = null;
		try {
			
			tasksFromFile = JsonHelper.parseJsonContent(JsonHelper.readJsonFile(SET_1_FILE_PATH));
			for (Task task : tasksFromFile) {
	            task.printTask();
	            System.out.println();
	        }
			
			Scheduler scheduler = new Scheduler(tasksFromFile);
			
			System.out.println("Scenario 1 Part 2: Delete task 'Intern Interview' from the schedule");
			scheduler.deleteTask("Intern Interview");
			System.out.println();
			System.out.println("Scenario 1 Part 3: Add task 'Intern Interview' to the schedule");
			/**
			 * Name: "Intern Interview"
			 * Type: "Appointment"
			 * Date: 20200427
			 * StartTime: 17
			 * Duration: 2.5
			 */
			TransientTask transientTask = new TransientTask("Intern Interview", TransientTaskType.APPOINTMENT, 20200427, 17F, 2.5F);
			try {
				scheduler.addTask(transientTask);
			} catch (Exception e) {
				e.printStackTrace();
				System.err.println("Could not add the Intern Interview Task");
			}
			System.out.println();
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
				System.err.println("Could not add Watch a moive Task");
			}
			System.out.println();
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
				System.err.println("Could not add Watch a moive Task");
			}
			
			System.out.println();
			System.out.println("Scenario 1 Part 6: Add tasks from Set 2 into this scheduler and fail due to time conflict");
			
			tasksFromFile = JsonHelper.parseJsonContent(JsonHelper.readJsonFile(SET_2_FILE_PATH));
			System.out.println("Tasks from Set2.json: ");
			for (Task task : tasksFromFile) {
	            task.printTask();
	            System.out.println();
	        }
			
			try {
				
				scheduler.addAllTasks(tasksFromFile);
			} catch (Exception e) {
				e.printStackTrace();
				System.err.println("Could not add the tasks to the schedule");
			}
			scheduler.writeSchedule("modifiedSet1");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void doScenario2() {
		System.out.println("*Scenario 2: 1. Read the file Set2.json. This should work.");
		try {
			List<Task> tasksFromFile;
			tasksFromFile = JsonHelper.parseJsonContent(JsonHelper.readJsonFile(SET_2_FILE_PATH));
			for (Task task : tasksFromFile) {
	            task.printTask();
	            System.out.println();
	        }
			
			Scheduler scheduler = new Scheduler(tasksFromFile);
			
			System.out.println("2. Add an anti-task. This should fail, it does not exactly match a recurring task.");
			/**
			 * Name: "Skip-out"
			 * Type: "Cancellation"
			 * Date: 20200430
		     * StartTime: 19.25
		     * Duration: .75
		     * This should fail, it does not exactly match a recurring task.
			 */
			AntiTask antiTask = new AntiTask("Skip-out", "Cancellation", 20200430, 19.25F, 0.75F);
			try {
				scheduler.addTask(antiTask);
			} catch (Exception e) {
				e.printStackTrace();
				System.err.println("Could not add the task");
			}
			System.out.println();
			
			System.out.println("3. Add an anti-task. This should work");
			/**
			 * Name: "Skip a meal"
		     * Type: "Cancellation"
			 * Date: 20200428
			 * StartTime: 17
			 * Duration: 1
			 * This should work
			 */
			AntiTask antiTask2 = new AntiTask("Skip a meal", "Cancellation", 20200428, 17f, 1f);
			scheduler.addTask(antiTask2);
			System.out.println();
			
			System.out.println("4. Read the file Set1.json. This should work.");
			tasksFromFile = JsonHelper.parseJsonContent(JsonHelper.readJsonFile(SET_1_FILE_PATH));
			System.out.println("Tasks from Set1.json: ");
			for (Task task : tasksFromFile) {
	            task.printTask();
	            System.out.println();
	        }
			
			try {
				scheduler.addAllTasks(tasksFromFile);
			} catch (Exception e) {
				e.printStackTrace();
				System.out.println("Could not add a task");
			}
			scheduler.writeSchedule("modifiedSet2");
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
	public static void testRecurringOverlap() {
		RecurringTask test1 = new RecurringTask("Play Fire Emblem", RecurringTaskType.WORK, 20240514, 20240529, 10.00f, 1f, 7);
		RecurringTask test2 = new RecurringTask("Summer work", RecurringTaskType.WORK, 20240521, 20240619, 9.00f, 2f, 7);
		Scheduler scheduler = new Scheduler();
		try {
			scheduler.addTask(test1);
			List<RecurringTask> allOccurances = ((RecurringTask) scheduler.findTask("Play Fire Emblem")).getAllFutureOccurances();
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


