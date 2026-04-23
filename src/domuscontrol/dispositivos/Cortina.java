package domuscontrol.dispositivos;

/**
 * Cortina inteligente — suporte a abertura parcial ou total.
 */
public class Cortina extends DispositivoComAbertura {

    private static final long serialVersionUID = 1L;

    public Cortina(String id, String marca, String modelo, double consumoPorHora) {
        super(id, marca, modelo, consumoPorHora);
    }

    @Override
    public String getEstadoDetalhado() {
        return String.format("Cortina %s %s — Abertura: %d%%",
                getMarca(), getModelo(), getPercentagemAbertura());
    }
}
