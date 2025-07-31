package com.ftn.ues.socialnetwork.util;

import java.util.HashMap;
import java.util.Map;

public class SerbianLatinConverter {

    private static final Map<Character, String> cirToLatMap = new HashMap<>();

    static {
        cirToLatMap.put('а', "a"); cirToLatMap.put('б', "b"); cirToLatMap.put('в', "v");
        cirToLatMap.put('г', "g"); cirToLatMap.put('д', "d"); cirToLatMap.put('ђ', "dj");
        cirToLatMap.put('е', "e"); cirToLatMap.put('ж', "z"); cirToLatMap.put('з', "z");
        cirToLatMap.put('и', "i"); cirToLatMap.put('ј', "j"); cirToLatMap.put('к', "k");
        cirToLatMap.put('л', "l"); cirToLatMap.put('љ', "lj"); cirToLatMap.put('м', "m");
        cirToLatMap.put('н', "n"); cirToLatMap.put('њ', "nj"); cirToLatMap.put('о', "o");
        cirToLatMap.put('п', "p"); cirToLatMap.put('р', "r"); cirToLatMap.put('с', "s");
        cirToLatMap.put('т', "t"); cirToLatMap.put('ћ', "c"); cirToLatMap.put('у', "u");
        cirToLatMap.put('ф', "f"); cirToLatMap.put('х', "h"); cirToLatMap.put('ц', "c");
        cirToLatMap.put('ч', "c"); cirToLatMap.put('џ', "dz"); cirToLatMap.put('ш', "s");

        cirToLatMap.put('А', "A"); cirToLatMap.put('Б', "B"); cirToLatMap.put('В', "V");
        cirToLatMap.put('Г', "G"); cirToLatMap.put('Д', "D"); cirToLatMap.put('Ђ', "Dj");
        cirToLatMap.put('Е', "E"); cirToLatMap.put('Ж', "Z"); cirToLatMap.put('З', "Z");
        cirToLatMap.put('И', "I"); cirToLatMap.put('Ј', "J"); cirToLatMap.put('К', "K");
        cirToLatMap.put('Л', "L"); cirToLatMap.put('Љ', "Lj"); cirToLatMap.put('М', "M");
        cirToLatMap.put('Н', "N"); cirToLatMap.put('Њ', "Nj"); cirToLatMap.put('О', "O");
        cirToLatMap.put('П', "P"); cirToLatMap.put('Р', "R"); cirToLatMap.put('С', "S");
        cirToLatMap.put('Т', "T"); cirToLatMap.put('Ћ', "C"); cirToLatMap.put('У', "U");
        cirToLatMap.put('Ф', "F"); cirToLatMap.put('Х', "H"); cirToLatMap.put('Ц', "C");
        cirToLatMap.put('Ч', "C"); cirToLatMap.put('Џ', "Dz"); cirToLatMap.put('Ш', "S");
    }

    public static String toLatinLowercase(String input) {
        StringBuilder result = new StringBuilder();

        for (char c : input.toCharArray()) {
            result.append(cirToLatMap.getOrDefault(c, String.valueOf(c)));
        }

        return result.toString().toLowerCase();
    }
}
