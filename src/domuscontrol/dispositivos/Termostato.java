package domuscontrol.dispositivos;

/**
 * Termostato inteligente.
 * Permite definir a temperatura alvo e o modo de funcionamento.
 */
public class Termostato extends Dispositivo {

    private static final long serialVersionUID = 1L;

    public enum Modo { AQUECIMENTO, ARREFECIMENTO, VENTILACAO, DESLIGADO }

    private double temperaturaAlvo;
    private double temperaturaAtual;
    private Modo modo;

    private static final double TEMP_MIN = 5.0;
    private static final double TEMP_MAX = 35.0;

    public Termostato(String id, String marca, String modelo, double consumoPorHora) {
        super(id, marca, modelo, consumoPorHora);
        this.temperaturaAlvo = 20.0;
        this.temperaturaAtual = 18.0;
        this.modo = Modo.DESLIGADO;
    }

    @Override
    public void ligar() {
        super.ligar();
        this.modo = Modo.AQUECIMENTO;
    }

    @Override
    public void desligar() {
        super.desligar();
        this.modo = Modo.DESLIGADO;
    }

    public void setTemperaturaAlvo(double temp) {
        if (temp < TEMP_MIN || temp > TEMP_MAX)
            throw new IllegalArgumentException(
                "Temperatura deve estar entre " + TEMP_MIN + " e " + TEMP_MAX + " graus.");
        this.temperaturaAlvo = temp;
    }

    public void setTemperaturaAtual(double temp) {
        this.temperaturaAtual = temp;
    }

    public void setModo(Modo modo) {
        this.modo = modo;
        if (modo == Modo.DESLIGADO) super.desligar();
        else if (!estaLigado()) super.ligar();
    }

    public double getTemperaturaAlvo()  { return temperaturaAlvo; }
    public double getTemperaturaAtual() { return temperaturaAtual; }
    public Modo getModo()               { return modo; }

    @Override
    public String getEstadoDetalhado() {
        return String.format("Termostato %s %s — %s | Modo: %s | Alvo: %.1f°C | Atual: %.1f°C",
                getMarca(), getModelo(),
                estaLigado() ? "LIGADO" : "DESLIGADO",
                modo, temperaturaAlvo, temperaturaAtual);
    }
}
