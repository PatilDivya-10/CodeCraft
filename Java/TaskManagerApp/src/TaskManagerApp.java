import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class TaskManagerApp {
    private List<Task> tasks;
    private DefaultListModel<String> taskListModel;
    private JList<String> taskJList;
    private JTextField titleField;
    private JTextField dueDateField;
    private JTextField priorityField;

    public TaskManagerApp() {
        tasks = new ArrayList<>();
        taskListModel = new DefaultListModel<>();

        JFrame frame = new JFrame("Task Manager");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(400, 300);
        frame.setLayout(new BorderLayout());

        // Task List
        taskJList = new JList<>(taskListModel);
        JScrollPane scrollPane = new JScrollPane(taskJList);
        frame.add(scrollPane, BorderLayout.CENTER);

        // Input Panel
        JPanel inputPanel = new JPanel(new GridLayout(4, 2));
        inputPanel.add(new JLabel("Task Title:"));
        titleField = new JTextField();
        inputPanel.add(titleField);
        
        inputPanel.add(new JLabel("Due Date (YYYY-MM-DD):"));
        dueDateField = new JTextField();
        inputPanel.add(dueDateField);
        
        inputPanel.add(new JLabel("Priority (1-5):"));
        priorityField = new JTextField();
        inputPanel.add(priorityField);

        JButton addButton = new JButton("Add Task");
        addButton.addActionListener(e -> addTask());
        inputPanel.add(addButton);

        JButton deleteButton = new JButton("Delete Task");
        deleteButton.addActionListener(e -> deleteTask());
        inputPanel.add(deleteButton);

        frame.add(inputPanel, BorderLayout.SOUTH);
        frame.setVisible(true);
    }

    private void addTask() {
        String title = titleField.getText();
        String dueDateStr = dueDateField.getText();
        String priorityStr = priorityField.getText();

        try {
            LocalDate dueDate = LocalDate.parse(dueDateStr);
            int priority = Integer.parseInt(priorityStr);

            Task newTask = new Task(title, dueDate, priority);
            tasks.add(newTask);
            taskListModel.addElement(newTask.toString());

            titleField.setText("");
            dueDateField.setText("");
            priorityField.setText("");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Invalid input. Please try again.");
        }
    }

    private void deleteTask() {
        int selectedIndex = taskJList.getSelectedIndex();
        if (selectedIndex != -1) {
            tasks.remove(selectedIndex);
            taskListModel.remove(selectedIndex);
        } else {
            JOptionPane.showMessageDialog(null, "Select a task to delete.");
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(TaskManagerApp::new);
    }
}
