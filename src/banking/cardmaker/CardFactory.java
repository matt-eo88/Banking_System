package banking.cardmaker;

import java.util.ArrayList;
import java.util.concurrent.ThreadLocalRandom;

public abstract class CardFactory {

    public static Card issueCard() {
        Card card = new Card(createCard(), generatePin());
        return card;
    }

    private static String createCard() {
        String s = generateCardNumber();
        String[] parts = s.split("");
        ArrayList<Integer> numbers = luhnAlgorithm(parts);
        int check = findChecksum(numbers);
        StringBuilder cardNumber = new StringBuilder();
        cardNumber.append(s);
        cardNumber.append(check);
        return cardNumber.toString();
    }

    private static int findChecksum(ArrayList<Integer> numbers) {
        int sum = 0;
        for (int i : numbers) {
            sum += i;
        }
        int check = 0;
        for (int i = 0; i < 10; i++) {
            int x = sum + i;
            if (x % 10 == 0) {
                check = i;
                break;
            }
        }
        return check;
    }

    private static String generateCardNumber() {
        StringBuilder number = new StringBuilder("400000");
        for (int i = 0; i < 9; i++) {
            int n = ThreadLocalRandom.current().nextInt(0, 10);
            number.append(n);
        }
        return number.toString();
    }

    private static String generatePin() {
        StringBuilder pin = new StringBuilder();
        for (int i = 0; i < 4; i++) {
            int n = ThreadLocalRandom.current().nextInt(0, 10);
            pin.append(n);
        }
        return pin.toString();
    }

    private static ArrayList<Integer> createIntArray(String[] parts) {
        ArrayList<Integer> numbers = new ArrayList<>();
        for (String str : parts) {
            int n = Integer.valueOf(str);
            numbers.add(n);
        }
        return numbers;
    }

    private static ArrayList<Integer> luhnAlgorithm(String[] parts) {
        ArrayList<Integer> numbers = createIntArray(parts);
        for (int i = 0; i < numbers.size(); i++) {
            int n = numbers.get(i);
            if (i % 2 == 0) {
                n = n * 2;
                if (n > 9) {
                    n = n - 9;
                }
                numbers.set(i, n);
            }
        }
        return numbers;
    }

    public static boolean isLuhn(String number) {
        String[] parts = number.split("");
        ArrayList<Integer> numbers = createIntArray(parts);
        for (int i = 0; i < numbers.size() - 1; i++) {
            int n = numbers.get(i);
            if (i % 2 == 0) {
                n = n * 2;
                if (n > 9) {
                    n = n - 9;
                }
                numbers.set(i, n);
            }
        }
        int sum = 0;
        for (int i : numbers) {
            sum += i;
        }
        return sum % 10 == 0;
    }
}
