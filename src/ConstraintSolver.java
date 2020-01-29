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

}
