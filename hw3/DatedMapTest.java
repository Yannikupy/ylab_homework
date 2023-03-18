package hw3;

public class DatedMapTest {
    public static void main(String[] args) throws InterruptedException {
        DatedMap datedMap = new DatedMapImpl();
        datedMap.put("Last", "Insert");
        System.out.println(datedMap.get("Last"));
        System.out.println(datedMap.getKeyLastInsertionDate("Last"));
        datedMap.put("YLab", "COOL");
        System.out.println(datedMap.get("YLab"));
        System.out.println(datedMap.getKeyLastInsertionDate("YLab"));
        Thread.sleep(5000);
        datedMap.put("Last", "Bow");
        System.out.println(datedMap.get("Last"));
        System.out.println(datedMap.getKeyLastInsertionDate("Last"));
        datedMap.put("YLab", "coolest");
        System.out.println(datedMap.get("YLab"));
        System.out.println(datedMap.getKeyLastInsertionDate("Last"));
    }
}
