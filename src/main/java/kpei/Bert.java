package kpei;

import kpei.datatypes.Task;
import kpei.datatypes.TaskList;
import kpei.exceptions.BertException;
import kpei.exceptions.InvalidIndexException;
import kpei.storage.Storage;

/**
 * Domain controller for the BERT task assistant.
 * Manages task data and persistence, independent of user interface.
 */
public class Bert {

    private final String storageFilePath;
    private final Storage storage;
    private final TaskList taskList;

    /**
     * Constructs a {@code Bert} instance with the specified task persistence file path.
     *
     * @param storageFilePath File path for persisting task data.
     */
    public Bert(String storageFilePath) {
        this.storageFilePath = storageFilePath;
        this.storage = new Storage(storageFilePath);
        this.taskList = new TaskList();
    }

    /**
     * Loads tasks from storage into the task list.
     *
     * @throws BertException If an error occurs while reading tasks from storage.
     */
    public void load() throws BertException {
        storage.load(taskList);
    }

    /**
     * Saves the current task list to storage.
     *
     * @throws BertException If an error occurs while writing tasks to storage.
     */
    public void save() throws BertException {
        storage.save(taskList);
    }

    /**
     * Adds a task to the list and saves changes to storage.
     *
     * @param task The task to add.
     * @throws BertException If saving fails.
     */
    public void addTask(Task task) throws BertException {
        taskList.add(task);
        save();
    }

    /**
     * Marks the task at the specified 1-based index and saves changes to storage.
     *
     * @param index 1-based index of the task.
     * @return The marked task.
     * @throws InvalidIndexException If the index is out of bounds.
     * @throws BertException If saving fails.
     */
    public Task markTask(int index) throws BertException {
        Task task = taskList.mark(index);
        save();
        return task;
    }

    /**
     * Unmarks the task at the specified 1-based index and saves changes to storage.
     *
     * @param index 1-based index of the task.
     * @return The unmarked task.
     * @throws InvalidIndexException If the index is out of bounds.
     * @throws BertException If saving fails.
     */
    public Task unmarkTask(int index) throws BertException {
        Task task = taskList.unmark(index);
        save();
        return task;
    }

    /**
     * Deletes the task at the specified 1-based index and saves changes to storage.
     *
     * @param index 1-based index of the task to delete.
     * @return The deleted task.
     * @throws InvalidIndexException If the index is out of bounds.
     * @throws BertException If saving fails.
     */
    public Task deleteTask(int index) throws InvalidIndexException, BertException {
        Task task = taskList.remove(index);
        save();
        return task;
    }

    /**
     * Finds tasks containing the specified keyword in their description.
     *
     * @param keyword The search keyword.
     * @return A {@link TaskList} of matching tasks.
     */
    public TaskList findTasks(String keyword) {
        return taskList.find(keyword);
    }

    /**
     * Returns the task list managed by this instance.
     *
     * @return The task list.
     */
    public TaskList getTaskList() {
        return taskList;
    }

    /**
     * Returns the file path used for task storage.
     *
     * @return The storage file path.
     */
    public String getStorageFilePath() {
        return storageFilePath;
    }
}
