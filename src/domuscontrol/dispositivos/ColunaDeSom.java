package domuscontrol.dispositivos;

/**
 * Coluna de som inteligente.
 * Permite controlar o volume (0-100%) e a fonte de áudio.
 */
public class ColunaDeSom extends Dispositivo {

    private static final long serialVersionUID = 1L;

    private int volume; // 0 a 100
    private String fonte; // ex: "Bluetooth", "WiFi", "AUX"

    private static final int VOLUME_PADRAO = 50;

    public ColunaDeSom(String id, String marca, String modelo, double consumoPorHora) {
        super(id, marca, modelo, consumoPorHora);
        this.volume = VOLUME_PADRAO;
        this.fonte = "Bluetooth";
    }

    // Operações especificas

    public void setVolume(int volume) {
        if (volume < 0 || volume > 100)
            throw new IllegalArgumentException("Volume deve estar entre 0 e 100.");
        this.volume = volume;
    }

    public void aumentarVolume(int incremento) {
        setVolume(Math.min(100, this.volume + incremento));
    }

    public void diminuirVolume(int decremento) {
        setVolume(Math.max(0, this.volume - decremento));
    }

    public void setFonte(String fonte) {
        if (fonte == null || fonte.isBlank())
            throw new IllegalArgumentException("A fonte não pode ser vazia.");
        this.fonte = fonte;
    }

    // Getters

    public int getVolume()   { return volume; }
    public String getFonte() { return fonte; }

    // Estado detalhado

    @Override
    public String getEstadoDetalhado() {
        return String.format("Coluna %s %s — %s | Volume: %d%% | Fonte: %s",
                getMarca(), getModelo(),
                estaLigado() ? "LIGADA" : "DESLIGADA",
                volume, fonte);
    }
}
