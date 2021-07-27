package com.xampy.piigo.controllers.registerController;

import java.util.ArrayList;

public class RegisterController {

    public static boolean checkPhoneNumber (String number){

        ArrayList<String> characters = new ArrayList<>();
        characters.add("0");
        characters.add("1");
        characters.add("2");
        characters.add("3");
        characters.add("4");
        characters.add("5");
        characters.add("6");
        characters.add("7");
        characters.add("8");
        characters.add("9");

        //Check if the length is equal to 8
        if (number.length() == 8) {

            //che
            for (int i = 0; i < 8 ; i++) {
                if (!characters.contains(String.valueOf(number.charAt(i)))) {
                    //The number dos'nt contain a numeric char
                    //return false
                    return false;
                }
            }

            //If we reach there all thing is right
            return true;
        }

        //Otherwise
        return false;
    }
}
