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
 * Inicializa o sistema com um estado de teste completo e rico.
 * Inclui 3 casas, vários utilizadores, divisões e dispositivos.
 */
public class EstadoTeste {

    public static void inicializar(DomusControl dc) {
        try {
            System.out.println("=== A carregar estado de teste... ===\n");

            // ----------------------------------------------------------------
            // Utilizadores
            // ----------------------------------------------------------------
            Utilizador ana    = dc.criarUtilizador("U001", "Ana Silva",    "ana@email.com",    "1234");
            Utilizador bruno  = dc.criarUtilizador("U002", "Bruno Costa",  "bruno@email.com",  "abcd");
            Utilizador carla  = dc.criarUtilizador("U003", "Carla Mendes", "carla@email.com",  "pass");
            Utilizador david  = dc.criarUtilizador("U004", "David Lopes",  "david@email.com",  "1234");

            // ================================================================
            // CASA 1 — "Casa da Ana" (Porto)
            // ================================================================
            Casa casa1 = dc.criarCasa("CA001", "Casa da Ana", "Rua das Flores, 10, Porto", ana);
            bruno.adicionarCasa(casa1, TipoUtilizador.UTILIZADOR);

            // --- Divisões ---
            Divisao salaCasa1    = dc.criarDivisao("D001", "Sala de Estar",     casa1, ana);
            Divisao cozinhaCasa1 = dc.criarDivisao("D002", "Cozinha",           casa1, ana);
            Divisao quartoCasa1  = dc.criarDivisao("D003", "Quarto Principal",  casa1, ana);
            Divisao casaBanhoCasa1 = dc.criarDivisao("D004", "Casa de Banho",   casa1, ana);
            Divisao garagemCasa1 = dc.criarDivisao("D005", "Garagem",           casa1, ana);
            Divisao escritorioCasa1 = dc.criarDivisao("D006", "Escritorio",     casa1, ana);

            // --- Dispositivos Casa 1 ---
            Lampada lampSala1      = new Lampada("L001", "Philips", "Hue White Ambiance", 9.0, true);
            Lampada lampSala2      = new Lampada("L002", "Philips", "Hue Color",          10.0, true);
            ColunaDeSom colunaSala = new ColunaDeSom("C001", "Sonos", "One SL",            5.0);
            Cortina cortinaSala    = new Cortina("CT001", "Somfy", "Glydea Ultra",         15.0);
            Sensor sensorLuzSala   = new Sensor("S001", "Aqara", "MCCGQ01LM", 0.5, "Luminosidade", "lux");

            Lampada lampCozinha    = new Lampada("L003", "Ikea", "TRADFRI",               7.5);
            Rele tomadaCafe        = new Rele("R001", "TP-Link", "Tapo P100",             2.0);
            Rele tomadaMicroondas  = new Rele("R002", "TP-Link", "Tapo P110",             3.0);

            Lampada lampQuarto     = new Lampada("L004", "Philips", "Hue White",          8.0, true);
            Cortina cortinaQuarto  = new Cortina("CT002", "Somfy", "Glydea",              15.0);
            ColunaDeSom colunaQuarto = new ColunaDeSom("C002", "Bose", "SoundTouch 10",   4.0);
            Sensor sensorChuva     = new Sensor("S002", "Rain Bird", "RSD-BEx", 0.3, "Pluviosidade", "mm/h");

            Lampada lampBanho      = new Lampada("L005", "Osram", "Smart+ Spot",          6.0);
            Rele ventiladorBanho   = new Rele("R003", "Shelly", "Shelly 1",               10.0);

            PortaoGaragem portao   = new PortaoGaragem("PG001", "Somfy", "RTS Motor",     50.0);
            Sensor sensorMovGaragem = new Sensor("S003", "Xiaomi", "Mi Motion Sensor", 0.2, "Movimento", "bool");

            Lampada lampEscritorio = new Lampada("L006", "Philips", "Hue Go",             7.0, true);
            Rele tomadaPC          = new Rele("R004", "TP-Link", "Tapo P100",             2.0);

            // Registar todos no catalogo
            for (Dispositivo d : new Dispositivo[]{
                    lampSala1, lampSala2, colunaSala, cortinaSala, sensorLuzSala,
                    lampCozinha, tomadaCafe, tomadaMicroondas,
                    lampQuarto, cortinaQuarto, colunaQuarto, sensorChuva,
                    lampBanho, ventiladorBanho,
                    portao, sensorMovGaragem,
                    lampEscritorio, tomadaPC
            }) dc.registarDispositivo(d);

            // Associar a divisoes
            salaCasa1.adicionarDispositivo(lampSala1);
            salaCasa1.adicionarDispositivo(lampSala2);
            salaCasa1.adicionarDispositivo(colunaSala);
            salaCasa1.adicionarDispositivo(cortinaSala);
            salaCasa1.adicionarDispositivo(sensorLuzSala);

            cozinhaCasa1.adicionarDispositivo(lampCozinha);
            cozinhaCasa1.adicionarDispositivo(tomadaCafe);
            cozinhaCasa1.adicionarDispositivo(tomadaMicroondas);

            quartoCasa1.adicionarDispositivo(lampQuarto);
            quartoCasa1.adicionarDispositivo(cortinaQuarto);
            quartoCasa1.adicionarDispositivo(colunaQuarto);
            quartoCasa1.adicionarDispositivo(sensorChuva);

            casaBanhoCasa1.adicionarDispositivo(lampBanho);
            casaBanhoCasa1.adicionarDispositivo(ventiladorBanho);

            garagemCasa1.adicionarDispositivo(portao);
            garagemCasa1.adicionarDispositivo(sensorMovGaragem);

            escritorioCasa1.adicionarDispositivo(lampEscritorio);
            escritorioCasa1.adicionarDispositivo(tomadaPC);

            // --- Automacoes Casa 1 ---
            sensorLuzSala.ligar();
            sensorLuzSala.setValor(50.0);
            sensorChuva.ligar();
            sensorChuva.setValor(8.0);

            GestorAutomacoes gestorCasa1 = new GestorAutomacoes();

            Condicao condLuz = FabricaCondicoes.sensorAbaixoDe(sensorLuzSala, 100.0);
            Automacao autoLuz = new Automacao("A001", "Luz Fraca na Sala",
                    "Liga lampadas quando luminosidade < 100 lux", condLuz);
            autoLuz.adicionarAcao(FabricaAcoes.ligar(lampSala1));
            autoLuz.adicionarAcao(FabricaAcoes.definirIntensidade(lampSala1, 70));
            gestorCasa1.adicionarAutomacao(autoLuz);

            Condicao condChuva = FabricaCondicoes.sensorAcimaDe(sensorChuva, 5.0);
            Automacao autoChuva = new Automacao("A002", "Chuva Detectada",
                    "Fecha cortinas quando chove", condChuva);
            autoChuva.adicionarAcao(FabricaAcoes.definirAbertura(cortinaSala, 0));
            autoChuva.adicionarAcao(FabricaAcoes.definirAbertura(cortinaQuarto, 0));
            gestorCasa1.adicionarAutomacao(autoChuva);

            // --- Escalonamentos Casa 1 ---
            Escalonamento escalAcordar = new Escalonamento("E001", "Acordar", LocalTime.of(7, 30));
            escalAcordar.adicionarAcao(FabricaAcoes.definirAbertura(cortinaSala, 100));
            escalAcordar.adicionarAcao(FabricaAcoes.definirAbertura(cortinaQuarto, 100));
            escalAcordar.adicionarAcao(FabricaAcoes.definirIntensidade(lampQuarto, 30));
            gestorCasa1.adicionarEscalonamento(escalAcordar);

            Escalonamento escalDeitar = new Escalonamento("E002", "Hora de Dormir", LocalTime.of(23, 0));
            escalDeitar.adicionarAcao(FabricaAcoes.desligar(lampSala1));
            escalDeitar.adicionarAcao(FabricaAcoes.desligar(lampSala2));
            escalDeitar.adicionarAcao(FabricaAcoes.desligar(lampCozinha));
            escalDeitar.adicionarAcao(FabricaAcoes.desligar(colunaSala));
            gestorCasa1.adicionarEscalonamento(escalDeitar);

            Escalonamento escalCafe = new Escalonamento("E003", "Cafe Matinal",
                    LocalTime.of(7, 0),
                    Set.of(DayOfWeek.MONDAY, DayOfWeek.TUESDAY, DayOfWeek.WEDNESDAY,
                            DayOfWeek.THURSDAY, DayOfWeek.FRIDAY));
            escalCafe.adicionarAcao(FabricaAcoes.ligar(tomadaCafe));
            gestorCasa1.adicionarEscalonamento(escalCafe);

            Escalonamento escalLuzesSala = new Escalonamento("E004", "Luzes Sala Tarde",
                    LocalTime.of(18, 0), LocalTime.of(22, 0));
            escalLuzesSala.adicionarAcao(FabricaAcoes.ligar(lampSala1));
            escalLuzesSala.adicionarAcao(FabricaAcoes.definirIntensidade(lampSala1, 80));
            gestorCasa1.adicionarEscalonamento(escalLuzesSala);

            // --- Cenarios Casa 1 ---
            Cenario foraDeCasa = new Cenario("CEN001", "Fora de Casa", "Desliga tudo e fecha cortinas");
            foraDeCasa.adicionarAcao(FabricaAcoes.desligar(lampSala1));
            foraDeCasa.adicionarAcao(FabricaAcoes.desligar(lampSala2));
            foraDeCasa.adicionarAcao(FabricaAcoes.desligar(lampCozinha));
            foraDeCasa.adicionarAcao(FabricaAcoes.desligar(lampQuarto));
            foraDeCasa.adicionarAcao(FabricaAcoes.desligar(lampEscritorio));
            foraDeCasa.adicionarAcao(FabricaAcoes.desligar(colunaSala));
            foraDeCasa.adicionarAcao(FabricaAcoes.desligar(colunaQuarto));
            foraDeCasa.adicionarAcao(FabricaAcoes.desligar(tomadaCafe));
            foraDeCasa.adicionarAcao(FabricaAcoes.desligar(tomadaPC));
            foraDeCasa.adicionarAcao(FabricaAcoes.definirAbertura(cortinaSala, 0));
            foraDeCasa.adicionarAcao(FabricaAcoes.definirAbertura(cortinaQuarto, 0));
            foraDeCasa.adicionarAcao(FabricaAcoes.definirAbertura(portao, 0));
            gestorCasa1.adicionarCenario(foraDeCasa);

            Cenario jantarAmigos = new Cenario("CEN002", "Jantar com Amigos", "Luz ambiente e musica");
            jantarAmigos.adicionarAcao(FabricaAcoes.definirIntensidade(lampSala1, 40));
            jantarAmigos.adicionarAcao(FabricaAcoes.definirIntensidade(lampSala2, 30));
            jantarAmigos.adicionarAcao(FabricaAcoes.ligar(colunaSala));
            jantarAmigos.adicionarAcao(FabricaAcoes.definirVolume(colunaSala, 35));
            jantarAmigos.adicionarAcao(FabricaAcoes.definirAbertura(cortinaSala, 50));
            gestorCasa1.adicionarCenario(jantarAmigos);

            Cenario deitar = new Cenario("CEN003", "Deitar", "Desliga tudo, luz suave no quarto");
            deitar.adicionarAcao(FabricaAcoes.desligar(lampSala1));
            deitar.adicionarAcao(FabricaAcoes.desligar(lampSala2));
            deitar.adicionarAcao(FabricaAcoes.desligar(lampCozinha));
            deitar.adicionarAcao(FabricaAcoes.desligar(colunaSala));
            deitar.adicionarAcao(FabricaAcoes.definirIntensidade(lampQuarto, 10));
            deitar.adicionarAcao(FabricaAcoes.definirAbertura(cortinaQuarto, 0));
            gestorCasa1.adicionarCenario(deitar);

            Cenario acordar = new Cenario("CEN004", "Acordar", "Abre cortinas e luz suave");
            acordar.adicionarAcao(FabricaAcoes.definirAbertura(cortinaQuarto, 100));
            acordar.adicionarAcao(FabricaAcoes.definirAbertura(cortinaSala, 70));
            acordar.adicionarAcao(FabricaAcoes.definirIntensidade(lampQuarto, 50));
            acordar.adicionarAcao(FabricaAcoes.ligar(tomadaCafe));
            gestorCasa1.adicionarCenario(acordar);

            dc.setGestorAutomacoes(casa1, gestorCasa1);

            // ================================================================
            // CASA 2 — "Apartamento do Bruno" (Lisboa)
            // ================================================================
            Casa casa2 = dc.criarCasa("CA002", "Apartamento do Bruno", "Av. da Liberdade, 45, Lisboa", bruno);
            ana.adicionarCasa(casa2, TipoUtilizador.UTILIZADOR);

            Divisao salaJantarCasa2 = dc.criarDivisao("D010", "Sala de Jantar",    casa2, bruno);
            Divisao salaEstarCasa2  = dc.criarDivisao("D011", "Sala de Estar",     casa2, bruno);
            Divisao cozinhaCasa2    = dc.criarDivisao("D012", "Cozinha",            casa2, bruno);
            Divisao quartoMCasa2    = dc.criarDivisao("D013", "Quarto Master",      casa2, bruno);
            Divisao quartoHCasa2    = dc.criarDivisao("D014", "Quarto de Hospedes", casa2, bruno);
            Divisao varanda         = dc.criarDivisao("D015", "Varanda",            casa2, bruno);

            Lampada lampSalaJ      = new Lampada("L010", "IKEA", "TRADFRI E27",    8.5);
            Lampada lampSalaE      = new Lampada("L011", "Philips", "Hue Play",    7.0, true);
            Lampada lampSalaE2     = new Lampada("L012", "Philips", "Hue Play",    7.0, true);
            ColunaDeSom colunaTV   = new ColunaDeSom("C010", "Sonos", "Arc",       15.0);
            Cortina cortinaSalaJ   = new Cortina("CT010", "Ikea", "FYRTUR",        12.0);
            Cortina cortinaSalaE   = new Cortina("CT011", "Ikea", "FYRTUR",        12.0);

            Lampada lampCozinha2   = new Lampada("L013", "IKEA", "TRADFRI GU10",   5.0);
            Rele tomadaForno       = new Rele("R010", "Shelly", "Shelly 1PM",       5.0);
            Rele tomadaLava        = new Rele("R011", "Shelly", "Shelly 1PM",       5.0);

            Lampada lampQMaster    = new Lampada("L014", "Philips", "Hue White",   8.0, true);
            Cortina cortinaQM      = new Cortina("CT012", "Somfy", "Glydea",       15.0);
            ColunaDeSom colunaQM   = new ColunaDeSom("C011", "Marshall", "Stanmore", 6.0);

            Lampada lampQHosp      = new Lampada("L015", "IKEA", "TRADFRI E14",    4.0);
            Cortina cortinaQH      = new Cortina("CT013", "Ikea", "KADRILJ",       10.0);

            Lampada lampVaranda    = new Lampada("L016", "Osram", "Outdoor Spot",  8.0);
            Sensor sensorTemp      = new Sensor("S010", "Netatmo", "NWS01",   0.5, "Temperatura", "C");

            for (Dispositivo d : new Dispositivo[]{
                    lampSalaJ, lampSalaE, lampSalaE2, colunaTV, cortinaSalaJ, cortinaSalaE,
                    lampCozinha2, tomadaForno, tomadaLava,
                    lampQMaster, cortinaQM, colunaQM,
                    lampQHosp, cortinaQH,
                    lampVaranda, sensorTemp
            }) dc.registarDispositivo(d);

            salaJantarCasa2.adicionarDispositivo(lampSalaJ);
            salaJantarCasa2.adicionarDispositivo(cortinaSalaJ);

            salaEstarCasa2.adicionarDispositivo(lampSalaE);
            salaEstarCasa2.adicionarDispositivo(lampSalaE2);
            salaEstarCasa2.adicionarDispositivo(colunaTV);
            salaEstarCasa2.adicionarDispositivo(cortinaSalaE);

            cozinhaCasa2.adicionarDispositivo(lampCozinha2);
            cozinhaCasa2.adicionarDispositivo(tomadaForno);
            cozinhaCasa2.adicionarDispositivo(tomadaLava);

            quartoMCasa2.adicionarDispositivo(lampQMaster);
            quartoMCasa2.adicionarDispositivo(cortinaQM);
            quartoMCasa2.adicionarDispositivo(colunaQM);

            quartoHCasa2.adicionarDispositivo(lampQHosp);
            quartoHCasa2.adicionarDispositivo(cortinaQH);

            varanda.adicionarDispositivo(lampVaranda);
            varanda.adicionarDispositivo(sensorTemp);

            GestorAutomacoes gestorCasa2 = new GestorAutomacoes();

            sensorTemp.ligar();
            sensorTemp.setValor(35.0);
            Condicao condCalor = FabricaCondicoes.sensorAcimaDe(sensorTemp, 30.0);
            Automacao autoCalor = new Automacao("A010", "Calor na Varanda",
                    "Fecha cortinas quando temperatura > 30C", condCalor);
            autoCalor.adicionarAcao(FabricaAcoes.definirAbertura(cortinaSalaE, 20));
            autoCalor.adicionarAcao(FabricaAcoes.definirAbertura(cortinaSalaJ, 20));
            gestorCasa2.adicionarAutomacao(autoCalor);

            Condicao condFrio = FabricaCondicoes.sensorAbaixoDe(sensorTemp, 15.0);
            Automacao autoFrio = new Automacao("A011", "Frio Detectado",
                    "Liga luzes aconchegantes quando faz frio", condFrio);
            autoFrio.adicionarAcao(FabricaAcoes.definirIntensidade(lampSalaE, 60));
            autoFrio.adicionarAcao(FabricaAcoes.definirIntensidade(lampSalaE2, 60));
            gestorCasa2.adicionarAutomacao(autoFrio);

            Escalonamento escalFimDeSemana = new Escalonamento("E010", "Acordar Fim de Semana",
                    LocalTime.of(9, 0),
                    Set.of(DayOfWeek.SATURDAY, DayOfWeek.SUNDAY));
            escalFimDeSemana.adicionarAcao(FabricaAcoes.definirAbertura(cortinaQM, 50));
            escalFimDeSemana.adicionarAcao(FabricaAcoes.definirIntensidade(lampQMaster, 20));
            gestorCasa2.adicionarEscalonamento(escalFimDeSemana);

            Escalonamento escalNoite = new Escalonamento("E011", "Luzes Noturnas",
                    LocalTime.of(22, 0), LocalTime.of(7, 0));
            escalNoite.adicionarAcao(FabricaAcoes.ligar(lampVaranda));
            gestorCasa2.adicionarEscalonamento(escalNoite);

            Escalonamento escalTrabalho = new Escalonamento("E012", "Saida para Trabalho",
                    LocalTime.of(8, 30),
                    Set.of(DayOfWeek.MONDAY, DayOfWeek.TUESDAY, DayOfWeek.WEDNESDAY,
                            DayOfWeek.THURSDAY, DayOfWeek.FRIDAY));
            escalTrabalho.adicionarAcao(FabricaAcoes.desligar(lampSalaE));
            escalTrabalho.adicionarAcao(FabricaAcoes.desligar(lampSalaE2));
            escalTrabalho.adicionarAcao(FabricaAcoes.desligar(lampCozinha2));
            escalTrabalho.adicionarAcao(FabricaAcoes.definirAbertura(cortinaSalaE, 0));
            gestorCasa2.adicionarEscalonamento(escalTrabalho);

            Escalonamento escalRegresso = new Escalonamento("E013", "Regresso a Casa",
                    LocalTime.of(19, 0),
                    Set.of(DayOfWeek.MONDAY, DayOfWeek.TUESDAY, DayOfWeek.WEDNESDAY,
                            DayOfWeek.THURSDAY, DayOfWeek.FRIDAY));
            escalRegresso.adicionarAcao(FabricaAcoes.ligar(lampSalaE));
            escalRegresso.adicionarAcao(FabricaAcoes.definirIntensidade(lampSalaE, 70));
            escalRegresso.adicionarAcao(FabricaAcoes.definirAbertura(cortinaSalaE, 80));
            gestorCasa2.adicionarEscalonamento(escalRegresso);

            Cenario verCinema = new Cenario("CEN010", "Ver Cinema",
                    "Luzes baixas, volume alto, cortinas fechadas");
            verCinema.adicionarAcao(FabricaAcoes.definirIntensidade(lampSalaE, 15));
            verCinema.adicionarAcao(FabricaAcoes.definirIntensidade(lampSalaE2, 10));
            verCinema.adicionarAcao(FabricaAcoes.ligar(colunaTV));
            verCinema.adicionarAcao(FabricaAcoes.definirVolume(colunaTV, 60));
            verCinema.adicionarAcao(FabricaAcoes.definirAbertura(cortinaSalaE, 0));
            gestorCasa2.adicionarCenario(verCinema);

            Cenario foraCasa2 = new Cenario("CEN011", "Fora de Casa",
                    "Desliga tudo no apartamento");
            foraCasa2.adicionarAcao(FabricaAcoes.desligar(lampSalaJ));
            foraCasa2.adicionarAcao(FabricaAcoes.desligar(lampSalaE));
            foraCasa2.adicionarAcao(FabricaAcoes.desligar(lampSalaE2));
            foraCasa2.adicionarAcao(FabricaAcoes.desligar(lampCozinha2));
            foraCasa2.adicionarAcao(FabricaAcoes.desligar(lampQMaster));
            foraCasa2.adicionarAcao(FabricaAcoes.desligar(colunaTV));
            foraCasa2.adicionarAcao(FabricaAcoes.definirAbertura(cortinaSalaJ, 0));
            foraCasa2.adicionarAcao(FabricaAcoes.definirAbertura(cortinaSalaE, 0));
            foraCasa2.adicionarAcao(FabricaAcoes.definirAbertura(cortinaQM, 0));
            gestorCasa2.adicionarCenario(foraCasa2);

            Cenario deitarCasa2 = new Cenario("CEN012", "Deitar",
                    "Desliga sala e cozinha, luz minima no quarto");
            deitarCasa2.adicionarAcao(FabricaAcoes.desligar(lampSalaE));
            deitarCasa2.adicionarAcao(FabricaAcoes.desligar(lampSalaE2));
            deitarCasa2.adicionarAcao(FabricaAcoes.desligar(lampSalaJ));
            deitarCasa2.adicionarAcao(FabricaAcoes.desligar(colunaTV));
            deitarCasa2.adicionarAcao(FabricaAcoes.definirIntensidade(lampQMaster, 5));
            deitarCasa2.adicionarAcao(FabricaAcoes.definirAbertura(cortinaQM, 0));
            gestorCasa2.adicionarCenario(deitarCasa2);

            Cenario acordarCasa2 = new Cenario("CEN013", "Acordar",
                    "Abre cortinas e liga cafe");
            acordarCasa2.adicionarAcao(FabricaAcoes.definirAbertura(cortinaQM, 100));
            acordarCasa2.adicionarAcao(FabricaAcoes.definirIntensidade(lampQMaster, 40));
            acordarCasa2.adicionarAcao(FabricaAcoes.ligar(tomadaForno));
            gestorCasa2.adicionarCenario(acordarCasa2);

            dc.setGestorAutomacoes(casa2, gestorCasa2);

            // ================================================================
            // CASA 3 — "Moradia da Carla" (Braga)
            // ================================================================
            Casa casa3 = dc.criarCasa("CA003", "Moradia da Carla", "Rua do Souto, 22, Braga", carla);
            david.adicionarCasa(casa3, TipoUtilizador.UTILIZADOR);
            ana.adicionarCasa(casa3, TipoUtilizador.UTILIZADOR);

            Divisao salaCasa3      = dc.criarDivisao("D020", "Sala",               casa3, carla);
            Divisao cozinhaCasa3   = dc.criarDivisao("D021", "Cozinha",             casa3, carla);
            Divisao quartoP        = dc.criarDivisao("D022", "Quarto Principal",    casa3, carla);
            Divisao quarto2        = dc.criarDivisao("D023", "Quarto 2",            casa3, carla);
            Divisao quarto3        = dc.criarDivisao("D024", "Quarto 3",            casa3, carla);
            Divisao jardim         = dc.criarDivisao("D025", "Jardim",              casa3, carla);
            Divisao garagemCasa3   = dc.criarDivisao("D026", "Garagem",             casa3, carla);

            Lampada lampSala3      = new Lampada("L020", "Philips", "Hue Filament", 7.0);
            Lampada lampSala3b     = new Lampada("L021", "Philips", "Hue Filament", 7.0);
            ColunaDeSom colunaSala3 = new ColunaDeSom("C020", "Sonos", "Five",      15.0);
            Cortina cortinaSala3   = new Cortina("CT020", "Coulisse", "Neo",        18.0);
            Sensor sensorLuzSala3  = new Sensor("S020", "Fibaro", "FGMS-001", 0.3, "Luminosidade", "lux");
            Sensor sensorMovSala   = new Sensor("S021", "Fibaro", "FGMS-001", 0.3, "Movimento", "bool");

            Lampada lampCozinha3   = new Lampada("L022", "IKEA", "TRADFRI",        6.0);
            Rele tomadaCafe3       = new Rele("R020", "TP-Link", "Tapo P115",      2.0);
            Rele tomadaDishwasher  = new Rele("R021", "Shelly", "Shelly 2.5",      5.0);

            Lampada lampQP         = new Lampada("L023", "Philips", "Hue Color",   9.0, true);
            Cortina cortinaQP      = new Cortina("CT021", "Somfy", "Glydea",       15.0);
            ColunaDeSom colunaQP   = new ColunaDeSom("C021", "Bose", "SoundLink",  5.0);

            Lampada lampQ2         = new Lampada("L024", "IKEA", "TRADFRI",        5.0);
            Cortina cortinaQ2      = new Cortina("CT022", "Ikea", "FYRTUR",        12.0);

            Lampada lampQ3         = new Lampada("L025", "IKEA", "TRADFRI",        5.0);
            Cortina cortinaQ3      = new Cortina("CT023", "Ikea", "FYRTUR",        12.0);

            Lampada lampJardim     = new Lampada("L026", "Philips", "Hue Outdoor", 10.0);
            Lampada lampJardim2    = new Lampada("L027", "Philips", "Hue Outdoor", 10.0);
            Sensor sensorMovJardim = new Sensor("S022", "Hue", "PIR Outdoor", 0.4, "Movimento", "bool");

            PortaoGaragem portaoCasa3 = new PortaoGaragem("PG002", "Came", "GARD4",  45.0);
            Sensor sensorMovGar3   = new Sensor("S023", "Xiaomi", "Mi Sensor", 0.2, "Movimento", "bool");

            for (Dispositivo d : new Dispositivo[]{
                    lampSala3, lampSala3b, colunaSala3, cortinaSala3, sensorLuzSala3, sensorMovSala,
                    lampCozinha3, tomadaCafe3, tomadaDishwasher,
                    lampQP, cortinaQP, colunaQP,
                    lampQ2, cortinaQ2,
                    lampQ3, cortinaQ3,
                    lampJardim, lampJardim2, sensorMovJardim,
                    portaoCasa3, sensorMovGar3
            }) dc.registarDispositivo(d);

            salaCasa3.adicionarDispositivo(lampSala3);
            salaCasa3.adicionarDispositivo(lampSala3b);
            salaCasa3.adicionarDispositivo(colunaSala3);
            salaCasa3.adicionarDispositivo(cortinaSala3);
            salaCasa3.adicionarDispositivo(sensorLuzSala3);
            salaCasa3.adicionarDispositivo(sensorMovSala);

            cozinhaCasa3.adicionarDispositivo(lampCozinha3);
            cozinhaCasa3.adicionarDispositivo(tomadaCafe3);
            cozinhaCasa3.adicionarDispositivo(tomadaDishwasher);

            quartoP.adicionarDispositivo(lampQP);
            quartoP.adicionarDispositivo(cortinaQP);
            quartoP.adicionarDispositivo(colunaQP);

            quarto2.adicionarDispositivo(lampQ2);
            quarto2.adicionarDispositivo(cortinaQ2);

            quarto3.adicionarDispositivo(lampQ3);
            quarto3.adicionarDispositivo(cortinaQ3);

            jardim.adicionarDispositivo(lampJardim);
            jardim.adicionarDispositivo(lampJardim2);
            jardim.adicionarDispositivo(sensorMovJardim);

            garagemCasa3.adicionarDispositivo(portaoCasa3);
            garagemCasa3.adicionarDispositivo(sensorMovGar3);

            GestorAutomacoes gestorCasa3 = new GestorAutomacoes();

            sensorLuzSala3.ligar();
            sensorLuzSala3.setValor(40.0);
            sensorMovJardim.ligar();
            sensorMovJardim.setValor(1.0);

            Condicao condLuzBaixa3 = FabricaCondicoes.sensorAbaixoDe(sensorLuzSala3, 80.0);
            Automacao autoLuz3 = new Automacao("A020", "Luz Baixa na Sala",
                    "Liga luzes da sala quando escurece", condLuzBaixa3);
            autoLuz3.adicionarAcao(FabricaAcoes.ligar(lampSala3));
            autoLuz3.adicionarAcao(FabricaAcoes.ligar(lampSala3b));
            gestorCasa3.adicionarAutomacao(autoLuz3);

            Condicao condMovJardim = FabricaCondicoes.sensorAcimaDe(sensorMovJardim, 0.5);
            Automacao autoMovJardim = new Automacao("A021", "Movimento no Jardim",
                    "Liga luzes do jardim ao detetar movimento", condMovJardim);
            autoMovJardim.adicionarAcao(FabricaAcoes.ligar(lampJardim));
            autoMovJardim.adicionarAcao(FabricaAcoes.ligar(lampJardim2));
            gestorCasa3.adicionarAutomacao(autoMovJardim);

            Escalonamento escalManha3 = new Escalonamento("E020", "Manha", LocalTime.of(7, 0));
            escalManha3.adicionarAcao(FabricaAcoes.definirAbertura(cortinaQP, 100));
            escalManha3.adicionarAcao(FabricaAcoes.definirAbertura(cortinaQ2, 100));
            escalManha3.adicionarAcao(FabricaAcoes.definirAbertura(cortinaQ3, 100));
            escalManha3.adicionarAcao(FabricaAcoes.ligar(tomadaCafe3));
            gestorCasa3.adicionarEscalonamento(escalManha3);

            Escalonamento escalNoite3 = new Escalonamento("E021", "Luzes Jardim Noite",
                    LocalTime.of(20, 0), LocalTime.of(23, 30));
            escalNoite3.adicionarAcao(FabricaAcoes.ligar(lampJardim));
            escalNoite3.adicionarAcao(FabricaAcoes.ligar(lampJardim2));
            gestorCasa3.adicionarEscalonamento(escalNoite3);

            Escalonamento escalDormir3 = new Escalonamento("E022", "Dormir", LocalTime.of(23, 0));
            escalDormir3.adicionarAcao(FabricaAcoes.desligar(lampSala3));
            escalDormir3.adicionarAcao(FabricaAcoes.desligar(lampSala3b));
            escalDormir3.adicionarAcao(FabricaAcoes.desligar(colunaSala3));
            escalDormir3.adicionarAcao(FabricaAcoes.desligar(lampJardim));
            escalDormir3.adicionarAcao(FabricaAcoes.desligar(lampJardim2));
            gestorCasa3.adicionarEscalonamento(escalDormir3);

            Escalonamento escalFDS3 = new Escalonamento("E023", "Fim de Semana Manha",
                    LocalTime.of(9, 30),
                    Set.of(DayOfWeek.SATURDAY, DayOfWeek.SUNDAY));
            escalFDS3.adicionarAcao(FabricaAcoes.definirAbertura(cortinaSala3, 100));
            escalFDS3.adicionarAcao(FabricaAcoes.definirIntensidade(lampSala3, 50));
            gestorCasa3.adicionarEscalonamento(escalFDS3);

            Cenario foraCasa3 = new Cenario("CEN020", "Fora de Casa", "Desliga tudo e fecha tudo");
            foraCasa3.adicionarAcao(FabricaAcoes.desligar(lampSala3));
            foraCasa3.adicionarAcao(FabricaAcoes.desligar(lampSala3b));
            foraCasa3.adicionarAcao(FabricaAcoes.desligar(lampCozinha3));
            foraCasa3.adicionarAcao(FabricaAcoes.desligar(lampQP));
            foraCasa3.adicionarAcao(FabricaAcoes.desligar(lampQ2));
            foraCasa3.adicionarAcao(FabricaAcoes.desligar(lampQ3));
            foraCasa3.adicionarAcao(FabricaAcoes.desligar(colunaSala3));
            foraCasa3.adicionarAcao(FabricaAcoes.definirAbertura(cortinaSala3, 0));
            foraCasa3.adicionarAcao(FabricaAcoes.definirAbertura(cortinaQP, 0));
            foraCasa3.adicionarAcao(FabricaAcoes.definirAbertura(cortinaQ2, 0));
            foraCasa3.adicionarAcao(FabricaAcoes.definirAbertura(cortinaQ3, 0));
            foraCasa3.adicionarAcao(FabricaAcoes.definirAbertura(portaoCasa3, 0));
            gestorCasa3.adicionarCenario(foraCasa3);

            Cenario jantarCasa3 = new Cenario("CEN021", "Jantar com Amigos", "Ambiente acolhedor");
            jantarCasa3.adicionarAcao(FabricaAcoes.definirIntensidade(lampSala3, 35));
            jantarCasa3.adicionarAcao(FabricaAcoes.definirIntensidade(lampSala3b, 35));
            jantarCasa3.adicionarAcao(FabricaAcoes.ligar(colunaSala3));
            jantarCasa3.adicionarAcao(FabricaAcoes.definirVolume(colunaSala3, 30));
            jantarCasa3.adicionarAcao(FabricaAcoes.ligar(lampJardim));
            gestorCasa3.adicionarCenario(jantarCasa3);

            Cenario deitarCasa3 = new Cenario("CEN022", "Deitar", "Fecha cortinas e desliga");
            deitarCasa3.adicionarAcao(FabricaAcoes.desligar(lampSala3));
            deitarCasa3.adicionarAcao(FabricaAcoes.desligar(lampSala3b));
            deitarCasa3.adicionarAcao(FabricaAcoes.desligar(colunaSala3));
            deitarCasa3.adicionarAcao(FabricaAcoes.definirIntensidade(lampQP, 5));
            deitarCasa3.adicionarAcao(FabricaAcoes.definirAbertura(cortinaQP, 0));
            deitarCasa3.adicionarAcao(FabricaAcoes.desligar(lampJardim));
            deitarCasa3.adicionarAcao(FabricaAcoes.desligar(lampJardim2));
            gestorCasa3.adicionarCenario(deitarCasa3);

            Cenario acordarCasa3 = new Cenario("CEN023", "Acordar", "Abre tudo e liga cafe");
            acordarCasa3.adicionarAcao(FabricaAcoes.definirAbertura(cortinaQP, 100));
            acordarCasa3.adicionarAcao(FabricaAcoes.definirAbertura(cortinaSala3, 80));
            acordarCasa3.adicionarAcao(FabricaAcoes.definirIntensidade(lampQP, 40));
            acordarCasa3.adicionarAcao(FabricaAcoes.ligar(tomadaCafe3));
            gestorCasa3.adicionarCenario(acordarCasa3);

            dc.setGestorAutomacoes(casa3, gestorCasa3);

            // ----------------------------------------------------------------
            // Resumo
            // ----------------------------------------------------------------
            System.out.println("Estado de teste carregado com sucesso!");
            System.out.println("  Utilizadores : " + dc.getTodosUtilizadores().size());
            System.out.println("  Casas        : " + dc.getTodasCasas().size());
            System.out.println("  Casa 1 — " + casa1.getNome() + ": " + casa1.getDivisoes().size() + " divisoes, " + casa1.getTodosDispositivos().size() + " dispositivos");
            System.out.println("  Casa 2 — " + casa2.getNome() + ": " + casa2.getDivisoes().size() + " divisoes, " + casa2.getTodosDispositivos().size() + " dispositivos");
            System.out.println("  Casa 3 — " + casa3.getNome() + ": " + casa3.getDivisoes().size() + " divisoes, " + casa3.getTodosDispositivos().size() + " dispositivos");

        } catch (Exception e) {
            System.err.println("Erro ao inicializar estado de teste: " + e.getMessage());
            e.printStackTrace();
        }
    }
}