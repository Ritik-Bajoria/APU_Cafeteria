package Frames;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class OrderDetailsPanel extends JPanel {

    public OrderDetailsPanel() {
        setLayout(new BorderLayout());

        // Create table model
        DefaultTableModel tableModel = new DefaultTableModel();
        JTable table = new JTable(tableModel);

        // Add columns to the table model
        tableModel.addColumn("Customer ID");
        tableModel.addColumn("Name");
        tableModel.addColumn("Price");
        tableModel.addColumn("Quantity");
        tableModel.addColumn("Total");

        // Read order details from OrderDetails.txt and populate the table
        try (BufferedReader reader = new BufferedReader(new FileReader("OrderDetails.txt"))) {
            String line;
            double totalEarnings = 0;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(";");
                if (parts.length >= 5) {
                    String customerId = parts[0];
                    String name = parts[1];
                    double price = Double.parseDouble(parts[2].replace("Rs.", ""));
                    int quantity = Integer.parseInt(parts[3]);
                    double total = Double.parseDouble(parts[4]);
                    tableModel.addRow(new Object[]{customerId, name,"Rs."+ price, quantity, total});
                    totalEarnings += total; // Accumulate total earnings
                }
            }
            // Add total earnings row
            tableModel.addRow(new Object[]{"Total Earnings", "", "", "", totalEarnings});
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Add the table to a scroll pane and add it to the panel
        JScrollPane scrollPane = new JScrollPane(table);
        add(scrollPane, BorderLayout.CENTER);
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("Order Details Panel");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().add(new OrderDetailsPanel());
        frame.pack();
        frame.setVisible(true);
    }
}
