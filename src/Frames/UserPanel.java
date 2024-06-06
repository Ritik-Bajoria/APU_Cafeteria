package Frames;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class UserPanel extends JPanel{

    UserPanel(){
        setLayout(new BorderLayout());
        setPreferredSize(new Dimension(700, 500));
        
        DefaultTableModel model = new DefaultTableModel();
        model.addColumn("Name");
        model.addColumn("Number");
        model.addColumn("Email");
        model.addColumn("Address");
        model.addColumn("Password");

        try (BufferedReader reader = new BufferedReader(new FileReader("accountFile.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(";");
                if (parts.length >= 5) {
                    model.addRow(parts);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Create a JTable with the model
        JTable table = new JTable(model);
        table.setRowHeight(40); // Set the row height as needed

        // Add the table to a JScrollPane
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);

        // Add the JScrollPane to the panel
        add(scrollPane, BorderLayout.CENTER);
    }
}
