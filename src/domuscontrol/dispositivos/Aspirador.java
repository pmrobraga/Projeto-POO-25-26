package domuscontrol.dispositivos;

/**
 * Aspirador robô inteligente.
 * Permite controlar o modo de limpeza e monitorizar a bateria.
 */
public class Aspirador extends Dispositivo {

    private static final long serialVersionUID = 1L;

    public enum Modo { AUTO, MANUAL, TURBO, SILENCIOSO, DOCK }

    private Modo modo;
    private int bateria; // 0 a 100%
    private boolean aNaBase;

    public Aspirador(String id, String marca, String modelo, double consumoPorHora) {
        super(id, marca, modelo, consumoPorHora);
        this.modo    = Modo.DOCK;
        this.bateria = 100;
        this.aNaBase = true;
    }

    @Override
    public void ligar() {
        super.ligar();
        this.modo    = Modo.AUTO;
        this.aNaBase = false;
    }

    @Override
    public void desligar() {
        super.desligar();
        this.modo    = Modo.DOCK;
        this.aNaBase = true;
    }

    public void setModo(Modo modo) {
        if (modo == Modo.DOCK) { desligar(); return; }
        if (!estaLigado()) super.ligar();
        this.modo    = modo;
        this.aNaBase = false;
    }

    public void voltarABase() {
        this.modo    = Modo.DOCK;
        this.aNaBase = true;
        super.desligar();
    }

    public void setBateria(int bateria) {
        if (bateria < 0 || bateria > 100)
            throw new IllegalArgumentException("Bateria deve estar entre 0 e 100.");
        this.bateria = bateria;
    }

    public Modo getModo()     { return modo; }
    public int getBateria()   { return bateria; }
    public boolean isNaBase() { return aNaBase; }

    @Override
    public String getEstadoDetalhado() {
        return String.format("Aspirador %s %s — %s | Modo: %s | Bateria: %d%% | %s",
                getMarca(), getModelo(),
                estaLigado() ? "LIGADO" : "DESLIGADO",
                modo, bateria,
                aNaBase ? "Na base" : "A limpar");
    }
}
