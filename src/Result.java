import java.time.LocalDateTime;

public class Result
{
    private long cost, statesExamined;
    private int[] coloring;
    private LocalDateTime start, stop;

    public Result(){ }

    public Result(long newCost, long newStates, int[] newColoring, LocalDateTime newStart, LocalDateTime newStop)
    {
        cost = newCost;
        statesExamined = newStates;
        coloring = newColoring;
        start = newStart;
        stop = newStop;
    }

    public long getCost() { return cost; }
    public long getStatesExamined() { return statesExamined; }
    public LocalDateTime getStart() { return start; }
    public LocalDateTime getStop() { return stop; }

    public boolean compareColoring(int[] comparison)
    {
        if (comparison.length != coloring.length) return false;
        for (int i = 0; i <= coloring.length - 1; i++) if (coloring[i] != comparison[i]) return false;
        return true;
    }

    public String getColoring()
    {
        String retVal = "";
        String line1 = "";
        String line2 = "";

        for (int i = 0; i <= coloring.length - 1; i++)
        {
            line1 += " " + String.format("%1$" + 3 + "s",i) + " ";
            line2 += " " + String.format("%1$" + 3 + "s", colorMap(coloring[i])) + " ";
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

    public String toString()
    {
        return "Cost: " + getCost() + " | States: " + getStatesExamined();
    }
}
