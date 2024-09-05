package taskalyn;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class TaskManager {
    private List<Task> tasks;
    private Database database;
    private Ui ui;

    public TaskManager(Database database, Ui ui) {
        this.database = database;
        this.ui = ui;
        this.tasks = new ArrayList<>(100);
        loadDatabase();
    }

    public void addTask(Task task) {
        tasks.add(task);
        ui.printLines("Got it, I've added this task to your list!\n" +
                "      " + task.toString() + "\n" + "    Wah bro... " + getTaskSize() + (getTaskSize() > 1 ? " tasks already!" : " task already!"));
        updateDatabase();
    }

    public void deleteTask(int taskId) {
        if (taskId <= tasks.size() + 1) {
            Task task = tasks.get(taskId - 1);
            tasks.remove(task);
            ui.printLines("Awesome bro! One task gone :D\n" +
                    "      " + task.toString() + "\n" + "    Wah bro... " + getTaskSize() + (getTaskSize() > 1 ? " tasks already!" : " task already!"));
        }
        updateDatabase();
    }

    public int getTaskSize() {
        return tasks.size();
    }

    public void listTasks() {
        String taskString = "Here are the tasks in your list:\n" + "    ";

        if (!tasks.isEmpty()) {
            for (int i = 0; i < tasks.size(); i++) {
                if (i != tasks.size() - 1) {
                    taskString += i + 1 + "." + tasks.get(i).toString() + "\n    ";
                }
                else {
                    taskString += i + 1 + "." + tasks.get(i).toString();
                }
            }
        }
        else {
            taskString += "Nothing!";
        }

        ui.printLines(taskString);
    }

    public void completeTask(int taskId) {
        if (taskId <= tasks.size() + 1) {
            tasks.get(taskId - 1).setComplete();
            ui.printLines("Nice, I've marked this task as complete:\n" +
                    "       " + tasks.get(taskId - 1).toString());
        }
        updateDatabase();
    }

    public void incompleteTask(int taskId) {
        if (taskId <= tasks.size() + 1) {
            tasks.get(taskId - 1).setIncomplete();
            ui.printLines("Ok, I've marked this task as incomplete:\n" +
                    "       " + tasks.get(taskId - 1).toString());
        }
        updateDatabase();
    }

    /**
     * Searches for tasks with the given keyword in their descriptions.
     *
     * @param keyword Keyword input from user.
     */
    public void searchTasksByKeyword(String keyword) {
        List<Task> matchedTasks = new ArrayList<>();
        for (Task task : tasks) {
            if (task.getTaskDescription().contains(keyword)) {
                matchedTasks.add(task);
            }
        }
        if (!matchedTasks.isEmpty()) {
            String matchingTasks = "Here are the matching tasks in your list:\n" + "    ";
            for (int i = 0; i < matchedTasks.size(); i++) {
                if (i != matchedTasks.size() - 1) {
                    matchingTasks += i + 1 + "." + matchedTasks.get(i).toString() + "\n    ";
                } else {
                    matchingTasks += i + 1 + "." + matchedTasks.get(i).toString();
                }
            }
            ui.printLines(matchingTasks);
        } else {
            ui.printLines("Aw... there are no matching tasks :(");
        }
    }

    /**
     * Loads database when TaskManager is initialised.
     */
    private void loadDatabase() {
        try {
            List<String> txtLines = this.database.readFromDatabase();
            for (String line : txtLines) {
                Task task = parseTaskFromString(line);
                tasks.add(task);
            }
        } catch (IOException e) {
            System.out.println("Error reading database: " + e.getMessage());
        }
    }

    /**
     * Parses type of task and creates a new Task object of that type.
     *
     * @param line String line from each line in database file.
     * @return A new TodoTask, DeadlineTask, or EventTask object.
     */
    private Task parseTaskFromString(String line) {
        String[] parts = line.split(" \\| ");
        if (parts.length >= 3) {
            String taskType = parts[0];
            boolean isCompleted = parts[1].equals("1");
            String taskInfo = parts[2];

            switch (taskType) {
            case "T":
                return new TodoTask(taskInfo, isCompleted);

            case "D":
                if (parts.length == 4) {
                    String date = parts[3];
                    return new DeadlineTask(taskInfo, date, isCompleted);
                }
                break;

            case "E":
                if (parts.length == 5) {
                    String toDate = parts[3];
                    String fromDate = parts[4];
                    return new EventTask(taskInfo, toDate, fromDate, isCompleted);
                }
                break;

            default:
            }
        }
        return null;
    }

    /**
     * Updates database with new task information.
     */
    private void updateDatabase() {
        List<String> txtLines = new ArrayList<>();
        for (Task task : tasks) {
            txtLines.add(task.toDatabaseFormat());
        }
        database.writeToDatabase(txtLines);
    }
}
