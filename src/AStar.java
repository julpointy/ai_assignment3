import javafx.util.Pair;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.PriorityQueue;

public class AStar {
    /**
     * Find the path between the start and finish locations on the given board using the given heuristic
     * @param start The location of the start position
     * @param finish The location of the goal position
     * @param b The board being used
     * @param heuristic The heuristic value being used
     */
    public void astar(Coordinate start, Coordinate finish, Board b, int heuristic, File CSV) {
        // Create a priority queue for the frontier and add the starting location to it
        PriorityQueue<Pair<Coordinate, Integer>> frontier = new PriorityQueue<>((x, y) -> x.getValue() - y.getValue());
        frontier.add(new Pair(start, 0));

        // Create a hash map to keep track of which moves led to which moves and put the
        // starting location in
        HashMap<Coordinate, Coordinate> cameFrom = new HashMap<>();
        cameFrom.put(start, null);

        // Create a hash map to keep track of the cost so far from the starting location
        // for each coordinate
        HashMap<Coordinate, Integer> costSoFar = new HashMap<>();
        costSoFar.put(start, 0);

        int nodesExpanded = 0;

        while(!frontier.isEmpty()) {
            // Remove a coordinate to expand
            Pair<Coordinate, Integer> currLocationCost = frontier.poll();
            nodesExpanded++;
            Coordinate currLocation = currLocationCost.getKey();

            // Check if the current coordinate is the goal position
            if(currLocation.equals(finish)) {
                // Print the score of the path found, the number of actions required to reach the
                // goal, the number of nodes expanded, and the series of actions
                System.out.print("Score of the path found: ");
                System.out.println(100 - costSoFar.get(currLocation));
                String[] movesMade = currLocation.path.split("\n");
                int movesTaken = movesMade.length - 1;
                System.out.println("Number of Actions: " + movesTaken);
                System.out.println("Nodes Expanded: " + nodesExpanded);
                System.out.println("Series of Actions:");
                System.out.println(currLocation.path.trim());
                this.pathToCSV(currLocation.path, CSV);
                break;
            }

            // Get the possible next moves for the current coordinate
            List<Coordinate> moves = b.possibleMoves(currLocation);

            // Loop through the possible next moves
            for (int i = 0; i < moves.size(); i++) {
                Coordinate next = moves.get(i);
                int newCost = costSoFar.get(currLocation) + b.getCost(currLocation, next);
                next.setPath(currLocation.path + next.move.toString() + " " + b.xDistance(finish, next) + " " + b.yDistance(finish,next)+ " " + newCost + " " + b.calculateHeuristic(5,finish,next) + " " + b.minTurns(next,finish) + " " + b.goalEnterCost(next, finish) + "\n");
                if (!costSoFar.containsKey(next) || newCost < costSoFar.get(next)) {
                    costSoFar.put(next, newCost);
                    int priority = newCost + b.calculateHeuristic(heuristic, finish, next);
                    frontier.add(new Pair(next, priority));
                    cameFrom.put(next, currLocation);
                }
            }
        }
    }


    /**
     * Method which takes a string path, and a file and writes the data from that string to the CSV file that it is given
     * @param path of the A* algorithm. Represented as a string that also holds the xDistance to goal, yDistance to goal, real cost, as well as other values
     * @param CSV CSV file that we are writing the data to
     */
    public void pathToCSV(String path, File CSV){
        path.trim();  // trim the string to remove newlines or spaces at the front or end of the string
        String[] splitString = path.split("\n"); // split the string by newlines (each move is a newline)
        String[][] splitString2 = new String[splitString.length][7]; //create a 2d array to split each move into storing its own data within a different array node
        for(int i = 0; i < splitString.length; i++){  // loop through the string array
            String newSplit = splitString[i];
            String[] newString = newSplit.split(" ");  // split the string array into another delimited array of strings
            int s2int = 0;
            for(String s2 : newString){   // store the array in a 2d array
                splitString2[i][s2int] = s2;
                s2int++;
            }
        }
        String[][] finalStringSplit = new String[splitString.length][6];  // take the delimited strings and only keep what we want to write to the CSV file
        int j = splitString.length;
        for (int i = 0; i < splitString.length; i++) {
            finalStringSplit[j - 1][5] = splitString2[i][3];   // actual cost
            j = j - 1;
            finalStringSplit[i][0] = splitString2[i][1];  // xDistance
            finalStringSplit[i][1] = splitString2[i][2];  // yDistance
            finalStringSplit[i][2] = splitString2[i][5];  // number of turns
            finalStringSplit[i][3] = splitString2[i][4];  // heuristic 5 estimate
            finalStringSplit[i][4] = splitString2[i][6];  // goal entrance cost
        }

        try {   // try to open file and append to it
            FileWriter outputFile = new FileWriter(CSV, true);

            BufferedWriter out = new BufferedWriter(outputFile);

            for(String[] s : finalStringSplit){ // write data from array to file
                for(String s1 : s){
                    out.write(s1);
                    out.append(",");
                }
                out.newLine();
            }
            out.close();  // close the file

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
