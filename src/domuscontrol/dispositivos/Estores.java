package domuscontrol.dispositivos;

/**
 * Estores exteriores inteligentes.
 * Herda de DispositivoComAbertura e adiciona proteção UV.
 */
public class Estores extends DispositivoComAbertura {

    private static final long serialVersionUID = 1L;

    private boolean protecaoUVAtiva;
    private int inclinacaoLamelas; // 0 a 90 graus (para estores venezianos)
    private final boolean temLamelas;

    public Estores(String id, String marca, String modelo,
                   double consumoPorHora, boolean temLamelas) {
        super(id, marca, modelo, consumoPorHora);
        this.temLamelas        = temLamelas;
        this.protecaoUVAtiva  = false;
        this.inclinacaoLamelas = 45;
    }

    public Estores(String id, String marca, String modelo, double consumoPorHora) {
        this(id, marca, modelo, consumoPorHora, false);
    }

    public void setProtecaoUV(boolean activa) {
        this.protecaoUVAtiva = activa;
    }

    public void setInclinacaoLamelas(int graus) {
        if (!temLamelas)
            throw new UnsupportedOperationException("Estes estores nao tem lamelas.");
        if (graus < 0 || graus > 90)
            throw new IllegalArgumentException("Inclinacao deve estar entre 0 e 90 graus.");
        this.inclinacaoLamelas = graus;
    }

    public boolean isProtecaoUVActiva()  { return protecaoUVAtiva; }
    public boolean isTemLamelas()        { return temLamelas; }
    public int getInclinacaoLamelas()    { return inclinacaoLamelas; }

    @Override
    public String getEstadoDetalhado() {
        StringBuilder sb = new StringBuilder();
        sb.append(String.format("Estores %s %s — Abertura: %d%% | UV: %s",
                getMarca(), getModelo(),
                getPercentagemAbertura(),
                protecaoUVAtiva ? "ATIVA" : "INATIVA"));
        if (temLamelas)
            sb.append(String.format(" | Lamelas: %d°", inclinacaoLamelas));
        return sb.toString();
    }
}
