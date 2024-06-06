package Components;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;

public class CartTablePanel extends JPanel {

    public CartTablePanel(String id) {
        setLayout(new GridBagLayout());

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;

        JPanel innerPanel = new JPanel(new BorderLayout());
        innerPanel.setPreferredSize(new Dimension(280, 200));

        // Create table data
        DefaultTableModel model = new DefaultTableModel();
        model.addColumn("Name");
        model.addColumn("Price");
        model.addColumn("Quantity");
        model.addColumn("Total");

        double grandTotal = 0; // Initialize grand total

        try (BufferedReader reader = new BufferedReader(new FileReader("orderList.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(";");
                if (parts.length >= 2) {
                    double price = Double.parseDouble(parts[1].replace("Rs.", ""));
                    double total = price * 1; // Assuming initial quantity is 1
                    grandTotal += total; // Update grand total
                    model.addRow(new Object[]{parts[0], parts[1], 1, total}); // Add row with initial quantity and total set to 0
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Create JTable
        JTable table = new JTable(model) {
            @Override
            public boolean isCellEditable(int row, int column) {
                // Allow editing only for the quantity column (index 2)
                return column == 2;
            }
        };

        table.getColumnModel().getColumn(3).setCellRenderer((TableCellRenderer) new CurrencyRenderer()); // Render total column as currency
        table.getColumnModel().getColumn(3).setCellEditor(new DefaultCellEditor(new JTextField())); // Make total column editable

        // Add listener to update totals when quantity changes
        table.getModel().addTableModelListener(e -> {
            if (e.getColumn() == 2) { // Check if quantity column was edited
                int row = e.getFirstRow();
                int quantity = Integer.parseInt(model.getValueAt(row, 2).toString());
                double price = Double.parseDouble(model.getValueAt(row, 1).toString().replace("Rs.", ""));
                double total = quantity * price;
                model.setValueAt(total, row, 3); // Update total column
                updateGrandTotal(model); // Update grand total
            }
        }); // Add grand total row at the end
        model.addRow(new Object[]{"Grand Total:", "", "", grandTotal}); // Add row with grand total

        innerPanel.add(new JScrollPane(table));

        add(innerPanel, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        JButton proceedToPayButton = new JButton("Proceed To Pay");
        proceedToPayButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String[] options = {"Online Payment", "Cash"};
                int choice = JOptionPane.showOptionDialog(null, "Choose Payment Method", "Payment Method", JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[0]);
                if (choice != JOptionPane.CLOSED_OPTION) {
                    String paymentMethod = options[choice];
                    int confirm = JOptionPane.showConfirmDialog(null, "Confirm Payment Method: " + paymentMethod, "Confirm", JOptionPane.YES_NO_OPTION);
                    if (confirm == JOptionPane.YES_OPTION) {
                        saveOrderDetails(model,id);
                        JOptionPane.showMessageDialog(null, "Order placed successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
                    }
                }
            }
        });
        add(proceedToPayButton, gbc);
    }

    private void updateGrandTotal(DefaultTableModel model) {
        double grandTotal = 0;
        for (int row = 0; row < model.getRowCount(); row++) {
            double total = Double.parseDouble(model.getValueAt(row, 3).toString());
            grandTotal += total;
        }
        model.setValueAt(grandTotal, model.getRowCount() - 1, 3); // Update grand total row
    }

    private void saveOrderDetails(DefaultTableModel model, String id) {
        try (PrintWriter writer = new PrintWriter(new FileWriter("OrderDetails.txt", true))) {
            for (int row = 0; row < model.getRowCount() - 1; row++) {
                StringBuilder line = new StringBuilder(id); // Append customer ID at the beginning of each line
                for (int col = 0; col < model.getColumnCount(); col++) {
                    line.append(";").append(model.getValueAt(row, col)); // Append order details
                }
                writer.println(line.toString());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    

    public static void main(String[] args) {
        JFrame frame = new JFrame("Cart Table Panel");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().add(new CartTablePanel("Demo Customer"));
        frame.pack();
        frame.setVisible(true);
    }
}

class CurrencyRenderer extends DefaultTableCellRenderer {
    public CurrencyRenderer() {
        setHorizontalAlignment(JLabel.RIGHT);
    }

    @Override
    public void setValue(Object value) {
        if (value != null) {
            value = String.format("Rs.%.2f", Double.parseDouble(value.toString()));
        }
        super.setValue(value);
    }
}
