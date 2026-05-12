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
            dc.reset(); // Limpar estado anterior

            // ----------------------------------------------------------------
            // Utilizadores
            // ----------------------------------------------------------------
            Utilizador ana    = dc.criarUtilizador("user1", "Ana Silva",    "ana@email.com",    "1234");
            Utilizador bruno  = dc.criarUtilizador("user2", "Bruno Costa",  "bruno@email.com",  "1234");
            Utilizador carla  = dc.criarUtilizador("user3", "Carla Mendes", "carla@email.com",  "1234");
            Utilizador david  = dc.criarUtilizador("user4", "David Lopes",  "david@email.com",  "1234");

            // ================================================================
            // CASA 1 — "Casa da Ana" (Porto)
            // ================================================================
            Casa casa1 = dc.criarCasa("Casa1", "Casa da Ana", "Rua das Flores, 10, Porto", ana);
            bruno.adicionarCasa(casa1, TipoUtilizador.UTILIZADOR);

            // --- Divisões ---
            Divisao salaCasa1    = dc.criarDivisao("Div1", "Sala de Estar",     casa1, ana);
            Divisao cozinhaCasa1 = dc.criarDivisao("Div2", "Cozinha",           casa1, ana);
            Divisao quartoCasa1  = dc.criarDivisao("Div3", "Quarto Principal",  casa1, ana);
            Divisao casaBanhoCasa1 = dc.criarDivisao("Div4", "Casa de Banho",   casa1, ana);
            Divisao garagemCasa1 = dc.criarDivisao("Div5", "Garagem",           casa1, ana);
            Divisao escritorioCasa1 = dc.criarDivisao("Div6", "Escritorio",     casa1, ana);

            // --- Dispositivos Casa 1 ---
            Termostato termoSala   = new Termostato("Term1", "Nest", "Learning Thermostat", 3.0);
            Televisao tvSala           = new Televisao("TV1", "Samsung", "QLED 55", 120.0);
            SensorMovimento sMovSala   = new SensorMovimento("SM1", "Philips", "Hue Motion", 0.3);
            SensorFumo sFumoSala       = new SensorFumo("SF1", "Fibaro", "Smoke Sensor", 0.5);
            SensorTemperatura sTempSala = new SensorTemperatura("ST1", "Netatmo", "Smart Room", 0.5);
            Lampada lampSala1      = new Lampada("L1", "Philips", "Hue White Ambiance", 9.0, true);
            Lampada lampSala2      = new Lampada("L2", "Philips", "Hue Color",          10.0, true);
            ColunaDeSom colunaSala = new ColunaDeSom("CS1", "Sonos", "One SL",            5.0);
            Cortina cortinaSala    = new Cortina("CT1", "Somfy", "Glydea Ultra",         15.0);
            Sensor sensorLuzSala   = new Sensor("SL1", "Aqara", "MCCGQ01LM", 0.5, "Luminosidade", "lux");

            Lampada lampCozinha    = new Lampada("L3", "Ikea", "TRADFRI",               7.5);
            Rele tomadaCafe        = new Rele("R1", "TP-Link", "Tapo P100",             2.0);
            Rele tomadaMicroondas  = new Rele("R2", "TP-Link", "Tapo P110",             3.0);

            Lampada lampQuarto     = new Lampada("L4", "Philips", "Hue White",          8.0, true);
            Cortina cortinaQuarto  = new Cortina("CT2", "Somfy", "Glydea",              15.0);
            ColunaDeSom colunaQuarto = new ColunaDeSom("CS2", "Bose", "SoundTouch 10",   4.0);
            Sensor sensorChuva     = new Sensor("SC2", "Rain Bird", "RSD-BEx", 0.3, "Pluviosidade", "mm/h");

            Lampada lampBanho      = new Lampada("L5", "Osram", "Smart+ Spot",          6.0);
            Rele ventiladorBanho   = new Rele("R3", "Shelly", "Shelly 1",               10.0);

            PortaoGaragem portao   = new PortaoGaragem("PG1", "Somfy", "RTS Motor",     50.0);
            Sensor sensorMovGaragem = new Sensor("SM3", "Xiaomi", "Mi Motion Sensor", 0.2, "Movimento", "bool");

            Lampada lampEscritorio = new Lampada("L6", "Philips", "Hue Go",             7.0, true);
            Rele tomadaPC          = new Rele("R4", "TP-Link", "Tapo P100",             2.0);

            // Registar todos no catalogo
            for (Dispositivo d : new Dispositivo[]{
                    termoSala, tvSala, sMovSala, sFumoSala, sTempSala,
                    lampSala1, lampSala2, colunaSala, cortinaSala, sensorLuzSala,
                    lampCozinha, tomadaCafe, tomadaMicroondas,
                    lampQuarto, cortinaQuarto, colunaQuarto, sensorChuva,
                    lampBanho, ventiladorBanho,
                    portao, sensorMovGaragem,
                    lampEscritorio, tomadaPC
            }) dc.registarDispositivo(d);

            // Associar a divisoes
            salaCasa1.adicionarDispositivo(termoSala);
            salaCasa1.adicionarDispositivo(tvSala);
            salaCasa1.adicionarDispositivo(sMovSala);
            salaCasa1.adicionarDispositivo(sFumoSala);
            salaCasa1.adicionarDispositivo(sTempSala);
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
            sMovSala.ligar();
            sTempSala.ligar();
            sTempSala.setValor(19.0);
            sTempSala.setHumidade(55.0);
            sFumoSala.ligar();
            termoSala.ligar();
            termoSala.setTemperaturaAlvo(21.0);

            GestorAutomacoes gestorCasa1 = new GestorAutomacoes();

            Condicao condLuz = FabricaCondicoes.sensorAbaixoDe(sensorLuzSala, 100.0);
            Automacao autoLuz = new Automacao("Aut1", "Luz Fraca na Sala",
                    "Liga lampadas quando luminosidade < 100 lux", condLuz);
            autoLuz.adicionarAcao(FabricaAcoes.ligar(lampSala1));
            autoLuz.adicionarAcao(FabricaAcoes.definirIntensidade(lampSala1, 70));
            gestorCasa1.adicionarAutomacao(autoLuz);

            Condicao condChuva = FabricaCondicoes.sensorAcimaDe(sensorChuva, 5.0);
            Automacao autoChuva = new Automacao("Aut2", "Chuva Detetada",
                    "Fecha cortinas quando chove", condChuva);
            autoChuva.adicionarAcao(FabricaAcoes.definirAbertura(cortinaSala, 0));
            autoChuva.adicionarAcao(FabricaAcoes.definirAbertura(cortinaQuarto, 0));
            gestorCasa1.adicionarAutomacao(autoChuva);

            // --- Escalonamentos Casa 1 ---
            Escalonamento escalAcordar = new Escalonamento("Esc1", "Acordar", LocalTime.of(7, 30));
            escalAcordar.adicionarAcao(FabricaAcoes.definirAbertura(cortinaSala, 100));
            escalAcordar.adicionarAcao(FabricaAcoes.definirAbertura(cortinaQuarto, 100));
            escalAcordar.adicionarAcao(FabricaAcoes.definirIntensidade(lampQuarto, 30));
            gestorCasa1.adicionarEscalonamento(escalAcordar);

            Escalonamento escalDeitar = new Escalonamento("Esc2", "Hora de Dormir", LocalTime.of(23, 0));
            escalDeitar.adicionarAcao(FabricaAcoes.desligar(lampSala1));
            escalDeitar.adicionarAcao(FabricaAcoes.desligar(lampSala2));
            escalDeitar.adicionarAcao(FabricaAcoes.desligar(lampCozinha));
            escalDeitar.adicionarAcao(FabricaAcoes.desligar(colunaSala));
            gestorCasa1.adicionarEscalonamento(escalDeitar);

            Escalonamento escalCafe = new Escalonamento("Esc3", "Cafe Matinal",
                    LocalTime.of(7, 0),
                    Set.of(DayOfWeek.MONDAY, DayOfWeek.TUESDAY, DayOfWeek.WEDNESDAY,
                            DayOfWeek.THURSDAY, DayOfWeek.FRIDAY));
            escalCafe.adicionarAcao(FabricaAcoes.ligar(tomadaCafe));
            gestorCasa1.adicionarEscalonamento(escalCafe);

            Escalonamento escalLuzesSala = new Escalonamento("Esc4", "Luzes Sala Tarde",
                    LocalTime.of(18, 0), LocalTime.of(22, 0));
            escalLuzesSala.adicionarAcao(FabricaAcoes.ligar(lampSala1));
            escalLuzesSala.adicionarAcao(FabricaAcoes.definirIntensidade(lampSala1, 80));
            gestorCasa1.adicionarEscalonamento(escalLuzesSala);

            // --- Cenarios Casa 1 ---
            Cenario foraDeCasa = new Cenario("Cen1", "Fora de Casa", "Desliga tudo e fecha cortinas");
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

            Cenario jantarAmigos = new Cenario("Cen2", "Jantar com Amigos", "Luz ambiente e musica");
            jantarAmigos.adicionarAcao(FabricaAcoes.definirIntensidade(lampSala1, 40));
            jantarAmigos.adicionarAcao(FabricaAcoes.definirIntensidade(lampSala2, 30));
            jantarAmigos.adicionarAcao(FabricaAcoes.ligar(colunaSala));
            jantarAmigos.adicionarAcao(FabricaAcoes.definirVolume(colunaSala, 35));
            jantarAmigos.adicionarAcao(FabricaAcoes.definirAbertura(cortinaSala, 50));
            gestorCasa1.adicionarCenario(jantarAmigos);

            Cenario deitar = new Cenario("Cen3", "Deitar", "Desliga tudo, luz suave no quarto");
            deitar.adicionarAcao(FabricaAcoes.desligar(lampSala1));
            deitar.adicionarAcao(FabricaAcoes.desligar(lampSala2));
            deitar.adicionarAcao(FabricaAcoes.desligar(lampCozinha));
            deitar.adicionarAcao(FabricaAcoes.desligar(colunaSala));
            deitar.adicionarAcao(FabricaAcoes.definirIntensidade(lampQuarto, 10));
            deitar.adicionarAcao(FabricaAcoes.definirAbertura(cortinaQuarto, 0));
            gestorCasa1.adicionarCenario(deitar);

            Cenario acordar = new Cenario("Cen4", "Acordar", "Abre cortinas e luz suave");
            acordar.adicionarAcao(FabricaAcoes.definirAbertura(cortinaQuarto, 100));
            acordar.adicionarAcao(FabricaAcoes.definirAbertura(cortinaSala, 70));
            acordar.adicionarAcao(FabricaAcoes.definirIntensidade(lampQuarto, 50));
            acordar.adicionarAcao(FabricaAcoes.ligar(tomadaCafe));
            gestorCasa1.adicionarCenario(acordar);

            dc.setGestorAutomacoes(casa1, gestorCasa1);

            // ================================================================
            // CASA 2 — "Apartamento do Bruno" (Lisboa)
            // ================================================================
            Casa casa2 = dc.criarCasa("Casa2", "Apartamento do Bruno", "Av. da Liberdade, 45, Lisboa", bruno);
            ana.adicionarCasa(casa2, TipoUtilizador.UTILIZADOR);

            Divisao salaJantarCasa2 = dc.criarDivisao("Div10", "Sala de Jantar",    casa2, bruno);
            Divisao salaEstarCasa2  = dc.criarDivisao("Div11", "Sala de Estar",     casa2, bruno);
            Divisao cozinhaCasa2    = dc.criarDivisao("Div12", "Cozinha",            casa2, bruno);
            Divisao quartoMCasa2    = dc.criarDivisao("Div13", "Quarto Master",      casa2, bruno);
            Divisao quartoHCasa2    = dc.criarDivisao("Div14", "Quarto de Hospedes", casa2, bruno);
            Divisao varanda         = dc.criarDivisao("Div15", "Varanda",            casa2, bruno);

            Aspirador aspirador    = new Aspirador("Asp1", "iRobot", "Roomba j7+", 25.0);
            Televisao tvSalaE          = new Televisao("TV2", "LG", "OLED 65", 130.0);
            Estores estoresSala        = new Estores("EST1", "Somfy", "Oximo", 20.0, true);
            SensorTemperatura sTempCasa2 = new SensorTemperatura("ST2", "Eve", "Room", 0.4);
            Lampada lampSalaJ      = new Lampada("L10", "IKEA", "TRADFRI E27",    8.5);
            Lampada lampSalaE      = new Lampada("L11", "Philips", "Hue Play",    7.0, true);
            Lampada lampSalaE2     = new Lampada("L12", "Philips", "Hue Play",    7.0, true);
            ColunaDeSom colunaTV   = new ColunaDeSom("CS10", "Sonos", "Arc",       15.0);
            Cortina cortinaSalaJ   = new Cortina("CT10", "Ikea", "FYRTUR",        12.0);
            Cortina cortinaSalaE   = new Cortina("CT11", "Ikea", "FYRTUR",        12.0);

            Lampada lampCozinha2   = new Lampada("L13", "IKEA", "TRADFRI GU10",   5.0);
            Rele tomadaForno       = new Rele("R10", "Shelly", "Shelly 1PM",       5.0);
            Rele tomadaLava        = new Rele("R11", "Shelly", "Shelly 1PM",       5.0);

            Lampada lampQMaster    = new Lampada("L14", "Philips", "Hue White",   8.0, true);
            Cortina cortinaQM      = new Cortina("CT12", "Somfy", "Glydea",       15.0);
            ColunaDeSom colunaQM   = new ColunaDeSom("CS11", "Marshall", "Stanmore", 6.0);

            Lampada lampQHosp      = new Lampada("L15", "IKEA", "TRADFRI E14",    4.0);
            Cortina cortinaQH      = new Cortina("CT13", "Ikea", "KADRILJ",       10.0);

            Lampada lampVaranda    = new Lampada("L16", "Osram", "Outdoor Spot",  8.0);
            Sensor sensorTemp      = new Sensor("ST10", "Netatmo", "NWS01",   0.5, "Temperatura", "C");

            for (Dispositivo d : new Dispositivo[]{
                    aspirador, tvSalaE, estoresSala, sTempCasa2,
                    lampSalaJ, lampSalaE, lampSalaE2, colunaTV, cortinaSalaJ, cortinaSalaE,
                    lampCozinha2, tomadaForno, tomadaLava,
                    lampQMaster, cortinaQM, colunaQM,
                    lampQHosp, cortinaQH,
                    lampVaranda, sensorTemp
            }) dc.registarDispositivo(d);

            salaJantarCasa2.adicionarDispositivo(lampSalaJ);
            salaJantarCasa2.adicionarDispositivo(cortinaSalaJ);

            salaEstarCasa2.adicionarDispositivo(tvSalaE);
            salaEstarCasa2.adicionarDispositivo(estoresSala);
            salaEstarCasa2.adicionarDispositivo(sTempCasa2);
            salaEstarCasa2.adicionarDispositivo(lampSalaE);
            salaEstarCasa2.adicionarDispositivo(lampSalaE2);
            salaEstarCasa2.adicionarDispositivo(colunaTV);
            salaEstarCasa2.adicionarDispositivo(cortinaSalaE);
            cozinhaCasa2.adicionarDispositivo(aspirador);

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
            sTempCasa2.ligar();
            sTempCasa2.setValor(22.0);
            sTempCasa2.setHumidade(60.0);
            Condicao condCalor = FabricaCondicoes.sensorAcimaDe(sensorTemp, 30.0);
            Automacao autoCalor = new Automacao("Aut10", "Calor na Varanda",
                    "Fecha cortinas quando temperatura > 30C", condCalor);
            autoCalor.adicionarAcao(FabricaAcoes.definirAbertura(cortinaSalaE, 20));
            autoCalor.adicionarAcao(FabricaAcoes.definirAbertura(cortinaSalaJ, 20));
            gestorCasa2.adicionarAutomacao(autoCalor);

            Condicao condFrio = FabricaCondicoes.sensorAbaixoDe(sensorTemp, 15.0);
            Automacao autoFrio = new Automacao("Aut11", "Frio Detectado",
                    "Liga luzes aconchegantes quando faz frio", condFrio);
            autoFrio.adicionarAcao(FabricaAcoes.definirIntensidade(lampSalaE, 60));
            autoFrio.adicionarAcao(FabricaAcoes.definirIntensidade(lampSalaE2, 60));
            gestorCasa2.adicionarAutomacao(autoFrio);

            Escalonamento escalFimDeSemana = new Escalonamento("Esc10", "Acordar Fim de Semana",
                    LocalTime.of(9, 0),
                    Set.of(DayOfWeek.SATURDAY, DayOfWeek.SUNDAY));
            escalFimDeSemana.adicionarAcao(FabricaAcoes.definirAbertura(cortinaQM, 50));
            escalFimDeSemana.adicionarAcao(FabricaAcoes.definirIntensidade(lampQMaster, 20));
            gestorCasa2.adicionarEscalonamento(escalFimDeSemana);

            Escalonamento escalNoite = new Escalonamento("Esc11", "Luzes Noturnas",
                    LocalTime.of(22, 0), LocalTime.of(7, 0));
            escalNoite.adicionarAcao(FabricaAcoes.ligar(lampVaranda));
            gestorCasa2.adicionarEscalonamento(escalNoite);

            Escalonamento escalTrabalho = new Escalonamento("Esc12", "Saida para Trabalho",
                    LocalTime.of(8, 30),
                    Set.of(DayOfWeek.MONDAY, DayOfWeek.TUESDAY, DayOfWeek.WEDNESDAY,
                            DayOfWeek.THURSDAY, DayOfWeek.FRIDAY));
            escalTrabalho.adicionarAcao(FabricaAcoes.desligar(lampSalaE));
            escalTrabalho.adicionarAcao(FabricaAcoes.desligar(lampSalaE2));
            escalTrabalho.adicionarAcao(FabricaAcoes.desligar(lampCozinha2));
            escalTrabalho.adicionarAcao(FabricaAcoes.definirAbertura(cortinaSalaE, 0));
            gestorCasa2.adicionarEscalonamento(escalTrabalho);

            Escalonamento escalRegresso = new Escalonamento("Esc13", "Regresso a Casa",
                    LocalTime.of(19, 0),
                    Set.of(DayOfWeek.MONDAY, DayOfWeek.TUESDAY, DayOfWeek.WEDNESDAY,
                            DayOfWeek.THURSDAY, DayOfWeek.FRIDAY));
            escalRegresso.adicionarAcao(FabricaAcoes.ligar(lampSalaE));
            escalRegresso.adicionarAcao(FabricaAcoes.definirIntensidade(lampSalaE, 70));
            escalRegresso.adicionarAcao(FabricaAcoes.definirAbertura(cortinaSalaE, 80));
            gestorCasa2.adicionarEscalonamento(escalRegresso);

            Cenario verCinema = new Cenario("Cen10", "Ver Cinema",
                    "Luzes baixas, volume alto, cortinas fechadas");
            verCinema.adicionarAcao(FabricaAcoes.definirIntensidade(lampSalaE, 15));
            verCinema.adicionarAcao(FabricaAcoes.definirIntensidade(lampSalaE2, 10));
            verCinema.adicionarAcao(FabricaAcoes.ligar(colunaTV));
            verCinema.adicionarAcao(FabricaAcoes.definirVolume(colunaTV, 60));
            verCinema.adicionarAcao(FabricaAcoes.definirAbertura(cortinaSalaE, 0));
            gestorCasa2.adicionarCenario(verCinema);

            Cenario foraCasa2 = new Cenario("Cen11", "Fora de Casa",
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

            Cenario deitarCasa2 = new Cenario("Cen12", "Deitar",
                    "Desliga sala e cozinha, luz minima no quarto");
            deitarCasa2.adicionarAcao(FabricaAcoes.desligar(lampSalaE));
            deitarCasa2.adicionarAcao(FabricaAcoes.desligar(lampSalaE2));
            deitarCasa2.adicionarAcao(FabricaAcoes.desligar(lampSalaJ));
            deitarCasa2.adicionarAcao(FabricaAcoes.desligar(colunaTV));
            deitarCasa2.adicionarAcao(FabricaAcoes.definirIntensidade(lampQMaster, 5));
            deitarCasa2.adicionarAcao(FabricaAcoes.definirAbertura(cortinaQM, 0));
            gestorCasa2.adicionarCenario(deitarCasa2);

            Cenario acordarCasa2 = new Cenario("Cen13", "Acordar",
                    "Abre cortinas e liga cafe");
            acordarCasa2.adicionarAcao(FabricaAcoes.definirAbertura(cortinaQM, 100));
            acordarCasa2.adicionarAcao(FabricaAcoes.definirIntensidade(lampQMaster, 40));
            acordarCasa2.adicionarAcao(FabricaAcoes.ligar(tomadaForno));
            gestorCasa2.adicionarCenario(acordarCasa2);

            dc.setGestorAutomacoes(casa2, gestorCasa2);

            // ================================================================
            // CASA 3 — "Moradia da Carla" (Braga)
            // ================================================================
            Casa casa3 = dc.criarCasa("Casa3", "Moradia da Carla", "Rua do Souto, 22, Braga", carla);
            david.adicionarCasa(casa3, TipoUtilizador.UTILIZADOR);
            ana.adicionarCasa(casa3, TipoUtilizador.UTILIZADOR);

            Divisao salaCasa3      = dc.criarDivisao("Div20", "Sala",               casa3, carla);
            Divisao cozinhaCasa3   = dc.criarDivisao("Div21", "Cozinha",             casa3, carla);
            Divisao quartoP        = dc.criarDivisao("Div22", "Quarto Principal",    casa3, carla);
            Divisao quarto2        = dc.criarDivisao("Div23", "Quarto 2",            casa3, carla);
            Divisao quarto3        = dc.criarDivisao("Div24", "Quarto 3",            casa3, carla);
            Divisao jardim         = dc.criarDivisao("Div25", "Jardim",              casa3, carla);
            Divisao garagemCasa3   = dc.criarDivisao("Div26", "Garagem",             casa3, carla);

            SensorMovimento sMovCasa3 = new SensorMovimento("SM2", "Aqara", "FP1", 0.3);
            Termostato termoCasa3     = new Termostato("Term2", "Tado", "Smart Thermostat", 3.0);
            Televisao tvCasa3         = new Televisao("TV3", "Sony", "Bravia 55", 110.0);
            SensorFumo sFumoCasa3     = new SensorFumo("SF2", "Nest", "Protect", 0.5);
            Lampada lampSala3      = new Lampada("L20", "Philips", "Hue Filament", 7.0);
            Lampada lampSala3b     = new Lampada("L21", "Philips", "Hue Filament", 7.0);
            ColunaDeSom colunaSala3 = new ColunaDeSom("CS20", "Sonos", "Five",      15.0);
            Cortina cortinaSala3   = new Cortina("CT20", "Coulisse", "Neo",        18.0);
            Sensor sensorLuzSala3  = new Sensor("SL20", "Fibaro", "FGMS-001", 0.3, "Luminosidade", "lux");
            Sensor sensorMovSala   = new Sensor("SM21", "Fibaro", "FGMS-001", 0.3, "Movimento", "bool");

            Lampada lampCozinha3   = new Lampada("L22", "IKEA", "TRADFRI",        6.0);
            Rele tomadaCafe3       = new Rele("R20", "TP-Link", "Tapo P115",      2.0);
            Rele tomadaDishwasher  = new Rele("R21", "Shelly", "Shelly 2.5",      5.0);

            Lampada lampQP         = new Lampada("L23", "Philips", "Hue Color",   9.0, true);
            Cortina cortinaQP      = new Cortina("CT21", "Somfy", "Glydea",       15.0);
            ColunaDeSom colunaQP   = new ColunaDeSom("CS21", "Bose", "SoundLink",  5.0);

            Lampada lampQ2         = new Lampada("L24", "IKEA", "TRADFRI",        5.0);
            Cortina cortinaQ2      = new Cortina("CT22", "Ikea", "FYRTUR",        12.0);

            Lampada lampQ3         = new Lampada("L25", "IKEA", "TRADFRI",        5.0);
            Cortina cortinaQ3      = new Cortina("CT23", "Ikea", "FYRTUR",        12.0);

            Lampada lampJardim     = new Lampada("L26", "Philips", "Hue Outdoor", 10.0);
            Lampada lampJardim2    = new Lampada("L27", "Philips", "Hue Outdoor", 10.0);
            Sensor sensorMovJardim = new Sensor("SM22", "Hue", "PIR Outdoor", 0.4, "Movimento", "bool");

            PortaoGaragem portaoCasa3 = new PortaoGaragem("PG2", "Came", "GARD4",  45.0);
            Sensor sensorMovGar3   = new Sensor("SM23", "Xiaomi", "Mi Sensor", 0.2, "Movimento", "bool");

            for (Dispositivo d : new Dispositivo[]{
                    sMovCasa3, termoCasa3, tvCasa3, sFumoCasa3,
                    lampSala3, lampSala3b, colunaSala3, cortinaSala3, sensorLuzSala3, sensorMovSala,
                    lampCozinha3, tomadaCafe3, tomadaDishwasher,
                    lampQP, cortinaQP, colunaQP,
                    lampQ2, cortinaQ2,
                    lampQ3, cortinaQ3,
                    lampJardim, lampJardim2, sensorMovJardim,
                    portaoCasa3, sensorMovGar3
            }) dc.registarDispositivo(d);

            salaCasa3.adicionarDispositivo(sMovCasa3);
            salaCasa3.adicionarDispositivo(termoCasa3);
            salaCasa3.adicionarDispositivo(tvCasa3);
            salaCasa3.adicionarDispositivo(sFumoCasa3);
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
            sMovCasa3.ligar();
            termoCasa3.ligar();
            termoCasa3.setTemperaturaAlvo(20.0);
            sFumoCasa3.ligar();

            Condicao condLuzBaixa3 = FabricaCondicoes.sensorAbaixoDe(sensorLuzSala3, 80.0);
            Automacao autoLuz3 = new Automacao("Aut20", "Luz Baixa na Sala",
                    "Liga luzes da sala quando escurece", condLuzBaixa3);
            autoLuz3.adicionarAcao(FabricaAcoes.ligar(lampSala3));
            autoLuz3.adicionarAcao(FabricaAcoes.ligar(lampSala3b));
            gestorCasa3.adicionarAutomacao(autoLuz3);

            Condicao condMovJardim = FabricaCondicoes.sensorAcimaDe(sensorMovJardim, 0.5);
            Automacao autoMovJardim = new Automacao("Aut21", "Movimento no Jardim",
                    "Liga luzes do jardim ao detetar movimento", condMovJardim);
            autoMovJardim.adicionarAcao(FabricaAcoes.ligar(lampJardim));
            autoMovJardim.adicionarAcao(FabricaAcoes.ligar(lampJardim2));
            gestorCasa3.adicionarAutomacao(autoMovJardim);

            Escalonamento escalManha3 = new Escalonamento("Esc20", "Manha", LocalTime.of(7, 0));
            escalManha3.adicionarAcao(FabricaAcoes.definirAbertura(cortinaQP, 100));
            escalManha3.adicionarAcao(FabricaAcoes.definirAbertura(cortinaQ2, 100));
            escalManha3.adicionarAcao(FabricaAcoes.definirAbertura(cortinaQ3, 100));
            escalManha3.adicionarAcao(FabricaAcoes.ligar(tomadaCafe3));
            gestorCasa3.adicionarEscalonamento(escalManha3);

            Escalonamento escalNoite3 = new Escalonamento("Esc21", "Luzes Jardim Noite",
                    LocalTime.of(20, 0), LocalTime.of(23, 30));
            escalNoite3.adicionarAcao(FabricaAcoes.ligar(lampJardim));
            escalNoite3.adicionarAcao(FabricaAcoes.ligar(lampJardim2));
            gestorCasa3.adicionarEscalonamento(escalNoite3);

            Escalonamento escalDormir3 = new Escalonamento("Esc22", "Dormir", LocalTime.of(23, 0));
            escalDormir3.adicionarAcao(FabricaAcoes.desligar(lampSala3));
            escalDormir3.adicionarAcao(FabricaAcoes.desligar(lampSala3b));
            escalDormir3.adicionarAcao(FabricaAcoes.desligar(colunaSala3));
            escalDormir3.adicionarAcao(FabricaAcoes.desligar(lampJardim));
            escalDormir3.adicionarAcao(FabricaAcoes.desligar(lampJardim2));
            gestorCasa3.adicionarEscalonamento(escalDormir3);

            Escalonamento escalFDS3 = new Escalonamento("Esc23", "Fim de Semana Manha",
                    LocalTime.of(9, 30),
                    Set.of(DayOfWeek.SATURDAY, DayOfWeek.SUNDAY));
            escalFDS3.adicionarAcao(FabricaAcoes.definirAbertura(cortinaSala3, 100));
            escalFDS3.adicionarAcao(FabricaAcoes.definirIntensidade(lampSala3, 50));
            gestorCasa3.adicionarEscalonamento(escalFDS3);

            Cenario foraCasa3 = new Cenario("Cen20", "Fora de Casa", "Desliga tudo e fecha tudo");
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

            Cenario jantarCasa3 = new Cenario("Cen21", "Jantar com Amigos", "Ambiente acolhedor");
            jantarCasa3.adicionarAcao(FabricaAcoes.definirIntensidade(lampSala3, 35));
            jantarCasa3.adicionarAcao(FabricaAcoes.definirIntensidade(lampSala3b, 35));
            jantarCasa3.adicionarAcao(FabricaAcoes.ligar(colunaSala3));
            jantarCasa3.adicionarAcao(FabricaAcoes.definirVolume(colunaSala3, 30));
            jantarCasa3.adicionarAcao(FabricaAcoes.ligar(lampJardim));
            gestorCasa3.adicionarCenario(jantarCasa3);

            Cenario deitarCasa3 = new Cenario("Cen22", "Deitar", "Fecha cortinas e desliga");
            deitarCasa3.adicionarAcao(FabricaAcoes.desligar(lampSala3));
            deitarCasa3.adicionarAcao(FabricaAcoes.desligar(lampSala3b));
            deitarCasa3.adicionarAcao(FabricaAcoes.desligar(colunaSala3));
            deitarCasa3.adicionarAcao(FabricaAcoes.definirIntensidade(lampQP, 5));
            deitarCasa3.adicionarAcao(FabricaAcoes.definirAbertura(cortinaQP, 0));
            deitarCasa3.adicionarAcao(FabricaAcoes.desligar(lampJardim));
            deitarCasa3.adicionarAcao(FabricaAcoes.desligar(lampJardim2));
            gestorCasa3.adicionarCenario(deitarCasa3);

            Cenario acordarCasa3 = new Cenario("Cen23", "Acordar", "Abre tudo e liga cafe");
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
