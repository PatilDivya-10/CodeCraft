import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class QuizApp extends JFrame implements ActionListener {
    private JLabel questionLabel;
    private JRadioButton[] options;
    private JButton nextButton;
    private ButtonGroup group;
    private int score = 0;
    private int currentQuestion = 0;

    // Array of questions (5 questions max)
    String[] questions = {
        "What is the capital of France?",
        "Who is known as the father of computers?",
        "Which planet is known as the Red Planet?",
        "Which language is known as the backbone of the web?",
        "Who wrote 'Pride and Prejudice'?"
    };

    // Array of options for each question
    String[][] optionsList = {
        {"Paris", "London", "Berlin", "Rome"},
        {"Charles Babbage", "Alan Turing", "Thomas Edison", "Isaac Newton"},
        {"Earth", "Mars", "Jupiter", "Saturn"},
        {"Java", "Python", "JavaScript", "C++"},
        {"Jane Austen", "Emily Brontë", "J.K. Rowling", "Charles Dickens"}
    };

    // Array of correct answers (index of the correct option in optionsList)
    int[] correctAnswers = {0, 0, 1, 2, 0};

    public QuizApp() {
        setTitle("Simple Quiz Application");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // Question Label
        questionLabel = new JLabel();
        questionLabel.setFont(new Font("Arial", Font.BOLD, 16));
        add(questionLabel, BorderLayout.NORTH);

        // Options as radio buttons
        options = new JRadioButton[4];
        group = new ButtonGroup();
        JPanel optionsPanel = new JPanel(new GridLayout(4, 1));
        for (int i = 0; i < 4; i++) {
            options[i] = new JRadioButton();
            group.add(options[i]);  // Grouping radio buttons to ensure only one can be selected at a time
            optionsPanel.add(options[i]);
        }
        add(optionsPanel, BorderLayout.CENTER);

        // Next Button
        nextButton = new JButton("Next");
        nextButton.addActionListener(this);
        add(nextButton, BorderLayout.SOUTH);

        loadQuestion(currentQuestion);  // Load the first question initially
    }

    // Load a question and options
    private void loadQuestion(int questionIndex) {
        questionLabel.setText(questions[questionIndex]);
        for (int i = 0; i < 4; i++) {
            options[i].setText(optionsList[questionIndex][i]);
            options[i].setSelected(false);  // Deselect all options when loading a new question
        }
    }

    // Check the selected answer
    private boolean checkAnswer() {
        for (int i = 0; i < 4; i++) {
            if (options[i].isSelected() && i == correctAnswers[currentQuestion]) {
                return true;
            }
        }
        return false;
    }

    // Display final score, correct answers, and feedback
    private void displayScore() {
        StringBuilder result = new StringBuilder("Your Score: " + score + "/" + questions.length + "\nCorrect Answers:\n");
        for (int i = 0; i < questions.length; i++) {
            result.append(questions[i]).append(" - ").append(optionsList[i][correctAnswers[i]]).append("\n");
        }

        // Provide feedback based on score
        if (score == questions.length) {
            result.append("\nExcellent! You got all answers correct!");
        } else if (score >= 3) {
            result.append("\nGood job! You got most answers right.");
        } else {
            result.append("\nKeep practicing! You'll improve with more attempts.");
        }

        JOptionPane.showMessageDialog(this, result.toString(), "Quiz Results", JOptionPane.INFORMATION_MESSAGE);
        System.exit(0);  // Exit the application after showing the results
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        // Check if the selected answer is correct and update score
        if (checkAnswer()) {
            score++;
        }
        currentQuestion++;  // Move to the next question

        // If there are more questions, load the next one. Otherwise, show the score
        if (currentQuestion < questions.length) {
            loadQuestion(currentQuestion);
        } else {
            displayScore();  // End of the quiz, display the score and correct answers
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new QuizApp().setVisible(true);
        });
    }
}
