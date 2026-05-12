package domuscontrol.ui;

import java.util.Scanner;

/**
 * Classe utilitária com métodos de leitura do teclado.
 * Usada pelos menus para ler input do utilizador.
 */
public class Leitura {

    private static final Scanner sc = new Scanner(System.in);

    private Leitura() {}

    public static String lerString(String prompt) {
        System.out.print(prompt);
        return sc.nextLine().trim();
    }

    public static int lerInt(String prompt) {
        while (true) {
            try {
                System.out.print(prompt);
                return Integer.parseInt(sc.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.println("  Valor invalido. Tente novamente.");
            }
        }
    }

    public static double lerDouble(String prompt) {
        while (true) {
            try {
                System.out.print(prompt);
                return Double.parseDouble(sc.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.println("  Valor invalido. Tente novamente.");
            }
        }
    }

    public static boolean lerSimNao(String prompt) {
        System.out.print(prompt + " (s/n): ");
        return sc.nextLine().trim().equalsIgnoreCase("s");
    }
}
