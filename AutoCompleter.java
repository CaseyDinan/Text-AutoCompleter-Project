import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;
import java.io.Serializable;

public class AutoCompleter implements Serializable{
    public UserHistory history;

    public DLB dictionary;

    public AutoCompleter(String dictFile, String uhFile) {
        try {
            dictionary = new DLB();
            File dict = new File(dictFile);
            BufferedReader dictReader = new BufferedReader(new FileReader(dict));
            String dictWords;
            while ((dictWords = dictReader.readLine()) != null) {
                dictionary.add(dictWords);
            }
        }
        catch (Exception e) {
            System.out.println("Error: Invalid file input.");
        }
        try {
            history = new UserHistory();
            File user = new File(uhFile);
            BufferedReader userReader = new BufferedReader(new FileReader(user));
            String userWords;
            while ((userWords = userReader.readLine()) != null) {
                String[] splitWord = userWords.split(",", 2);
                int frequency = Integer.parseInt(splitWord[1]);
                for (int i = 0; i < frequency; i++)
                history.add(splitWord[0]);
            }
        }
        catch (Exception e) {
            history = new UserHistory();
        }
    }

    public AutoCompleter(String dictFile) {
        try {
            dictionary = new DLB();
            history = new UserHistory();
            File dict = new File(dictFile);
            BufferedReader dictReader = new BufferedReader(new FileReader(dict));
            String dictWords;
            while (dictReader.ready()) {
                dictWords = dictReader.readLine();
                dictionary.add(dictWords);
            }
        }
        catch (Exception e) {
            System.out.println("Error: Invalid file input.");
            dictionary = new DLB();
            history = new UserHistory();
        }
    }

    public ArrayList<String> nextChar(char next) {
        dictionary.searchByChar(next);
        history.searchByChar(next);
        ArrayList<String> dictWords = null;
        ArrayList<String> historyWords = history.suggest();
        if (historyWords.size() < 5) {
            dictWords = dictionary.suggest();
        }
        int i = 0;
        while (historyWords.size() < 5 && i < dictWords.size()) {
            if (!historyWords.contains(dictWords.get(i))) {
                historyWords.add(dictWords.get(i));
            }
            i++;
        }
        return historyWords;
    }

    public void finishWord(String cur) {
        history.add(cur);
        dictionary.resetByChar();
        history.resetByChar();
    }

    public void saveUserHistory(String fileName) {
        File uHistory = new File(fileName);
        try {
            if (uHistory.exists()) {
                FileWriter historyWriter = new FileWriter(fileName);
                ArrayList<String> userWords = history.traverse();
                for (int i = 0; i < userWords.size(); i++) {
                    historyWriter.write(userWords.get(i) + "\n");
                }
                historyWriter.close();
            }
            else {
                uHistory.createNewFile();
                FileWriter historyWriter = new FileWriter(fileName);
                ArrayList<String> userWords = history.traverse();
                for (int i = 0; i < userWords.size(); i++) {
                    historyWriter.write(userWords.get(i) + "\n");
                }
                historyWriter.close();
            }
        }
        catch (Exception e) {
            System.out.print("Error writing to file");
        }
        }
}
