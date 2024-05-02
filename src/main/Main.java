package main;

public class Main {

	public static void main(String[] args) {
		Task testTask = new Task("Dummy Task", "debug", 20240501, 12.0F, 1.0F);
		testTask.printTask();
		testTask.prettyPrintDate();
	}
}
