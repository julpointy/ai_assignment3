import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class Main {
    /**
     * This is the main for the project, run this to have the A* algorithm run
     * @param args The first command line arg should be a heuristic number (1-6)
     * and the second should be a filename (without spaces)
     */
    public static void main(String[] args) {
        // Create counters for heuristic number, board height and width, and coordinates for the start and goal
        int currHeuristic = 0;
        int boardheight = 0;
        int boardwidth = 0;
        Coordinate start = new Coordinate();
        Coordinate goal = new Coordinate();

        // Check if the input is correct and specifies what needs to be changed
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
            System.out.println("Please input the heuristic number as the first input.");
            return;
        }

        // Read the second input as the filename and open the file
        String Filename = args[1];
        try {
            File myObj = new File("/Users/mollysunray/OneDrive - Worcester Polytechnic Institute (wpi.edu)/Courses/Junior/CS 4341/Assignments/Assignment1/src/" + Filename);
            Scanner myReader = new Scanner(myObj);
            int i = 0;
            // While the file is open, parse through it to calculate the board size
            while (myReader.hasNextLine()) {
                String data = myReader.nextLine();
                String[] spaces = data.split("\t");
                boardwidth = spaces.length;
                boardheight++;
                i++;
            }
            myReader.close();
        } catch (FileNotFoundException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }

        // Reopen the file to collect the values at each board space to place in an array
        Board board = new Board(boardheight, boardwidth);
        try {
            File myObj = new File("/Users/mollysunray/OneDrive - Worcester Polytechnic Institute (wpi.edu)/Courses/Junior/CS 4341/Assignments/Assignment1/src/" + Filename);
            Scanner myReader = new Scanner(myObj);
            int i = 0;
            while (myReader.hasNextLine()) {
                String data = myReader.nextLine();
                String[] spaces = data.split("\t");
                for (int j = 0; j < spaces.length; j++) {
                    if (spaces[j].equals("S")) {
                        spaces[j] = "1";
                        start.setRowloc(i);
                        start.setColumnloc(j);
                        start.setValue(1);
                    }
                    if (spaces[j].equals("G")) {
                        spaces[j] = "1";
                        goal.setRowloc(i);
                        goal.setColumnloc(j);
                        start.setValue(1);
                    }
                    try {
                        board.board[i][j] = Integer.parseInt(spaces[j]);
                    } catch (NumberFormatException e) {
                        e.printStackTrace();
                    }
                }
                i++;
            }
            myReader.close();
        } catch (FileNotFoundException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }

        // Once the board has been created, start a counter to check how long the A* algorithm takes
        // Pass into the A* algorithm the board, the goal and start coordinates, and the heuristic
        // that it will make decisions off of
        long startTime = System.nanoTime();
        AStar aStar = new AStar();
        aStar.astar(start, goal, board, currHeuristic);
        long endTime = System.nanoTime();
        long elapsedTime = (endTime - startTime) / 1000000000;
    }
}
