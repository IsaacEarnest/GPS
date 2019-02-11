package com.example.gps;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Scanner;

class DataManager {
    private File saveDir;
     public DataManager(File saveDir){
         this.saveDir = saveDir;
     }
     public void savePlace(place p) throws IOException {
         File output = new File(saveDir, p.getName());
         PrintWriter pw = new PrintWriter(new FileWriter(output));
         pw.println(p.toString());
     }
     public place openPlace(String name) throws IOException{
         File input = new File(saveDir,name);
         Scanner s = new Scanner(input);
         String text = s.nextLine();
         s.close();
         return place.parse(text);
     }
}
