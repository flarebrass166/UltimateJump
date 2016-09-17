package TeamProject;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;

public class highScore {
    PrintWriter writer;
    List<User> users = new ArrayList();

    public void readInScores(){
        try (Scanner readIn = new Scanner(highScore.class.getResourceAsStream("\\HighScore.txt"))){
            while(readIn.hasNextLine()){
                String line = readIn.nextLine();
                User user = getUser(line);
                users.add(user);
            }
        } catch (Exception e1) {
            e1.printStackTrace();
        }
    }
    public void saveScore(String userName, int score){
        users.add(new User(userName, score));
        Collections.sort(users);
        if(users.size() > 5){
            users.remove(0);
        }
        Collections.reverse(users);

        try{
            writer = new PrintWriter("HighScore.txt");
            for(User e: users){
                writer.println(e.toString());
            }
            writer.close();
        }catch(IOException ex){
            ex.printStackTrace();
        }
    }

    public User getUser(String nextLine) {
        try{
            String[] lines = nextLine.split("  ");
            return new User(lines[0], Integer.parseInt(lines[1]));
        }		catch(NumberFormatException | ArrayIndexOutOfBoundsException e){
            System.err.printf("%s... could not be read in as a User%n", nextLine);
        }
        return null;
    }

    public String allScores(int index){
        String allScores = "";
        try{
            allScores = users.get(index).toString();
        }catch(IndexOutOfBoundsException e){

        }
        return allScores;
    }
}