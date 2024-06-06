package Frames;

import Components.*;
import Controller.*;

import java.util.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import javax.swing.*;
import javax.swing.border.*;

public class Customer implements ActionListener{
    ArrayList<String> foodList;
    JButton[] buyBtn;
    JFrame customerFrame;
    CartTablePanel ctpanel;
    JScrollPane scrollPane1;
    static String id1;

    public Customer(String id){
        id1=id;
    File f = new File("orderList.txt");  
    try{
        FileWriter writer = new FileWriter(f, false);


        // Close the writer
        writer.close();

    } catch(IOException e){
        e.printStackTrace();
    }
    //Creating the customer frame 
    customerFrame = new JFrame("APU Cafeteria: Customer Login- "+(id));
    customerFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    Container contentPane = customerFrame.getContentPane();
    contentPane.setBackground(new Color(56, 34, 14));
    customerFrame.setSize(900,600);
    contentPane.setLayout(new GridBagLayout());

    // Creating a panel inside customer frame
    JPanel customerPanel = new JPanel();
    customerPanel.setLayout(new BorderLayout());
    customerPanel.setPreferredSize(new Dimension(540, 520));
    customerPanel.setBorder(new LineBorder(Color.LIGHT_GRAY, 2));

    // Create a JScrollPane and add it to the panel
    JScrollPane scrollPane = new JScrollPane(); 
    customerPanel.add(scrollPane, BorderLayout.CENTER);
    scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);

    JPanel menuPanel =new JPanel();
    menuPanel.setLayout(new GridBagLayout());
    menuPanel.setBackground(new Color(252,248,186));

    scrollPane.setViewportView(menuPanel);

    //Create Fooditem class
    FoodItem food=new FoodItem();
    File file=new File("foodItem.txt");
    foodList= new ArrayList<>();
    try{
        Scanner sc= new Scanner(file);
        while(sc.hasNextLine()){
            String line = sc.nextLine();
            foodList.add(line);
        }
        sc.close();
    } catch(IOException e){
        e.printStackTrace();
    }

    //Creating GridBagConstraints
    GridBagConstraints constraints = new GridBagConstraints();
    JPanel itemPanel[]= new JPanel[foodList.size()];
    int x_axis=0,y_axis=0;
    buyBtn= new JButton[foodList.size()];
    for(int i=0; i<foodList.size(); i++){
        x_axis++;
        itemPanel[i]= new JPanel();
        String[] itemDetails = foodList.get(i).split(";");
        itemPanel[i].setLayout(new GridBagLayout());
        for(int j=0; j<itemDetails.length ; j++){
            if(j==0){
                constraints.insets=new Insets(0, 0, 0, 5);
                JLabel itemLabel=new JLabel(itemDetails[j]);
                itemLabel.setFont(CustomComponent.labelFont);
                constraints.anchor = GridBagConstraints.WEST;
                CustomComponent.addComponent(itemPanel[i], itemLabel, constraints, 0, 0, 1, 1); //j for y_axis
            } else if(j==1){
                JLabel itemLabel = new JLabel(itemDetails[j]);
                constraints.insets=new Insets(10, 0, 0, 5);
                itemLabel.setFont(new Font("Arial", Font.BOLD, 10));
                constraints.anchor = GridBagConstraints.WEST;
                itemLabel.setForeground(new Color(150,99,99));
                CustomComponent.addComponent(itemPanel[i], itemLabel, constraints, 0, 2, 1, 1);
            } else{
                constraints.insets=new Insets(10, 0, 0, 0);
                JLabel item = new JLabel("Ratings:"+itemDetails[j]);
                item.setFont(new Font("Arial", Font.PLAIN +Font.BOLD, 12));
                constraints.anchor = GridBagConstraints.WEST;
                CustomComponent.addComponent(itemPanel[i], item, constraints, 0, 3, 1, 1);
            }
        } 
        constraints.insets=new Insets(30, 10, 0, 10);
        buyBtn[i] = CustomComponent.createJButton("Add to Cart", new Font("Inter Semi Bold", Font.BOLD, 12), 
        new Dimension(80,25), Color.WHITE, new Color(50,120,50), false, false);
        CustomComponent.addComponent(itemPanel[i], buyBtn[i], constraints, 0, 4, 2, 1);
        buyBtn[i].addActionListener(this);
        itemPanel[i].setBorder(new LineBorder(Color.GRAY));
        itemPanel[i].setPreferredSize(new Dimension(150,150));
        constraints.gridx=x_axis;
        constraints.gridy=y_axis;
        constraints.gridheight=1;
        constraints.gridwidth=1;
        constraints.insets=new Insets(10, 10, 10, 10);
        menuPanel.add(itemPanel[i],constraints);
        if((x_axis%3)==0){
            y_axis++;
            x_axis=0;
        }
    }

    // for extra available space provided horizontally
    // constraints.fill=GridBagConstraints.NONE;
    constraints.gridx=0;
    constraints.gridy=0;
    constraints.gridheight=1;
    constraints.gridwidth=1;
    constraints.insets=new Insets(0, 0, 0, 0);
    contentPane.add(customerPanel, constraints);

    JPanel cartPanel=new JPanel();
    cartPanel.setPreferredSize(new Dimension(310,300));
    cartPanel.setBorder(new LineBorder(Color.BLACK, 2));
    cartPanel.setLayout(new GridBagLayout());
    JLabel cartLabel= new JLabel("Your Cart");
    constraints.fill = GridBagConstraints.HORIZONTAL;
    cartLabel.setFont(CustomComponent.labelFont);
    CustomComponent.addComponent(cartPanel, cartLabel, constraints, 0, 1, 1, 1);

    JPanel outerCartPanel=new JPanel();
    outerCartPanel.setLayout(new BorderLayout());
    scrollPane1 = new JScrollPane();
    outerCartPanel.add(scrollPane1, BorderLayout.CENTER);
    
    outerCartPanel.setPreferredSize(new Dimension(280,240));
    scrollPane1.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);

    // innerCartPanel.setLayout(new GridBagLayout());
    ctpanel=new CartTablePanel(id);
    scrollPane1.setViewportView(ctpanel);
    // innerCartPanel.setBackground(Color.RED);
    // innerCartPanel.add(table);
    constraints.insets=new Insets(0, 0, 0, 0);
    constraints.fill = GridBagConstraints.BOTH;
    CustomComponent.addComponent(cartPanel, outerCartPanel, constraints, 0, 2, 1, 1);
    CustomComponent.addComponent(cartPanel, new ProductRatingPanel(), constraints, 0, 3, 1, 1);
    constraints.gridx=1;
    constraints.gridy=0;
    constraints.gridheight=2;
    constraints.gridwidth=1;
    constraints.insets=new Insets(0, 0, 0, 0);
    constraints.fill = GridBagConstraints.BOTH;
    contentPane.add(cartPanel,constraints);
    JButton logoutButton = CustomComponent.createJButton("Logout", CustomComponent.btnFont, new Dimension(100,26), Color.BLACK, 
    new Color(252, 248, 186), true, false);
    GridBagConstraints gbc = new GridBagConstraints();
    gbc.gridx=0;
    gbc.gridy=0;
    gbc.anchor=GridBagConstraints.EAST;
    logoutButton.addActionListener(new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            int confirmLogout = JOptionPane.showConfirmDialog(customerFrame,
                    "Are you sure you want to logout?", "Logout Confirmation", JOptionPane.YES_NO_OPTION);
            if (confirmLogout == JOptionPane.YES_OPTION) {
                customerFrame.dispose(); // Close the managerFrame
                new Login(); // Open the login screen
            }
        }
    });
    cartPanel.add(logoutButton,gbc);
    customerFrame.setLocationRelativeTo(null);
    customerFrame.setResizable(false);
    customerFrame.setVisible(true);
    }

    public void actionPerformed(ActionEvent e) {
        for(int i = 0; i < buyBtn.length; i++) {
            if(e.getSource() == buyBtn[i]) {
                CustomerController.processBuy(foodList.get(i), i,id1);
                ctpanel=new CartTablePanel(id1);
                scrollPane1.setViewportView(ctpanel);
                break; // Exit the loop once the button is found
            }
        }
    }

    public static void main(String args[]){
        new Customer("Demo Customer");
    }
}