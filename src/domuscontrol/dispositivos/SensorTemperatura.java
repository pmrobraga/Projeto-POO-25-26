package domuscontrol.dispositivos;

/**
 * Sensor de temperatura e humidade.
 * Muito útil para automações com o termostato.
 */
public class SensorTemperatura extends Sensor {

    private static final long serialVersionUID = 1L;

    private double humidade; // 0 a 100%
    private double sensacaoTermica;

    public SensorTemperatura(String id, String marca, String modelo, double consumoPorHora) {
        super(id, marca, modelo, consumoPorHora, "Temperatura", "C");
        this.humidade       = 50.0;
        this.sensacaoTermica = 20.0;
    }

    public void setHumidade(double humidade) {
        if (humidade < 0 || humidade > 100)
            throw new IllegalArgumentException("Humidade deve estar entre 0 e 100%.");
        this.humidade = humidade;
        calcularSensacaoTermica();
    }

    private void calcularSensacaoTermica() {
        // Formula simplificada de sensacao termica
        double t = getValor();
        this.sensacaoTermica = t - 0.4 * (t - 10) * (1 - humidade / 100.0);
    }

    @Override
    public void setValor(double temperatura) {
        super.setValor(temperatura);
        calcularSensacaoTermica();
    }

    public double getHumidade()        { return humidade; }
    public double getSensacaoTermica() { return sensacaoTermica; }

    @Override
    public String getEstadoDetalhado() {
        return String.format("Sensor Temp %s %s — %.1f°C | Humidade: %.1f%% | Sensacao: %.1f°C | %s",
                getMarca(), getModelo(),
                getValor(), humidade, sensacaoTermica,
                estaLigado() ? "ATIVO" : "INATIVO");
    }
}
