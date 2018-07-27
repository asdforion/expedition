/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package expedition;

import static expedition.Expedition.textSpeed;
import static expedition.Frame.tab;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

/**
 *
 * @author j76785
 */
public class Other {

    public static <T extends Object> void incMapping(Map<T, Integer> in, T which, int increment) {
        in.put(which, (in.get(which) + increment));
    }

    public static <T extends Object> T pickRandom(Map<T, Integer> in) {
        Random rand = new Random();
        int lowestEmptyIndex = 0;
        int totalChoices = sumVals(in);
        ArrayList<T> choices = new ArrayList<>();

        for (T obj : in.keySet()) {
            for (int i = lowestEmptyIndex; i < (lowestEmptyIndex + in.get(obj)); i++) {
                choices.add(obj);
            }
        }
        return choices.get(rand.nextInt(totalChoices));
    }

    public static int sumVals(Map<?, Integer> in) {
        Integer sum = 0;
        for (Integer x : in.values()) {
            sum += x;
        }
        return (int) sum;
    }

    //returns a number between 1 and 2, heavily weighted to 1. (odds are 4 to 1 in favor of 1)
    public static int d2(int oneOdds) {
        Map<Integer, Integer> d2 = new HashMap<>();
        d2.put(1, oneOdds);
        d2.put(2, 1);
        return pickRandom(d2);
    }

    public static <T extends Object> boolean contains(T[] arr, T val) {
        for (T x : arr) {
            if (x == val || x.equals(val)) {
                return true;
            }
        }
        return false;
    }

    public static String fixCase(String str) {
        if (str.startsWith("MC")) {
            return "Mc" + str.substring(2, 3).toUpperCase() + str.substring(3).toLowerCase();
        } else {
            return str.substring(0, 1).toUpperCase() + str.substring(1).toLowerCase();
        }
    }

    public static String getRemainderSpaces(String str, int length) {
        String out = "";
        for (int i = 0; i < length - str.length(); i++) {
            out = out + " ";
        }
        return out;
    }

    public static String grs(String str, int length) {
        return getRemainderSpaces(str, length);
    }

    public static void slowPrintln(String input) throws InterruptedException {
        Thread.sleep((textSpeed + 1) * 50);
        println(input);
        Thread.sleep((textSpeed + 1) * 50);

    }

    public static void printTab() {
        print(tab);
    }

    public static String getAn(String in) {
        String first = in.substring(0, 1);
        if (first.equals("a") || first.equals("e") || first.equals("i") || first.equals("o") || first.equals("u")) {
            return "an";
        } else {
            return "a";
        }
    }

    public static String repeatStr(String in, int numTimes) {
        String build = "";
        for (int i = 0; i < numTimes; i++) {
            build = build + in;
        }
        return build;
    }

    /*public static <T extends Object> String toList(Collection<T> list) {
        if (list.size() <= 0) {
            return "";
        }
        ArrayList<T> build = new ArrayList<>();
        for (T a : list) {
            build.add(a);
        }
        if (build.size() == 1) {
            return build.get(0).toString();
        } else if (build.size() == 2) {
            return build.get(0).toString() + " and " + build.get(1).toString();
        } else {
            int i = 0;
            while (i < build.size()) {

            }
        }
        return "";
    }
*/

    public static void br() {
        System.out.print("\n");
    }

    public static void br(int rev) {
        for (int i = 0; i < rev; i++) {
            System.out.print("\n");
        }
    }
    
    public static void error(String in){
        br();
        tprintln(in);
    }

    public static void print(String in) {
        System.out.print(in);
    }

    public static void println(String in) {
        System.out.println(in);
    }

    public static void tprint(String in) {
        printTab();
        print(in);
    }

    public static void tprintln(String in) {
        printTab();
        println(in);
    }

}
