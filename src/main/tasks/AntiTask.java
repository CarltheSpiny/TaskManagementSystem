package main.tasks;

public class AntiTaskScheduler {
    private State currentState;

    public AntiTaskScheduler() {
        // Initial state: Null
        currentState = new NullState();
    }

    // Transition: Null -> Created
    public void createTask() {
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
