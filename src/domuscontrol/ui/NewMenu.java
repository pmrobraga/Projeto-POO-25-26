package domuscontrol.ui;

import java.util.*;

/**
 * Classe que implementa um menu em modo texto.
 * Baseada na classe fornecida nas aulas de POO,
 * com adaptações para o projeto DomusControl.
 */
public class NewMenu {

    /** Functional interface para handlers. */
    public interface Handler {
        void execute();
    }

    /** Functional interface para pré-condições. */
    public interface PreCondition {
        boolean validate();
    }

    private static Scanner is = new Scanner(System.in);

    private final String titulo;
    private final List<String> opcoes;
    private final List<PreCondition> disponivel;
    private final List<Handler> handlers;

    public NewMenu(String titulo, String[] opcoes) {
        this.titulo = titulo;
        this.opcoes = Arrays.asList(opcoes);
        this.disponivel = new ArrayList<>();
        this.handlers = new ArrayList<>();
        this.opcoes.forEach(s -> {
            this.disponivel.add(() -> true);
            this.handlers.add(() -> System.out.println("\nATENCAO: Opcao nao implementada!"));
        });
    }

    /**
     * Corre apenas uma iteracao — mostra o menu, le a opcao e executa.
     * Usado pelo MenuPrincipal para recriar o menu a cada iteracao.
     */
    public void runOnce() {
        show();
        int op = readOption();
        if (op > 0 && !this.disponivel.get(op - 1).validate()) {
            System.out.println("  Opcao indisponivel! Tente novamente.");
        } else if (op > 0) {
            this.handlers.get(op - 1).execute();
        }
    }

    public void run() {
        int op;
        do {
            show();
            op = readOption();
            if (op > 0 && !this.disponivel.get(op - 1).validate()) {
                System.out.println("  Opcao indisponivel! Tente novamente.");
            } else if (op > 0) {
                this.handlers.get(op - 1).execute();
            }
        } while (op != 0);
    }

    public void setPreCondition(int i, PreCondition b) {
        this.disponivel.set(i - 1, b);
    }

    public void setHandler(int i, Handler h) {
        this.handlers.set(i - 1, h);
    }

    private static final int LARGURA = 52;

    public void show() {
        String borda = "═".repeat(LARGURA);
        System.out.println("\n╔" + borda + "╗");
        System.out.println("║  " + pad(titulo) + "║");
        System.out.println("╠" + borda + "╣");
        for (int i = 0; i < this.opcoes.size(); i++) {
            String linha = (i + 1) + " - " + (this.disponivel.get(i).validate()
                    ? this.opcoes.get(i) : "(indisponivel)");
            System.out.println("║  " + pad(linha) + "║");
        }
        System.out.println("║  " + pad("0 - Sair") + "║");
        System.out.println("╚" + borda + "╝");
    }

    private String pad(String texto) {
        int espacos = LARGURA - 2 - texto.length();
        if (espacos <= 0) return texto;
        return texto + " ".repeat(espacos);
    }

    public int readOption() {
        int op;
        System.out.print("  Opcao: ");
        try {
            op = Integer.parseInt(is.nextLine().trim());
        } catch (NumberFormatException e) {
            op = -1;
        }
        if (op < 0 || op > this.opcoes.size()) {
            System.out.println("  Opcao invalida!");
            op = -1;
        }
        return op;
    }

    public boolean isDisponivel(int i) {
        if (i < 1 || i > this.disponivel.size()) return false;
        return this.disponivel.get(i - 1).validate();
    }

    public void executeHandler(int i) {
        if (i < 1 || i > this.handlers.size()) return;
        this.handlers.get(i - 1).execute();
    }
}

