package Controller;

import java.io.*;
import java.util.*;
import java.awt.*;
import javax.swing.*;

public class ManagerController{
    public static void processEdit(String lineContent, int lineNumber) {
        // Split the line content by ";" to get individual parts
        String[] parts = lineContent.split(";");

        // Create JTextFields to allow editing of each part
        JTextField[] textFields = new JTextField[parts.length];
        for (int i = 0; i < parts.length; i++) {
            textFields[i] = new JTextField(parts[i]);
        }

        // Create a panel to hold the JTextFields
        JPanel editPanel = new JPanel(new GridLayout(parts.length, 2));
        for (int i = 0; i < parts.length-1; i++) {
            if(i==0){
                editPanel.add(new JLabel("Item Name:")); 
            } else if(i==1){
                editPanel.add(new JLabel("Price")); 
            } 
            editPanel.add(textFields[i]);
        }

        // Show a dialog with the edit panel
        int result = JOptionPane.showConfirmDialog(null, editPanel, "Edit Item",
                JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

        // If the user clicks OK, save the changes
        if (result == JOptionPane.OK_OPTION) {
            // Concatenate the edited parts into a new line content
            StringBuilder newLineContent = new StringBuilder();
            for (int i = 0; i < parts.length; i++) {
                newLineContent.append(textFields[i].getText());
                if (i < parts.length - 1) {
                    newLineContent.append(";");
                }
            }

            // Read all lines from the file into a list
            File file = new File("foodItem.txt");
            ArrayList<String> lines = new ArrayList<>();
            try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    lines.add(line);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

            // Update the specified line with the new content
            lines.set(lineNumber, newLineContent.toString());

            // Write the updated lines back to the file
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
                for (String line : lines) {
                    writer.write(line);
                    writer.newLine();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

            JOptionPane.showMessageDialog(null, "Changes saved successfully.");
        }
    }
}