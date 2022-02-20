import java.io.*;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

public class BoardGenerator {

    /**
     * method that generates a board of size lengthOfArray and widthOfArray
     * @throws IOException IO exception that is possible due to writing to a file
     */
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
                writer = new BufferedWriter(new FileWriter("C:\\Users\\Josh\\Documents\\ai_assignment3\\src\\boards\\board.txt"));
                writer.write(builder.toString());//save the string representation of the board
                writer.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    /**
     * helper function that generates an array of fixed size with random ints from 1-9
     * @param x length of array
     * @param y width of array
     * @return return the 2D array
     * @throws IOException exception from writing to a file
     */
    public String[][] generate2DArray(int x, int y) throws IOException {
        String[][] array = new String[x][y]; // initialize array of size x and y
        for (int i = 0; i < array.length; i++) { // loop through each location in array
            for (int j = 0; j < array.length; j++) {
                int randomNum = ThreadLocalRandom.current().nextInt(1, 10);//generate a random int for each location
                array[i][j] = (Integer.toString(randomNum));
            }
        }

        int randomXNum = ThreadLocalRandom.current().nextInt(1, x); // generate a random location for start and finish
        int randomYNum = ThreadLocalRandom.current().nextInt(1, y);

        int randomXNum2 = ThreadLocalRandom.current().nextInt(1, x);
        int randomYNum2 = ThreadLocalRandom.current().nextInt(1, x);

        array[randomXNum][randomYNum] = "G";

        while(randomXNum == randomXNum2 && randomYNum == randomYNum2){
            randomXNum2 = ThreadLocalRandom.current().nextInt(1, x);
            randomYNum2 = ThreadLocalRandom.current().nextInt(1, x);
        }

        array[randomXNum2][randomYNum2] = "S";

        return array;
    }
}
