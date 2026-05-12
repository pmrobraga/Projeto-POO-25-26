package domuscontrol.casa;

import domuscontrol.dispositivos.Dispositivo;
import domuscontrol.excecoes.DivisaoJaExisteException;
import domuscontrol.excecoes.DivisaoNaoEncontradaException;
import domuscontrol.excecoes.DispositivoNaoEncontradoException;

import java.io.Serializable;
import java.util.*;

/**
 * Representa uma casa no DomusControl.
 * Contém divisões e agrega toda a lógica de gestão da habitação.
 */
public class Casa implements Serializable {

    private static final long serialVersionUID = 1L;

    private final String id;
    private String nome;
    private String morada;

    private final List<Divisao> divisoes;

    // Construtor

    public Casa(String id, String nome, String morada) {
        if (id == null || id.isBlank())
            throw new IllegalArgumentException("O ID da casa não pode ser vazio.");
        if (nome == null || nome.isBlank())
            throw new IllegalArgumentException("O nome da casa não pode ser vazio.");

        this.id = id;
        this.nome = nome;
        this.morada = morada;
        this.divisoes = new ArrayList<>();
    }

    // Gestão de divisões

    public void adicionarDivisao(Divisao d) throws DivisaoJaExisteException {
        if (d == null) throw new IllegalArgumentException("Divisão não pode ser nula.");
        if (divisoes.contains(d))
            throw new DivisaoJaExisteException("A divisão " + d.getId() + " já existe nesta casa.");
        divisoes.add(d);
    }

    public void removerDivisao(String idDivisao) throws DivisaoNaoEncontradaException {
        Divisao d = getDivisaoPorId(idDivisao);
        divisoes.remove(d);
    }

    public Divisao getDivisaoPorId(String idDivisao) throws DivisaoNaoEncontradaException {
        return divisoes.stream()
                .filter(d -> d.getId().equals(idDivisao))
                .findFirst()
                .orElseThrow(() -> new DivisaoNaoEncontradaException(
                        "Divisão '" + idDivisao + "' não encontrada na casa '" + nome + "'."));
    }

    public List<Divisao> getDivisoes() {
        return Collections.unmodifiableList(divisoes);
    }

    // Acesso a dispositivos globalmente na casa

    public Dispositivo getDispositivoPorId(String idDispositivo) throws DispositivoNaoEncontradoException {
        for (Divisao div : divisoes) {
            try {
                return div.getDispositivoPorId(idDispositivo);
            } catch (DispositivoNaoEncontradoException ignored) {}
        }
        throw new DispositivoNaoEncontradoException(
                "Dispositivo '" + idDispositivo + "' não encontrado em nenhuma divisão da casa '" + nome + "'.");
    }

    public List<Dispositivo> getTodosDispositivos() {
        List<Dispositivo> todos = new ArrayList<>();
        for (Divisao d : divisoes) todos.addAll(d.getDispositivos());
        return Collections.unmodifiableList(todos);
    }

    // Consumos

    public double getConsumoTotal() {
        return divisoes.stream().mapToDouble(Divisao::getConsumoTotal).sum();
    }

    public double getConsumoPorHoraAtual() {
        return divisoes.stream().mapToDouble(Divisao::getConsumoPorHoraAtual).sum();
    }

    // Top 3 divisões com mais dispositivos

    public List<Divisao> getTopDivisoesPorNumeroDispositivos(int n) {
        List<Divisao> copia = new ArrayList<>(divisoes);
        copia.sort((a, b) -> b.getNumeroDispositivos() - a.getNumeroDispositivos());
        return copia.subList(0, Math.min(n, copia.size()));
    }

    // Getters / Setters

    public String getId()      { return id; }
    public String getNome()    { return nome; }
    public String getMorada()  { return morada; }

    public void setNome(String nome) {
        if (nome == null || nome.isBlank())
            throw new IllegalArgumentException("O nome da casa não pode ser vazio.");
        this.nome = nome;
    }

    public void setMorada(String morada) { this.morada = morada; }

    // toString / equals / hashCode

    @Override
    public String toString() {
        return String.format("Casa [%s] %s — %s (%d divisões)",
                id, nome, morada, divisoes.size());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Casa)) return false;
        return id.equals(((Casa) o).id);
    }

    @Override
    public int hashCode() { return id.hashCode(); }
}
