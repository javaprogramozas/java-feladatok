package alapfeladatok;

public class A15 {

    public static void main(String[] args) {
//        iteratívMegoldás();
        rekurzívMegoldás();
    }

    private static void iteratívMegoldás() {
        int előzőElőtti = 0;
        int előző = 1;

        System.out.print(előzőElőtti + " " + előző);

        for (int n = 2; n <= 10; n++) {
            int aktuális = előzőElőtti + előző;
            System.out.print(" " + aktuális);
            előzőElőtti = előző;
            előző = aktuális;
        }
    }

    private static void rekurzívMegoldás() {
        for (int n = 0; n <= 10; n++) {
            System.out.print(fibonacci(n) + " ");
        }
    }

    private static int fibonacci(int n) {
        if (n == 0) {
            return 0;
        }
        if (n == 1) {
            return 1;
        }
        return fibonacci(n - 1) + fibonacci(n - 2);
    }

    private static int fibonacci2(int n, int[] elemek) {
        if (n == 0) {
            elemek[0] = 0;
            return 0;
        }
        if (n == 1) {
            elemek[1] = 1;
            return 1;
        }
        if (elemek[n] == 0) {
            elemek[n] = fibonacci2(n - 1, elemek) + fibonacci2(n - 2, elemek);
        }
        return elemek[n];
    }
}
