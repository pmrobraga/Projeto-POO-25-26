package domuscontrol.ui;


import domuscontrol.DomusControl;
import domuscontrol.casa.Casa;
import domuscontrol.utilizadores.Utilizador;

/**
 * Menu de gestao de casas e divisoes.
 */
public class MenuCasas {

    private final DomusControl dc;
    private final Sessao sessao;

    public MenuCasas(DomusControl dc, Sessao sessao) {
        this.dc = dc;
        this.sessao = sessao;
    }

    public void run() {
        NewMenu menu = new NewMenu("Casas e Divisoes", new String[]{
            "Listar casas",
            "Criar casa",
            "Ver divisoes de uma casa",
            "Criar divisao"
        });

        menu.setHandler(1, this::listarCasas);
        menu.setHandler(2, this::criarCasa);
        menu.setHandler(3, this::verDivisoes);
        menu.setHandler(4, this::criarDivisao);

        menu.setPreCondition(3, () -> !dc.getTodasCasas().isEmpty());
        menu.setPreCondition(4, () -> sessao.isAdminEmAlgumaCasa());

        menu.run();
    }

    private void listarCasas() {
        System.out.println();
        if (dc.getTodasCasas().isEmpty()) {
            System.out.println("  Nenhuma casa registada.");
            return;
        }
        for (Casa c : dc.getTodasCasas()) {
            System.out.println("  " + c);
            c.getDivisoes().forEach(d ->
                System.out.printf("    > %-20s (%d dispositivos)%n",
                    d.getNome(), d.getNumeroDispositivos()));
        }
    }

    private void criarCasa() {
        try {
            String id     = Leitura.lerString("  ID da casa: ");
            String nome   = Leitura.lerString("  Nome: ");
            String morada = Leitura.lerString("  Morada: ");
            String uid    = Leitura.lerString("  ID do proprietario (admin): ");
            Utilizador u  = dc.getUtilizador(uid);
            dc.criarCasa(id, nome, morada, u);
            System.out.println("  [OK] Casa '" + nome + "' criada.");
        } catch (Exception e) { System.out.println("  [ERRO] " + e.getMessage()); }
    }

    private void verDivisoes() {
        try {
            String cid = Leitura.lerString("  ID da casa: ");
            Casa c = dc.getCasa(cid);
            System.out.println("\n  Divisoes de " + c.getNome() + ":");
            System.out.println("  ----------------------------------");
            c.getDivisoes().forEach(d ->
                System.out.printf("  [%s] %-20s | %d disp. | %.1f Wh/h%n",
                    d.getId(), d.getNome(),
                    d.getNumeroDispositivos(),
                    d.getConsumoPorHoraAtual()));
        } catch (Exception e) { System.out.println("  [ERRO] " + e.getMessage()); }
    }

    private void criarDivisao() {
        try {
            String cid  = Leitura.lerString("  ID da casa: ");
            String did  = Leitura.lerString("  ID da divisao: ");
            String nome = Leitura.lerString("  Nome: ");
            String uid  = Leitura.lerString("  ID do admin: ");
            Casa c       = dc.getCasa(cid);
            Utilizador u = dc.getUtilizador(uid);
            dc.criarDivisao(did, nome, c, u);
            System.out.println("  [OK] Divisao '" + nome + "' criada.");
        } catch (Exception e) { System.out.println("  [ERRO] " + e.getMessage()); }
    }
}
