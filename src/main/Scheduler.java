package main;

import java.util.*;
import main.tasks.*;

public class Scheduler {
	private List<Task> tasks;
	
	public Scheduler() {
		this.tasks = new LinkedList<Task>();
	}
	
	//does not check for overlap if task extends into next day
	private boolean overlap(Task task) {
		float endTime = task.getStartTime() + task.getDuration();
		for(Task t: tasks) {
			if(t.getDate() == task.getDate()) {
				if(endTime > t.getStartTime()) {
					return true;
				}
			}
		}
		
		return false;
	}
	
	public void addTask(Task task) {
		if(overlap(task)) {
			System.out.println("Error: Task conflicts with existing tasks. Could not be added.");
			return;
		}
		
		int i = 0;
		for(Task t : tasks){
			if(task.getStartTime() < t.getStartTime()) {
				break;
			}
			i++;
		}
		
		tasks.add(i, task);
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
	
	public void deleteTask(Task task) {
		tasks.remove(task);
	}
	
	public void deleteTask(String taskName) {
		deleteTask(findTask(taskName));
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
}
