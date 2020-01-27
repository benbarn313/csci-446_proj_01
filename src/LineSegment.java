public class LineSegment implements Comparable<LineSegment>
{
    private Point p1, p2;
    private double length;

    public LineSegment(Point new1, Point new2)
    {
        p1 = new1;
        p2 = new2;
        length = p1.distanceTo(p2);
    }

    public Point getP1() { return p1; }
    public Point getP2() { return p2; }
    public double getLength() { return length; }

    //implement comparable
    public int compareTo(LineSegment otherSegment)
    {
        if (length == otherSegment.getLength()) return 0;
        else if (length > otherSegment.getLength()) return 1;
        else return -1;
    }

    public boolean equals(LineSegment otherSegment)
    {
        //two segments are equal if they use the same points, in any order
        if ((p1.equals(otherSegment.getP1())) && p2.equals(otherSegment.getP2())
                || (p1.equals(otherSegment.getP2()) && p2.equals(otherSegment.getP1()))) return true;
        return false;
    }

    //test if another line segment intersects with this one
    public boolean doesIntersect(LineSegment otherSegment)
    {
        int o1 = Point.getOrientation(getP1(), getP2(), otherSegment.getP1());
        int o2 = Point.getOrientation(getP1(), getP2(), otherSegment.getP2());
        int o3 = Point.getOrientation(otherSegment.getP1(), otherSegment.getP2(), getP1());
        int o4 = Point.getOrientation(otherSegment.getP1(), otherSegment.getP2(), getP2());

        //general case
        if (o1 != o2 && o3 != o4) return true;

        //Special cases - the line segments are colinear
        //Test if any of the points of one segment are between the start/end of the other
        if (o1 == 0 && otherSegment.getP1().liesBetween(getP1(), getP2())) return true;
        if (o2 == 0 && otherSegment.getP2().liesBetween(getP1(), getP2())) return true;
        if (o3 == 0 && getP1().liesBetween(otherSegment.getP1(), otherSegment.getP2())) return true;
        if (o4 == 0 && getP2().liesBetween(otherSegment.getP1(), otherSegment.getP2())) return true;

        //line segments don't intersect
        return false;
    }

    public String toString() { return getP1() + ", " + getP2() + " -> " + getLength(); }
}
