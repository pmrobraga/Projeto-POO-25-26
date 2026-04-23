package domuscontrol.dispositivos;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.Duration;

/**
 * Classe abstrata que representa um dispositivo genérico no DomusControl.
 * Todos os dispositivos herdam desta classe.
 *
 * Implementa Serializable para permitir gravação em ficheiro binário.
 */
public abstract class Dispositivo implements Operavel, Serializable {

    private static final long serialVersionUID = 1L;

    // --- Identificação ---
    private final String id;
    private final String marca;
    private final String modelo;

    // --- Consumo e estado ---
    private final double consumoPorHora; // em Wh
    private boolean ligado;

    // --- Estatísticas ---
    private int numeroActivacoes;
    private long tempoLigadoMinutos; // total acumulado
    private LocalDateTime ultimaActivacao;

    // -------------------------------------------------------------------------
    // Construtor
    // -------------------------------------------------------------------------

    public Dispositivo(String id, String marca, String modelo, double consumoPorHora) {
        if (id == null || id.isBlank())
            throw new IllegalArgumentException("O ID do dispositivo não pode ser nulo ou vazio.");
        if (marca == null || marca.isBlank())
            throw new IllegalArgumentException("A marca não pode ser nula ou vazia.");
        if (modelo == null || modelo.isBlank())
            throw new IllegalArgumentException("O modelo não pode ser nulo ou vazio.");
        if (consumoPorHora < 0)
            throw new IllegalArgumentException("O consumo por hora não pode ser negativo.");

        this.id = id;
        this.marca = marca;
        this.modelo = modelo;
        this.consumoPorHora = consumoPorHora;
        this.ligado = false;
        this.numeroActivacoes = 0;
        this.tempoLigadoMinutos = 0;
        this.ultimaActivacao = null;
    }

    // -------------------------------------------------------------------------
    // Implementação de Operavel
    // -------------------------------------------------------------------------

    @Override
    public void ligar() {
        if (!ligado) {
            ligado = true;
            numeroActivacoes++;
            ultimaActivacao = LocalDateTime.now();
        }
    }

    @Override
    public void desligar() {
        if (ligado) {
            ligado = false;
            if (ultimaActivacao != null) {
                long minutos = Duration.between(ultimaActivacao, LocalDateTime.now()).toMinutes();
                tempoLigadoMinutos += minutos;
            }
        }
    }

    @Override
    public boolean estaLigado() {
        return ligado;
    }

    /**
     * Cada subclasse fornece uma descrição detalhada do seu estado.
     */
    @Override
    public abstract String getEstadoDetalhado();

    // -------------------------------------------------------------------------
    // Simulação de tempo (para testes e avanço manual do relógio)
    // -------------------------------------------------------------------------

    /**
     * Regista manualmente minutos de funcionamento (usado na simulação de tempo).
     */
    public void registarTempoFuncionamento(long minutos) {
        if (ligado && minutos > 0) {
            tempoLigadoMinutos += minutos;
        }
    }

    /**
     * Calcula o consumo total em Wh com base no tempo ligado.
     */
    public double getConsumoTotal() {
        return (tempoLigadoMinutos / 60.0) * consumoPorHora;
    }

    // -------------------------------------------------------------------------
    // Getters
    // -------------------------------------------------------------------------

    public String getId()               { return id; }
    public String getMarca()            { return marca; }
    public String getModelo()           { return modelo; }
    public double getConsumoPorHora()   { return consumoPorHora; }
    public int getNumeroActivacoes()    { return numeroActivacoes; }
    public long getTempoLigadoMinutos() { return tempoLigadoMinutos; }
    public LocalDateTime getUltimaActivacao() { return ultimaActivacao; }

    // -------------------------------------------------------------------------
    // toString / equals / hashCode
    // -------------------------------------------------------------------------

    @Override
    public String toString() {
        return String.format("[%s] %s %s | Consumo: %.1f Wh/h | Estado: %s",
                id, marca, modelo, consumoPorHora,
                ligado ? "LIGADO" : "DESLIGADO");
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Dispositivo)) return false;
        return id.equals(((Dispositivo) o).id);
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }
}
