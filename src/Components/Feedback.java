package Components;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class Feedback extends JPanel {

    public Feedback() {

        // Create and add the label to the frame
        JLabel label = new JLabel("Customer Feedbacks");
        label.setFont(new Font("Tahoma", Font.BOLD, 30));
        label.setHorizontalAlignment(SwingConstants.CENTER);
        label.setForeground(new Color(252, 248, 186));
        add(label);

        // Create panel for table
        setLayout(new BorderLayout());
        setBounds(50, 70, 600, 400);
        setBackground(new Color(252, 248, 186));

        // Create table model
        DefaultTableModel tableModel = new DefaultTableModel();
        tableModel.addColumn("Name");
        tableModel.addColumn("Feedback");

        // Create table
        JTable table = new JTable(tableModel);
        table.setRowHeight(30);
        table.setBackground(new Color(252, 248, 186)); // Set background color of the table
        table.setFont(new Font("Tahoma", Font.PLAIN, 16)); // Set font for table content
        
        // Read data from the text file and add to the table
        try {
            BufferedReader reader = new BufferedReader(new FileReader("feedbacks.txt"));
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(":");
                if (parts.length == 2) {
                    tableModel.addRow(new Object[]{parts[0], parts[1]});
                }
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        JScrollPane scrollPane = new JScrollPane(table);
        add(scrollPane, BorderLayout.CENTER);

    }

}

