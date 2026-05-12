package domuscontrol.ui;


import domuscontrol.DomusControl;
import domuscontrol.casa.Casa;
import domuscontrol.utilizadores.TipoUtilizador;
import domuscontrol.utilizadores.Utilizador;

/**
 * Menu de gestao de utilizadores.
 */
public class MenuUtilizadores {

    private final DomusControl dc;
    private final Sessao sessao;

    public MenuUtilizadores(DomusControl dc, Sessao sessao) {
        this.dc = dc;
        this.sessao = sessao;
    }

    public void run() {
        NewMenu menu = new NewMenu("Utilizadores", new String[]{
            "Listar utilizadores",
            "Criar utilizador",
            "Ver casas de um utilizador",
            "Adicionar utilizador a uma casa"
        });

        menu.setHandler(1, this::listar);
        menu.setHandler(2, this::criar);
        menu.setHandler(3, this::verCasas);
        menu.setHandler(4, this::adicionarACasa);

        // Pre-condicao: so pode ver casas ou adicionar se houver utilizadores
        menu.setPreCondition(3, () -> !dc.getTodosUtilizadores().isEmpty());
        menu.setPreCondition(4, () -> !dc.getTodosUtilizadores().isEmpty() && !dc.getTodasCasas().isEmpty());

        menu.run();
    }

    private void listar() {
        System.out.println();
        if (dc.getTodosUtilizadores().isEmpty()) {
            System.out.println("  Nenhum utilizador registado.");
            return;
        }
        dc.getTodosUtilizadores().forEach(u -> System.out.println("  " + u));
    }

    private void criar() {
        try {
            String id    = Leitura.lerString("  ID: ");
            String nome  = Leitura.lerString("  Nome: ");
            String email = Leitura.lerString("  Email: ");
            String pass  = Leitura.lerString("  Password: ");
            dc.criarUtilizador(id, nome, email, pass);
            System.out.println("  [OK] Utilizador '" + nome + "' criado.");
        } catch (Exception e) { System.out.println("  [ERRO] " + e.getMessage()); }
    }

    private void verCasas() {
        try {
            String uid = Leitura.lerString("  ID do utilizador: ");
            Utilizador u = dc.getUtilizador(uid);
            if (u.getCasas().isEmpty()) {
                System.out.println("  Este utilizador nao tem casas.");
                return;
            }
            for (Casa c : u.getCasas()) {
                try {
                    TipoUtilizador tipo = u.getTipoNaCasa(c);
                    System.out.printf("  %s — %s (%s)%n", c.getId(), c.getNome(), tipo);
                } catch (Exception ignored) {}
            }
        } catch (Exception e) { System.out.println("  [ERRO] " + e.getMessage()); }
    }

    private void adicionarACasa() {
        try {
            String uid = Leitura.lerString("  ID do utilizador: ");
            String cid = Leitura.lerString("  ID da casa: ");
            System.out.println("  Tipo: 1=ADMINISTRADOR  2=UTILIZADOR");
            String tipo = Leitura.lerString("  Opcao: ");
            Utilizador u = dc.getUtilizador(uid);
            Casa c = dc.getCasa(cid);
            TipoUtilizador t = "1".equals(tipo) ? TipoUtilizador.ADMINISTRADOR : TipoUtilizador.UTILIZADOR;
            u.adicionarCasa(c, t);
            System.out.println("  [OK] Utilizador adicionado como " + t + ".");
        } catch (Exception e) { System.out.println("  [ERRO] " + e.getMessage()); }
    }
}
