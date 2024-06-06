package Components;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

import Frames.*;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;

public class ManageFoodItemsPanel extends JPanel {

    public ManageFoodItemsPanel() {
        setLayout(new GridBagLayout());
        GridBagConstraints constraints=new GridBagConstraints();
        setBackground(new Color(252, 248, 186));

        // Create and add the label to the panel
        JLabel label = new JLabel("Manage Food Items");
        label.setFont(new Font("Tahoma", Font.BOLD, 20));
        label.setHorizontalAlignment(SwingConstants.CENTER);
        constraints.gridx=0;
        constraints.gridy=0;
        add(label, constraints);

        // Create and add the inner panel for adding items
        AddItemPanel addItemPanel = new AddItemPanel();
        addItemPanel.setBackground(new Color(252, 248, 186));
        constraints.gridx=0;
        constraints.gridy=1;
        add(addItemPanel, constraints);

        // Create and add the inner panel for deleting items
        DeleteItemPanel deleteItemPanel = new DeleteItemPanel();
        constraints.gridx=0;
        constraints.gridy=2;
        add(deleteItemPanel, constraints);
    }

    private class AddItemPanel extends JPanel {
        private JTextField itemNameField;
        private JTextField priceField;

        public AddItemPanel() {
            setLayout(new GridBagLayout());
            setPreferredSize(new Dimension(300, 200));
            GridBagConstraints gbc = new GridBagConstraints();
            gbc.gridx = 0;
            gbc.gridy = 0;
            gbc.insets = new Insets(5, 5, 5, 5);

            // Add item name label and text field
            JLabel itemNameLabel = new JLabel("Item Name:");
            add(itemNameLabel, gbc);

            gbc.gridx++;
            itemNameField = new JTextField(15);
            add(itemNameField, gbc);

            gbc.gridx = 0;
            gbc.gridy++;
            // Add price label and text field
            JLabel priceLabel = new JLabel("Price (Rs.):");
            add(priceLabel, gbc);

            gbc.gridx++;
            priceField = new JTextField(15);
            add(priceField, gbc);

            gbc.gridx = 0;
            gbc.gridy++;
            // Add button for adding item
            JButton addButton = new JButton("Add Item");
            addButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    addItem();
                }
            });
            gbc.anchor=GridBagConstraints.CENTER;
            gbc.gridwidth=2;
            add(addButton, gbc);
        }

        private void addItem() {
            String itemName = itemNameField.getText().trim();
            String priceStr = priceField.getText().trim();

            if (!itemName.isEmpty() && !priceStr.isEmpty()) {
                try (PrintWriter writer = new PrintWriter(new FileWriter("foodItem.txt", true))) {
                    writer.println(itemName + ";" + priceStr + ";0"); // Set initial rating to 0
                    JOptionPane.showMessageDialog(this, "Item added successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
                    itemNameField.setText("");
                    priceField.setText("");
                    new DeleteItemPanel().loadItemsForDeletion();

                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else {
                JOptionPane.showMessageDialog(this, "Please enter item name and price!", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private class DeleteItemPanel extends JPanel {
        private JTable table;
        private DefaultTableModel tableModel;
        private JTextField selectedItemField;

        public DeleteItemPanel() {
            setLayout(new BorderLayout());
            setPreferredSize(new Dimension(400, 200));

            // Create table model
            tableModel = new DefaultTableModel();
            tableModel.addColumn("Item Name");

            // Create table
            table = new JTable(tableModel);
            table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
            table.getSelectionModel().addListSelectionListener(e -> {
                int selectedRow = table.getSelectedRow();
                if (selectedRow != -1) {
                    String selectedItem = (String) table.getValueAt(selectedRow, 0);
                    selectedItemField.setText(selectedItem);
                }
            });

            // Load existing items for deletion
            loadItemsForDeletion();

            // Add table to scroll pane
            JScrollPane scrollPane = new JScrollPane(table);
            add(scrollPane, BorderLayout.CENTER);

            // Create panel for selected item and delete button
            JPanel deletePanel = new JPanel(new FlowLayout());
            selectedItemField = new JTextField(15);
            selectedItemField.setEditable(false);
            deletePanel.add(selectedItemField);

            JButton deleteButton = new JButton("Delete Item");
            deleteButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    deleteSelectedItem();
                }
            });
            deletePanel.add(deleteButton);

            add(deletePanel, BorderLayout.SOUTH);
        }

        private void loadItemsForDeletion() {
            tableModel.setRowCount(0);
            try (BufferedReader reader = new BufferedReader(new FileReader("foodItem.txt"))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    String[] parts = line.split(";");
                    if (parts.length >= 1) {
                        tableModel.addRow(new Object[]{parts[0]});
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        private void deleteSelectedItem() {
            String selectedItem = selectedItemField.getText().trim();
            if (!selectedItem.isEmpty()) {
                int confirm = JOptionPane.showConfirmDialog(this, "Are you sure you want to delete the item: " + selectedItem + "?", "Confirm Deletion", JOptionPane.YES_NO_OPTION);
                if (confirm == JOptionPane.YES_OPTION) {
                    try {
                        File inputFile = new File("foodItem.txt");
                        File tempFile = new File("tempFoodItem.txt");

                        BufferedReader reader = new BufferedReader(new FileReader(inputFile));
                        PrintWriter writer = new PrintWriter(new FileWriter(tempFile));

                        String line;
                        while ((line = reader.readLine()) != null) {
                            if (!line.startsWith(selectedItem + ";")) {
                                writer.println(line);
                            }
                        }
                        reader.close();
                        writer.close();

                        inputFile.delete();
                        tempFile.renameTo(inputFile);

                        JOptionPane.showMessageDialog(this, "Item deleted successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
                        loadItemsForDeletion();
                        selectedItemField.setText("");
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            } else {
                JOptionPane.showMessageDialog(this, "Please select an item to delete!", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("Manage Food Items");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().add(new ManageFoodItemsPanel());
        frame.pack();
        frame.setVisible(true);
    }
}
