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
    public static boolean doesIntersect(LineSegment seg1, LineSegment seg2)
    {
        //SPECIAL CASE - if two line segments share ANY endpoints then DON'T consider them as intersecting
        if (seg1.getP1().equals(seg2.getP1()) || seg1.getP1().equals(seg2.getP2())
            || seg1.getP2().equals(seg2.getP1()) || seg1.getP2().equals(seg2.getP2())) return false;

        //orientation through seg1 to seg2.p1
        int o1 = Point.getOrientation(seg1.getP1(), seg1.getP2(), seg2.getP1());
        //orientation through seg1 to seg2.p2
        int o2 = Point.getOrientation(seg1.getP1(), seg1.getP2(), seg2.getP2());
        //orientation through seg2 to seg1.p1
        int o3 = Point.getOrientation(seg2.getP1(), seg2.getP2(), seg1.getP1());
        //orientation through seg2 to seg1.p2
        int o4 = Point.getOrientation(seg2.getP1(), seg2.getP2(), seg1.getP2());

        //general case
        if (o1 != o2 && o3 != o4) return true;

        //Special cases - point of one line segment is collinear with points of another
        //seg2.p1 is collinear with seg1.p1 and seg1.p2 - test if seg2.p1 lies between them
        if (o1 == 0 && seg2.getP1().liesBetween(seg1.getP1(), seg1.getP2())) return true;
        //seg2.p2 is collinear with seg1.p1 and seg1.p2 - test if seg2.p1 lies between them
        if (o2 == 0 && seg2.getP2().liesBetween(seg1.getP1(), seg1.getP2())) return true;
        //seg1.p1 is collinear with seg2.p1 and seg2.p2 - test if seg1.p1 lies between them
        if (o3 == 0 && seg1.getP1().liesBetween(seg2.getP1(), seg2.getP2())) return true;
        //seg1.p2 is collinear with seg2.p1 and seg2.p2 - test if seg1.p2 lies between them
        if (o4 == 0 && seg1.getP2().liesBetween(seg2.getP1(), seg2.getP2())) return true;

        //line segments don't intersect
        return false;
    }

    public String toString() { return getP1() + ", " + getP2() + " -> " + getLength(); }
}
