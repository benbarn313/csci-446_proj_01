import java.util.ArrayList;
import java.util.Queue;

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
    //*********Code for BackTracking w/ Fwd Check********
    //NOTE: this algorithm uses an extra global var: ArrayList<Integer>[] possibleValues
    //This will represent the possible legal color assignments for any given node, i.e. possibleValues[i] is an ArrayList of all the color vals that node i can be legally assigned

    public void backtrackWithFwdCheck(Graph myGraph, int numColors)
    {
        prepare(myGraph.getNumNodes(), numColors);

        //initialize possible values data structure
        possibleValues = populatePossibleValues(myGraph);

        btWithFwdCheckColor(getNextNode(myGraph), myGraph);

        if (!hasSolution(myGraph)) cost *= -1;
    }

    private void btWithFwdCheckColor(int curNode, Graph myGraph)
    {
        cost++;
        boolean foundColor = false;

        //loop through the possible valid color assignments for the current node
        for (Integer curColor : possibleValues[curNode])
        {
            cost++;

            //check that this coloring is valid, using the additional look-ahead step to prune the search tree
            //don't need to check if it causes conflicts, because we are already limiting the options to only valid colors
            if (lookAhead(myGraph, curNode, curColor))
            {
                //nodeColors[curNode] = curColor
                int nextNode = getNextNode(myGraph); //uses a heuristic function to pick the next node to color in order to speed up processing

                if (nextNode < nodeColors.length) //check that the next node actually exists
                {
                    btWithFwdCheckColor(nextNode, myGraph); //recursively attempt to color the next node

                    //test if we successfully assigned the next node a color
                    //if so, then the coloring we just tried for curNode is good and the color was found!
                    if (nodeColors[nextNode] != colorVals[0]) foundColor = true;
                }
                else
                {
                    foundColor = true;
                }
            }

            //if we found a color assignment that works, stop trying any other ones
            if (foundColor) break;
        }

        //if there wasn't a valid color for this node, set it back to uncolored (backtracking step)
        if (!foundColor) resetNodeColor(myGraph, curNode);
    }

    private boolean lookAhead(Graph myGraph, int testNode, int testColor)
    {
        nodeColors[testNode] = testColor; //temporarily assign the test node with the test color - we're already guaranteed the test coloring won't cause any conflicts

        ArrayList<Integer> neighborList = myGraph.getEdges(testNode);

        //Check to make sure that each of the neighbors of the test node still have at least one valid variable assignment
        for (Integer i : neighborList)
        {
            cost++;

            //however, we only care about the neighbor node if it hasn't been assigned a color yet
            if (nodeColors[i] == colorVals[0])
            {
                //get the possible valid colors for node i given the test coloring
                possibleValues[i] = getPossibleValuesForNode(myGraph, i);
            }

            //if node i ended up with no valid colors, then the test color assignment failed the look-ahead
            if (possibleValues[i].size() == 0)
            {
                resetNodeColor(myGraph, testNode); //reset the test node back to uncolored
                return false;
            }
        }

        //if we made it this far, then that means that all the test node's neighbors still have at least one valid color, so it passed the look-ahead
        return true;
    }

    private void resetNodeColor(Graph myGraph, int theNode)
    {
        nodeColors[theNode] = colorVals[0]; //set the node to uncolored

        ArrayList<Integer> neighborList = myGraph.getEdges(theNode);

        //update the possible values of the node's neighbors, since this node is now uncolored
        for (Integer j : neighborList)
        {
            cost++;
            //if the neighbor isn't colored yet, update it's possible values
            //(if it is colored, that indicates that somewhere in the recursion chain we're already looping through it's possible vals)
            if (nodeColors[j] == colorVals[0]) possibleValues[j] = getPossibleValuesForNode(myGraph, j);
        }
    }
    //*********End Code for BackTracking w/ Fwd Check****
    //***************************************************

    //***************************************************
    //*********Code for BackTracking w/ MAC**************
    //NOTE: this algorithm uses an extra global var: ArrayList<Integer>[] possibleValues
    //This will represent the possible legal color assignments for any given node, i.e. possibleValues[i] is an ArrayList of all the color vals that node i can be legally assigned

    public void backtrackWithMAC(Graph myGraph, int numColors)
    {
        prepare(myGraph.getNumNodes(), numColors);

        //initialize possible values data structure
        possibleValues = populatePossibleValues(myGraph);

        btWithMACColor(getNextNode(myGraph), myGraph);

        if (!hasSolution(myGraph)) cost *= -1;
    }

    private void btWithMACColor(int curNode, Graph myGraph)
    {
        cost++;
        boolean foundColor = false;

        //loop through the possible valid color assignments for the current node
        for (Integer curColor : possibleValues[curNode])
        {
            cost++;

            //check if this coloring will maintain arc consistency after the effect has propagated through the constraints
            //don't need to check if it causes conflicts, because we are already limiting the options to only valid colors
            if (checkArcConsistency(myGraph, curNode, curColor))
            {
                int nextNode = getNextNode(myGraph);

                if (nextNode < nodeColors.length) //check that the next node actually exists
                {
                    btWithMACColor(nextNode, myGraph); //recursively attempt to color the next node

                    //test if we successfully assigned the next node a color
                    //if so, then the coloring we just tried for curNode is good and the color was found!
                    if (nodeColors[nextNode] != colorVals[0]) foundColor = true;
                }
                else
                {
                    foundColor = true;
                }
            }

            //if we found a color assignment that works, stop trying any other ones
            if (foundColor) break;
        }

        //THIS PART MIGHT NOT BE RIGHT
        //if there wasn't a valid color for this node, set it back to uncolored (backtracking step)
        if (!foundColor)
        {
            nodeColors[curNode] = colorVals[0];
            possibleValues = populatePossibleValues(myGraph);
        }
    }

    private boolean checkArcConsistency(Graph myGraph, int testNode, int testColor)
    {
        ArrayList<Integer>[] tempPossibleVals = populatePossibleValues(myGraph);
        nodeColors[testNode] = testColor; //temporarily assign the test node with the test color

        Queue<int[]> allEdges = myGraph.getAllEdges();

        while (allEdges.size() != 0)
        {
            cost++;
            int[] curEdge = allEdges.poll();
            int node1 = curEdge[0];
            int node2 = curEdge[1];

            //we're only concerned with testing arc consistency with unassigned (uncolored) nodes
            if (nodeColors[node1] != colorVals[0])
            {
                boolean removed = false;
                Integer[] possibleValsArr = (Integer[]) tempPossibleVals[node1].toArray();

                for (int i = 0; i <= possibleValsArr.length - 1; i++)
                {
                    cost++;
                    nodeColors[node1] = possibleValsArr[i];
                    ArrayList<Integer> node2NewPossVals = getPossibleValuesForNode(myGraph, node2);
                    nodeColors[node1] = colorVals[0]; //undo temporary color assignment

                    //if this potential coloring for node1 meant that node2 had no valid colors, then throw it out as a possibility for node1
                    if (node2NewPossVals.size() == 0)
                    {
                        tempPossibleVals[node1].remove(possibleValsArr[i]);
                        removed = true;
                    }
                }

                //if we removed any possible values from node1, then we need to rerun the arc consistency check on its neighbors
                if (removed)
                {
                    ArrayList<Integer> neighborList = myGraph.getEdges(node1);
                    for (int j : neighborList)
                    {
                        int[] edge = {j, node1};
                        allEdges.add(edge);
                    }
                }

                //if there are no possible values for node1 that allow it to maintain arc consistency, then this test coloring has failed the check
                if (tempPossibleVals[node1].size() == 0)
                {
                    nodeColors[testNode] = colorVals[0]; //undo temporary color assignment
                    return false;
                }
            }
        }

        //if we got all the way here, that means that the proposed coloring maintains arc consistency in the graph!
        //Also we trimmed the possible values way down
        possibleValues = tempPossibleVals;
        return true;
    }
    //*********End Code for BackTracking w/ MAC**********
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

    private ArrayList<Integer> getPossibleValuesForNode(Graph myGraph, int theNode) //SHARED BACKTRACKING PROC
    {
        ArrayList<Integer> values = new ArrayList<Integer>();
        ArrayList<Integer> neighborList = myGraph.getEdges(theNode);

        //loop through all possible colors and determine which ones are valid/possible for this node
        for (int i = 1; i <= colorVals.length - 1; i++)
        {
            cost++;
            boolean eligible = true;

            //test if the current color is shared with any of the node's neighbors
            if (causesConflicts(myGraph, theNode, colorVals[i])) eligible = false;
            if (eligible) values.add(colorVals[i]);
        }

        return values;
    }

    private ArrayList<Integer>[] populatePossibleValues(Graph myGraph)
    {
        //initialize possible values data structure
        ArrayList<Integer>[] retList = (ArrayList<Integer>[]) new ArrayList[myGraph.getNumNodes()];
        for (int i = 0; i <= retList.length - 1; i++)
        {
            cost++;
            if (nodeColors[i] == colorVals[0]) retList[i] = getPossibleValuesForNode(myGraph, i);
            else if (possibleValues[i] != null) retList[i] = possibleValues[i];
		    else System.out.println("ERROR POPULATING POSSIBLE VALUES!");
        }

        return retList;
    }
    //*********End Shared Backtracking Procedures********
    //***************************************************

}
