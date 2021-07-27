package com.xampy.piigo.controllers.userdataset;

import java.util.ArrayList;

public class VehicueIdentifierController {

    public static boolean checkVehicleIdentifier(String identifier) {

        ArrayList<String> characters_numbers = new ArrayList<>();
        characters_numbers.add("0");
        characters_numbers.add("1");
        characters_numbers.add("2");
        characters_numbers.add("3");
        characters_numbers.add("4");
        characters_numbers.add("5");
        characters_numbers.add("6");
        characters_numbers.add("7");
        characters_numbers.add("8");
        characters_numbers.add("9");


        ArrayList<String> alphabets = new ArrayList<>();
        alphabets.add("a");
        alphabets.add("b");
        alphabets.add("c");
        alphabets.add("d");
        alphabets.add("e");
        alphabets.add("f");
        alphabets.add("g");
        alphabets.add("h");
        alphabets.add("i");
        alphabets.add("j");
        alphabets.add("k");
        alphabets.add("l");
        alphabets.add("m");
        alphabets.add("n");
        alphabets.add("o");
        alphabets.add("p");
        alphabets.add("q");
        alphabets.add("r");
        alphabets.add("s");
        alphabets.add("t");
        alphabets.add("u");
        alphabets.add("v");
        alphabets.add("w");
        alphabets.add("x");
        alphabets.add("y");
        alphabets.add("z");




        String ident = identifier.toLowerCase();

        //Check the first two letter to be equal to TG
        if (ident.length() == 10 && ident.startsWith("tg ")){

            for (int i = 3; i < 7; i++) {
                if (!characters_numbers.contains(String.valueOf(ident.charAt(i)))) {
                    //The number dos'nt contain an numeric char
                    //return false
                    return false;
                }
            }

            if(ident.charAt(7) == ident.charAt(2)) {
                //We have an space

                for (int i = 8; i < 10; i++) {
                    if (!alphabets.contains(String.valueOf(ident.charAt(i)))) {
                        //The number dos'nt contain an numeric char
                        //return false
                        return false;
                    }
                }


                //All thing is correct
                return true;
            }

        }

        //otherwise
        return false;
    }
}
