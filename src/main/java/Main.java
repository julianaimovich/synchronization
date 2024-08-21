import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class Main {

    public static final Map<Integer, Integer> sizeToFreq = new HashMap<>();

    public static void main(String[] args) {

        int threadsNumber = 1000;
        int commandLength = 100;
        char charToCount = 'R';
        String letters = "RLRFR";

        for (int i = 0; i < threadsNumber; i++) {
            new Thread(() -> {
                synchronized (sizeToFreq) {
                    String route = generateRoute(letters, commandLength);
                    // Key - количество повторений R в строке
                    int repeatCount = (int) route.chars().filter(ch -> ch == charToCount).count();
                    if (sizeToFreq.containsKey(repeatCount)) {
                        int stringCount = sizeToFreq.get(repeatCount);
                        sizeToFreq.put(repeatCount, stringCount + 1);
                    } else {
                        sizeToFreq.put(repeatCount, 1);
                    }
                }
            }).start();
        }
        getResults(sizeToFreq);
    }

    public static String generateRoute(String letters, int length) {
        Random random = new Random();
        StringBuilder route = new StringBuilder();
        for (int i = 0; i < length; i++) {
            route.append(letters.charAt(random.nextInt(letters.length())));
        }
        return route.toString();
    }

    public static void getResults(Map<Integer, Integer> result) {

        int stringWithMaxRepeat = result.entrySet().stream().max(Map.Entry.comparingByValue()).get().getValue();
        int repeatCount = result.entrySet().stream().filter(entry -> entry.getValue().equals(stringWithMaxRepeat)).map(Map.Entry::getKey).toList().get(0);

        System.out.println("Самое частое количество появлений " + repeatCount + " (встретилось " + stringWithMaxRepeat + " раз)");
        System.out.println("Другие размеры:");
        for (Map.Entry<Integer, Integer> entry : result.entrySet()) {
            System.out.println("- " + entry.getKey() + " (" + entry.getValue() + " раз)");
        }
    }
}