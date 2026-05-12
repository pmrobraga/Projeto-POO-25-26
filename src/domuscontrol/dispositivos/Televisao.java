package domuscontrol.dispositivos;

/**
 * Televisão inteligente.
 * Permite controlar canal, volume e fonte de entrada.
 */
public class Televisao extends Dispositivo {

    private static final long serialVersionUID = 1L;

    public enum Fonte { HDMI1, HDMI2, HDMI3, TV, STREAMING }

    private int canal;
    private int volume;
    private Fonte fonte;

    private static final int VOLUME_PADRAO = 20;
    private static final int CANAL_PADRAO  = 1;

    public Televisao(String id, String marca, String modelo, double consumoPorHora) {
        super(id, marca, modelo, consumoPorHora);
        this.canal  = CANAL_PADRAO;
        this.volume = VOLUME_PADRAO;
        this.fonte  = Fonte.TV;
    }

    public void setCanal(int canal) {
        if (canal < 1) throw new IllegalArgumentException("Canal deve ser maior que 0.");
        this.canal = canal;
    }

    public void setVolume(int volume) {
        if (volume < 0 || volume > 100)
            throw new IllegalArgumentException("Volume deve estar entre 0 e 100.");
        this.volume = volume;
    }

    public void aumentarVolume(int inc) { setVolume(Math.min(100, this.volume + inc)); }
    public void diminuirVolume(int dec) { setVolume(Math.max(0, this.volume - dec)); }

    public void setFonte(Fonte fonte) {
        if (fonte == null) throw new IllegalArgumentException("Fonte nao pode ser nula.");
        this.fonte = fonte;
    }

    public int getCanal()    { return canal; }
    public int getVolume()   { return volume; }
    public Fonte getFonte()  { return fonte; }

    @Override
    public String getEstadoDetalhado() {
        return String.format("TV %s %s — %s | Canal: %d | Volume: %d | Fonte: %s",
                getMarca(), getModelo(),
                estaLigado() ? "LIGADA" : "DESLIGADA",
                canal, volume, fonte);
    }
}
