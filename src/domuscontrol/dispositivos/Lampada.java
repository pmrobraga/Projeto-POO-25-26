package domuscontrol.dispositivos;

/**
 * Lâmpada inteligente.
 * Permite ajustar a intensidade da luminosidade (0-100%)
 * e, opcionalmente, a temperatura de cor (2700K - 4000K).
 */
public class Lampada extends Dispositivo {

    private static final long serialVersionUID = 1L;

    private int intensidade;          // 0 a 100 (%)
    private final boolean temCor;
    private int temperaturaKelvin;    // 2700 a 4000 K (só se temCor == true)

    private static final int INTENSIDADE_PADRAO = 100;
    private static final int TEMPERATURA_PADRAO = 3000;
    private static final int TEMP_MIN = 2700;
    private static final int TEMP_MAX = 4000;

    // -------------------------------------------------------------------------
    // Construtores
    // -------------------------------------------------------------------------

    public Lampada(String id, String marca, String modelo, double consumoPorHora, boolean temCor) {
        super(id, marca, modelo, consumoPorHora);
        this.temCor = temCor;
        this.intensidade = INTENSIDADE_PADRAO;
        this.temperaturaKelvin = TEMPERATURA_PADRAO;
    }

    public Lampada(String id, String marca, String modelo, double consumoPorHora) {
        this(id, marca, modelo, consumoPorHora, false);
    }

    // -------------------------------------------------------------------------
    // Operações específicas
    // -------------------------------------------------------------------------

    public void setIntensidade(int intensidade) {
        if (intensidade < 0 || intensidade > 100)
            throw new IllegalArgumentException("Intensidade deve estar entre 0 e 100.");
        this.intensidade = intensidade;
        // Se colocarem a 0, desliga automaticamente
        if (intensidade == 0) super.desligar();
        else if (!estaLigado()) super.ligar();
    }

    public void setTemperatura(int kelvin) {
        if (!temCor)
            throw new UnsupportedOperationException("Esta lâmpada não suporta ajuste de cor.");
        if (kelvin < TEMP_MIN || kelvin > TEMP_MAX)
            throw new IllegalArgumentException(
                    String.format("Temperatura deve estar entre %dK e %dK.", TEMP_MIN, TEMP_MAX));
        this.temperaturaKelvin = kelvin;
    }

    // -------------------------------------------------------------------------
    // Getters
    // -------------------------------------------------------------------------

    public int getIntensidade()       { return intensidade; }
    public boolean isTemCor()         { return temCor; }
    public int getTemperaturaKelvin() { return temperaturaKelvin; }

    // -------------------------------------------------------------------------
    // Estado detalhado
    // -------------------------------------------------------------------------

    @Override
    public String getEstadoDetalhado() {
        StringBuilder sb = new StringBuilder();
        sb.append(String.format("Lâmpada %s %s — %s | Intensidade: %d%%",
                getMarca(), getModelo(),
                estaLigado() ? "LIGADA" : "DESLIGADA",
                intensidade));
        if (temCor) {
            sb.append(String.format(" | Temperatura: %dK", temperaturaKelvin));
        }
        return sb.toString();
    }
}
