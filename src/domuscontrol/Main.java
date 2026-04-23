package domuscontrol;

import domuscontrol.automacoes.Automacao;
import domuscontrol.automacoes.GestorAutomacoes;
import domuscontrol.casa.Casa;
import domuscontrol.dispositivos.*;

import java.io.File;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;

/**
 * Ponto de entrada do DomusControl.
 * Menu principal em modo texto.
 */
public class Main {

    private static final Scanner sc = new Scanner(System.in);
    private static final DateTimeFormatter FMT = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

    public static void main(String[] args) {
        DomusControl dc = DomusControl.getInstance();

        // Tenta carregar estado de ficheiro; se não existir, carrega estado de teste
        File f = new File("domuscontrol.dat");
        if (f.exists()) {
            try {
                dc = DomusControl.carregarEstado();
                System.out.println("Estado carregado do ficheiro.\n");
            } catch (Exception e) {
                System.err.println("Erro ao carregar estado: " + e.getMessage());
                EstadoTeste.inicializar(dc);
            }
        } else {
            EstadoTeste.inicializar(dc);
        }

        menuPrincipal(dc);
    }

    // =========================================================================
    // Menu Principal
    // =========================================================================

    private static void menuPrincipal(DomusControl dc) {
        boolean sair = false;
        while (!sair) {
            System.out.println("\n╔══════════════════════════════════╗");
            System.out.println("║        DomusControl v1.0         ║");
            System.out.printf ("║  Relógio: %-22s ║%n", dc.getRelogio().format(FMT));
            System.out.println("╠══════════════════════════════════╣");
            System.out.println("║ 1. Gerir Utilizadores            ║");
            System.out.println("║ 2. Gerir Casas e Divisões        ║");
            System.out.println("║ 3. Gerir Dispositivos            ║");
            System.out.println("║ 4. Automações e Escalonamentos   ║");
            System.out.println("║ 5. Cenários                      ║");
            System.out.println("║ 6. Estatísticas                  ║");
            System.out.println("║ 7. Avançar Tempo                 ║");
            System.out.println("║ 8. Gravar Estado                 ║");
            System.out.println("║ 0. Sair                          ║");
            System.out.println("╚══════════════════════════════════╝");
            System.out.print("Opção: ");

            String opcao = sc.nextLine().trim();
            switch (opcao) {
                case "1": menuUtilizadores(dc);         break;
                case "2": menuCasas(dc);                break;
                case "3": menuDispositivos(dc);         break;
                case "4": menuAutomacoes(dc);           break;
                case "5": menuCenarios(dc);             break;
                case "6": menuEstatisticas(dc);         break;
                case "7": avancarTempo(dc);             break;
                case "8": gravarEstado(dc);             break;
                case "0": sair = true;                  break;
                default:  System.out.println("Opção inválida.");
            }
        }
        System.out.println("Até logo!");
    }

    // =========================================================================
    // Utilizadores
    // =========================================================================

    private static void menuUtilizadores(DomusControl dc) {
        System.out.println("\n-- Utilizadores --");
        System.out.println("1. Listar utilizadores");
        System.out.println("2. Criar utilizador");
        System.out.print("Opção: ");
        String op = sc.nextLine().trim();

        if ("1".equals(op)) {
            dc.getTodosUtilizadores().forEach(System.out::println);
        } else if ("2".equals(op)) {
            try {
                System.out.print("ID: "); String id = sc.nextLine().trim();
                System.out.print("Nome: "); String nome = sc.nextLine().trim();
                System.out.print("Email: "); String email = sc.nextLine().trim();
                System.out.print("Password: "); String pass = sc.nextLine().trim();
                dc.criarUtilizador(id, nome, email, pass);
                System.out.println("Utilizador criado com sucesso.");
            } catch (Exception e) {
                System.err.println("Erro: " + e.getMessage());
            }
        }
    }

    // =========================================================================
    // Casas
    // =========================================================================

    private static void menuCasas(DomusControl dc) {
        System.out.println("\n-- Casas e Divisões --");
        System.out.println("1. Listar casas");
        System.out.println("2. Criar casa");
        System.out.println("3. Ver divisões de uma casa");
        System.out.print("Opção: ");
        String op = sc.nextLine().trim();

        try {
            if ("1".equals(op)) {
                dc.getTodasCasas().forEach(c -> {
                    System.out.println(c);
                    c.getDivisoes().forEach(d -> System.out.println("   " + d));
                });
            } else if ("2".equals(op)) {
                System.out.print("ID da casa: "); String id = sc.nextLine().trim();
                System.out.print("Nome: "); String nome = sc.nextLine().trim();
                System.out.print("Morada: "); String morada = sc.nextLine().trim();
                System.out.print("ID do proprietário (admin): "); String uid = sc.nextLine().trim();
                dc.criarCasa(id, nome, morada, dc.getUtilizador(uid));
                System.out.println("Casa criada.");
            } else if ("3".equals(op)) {
                System.out.print("ID da casa: "); String cid = sc.nextLine().trim();
                Casa c = dc.getCasa(cid);
                c.getDivisoes().forEach(d -> System.out.println(d + " — consumo atual: "
                        + String.format("%.1f", d.getConsumoPorHoraActual()) + " Wh/h"));
            }
        } catch (Exception e) {
            System.err.println("Erro: " + e.getMessage());
        }
    }

    // =========================================================================
    // Dispositivos
    // =========================================================================

    private static void menuDispositivos(DomusControl dc) {
        System.out.println("\n-- Dispositivos --");
        System.out.println("1. Listar todos os dispositivos");
        System.out.println("2. Operar dispositivo (ligar/desligar)");
        System.out.print("Opção: ");
        String op = sc.nextLine().trim();

        try {
            if ("1".equals(op)) {
                for (Casa c : dc.getTodasCasas()) {
                    System.out.println("\n" + c.getNome() + ":");
                    for (Dispositivo d : c.getTodosDispositivos()) {
                        System.out.println("  " + d.getEstadoDetalhado());
                    }
                }
            } else if ("2".equals(op)) {
                System.out.print("ID do dispositivo: "); String did = sc.nextLine().trim();
                System.out.print("ID da casa: "); String cid = sc.nextLine().trim();
                Dispositivo d = dc.getCasa(cid).getDispositivoPorId(did);
                System.out.println("Estado atual: " + d.getEstadoDetalhado());
                System.out.print("Ação (1=ligar, 2=desligar): ");
                String acao = sc.nextLine().trim();
                if ("1".equals(acao)) { d.ligar(); System.out.println("Ligado."); }
                else if ("2".equals(acao)) { d.desligar(); System.out.println("Desligado."); }
            }
        } catch (Exception e) {
            System.err.println("Erro: " + e.getMessage());
        }
    }

    // =========================================================================
    // Automações e Escalonamentos
    // =========================================================================

    private static void menuAutomacoes(DomusControl dc) {
        System.out.println("\n-- Automações e Escalonamentos --");
        System.out.println("1. Listar automações de uma casa");
        System.out.println("2. Listar escalonamentos de uma casa");
        System.out.println("3. Activar/desactivar automação");
        System.out.print("Opção: ");
        String op = sc.nextLine().trim();

        try {
            System.out.print("ID da casa: "); String cid = sc.nextLine().trim();
            Casa c = dc.getCasa(cid);
            GestorAutomacoes g = dc.getGestorAutomacoes(c);

            if ("1".equals(op)) {
                g.getAutomacoes().forEach(System.out::println);
            } else if ("2".equals(op)) {
                g.getEscalonamentos().forEach(System.out::println);
            } else if ("3".equals(op)) {
                System.out.print("ID da automação: "); String aid = sc.nextLine().trim();
                Automacao a = g.getAutomacao(aid);
                a.setActiva(!a.isAtiva());
                System.out.println("Automação agora: " + (a.isAtiva() ? "ATIVA" : "INATIVA"));
            }
        } catch (Exception e) {
            System.err.println("Erro: " + e.getMessage());
        }
    }

    // =========================================================================
    // Cenários
    // =========================================================================

    private static void menuCenarios(DomusControl dc) {
        System.out.println("\n-- Cenários --");
        System.out.println("1. Listar cenários de uma casa");
        System.out.println("2. Activar cenário");
        System.out.print("Opção: ");
        String op = sc.nextLine().trim();

        try {
            System.out.print("ID da casa: "); String cid = sc.nextLine().trim();
            Casa c = dc.getCasa(cid);
            GestorAutomacoes g = dc.getGestorAutomacoes(c);

            if ("1".equals(op)) {
                g.getCenarios().forEach(System.out::println);
            } else if ("2".equals(op)) {
                System.out.print("ID do cenário: "); String cenId = sc.nextLine().trim();
                g.getCenario(cenId).activar();
            }
        } catch (Exception e) {
            System.err.println("Erro: " + e.getMessage());
        }
    }

    // =========================================================================
    // Estatísticas
    // =========================================================================

    private static void menuEstatisticas(DomusControl dc) {
        System.out.println("\n-- Estatísticas --");

        // Casa que mais consome
        dc.getCasaQueMaisConsome().ifPresentOrElse(
            c -> System.out.println("Casa com maior consumo: " + c.getNome()
                    + " (" + String.format("%.1f", c.getConsumoTotal()) + " Wh)"),
            () -> System.out.println("Sem dados de consumo.")
        );

        // Top 3 divisões com mais dispositivos
        System.out.println("\nTop 3 divisões com mais dispositivos:");
        dc.getTopDivisoesPorDispositivos(3).forEach(e ->
            System.out.printf("  %s > %s (%d dispositivos)%n",
                e.getKey().getNome(),
                e.getValue().getNome(),
                e.getValue().getNumeroDispositivos())
        );

        // Top 3 dispositivos por activações (todas as casas)
        System.out.println("\nTop 3 dispositivos por activações (por casa):");
        for (Casa c : dc.getTodasCasas()) {
            System.out.println("  " + c.getNome() + ":");
            dc.getTopDispositivosPorActivacoes(c, 3).forEach(d ->
                System.out.printf("    %s — %d activações, %d min ligado%n",
                    d.getModelo(), d.getNumeroActivacoes(), d.getTempoLigadoMinutos())
            );
        }
    }

    // =========================================================================
    // Utilitários
    // =========================================================================

    private static void avancarTempo(DomusControl dc) {
        System.out.print("Avançar quantos minutos? ");
        try {
            int min = Integer.parseInt(sc.nextLine().trim());
            dc.avancarTempo(min);
            System.out.println("Relógio: " + dc.getRelogio().format(FMT));
        } catch (NumberFormatException e) {
            System.out.println("Valor inválido.");
        }
    }

    private static void gravarEstado(DomusControl dc) {
        try {
            dc.gravarEstado();
        } catch (Exception e) {
            System.err.println("Erro ao gravar: " + e.getMessage());
        }
    }
}
