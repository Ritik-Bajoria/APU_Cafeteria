package Components;

import java.util.*;
import java.awt.*;
import javax.swing.*;

public class CustomComponent{

    public static Font labelFont = new Font("Arial Rounded MT Bold", Font.PLAIN, 14);
    public static Font fieldFont = new Font("Arial", Font.PLAIN, 14);
    public static Font btnFont = new Font("Inter Semi Bold", Font.BOLD, 14);
    // Method to add a component to the container with specified constraints using GridBagLayout
    public static void addComponent(Container container, Component component, GridBagConstraints constraints,
		int gridx, int gridy, int gridwidth, int gridheight) {
	
			// Set the position of the component in the grid
			constraints.gridx = gridx;
			constraints.gridy = gridy;
	
			// Set the number of cells that the component will occupy horizontally and vertically
			constraints.gridwidth = gridwidth;
			constraints.gridheight = gridheight;
	
			// Add the component to the container with the specified constraints
			container.add(component, constraints);
		}    
		
		
		//create custom JButton 
		public static JButton createJButton(String text,Font font,
        Dimension dimension,Color foregroundColor,Color backgroundColor, boolean borderPainted, boolean focusPainted){
        JButton jButton = new JButton(text);
        jButton.setFont(font);
        jButton.setMargin(new Insets(0, 0, 0, 0));
        jButton.setPreferredSize(dimension);
        jButton.setForeground(foregroundColor);
        jButton.setBackground(backgroundColor);
        jButton.setBorderPainted(borderPainted);
        jButton.setFocusPainted(focusPainted);
        jButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        return jButton;
    }
}