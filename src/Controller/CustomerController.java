package Controller;

import java.io.*;

import Components.CartTablePanel;

public class CustomerController{

    public CustomerController(){
        
    }

    static File file = new File("orderList.txt");
    public static void processBuy(String lineContent, int lineNumber,String id){
        // Split the line content by ";" to get individual parts
        String[] parts = lineContent.split(";");

        try (FileWriter writer = new FileWriter(file,true)) {
            if (!file.exists()) {
                file.createNewFile();
            }
            int i=0;
            while (i<2) {
                    writer.write(parts[i]+";");
                    i++;
                }
                writer.write("\n");
            } catch (IOException e) {
            e.printStackTrace();
        }
        new CartTablePanel("Customer");
    }
    
}