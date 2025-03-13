
import java.util.*;

// 22520751
// Lớp điểm
class Point implements Comparable<Point> {

    int x, y;

    // Hàm khởi tạo
    public Point(Scanner sc) {
        this.x = sc.nextInt();
        this.y = sc.nextInt();
    }

    // Hàm so sánh 2 điểm (so sánh theo hoành độ, nếu hoành độ bằng nhau thì so sánh theo tung độ)
    // Trả về số âm nếu điểm hiện tại nhỏ hơn điểm p, trả về số dương nếu ngược lại, trả về 0 nếu bằng nhau
    @Override
    public int compareTo(Point p) {
        if (this.x == p.x) {
            return Long.compare(this.y, p.y);
        }
        return Long.compare(this.x, p.x);
    }

    // Hàm in ra điểm
    @Override
    public String toString() {
        return "(" + x + ", " + y + ")";
    }
}

// Lớp vector
class Vector {

    int x, y;

    // Hàm khởi tạo
    public Vector(int x, int y) {
        this.x = x;
        this.y = y;
    }

    // Hàm tính tích có hướng
    public int CrossProduct(Vector v) {
        return this.x * v.y - this.y * v.x;
    }

    // Hàm tìm vector từ 2 điểm
    public static Vector Subtract(Point a, Point b) {
        return new Vector(a.x - b.x, a.y - b.y);
    }
}

class ConvexHull {

    // Hàm xác định hướng quay của 3 điểm
    public static int orientation(Point a, Point b, Point c) {
        Vector ab = Vector.Subtract(b, a);
        Vector bc = Vector.Subtract(c, b);
        int cross = ab.CrossProduct(bc);

        if (cross < 0) {
            return -1; // Theo chiều kim đồng hồ
        }
        if (cross > 0) {
            return 1; // Ngược chiều kim đồng hồ
        }
        return 0; // Thẳng hàng
    }

    // Hàm kiểm tra ba điểm quay cùng chiều kim đồng hồ (Clockwise)
    public static boolean CW(Point a, Point b, Point c, boolean colinear) {
        int orientation = ConvexHull.orientation(a, b, c);
        return orientation < 0 || (colinear && orientation == 0);
    }

    // Hàm kiểm tra ba điểm quay ngược chiều kim đồng hồ (Counter Clockwise)
    public static boolean CCW(Point a, Point b, Point c, boolean colinear) {
        int orientation = ConvexHull.orientation(a, b, c);
        return orientation > 0 || (colinear && orientation == 0);
    }

    public static List<Point> convexHull(List<Point> points, boolean colinear) {
        int n = points.size();
        if (n < 3) {
            return new ArrayList<>(points);
        }

        Collections.sort(points);

        Point first = points.get(0);
        Point last = points.get(n - 1);
        ArrayList<Point> upper = new ArrayList<>();
        ArrayList<Point> lower = new ArrayList<>();
        upper.add(first);
        lower.add(first);

        for (int i = 1; i < n; i++) {
            Point p = points.get(i);

            if (i == n - 1 || CW(first, p, last, colinear)) {
                while (upper.size() >= 2 && !CW(upper.get(upper.size() - 2), upper.get(upper.size() - 1), p, colinear)) {
                    upper.remove(upper.size() - 1);
                }
                upper.add(p);
            }

            if (i == n - 1 || CCW(first, p, last, colinear)) {
                while (lower.size() >= 2 && !CCW(lower.get(lower.size() - 2), lower.get(lower.size() - 1), p, colinear)) {
                    lower.remove(lower.size() - 1);
                }
                lower.add(p);
            }
        }

        List<Point> hull = new ArrayList<>(upper);
        for (int i = lower.size() - 2; i > 0; i--) {
            hull.add(lower.get(i));
        }

        return hull;
    }
}

public class bai_3 {

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int n = sc.nextInt();

        List<Point> points = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            Point p = new Point(sc);
            points.add(p);
        }

        List<Point> hull = ConvexHull.convexHull(points, false);

        for (Point p : hull) {
            System.out.println(p);
        }

        sc.close();
    }
}
