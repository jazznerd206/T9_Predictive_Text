import java.io.File;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;

public class Trie {

    Node root;

    Trie() {
        this.root = new Node();
    }

    public class Node {
        LinkedList<String> words;
        HashMap<Integer, Node> m;

        public Node() {
            this.words = new LinkedList();
            this.m = new HashMap();
        }
    }

    public void insert(int[] t9, String word) {
        Node curr = root;
        for (int i = 0; i < t9.length; i++) {
            Integer d = t9[i];
            if (!curr.m.containsKey(d)) {
                curr.m.put(d, new Node());
            }
            curr = curr.m.get(d);
        }
        curr.words.add(word);
        return;
    }

    public List<String> lookup(int[] t9) {
        Node curr = root;
        for (int i = 0; i < t9.length; i++) {
            Integer d = t9[i];
            if (!curr.m.containsKey(d)) {
                return Collections.emptyList();
            }
            curr = curr.m.get(d);
        }
        return curr.words;
    }

    public List<String> deepLookup(int[] t9) {
        Node curr = root;
        for (int i = 0; i < t9.length; i++) {
            Integer d = t9[i];
            if (!curr.m.containsKey(d)) {
                return Collections.emptyList();
            }
            curr = curr.m.get(d);
        }
        return deepLookupHelper(curr);
    }

    public List<String> deepLookupHelper(Node curr) {
        if (curr == null) {
            return Collections.emptyList();
        }
        if (curr.m.size() == 0) {
            return curr.words;
        }
        List<String> mergedResult = new LinkedList<>();
        for (Node next : curr.m.values()) {
            List<String> words = deepLookupHelper(next);
            mergedResult.addAll(words);
        }
        return mergedResult;
    }

    private static int charToT9(char ch) {
        if ('a' <= ch && ch <= 'c')
            return 2;
        if ('d' <= ch && ch <= 'f')
            return 3;
        if ('g' <= ch && ch <= 'i')
            return 4;
        if ('j' <= ch && ch <= 'l')
            return 5;
        if ('m' <= ch && ch <= 'o')
            return 6;
        if ('p' <= ch && ch <= 's')
            return 7;
        if ('t' <= ch && ch <= 'v')
            return 8;
        if ('w' <= ch && ch <= 'z')
            return 9;
        return 0;
    }

    public static int[] toT9(String word) {
        int[] t9 = new int[word.length()];
        for (int i = 0; i < t9.length; i++) {
            t9[i] = charToT9(word.charAt(i));
        }
        return t9;
    }

    private static boolean containsLettersOnly(String word) {
        for (char ch : word.toCharArray()) {
            if (!Character.isLetter(ch)) {
                return false;
            }
        }
        return true;
    }

    private static boolean isValidT9(String token) {
        for (char ch : token.toCharArray()) {
            if (!Character.isDigit(ch)) {
                return false;
            }
        }
        return true;
    }

    public static int[] toIntArray(String input) {
        int[] t9 = new int[input.length()];
        for (int i = 0; i < t9.length; i++) {
            t9[i] = input.charAt(i) - '0';
        }
        return t9;
    }

    public static void main(String[] args) {
        try {
            // http://github.com/dwyl/english-words/blob/master/words.txt
            File dictionary = new File("/Users/miller/Downloads/words.txt");
            Scanner scan = new Scanner(dictionary);

            // Build our t9ToWord index
            Trie t9ToWords = new Trie();
            while (scan.hasNextLine()) {
                String line = scan.nextLine();
                String word = line.trim().toLowerCase();
                if (containsLettersOnly(word)) {
                    t9ToWords.insert(toT9(word), word);
                }
            }
            scan.close();

            Scanner input = new Scanner(System.in);
            String line = "";
            while (true) {
                System.out.print("Enter T9: ");
                line = input.nextLine();
                line = line.trim().toLowerCase();
                if (line.startsWith("q")) {
                    break;
                }
                String token = line.split(" ")[0];
                if (isValidT9(token)) {
                    List<String> words = t9ToWords.deepLookup(toIntArray(token));
                    System.out.println("Possible words are: " + words.toString());
                }
            }
            input.close();
        } catch (Exception ex) {
            System.out.println(ex.toString());
        }

        System.out.println("Goodbye!");
    }
}
