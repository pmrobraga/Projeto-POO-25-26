package domuscontrol.dispositivos;

import java.time.LocalDateTime;

/**
 * Sensor de fumo — alarme de incêndio.
 * Quando deteta fumo, dispara o alarme.
 */
public class SensorFumo extends Sensor {

    private static final long serialVersionUID = 1L;

    private boolean alarmeAtivo;
    private LocalDateTime ultimoAlarme;
    private double limiarAlarme; // concentracao de fumo que dispara o alarme

    public SensorFumo(String id, String marca, String modelo, double consumoPorHora) {
        super(id, marca, modelo, consumoPorHora, "Fumo", "%");
        this.alarmeAtivo  = false;
        this.limiarAlarme  = 10.0; // 10% de concentracao
    }

    @Override
    public void setValor(double concentracao) {
        super.setValor(concentracao);
        if (concentracao >= limiarAlarme && !alarmeAtivo) {
            dispararAlarme();
        } else if (concentracao < limiarAlarme) {
            alarmeAtivo = false;
        }
    }

    private void dispararAlarme() {
        this.alarmeAtivo  = true;
        this.ultimoAlarme  = LocalDateTime.now();
        System.out.println("  !!! ALARME DE FUMO: " + getMarca() + " " + getModelo() + " !!!");
    }

    public void silenciarAlarme() {
        this.alarmeAtivo = false;
    }

    public void setLimiarAlarme(double limiar) {
        if (limiar <= 0) throw new IllegalArgumentException("Limiar deve ser positivo.");
        this.limiarAlarme = limiar;
    }

    public boolean isAlarmeAtivo()           { return alarmeAtivo; }
    public LocalDateTime getUltimoAlarme()    { return ultimoAlarme; }
    public double getLimiarAlarme()           { return limiarAlarme; }

    @Override
    public String getEstadoDetalhado() {
        return String.format("Sensor Fumo %s %s — %s | Fumo: %.1f%% | Alarme: %s",
                getMarca(), getModelo(),
                estaLigado() ? "ATIVO" : "INATIVO",
                getValor(),
                alarmeAtivo ? "DISPARADO!" : "Normal");
    }
}
