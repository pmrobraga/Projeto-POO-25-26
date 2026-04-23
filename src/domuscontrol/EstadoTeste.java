package domuscontrol;

import domuscontrol.automacoes.*;
import domuscontrol.casa.Casa;
import domuscontrol.casa.Divisao;
import domuscontrol.cenarios.Cenario;
import domuscontrol.dispositivos.*;
import domuscontrol.utilizadores.TipoUtilizador;
import domuscontrol.utilizadores.Utilizador;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.Set;

/**
 * Inicializa o sistema com um estado de teste completo.
 * Útil para demonstrações antes da serialização estar a funcionar.
 */
public class EstadoTeste {

    public static void inicializar(DomusControl dc) {
        try {
            System.out.println("=== A carregar estado de teste... ===\n");

            // ----------------------------------------------------------------
            // Utilizadores
            // ----------------------------------------------------------------
            Utilizador admin = dc.criarUtilizador("U001", "Ana Silva", "ana@email.com", "1234");
            Utilizador user1 = dc.criarUtilizador("U002", "Bruno Costa", "bruno@email.com", "abcd");

            // ----------------------------------------------------------------
            // Dispositivos — catálogo global
            // ----------------------------------------------------------------

            // Lâmpadas
            Lampada lampSala    = new Lampada("L001", "Philips", "Hue White", 9.0, true);
            Lampada lampCozinha = new Lampada("L002", "Ikea", "TRADFRI", 7.5);
            Lampada lampQuarto  = new Lampada("L003", "Philips", "Hue Color", 10.0, true);

            // Colunas
            ColunaDeSom colunaSala = new ColunaDeSom("C001", "Sonos", "One SL", 5.0);

            // Cortinas
            Cortina cortinaSala   = new Cortina("CT001", "Somfy", "Glydea", 15.0);
            Cortina cortinaQuarto = new Cortina("CT002", "Somfy", "Glydea", 15.0);

            // Portão
            PortaoGaragem portao = new PortaoGaragem("PG001", "Somfy", "RTS", 50.0);

            // Relé / Tomada
            Rele tomadaCafe = new Rele("R001", "TP-Link", "Tapo P100", 2.0);

            // Sensores
            Sensor sensorLuz  = new Sensor("S001", "Aqara", "MCCGQ01LM", 0.5, "Luminosidade", "lux");
            Sensor sensorChuva = new Sensor("S002", "Rain Bird", "RSD-BEx", 0.3, "Pluviosidade", "mm/h");

            // Registar no catálogo global
            for (Dispositivo d : new Dispositivo[]{
                    lampSala, lampCozinha, lampQuarto,
                    colunaSala, cortinaSala, cortinaQuarto,
                    portao, tomadaCafe, sensorLuz, sensorChuva
            }) {
                dc.registarDispositivo(d);
            }

            // ----------------------------------------------------------------
            // Casa 1 — "Casa da Ana"
            // ----------------------------------------------------------------
            Casa casa1 = dc.criarCasa("CA001", "Casa da Ana", "Rua das Flores, 10, Porto", admin);
            user1.adicionarCasa(casa1, TipoUtilizador.UTILIZADOR);

            // Divisões
            Divisao sala    = dc.criarDivisao("D001", "Sala de Estar", casa1, admin);
            Divisao cozinha = dc.criarDivisao("D002", "Cozinha",       casa1, admin);
            Divisao quarto  = dc.criarDivisao("D003", "Quarto Principal", casa1, admin);
            Divisao garagem = dc.criarDivisao("D004", "Garagem",       casa1, admin);

            // Associar dispositivos às divisões
            sala.adicionarDispositivo(lampSala);
            sala.adicionarDispositivo(colunaSala);
            sala.adicionarDispositivo(cortinaSala);
            sala.adicionarDispositivo(sensorLuz);

            cozinha.adicionarDispositivo(lampCozinha);
            cozinha.adicionarDispositivo(tomadaCafe);

            quarto.adicionarDispositivo(lampQuarto);
            quarto.adicionarDispositivo(cortinaQuarto);
            quarto.adicionarDispositivo(sensorChuva);

            garagem.adicionarDispositivo(portao);

            // ----------------------------------------------------------------
            // Gestor de automações da Casa 1
            // ----------------------------------------------------------------
            GestorAutomacoes gestor = new GestorAutomacoes();

            // --- Automação 1: luminosidade baixa → ligar lâmpada da sala ---
            sensorLuz.ligar();
            sensorLuz.setValor(50.0); // 50 lux (escuro)

            Condicao condLuz = FabricaCondicoes.sensorAbaixoDe(sensorLuz, 100.0);
            Automacao autoLuz = new Automacao("A001", "Luz Fraca",
                    "Liga lâmpada da sala quando luminosidade < 100 lux", condLuz);
            autoLuz.adicionarAcao(FabricaAcoes.ligar(lampSala));
            autoLuz.adicionarAcao(FabricaAcoes.definirIntensidade(lampSala, 70));
            gestor.adicionarAutomacao(autoLuz);

            // --- Automação 2: chuva → fechar cortinas ---
            sensorChuva.ligar();
            sensorChuva.setValor(8.0); // 8 mm/h (chuva moderada)

            Condicao condChuva = FabricaCondicoes.sensorAcimaDe(sensorChuva, 5.0);
            Automacao autoChuva = new Automacao("A002", "Chuva Detectada",
                    "Fecha cortinas quando deteta chuva > 5 mm/h", condChuva);
            autoChuva.adicionarAcao(FabricaAcoes.definirAbertura(cortinaSala, 0));
            autoChuva.adicionarAcao(FabricaAcoes.definirAbertura(cortinaQuarto, 0));
            gestor.adicionarAutomacao(autoChuva);

            // --- Escalonamento 1: acordar — abre cortinas às 07:30 ---
            Escalonamento escalAcordar = new Escalonamento("E001", "Acordar",
                    LocalTime.of(7, 30));
            escalAcordar.adicionarAcao(FabricaAcoes.definirAbertura(cortinaSala, 100));
            escalAcordar.adicionarAcao(FabricaAcoes.definirAbertura(cortinaQuarto, 100));
            escalAcordar.adicionarAcao(FabricaAcoes.definirIntensidade(lampQuarto, 30));
            gestor.adicionarEscalonamento(escalAcordar);

            // --- Escalonamento 2: deitar — apaga luzes às 23:00 ---
            Escalonamento escalDeitar = new Escalonamento("E002", "Hora de Dormir",
                    LocalTime.of(23, 0));
            escalDeitar.adicionarAcao(FabricaAcoes.desligar(lampSala));
            escalDeitar.adicionarAcao(FabricaAcoes.desligar(lampCozinha));
            escalDeitar.adicionarAcao(FabricaAcoes.desligar(colunaSala));
            gestor.adicionarEscalonamento(escalDeitar);

            // --- Escalonamento 3: café de manhã (seg-sex às 07:00) ---
            Escalonamento escalCafe = new Escalonamento("E003", "Café Matinal",
                    LocalTime.of(7, 0),
                    Set.of(DayOfWeek.MONDAY, DayOfWeek.TUESDAY, DayOfWeek.WEDNESDAY,
                            DayOfWeek.THURSDAY, DayOfWeek.FRIDAY));
            escalCafe.adicionarAcao(FabricaAcoes.ligar(tomadaCafe));
            gestor.adicionarEscalonamento(escalCafe);

            // --- Escalonamento 4: luzes da sala entre 18:00 e 22:00 ---
            Escalonamento escalLuzesSala = new Escalonamento("E004", "Luzes Sala Tarde",
                    LocalTime.of(18, 0), LocalTime.of(22, 0));
            escalLuzesSala.adicionarAcao(FabricaAcoes.ligar(lampSala));
            escalLuzesSala.adicionarAcao(FabricaAcoes.definirIntensidade(lampSala, 80));
            gestor.adicionarEscalonamento(escalLuzesSala);

            // ----------------------------------------------------------------
            // Cenários
            // ----------------------------------------------------------------

            // Cenário 1: Fora de Casa
            Cenario foraDeCasa = new Cenario("CEN001", "Fora de Casa",
                    "Desliga tudo, fecha cortinas e portão");
            foraDeCasa.adicionarAcao(FabricaAcoes.desligar(lampSala));
            foraDeCasa.adicionarAcao(FabricaAcoes.desligar(lampCozinha));
            foraDeCasa.adicionarAcao(FabricaAcoes.desligar(lampQuarto));
            foraDeCasa.adicionarAcao(FabricaAcoes.desligar(colunaSala));
            foraDeCasa.adicionarAcao(FabricaAcoes.desligar(tomadaCafe));
            foraDeCasa.adicionarAcao(FabricaAcoes.definirAbertura(cortinaSala, 0));
            foraDeCasa.adicionarAcao(FabricaAcoes.definirAbertura(cortinaQuarto, 0));
            foraDeCasa.adicionarAcao(FabricaAcoes.definirAbertura(portao, 0));
            gestor.adicionarCenario(foraDeCasa);

            // Cenário 2: Jantar com Amigos
            Cenario jantarAmigos = new Cenario("CEN002", "Jantar com Amigos",
                    "Luz ambiente na sala, música e cortinas a 50%");
            jantarAmigos.adicionarAcao(FabricaAcoes.definirIntensidade(lampSala, 40));
            jantarAmigos.adicionarAcao(FabricaAcoes.definirVolume(colunaSala, 35));
            jantarAmigos.adicionarAcao(FabricaAcoes.ligar(colunaSala));
            jantarAmigos.adicionarAcao(FabricaAcoes.definirAbertura(cortinaSala, 50));
            gestor.adicionarCenario(jantarAmigos);

            // Cenário 3: Deitar
            Cenario deitar = new Cenario("CEN003", "Deitar",
                    "Desliga tudo exceto luz suave do quarto, fecha cortinas");
            deitar.adicionarAcao(FabricaAcoes.desligar(lampSala));
            deitar.adicionarAcao(FabricaAcoes.desligar(lampCozinha));
            deitar.adicionarAcao(FabricaAcoes.desligar(colunaSala));
            deitar.adicionarAcao(FabricaAcoes.definirIntensidade(lampQuarto, 10));
            deitar.adicionarAcao(FabricaAcoes.definirAbertura(cortinaQuarto, 0));
            gestor.adicionarCenario(deitar);

            // Cenário 4: Acordar
            Cenario acordar = new Cenario("CEN004", "Acordar",
                    "Abre cortinas e liga luz suave do quarto");
            acordar.adicionarAcao(FabricaAcoes.definirAbertura(cortinaQuarto, 100));
            acordar.adicionarAcao(FabricaAcoes.definirAbertura(cortinaSala, 70));
            acordar.adicionarAcao(FabricaAcoes.definirIntensidade(lampQuarto, 50));
            gestor.adicionarCenario(acordar);

            // Guardar o gestor na casa (via DomusControl)
            dc.setGestorAutomacoes(casa1, gestor);

            System.out.println("Estado de teste carregado com sucesso!");
            System.out.println("  - " + dc.getTodosUtilizadores().size() + " utilizadores");
            System.out.println("  - " + dc.getTodasCasas().size() + " casas");
            System.out.println("  - Gestor: " + gestor);

        } catch (Exception e) {
            System.err.println("Erro ao inicializar estado de teste: " + e.getMessage());
            e.printStackTrace();
        }
    }

}
