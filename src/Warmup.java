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

    public static void main(String[] args) {
        System.out.println("--- FizzBuzz 1 to 20 ---");
        fizzBuzz();

        System.out.println("--- Palindromes ---");
        String[] words = {"racecar", "level", "Madam", "java"};
        for (String word : words) {
            System.out.println(word + " -> " + isPalindrome(word));
        }
    }
}
