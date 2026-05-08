package domuscontrol;

import domuscontrol.automacoes.Automacao;
import domuscontrol.automacoes.GestorAutomacoes;
import domuscontrol.casa.Casa;
import domuscontrol.dispositivos.*;
import domuscontrol.utilizadores.Utilizador;

import java.io.File;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;

public class Main {

    private static final Scanner sc = new Scanner(System.in);
    private static final DateTimeFormatter FMT = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

    public static void main(String[] args) {
        DomusControl dc = DomusControl.getInstance();
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

    private static void menuPrincipal(DomusControl dc) {
        boolean sair = false;
        while (!sair) {
            System.out.println("\n╔══════════════════════════════════╗");
            System.out.println("║        DomusControl v1.0         ║");
            System.out.printf ("║  Relogio: %-22s ║%n", dc.getRelogio().format(FMT));
            System.out.println("╠══════════════════════════════════╣");
            System.out.println("║ 1. Gerir Utilizadores            ║");
            System.out.println("║ 2. Gerir Casas e Divisoes        ║");
            System.out.println("║ 3. Gerir Dispositivos            ║");
            System.out.println("║ 4. Automacoes e Escalonamentos   ║");
            System.out.println("║ 5. Cenarios                      ║");
            System.out.println("║ 6. Estatisticas                  ║");
            System.out.println("║ 7. Avancar Tempo                 ║");
            System.out.println("║ 8. Gravar Estado                 ║");
            System.out.println("║ 0. Sair                          ║");
            System.out.println("╚══════════════════════════════════╝");
            System.out.print("Opcao: ");
            String opcao = sc.nextLine().trim();
            switch (opcao) {
                case "1": menuUtilizadores(dc);  break;
                case "2": menuCasas(dc);          break;
                case "3": menuDispositivos(dc);   break;
                case "4": menuAutomacoes(dc);     break;
                case "5": menuCenarios(dc);       break;
                case "6": menuEstatisticas(dc);   break;
                case "7": avancarTempo(dc);       break;
                case "8": gravarEstado(dc);       break;
                case "0": sair = true;            break;
                default:  System.out.println("Opcao invalida.");
            }
        }
        System.out.println("Ate logo!");
    }

    private static void menuUtilizadores(DomusControl dc) {
        System.out.println("\n-- Utilizadores --");
        System.out.println("1. Listar utilizadores");
        System.out.println("2. Criar utilizador");
        System.out.print("Opcao: ");
        String op = sc.nextLine().trim();
        if ("1".equals(op)) {
            dc.getTodosUtilizadores().forEach(System.out::println);
        } else if ("2".equals(op)) {
            try {
                System.out.print("ID: ");       String id    = sc.nextLine().trim();
                System.out.print("Nome: ");     String nome  = sc.nextLine().trim();
                System.out.print("Email: ");    String email = sc.nextLine().trim();
                System.out.print("Password: "); String pass  = sc.nextLine().trim();
                dc.criarUtilizador(id, nome, email, pass);
                System.out.println("Utilizador criado com sucesso.");
            } catch (Exception e) { System.err.println("Erro: " + e.getMessage()); }
        }
    }

    private static void menuCasas(DomusControl dc) {
        System.out.println("\n-- Casas e Divisoes --");
        System.out.println("1. Listar casas");
        System.out.println("2. Criar casa");
        System.out.println("3. Ver divisoes de uma casa");
        System.out.println("4. Criar divisao");
        System.out.print("Opcao: ");
        String op = sc.nextLine().trim();
        try {
            if ("1".equals(op)) {
                dc.getTodasCasas().forEach(c -> {
                    System.out.println(c);
                    c.getDivisoes().forEach(d -> System.out.println("   " + d));
                });
            } else if ("2".equals(op)) {
                System.out.print("ID da casa: ");  String id     = sc.nextLine().trim();
                System.out.print("Nome: ");        String nome   = sc.nextLine().trim();
                System.out.print("Morada: ");      String morada = sc.nextLine().trim();
                System.out.print("ID do admin: "); String uid    = sc.nextLine().trim();
                dc.criarCasa(id, nome, morada, dc.getUtilizador(uid));
                System.out.println("Casa criada com sucesso.");
            } else if ("3".equals(op)) {
                System.out.print("ID da casa: "); String cid = sc.nextLine().trim();
                dc.getCasa(cid).getDivisoes().forEach(d ->
                        System.out.println(d + " — consumo: "
                                + String.format("%.1f", d.getConsumoPorHoraActual()) + " Wh/h"));
            } else if ("4".equals(op)) {
                System.out.print("ID da casa: ");    String cid  = sc.nextLine().trim();
                System.out.print("ID da divisao: "); String did  = sc.nextLine().trim();
                System.out.print("Nome: ");          String nome = sc.nextLine().trim();
                System.out.print("ID do admin: ");   String uid  = sc.nextLine().trim();
                dc.criarDivisao(did, nome, dc.getCasa(cid), dc.getUtilizador(uid));
                System.out.println("Divisao criada com sucesso.");
            }
        } catch (Exception e) { System.err.println("Erro: " + e.getMessage()); }
    }

    private static void menuDispositivos(DomusControl dc) {
        System.out.println("\n-- Dispositivos --");
        System.out.println("1. Listar todos os dispositivos");
        System.out.println("2. Operar dispositivo (ligar/desligar)");
        System.out.println("3. Criar dispositivo");
        System.out.println("4. Associar dispositivo a divisao");
        System.out.print("Opcao: ");
        String op = sc.nextLine().trim();
        try {
            if ("1".equals(op)) {
                for (Casa c : dc.getTodasCasas()) {
                    System.out.println("\n" + c.getNome() + ":");
                    for (Dispositivo d : c.getTodosDispositivos())
                        System.out.println("  " + d.getEstadoDetalhado());
                }
            } else if ("2".equals(op)) {
                System.out.print("ID do dispositivo: "); String did = sc.nextLine().trim();
                System.out.print("ID da casa: ");        String cid = sc.nextLine().trim();
                Dispositivo d = dc.getCasa(cid).getDispositivoPorId(did);
                System.out.println("Estado atual: " + d.getEstadoDetalhado());
                System.out.print("Acao (1=ligar, 2=desligar): ");
                String acao = sc.nextLine().trim();
                if ("1".equals(acao)) { d.ligar();    System.out.println("Ligado."); }
                else if ("2".equals(acao)) { d.desligar(); System.out.println("Desligado."); }
            } else if ("3".equals(op)) {
                criarDispositivo(dc);
            } else if ("4".equals(op)) {
                associarDispositivo(dc);
            }
        } catch (Exception e) { System.err.println("Erro: " + e.getMessage()); }
    }

    private static void criarDispositivo(DomusControl dc) {
        try {
            System.out.println("\nTipos disponiveis:");
            System.out.println("1. Rele");
            System.out.println("2. Lampada");
            System.out.println("3. Coluna de Som");
            System.out.println("4. Cortina");
            System.out.println("5. Portao de Garagem");
            System.out.println("6. Sensor");
            System.out.print("Tipo: "); String tipo = sc.nextLine().trim();
            System.out.print("ID: ");     String id     = sc.nextLine().trim();
            System.out.print("Marca: ");  String marca  = sc.nextLine().trim();
            System.out.print("Modelo: "); String modelo = sc.nextLine().trim();
            System.out.print("Consumo por hora (Wh): ");
            double consumo = Double.parseDouble(sc.nextLine().trim());
            Dispositivo d = null;
            switch (tipo) {
                case "1": d = new Rele(id, marca, modelo, consumo); break;
                case "2":
                    System.out.print("Tem controlo de cor? (s/n): ");
                    boolean temCor = sc.nextLine().trim().equalsIgnoreCase("s");
                    d = new Lampada(id, marca, modelo, consumo, temCor);
                    break;
                case "3": d = new ColunaDeSom(id, marca, modelo, consumo); break;
                case "4": d = new Cortina(id, marca, modelo, consumo); break;
                case "5": d = new PortaoGaragem(id, marca, modelo, consumo); break;
                case "6":
                    System.out.print("Tipo de sensor (ex: Luminosidade): ");
                    String tipoSensor = sc.nextLine().trim();
                    System.out.print("Unidade (ex: lux): ");
                    String unidade = sc.nextLine().trim();
                    d = new Sensor(id, marca, modelo, consumo, tipoSensor, unidade);
                    break;
                default: System.out.println("Tipo invalido."); return;
            }
            dc.registarDispositivo(d);
            System.out.println("Dispositivo criado: " + d);
        } catch (Exception e) { System.err.println("Erro: " + e.getMessage()); }
    }

    private static void associarDispositivo(DomusControl dc) {
        try {
            System.out.print("ID da casa: ");        String cid  = sc.nextLine().trim();
            System.out.print("ID da divisao: ");     String did  = sc.nextLine().trim();
            System.out.print("ID do dispositivo: "); String disp = sc.nextLine().trim();
            System.out.print("ID do admin: ");       String uid  = sc.nextLine().trim();
            dc.associarDispositivoADivisao(disp, dc.getCasa(cid), did, dc.getUtilizador(uid));
            System.out.println("Dispositivo associado com sucesso.");
        } catch (Exception e) { System.err.println("Erro: " + e.getMessage()); }
    }

    private static void menuAutomacoes(DomusControl dc) {
        System.out.println("\n-- Automacoes e Escalonamentos --");
        System.out.println("1. Listar automacoes");
        System.out.println("2. Listar escalonamentos");
        System.out.println("3. Activar/desactivar automacao");
        System.out.print("Opcao: ");
        String op = sc.nextLine().trim();
        try {
            System.out.print("ID da casa: "); String cid = sc.nextLine().trim();
            GestorAutomacoes g = dc.getGestorAutomacoes(dc.getCasa(cid));
            if ("1".equals(op)) {
                g.getAutomacoes().forEach(System.out::println);
            } else if ("2".equals(op)) {
                g.getEscalonamentos().forEach(System.out::println);
            } else if ("3".equals(op)) {
                System.out.print("ID da automacao: "); String aid = sc.nextLine().trim();
                Automacao a = g.getAutomacao(aid);
                a.setActiva(!a.isAtiva());
                System.out.println("Automacao agora: " + (a.isAtiva() ? "ACTIVA" : "INACTIVA"));
            }
        } catch (Exception e) { System.err.println("Erro: " + e.getMessage()); }
    }

    private static void menuCenarios(DomusControl dc) {
        System.out.println("\n-- Cenarios --");
        System.out.println("1. Listar cenarios");
        System.out.println("2. Ativar cenario");
        System.out.print("Opcao: ");
        String op = sc.nextLine().trim();
        try {
            System.out.print("ID da casa: "); String cid = sc.nextLine().trim();
            GestorAutomacoes g = dc.getGestorAutomacoes(dc.getCasa(cid));
            if ("1".equals(op)) {
                g.getCenarios().forEach(System.out::println);
            } else if ("2".equals(op)) {
                System.out.print("ID do cenario: "); String cenId = sc.nextLine().trim();
                g.getCenario(cenId).activar();
            }
        } catch (Exception e) { System.err.println("Erro: " + e.getMessage()); }
    }

    private static void menuEstatisticas(DomusControl dc) {
        System.out.println("\n-- Estatisticas --");
        dc.getCasaQueMaisConsome().ifPresentOrElse(
                c -> System.out.println("Casa com maior consumo: " + c.getNome()
                        + " (" + String.format("%.1f", c.getConsumoTotal()) + " Wh)"),
                () -> System.out.println("Sem dados de consumo.")
        );
        System.out.println("\nTop 3 divisoes com mais dispositivos:");
        dc.getTopDivisoesPorDispositivos(3).forEach(e ->
                System.out.printf("  %s > %s (%d dispositivos)%n",
                        e.getKey().getNome(), e.getValue().getNome(),
                        e.getValue().getNumeroDispositivos()));
        System.out.println("\nTop 3 dispositivos por activacoes (por casa):");
        for (Casa c : dc.getTodasCasas()) {
            System.out.println("  " + c.getNome() + ":");
            dc.getTopDispositivosPorActivacoes(c, 3).forEach(d ->
                    System.out.printf("    %s — %d activacoes, %d min ligado%n",
                            d.getModelo(), d.getNumeroActivacoes(), d.getTempoLigadoMinutos()));
        }
    }

    private static void avancarTempo(DomusControl dc) {
        System.out.print("Avancar quantos minutos? ");
        try {
            int min = Integer.parseInt(sc.nextLine().trim());
            dc.avancarTempo(min);
            System.out.println("Relogio: " + dc.getRelogio().format(FMT));
        } catch (NumberFormatException e) { System.out.println("Valor invalido."); }
    }

    private static void gravarEstado(DomusControl dc) {
        try {
            dc.gravarEstado();
        } catch (Exception e) { System.err.println("Erro ao gravar: " + e.getMessage()); }
    }
}