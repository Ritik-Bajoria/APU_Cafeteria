package Components;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;

public class ProductRatingPanel extends JPanel {

    private JTextField feedbackField;
    private String selectedFoodItem;
    static double updatedRating;

    public ProductRatingPanel() {
        setLayout(new GridBagLayout());
        setPreferredSize(new Dimension(250,220));
        GridBagConstraints gbc=new GridBagConstraints();
        // Button to give rating
        JButton ratingButton = new JButton("Give Rating");
        ratingButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                selectFoodItem();
            }
        });
        gbc.gridx=0;
        gbc.gridy=0;
        gbc.gridheight=1;
        gbc.gridwidth=1;
        gbc.anchor=GridBagConstraints.CENTER;
        gbc.fill = GridBagConstraints.BOTH;
        ratingButton.setPreferredSize(new Dimension(200,30));
        gbc.insets=new Insets(0, 0, 10, 0);
        add(ratingButton,gbc);

        // Text field for feedback
        feedbackField = new JTextField();
        gbc.gridx=0;
        gbc.gridy=1;
        gbc.gridheight=2;
        gbc.gridwidth=1;
        feedbackField.setPreferredSize(new Dimension(260,80));
        add(feedbackField,gbc);

        // Button to give feedback
        JButton feedbackButton = new JButton("Give Feedback");
        feedbackButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                giveFeedback();
            }
        });
        gbc.gridx=0;
        gbc.gridy=3;
        gbc.gridheight=1;
        gbc.gridwidth=1;
        feedbackButton.setPreferredSize(new Dimension(250,40));
        add(feedbackButton,gbc);
    }

    private void selectFoodItem() {
        try (BufferedReader reader = new BufferedReader(new FileReader("foodItem.txt"))) {
            String line;
            StringBuilder foodItems = new StringBuilder();
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(";");
                foodItems.append(parts[0]).append("\n");
            }

            String selected = (String) JOptionPane.showInputDialog(this, "Select a food item to rate:", "Food Item Selection",
                    JOptionPane.PLAIN_MESSAGE, null, foodItems.toString().split("\n"), null);

            if (selected != null) {
                selectedFoodItem = selected.trim();
                String ratingStr = JOptionPane.showInputDialog(this, "Enter rating for " + selectedFoodItem + ": (1-5)");
                if (ratingStr != null) {
                    try {
                        int rating = Integer.parseInt(ratingStr);
                        
                        modifyRating(selectedFoodItem, rating);
                    } catch (NumberFormatException ex) {
                        JOptionPane.showMessageDialog(this, "Invalid rating! Please enter a number between 1 and 5.", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void modifyRating(String foodItem, int newRating) {
        try {
            File inputFile = new File("foodItem.txt");
            File tempFile = new File("tempFoodItem.txt");
    
            BufferedReader reader = new BufferedReader(new FileReader(inputFile));
            BufferedWriter writer = new BufferedWriter(new FileWriter(tempFile));
    
            String line;
            boolean found = false;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(";");
                if (parts[0].equals(foodItem)) {
                    double oldRating = Double.parseDouble(parts[2]);
                    double updatedRating;
                    if (oldRating != 0) {
                        updatedRating = (oldRating + newRating) / 2.0;
                    } else {
                        updatedRating = newRating;
                    }
                    line = parts[0] + ";" + parts[1] + ";" + updatedRating;
                    found = true;
                }
                writer.write(line + "\n");
            }
            
            if (!found) {
                // If the food item was not found, add it with the new rating
                writer.write(foodItem + ";" + "0.0" + ";" + newRating + "\n");
            }
    
            reader.close();
            writer.close();
            
            // Read content from the temporary file
            StringBuilder content = new StringBuilder();
            try (BufferedReader br = new BufferedReader(new FileReader(tempFile))) {
                // String line;
                while ((line = br.readLine()) != null) {
                    content.append(line).append("\n");
                }
            }

            // Write content to the original file
            try (BufferedWriter bw = new BufferedWriter(new FileWriter(inputFile))) {
                bw.write(content.toString());
            }

            // Delete the temporary file
            tempFile.delete();

    
            JOptionPane.showMessageDialog(this, "Rating updated successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    
    

    private void giveFeedback() {
        String feedback = feedbackField.getText().trim();
        if (!feedback.isEmpty()) {
            String username = JOptionPane.showInputDialog(this, "Enter your username:");
            if (username != null && !username.isEmpty()) {
                try (PrintWriter writer = new PrintWriter(new FileWriter("feedbacks.txt", true))) {
                    writer.println(username + ": " + feedback);
                    writer.flush();
                    JOptionPane.showMessageDialog(this, "Feedback submitted successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
                    feedbackField.setText("");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else {
                JOptionPane.showMessageDialog(this, "Username cannot be empty!", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } else {
            JOptionPane.showMessageDialog(this, "Feedback cannot be empty!", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

}
