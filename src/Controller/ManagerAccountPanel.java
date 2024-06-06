package Controller;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;

public class ManagerAccountPanel extends JPanel {

    private JTextField newUsernameField;
    private JPasswordField newPasswordField;
    private JPasswordField confirmPasswordField;

    public ManagerAccountPanel() {
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.insets = new Insets(5, 5, 5, 5);

        // New username label and field
        JLabel newUsernameLabel = new JLabel("New Username:");
        add(newUsernameLabel, gbc);

        gbc.gridx = 1;
        newUsernameField = new JTextField(15);
        add(newUsernameField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;

        // New password label and field
        JLabel newPasswordLabel = new JLabel("New Password:");
        add(newPasswordLabel, gbc);

        gbc.gridx = 1;
        newPasswordField = new JPasswordField(15);
        add(newPasswordField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;

        // Confirm password label and field
        JLabel confirmPasswordLabel = new JLabel("Confirm Password:");
        add(confirmPasswordLabel, gbc);

        gbc.gridx = 1;
        confirmPasswordField = new JPasswordField(15);
        add(confirmPasswordField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 2;

        // Button to update username and password
        JButton updateButton = new JButton("Update Password");
        add(updateButton, gbc);

        // Action listener for the update button
        updateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                promptForOldCredentials();
            }
        });
    }

    // Prompt for old username and password
    private void promptForOldCredentials() {
        String oldUsername = JOptionPane.showInputDialog(this, "Enter Old Username:");
        if (oldUsername == null) return; // User canceled
        String oldPassword = new String(JOptionPane.showInputDialog(this, "Enter Old Password:"));
        if (oldPassword == null) return; // User canceled

        // Validate old username and password
        if (validateOldCredentials(oldUsername, oldPassword)) {
            updateManagerAccount();
            newUsernameField.setText("");
            newPasswordField.setText("");
            confirmPasswordField.setText("");


        } else {
            JOptionPane.showMessageDialog(this, "Wrong password! Permission denied.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    // Validate old username and password
    private boolean validateOldCredentials(String oldUsername, String oldPassword) {
        try (BufferedReader reader = new BufferedReader(new FileReader("managerAccount.txt"))) {
            String line = reader.readLine();
            if (line != null) {
                String[] parts = line.split(";");
                if (parts.length == 2) {
                    String storedUsername = parts[0];
                    String storedPassword = parts[1];
                    return ((oldUsername.compareToIgnoreCase(storedUsername) + oldPassword.compareTo(storedPassword))==0);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    // Update manager account details in file
    private void updateManagerAccount() {
        String newUsername = newUsernameField.getText().trim();
        String newPassword = new String(newPasswordField.getPassword()).trim();
        String confirmPassword = new String(confirmPasswordField.getPassword()).trim();

        if (!newUsername.isEmpty() && !newPassword.isEmpty() && newPassword.equals(confirmPassword)) {
            try (PrintWriter writer = new PrintWriter(new FileWriter("managerAccount.txt"))) {
                writer.println(newUsername + ";" + newPassword);
                writer.close();
                JOptionPane.showMessageDialog(this, "Manager account details updated successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
            } catch (IOException e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(this, "Failed to update manager account details!", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } else {
            JOptionPane.showMessageDialog(this, "New password fields must not be empty and must match!", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("Manager Account Panel");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().add(new ManagerAccountPanel());
        frame.pack();
        frame.setVisible(true);
    }
}
