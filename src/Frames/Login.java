package Frames;

import java.util.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import javax.swing.*;
import javax.swing.border.*;
import Components.*;

public class Login{

    //Constructor for UI
    public Login(){
        //Creating the login frame 
        JFrame loginFrame = new JFrame("APU Cafeteria");
        loginFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        Container contentPane = loginFrame.getContentPane();
        contentPane.setBackground(new Color(56, 34, 14));
        loginFrame.setSize(800,600);
        contentPane.setLayout(new GridBagLayout());

        //Creating a panel inside login frame
        JPanel loginPanel = new JPanel();
        contentPane.add(loginPanel);

        //Creating GridBagConstraints
        GridBagConstraints constraints = new GridBagConstraints();

        //for extra available space provided horizontally
        constraints.fill=GridBagConstraints.HORIZONTAL;
        // constraints.anchor=GridBagConstraints.CENTER;

        loginPanel.setLayout(new GridBagLayout());
        loginPanel.setBackground(new Color(252,248,186));

        //Adding components to the panel inside login frame
        JLabel userLabel=new JLabel("Username:");
        userLabel.setFont(CustomComponent.labelFont);
        CustomComponent.addComponent(loginPanel, userLabel, constraints, 0, 0, 1, 1);

        JTextField user= new JTextField();
        user.setPreferredSize(new Dimension(180,30));
        user.setFont(CustomComponent.fieldFont);
        CustomComponent.addComponent(loginPanel, user, constraints,1, 0, 1, 1);

        //to add black space in between
        constraints.insets = new Insets(20,0,0,0);

        JLabel passLabel=new JLabel("Password:");
        passLabel.setFont(CustomComponent.labelFont);
        CustomComponent.addComponent(loginPanel, passLabel, constraints, 0, 1, 1, 1);

        JPasswordField pass=new JPasswordField();
        pass.setFont(CustomComponent.fieldFont);
        pass.setPreferredSize(new Dimension(180,30));
        CustomComponent.addComponent(loginPanel, pass, constraints, 1, 1, 1, 1); 


        JButton loginBtn = new JButton("login");
        loginBtn.setFont(CustomComponent.btnFont);
        loginBtn.setBackground(new Color(2,64,74));
        loginBtn.setForeground(Color.WHITE);
        loginBtn.setBorderPainted(false);
        loginBtn.setFocusPainted(false);
        loginBtn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        CustomComponent.addComponent(loginPanel, loginBtn, constraints, 0, 2, 2, 1);
        loginBtn.addActionListener(ActionListener ->{
            String id = user.getText();
            String password = pass.getText();
            if(isRegisteredAsCustomer(id , password)){
                loginFrame.dispose();
                new Customer(id);
            }
            else if(isRegisteredAsManager(id , password)){
                loginFrame.dispose();
                new Manager();
            }
            else{
                JOptionPane.showMessageDialog(new JPanel(), "Login Unsuccessful ! \n Entered username or password is incorrect");
            }
        });

        JButton registerBtn = CustomComponent.createJButton("Don't have an account? Register here",
        new Font("Inter Semi Bold",Font.ITALIC,14), null, Color.BLACK, null, false, false);
        CustomComponent.addComponent(loginPanel, registerBtn, constraints, 0, 3, 2, 1);
        registerBtn.addActionListener(ActionListener ->{loginFrame.dispose(); new Register();});

        loginPanel.setPreferredSize(new Dimension(400, 400));
        // loginPanel.setBackground(Color.RED);
        loginPanel.setBorder(new LineBorder(Color.gray,2));
        constraints.fill=GridBagConstraints.HORIZONTAL;
        constraints.anchor=GridBagConstraints.CENTER;

        // Get the screen size
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        System.out.println(screenSize);

        // Calculate the position to center the frame
        int x = (screenSize.width - loginFrame.getWidth()) / 2;
        int y = (screenSize.height - loginFrame.getHeight()) / 2;
        loginFrame.setLocation(x,y);
        loginFrame.setVisible(true);
    }
    public boolean isRegisteredAsCustomer(String id, String pass){
        try{
            File file=new File("accountFile.txt");
            if (!file.exists()) {
                file.createNewFile();
            }
            Scanner sc= new Scanner(file);
            while(sc.hasNextLine()){
                String line= sc.nextLine();
                String[] parts = line.split(";");
                if((parts[0].compareToIgnoreCase(id)==0) && (parts[4].compareTo(pass)==0)){
                    sc.close();
                    return true; //user verified
                }
            }
        }
        catch(IOException e){
            e.printStackTrace();
        }   
        return false;

    }

    
    public boolean isRegisteredAsManager(String id, String pass){
        try{
            File file=new File("managerAccount.txt");
            if (!file.exists()) {
                file.createNewFile();
            }
            Scanner sc= new Scanner(file);
            while(sc.hasNextLine()){
                String line= sc.nextLine();
                String[] parts = line.split(";");
                if((parts[0].compareToIgnoreCase(id)==0) && (parts[1].compareTo(pass)==0)){
                    sc.close();
                    return true; //user verified
                }
            }
        }
        catch(IOException e){
            e.printStackTrace();
        }   
        return false;

    }

    public static void main(String args[]){
        new Login();
    } 
}