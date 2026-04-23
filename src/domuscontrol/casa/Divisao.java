package domuscontrol.casa;

import domuscontrol.dispositivos.Dispositivo;
import domuscontrol.excecoes.DispositivoJaExisteException;
import domuscontrol.excecoes.DispositivoNaoEncontradoException;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Representa uma divisão de uma casa (sala, quarto, cozinha, etc.).
 * Agrega os dispositivos instalados nessa divisão.
 */
public class Divisao implements Serializable {

    private static final long serialVersionUID = 1L;

    private final String id;
    private String nome;
    private final List<Dispositivo> dispositivos;

    // -------------------------------------------------------------------------
    // Construtor
    // -------------------------------------------------------------------------

    public Divisao(String id, String nome) {
        if (id == null || id.isBlank())
            throw new IllegalArgumentException("O ID da divisão não pode ser vazio.");
        if (nome == null || nome.isBlank())
            throw new IllegalArgumentException("O nome da divisão não pode ser vazio.");
        this.id = id;
        this.nome = nome;
        this.dispositivos = new ArrayList<>();
    }

    // -------------------------------------------------------------------------
    // Gestão de dispositivos
    // -------------------------------------------------------------------------

    public void adicionarDispositivo(Dispositivo d) throws DispositivoJaExisteException {
        if (d == null) throw new IllegalArgumentException("Dispositivo não pode ser nulo.");
        if (dispositivos.contains(d))
            throw new DispositivoJaExisteException("O dispositivo " + d.getId() + " já existe nesta divisão.");
        dispositivos.add(d);
    }

    public void removerDispositivo(String idDispositivo) throws DispositivoNaoEncontradoException {
        Dispositivo d = getDispositivoPorId(idDispositivo);
        dispositivos.remove(d);
    }

    public Dispositivo getDispositivoPorId(String idDispositivo) throws DispositivoNaoEncontradoException {
        return dispositivos.stream()
                .filter(d -> d.getId().equals(idDispositivo))
                .findFirst()
                .orElseThrow(() -> new DispositivoNaoEncontradoException(
                        "Dispositivo com ID '" + idDispositivo + "' não encontrado na divisão '" + nome + "'."));
    }

    public List<Dispositivo> getDispositivos() {
        return Collections.unmodifiableList(dispositivos);
    }

    public int getNumeroDispositivos() {
        return dispositivos.size();
    }

    // -------------------------------------------------------------------------
    // Consumo da divisão
    // -------------------------------------------------------------------------

    public double getConsumoTotal() {
        return dispositivos.stream()
                .mapToDouble(Dispositivo::getConsumoTotal)
                .sum();
    }

    public double getConsumoPorHoraActual() {
        return dispositivos.stream()
                .filter(Dispositivo::estaLigado)
                .mapToDouble(Dispositivo::getConsumoPorHora)
                .sum();
    }

    // -------------------------------------------------------------------------
    // Getters / Setters
    // -------------------------------------------------------------------------

    public String getId()   { return id; }
    public String getNome() { return nome; }

    public void setNome(String nome) {
        if (nome == null || nome.isBlank())
            throw new IllegalArgumentException("O nome da divisão não pode ser vazio.");
        this.nome = nome;
    }

    // -------------------------------------------------------------------------
    // toString
    // -------------------------------------------------------------------------

    @Override
    public String toString() {
        return String.format("Divisão [%s] %s (%d dispositivos)", id, nome, dispositivos.size());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Divisao)) return false;
        return id.equals(((Divisao) o).id);
    }

    @Override
    public int hashCode() { return id.hashCode(); }
}
