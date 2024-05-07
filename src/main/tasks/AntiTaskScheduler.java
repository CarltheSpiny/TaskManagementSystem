package main.tasks;

import java.time.LocalDate;
import java.time.LocalTime;

public class AntiTask extends Task {
    private State currentState;

    public AntiTask(String name, String type, LocalDate date, LocalTime startTime, float duration) {
        super(name, type, convertLocalDateToInt(date), convertLocalTimeToFloat(startTime), duration);
        currentState = new NullState(this);
    }

    // Transition: Null -> Created
    public void createTask() {
        currentState.createTask();
    }

    // Transition: Created -> Pending or Removed
    public void addToSchedule() {
        currentState.addToSchedule();
    }

    // Transition: Pending -> Removed or Match
    public void findMatchWithRecurringTask() {
        currentState.findMatchWithRecurringTask();
    }

    // Transition: Removed -> Null or Match -> Scheduled
    public void scheduled() {
        currentState.scheduled();
    }

    // Transition: Removed -> Null
    public void removed() {
        currentState.removed();
    }

    // Convert LocalDate to int format (YYYYMMDD)
    private static int convertLocalDateToInt(LocalDate date) {
        return Integer.parseInt(date.toString().replace("-", ""));
    }

    // Convert LocalTime to float format (24-hour format as a decimal)
    private static float convertLocalTimeToFloat(LocalTime time) {
        return time.getHour() + (time.getMinute() / 60.0f);
    }

    // Set current state
    public void setCurrentState(State state) {
        currentState = state;
    }
}

// Interface representing a state
interface State {
    void createTask();
    void addToSchedule();
    void findMatchWithRecurringTask();
    void scheduled();
    void removed();
}

// Null state
class NullState implements State {
    private AntiTask antiTask;

    public NullState(AntiTask antiTask) {
        this.antiTask = antiTask;
    }

    @Override
    public void createTask() {
        System.out.println("Anti-task created.");
        antiTask.setCurrentState(new CreatedState(antiTask));
    }

    @Override
    public void addToSchedule() {
        System.out.println("Error: Cannot add anti-task to schedule. Anti-task doesn't exist.");
    }

    @Override
    public void findMatchWithRecurringTask() {
        System.out.println("Error: Cannot find match for anti-task. Anti-task doesn't exist.");
    }

    @Override
    public void scheduled() {
        System.out.println("Error: Anti-task cannot be scheduled. Anti-task doesn't exist.");
    }

    @Override
    public void removed() {
        System.out.println("Error: Anti-task cannot be removed. Anti-task doesn't exist.");
    }
}

// Created state
class CreatedState implements State {
    private AntiTask antiTask;

    public CreatedState(AntiTask antiTask) {
        this.antiTask = antiTask;
    }

    @Override
    public void createTask() {
        System.out.println("Error: Anti-task already exists.");
    }

    @Override
    public void addToSchedule() {
        System.out.println("Anti-task added to schedule.");
        antiTask.setCurrentState(new PendingState(antiTask));
    }

    @Override
    public void findMatchWithRecurringTask() {
        System.out.println("Error: Cannot find match for anti-task. Anti-task not yet added to schedule.");
    }

    @Override
    public void scheduled() {
        System.out.println("Error: Anti-task cannot be scheduled. Anti-task is not in pending state.");
    }

    @Override
    public void removed() {
        System.out.println("Error: Anti-task cannot be removed. Anti-task is not in pending state.");
    }
}

// Pending state
class PendingState implements State {
    private AntiTask antiTask;

    public PendingState(AntiTask antiTask) {
        this.antiTask = antiTask;
    }

    @Override
    public void createTask() {
        System.out.println("Error: Anti-task already exists.");
    }

    @Override
    public void addToSchedule() {
        System.out.println("Error: Anti-task is already in the schedule.");
    }

    @Override
    public void findMatchWithRecurringTask() {
        System.out.println("No match found for anti-task. Anti-task removed from schedule.");
        antiTask.setCurrentState(new RemovedState(antiTask));
    }

    @Override
    public void scheduled() {
        System.out.println("Anti-task scheduled.");
        antiTask.setCurrentState(new ScheduledState(antiTask));
    }

    @Override
    public void removed() {
        System.out.println("Anti-task removed from schedule.");
        antiTask.setCurrentState(new RemovedState(antiTask));
    }
}

// Scheduled state
class ScheduledState implements State {
    private AntiTask antiTask;

    public ScheduledState(AntiTask antiTask) {
        this.antiTask = antiTask;
    }

    @Override
    public void createTask() {
        System.out.println("Error: Anti-task already exists.");
    }

    @Override
    public void addToSchedule() {
        System.out.println("Error: Anti-task is already in the schedule.");
    }

    @Override
    public void findMatchWithRecurringTask() {
        System.out.println("Error: Anti-task is already scheduled.");
    }

    @Override
    public void scheduled() {
        System.out.println("Error: Anti-task is already scheduled.");
    }

    @Override
    public void removed() {
        System.out.println("Error: Anti-task cannot be removed once scheduled.");
    }
}

// Removed state
class RemovedState implements State {
    private AntiTask antiTask;

    public RemovedState(AntiTask antiTask) {
        this.antiTask = antiTask;
    }

    @Override
    public void createTask() {
        System.out.println("Error: Anti-task already exists.");
    }

    @Override
    public void addToSchedule() {
        System.out.println("Error: Cannot add anti-task to schedule. Anti-task is removed.");
    }

    @Override
    public void findMatchWithRecurringTask() {
        System.out.println("Error: Cannot find match for anti-task. Anti-task is removed.");
    }

    @Override
    public void scheduled() {
        System.out.println("Error: Anti-task cannot be scheduled. Anti-task is removed.");
    }

    @Override
    public void removed() {
        System.out.println("Error: Anti-task already removed.");
    }
}
