import java.io.File;
import java.util.ArrayList;
import java.io.Serializable;

public class UserHistory implements Serializable{

    public UHNode root;

    public String search = "";

    public void UserHistory() {
        root = null;
    }

    public void add(String key) {
        if (root == null) {
            root = new UHNode(key.charAt(0));
            UHNode curr = root;
            for (int i = 1; i < key.length(); i++) {
                UHNode val = new UHNode(key.charAt(i));
                curr.setDown(val);
                curr = curr.getDown(); 
            }
            UHNode end = new UHNode('^', 1);
            curr.setDown(end);
        }
        else {
            UHNode curr = root;
            boolean pathFound = false;
            for (int i = 0; i < key.length(); i++) {
                UHNode val = new UHNode(key.charAt(i));
                if (pathFound == false) {
                    while (curr.getLet() != val.getLet() && curr.getRight() != null) {
                        curr = curr.getRight();
                    }
                    if (curr.getLet() != val.getLet()) {
                        curr.setRight(val);
                        curr = curr.getRight();
                        pathFound = true;
                    }
                    else {
                        if (i != (key.length() - 1)) {
                            curr = curr.getDown();
                        }
                    }
                }
                else {
                    curr.setDown(val);
                    curr = curr.getDown();
                }
            }
            if (pathFound) {
                UHNode end = new UHNode('^', 1);
                curr.setDown(end);
            }
            else {
                if (curr.getDown().getLet() != '^') {
                    UHNode temp = curr.getDown();
                    UHNode end = new UHNode('^', 1);
                    curr.setDown(end);
                    curr.getDown().setRight(temp);
                }
                else {
                    curr.getDown().updateFreq();
                }
            }
        }  
    }

    public boolean contains(String key) {
        UHNode curr = root;
        for (int i = 0; i < key.length(); i++) {
            char val = key.charAt(i);
            while (curr != null && curr.getLet() != val) {
                curr = curr.getRight();
            }
            if (curr == null) {
                return false;
            }
            else {
                curr = curr.getDown();
            }
        }
        if (curr.getLet() == '^') {
            return true;
        }
        else {
        return false;
        }
    }

    public boolean containsPrefix(String pre) {
        UHNode curr = root;
        for (int i = 0; i < pre.length(); i++) {
            char val = pre.charAt(i);
            while (curr != null && curr.getLet() != val) {
                curr = curr.getRight();
            }
            if (curr == null) {
                return false;
            }
            else {
                curr = curr.getDown();
            }
        }
        if (curr == null) {
            return false;
        }
        if (curr.getLet() != '^' || curr.getRight() != null) {
            return true;
        }
        else {
            return false;
        }      
    }

    public int searchByChar(char next) {
        search = search + next;
        if (this.contains(search) && this.containsPrefix(search)) {
            return 2;
        }
        else if (this.contains(search)) {
            return 1;
        }
        else if (this.containsPrefix(search)) {
            return 0;
        }
        else {
            return -1;
        }
    }

    public void resetByChar() {
        search = "";
    }

    public ArrayList<String> suggest() {
        UHNode curr = root;
        ArrayList<String> suggestions = new ArrayList();
        String word = "";
        for (int i = 0; i < search.length(); i++) {
            char val = search.charAt(i);
            while (curr != null && curr.getLet() != val) {
                curr = curr.getRight();
            }
            if (curr == null) {
                UHNode options = suggestionHelper(curr, null, word);
                int j = 0;
            while (options != null && j < 6) {
                suggestions.add(options.getWord());
                j = j + 1;
                }
                return suggestions;
            }
            else {
                word = word + curr.getLet();
                curr = curr.getDown();
            }
        }
        UHNode options = suggestionHelper(curr, null, word);
        int i = 0;
        while (options != null && i < 5) {
            suggestions.add(options.getWord());
            options = options.getDown();
            i = i + 1;
        }
        return suggestions;
    }

    public UHNode suggestionHelper(UHNode curr, UHNode words, String word) {
        if (curr == null) {
            return words;
        }
        if (curr.getLet() == '^') {
            UHNode sortedWord = new UHNode(word, curr.getFreq());
            if (words == null) {
                words = sortedWord;
            }
            else if(words.getDown() == null) {
                if (words.getFreq() <= sortedWord.getFreq()) {
                    sortedWord.setDown(words);
                    words = sortedWord;
                }
                else {
                    words.setDown(sortedWord);
                }
            }
            else {
                if (words.getFreq() <= sortedWord.getFreq()) {
                    sortedWord.setDown(words);
                    words = sortedWord;
                    }
                else {
                    UHNode loc = words;
                    boolean added = false;
                    while (loc.getDown() != null && !added) {
                        //System.out.println("test");
                        if (loc.getDown().getFreq() <= sortedWord.getFreq()) {
                            UHNode temp = loc.getDown();
                            loc.setDown(sortedWord);
                            loc.getDown().setDown(temp);
                            added = true;
                        }
                        else {
                            loc = loc.getDown();
                        }
                    }
                    if (!added) {
                        loc.setDown(sortedWord);
                    }
                }
            }
        }
        words = suggestionHelper(curr.getDown(), words, word + curr.getLet());
        words = suggestionHelper(curr.getRight(), words, word);
        return words;
    }

     public ArrayList<String> traverse() {
        ArrayList<String> words = new ArrayList();
        String word = "";
        UHNode curr = root;
        return traverseHelper(curr, words, word);
     }

    public ArrayList<String> traverseHelper(UHNode curr, ArrayList<String> words, String word) {
        if (curr == null) {
            return words;
        }
        if (curr.getLet() == '^') {
            words.add(word + "," + curr.getFreq());
        }
        words = traverseHelper(curr.getDown(), words, word + curr.getLet());
        words = traverseHelper(curr.getRight(), words, word);
        return words;
    }

     public int count() {
        UHNode curr = root;
        return countHelper(curr, 0);
     }

     public int countHelper(UHNode curr, int count) {
        if (curr == null) {
            return count;
        }
        else if (curr.getLet() == '^') {
            count = count + 1;
        }
        count = countHelper(curr.getDown(), count);
        count = countHelper(curr.getRight(), count);
        return count;
     }
}
class UHNode {
    private int freq;

    private char let;

    private String word;

    private UHNode right;

    private UHNode down;

    public UHNode(char let, int freq) {
        this.freq = freq;
        this.let = let;
        this.word = null;
        this.right = null;
        this.down = null;
    }

    public UHNode(char let) {
        this.freq = 0;
        this.let = let;
        this.word = null;
        this.right = null;
        this.down = null;
    }

    public UHNode(String word, int freq) {
        this.freq = freq;
        this.let = ' ';
        this.word = word;
        this.right = null;
        this.down = null;
    }

    public int getFreq() {
        return freq;
    }

    public void updateFreq() {
        freq = freq + 1;
    }
    public char getLet() {
        return let;
    }

    public String getWord() {
        return word;
    }

    public UHNode getRight() {
        return right;
    }

    public UHNode getDown() {
        return down;
    }

    public void setRight(UHNode r) {
        right = r;
    }

    public void setDown(UHNode d) {
        down = d;
    }
}
