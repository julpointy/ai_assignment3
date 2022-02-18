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

        String filename = args[1];

        File CSV = new File("C:\\Users\\Josh\\Documents\\ai_assignment3\\src\\Output\\Output.csv");

        for(int l = 0; l < 500; l++){
            Tester test = new Tester(boardwidth, boardheight, filename, start, goal, currHeuristic, CSV);
            System.out.println(l);
        }
    }
}

