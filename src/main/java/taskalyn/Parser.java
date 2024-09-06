package taskalyn;

/**
 * Manages scanning of input and parsing of commands.
 */
public class Parser {
    private TaskManager taskManager;
    private Ui ui;

    /**
     * Constructs the Parser object with ui and taskmanager.
     *
     * @param ui Ui object to manage user interaction.
     * @param taskManager TaskManager object to manage tasks.
     */
    public Parser(Ui ui, TaskManager taskManager) {
        this.ui = ui;
        this.taskManager = taskManager;
    }

    /**
     * Parses user input commands and handles task management.
     *
     * @param taskManager TaskManager object to manage tasks.
     * @return {@code true} to allow inputs, {@code false} to stop parsing.
     */
    public String parse(TaskManager taskManager, String input) {
        while (true) {
            String[] items = input.split(" ", 2);
            String command = items[0];

            try {
                switch (command) {
                case "bye":
                    return ui.showByeMessage();
                case "list":
                    return taskManager.listTasks();
                case "find":
                    if (items.length != 2) {
                        throw new CommandFormatException("Aw... find command must have just 2 arguments: the command, and the keyword.");
                    } else {
                        return taskManager.searchTasksByKeyword(items[1]);
                    }
                case "delete":
                    if (items.length != 2) {
                        throw new CommandFormatException("Aw... delete command must have just 2 arguments: the " +
                                "command, and the task number.");
                    }
                    try {
                        Integer i = Integer.parseInt(items[1]);
                        if (i > 0 && i < taskManager.getTaskSize() + 1) {
                            return taskManager.deleteTask(i);
                        } else {
                            throw new NoSuchTaskException("Aw, that task doesn't exist. Try again!");
                        }
                    } catch (NumberFormatException e) {
                        throw new CommandFormatException("Aw... delete command must be followed by an integer");
                    } catch (IndexOutOfBoundsException e) {
                        throw new NoSuchTaskException("Aw, that task doesn't exist. Try again!");
                    } catch (Exception e) {
                        throw new CommandFormatException("Aw... delete command must have just 2 arguments: the " +
                                "command, and the task number.");
                    }
                case "mark":
                    if (items.length != 2) {
                        throw new CommandFormatException("Aw... mark command must have just 2 arguments: the command," +
                                " and the task number.");
                    }
                    try {
                        Integer i = Integer.parseInt(items[1]);
                        if (i > 0 && i <= taskManager.getTaskSize() + 1) {
                            return taskManager.markTaskAsComplete(i);
                        } else {
                            throw new NoSuchTaskException("Aw, that task doesn't exist. Try again!");
                        }
                    } catch (NumberFormatException e) {
                        throw new CommandFormatException("Aw... mark command must be followed by an integer");
                    } catch (IndexOutOfBoundsException e) {
                        throw new NoSuchTaskException("Aw, that task doesn't exist. Try again!");
                    }
                case "unmark":
                    if (items.length != 2) {
                        throw new CommandFormatException("Aw... unmark command must have 2 arguments: the command and" +
                                " the task number.");
                    }
                    try {
                        Integer i = Integer.parseInt(items[1]);
                        if (i > 0 && i <= taskManager.getTaskSize() + 1) {
                            return taskManager.markTaskAsIncomplete(i);
                        } else {
                            throw new NoSuchTaskException("Aw, that task doesn't exist. Try again!");
                        }
                    } catch (NumberFormatException e) {
                        throw new CommandFormatException("Aw... unmark command must be followed by an integer");
                    } catch (IndexOutOfBoundsException e) {
                        throw new NoSuchTaskException("Aw, that task doesn't exist. Try again!");
                    }
                case "todo":
                    if (items.length != 2) {
                        throw new CommandFormatException("Aw... todo command must contain 2 arguments: todo and the " +
                                "task at hand!");
                    }
                    if (items[1].equals("")) {
                        throw new CommandFormatException("Aw... task description cannot be empty!");
                    }
                    try {
                        return taskManager.addTask(new TodoTask(items[1], false));
                    } catch (Exception e) {
                        throw new CommandFormatException("Aw... todo command must contain 2 arguments: todo and the " +
                                "task at hand!");
                    }
                case "deadline":
                    if (items.length != 2) {
                        throw new CommandFormatException("Aw... your deadline command is incomplete. Try this: " +
                                "deadline {task} /by {yyyy-MM-dd HHmm}");
                    }
                    try {
                        if (items[1].contains("/by")) {
                            String[] deadlineString = items[1].split(" /by ", 2);
                            if (deadlineString.length == 2) {
                                String datePattern = "\\d{4}-\\d{2}-\\d{2} \\d{4}";
                                if (deadlineString[1].matches(datePattern)) {
                                    return taskManager.addTask(new DeadlineTask(deadlineString[0], deadlineString[1], false));
                                } else {
                                    throw new CommandFormatException("Aw... the date format must be yyyy-MM-dd HHmm");
                                }
                            } else {
                                throw new CommandFormatException("Aw... your deadline command must contain the task, " +
                                        "/by, and the deadline.");
                            }
                        } else {
                            throw new CommandFormatException("Aw... your deadline command doesn't have a deadline " +
                                    "date set!");
                        }
                    } catch (Exception e) {
                        throw new CommandFormatException("Aw... your deadline command is incorrect. Try this: " +
                                "deadline {task} /by {yyyy-MM-dd HHmm}");
                    }
                case "event":
                    if (items.length != 2) {
                        throw new CommandFormatException("Aw your event command is incomplete. Try this: event " +
                                "{event} /from {from} /to {to}");
                    }
                    try {
                        if (items[1].contains("/from")) {
                            String[] eventString = items[1].split(" /from ", 2);
                            if (eventString.length == 2) {
                                String taskString = eventString[0];
                                if (eventString[1].contains("/to")) {
                                    String[] dates = eventString[1].split(" /to ", 2);
                                    if (dates.length == 2) {
                                        String fromDate = dates[0];
                                        String toDate = dates[1];
                                        return taskManager.addTask(new EventTask(taskString, fromDate, toDate, false));
                                    } else {
                                        throw new CommandFormatException("Aw... you might be missing a from or to " +
                                                "date!");
                                    }
                                } else {
                                    throw new CommandFormatException("Aw... you might be missing the to date!");
                                }
                            } else {
                                throw new CommandFormatException("Aw... you might be missing the task description and" +
                                        " /from date!");
                            }
                        } else {
                            throw new CommandFormatException("Aw... you might be missing the /from date!");
                        }
                    } catch (Exception e) {
                        throw new CommandFormatException("Aw... your event command might be incorrect. Try this: " +
                                "event {event} /from {from} /to {to}");
                    }
                default:
                    return "Sorry bro, no clue what you're saying!";
                }
            } catch (NoSuchTaskException | CommandFormatException e) {
                return e.getMessage();
            }
        }
    }
}
