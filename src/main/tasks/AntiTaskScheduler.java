package main.tasks;

import java.time.LocalDate;
import java.time.LocalTime;

public class AntiTaskScheduler {
    private State currentState;
    private AntiTask antiTask;

    public AntiTaskScheduler() {
        // Initial state: Null
        currentState = new NullState();
    }

    // Transition: Null -> Created
    public void createTask(String name, String type, LocalDate date, LocalTime startTime, int duration) {
        antiTask = new AntiTask(name, type, date, startTime, duration);
        currentState = currentState.createTask();
    }

    // Transition: Created -> Pending or Removed
    public void addToSchedule() {
        currentState = currentState.addToSchedule();
    }

    // Transition: Pending -> Removed or Match
    public void findMatchWithRecurringTask() {
        currentState = currentState.findMatchWithRecurringTask();
    }

    // Transition: Removed -> Null
    public void removed() {
        currentState = currentState.removed();
    }

    // Transition: Match -> Scheduled
    public void scheduled() {
        currentState = currentState.scheduled();
    }
}

// Interface representing a state
interface State {
    State createTask();
    State addToSchedule();
    State findMatchWithRecurringTask();
    State scheduled();
    State removed();
}

// Null state
class NullState implements State {
    @Override
    public State createTask() {
        System.out.println("Anti-task created.");
        return new CreatedState();
    }

    @Override
    public State addToSchedule() {
        System.out.println("Error: Cannot add anti-task to schedule. Anti-task doesn't exist.");
        return this;
    }

    @Override
    public State findMatchWithRecurringTask() {
        System.out.println("Error: Cannot find match for anti-task. Anti-task doesn't exist.");
        return this;
    }

    @Override
    public State scheduled() {
        System.out.println("Error: Anti-task cannot be scheduled. Anti-task doesn't exist.");
        return this;
    }

    @Override
    public State removed() {
        System.out.println("Error: Anti-task cannot be removed. Anti-task doesn't exist.");
        return this;
    }
}

// Created state
class CreatedState implements State {
    @Override
    public State createTask() {
        System.out.println("Error: Anti-task already exists.");
        return this;
    }

    @Override
    public State addToSchedule() {
        System.out.println("Anti-task added to schedule.");
        return new PendingState();
    }

    @Override
    public State findMatchWithRecurringTask() {
        System.out.println("Error: Cannot find match for anti-task. Anti-task not yet added to schedule.");
        return this;
    }

    @Override
    public State scheduled() {
        System.out.println("Error: Anti-task cannot be scheduled. Anti-task is not in pending state.");
        return this;
    }

    @Override
    public State removed() {
        System.out.println("Error: Anti-task cannot be removed. Anti-task is not in pending state.");
        return this;
    }
}

// Pending state
class PendingState implements State {
    @Override
    public State createTask() {
        System.out.println("Error: Anti-task already exists.");
        return this;
    }

    @Override
    public State addToSchedule() {
        System.out.println("Error: Anti-task is already in the schedule.");
        return this;
    }

    @Override
    public State findMatchWithRecurringTask() {
        System.out.println("No match found for anti-task. Anti-task removed from schedule.");
        return new RemovedState();
    }

    @Override
    public State scheduled() {
        System.out.println("Anti-task scheduled.");
        return new ScheduledState();
    }

    @Override
    public State removed() {
        System.out.println("Anti-task removed from schedule.");
        return new RemovedState();
    }
}

// Scheduled state
class ScheduledState implements State {
    @Override
    public State createTask() {
        System.out.println("Error: Anti-task already exists.");
        return this;
    }

    @Override
    public State addToSchedule() {
        System.out.println("Error: Anti-task is already in the schedule.");
        return this;
    }

    @Override
    public State findMatchWithRecurringTask() {
        System.out.println("Error: Anti-task is already scheduled.");
        return this;
    }

    @Override
    public State scheduled() {
        System.out.println("Error: Anti-task is already scheduled.");
        return this;
    }

    @Override
    public State removed() {
        System.out.println("Error: Anti-task cannot be removed once scheduled.");
        return this;
    }
}

// Removed state
class RemovedState implements State {
    @Override
    public State createTask() {
        System.out.println("Error: Anti-task already exists.");
        return this;
    }

    @Override
    public State addToSchedule() {
        System.out.println("Error: Cannot add anti-task to schedule. Anti-task is removed.");
        return this;
    }

    @Override
    public State findMatchWithRecurringTask() {
        System.out.println("Error: Cannot find match for anti-task. Anti-task is removed.");
        return this;
    }

    @Override
    public State scheduled() {
        System.out.println("Error: Anti-task cannot be scheduled. Anti-task is removed.");
        return this;
    }

    @Override
    public State removed() {
        System.out.println("Error: Anti-task already removed.");
        return this;
    }
}

class AntiTask {
    private String name;
    private String type;
    private LocalDate date;
    private LocalTime startTime;
    private int duration;

    public AntiTask(String name, String type, LocalDate date, LocalTime startTime, int duration) {
        this.name = name;
        this.type = type;
        this.date = date;
        this.startTime = startTime;
        this.duration = duration;
    }

    // Getters and setters
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public LocalTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalTime startTime) {
        this.startTime = startTime;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }
}

