package Frames;

import java.util.*;
import java.io.*;
import java.awt.*;
import javax.swing.*;
import javax.swing.border.*;
import Components.*;


public class Register{


    JTextField enterName;
    JTextField enterNumber;
    JTextField enterMail;
    JTextField enteraddress;
    JPasswordField pass;
    JPasswordField confirmPass;
    JFrame registerFrame;

    //Constructor for UI
    public Register(){
        //Creating the Register frame 
        registerFrame = new JFrame("APU Cafeteria");
        registerFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        Container contentPane = registerFrame.getContentPane();
        contentPane.setBackground(new Color(56, 34, 14));
        registerFrame.setSize(800,600);
        contentPane.setLayout(new GridBagLayout());

        //Creating a panel inside register frame
        JPanel registerPanel = new JPanel();
        contentPane.add(registerPanel);

        //Creating GridBagConstraints
        GridBagConstraints constraints = new GridBagConstraints();

        //for extra available space provided horizontally
        constraints.fill=GridBagConstraints.HORIZONTAL;

        registerPanel.setLayout(new GridBagLayout());
        registerPanel.setBackground(new Color(252,248,186));

        //Adding components to the panel inside Register frame
        JLabel name=new JLabel("Name:");
        name.setFont(CustomComponent.labelFont);
        CustomComponent.addComponent(registerPanel, name, constraints, 0, 0, 1, 1);

        enterName= new JTextField();
        enterName.setPreferredSize(new Dimension(180,30));
        enterName.setFont(CustomComponent.fieldFont);
        CustomComponent.addComponent(registerPanel, enterName, constraints,1, 0, 1, 1);

        //to add black space in between
        constraints.insets = new Insets(15,0,0,0);

        JLabel number=new JLabel("Contact Number:");
        number.setFont(CustomComponent.labelFont);
        CustomComponent.addComponent(registerPanel, number, constraints, 0, 1, 1, 1);

        enterNumber= new JTextField();
        enterNumber.setPreferredSize(new Dimension(180,30));
        enterNumber.setFont(CustomComponent.fieldFont);
        CustomComponent.addComponent(registerPanel, enterNumber, constraints,1, 1, 1, 1);

        JLabel mail=new JLabel("Contact mail:");
        mail.setFont(CustomComponent.labelFont);
        CustomComponent.addComponent(registerPanel, mail, constraints, 0, 2, 1, 1);

        enterMail= new JTextField();
        enterMail.setPreferredSize(new Dimension(180,30));
        enterMail.setFont(CustomComponent.fieldFont);
        CustomComponent.addComponent(registerPanel, enterMail, constraints,1, 2, 1, 1);        
        
        JLabel address=new JLabel("Address:");
        address.setFont(CustomComponent.labelFont);
        CustomComponent.addComponent(registerPanel, address, constraints, 0, 3, 1, 1);

        enteraddress= new JTextField();
        enteraddress.setPreferredSize(new Dimension(180,30));
        enteraddress.setFont(CustomComponent.fieldFont);
        CustomComponent.addComponent(registerPanel, enteraddress, constraints,1, 3, 1, 1);

        JLabel passLabel=new JLabel("Password:");
        passLabel.setFont(CustomComponent.labelFont);
        CustomComponent.addComponent(registerPanel, passLabel, constraints, 0, 4, 1, 1);

        pass=new JPasswordField();
        pass.setFont(CustomComponent.fieldFont);
        pass.setPreferredSize(new Dimension(180,30));
        CustomComponent.addComponent(registerPanel, pass, constraints, 1, 4, 1, 1); 

        JLabel confirm=new JLabel("Confirm Password:");
        confirm.setFont(CustomComponent.labelFont);
        CustomComponent.addComponent(registerPanel, confirm, constraints, 0, 5, 1, 1);

        confirmPass=new JPasswordField();
        confirmPass.setFont(CustomComponent.fieldFont);
        confirmPass.setPreferredSize(new Dimension(180,30));
        CustomComponent.addComponent(registerPanel, confirmPass, constraints, 1, 5, 1, 1); 

        JButton registerBtn = new JButton("Register");
        registerBtn.setFont(CustomComponent.btnFont);
        registerBtn.setBackground(new Color(0,200,200));
        registerBtn.setForeground(Color.WHITE);
        registerBtn.setBorderPainted(false);
        registerBtn.setFocusPainted(false);
        registerBtn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        CustomComponent.addComponent(registerPanel, registerBtn, constraints, 0, 6, 2, 1);
        registerBtn.addActionListener(ActionEvent ->{processRegister();});

        JButton loginBtn = new JButton("Back to login");
        loginBtn.setFont(CustomComponent.btnFont);
        loginBtn.setBackground(new Color(0,87,225));
        loginBtn.setForeground(Color.WHITE);
        loginBtn.setBorderPainted(false);
        loginBtn.setFocusPainted(false);
        loginBtn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        CustomComponent.addComponent(registerPanel, loginBtn, constraints, 0, 7, 2, 1);
        loginBtn.addActionListener(ActiveEvent ->{
            registerFrame.dispose(); 
            new Login();
        });

        registerPanel.setPreferredSize(new Dimension(400, 400));
        // registerPanel.setBackground(Color.RED);
        registerPanel.setBorder(new LineBorder(Color.gray,2));
        constraints.fill=GridBagConstraints.HORIZONTAL;
        constraints.anchor=GridBagConstraints.CENTER;
        
        registerFrame.setLocationRelativeTo(null);
        registerFrame.setVisible(true);
    }

    public boolean processRegister() {
        String name = enterName.getText();
        String number =enterNumber.getText();
        String mail= enterMail.getText();
        String address = enteraddress.getText();
        String password = pass.getText();
        String confirmPassword = confirmPass.getText();
        try{
            File file=new File("accountFile.txt");
            if (!file.exists()) {
                file.createNewFile();
            }
            FileWriter fw = new FileWriter(file, true);
            if(confirmPassword.compareTo(password)==0){
                if(!(isRegistered(number))){
                    fw.write(name+";"+number+";"+mail+";"+address+";"+password+"\n");
                    fw.close();  
                    JOptionPane.showMessageDialog(new JPanel(), "Registration Successful.");
                    registerFrame.dispose();
                    new Customer(name);
                } else{
                    JOptionPane.showMessageDialog(new JPanel(), "Registration UnSuccessful ! \n The enetered phone number is already registered.");
                }
            }
            else{
                JOptionPane.showMessageDialog(new JPanel(), "Registration Unsuccessful ! \n You entered wrong password in the confirmation Field");
            }
        } 
        catch (IOException e) {
            e.printStackTrace();
        }
        return true;
    }

    public static boolean isRegistered(String number){
        try{
            File file=new File("accountFile.txt");
            if (!file.exists()) {
                file.createNewFile();
            }
            Scanner sc= new Scanner(file);
            while(sc.hasNextLine()){
                String line= sc.nextLine();
                String[] parts = line.split(";");
                if(parts[1].compareTo(number)==0){
                    sc.close();
                    return true; //number already exits
                }

            }
        }
        catch(IOException e){
            e.printStackTrace();
        }   
        return false;
    }
    
    public static void main(String args[]){
        new Register();
    } 
}