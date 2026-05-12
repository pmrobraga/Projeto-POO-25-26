package domuscontrol.ui;


import domuscontrol.DomusControl;
import domuscontrol.automacoes.GestorAutomacoes;
import domuscontrol.cenarios.Cenario;

/**
 * Menu de gestao e ativacao de cenarios.
 */
public class MenuCenarios {

    private final DomusControl dc;
    private final Sessao sessao;

    public MenuCenarios(DomusControl dc, Sessao sessao) {
        this.dc = dc;
        this.sessao = sessao;
    }

    public void run() {
        NewMenu menu = new NewMenu("Cenarios", new String[]{
            "Listar cenarios de uma casa",
            "Ativar cenario",
            "Ver detalhes de um cenario"
        });

        menu.setHandler(1, this::listar);
        menu.setHandler(2, this::ativar);
        menu.setHandler(3, this::detalhes);

        menu.setPreCondition(1, () -> !dc.getTodasCasas().isEmpty());
        menu.setPreCondition(2, () -> !dc.getTodasCasas().isEmpty());
        menu.setPreCondition(3, () -> !dc.getTodasCasas().isEmpty());

        menu.run();
    }

    private void listar() {
        try {
            String cid = Leitura.lerString("  ID da casa: ");
            GestorAutomacoes g = dc.getGestorAutomacoes(dc.getCasa(cid));
            System.out.println();
            if (g.getCenarios().isEmpty()) { System.out.println("  Nenhum cenario definido."); return; }
            g.getCenarios().forEach(c -> System.out.println("  " + c));
        } catch (Exception e) { System.out.println("  [ERRO] " + e.getMessage()); }
    }

    private void ativar() {
        try {
            String cid   = Leitura.lerString("  ID da casa: ");
            String cenId = Leitura.lerString("  ID do cenario: ");
            GestorAutomacoes g = dc.getGestorAutomacoes(dc.getCasa(cid));
            Cenario c = g.getCenario(cenId);
            System.out.println("\n  A ativar cenario '" + c.getNome() + "'...");
            System.out.println("  ----------------------------------");
            c.ativar();
            System.out.println("  [OK] " + c.getAcoes().size() + " acoes executadas.");
        } catch (Exception e) { System.out.println("  [ERRO] " + e.getMessage()); }
    }

    private void detalhes() {
        try {
            String cid   = Leitura.lerString("  ID da casa: ");
            String cenId = Leitura.lerString("  ID do cenario: ");
            GestorAutomacoes g = dc.getGestorAutomacoes(dc.getCasa(cid));
            Cenario c = g.getCenario(cenId);
            System.out.println("\n  " + c);
            System.out.println("  Acoes:");
            c.getAcoes().forEach(a -> System.out.println("    - " + a.descrever()));
            if (c.getUltimaAtivacao() != null)
                System.out.println("  Ultima ativacao: " + c.getUltimaAtivacao());
        } catch (Exception e) { System.out.println("  [ERRO] " + e.getMessage()); }
    }
}
