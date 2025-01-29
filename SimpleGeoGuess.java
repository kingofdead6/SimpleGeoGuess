import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.*;

public class SimpleGeoGuess {

    private JFrame frame;
    private JLabel imageLabel;
    private JLabel questionLabel;
    private JPanel buttonPanel;
    private JButton[] choiceButtons;
    private JButton nextButton;
    private JButton stopButton;
    private JLabel scoreLabel;

    private Map<String, String> imagePlaceMap;
    private ArrayList<String> imagePaths;
    private int currentImageIndex;
    private String correctAnswer;
    private int score;

    public SimpleGeoGuess() {
        showMainMenu();
    }

    private void showMainMenu() {
        JFrame mainFrame = new JFrame("GeoGuess Main Menu");
        mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mainFrame.setExtendedState(JFrame.MAXIMIZED_BOTH); 
        mainFrame.setLayout(new BorderLayout());
        mainFrame.getContentPane().setBackground(Color.BLACK); 

        JLabel welcomeLabel = new JLabel("Welcome to GeoGuess Game", SwingConstants.CENTER);
        welcomeLabel.setFont(new Font("Monospaced", Font.BOLD, 36));
        welcomeLabel.setForeground(new Color(0, 255, 255)); 
        welcomeLabel.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));

        JPanel buttonPanel = new JPanel(new GridLayout(2, 1, 15, 15));
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(30, 80, 30, 80));
        buttonPanel.setBackground(Color.BLACK);

        JButton startButton = new JButton("Start Game");
        JButton exitButton = new JButton("Exit");

        styleButton(startButton, new Color(0, 255, 255));
        styleButton(exitButton, new Color(255, 0, 255)); 
        startButton.addActionListener(e -> {
            mainFrame.dispose();
            initializeGameData();
            createUI();
            loadNextImage();
        });

        exitButton.addActionListener(e -> System.exit(0));

        buttonPanel.add(startButton);
        buttonPanel.add(exitButton);

        mainFrame.add(welcomeLabel, BorderLayout.NORTH);
        mainFrame.add(buttonPanel, BorderLayout.CENTER);
        mainFrame.setLocationRelativeTo(null);
        mainFrame.setVisible(true);
    }

    private void initializeGameData() {
        imagePlaceMap = new HashMap<>();
        imagePlaceMap.put("img/image1.png", "Eiffel Tower");
        imagePlaceMap.put("img/image2.jpg", "Great Wall of China");
        imagePlaceMap.put("img/image3.jpeg", "Pyramids of Giza");
        imagePlaceMap.put("img/image4.jpg", "Statue of Liberty");
        imagePlaceMap.put("img/image5.jpg", "Taj Mahal");
        imagePlaceMap.put("img/image6.jpeg", "Machu Picchu");
        imagePlaceMap.put("img/image7.jpeg", "Colosseum");
        imagePlaceMap.put("img/image8.jpg", "Sydney Opera House");
        imagePlaceMap.put("img/image9.jpg", "Christ the Redeemer");
        imagePlaceMap.put("img/image10.jpg", "Petra");

        imagePaths = new ArrayList<>(imagePlaceMap.keySet());
        Collections.shuffle(imagePaths); 
        currentImageIndex = 0;
        score = 0;
    }

    private void createUI() {
        frame = new JFrame("GeoGuess Game");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setExtendedState(JFrame.MAXIMIZED_BOTH); 
        frame.setLayout(new BorderLayout());
        frame.getContentPane().setBackground(Color.BLACK);

        imageLabel = new JLabel();
        imageLabel.setHorizontalAlignment(SwingConstants.CENTER);

        questionLabel = new JLabel("Where is this place?", SwingConstants.CENTER);
        questionLabel.setFont(new Font("Monospaced", Font.BOLD, 36));
        questionLabel.setForeground(new Color(0, 255, 255));

        scoreLabel = new JLabel("Score: 0", SwingConstants.CENTER);
        scoreLabel.setFont(new Font("Monospaced", Font.BOLD, 24));
        scoreLabel.setForeground(new Color(255, 0, 255)); 
        scoreLabel.setBorder(BorderFactory.createEmptyBorder(10, 0, 20, 0));

        buttonPanel = new JPanel(new GridLayout(2, 2, 15, 15));
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(20, 50, 20, 50));
        buttonPanel.setBackground(Color.BLACK); 

        choiceButtons = new JButton[4];

        for (int i = 0; i < 4; i++) {
            choiceButtons[i] = new JButton();
            styleButton(choiceButtons[i], new Color(0, 255, 255)); 
            choiceButtons[i].addActionListener(new ChoiceButtonHandler());
            addHoverEffect(choiceButtons[i]);
            buttonPanel.add(choiceButtons[i]);
        }

        nextButton = new JButton("Next");
        styleButton(nextButton, new Color(255, 0, 255));
        nextButton.setEnabled(false);
        nextButton.addActionListener(new NextButtonHandler());
        addHoverEffect(nextButton);

        stopButton = new JButton("Stop Game");
        styleButton(stopButton, new Color(255, 69, 0)); 
        stopButton.addActionListener(e -> showEndScreen());
        addHoverEffect(stopButton);

        JPanel bottomPanel = new JPanel(new BorderLayout());
        bottomPanel.setBackground(Color.BLACK); 
        bottomPanel.add(buttonPanel, BorderLayout.CENTER);

        JPanel controlPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        controlPanel.setBackground(Color.BLACK);
        controlPanel.add(nextButton);
        controlPanel.add(stopButton);

        bottomPanel.add(controlPanel, BorderLayout.SOUTH);

        frame.add(scoreLabel, BorderLayout.NORTH);
        frame.add(imageLabel, BorderLayout.CENTER);
        frame.add(questionLabel, BorderLayout.SOUTH);
        frame.add(bottomPanel, BorderLayout.SOUTH);

        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    private void loadNextImage() {
        if (currentImageIndex >= imagePaths.size()) {
            showEndScreen();
            return;
        }

        String imagePath = imagePaths.get(currentImageIndex);
        correctAnswer = imagePlaceMap.get(imagePath);
        ImageIcon icon = new ImageIcon(imagePath);

        Image img = icon.getImage().getScaledInstance(800, 400, Image.SCALE_SMOOTH);
        imageLabel.setIcon(new ImageIcon(img));

        String[] options = generateOptions(correctAnswer);
        for (int i = 0; i < 4; i++) {
            choiceButtons[i].setText(options[i]);
            choiceButtons[i].setEnabled(true);
        }
        nextButton.setEnabled(false);
    }

    private String[] generateOptions(String correctAnswer) {
        ArrayList<String> places = new ArrayList<>(imagePlaceMap.values());
        places.remove(correctAnswer); 
        Collections.shuffle(places);

        String[] options = new String[4];
        int correctIndex = (int) (Math.random() * 4);
        options[correctIndex] = correctAnswer;

        for (int i = 0, j = 0; i < 4; i++) {
            if (options[i] == null) {
                options[i] = places.get(j++);
            }
        }
        return options;
    }

    private class ChoiceButtonHandler implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            JButton selectedButton = (JButton) e.getSource();
            String selectedAnswer = selectedButton.getText();

            if (selectedAnswer.equals(correctAnswer)) {
                score++;
                showCustomDialog("You are correct! Well done!", new Color(0, 255, 0)); 
            } else {
                showCustomDialog("Oops! The correct answer was: " + correctAnswer + ". Better luck next time!", new Color(255, 0, 0)); // Neon red
            }
            scoreLabel.setText("Score: " + score);

            for (JButton button : choiceButtons) {
                button.setEnabled(false);
            }
            nextButton.setEnabled(true);
        }
    }

    private void showCustomDialog(String message, Color color) {
        JDialog dialog = new JDialog(frame, "Result", true);
        dialog.setSize(500, 250);
        dialog.setLayout(new BorderLayout());
        dialog.getContentPane().setBackground(Color.BLACK);

        JLabel label = new JLabel(message, SwingConstants.CENTER);
        label.setFont(new Font("Monospaced", Font.BOLD, 24));
        label.setForeground(color); 
        label.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));

        JButton closeButton = new JButton("Close");
        styleButton(closeButton, color);
        closeButton.addActionListener(e -> dialog.dispose());
        addHoverEffect(closeButton);

        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.BLACK); 
        panel.add(label, BorderLayout.CENTER);
        panel.add(closeButton, BorderLayout.SOUTH);

        dialog.add(panel);
        dialog.setLocationRelativeTo(frame);
        dialog.setVisible(true);
    }

    private void showEndScreen() {
        frame.dispose(); 

        JFrame endFrame = new JFrame("Game Over");
        endFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        endFrame.setExtendedState(JFrame.MAXIMIZED_BOTH); 
        endFrame.setLayout(new BorderLayout());
        endFrame.getContentPane().setBackground(Color.BLACK);

        JLabel endLabel = new JLabel("Game Over! Your final score is: " + score, SwingConstants.CENTER);
        endLabel.setFont(new Font("Monospaced", Font.BOLD, 36));
        endLabel.setForeground(new Color(0, 255, 255)); 
        endLabel.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));

        JButton restartButton = new JButton("Restart Game");
        styleButton(restartButton, new Color(0, 255, 255)); 
        restartButton.addActionListener(e -> {
            endFrame.dispose();
            new SimpleGeoGuess(); 
        });
        addHoverEffect(restartButton);

        JButton exitButton = new JButton("Exit");
        styleButton(exitButton, new Color(255, 0, 255)); 
        exitButton.addActionListener(e -> System.exit(0));
        addHoverEffect(exitButton);

        JPanel buttonPanel = new JPanel(new GridLayout(2, 1, 15, 15));
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(30, 80, 30, 80));
        buttonPanel.setBackground(Color.BLACK); 
        buttonPanel.add(restartButton);
        buttonPanel.add(exitButton);

        endFrame.add(endLabel, BorderLayout.CENTER);
        endFrame.add(buttonPanel, BorderLayout.SOUTH);
        endFrame.setLocationRelativeTo(null);
        endFrame.setVisible(true);
    }

    private class NextButtonHandler implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            currentImageIndex++;
            loadNextImage();
        }
    }

    private void styleButton(JButton button, Color neonColor) {
        button.setFont(new Font("Monospaced", Font.BOLD, 18));
        button.setBackground(Color.BLACK); 
        button.setForeground(neonColor); 
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createLineBorder(neonColor, 2)); 
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
    }

    private void addHoverEffect(JButton button) {
        Color originalColor = button.getForeground();
        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                button.setBackground(originalColor); 
                button.setForeground(Color.BLACK); 
            }

            @Override
            public void mouseExited(MouseEvent e) {
                button.setBackground(Color.BLACK);
                button.setForeground(originalColor);
            }
        });
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(SimpleGeoGuess::new);
    }
}