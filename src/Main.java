import java.util.NoSuchElementException;
import java.util.Random;
import java.util.Scanner;

/**
 * Лабораторна робота №2. Обробка текстових даних мовою Java. Варіант 8.
 * <p>
 * Завдання 1: у п'ятизначних числах кожна парна цифра зменшується у два рази,
 * після чого перша і друга цифри міняються місцями. Числа обробляються як рядки.
 * <p>
 * Завдання 2: у тексті знаходяться всі цифри та обчислюється їхня сума.
 */
public class Main {

    // Межі кількості випадкових чисел. Десять рядків результату вміщуються на екрані.
    private static final int MIN_COUNT = 1;
    private static final int MAX_COUNT = 10;

    // Контрольні числа: приклад з умови та число з нулем на другому місці.
    private static final int[] CONTROL_NUMBERS = {64583, 70812};

    // Текст для завдання 2. Текстовий блок зберігає переходи на новий рядок.
    private static final String TEXT = """
            У 2026 році студенти виконують 6 лабораторних робіт.
            Кожна робота має 2 завдання, а звіт містить 4 розділи.
            Мій варіант завдання - 8.
            Чи вистачить 15 хвилин на перевірку?""";

    public static void main(String[] args) {
        System.out.println("Розробник: Ярмак Ю. О.");
        System.out.println();

        Scanner scanner = new Scanner(System.in);
        Random random = new Random();
        while (true) {
            int choice = readInt(scanner,
                    "Оберіть завдання (1 - числа, 2 - текст, 0 - вихід): ", 0, 2);
            System.out.println();
            switch (choice) {
                case 1 -> runNumberTask(scanner, random);
                case 2 -> runTextTask();
                default -> {
                    System.out.println("Роботу завершено.");
                    return;
                }
            }
            System.out.println();
        }
    }

    /** Завдання 1: обробляє контрольні числа, а потім задану кількість випадкових чисел. */
    static void runNumberTask(Scanner scanner, Random random) {
        System.out.println("Завдання 1. Обробка числових даних");
        System.out.println("Формат: вихідне число -> парні цифри зменшено у 2 рази -> 1-ша і 2-га цифри переставлено");
        System.out.println("Контрольні числа:");
        for (int number : CONTROL_NUMBERS) {
            printNumberResult(number);
        }

        int count = readInt(scanner,
                "Введіть кількість випадкових чисел (від " + MIN_COUNT + " до " + MAX_COUNT + "): ",
                MIN_COUNT, MAX_COUNT);
        System.out.println("Випадкові числа:");
        for (int i = 0; i < count; i++) {
            printNumberResult(randomFiveDigitNumber(random));
        }
    }

    static int randomFiveDigitNumber(Random random) {
        return 10000 + random.nextInt(90000);
    }

    /**
     * Перетворює число на рядок, обробляє його і виводить проміжний та кінцевий результат.
     * Результат залишається рядком, бо після перестановки першою цифрою може стати 0.
     */
    static void printNumberResult(int number) {
        String source = String.valueOf(number);
        String halved = halveEvenDigits(source);
        String result = swapFirstTwoDigits(halved);

        // Якщо перетворити такий рядок назад у число, нуль зник би і цифр стало б чотири.
        String note = result.charAt(0) == '0' ? "   (нуль на початку зберігається)" : "";
        System.out.println("  " + source + " -> " + halved + " -> " + result + note);
    }

    /**
     * Зменшує кожну парну цифру рядка у два рази.
     * Половина парної цифри завжди є цифрою від 0 до 4, тому довжина рядка не змінюється.
     * Нуль теж парний, і після ділення він залишається нулем.
     */
    static String halveEvenDigits(String digits) {
        StringBuilder sb = new StringBuilder(digits);
        for (int i = 0; i < sb.length(); i++) {
            int digit = Character.digit(sb.charAt(i), 10);
            if (digit % 2 == 0) {
                sb.setCharAt(i, Character.forDigit(digit / 2, 10));
            }
        }
        return sb.toString();
    }

    static String swapFirstTwoDigits(String digits) {
        StringBuilder sb = new StringBuilder(digits);
        char first = sb.charAt(0);
        sb.setCharAt(0, sb.charAt(1));
        sb.setCharAt(1, first);
        return sb.toString();
    }

    /** Завдання 2: виводить текст до і після обробки, знайдені цифри та їхню суму. */
    static void runTextTask() {
        System.out.println("Завдання 2. Обробка текстових даних");
        System.out.println("Вхідні дані: текст задано в програмі, додаткові дані не потрібні.");
        System.out.println("Текст до обробки:");
        System.out.println(TEXT);

        String digits = findDigits(TEXT);

        System.out.println();
        System.out.println("Текст після обробки (пошук цифр його не змінює):");
        System.out.println(TEXT);
        System.out.println();
        if (digits.isEmpty()) {
            System.out.println("У тексті немає цифр, тому сума дорівнює 0.");
            return;
        }
        System.out.println("Знайдені цифри (" + digits.length() + "): " + String.join(" ", digits.split("")));
        System.out.println("Сума цифр: " + String.join(" + ", digits.split("")) + " = " + sumDigits(digits));
    }

    /**
     * Повертає всі цифри тексту в порядку їх появи.
     * Кожна цифра враховується окремо, тому число 2026 дає цифри 2, 0, 2, 6.
     */
    static String findDigits(String text) {
        StringBuilder digits = new StringBuilder();
        for (int i = 0; i < text.length(); i++) {
            char ch = text.charAt(i);
            if (Character.isDigit(ch)) {
                digits.append(ch);
            }
        }
        return digits.toString();
    }

    /** Обчислює суму цифр, записаних у рядку. */
    static int sumDigits(String digits) {
        int sum = 0;
        for (int i = 0; i < digits.length(); i++) {
            // Character.digit перетворює символ '7' на число 7.
            sum += Character.digit(digits.charAt(i), 10);
        }
        return sum;
    }

    /**
     * Читає ціле число з проміжку [min, max].
     * Повторює запит, доки користувач не введе правильне значення.
     */
    static int readInt(Scanner scanner, String prompt, int min, int max) {
        while (true) {
            System.out.print(prompt);
            if (!scanner.hasNext()) {
                throw new NoSuchElementException("Введення закінчилось, а число не введено.");
            }
            if (!scanner.hasNextInt()) {
                System.out.println("Потрібно ввести ціле число.");
                scanner.next(); // пропускаємо некоректне значення
                continue;
            }
            int value = scanner.nextInt();
            if (value >= min && value <= max) {
                return value;
            }
            System.out.println("Число має бути від " + min + " до " + max + ".");
        }
    }
}
