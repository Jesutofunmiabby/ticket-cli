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

    public static void main(String[] args) {
        System.out.println("--- FizzBuzz 1 to 20 ---");
        fizzBuzz();
    }
}
