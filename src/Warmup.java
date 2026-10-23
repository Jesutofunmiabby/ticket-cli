public class Warmup {

    // Part 1: FizzBuzz from 1 to 20
    static void fizzBuzz() {
        for (int i = 1; i <= 20; i++) {
            if (i % 15 == 0) {
                System.out.println("FizzBuzz");
            } else if (i % 3 == 0) {
                System.out.println("Fizz");
            } else if (i % 5 == 0) {
                System.out.println("Buzz");
            } else {
                System.out.println(i);
            }
        }
    }

    // Part 2: a word is a palindrome if it reads the same backwards
    static boolean isPalindrome(String s) {
        String lower = s.toLowerCase();
        String reversed = new StringBuilder(lower).reverse().toString();
        return reversed.equals(lower);
    }

    // Part 3: largest value in an int array
    static int largest(int[] numbers) {
        if (numbers.length == 0) {
            throw new IllegalArgumentException("Array must not be empty");
        }
        int max = numbers[0];
        for (int n : numbers) {
            if (n > max) {
                max = n;
            }
        }
        return max;
    }

    // Part 3: average of an int array, as a double so decimals are kept
    static double average(int[] numbers) {
        if (numbers.length == 0) {
            throw new IllegalArgumentException("Array must not be empty");
        }
        int sum = 0;
        for (int n : numbers) {
            sum += n;
        }
        return (double) sum / numbers.length;
    }

    public static void main(String[] args) {
        System.out.println("--- FizzBuzz 1 to 20 ---");
        fizzBuzz();

        System.out.println("--- Palindromes ---");
        String[] words = {"racecar", "level", "Madam", "java"};
        for (String word : words) {
            System.out.println(word + " -> " + isPalindrome(word));
        }

        System.out.println("--- Largest and average ---");
        int[] numbers = {4, 9, 2, 7, 3};
        System.out.println("Largest: " + largest(numbers));
        System.out.println("Average: " + average(numbers));
    }
}
