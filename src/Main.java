import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Scanner;

public class Main {
    /**
     * This is the main for the project, run this to have the A* algorithm run
     * @param args The first command line arg should be a heuristic number (1-6)
     * and the second should be a filename (without spaces)
     */
    public static void main(String[] args) {

        //First we created counters for heuristic number, board height and width, and coordinates for the start and goal
        int currHeuristic = 0;
        int boardheight = 0;
        int boardwidth = 0;
        Coordinate start = new Coordinate();
        Coordinate goal = new Coordinate();

        //checks if the input is correct and specifies what needs to be changed
        if (args.length < 2 || args.length > 3) {
            System.out.println("An error occurred.");
            System.out.println("Invalid number of input. ");
            System.out.println("Please input a heuristic number and the filename. ");
        }

        // Read the first command line arg as the heuristic number
        String initialInt =  args[0];
        try {
            currHeuristic = Integer.parseInt(initialInt);
        } catch (NumberFormatException e) {
            System.out.println("Please input the filename as the first input.");
            return;
        }

        String filename = args[1]; // pull the filename from the second argument

        File CSV = new File("C:\\Users\\julpo\\Documents\\GitHub\\ai_assignment3\\src\\Output\\Output.csv"); // generate a CSV file to store collected data for machine learning

        for(int l = 0; l < 1000; l++){ //run the algorithm with random board for L times
            Tester test = new Tester(boardwidth, boardheight, filename, start, goal, currHeuristic, CSV); //helper class that holds the board generation, algorithm call, and whatnot to allow for garbage collector to remove it after a few loops and not exist in main forever
            //System.out.println(l); // print out which board out of l it completed
        }
    }
}

