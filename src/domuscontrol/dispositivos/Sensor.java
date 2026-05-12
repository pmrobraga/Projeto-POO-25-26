package domuscontrol.dispositivos;

/**
 * Sensor genérico de leitura de valores ambientais.
 * Exemplos: sensor de pluviosidade, luminosidade, temperatura.
 * Usado pelas automações para verificar condições.
 */
public class Sensor extends Dispositivo {

    private static final long serialVersionUID = 1L;

    private double valorAtual;
    private final String unidade;    // ex: "lux", "mm/h", "°C"
    private final String tipoSensor; // ex: "Luminosidade", "Pluviosidade", "Temperatura"

    public Sensor(String id, String marca, String modelo, double consumoPorHora,
                  String tipoSensor, String unidade) {
        super(id, marca, modelo, consumoPorHora);
        this.tipoSensor = tipoSensor;
        this.unidade = unidade;
        this.valorAtual = 0.0;
    }

    // Leitura e escrita de valor

    public void setValor(double valor) {
        this.valorAtual = valor;
    }

    public double getValor() {
        return valorAtual;
    }

    public String getUnidade()    { return unidade; }
    public String getTipoSensor() { return tipoSensor; }

    // Estado detalhado

    @Override
    public String getEstadoDetalhado() {
        return String.format("Sensor de %s %s %s — Leitura: %.2f %s | %s",
                tipoSensor, getMarca(), getModelo(),
                valorAtual, unidade,
                estaLigado() ? "ATIVO" : "INATIVO");
    }
}
