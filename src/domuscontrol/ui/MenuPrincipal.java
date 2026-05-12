package domuscontrol.ui;

import domuscontrol.DomusControl;
import domuscontrol.EstadoTeste;

import java.time.format.DateTimeFormatter;

/**
 * Menu principal do DomusControl.
 */
public class MenuPrincipal {

    private static final DateTimeFormatter FMT = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

    private final DomusControl dc;
    private final Sessao sessao;
    private boolean continuar = true;

    public MenuPrincipal(DomusControl dc, Sessao sessao) {
        this.dc     = dc;
        this.sessao = sessao;
    }

    public void run() {
        continuar = true;
        while (continuar) {
            mostrarMenu();
        }
        System.out.println("\n  Ate logo!");
    }

    private void mostrarMenu() {
        String tipoAcesso = sessao.isAdminEmAlgumaCasa() ? "ADMIN" : "USER";
        String titulo = "DomusControl [" + tipoAcesso + "] " + sessao.getNome()
                + " | " + dc.getRelogio().format(FMT);

        NewMenu menu = new NewMenu(titulo, new String[]{
                "Gerir Utilizadores",
                "Gerir Casas e Divisoes",
                "Gerir Dispositivos",
                "Automacoes e Escalonamentos",
                "Cenarios",
                "Estatisticas",
                "Avancar Tempo",
                "Gravar Estado",
                "Recarregar Estado de Teste",
                "Logout"
        });

        menu.setHandler(1, () -> new MenuUtilizadores(dc, sessao).run());
        menu.setHandler(2, () -> new MenuCasas(dc, sessao).run());
        menu.setHandler(3, () -> new MenuDispositivos(dc, sessao).run());
        menu.setHandler(4, () -> new MenuAutomacoes(dc, sessao).run());
        menu.setHandler(5, () -> new MenuCenarios(dc, sessao).run());
        menu.setHandler(6, () -> new MenuEstatisticas(dc).run());
        menu.setHandler(7, this::avancarTempo);
        menu.setHandler(8, this::gravarEstado);
        menu.setHandler(9, this::recarregarTeste);
        menu.setHandler(10, () -> { logout(); continuar = false; });

        menu.setPreCondition(1, () -> sessao.isAdminEmAlgumaCasa());
        menu.setPreCondition(2, () -> !dc.getTodasCasas().isEmpty());
        menu.setPreCondition(3, () -> !dc.getTodasCasas().isEmpty());
        menu.setPreCondition(4, () -> !dc.getTodasCasas().isEmpty());
        menu.setPreCondition(5, () -> !dc.getTodasCasas().isEmpty());
        menu.setPreCondition(6, () -> !dc.getTodasCasas().isEmpty());
        menu.setPreCondition(9, () -> sessao.isAdminEmAlgumaCasa());

        // Correr apenas UMA iteracao — quando volta recria com hora atualizada
        menu.show();
        int op = menu.readOption();
        if (op == 0) {
            continuar = false;
        } else if (op > 0) {
            if (!menu.isDisponivel(op)) {
                System.out.println("  Opcao indisponivel!");
            } else {
                menu.executeHandler(op);
            }
        }
    }

    private void avancarTempo() {
        int min = Leitura.lerInt("  Avancar quantos minutos? ");
        dc.avancarTempo(min);
        System.out.println("  [OK] Relogio: " + dc.getRelogio().format(FMT));
    }

    private void gravarEstado() {
        try {
            dc.gravarEstado();
            System.out.println("  [OK] Estado gravado em 'domuscontrol.dat'.");
        } catch (Exception e) { System.out.println("  [ERRO] " + e.getMessage()); }
    }

    private void recarregarTeste() {
        if (Leitura.lerSimNao("  Tens a certeza? Perdes o estado atual")) {
            EstadoTeste.inicializar(DomusControl.getInstance());
            System.out.println("  [OK] Estado de teste carregado.");
        }
    }

    private void logout() {
        sessao.logout();
        System.out.println("  Sessao terminada.");
    }
}


