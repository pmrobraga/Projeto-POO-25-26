package domuscontrol.dispositivos;

import java.time.LocalDateTime;

/**
 * Sensor de movimento — deteta presença.
 * Muito útil para automações (ex: ligar luzes ao detetar movimento).
 */
public class SensorMovimento extends Sensor {

    private static final long serialVersionUID = 1L;

    private boolean movimentoDetetado;
    private LocalDateTime ultimaDetecao;
    private int sensibilidade; // 1 (baixa) a 5 (alta)

    public SensorMovimento(String id, String marca, String modelo, double consumoPorHora) {
        super(id, marca, modelo, consumoPorHora, "Movimento", "bool");
        this.movimentoDetetado = false;
        this.sensibilidade      = 3;
    }

    public void detetarMovimento() {
        this.movimentoDetetado = true;
        this.ultimaDetecao     = LocalDateTime.now();
        setValor(1.0); // 1 = movimento detetado
    }

    public void limparMovimento() {
        this.movimentoDetetado = false;
        setValor(0.0); // 0 = sem movimento
    }

    public void setSensibilidade(int sensibilidade) {
        if (sensibilidade < 1 || sensibilidade > 5)
            throw new IllegalArgumentException("Sensibilidade deve estar entre 1 e 5.");
        this.sensibilidade = sensibilidade;
    }

    public boolean isMovimentoDetetado() { return movimentoDetetado; }
    public LocalDateTime getUltimaDetecao() { return ultimaDetecao; }
    public int getSensibilidade()          { return sensibilidade; }

    @Override
    public String getEstadoDetalhado() {
        return String.format("Sensor Movimento %s %s — %s | %s | Sensibilidade: %d/5",
                getMarca(), getModelo(),
                estaLigado() ? "ATIVO" : "INATIVO",
                movimentoDetetado ? "MOVIMENTO DETETADO" : "Sem movimento",
                sensibilidade);
    }
}
