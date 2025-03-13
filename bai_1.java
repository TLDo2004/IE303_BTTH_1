//22520751

import java.util.Scanner;

public class bai_1 {

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        System.out.println("Nhap vao ban kinh r: ");
        double r = 0.0d;
        do {
            r = sc.nextFloat();
            if (r <= 0) {
                System.out.println("Ban kinh phai lon hon 0, vui long nhap lai!");
            }
        } while (r <= 0);

        long n = (long) r * 100000; // Số phần chia để tích phân chính xác hơn

        double area = approximateCircleArea(r, n);
        System.out.println("Dien tich hinh tron la: " + area);

        sc.close();
    }

    public static double approximateCircleArea(double r, long n) {
        double dx = r / (double) n; // Bước nhảy dx
        double sum = 0.0d;

        for (int i = 0; i < n; i++) {
            double x = i * dx; // giá trị tại điểm x
            double y = Math.sqrt(r * r - x * x); // y = sqrt(r^2 - x^2)

            sum += y * dx;
        }

        return 4 * sum;
    }
}
