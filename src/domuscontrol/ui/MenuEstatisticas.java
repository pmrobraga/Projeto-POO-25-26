package domuscontrol.ui;


import domuscontrol.DomusControl;
import domuscontrol.casa.Casa;

/**
 * Menu de estatisticas do sistema.
 */
public class MenuEstatisticas {

    private final DomusControl dc;

    public MenuEstatisticas(DomusControl dc) {
        this.dc = dc;
    }

    public void run() {
        NewMenu menu = new NewMenu("Estatisticas", new String[]{
            "Casa que mais consome",
            "Top 3 dispositivos por ativacoes",
            "Top 3 dispositivos por tempo ligado",
            "Top 3 divisoes com mais dispositivos",
            "Consumo actual por casa"
        });

        menu.setHandler(1, this::casaMaisConsome);
        menu.setHandler(2, this::topAtivacoes);
        menu.setHandler(3, this::topTempoLigado);
        menu.setHandler(4, this::topDivisoes);
        menu.setHandler(5, this::consumoAtual);

        menu.setPreCondition(1, () -> !dc.getTodasCasas().isEmpty());
        menu.setPreCondition(2, () -> !dc.getTodasCasas().isEmpty());
        menu.setPreCondition(3, () -> !dc.getTodasCasas().isEmpty());
        menu.setPreCondition(4, () -> !dc.getTodasCasas().isEmpty());
        menu.setPreCondition(5, () -> !dc.getTodasCasas().isEmpty());

        menu.run();
    }

    private void casaMaisConsome() {
        dc.getCasaQueMaisConsome().ifPresentOrElse(
            c -> System.out.printf("%n  Casa com maior consumo: %s (%.1f Wh acumulados)%n",
                    c.getNome(), c.getConsumoTotal()),
            () -> System.out.println("  Sem dados de consumo ainda.")
        );
    }

    private void topAtivacoes() {
        try {
            String cid = Leitura.lerString("  ID da casa: ");
            Casa c = dc.getCasa(cid);
            System.out.println("\n  Top 3 por activacoes em " + c.getNome() + ":");
            System.out.println("  ----------------------------------");
            var top = dc.getTopDispositivosPorAtivacoes(c, 3);
            if (top.isEmpty()) { System.out.println("  Sem dados."); return; }
            int i = 1;
            for (var d : top)
                System.out.printf("  %d. %-20s | %d ativacoes | %d min%n",
                    i++, d.getMarca() + " " + d.getModelo(),
                    d.getNumeroAtivacoes(), d.getTempoLigadoMinutos());
        } catch (Exception e) { System.out.println("  [ERRO] " + e.getMessage()); }
    }

    private void topTempoLigado() {
        try {
            String cid = Leitura.lerString("  ID da casa: ");
            Casa c = dc.getCasa(cid);
            System.out.println("\n  Top 3 por tempo ligado em " + c.getNome() + ":");
            System.out.println("  ----------------------------------");
            var top = dc.getTopDispositivosPorTempo(c, 3);
            if (top.isEmpty()) { System.out.println("  Sem dados."); return; }
            int i = 1;
            for (var d : top)
                System.out.printf("  %d. %-20s | %d min | %.1f Wh%n",
                    i++, d.getMarca() + " " + d.getModelo(),
                    d.getTempoLigadoMinutos(), d.getConsumoTotal());
        } catch (Exception e) { System.out.println("  [ERRO] " + e.getMessage()); }
    }

    private void topDivisoes() {
        System.out.println("\n  Top 3 divisoes com mais dispositivos:");
        System.out.println("  ----------------------------------");
        var top = dc.getTopDivisoesPorDispositivos(3);
        if (top.isEmpty()) { System.out.println("  Sem dados."); return; }
        int i = 1;
        for (var entry : top)
            System.out.printf("  %d. %-15s > %-20s (%d dispositivos)%n",
                i++, entry.getKey().getNome(),
                entry.getValue().getNome(),
                entry.getValue().getNumeroDispositivos());
    }

    private void consumoAtual() {
        System.out.println("\n  Consumo atual por casa:");
        System.out.println("  ----------------------------------");
        for (Casa c : dc.getTodasCasas())
            System.out.printf("  %-25s | %.1f Wh/h actual | %.1f Wh total%n",
                c.getNome(), c.getConsumoPorHoraAtual(), c.getConsumoTotal());
    }
}
