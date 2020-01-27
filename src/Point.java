public class Point
{
    private double x,y;

    public Point(double newX, double newY)
    {
        x = newX;
        y = newY;
    }

    public double getX() { return x; }
    public double getY() { return y; }

    public double distanceTo(Point otherPoint)
    {
        //distance between 2 points
        return Math.sqrt(Math.pow((getX() - otherPoint.getX()),2) + Math.pow((getY() - otherPoint.getY()), 2));
    }

    public boolean equals(Point otherPoint)
    {
        if (getX() == otherPoint.getX() && getY() == otherPoint.getY()) return true;
        return false;
    }

    //test if this point lies between two others
    public boolean liesBetween(Point start, Point end)
    {
        //first make sure they are collinear
        if (getOrientation(start, this, end) != 0) return false;

        if (this.getX() <= Math.max(start.getX(), end.getX()) && this.getX() >= Math.min(start.getX(), end.getX())
                && this.getY() <= Math.max(start.getY(), end.getY()) && this.getY() >= Math.min(start.getY(), end.getY()))
            return true;
        else return false;
    }

    //return whether a path between 3 points is collinear, clockwise, or counter-clockwise
    public static int getOrientation(Point start, Point mid, Point end)
    {
        double test = (((end.getY() - start.getY()) * (mid.getX() - start.getX())) - ((mid.getY() - start.getY()) * (end.getX() - start.getX())));

        if (test == 0) return 0; //collinear
        else if (test < 0) return 1; //clockwise
        else return 2; //counter-clockwise
    }

    public String toString() { return "(" + getX() + "," + getY() + ")"; }
}
