import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.Normalizer;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Random;
import java.util.Scanner;
import java.util.Set;

public class Main {
    private static Set<String> set = makeSet();
    public static Scanner sc = new Scanner(System.in);

    private static boolean filterSet(String s) { //if string is only 3 characters long returns true
        return s.length() <= 3;
    }

    private static String transScript(String s) {
        s = s.toLowerCase(); //changes string to lowercase
        return Normalizer.normalize(s, Normalizer.Form.NFD) //normalizes string
                .replaceAll("\\p{InCOMBINING_DIACRITICAL_MARKS}+", ""); //replaces all diacritics with ""
    }

    private static Set<String> makeSet() {
        File file = new File("syn2010_lemma_cba.txt");
        Set<String> set = new LinkedHashSet<String>();
        try {
            Scanner sc = new Scanner(file, "windows-1250"); //creates new scanner object
            while (sc.hasNextLine()) { //loops if scanner finds text on line
                String string = sc.findInLine("[\\p{IsLatin}]+"); //saves next string
                int num = Integer.parseInt(sc.findInLine("[\\d]+"));
                if (filterSet(string)) {
                    sc.nextLine();
                    continue;
                }
                String out = transScript(string); //saves transcription to string s
                set.add(out);
                sc.nextLine();
            }
            sc.close(); //scanner closes file
        } catch (FileNotFoundException sce) {
            System.out.println("File not found");
            sce.printStackTrace();
        }
        return set;
    }

    private static String chooseRandom(Set set) {
        String s = null;
        Random rand = new Random(); //creates new RNG
        for (int c = 0; c < 20; c++) {
            int rnum = rand.nextInt(set.size()); //rolls random number
            Iterator<String> it = set.iterator(); //creates new iterator
            int i = 1; //counter set on 1 (1st element in set)
            while (i != rnum && it.hasNext()) {
                if (i == set.size()) {
                    break;
                }
                it.next();
                i++;
            }
            s = it.next();
        }
        return s;
    }

    public static boolean isTranslation(String s, List<String> list) {
        for (String el : list)
            if (s.equals(el)) {
                return true;
            }
        return false;
    }

    public static void main(String[] args) throws IOException {
        int points = 0;
        for (int i = 1; i < 21; i++) {
            String word = chooseRandom(set);
//        for (int i = 1; i < 3; i++) { //for testing
//            String word = "rozdat"; //for testing purposes
            TranslationList trList = new TranslationList(word);
            if (trList.getList() == null) {
                --i;
                continue;
            }
            trList.adjustTranslationList();


            System.out.println("Word " + i + "/20: " + word);
            System.out.print("Your translation: ");
            String answer = sc.next();
//
            if (isTranslation(answer, trList.getList())) {
                System.out.println("Correct!");
                ++points;
            } else {
                System.out.println("Incorrect!");
                System.out.print("Possible translations: ");
                int c = 0;
                for (String ele : trList.getList()) {
                    if (++c == 1) {
                        System.out.print(ele);
                    } else if (ele == null) {
                        continue;
                    } else {
                        System.out.print(", " + ele);
                    }
                }
                System.out.println(" ");
            }


            System.out.println("Points: " + points + "/20");
            System.out.println(" ");
        }

    }
}

