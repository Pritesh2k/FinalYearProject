package com.example.finalyearproject;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class PoliceData {
    private static String file_path = "E://FYP/2022-11-city-of-london-street - 2022-11-city-of-london-street.csv";

    public static ArrayList<String> Longitude = new ArrayList<>();
    public static ArrayList<String> Latitude = new ArrayList<>();
    public static ArrayList<String> Location = new ArrayList<>();
    public static ArrayList<String> CrimeType = new ArrayList<>();
    public static ArrayList<String> LOC = new ArrayList<>();

    public static ArrayList<String> LOC_messages = new ArrayList<String>();
    public static ArrayList<String> PackagedData = new ArrayList<>();
    public static int Length_Of_DataSet;

    public static void CreateLOCMEssages(){
        LOC_messages.add("Action to be taken by another organisation");
        LOC_messages.add("Awaiting court outcome");
        LOC_messages.add("Further investigation is not in the public interest");
        LOC_messages.add("Investigation complete; no suspect identified");
        LOC_messages.add("Offender given a caution");
        LOC_messages.add("Offender given a drugs possession warning");
        LOC_messages.add("Unable to prosecute suspect");
        LOC_messages.add("Under investigation");
        LOC_messages.add("Not data found");
    }

    public static void GetCrimedata(){
        getCrimeData();
        getLOCData();
        Length_Of_DataSet = Location.size();
    }

    public static ArrayList<String> PackagedData(){
        for (int counter = 1; counter < CrimeType.size(); counter++) {

            if (counter < Latitude.size() && counter < Longitude.size()) {
                PackagedData.add(CrimeType.get(counter));
                PackagedData.add(Longitude.get(counter));
                PackagedData.add(Latitude.get(counter));
                PackagedData.add(Location.get(counter));

                if (LOC.get(counter).equals("1")) {
                    PackagedData.add(LOC_messages.get(0));
                } else if (LOC.get(counter).equals("2")) {
                    PackagedData.add(LOC_messages.get(1));
                } else if (LOC.get(counter).equals("3")) {
                    PackagedData.add(LOC_messages.get(2));
                } else if (LOC.get(counter).equals("4")) {
                    PackagedData.add(LOC_messages.get(3));
                } else if (LOC.get(counter).equals("5")) {
                    PackagedData.add(LOC_messages.get(4));
                } else if (LOC.get(counter).equals("6")) {
                    PackagedData.add(LOC_messages.get(5));
                } else if (LOC.get(counter).equals("7")) {
                    PackagedData.add(LOC_messages.get(6));
                } else if (LOC.get(counter).equals("8")) {
                    PackagedData.add(LOC_messages.get(7));
                } else if (LOC.get(counter).equals("9")) {
                    PackagedData.add(LOC_messages.get(8));
                }

                return PackagedData;
            }
        }
        return null;
    }

    public static void getCrimeData() {
        try (BufferedReader br = new BufferedReader(new FileReader(file_path))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] values = line.split(",");

                //Crime Type
                if (values[9].equals("") || values[9] == null) {
                    CrimeType.add("");
                } else {
                    CrimeType.add(values[9]);
                }

                //Long
                if (values[4].equals("")) {
                    Longitude.add("No data found");
                } else {
                    Longitude.add(values[4]);
                }

                //Lat
                if (values[5].equals("")) {
                    Latitude.add("No data found");
                } else {
                    Latitude.add(values[5]);
                }

                //Location
                if (values[6].equals("")) {
                    Location.add("No data found");
                } else {
                    Location.add(values[6]);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public static void getLOCData() {
        try (BufferedReader br = new BufferedReader(new FileReader(file_path))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] values = line.split(",");

                LOC.add(values[0]);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
