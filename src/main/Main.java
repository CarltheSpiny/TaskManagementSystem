package main;

import main.tasks.RecurringTask;
import main.tasks.RecurringTask.RecurringTaskType;
import main.tasks.Task;
import main.tasks.TransientTask;
import main.tasks.TransientTask.TransientTasktype;

public class Main {

	public static void main(String[] args) {
		Task testTask = new TransientTask("Dummy Task", TransientTasktype.APPOINTMENT, 20240501, 12.0F, 1.0F);
		testTask.printTask();
		// testTask.prettyPrintDate();
		System.out.println("Task valid? " + testTask.isTaskValid());
		
		RecurringTask testRecTask = new RecurringTask("Dummy Rec Task", RecurringTaskType.CLASS, 20240501, 20240507, 10F, 2.0F, 7);
		testRecTask.printTask();
		System.out.println("Task valid? " + testRecTask.isTaskValid());
	}
}
