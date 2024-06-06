package Frames;

import Controller.ManagerAccountPanel;
import Controller.ManagerController;
import java.util.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import javax.swing.*;
import javax.swing.border.*;
import Components.*;

public class Manager implements ActionListener {

    ArrayList<String> foodList;
    JButton[] editBtn;
    static JFrame managerFrame;
    JPanel editPanel; // Renamed managerPanel to editPanel

    public Manager() {

        // Create default manager login
        createManager();

        // Creating the MANAGER frame 
        managerFrame = new JFrame("APU Cafeteria (Logged in as Manager)");
        managerFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        Container contentPane = managerFrame.getContentPane();
        contentPane.setBackground(new Color(56, 34, 14));
        managerFrame.setSize(800, 600);
        managerFrame.setLayout(new GridBagLayout());

        // Creating a tabbed pane inside Manager frame
        JTabbedPane tabbedPane = new JTabbedPane();

        // Creating edit panel
        editPanel = new JPanel();
        editPanel.setLayout(new BorderLayout());
        editPanel.setPreferredSize(new Dimension(700, 500));
        // editPanel.setBorder(new LineBorder(Color.LIGHT_GRAY, 1));

        // Create a JScrollPane and add it to the edit panel
        JScrollPane scrollPane = new JScrollPane();
        editPanel.add(scrollPane, BorderLayout.CENTER);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);

        JPanel menuPanel = new JPanel();
        menuPanel.setLayout(new GridBagLayout());
        menuPanel.setBackground(new Color(252, 248, 186));

        scrollPane.setViewportView(menuPanel);

        // Create Fooditem class
        FoodItem food = new FoodItem();
        File file = new File("foodItem.txt");
        foodList = new ArrayList<>();
        try {
            Scanner sc = new Scanner(file);
            while (sc.hasNextLine()) {
                String line = sc.nextLine();
                foodList.add(line);
            }
            sc.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Creating GridBagConstraints
        GridBagConstraints constraints = new GridBagConstraints();
        JPanel itemPanel[] = new JPanel[foodList.size()];
        int x_axis = 0, y_axis = 0;
        editBtn = new JButton[foodList.size()];
        for (int i = 0; i < foodList.size(); i++) {
            x_axis++;
            itemPanel[i] = new JPanel();
            String[] itemDetails = foodList.get(i).split(";");
            itemPanel[i].setLayout(new GridBagLayout());
            for (int j = 0; j < itemDetails.length; j++) {
                if (j == 0) {
                    constraints.insets = new Insets(0, 0, 0, 5);
                    JLabel itemLabel = new JLabel(itemDetails[j]);
                    itemLabel.setFont(CustomComponent.labelFont);
                    constraints.anchor = GridBagConstraints.WEST;
                    CustomComponent.addComponent(itemPanel[i], itemLabel, constraints, 0, 0, 1, 1); // j for y_axis
                } else if (j == 1) {
                    JLabel itemLabel = new JLabel(itemDetails[j]);
                    constraints.insets = new Insets(10, 0, 0, 5);
                    itemLabel.setFont(new Font("Arial", Font.BOLD, 10));
                    constraints.anchor = GridBagConstraints.WEST;
                    itemLabel.setForeground(new Color(150, 99, 99));
                    CustomComponent.addComponent(itemPanel[i], itemLabel, constraints, 0, 2, 1, 1);
                }  else {
                    constraints.insets = new Insets(10, 0, 0, 0);
                    JLabel item = new JLabel("Ratings:" + itemDetails[j]);
                    item.setFont(new Font("Arial", Font.PLAIN + Font.BOLD, 12));
                    constraints.anchor = GridBagConstraints.WEST;
                    CustomComponent.addComponent(itemPanel[i], item, constraints, 0, 3, 1, 1);
                }
            }
            constraints.insets = new Insets(30, 10, 0, 10);
            editBtn[i] = CustomComponent.createJButton("Edit Info", new Font("Inter Semi Bold", Font.BOLD, 12),
                    new Dimension(80, 25), Color.WHITE, new Color(50, 120, 50), false, false);
            CustomComponent.addComponent(itemPanel[i], editBtn[i], constraints, 0, 4, 2, 1);
            editBtn[i].addActionListener(this);
            itemPanel[i].setBorder(new LineBorder(Color.GRAY));
            itemPanel[i].setPreferredSize(new Dimension(150, 150));
            constraints.gridx = x_axis;
            constraints.gridy = y_axis;
            constraints.gridheight = 1;
            constraints.gridwidth = 1;
            constraints.insets = new Insets(10, 10, 10, 10);
            menuPanel.add(itemPanel[i], constraints);
            if ((x_axis % 4) == 0) {
                y_axis++;
                x_axis = 0;
            }
        }

        // for extra available space provided horizontally
        constraints.fill = GridBagConstraints.HORIZONTAL;
        UserPanel usertabbledPanel= new UserPanel();

        tabbedPane.addTab("Edit Menu", editPanel); // Add editPanel to the tabbed pane
        // Add other panels to the tabbed pane here if needed
        tabbedPane.addTab("User Details", usertabbledPanel);
        tabbedPane.addTab("Manage Items", new ManageFoodItemsPanel());
        tabbedPane.addTab("Order Details",new OrderDetailsPanel());
        tabbedPane.addTab("View Feedback", new Feedback());
        tabbedPane.addTab("Change password", new ManagerAccountPanel());
        
        JButton logoutButton = CustomComponent.createJButton("Logout", CustomComponent.btnFont, new Dimension(100,26), Color.BLACK, 
        new Color(252, 248, 186), true, false);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx=0;
        gbc.gridy=0;
        gbc.anchor=GridBagConstraints.EAST;
        logoutButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int confirmLogout = JOptionPane.showConfirmDialog(managerFrame,
                        "Are you sure you want to logout?", "Logout Confirmation", JOptionPane.YES_NO_OPTION);
                if (confirmLogout == JOptionPane.YES_OPTION) {
                    managerFrame.dispose(); // Close the managerFrame
                    new Login(); // Open the login screen
                }
            }
        });
        managerFrame.add(logoutButton,gbc);
        gbc.gridx=0;
        gbc.gridy=1;
        gbc.anchor=GridBagConstraints.CENTER;
        managerFrame.add(tabbedPane,gbc);
        managerFrame.setLocationRelativeTo(null);
        managerFrame.setResizable(false);
        managerFrame.setVisible(true);
    }

    public void actionPerformed(ActionEvent e) {
        for (int i = 0; i < editBtn.length; i++) {
            if (e.getSource() == editBtn[i]) {
                ManagerController.processEdit(foodList.get(i), i);
                managerFrame.dispose();
                new Manager();
                break; // Exit the loop once the button is found
            }
        }
    }

    public static void createManager() {
        try {
            File file = new File("managerAccount.txt");
            if (!(file.exists() && file.length() > 0)) {
                file.createNewFile();
                FileWriter fw = new FileWriter(file, true);
                fw.write("manager;12345\n");
                fw.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void refreshManager(){
        managerFrame.getContentPane().repaint();
    }

    public static void main(String args[]) {
        new Manager();
    }
}
