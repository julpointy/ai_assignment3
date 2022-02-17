import java.io.*;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

public class BoardGenerator {

    public BoardGenerator() throws IOException {
        int lengthOfArray = 8;
        int widthOfArray = 8;
        String[][] array = new String[0][];
        try {
            array = generate2DArray(lengthOfArray, widthOfArray);
        } catch (IOException e) {
            e.printStackTrace();
        }

        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < array.length; i++)//for each row
        {
            for (int j = 0; j < array.length; j++)//for each column
            {
                builder.append(array[i][j] + "\t");//append to the output string
                if (j < array.length - 1)//if this is not the last row element
                    builder.append("");//then add comma (if you don't like commas you can use spaces)
            }
            builder.append("\n");//append new line at the end of the row


            BufferedWriter writer = null;
            try {
                writer = new BufferedWriter(new FileWriter("C:\\Users\\Josh\\IdeaProjects\\CS4341-Project-3\\src\\Boards\\" + lengthOfArray + "x" + widthOfArray + "boards/board" + ".txt"));
                writer.write(builder.toString());//save the string representation of the board
                writer.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    public String[][] generate2DArray(int x, int y) throws IOException {
        String[][] array = new String[x][y];
        for (int i = 0; i < array.length; i++) {
            for (int j = 0; j < array.length; j++) {
                int randomNum = ThreadLocalRandom.current().nextInt(1, 10);
                array[i][j] = (Integer.toString(randomNum));
            }
        }

        array[x-2][1] = "S";
        array[1][y-2] = "G";

        return array;

    }
}
