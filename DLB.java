import java.io.File;
import java.util.ArrayList;
import java.io.Serializable;

public class DLB implements Serializable{

    public DLBNode root;

    public String search = "";

    public void DLB() {
        root = null;
    }

    public void add(String key) {
        if (root == null) {
            root = new DLBNode(key.charAt(0));
            DLBNode curr = root;
            for (int i = 1; i < key.length(); i++) {
                DLBNode val = new DLBNode(key.charAt(i));
                curr.setDown(val);
                curr = curr.getDown(); 
            }
            DLBNode end = new DLBNode('^');
            curr.setDown(end);
        }
        else {
            DLBNode curr = root;
            boolean pathFound = false;
            for (int i = 0; i < key.length(); i++) {
                DLBNode val = new DLBNode(key.charAt(i));
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
                DLBNode end = new DLBNode('^');
                curr.setDown(end);
            }
            else {
                if (curr.getDown().getLet() != '^') {
                    DLBNode temp = curr.getDown();
                    DLBNode end = new DLBNode('^');
                    curr.setDown(end);
                    curr.getDown().setRight(temp);
                }
            } 
            }
    }  

    public boolean contains(String key) {
        DLBNode curr = root;
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
        DLBNode curr = root;
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
        DLBNode curr = root;
        ArrayList<String> suggestions = new ArrayList();
        String word = "";
        for (int i = 0; i < search.length(); i++) {
            char val = search.charAt(i);
            while (curr != null && curr.getLet() != val) {
                curr = curr.getRight();
            }
            if (curr == null) {
                return suggestionHelper(curr, suggestions, word);
            }
            else {
                word = word + curr.getLet();
                curr = curr.getDown();
            }
        }
        return suggestionHelper(curr, suggestions, word);
    }

    public ArrayList<String> suggestionHelper(DLBNode curr, ArrayList<String> words, String word) {
        if (curr == null || words.size() > 5) {
            return words;
        }
        if (curr.getLet() == '^') {
            words.add(word);
        }
        words = traverseHelper(curr.getDown(), words, word + curr.getLet());
        words = traverseHelper(curr.getRight(), words, word);
        return words;
    }

     public ArrayList<String> traverse() {
        ArrayList<String> words = new ArrayList();
        String word = "";
        DLBNode curr = root;
        return traverseHelper(curr, words, word);
     }

    public ArrayList<String> traverseHelper(DLBNode curr, ArrayList<String> words, String word) {
        if (curr == null) {
            return words;
        }
        if (curr.getLet() == '^') {
            words.add(word);
        }
        words = traverseHelper(curr.getDown(), words, word + curr.getLet());
        words = traverseHelper(curr.getRight(), words, word);
        return words;
    }

     public int count() {
        DLBNode curr = root;
        return countHelper(curr, 0);
     }

     public int countHelper(DLBNode curr, int count) {
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
class DLBNode implements Serializable {
  
  private char let;

  private DLBNode right;

  private DLBNode down;

  public DLBNode(char let) {
      this.let = let;
      this.right = null;
      this.down = null;
  }

  public char getLet() {
      return let;
  }

  public DLBNode getRight() {
      return right;
  }

  public DLBNode getDown() {
      return down;
  }

  public void setRight(DLBNode r) {
      right = r;
  }

  public void setDown(DLBNode d) {
      down = d;
  }
}
