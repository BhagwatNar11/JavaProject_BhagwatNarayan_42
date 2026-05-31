import java.io.*;
import java.util.*;

class ExpenseTracker {

    static class Expense {
        String category, date;
        double amount;

        Expense(String c, double a, String d) {
            category = c;
            amount = a;
            date = d;
        }

        public String toString() {
            return category + " ₹" + amount + " " + date;
        }
    }

    public static void main(String[] args) throws Exception {

        Scanner sc = new Scanner(System.in);
        ArrayList<Expense> list = new ArrayList<>();

        FileWriter fw = new FileWriter("expense.txt");

        for (int i = 1; i <= 3; i++) {

            System.out.println("\nExpense " + i);

            System.out.print("Category : ");
            String c = sc.nextLine();

            System.out.print("Amount : ");
            double a = sc.nextDouble();
            sc.nextLine();

            System.out.print("Date (MM/YYYY): ");
            String d = sc.nextLine();

            Expense e = new Expense(c, a, d);
            list.add(e);

            fw.write(e + "\n");
        }

        fw.close();

        System.out.println("\n--- All Expenses ---");

        double total = 0;

        HashMap<String, Double> map = new HashMap<>();

        for (Expense e : list) {
            System.out.println(e);

            total += e.amount;

            map.put(
                e.category,
                map.getOrDefault(e.category, 0.0) + e.amount
            );
        }

        System.out.println("\nMonthly Report Total : ₹" + total);

        String high = "";
        double max = 0;

        for (String k : map.keySet()) {
            if (map.get(k) > max) {
                max = map.get(k);
                high = k;
            }
        }

        System.out.println("Highest Expense Category : " + high);

        System.out.println("\n--- Data loaded from file ---");

        BufferedReader br =
                new BufferedReader(new FileReader("expense.txt"));

        String line;

        while ((line = br.readLine()) != null) {
            System.out.println(line);
        }

        br.close();
        sc.close();
    }
}