package domuscontrol.dispositivos;

/**
 * Abstração para dispositivos com grau de abertura (cortinas, portões, estores).
 * Permite definir abertura de 0% (fechado) a 100% (totalmente aberto).
 */
public abstract class DispositivoComAbertura extends Dispositivo {

    private static final long serialVersionUID = 1L;

    private int percentagemAbertura; // 0 = fechado, 100 = totalmente aberto

    public DispositivoComAbertura(String id, String marca, String modelo, double consumoPorHora) {
        super(id, marca, modelo, consumoPorHora);
        this.percentagemAbertura = 0;
    }

    // -------------------------------------------------------------------------
    // Operações de abertura
    // -------------------------------------------------------------------------

    public void abrir() {
        setPercentagemAbertura(100);
        super.ligar();
    }

    public void fechar() {
        setPercentagemAbertura(0);
        super.desligar();
    }

    public void setPercentagemAbertura(int percentagem) {
        if (percentagem < 0 || percentagem > 100)
            throw new IllegalArgumentException("Percentagem de abertura deve ser entre 0 e 100.");
        this.percentagemAbertura = percentagem;
        if (percentagem > 0 && !estaLigado()) super.ligar();
        if (percentagem == 0 && estaLigado()) super.desligar();
    }

    // -------------------------------------------------------------------------
    // Getters
    // -------------------------------------------------------------------------

    public int getPercentagemAbertura() { return percentagemAbertura; }

    public boolean estaAberto()         { return percentagemAbertura == 100; }
    public boolean estaFechado()        { return percentagemAbertura == 0; }

    // -------------------------------------------------------------------------
    // Estado detalhado
    // -------------------------------------------------------------------------

    @Override
    public String getEstadoDetalhado() {
        return String.format("%s %s — Abertura: %d%%",
                getMarca(), getModelo(), percentagemAbertura);
    }
}
