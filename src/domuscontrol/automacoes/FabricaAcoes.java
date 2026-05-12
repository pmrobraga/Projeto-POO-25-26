package domuscontrol.automacoes;

import domuscontrol.dispositivos.*;

/**
 * Açao: ligar um dispositivo
 */
class AcaoLigar implements Acao {
    private final Dispositivo dispositivo;

    public AcaoLigar(Dispositivo dispositivo) {
        this.dispositivo = dispositivo;
    }

    @Override
    public void executar() {
        dispositivo.ligar();
    }

    @Override
    public String descrever() {
        return "Ligar " + dispositivo.getId() + " (" + dispositivo.getModelo() + ")";
    }
}

/**
 * Ação: desligar um dispositivo
 */
class AcaoDesligar implements Acao {
    private final Dispositivo dispositivo;

    public AcaoDesligar(Dispositivo dispositivo) {
        this.dispositivo = dispositivo;
    }

    @Override
    public void executar() {
        dispositivo.desligar();
    }

    @Override
    public String descrever() {
        return "Desligar " + dispositivo.getId() + " (" + dispositivo.getModelo() + ")";
    }
}

/**
 * Ação: definir a intensidade de uma lâmpada
 */
class AcaoDefinirIntensidade implements Acao {
    private final Lampada lampada;
    private final int intensidade;

    public AcaoDefinirIntensidade(Lampada lampada, int intensidade) {
        this.lampada = lampada;
        this.intensidade = intensidade;
    }

    @Override
    public void executar() {
        lampada.ligar();
        lampada.setIntensidade(intensidade);
    }

    @Override
    public String descrever() {
        return "Definir intensidade de " + lampada.getId() + " para " + intensidade + "%";
    }
}

/**
 * Ação: definir o volume de uma coluna de som
 */
class AcaoDefinirVolume implements Acao {
    private final ColunaDeSom coluna;
    private final int volume;

    public AcaoDefinirVolume(ColunaDeSom coluna, int volume) {
        this.coluna = coluna;
        this.volume = volume;
    }

    @Override
    public void executar() {
        coluna.ligar();
        coluna.setVolume(volume);
    }

    @Override
    public String descrever() {
        return "Definir volume de " + coluna.getId() + " para " + volume + "%";
    }
}

/**
 * Ação: definir a abertura de cortina ou portão.
 */
class AcaoDefinirAbertura implements Acao {
    private final DispositivoComAbertura dispositivo;
    private final int percentagem;

    public AcaoDefinirAbertura(DispositivoComAbertura dispositivo, int percentagem) {
        this.dispositivo = dispositivo;
        this.percentagem = percentagem;
    }

    @Override
    public void executar() {
        dispositivo.setPercentagemAbertura(percentagem);
    }

    @Override
    public String descrever() {
        return "Definir abertura de " + dispositivo.getId() + " para " + percentagem + "%";
    }
}

/**
 * Fábrica de ações — criação de ações concretas.
 * Evita que o resto do código precise de conhecer as subclasses.
 */
public class FabricaAcoes {

    private FabricaAcoes() {}

    public static Acao ligar(Dispositivo d) {
        return new AcaoLigar(d);
    }

    public static Acao desligar(Dispositivo d) {
        return new AcaoDesligar(d);
    }

    public static Acao definirIntensidade(Lampada l, int intensidade) {
        return new AcaoDefinirIntensidade(l, intensidade);
    }

    public static Acao definirVolume(ColunaDeSom c, int volume) {
        return new AcaoDefinirVolume(c, volume);
    }

    public static Acao definirAbertura(DispositivoComAbertura d, int percentagem) {
        return new AcaoDefinirAbertura(d, percentagem);
    }
}
