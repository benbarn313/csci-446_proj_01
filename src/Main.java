public class Main
{
    public static final int DEBUG_LEVEL = 1; //flag for triggering debug messages

    public static void main(String[] args)
    {
	    Graph myGraph = new Graph(20);
	    if (DEBUG_LEVEL >= 1) System.out.print(myGraph);
    }
}
