import java.util.ArrayList;

public class ConstraintSolver
{
    private long cost;
    private int[] nodeColors;
    private int[] colorVals;
    private ArrayList<Integer>[] possibleValues;

    public ConstraintSolver(){ };

    public long getCost() { return cost; }

    public String getColoring()
    {
        String retVal = "";
        String line1 = "";
        String line2 = "";

        for (int i = 0; i <= nodeColors.length - 1; i++)
        {
            line1 += " " + String.format("%1$" + 3 + "s",i) + " ";
            line2 += " " + String.format("%1$" + 3 + "s", colorMap(nodeColors[i])) + " ";
        }

        retVal = line1 + System.lineSeparator() + line2;
        return retVal;
    }

    private String colorMap(int color)
    {
        if (color == 0) return "NA";
        if (color == 1) return "R";
        if (color == 2) return "B";
        if (color == 3) return "G";
        if (color == 4) return "Y";
        return "XX";
    }

    private void prepare(int size, int numColors)
    {
        cost = 0;
        nodeColors = new int[size];
        colorVals = new int[numColors + 1];

        //initialize possible color values for nodes; "0" means "uncolored"
        for (int i = 0; i <= numColors; i++)
        {
            colorVals[i] = i;
        }
    }

    //Check that the current coloring satisfies the constraint for the given graph and that all nodes have colors
    private boolean hasSolution(Graph myGraph)
    {
        for (int i = 0; i <= nodeColors.length - 1; i++)
        {
            if (nodeColors[i] == 0) return false; //if a node was left uncolored then it wasn't solved
            ArrayList<Integer> neighborList = myGraph.getEdges(i);

            //Test if the node's neighbors share the same color (and that they've been assigned colors in the first place)
            for (Integer j : neighborList) if (nodeColors[i] == nodeColors[j]) return false;
        }

        return true;
    }

    //***************************************************
    //*********Code for Simple Backtracking**************
    public void simpleBacktrack(Graph myGraph, int numColors)
    {
        prepare(myGraph.getNumNodes(), numColors);

        simpleBTColor(getNextNode(myGraph), myGraph);

        if (!hasSolution(myGraph)) cost *= -1;
    }

    private void simpleBTColor(int curNode, Graph myGraph)
    {
        cost++;
        boolean foundColor = false;

        //loop through color assignments to the node, attempting to find a valid one
        int i = 1;
        while (!foundColor && i <= colorVals.length - 1)
        {
            cost++;

            //check that this coloring is valid
            if (!causesConflicts(myGraph, curNode, colorVals[i]))
            {
                nodeColors[curNode] = colorVals[i];
                int nextNode = getNextNode(myGraph); //uses a heuristic function to pick the next node to color in order to speed up processing

                if (nextNode < nodeColors.length) //check that the next node actually exists
                {
                    simpleBTColor(nextNode, myGraph); //recursively attempt to color the next node

                    //test if we successfully assigned the next node a color
                    //if so, then the coloring we just tried for curNode is good and the color was found!
                    if (nodeColors[nextNode] != colorVals[0]) foundColor = true;
                }
                else foundColor = true;
            }

            i++;
        }

        //if there wasn't a valid color for this node, set it back to uncolored (backtracking step)
        if(!foundColor) nodeColors[curNode] = colorVals[0];
    }
    //*********End Code for Simple Backtracking**********
    //***************************************************

    //***************************************************
    //*********Shared Backtracking Procedures************
    private boolean causesConflicts(Graph myGraph, int theNode, int newColor) //SHARED BACKTRACKING PROC
    {
        ArrayList<Integer> neighborList = myGraph.getEdges(theNode);

        for (Integer j : neighborList)
        {
            cost++;
            if (newColor == nodeColors[j]) return true;
        }

        return false;
    }

    //Procedure to facilitate getting the next node to color, gets the next uncolored node with the max number of neighbors
    //(heuristic is to color the node involved in the maximum number of constraints)
    private int getNextNode(Graph myGraph) //SHARED BACKTRACKING PROC
    {
        int maxNeighbors = -1;
        int nextNode = -1;

        for (int i = 0; i <= nodeColors.length - 1; i++) //could speed this up by using a priority queue instead of a dumb linear search
        {
            cost++;
            //test if node i is uncolored and has the most neighbors we've seen so far
            if (nodeColors[i] == colorVals[0] && myGraph.getEdges(i).size() > maxNeighbors)
            {
                maxNeighbors = myGraph.getEdges(i).size();
                nextNode = i;
            }
        }

        //if nextNode was never assigned a new value >= 0 then all nodes have a color
        //return an out-of-bounds value to indicate all nodes are colored and halt recursion
        if (nextNode < 0) nextNode = nodeColors.length;
        return nextNode;
    }
    //*********End Shared Backtracking Procedures********
    //***************************************************
}
