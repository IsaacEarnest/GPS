package com.example.gps;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Scanner;

class DataManager {
    private File saveDirPlace;
    private File saveDirPath;

    public static final String PLACE_DIR = "places";
    public static final String PATH_DIR = "paths";

     public DataManager(File saveDir){
         this.saveDirPlace = new File(saveDir, PLACE_DIR);
         if (!saveDirPlace.exists()) {
             saveDirPlace.mkdir();
         }

         this.saveDirPath = new File(saveDir, PATH_DIR);
         if (!saveDirPath.exists()) {
             saveDirPath.mkdir();
         }

     }
        //Place
     public void savePlace(Place p) throws IOException {
         File output = new File(saveDirPlace, p.getName());
         PrintWriter pw = new PrintWriter(new FileWriter(output));
         pw.println(p.toString());
         pw.close();
     }
     public Place openPlace(String name) throws IOException {
         return openPlace(new File(saveDirPlace, name));
     }

     public Place openPlace(File input) throws IOException {
        Scanner s = new Scanner(input);
        if (s.hasNextLine()) {
            String text = s.nextLine();
            s.close();
            return Place.parse(text);
        } else {
            return null;
        }
    }
    public ArrayList<Place> loadAllPlaces() throws IOException{
        ArrayList<Place> result = new ArrayList<>();
        for(File f: saveDirPlace.listFiles()){
            if (f.isFile()) {
                Place p = openPlace(f);
                if (p != null)
                    result.add(p);
            }
        }
        return result;
    }
     //path
    public void savePath(Path p) throws IOException {
        File output = new File(saveDirPath, p.getStart().getName()+p.getDestination().getName());
        PrintWriter pw = new PrintWriter(new FileWriter(output));
        pw.println(p.toString());
        pw.close();
    }
    public Path openPath(String name) throws IOException {
        return openPath(new File(saveDirPath, name));
    }

    public Path openPath(File input) throws IOException {
        Scanner s = new Scanner(input);
        String text = s.nextLine();
        s.close();
        return Path.parse(text);
    }
    public ArrayList<Path> loadAllPaths() throws IOException{
        ArrayList<Path> result = new ArrayList<>();
        for(File f: saveDirPath.listFiles()){
            result.add(openPath(f));
        }
        return result;
    }


}
