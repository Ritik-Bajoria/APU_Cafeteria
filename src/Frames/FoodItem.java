package Frames;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class FoodItem{

    public FoodItem(){
        //Create default menu
        File file=new File("foodItem.txt");
        try{
            if(!file.exists()){
                file.createNewFile();
                FileWriter fw = new FileWriter(file, true);
                fw.write("Veg Mo:Mo; Rs.120; 0\n");
                fw.write("Veg Chowmein; Rs.100; 0\n");
                fw.write("Coffee; Rs.60; 0\n");
                fw.write("Pasta; Rs.150; 0\n");
                fw.write("Pizza; Rs.540; 0\n");
                fw.write("MilkShake; Rs.100; 0\n");
                fw.write("Sphaggeti; Rs.450; 0\n");
                fw.write("Veg Sandwich; Rs.100; 0\n");
                fw.write("Buff Mo:Mo; Rs.160; 0\n");
                fw.write("Chicken Sandwich; Rs.140; 0\n");
                fw.write("Ceasar Salad; Rs.350; 0\n");
                fw.write("Soft Drinks; Rs.150; 0\n");
                fw.close();  
            }
        } catch(IOException e){
            e.printStackTrace();
        }
    }
}