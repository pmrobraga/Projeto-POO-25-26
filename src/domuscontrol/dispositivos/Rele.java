package domuscontrol.dispositivos;

/**
 * Relé: o dispositivo mais simples — apenas liga ou desliga.
 * Serve de base para tomadas inteligentes, etc.
 */
public class Rele extends Dispositivo {

    private static final long serialVersionUID = 1L;

    public Rele(String id, String marca, String modelo, double consumoPorHora) {
        super(id, marca, modelo, consumoPorHora);
    }

    @Override
    public String getEstadoDetalhado() {
        return String.format("Relé %s %s — %s", getMarca(), getModelo(),
                estaLigado() ? "LIGADO" : "DESLIGADO");
    }
}
