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
                next.setPath(currLocation.path + next.move.toString() + " " + b.xDistance(finish, next) + " " + b.yDistance(finish,next)+ " " + newCost + "\n");
                if (!costSoFar.containsKey(next) || newCost < costSoFar.get(next)) {
                    costSoFar.put(next, newCost);
                    int priority = newCost + b.calculateHeuristic(heuristic, finish, next);
                    frontier.add(new Pair(next, priority));
                    cameFrom.put(next, currLocation);
                }
            }
        }
    }

    public void pathToCSV(String path, File CSV){
        path.trim();
        String[] splitString = path.split("\n");
        String[][] splitString2 = new String[splitString.length][4];
        for(int i = 0; i < splitString.length; i++){
            String newSplit = splitString[i];
            String[] newString = newSplit.split(" ");
            int s2int = 0;
            for(String s2 : newString){
                splitString2[i][s2int] = s2;
                s2int++;
            }
        }
        String[][] finalStringSplit = new String[splitString.length][3];
        int j = splitString.length;
        for (int i = 0; i < splitString.length; i++) {
            finalStringSplit[j - 1][2] = splitString2[i][3];
            j = j - 1;
            finalStringSplit[i][1] = splitString2[i][2];
            finalStringSplit[i][0] = splitString2[i][1];
        }

        try {
            FileWriter outputFile = new FileWriter(CSV, true);

            BufferedWriter out = new BufferedWriter(outputFile);

            for(String[] s : finalStringSplit){
                for(String s1 : s){
                    out.write(s1);
                    out.append(",");
                }
                out.newLine();
            }
            out.close();

        } catch (IOException e) {
            e.printStackTrace();
        }


//        for(String[] s : finalStringSplit){
//            for(String s1: s){
//                System.out.println(s1);
//            }
//        }
    }
}
