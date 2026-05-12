package domuscontrol.ui;

import domuscontrol.DomusControl;
import domuscontrol.utilizadores.Utilizador;

/**
 * Menu de autenticação.
 */
public class MenuLogin {

    private final DomusControl dc;
    private final Sessao sessao;

    public MenuLogin(DomusControl dc, Sessao sessao) {
        this.dc     = dc;
        this.sessao = sessao;
    }

    public boolean run() {
        System.out.println("\n╔══════════════════════════════════╗");
        System.out.println("║        DomusControl v1.0         ║");
        System.out.println("║            Bem-vindo!            ║");
        System.out.println("╚══════════════════════════════════╝");

        // Se nao existirem utilizadores, criar o primeiro admin
        if (dc.getTodosUtilizadores().isEmpty()) {
            System.out.println("  Nenhum utilizador registado.");
            System.out.println("  E necessario criar o primeiro administrador.\n");
            return criarPrimeiroAdmin();
        }

        // Loop do menu de acesso
        boolean sair = false;
        while (!sair) {
            System.out.println("\n  1. Login");
            System.out.println("  2. Criar utilizador");
            System.out.println("  0. Sair");
            String op = Leitura.lerString("  Opcao: ");

            switch (op) {
                case "1":
                    if (fazerLogin()) return true; // login bem sucedido — sai imediatamente
                    break;
                case "2":
                    criarUtilizador();
                    break;
                case "0":
                    sair = true;
                    break;
                default:
                    System.out.println("  Opcao invalida.");
            }
        }
        return false;
    }

    private boolean fazerLogin() {
        int tentativas = 3;
        while (tentativas > 0) {
            String email = Leitura.lerString("  Email: ");
            String pass  = Leitura.lerString("  Password: ");

            Utilizador encontrado = dc.getTodosUtilizadores().stream()
                    .filter(u -> u.getEmail().equalsIgnoreCase(email))
                    .findFirst()
                    .orElse(null);

            if (encontrado != null && encontrado.autenticar(pass)) {
                sessao.login(encontrado);
                System.out.println("\n  [OK] Bem-vindo, " + encontrado.getNome() + "!");
                System.out.println("  Tipo de acesso: " +
                        (sessao.isAdminEmAlgumaCasa() ? "ADMINISTRADOR" : "UTILIZADOR"));
                return true;
            }

            tentativas--;
            if (tentativas > 0)
                System.out.println("  [ERRO] Credenciais invalidas. " + tentativas + " tentativa(s) restante(s).");
        }
        System.out.println("  [ERRO] Numero de tentativas excedido.");
        return false;
    }

    private void criarUtilizador() {
        try {
            System.out.println("\n  -- Registo de novo utilizador --");
            String id    = Leitura.lerString("  ID: ");
            String nome  = Leitura.lerString("  Nome: ");
            String email = Leitura.lerString("  Email: ");
            String pass  = Leitura.lerString("  Password: ");
            dc.criarUtilizador(id, nome, email, pass);
            System.out.println("  [OK] Utilizador '" + nome + "' criado. Faca login para continuar.");
        } catch (Exception e) {
            System.out.println("  [ERRO] " + e.getMessage());
        }
    }

    private boolean criarPrimeiroAdmin() {
        try {
            System.out.println("  -- Criar primeiro administrador --");
            String id    = Leitura.lerString("  ID: ");
            String nome  = Leitura.lerString("  Nome: ");
            String email = Leitura.lerString("  Email: ");
            String pass  = Leitura.lerString("  Password: ");
            Utilizador u = dc.criarUtilizador(id, nome, email, pass);
            sessao.login(u);
            System.out.println("\n  [OK] Administrador criado. Bem-vindo, " + nome + "!");
            return true;
        } catch (Exception e) {
            System.out.println("  [ERRO] " + e.getMessage());
            return false;
        }
    }
}
