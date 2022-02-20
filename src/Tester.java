import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Scanner;

public class Tester {
    public Tester(int boardwidth, int boardheight, String Filename1, Coordinate start, Coordinate goal, int currHeuristic, File CSV) {

        try {
            BoardGenerator test = new BoardGenerator();
        } catch (IOException e) {
            e.printStackTrace();
        }

        //reads the second input as the filename and opens the file
        String Filename = Filename1;
        try {

            File myObj = new File("C:\\Users\\Josh\\IdeaProjects\\ai_assignment3\\src\\Boards\\" + Filename);
            Scanner myReader = new Scanner(myObj);
            int i = 0;
            //while the file is open, we parse through it to calculate the board size
            while (myReader.hasNextLine()) {
                String data = myReader.nextLine();
                String[] spaces = data.split("\t");
                boardwidth = spaces.length;
                for (String s : spaces) {
                    //System.out.println(s);
                }
                boardheight++;
                i++;
            }
            myReader.close();
        } catch (FileNotFoundException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }

        //We reopen the file to collect the values at each board space to place in a 2x2 array
        Board board = new Board(boardheight, boardwidth);
        try {
            File myObj = new File("C:\\Users\\Josh\\IdeaProjects\\ai_assignment3\\src\\Boards\\" + Filename);
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

        //once the board has been created, we start a counter to check how long the A* algorithm takes
        //We pass into the A* algorithm the board, the goal and start coordinate, and the heuristic that it will make decisions off of
        long startTime = System.nanoTime();
        AStar aStar = new AStar();
        aStar.astar(start, goal, board, currHeuristic, CSV);
        long endTime = System.nanoTime();
        long elapsedTime = (endTime - startTime) / 1000000000;
        System.out.println(elapsedTime);
    }
}
