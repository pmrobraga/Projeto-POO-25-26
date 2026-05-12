package domuscontrol.ui;


import domuscontrol.DomusControl;
import domuscontrol.automacoes.Automacao;
import domuscontrol.automacoes.Escalonamento;
import domuscontrol.automacoes.GestorAutomacoes;
import domuscontrol.casa.Casa;

/**
 * Menu de gestao de automacoes e escalonamentos.
 */
public class MenuAutomacoes {

    private final DomusControl dc;
    private final Sessao sessao;

    public MenuAutomacoes(DomusControl dc, Sessao sessao) {
        this.dc = dc;
        this.sessao = sessao;
    }

    public void run() {
        NewMenu menu = new NewMenu("Automacoes e Escalonamentos", new String[]{
            "Listar automacoes de uma casa",
            "Listar escalonamentos de uma casa",
            "Ativar/desativar automacao",
            "Ativar/desativar escalonamento",
            "Disparar automacao manualmente"
        });

        menu.setHandler(1, this::listarAutomacoes);
        menu.setHandler(2, this::listarEscalonamentos);
        menu.setHandler(3, this::toggleAutomacao);
        menu.setHandler(4, this::toggleEscalonamento);
        menu.setHandler(5, this::dispararAutomacao);

        menu.setPreCondition(1, () -> !dc.getTodasCasas().isEmpty());
        menu.setPreCondition(2, () -> !dc.getTodasCasas().isEmpty());
        menu.setPreCondition(3, () -> !dc.getTodasCasas().isEmpty());
        menu.setPreCondition(4, () -> !dc.getTodasCasas().isEmpty());
        menu.setPreCondition(5, () -> !dc.getTodasCasas().isEmpty());

        menu.run();
    }

    private GestorAutomacoes getGestor() throws Exception {
        String cid = Leitura.lerString("  ID da casa: ");
        Casa c = dc.getCasa(cid);
        return dc.getGestorAutomacoes(c);
    }

    private void listarAutomacoes() {
        try {
            GestorAutomacoes g = getGestor();
            System.out.println();
            if (g.getAutomacoes().isEmpty()) { System.out.println("  Nenhuma automacao definida."); return; }
            g.getAutomacoes().forEach(a -> System.out.println("  " + a));
        } catch (Exception e) { System.out.println("  [ERRO] " + e.getMessage()); }
    }

    private void listarEscalonamentos() {
        try {
            GestorAutomacoes g = getGestor();
            System.out.println();
            if (g.getEscalonamentos().isEmpty()) { System.out.println("  Nenhum escalonamento definido."); return; }
            g.getEscalonamentos().forEach(e -> System.out.println("  " + e));
        } catch (Exception e) { System.out.println("  [ERRO] " + e.getMessage()); }
    }

    private void toggleAutomacao() {
        try {
            GestorAutomacoes g = getGestor();
            String aid = Leitura.lerString("  ID da automacao: ");
            Automacao a = g.getAutomacao(aid);
            a.setAtiva(!a.isAtiva());
            System.out.println("  [OK] '" + a.getNome() + "' agora: " + (a.isAtiva() ? "ATIVA" : "INATIVA"));
        } catch (Exception e) { System.out.println("  [ERRO] " + e.getMessage()); }
    }

    private void toggleEscalonamento() {
        try {
            GestorAutomacoes g = getGestor();
            String eid = Leitura.lerString("  ID do escalonamento: ");
            Escalonamento e = g.getEscalonamento(eid);
            e.setAtivo(!e.isAtivo());
            System.out.println("  [OK] '" + e.getNome() + "' agora: " + (e.isAtivo() ? "ATIVO" : "INATIVO"));
        } catch (Exception e) { System.out.println("  [ERRO] " + e.getMessage()); }
    }

    private void dispararAutomacao() {
        try {
            GestorAutomacoes g = getGestor();
            String aid = Leitura.lerString("  ID da automacao: ");
            Automacao a = g.getAutomacao(aid);
            boolean disparou = a.avaliarEExecutar();
            if (disparou) {
                System.out.println("  [OK] '" + a.getNome() + "' disparada! (" + a.getAcoes().size() + " acoes)");
            } else {
                System.out.println("  Condicao nao verificada — nao disparou.");
                System.out.println("  Condicao: " + a.getCondicao().descrever());
            }
        } catch (Exception e) { System.out.println("  [ERRO] " + e.getMessage()); }
    }
}
