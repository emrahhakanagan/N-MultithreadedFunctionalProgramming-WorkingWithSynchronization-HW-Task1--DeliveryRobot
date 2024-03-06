import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class DeliveryRobot {

    public static final Map<Integer, Integer> sizeToFreq = new HashMap<>();

    public static void main(String[] args) throws InterruptedException {
        Thread[] threads = new Thread[1000];

        for (int i = 0; i < threads.length; i++) {
            threads[i] = new Thread(() -> {
                String route = generateRoute("RLRFR", 100);
                int rCount = countOccurrences(route, 'R');

                synchronized (sizeToFreq) {
                    sizeToFreq.put(rCount, sizeToFreq.getOrDefault(rCount, 0) + 1);
                }
            });
            threads[i].start();
        }

        for (Thread thread : threads) {
            thread.join();
        }

        analyzeAndPrintResults();
    }

    public static String generateRoute(String letters, int length) {
        Random random = new Random();
        StringBuilder route = new StringBuilder();
        for (int i = 0; i < length; i++) {
            route.append(letters.charAt(random.nextInt(letters.length())));
        }
        return route.toString();
    }

    public static int countOccurrences(String str, char letter) {
        return (int) str.chars().filter(ch -> ch == letter).count();
    }

    public static void analyzeAndPrintResults() {
        int mostFrequentCount = sizeToFreq.entrySet().stream()
                .max(Map.Entry.comparingByValue())
                .map(Map.Entry::getKey)
                .orElse(-1);

        int frequency = sizeToFreq.get(mostFrequentCount);

        System.out.println("Самое частое количество повторений " + mostFrequentCount + " (встретилось " + frequency + " раз)");
        System.out.println("Другие размеры:");
        sizeToFreq.forEach((key, value) -> {
            if (key != mostFrequentCount) {
                System.out.println("- " + key + " (" + value + " раз)");
            }
        });
    }
}
