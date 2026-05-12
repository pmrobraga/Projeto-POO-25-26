package domuscontrol.ui;


import domuscontrol.DomusControl;
import domuscontrol.casa.Casa;
import domuscontrol.dispositivos.*;
import domuscontrol.utilizadores.Utilizador;

/**
 * Menu de gestao e operacao de dispositivos.
 */
public class MenuDispositivos {

    private final DomusControl dc;
    private final Sessao sessao;

    public MenuDispositivos(DomusControl dc, Sessao sessao) {
        this.dc = dc;
        this.sessao = sessao;
    }

    public void run() {
        NewMenu menu = new NewMenu("Dispositivos", new String[]{
                "Listar dispositivos de uma casa",
                "Operar dispositivo",
                "Criar dispositivo",
                "Associar dispositivo a divisao",
                "Ver estado detalhado de um dispositivo"
        });

        menu.setHandler(1, this::listar);
        menu.setHandler(2, this::operar);
        menu.setHandler(3, this::criar);
        menu.setHandler(4, this::associar);
        menu.setHandler(5, this::verEstado);

        menu.setPreCondition(2, () -> !dc.getTodasCasas().isEmpty());
        menu.setPreCondition(4, () -> sessao.isAdminEmAlgumaCasa());
        menu.setPreCondition(5, () -> !dc.getTodasCasas().isEmpty());

        menu.run();
    }

    private void listar() {
        try {
            System.out.println();
            System.out.println("  Casas disponiveis:");
            dc.getTodasCasas().forEach(c -> System.out.println("    [" + c.getId() + "] " + c.getNome()));
            String cid = Leitura.lerString("  ID da casa: ");
            Casa c = dc.getCasa(cid);
            System.out.println("\n  Dispositivos em " + c.getNome() + ":");
            System.out.println("  ----------------------------------");
            for (var div : c.getDivisoes()) {
                System.out.println("  [" + div.getNome() + "]");
                if (div.getDispositivos().isEmpty()) {
                    System.out.println("    (sem dispositivos)");
                } else {
                    div.getDispositivos().forEach(d ->
                            System.out.printf("    [%s] %s%n", d.getId(), d.getEstadoDetalhado()));
                }
            }
        } catch (Exception e) { System.out.println("  [ERRO] " + e.getMessage()); }
    }

    private void operar() {
        try {
            System.out.println();
            dc.getTodasCasas().forEach(c -> System.out.println("  [" + c.getId() + "] " + c.getNome()));
            String cid = Leitura.lerString("  ID da casa: ");
            String did = Leitura.lerString("  ID do dispositivo: ");
            Dispositivo d = dc.getCasa(cid).getDispositivoPorId(did);
            System.out.println("\n  Estado atual: " + d.getEstadoDetalhado());
            System.out.println("  ----------------------------------");
            operarDispositivo(d);
        } catch (Exception e) { System.out.println("  [ERRO] " + e.getMessage()); }
    }

    private void operarDispositivo(Dispositivo d) {
        if (d instanceof Televisao) {
            NewMenu m = new NewMenu("Operar TV " + d.getModelo(), new String[]{
                    "Ligar", "Desligar", "Mudar canal", "Definir volume", "Mudar fonte"
            });
            m.setHandler(1, () -> { d.ligar(); System.out.println("  [OK] Ligada."); });
            m.setHandler(2, () -> { d.desligar(); System.out.println("  [OK] Desligada."); });
            m.setHandler(3, () -> {
                int c = Leitura.lerInt("  Canal: ");
                try { ((Televisao) d).setCanal(c); System.out.println("  [OK] Canal: " + c); }
                catch (Exception e) { System.out.println("  [ERRO] " + e.getMessage()); }
            });
            m.setHandler(4, () -> {
                int v = Leitura.lerInt("  Volume (0-100): ");
                try { ((Televisao) d).setVolume(v); System.out.println("  [OK] Volume: " + v); }
                catch (Exception e) { System.out.println("  [ERRO] " + e.getMessage()); }
            });
            m.setHandler(5, () -> {
                System.out.println("  Fontes: HDMI1, HDMI2, HDMI3, TV, STREAMING");
                String f = Leitura.lerString("  Fonte: ");
                try { ((Televisao) d).setFonte(Televisao.Fonte.valueOf(f.toUpperCase()));
                    System.out.println("  [OK] Fonte: " + f); }
                catch (Exception e) { System.out.println("  [ERRO] Fonte invalida."); }
            });
            m.run();

        } else if (d instanceof Termostato) {
            NewMenu m = new NewMenu("Operar Termostato " + d.getModelo(), new String[]{
                    "Ligar", "Desligar", "Definir temperatura alvo", "Definir modo"
            });
            m.setHandler(1, () -> { d.ligar(); System.out.println("  [OK] Ligado."); });
            m.setHandler(2, () -> { d.desligar(); System.out.println("  [OK] Desligado."); });
            m.setHandler(3, () -> {
                double t = Leitura.lerDouble("  Temperatura alvo (5-35°C): ");
                try { ((Termostato) d).setTemperaturaAlvo(t); System.out.println("  [OK] Temp alvo: " + t + "°C"); }
                catch (Exception e) { System.out.println("  [ERRO] " + e.getMessage()); }
            });
            m.setHandler(4, () -> {
                System.out.println("  Modos: AQUECIMENTO, ARREFECIMENTO, VENTILACAO, DESLIGADO");
                String modo = Leitura.lerString("  Modo: ");
                try { ((Termostato) d).setModo(Termostato.Modo.valueOf(modo.toUpperCase()));
                    System.out.println("  [OK] Modo: " + modo); }
                catch (Exception e) { System.out.println("  [ERRO] Modo invalido."); }
            });
            m.run();

        } else if (d instanceof Aspirador) {
            NewMenu m = new NewMenu("Operar Aspirador " + d.getModelo(), new String[]{
                    "Ligar (modo AUTO)", "Desligar / Voltar a base", "Definir modo"
            });
            m.setHandler(1, () -> { d.ligar(); System.out.println("  [OK] A aspirar."); });
            m.setHandler(2, () -> { ((Aspirador) d).voltarABase(); System.out.println("  [OK] A voltar a base."); });
            m.setHandler(3, () -> {
                System.out.println("  Modos: AUTO, MANUAL, TURBO, SILENCIOSO, DOCK");
                String modo = Leitura.lerString("  Modo: ");
                try { ((Aspirador) d).setModo(Aspirador.Modo.valueOf(modo.toUpperCase()));
                    System.out.println("  [OK] Modo: " + modo); }
                catch (Exception e) { System.out.println("  [ERRO] Modo invalido."); }
            });
            m.run();

        } else if (d instanceof Estores) {
            NewMenu m = new NewMenu("Operar Estores " + d.getModelo(), new String[]{
                    "Abrir totalmente", "Fechar totalmente", "Definir abertura (%)",
                    "Ativar/desativar protecao UV"
            });
            m.setHandler(1, () -> { ((Estores) d).abrir(); System.out.println("  [OK] Abertos."); });
            m.setHandler(2, () -> { ((Estores) d).fechar(); System.out.println("  [OK] Fechados."); });
            m.setHandler(3, () -> {
                int v = Leitura.lerInt("  Abertura (0-100%): ");
                try { ((Estores) d).setPercentagemAbertura(v); System.out.println("  [OK] Abertura: " + v + "%"); }
                catch (Exception e) { System.out.println("  [ERRO] " + e.getMessage()); }
            });
            m.setHandler(4, () -> {
                boolean uv = !((Estores) d).isProtecaoUVActiva();
                ((Estores) d).setProtecaoUV(uv);
                System.out.println("  [OK] Protecao UV: " + (uv ? "ATIVA" : "INATIVA"));
            });
            m.run();

        } else if (d instanceof SensorMovimento) {
            NewMenu m = new NewMenu("Operar Sensor Movimento " + d.getModelo(), new String[]{
                    "Ligar sensor", "Desligar sensor",
                    "Simular movimento", "Limpar movimento", "Definir sensibilidade"
            });
            m.setHandler(1, () -> { d.ligar(); System.out.println("  [OK] Sensor ativo."); });
            m.setHandler(2, () -> { d.desligar(); System.out.println("  [OK] Sensor inativo."); });
            m.setHandler(3, () -> { ((SensorMovimento) d).detetarMovimento(); System.out.println("  [OK] Movimento detetado!"); });
            m.setHandler(4, () -> { ((SensorMovimento) d).limparMovimento(); System.out.println("  [OK] Sem movimento."); });
            m.setHandler(5, () -> {
                int s = Leitura.lerInt("  Sensibilidade (1-5): ");
                try { ((SensorMovimento) d).setSensibilidade(s); System.out.println("  [OK] Sensibilidade: " + s); }
                catch (Exception e) { System.out.println("  [ERRO] " + e.getMessage()); }
            });
            m.run();

        } else if (d instanceof SensorFumo) {
            NewMenu m = new NewMenu("Operar Sensor Fumo " + d.getModelo(), new String[]{
                    "Ligar sensor", "Desligar sensor",
                    "Simular concentracao de fumo", "Silenciar alarme"
            });
            m.setHandler(1, () -> { d.ligar(); System.out.println("  [OK] Sensor ativo."); });
            m.setHandler(2, () -> { d.desligar(); System.out.println("  [OK] Sensor inativo."); });
            m.setHandler(3, () -> {
                double v = Leitura.lerDouble("  Concentracao de fumo (%): ");
                ((SensorFumo) d).setValor(v);
                System.out.println("  [OK] Concentracao: " + v + "%");
            });
            m.setHandler(4, () -> { ((SensorFumo) d).silenciarAlarme(); System.out.println("  [OK] Alarme silenciado."); });
            m.run();

        } else if (d instanceof SensorTemperatura) {
            NewMenu m = new NewMenu("Operar Sensor Temperatura " + d.getModelo(), new String[]{
                    "Ligar sensor", "Desligar sensor",
                    "Definir temperatura", "Definir humidade"
            });
            m.setHandler(1, () -> { d.ligar(); System.out.println("  [OK] Sensor ativo."); });
            m.setHandler(2, () -> { d.desligar(); System.out.println("  [OK] Sensor inativo."); });
            m.setHandler(3, () -> {
                double t = Leitura.lerDouble("  Temperatura (°C): ");
                ((SensorTemperatura) d).setValor(t);
                System.out.println("  [OK] Temperatura: " + t + "°C");
            });
            m.setHandler(4, () -> {
                double h = Leitura.lerDouble("  Humidade (%): ");
                try { ((SensorTemperatura) d).setHumidade(h); System.out.println("  [OK] Humidade: " + h + "%"); }
                catch (Exception e) { System.out.println("  [ERRO] " + e.getMessage()); }
            });
            m.run();

        } else if (d instanceof Lampada && ((Lampada) d).isTemCor()) {
            NewMenu m = new NewMenu("Operar Lampada " + d.getModelo(), new String[]{
                    "Ligar", "Desligar", "Definir intensidade", "Definir temperatura de cor"
            });
            m.setHandler(1, () -> { d.ligar(); System.out.println("  [OK] Ligada."); });
            m.setHandler(2, () -> { d.desligar(); System.out.println("  [OK] Desligada."); });
            m.setHandler(3, () -> {
                int v = Leitura.lerInt("  Intensidade (0-100%): ");
                try { ((Lampada) d).setIntensidade(v); System.out.println("  [OK] Intensidade: " + v + "%"); }
                catch (Exception e) { System.out.println("  [ERRO] " + e.getMessage()); }
            });
            m.setHandler(4, () -> {
                int k = Leitura.lerInt("  Temperatura (2700-4000 K): ");
                try { ((Lampada) d).setTemperatura(k); System.out.println("  [OK] Temperatura: " + k + "K"); }
                catch (Exception e) { System.out.println("  [ERRO] " + e.getMessage()); }
            });
            m.run();

        } else if (d instanceof Lampada) {
            NewMenu m = new NewMenu("Operar Lampada " + d.getModelo(), new String[]{
                    "Ligar", "Desligar", "Definir intensidade"
            });
            m.setHandler(1, () -> { d.ligar(); System.out.println("  [OK] Ligada."); });
            m.setHandler(2, () -> { d.desligar(); System.out.println("  [OK] Desligada."); });
            m.setHandler(3, () -> {
                int v = Leitura.lerInt("  Intensidade (0-100%): ");
                try { ((Lampada) d).setIntensidade(v); System.out.println("  [OK] Intensidade: " + v + "%"); }
                catch (Exception e) { System.out.println("  [ERRO] " + e.getMessage()); }
            });
            m.run();

        } else if (d instanceof ColunaDeSom) {
            NewMenu m = new NewMenu("Operar Coluna " + d.getModelo(), new String[]{
                    "Ligar", "Desligar", "Definir volume"
            });
            m.setHandler(1, () -> { d.ligar(); System.out.println("  [OK] Ligada."); });
            m.setHandler(2, () -> { d.desligar(); System.out.println("  [OK] Desligada."); });
            m.setHandler(3, () -> {
                int v = Leitura.lerInt("  Volume (0-100): ");
                try { ((ColunaDeSom) d).setVolume(v); System.out.println("  [OK] Volume: " + v); }
                catch (Exception e) { System.out.println("  [ERRO] " + e.getMessage()); }
            });
            m.run();

        } else if (d instanceof DispositivoComAbertura) {
            NewMenu m = new NewMenu("Operar " + d.getModelo(), new String[]{
                    "Abrir totalmente", "Fechar totalmente", "Definir abertura (%)"
            });
            m.setHandler(1, () -> { ((DispositivoComAbertura) d).abrir(); System.out.println("  [OK] Aberto."); });
            m.setHandler(2, () -> { ((DispositivoComAbertura) d).fechar(); System.out.println("  [OK] Fechado."); });
            m.setHandler(3, () -> {
                int v = Leitura.lerInt("  Abertura (0-100%): ");
                try { ((DispositivoComAbertura) d).setPercentagemAbertura(v); System.out.println("  [OK] Abertura: " + v + "%"); }
                catch (Exception e) { System.out.println("  [ERRO] " + e.getMessage()); }
            });
            m.run();

        } else {
            NewMenu m = new NewMenu("Operar " + d.getModelo(), new String[]{ "Ligar", "Desligar" });
            m.setHandler(1, () -> { d.ligar(); System.out.println("  [OK] Ligado."); });
            m.setHandler(2, () -> { d.desligar(); System.out.println("  [OK] Desligado."); });
            m.run();
        }
    }

    private void criar() {
        NewMenu tipoMenu = new NewMenu("Tipo de Dispositivo", new String[]{
                "Rele", "Lampada", "Coluna de Som", "Televisao",
                "Termostato", "Aspirador", "Cortina", "Estores",
                "Portao de Garagem", "Sensor", "Sensor Movimento",
                "Sensor Fumo", "Sensor Temperatura"
        });

        final Dispositivo[] novo = {null};

        tipoMenu.setHandler(1,  () -> novo[0] = criarBase(id -> new Rele(id[0], id[1], id[2], id[3] == null ? 0 : Double.parseDouble(id[3]))));
        tipoMenu.setHandler(2,  () -> {
            String[] b = lerBase();
            boolean cor = Leitura.lerSimNao("  Tem controlo de cor?");
            try { novo[0] = new Lampada(b[0], b[1], b[2], Double.parseDouble(b[3]), cor); }
            catch (Exception e) { System.out.println("  [ERRO] " + e.getMessage()); }
        });
        tipoMenu.setHandler(3,  () -> novo[0] = criarBase(id -> new ColunaDeSom(id[0], id[1], id[2], Double.parseDouble(id[3]))));
        tipoMenu.setHandler(4,  () -> novo[0] = criarBase(id -> new Televisao(id[0], id[1], id[2], Double.parseDouble(id[3]))));
        tipoMenu.setHandler(5,  () -> novo[0] = criarBase(id -> new Termostato(id[0], id[1], id[2], Double.parseDouble(id[3]))));
        tipoMenu.setHandler(6,  () -> novo[0] = criarBase(id -> new Aspirador(id[0], id[1], id[2], Double.parseDouble(id[3]))));
        tipoMenu.setHandler(7,  () -> novo[0] = criarBase(id -> new Cortina(id[0], id[1], id[2], Double.parseDouble(id[3]))));
        tipoMenu.setHandler(8,  () -> {
            String[] b = lerBase();
            boolean lamelas = Leitura.lerSimNao("  Tem lamelas?");
            try { novo[0] = new Estores(b[0], b[1], b[2], Double.parseDouble(b[3]), lamelas); }
            catch (Exception e) { System.out.println("  [ERRO] " + e.getMessage()); }
        });
        tipoMenu.setHandler(9,  () -> novo[0] = criarBase(id -> new PortaoGaragem(id[0], id[1], id[2], Double.parseDouble(id[3]))));
        tipoMenu.setHandler(10, () -> {
            String[] b = lerBase();
            String tipo = Leitura.lerString("  Tipo sensor (ex: Luminosidade): ");
            String unidade = Leitura.lerString("  Unidade (ex: lux): ");
            try { novo[0] = new Sensor(b[0], b[1], b[2], Double.parseDouble(b[3]), tipo, unidade); }
            catch (Exception e) { System.out.println("  [ERRO] " + e.getMessage()); }
        });
        tipoMenu.setHandler(11, () -> novo[0] = criarBase(id -> new SensorMovimento(id[0], id[1], id[2], Double.parseDouble(id[3]))));
        tipoMenu.setHandler(12, () -> novo[0] = criarBase(id -> new SensorFumo(id[0], id[1], id[2], Double.parseDouble(id[3]))));
        tipoMenu.setHandler(13, () -> novo[0] = criarBase(id -> new SensorTemperatura(id[0], id[1], id[2], Double.parseDouble(id[3]))));

        tipoMenu.run();

        if (novo[0] != null) {
            try {
                dc.registarDispositivo(novo[0]);
                System.out.println("  [OK] Dispositivo criado: " + novo[0]);
            } catch (Exception e) { System.out.println("  [ERRO] " + e.getMessage()); }
        }
    }

    private String[] lerBase() {
        String id     = Leitura.lerString("  ID: ");
        String marca  = Leitura.lerString("  Marca: ");
        String modelo = Leitura.lerString("  Modelo: ");
        String consumo = String.valueOf(Leitura.lerDouble("  Consumo por hora (Wh): "));
        return new String[]{id, marca, modelo, consumo};
    }

    private Dispositivo criarBase(java.util.function.Function<String[], Dispositivo> factory) {
        try {
            return factory.apply(lerBase());
        } catch (Exception e) {
            System.out.println("  [ERRO] " + e.getMessage());
            return null;
        }
    }

    private void associar() {
        try {
            System.out.println();
            dc.getTodasCasas().forEach(c -> System.out.println("  [" + c.getId() + "] " + c.getNome()));
            String cid  = Leitura.lerString("  ID da casa: ");
            String did  = Leitura.lerString("  ID da divisao: ");
            String disp = Leitura.lerString("  ID do dispositivo: ");
            String uid  = Leitura.lerString("  ID do admin: ");
            Casa c       = dc.getCasa(cid);
            Utilizador u = dc.getUtilizador(uid);
            dc.associarDispositivoADivisao(disp, c, did, u);
            System.out.println("  [OK] Dispositivo associado.");
        } catch (Exception e) { System.out.println("  [ERRO] " + e.getMessage()); }
    }

    private void verEstado() {
        try {
            System.out.println();
            dc.getTodasCasas().forEach(c -> System.out.println("  [" + c.getId() + "] " + c.getNome()));
            String cid = Leitura.lerString("  ID da casa: ");
            String did = Leitura.lerString("  ID do dispositivo: ");
            Dispositivo d = dc.getCasa(cid).getDispositivoPorId(did);
            System.out.println("\n  " + d.getEstadoDetalhado());
            System.out.printf("  Ativacoes: %d | Tempo ligado: %d min | Consumo total: %.2f Wh%n",
                    d.getNumeroAtivacoes(), d.getTempoLigadoMinutos(), d.getConsumoTotal());
        } catch (Exception e) { System.out.println("  [ERRO] " + e.getMessage()); }
    }
}
