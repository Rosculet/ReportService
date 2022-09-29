import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Scanner;


/*
 Возможно не точь-в-точь исполнил по заданию, но захотелось реализовать свою логику максимально близкую, на мой взгляд, к бизнесу.
 Class ReportService содержит статик Мапу serviceMap - наша база данных. Class CacheService private static вовращает ВСЮ информацию.
 Class Key private static возвращает СТРАНУ и ДЕПАРТАМЕНТ ПОД KEY для hashmap, переопределены hashcode и equals.
 Метод addBranch принимает обьект класса CacheService(наш филиал), где автоматически инициализирует у себя Key, забирая поля country и department.
 Метод getBranchInfoAndAddIfNotFound проверяет есть ли значения по ключу, если нет, предлагает дозаполнить поля sales, income, потом добавить
 данный филиал (Branch) в базу и снова вызвать себя.
 */

public class ReportService {

    private static Map<Key, CacheService> serviceMap = new HashMap<>();

    private static class CacheService {
        String country;
        String department;
        int sales;
        int income;

        public CacheService(String country, String department, int sales, int income) {
            this.country = country;
            this.department = department;
            this.sales = sales;
            this.income = income;
        }

        @Override
        public String toString() {
            final StringBuffer sb = new StringBuffer(" Full Information { ");
            sb.append("country='").append(country).append('\'');
            sb.append(", department='").append(department).append('\'');
            sb.append(", sales=").append(sales);
            sb.append(", income=").append(income);
            sb.append(" }");
            return sb.toString();
        }
    }

    private static class Key {
        String country;
        String department;

        public Key(String country, String department) {
            this.country = country;
            this.department = department;
        }

        @Override
        public String toString() {
            return country + " | " + department + " ";
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Key key = (Key) o;
            return Objects.equals(country, key.country) && Objects.equals(department, key.department);
        }

        @Override
        public int hashCode() {
            return Objects.hash(country, department);
        }
    }

    public static void addBranch(CacheService cacheService) {
        serviceMap.put(new Key(cacheService.country, cacheService.department), cacheService);
    }

    public static void getBranchInfoAndAddIfNotFound(Key key) {
        if (key.country == null || key.department == null) {
            System.out.println("Failed Information");
            return;
        }

        if (serviceMap.containsKey(key)) {
            System.out.println(serviceMap.get(key));
        } else {
            System.out.println(" Branch not found, add this Branch press 'y' ");
            Scanner scanner = new Scanner(System.in);
            if (scanner.nextLine().equals("y")) {
                System.out.println("set sales");
                int sales = scanner.nextInt();
                System.out.println("set income");
                int income = scanner.nextInt();
                addBranch(new CacheService(key.country, key.department, sales, income));
                getBranchInfoAndAddIfNotFound(new Key(key.country, key.department));
                scanner.close();
            }
        }
    }

    public static void main(String[] args) {
        ReportService.addBranch(new CacheService("Berlin", "Finance", 890000, 90000));
        ReportService.addBranch(new CacheService("Toronto", "Finance", 890000, 90000));
        ReportService.getBranchInfoAndAddIfNotFound(new Key("Berlin", "Finance"));
        ReportService.getBranchInfoAndAddIfNotFound(new Key(null, null));
    }
}
