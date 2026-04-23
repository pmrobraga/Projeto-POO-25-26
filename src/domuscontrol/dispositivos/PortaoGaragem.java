package domuscontrol.dispositivos;

/**
 * Portão de garagem — abertura total, parcial ou fechado.
 */
public class PortaoGaragem extends DispositivoComAbertura {

    private static final long serialVersionUID = 1L;

    public PortaoGaragem(String id, String marca, String modelo, double consumoPorHora) {
        super(id, marca, modelo, consumoPorHora);
    }

    @Override
    public String getEstadoDetalhado() {
        return String.format("Portão %s %s — Abertura: %d%%",
                getMarca(), getModelo(), getPercentagemAbertura());
    }
}
