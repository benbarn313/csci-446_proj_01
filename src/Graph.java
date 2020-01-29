import java.util.*;

public class Graph {
    private Point[] points;
    private ArrayList<Integer>[] edges;
    private int numNodes;

    public Graph(int size) {
        numNodes = size;
        points = new Point[numNodes];
        edges = (ArrayList<Integer>[]) new ArrayList[size];
        for (int i = 0; i <= numNodes - 1; i++) edges[i] = new ArrayList<>(); //initialize adjacency lists

        buildGraph();
    }

    public int getNumNodes() { return numNodes; }
    public ArrayList<Integer> getEdges(int theNode) { return edges[theNode]; }

    public Queue<int[]> getAllEdges()
    {
        Queue<int[]> retVal = new LinkedList<int[]>();

        for (int i = 0; i <= numNodes - 1; i++)
        {
            ArrayList<Integer> neighborList = edges[i];

            for (Integer j : neighborList)
            {
                int[] edge = {i, j};
                retVal.add(edge);
            }
        }

        return retVal;
    }

    private void buildGraph()
    {
        //populate the points
        for (int i = 0; i <= numNodes - 1; i++) points[i] = getNextPoint();

        //priority queues to determine closest neighbor
        PriorityQueue<LineSegment>[] neighborQueues = populateQueues();
        //code to print the queue array for debugging; blocks graph creation
        if (Main.DEBUG_LEVEL >= 2) printQueues(populateQueues());

        boolean breakLoop = false;
        ArrayList<LineSegment> allSegments = new ArrayList<LineSegment>();
        Random randGen = new Random();

        //loop to build the edges of the graph
        while (!breakLoop)
        {
            int index = randGen.nextInt(numNodes); //randomly pick the next point to work with
            int oldIndex = index;

            while (neighborQueues[index].size() == 0 && !breakLoop) //if we picked a point that's gone through all it's neighbors, try the next one
            {
                index++;
                if (index == numNodes) index = 0;
                if (index == oldIndex) breakLoop = true; //if we've gone through all the points and they're all out of neighbors then break the loops
            }

            //get the next eligible line segment
            boolean getNextSegment = true;
            LineSegment curSegment = null;

            while (neighborQueues[index].size() > 0 && getNextSegment)
            {
                curSegment = neighborQueues[index].poll();
                //test for eligibilty - scan allSegments to see if it's already been used or intersects with any other segments
                if (isValidSegment(allSegments, curSegment)) getNextSegment = false;
            }

            if (!getNextSegment && curSegment != null) //means the current segment is valid
            {
                //determine the index of the line segment points in the points[] array
                int i = getIndexOfPoint(curSegment.getP1());
                int j = getIndexOfPoint(curSegment.getP2());

                //Add the edges; since this is not a directional graph we need to add the edge for both points
                edges[i].add(j);
                edges[j].add(i);

                allSegments.add(curSegment);
            }
        }
    }

    private Point getNextPoint()
    {
        Point thePoint;
        Random randGen = new Random();

        do {
            thePoint = new Point(randGen.nextDouble(), randGen.nextDouble());
        } while (getIndexOfPoint(thePoint) >= 0); //repeat until we get a unique point

        return thePoint;
    }

    private PriorityQueue<LineSegment>[] populateQueues()
    {
        PriorityQueue<LineSegment>[] retQueues = (PriorityQueue<LineSegment>[]) new PriorityQueue[numNodes];

        //populate the queue for point i with all the possible line segments from point i to other points
        for (int i = 0; i <= numNodes - 1; i++)
        {
            retQueues[i] = new PriorityQueue<>();

            for (int j = 0; j <= numNodes - 1; j++)
            {
                //LineSegment implements comparable based on distance property
                if (i != j) retQueues[i].add(new LineSegment(points[i], points[j]));
            }
        }

        return retQueues;
    }

    //helper function to test if a segment is valid
    private boolean isValidSegment(ArrayList<LineSegment> allSegments, LineSegment testSegment)
    {
        for (LineSegment curSeg : allSegments)
        {
            if (curSeg.equals(testSegment) || LineSegment.doesIntersect(curSeg, testSegment)) return false;
        }
        return true;
    }

    //helper function to determine what index a point has in the array of points
    private int getIndexOfPoint(Point thePoint)
    {
        for (int i = 0; i <= numNodes - 1; i++)
        {
            if (points[i] == null) return -1;
            if (thePoint.equals(points[i])) return i;
        }
        return -1;
}

    private void printQueues(PriorityQueue[] theQueues)
    {
        for (int i = 0; i <= theQueues.length - 1; i++)
        {
            System.out.println(i + "==========================");
            while (theQueues[i].size() > 0)
            {
                LineSegment curSeg = (LineSegment) theQueues[i].poll();
                System.out.println(curSeg);
            }
        }
    }

    public String toString()
    {
        String retString = "";

        for (int i = 0; i <= numNodes - 1; i++)
        {
            retString += i + ": ";
            for (int curNeighbor : edges[i]) retString += curNeighbor + ", ";
            retString += System.lineSeparator();
        }

        return retString;
    }
}
