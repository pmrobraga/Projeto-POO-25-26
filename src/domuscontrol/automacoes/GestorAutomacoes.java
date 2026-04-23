package domuscontrol.automacoes;

import domuscontrol.cenarios.Cenario;
import domuscontrol.excecoes.CasaNaoEncontradaException;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.*;

/**
 * Gere todas as automações, escalonamentos e cenários associados a uma casa.
 *
 * É o ponto central para:
 *  - registar/remover automações, escalonamentos e cenários
 *  - disparar a avaliação de todos os automatismos (chamado a cada tick do relógio)
 */
public class GestorAutomacoes implements Serializable {

    private static final long serialVersionUID = 1L;

    private final Map<String, Automacao>    automacoes;
    private final Map<String, Escalonamento> escalonamentos;
    private final Map<String, Cenario>      cenarios;

    // -------------------------------------------------------------------------
    // Construtor
    // -------------------------------------------------------------------------

    public GestorAutomacoes() {
        this.automacoes     = new LinkedHashMap<>();
        this.escalonamentos = new LinkedHashMap<>();
        this.cenarios       = new LinkedHashMap<>();
    }

    // -------------------------------------------------------------------------
    // Automações
    // -------------------------------------------------------------------------

    public void adicionarAutomacao(Automacao a) {
        if (a == null) throw new IllegalArgumentException("Automação não pode ser nula.");
        if (automacoes.containsKey(a.getId()))
            throw new IllegalArgumentException("Já existe automação com ID '" + a.getId() + "'.");
        automacoes.put(a.getId(), a);
    }

    public void removerAutomacao(String id) {
        if (!automacoes.containsKey(id))
            throw new NoSuchElementException("Automação '" + id + "' não encontrada.");
        automacoes.remove(id);
    }

    public Automacao getAutomacao(String id) {
        Automacao a = automacoes.get(id);
        if (a == null)
            throw new NoSuchElementException("Automação '" + id + "' não encontrada.");
        return a;
    }

    public Collection<Automacao> getAutomacoes() {
        return Collections.unmodifiableCollection(automacoes.values());
    }

    // -------------------------------------------------------------------------
    // Escalonamentos
    // -------------------------------------------------------------------------

    public void adicionarEscalonamento(Escalonamento e) {
        if (e == null) throw new IllegalArgumentException("Escalonamento não pode ser nulo.");
        if (escalonamentos.containsKey(e.getId()))
            throw new IllegalArgumentException("Já existe escalonamento com ID '" + e.getId() + "'.");
        escalonamentos.put(e.getId(), e);
    }

    public void removerEscalonamento(String id) {
        if (!escalonamentos.containsKey(id))
            throw new NoSuchElementException("Escalonamento '" + id + "' não encontrado.");
        escalonamentos.remove(id);
    }

    public Escalonamento getEscalonamento(String id) {
        Escalonamento e = escalonamentos.get(id);
        if (e == null)
            throw new NoSuchElementException("Escalonamento '" + id + "' não encontrado.");
        return e;
    }

    public Collection<Escalonamento> getEscalonamentos() {
        return Collections.unmodifiableCollection(escalonamentos.values());
    }

    // -------------------------------------------------------------------------
    // Cenários
    // -------------------------------------------------------------------------

    public void adicionarCenario(Cenario c) {
        if (c == null) throw new IllegalArgumentException("Cenário não pode ser nulo.");
        if (cenarios.containsKey(c.getId()))
            throw new IllegalArgumentException("Já existe cenário com ID '" + c.getId() + "'.");
        cenarios.put(c.getId(), c);
    }

    public void removerCenario(String id) {
        if (!cenarios.containsKey(id))
            throw new NoSuchElementException("Cenário '" + id + "' não encontrado.");
        cenarios.remove(id);
    }

    public Cenario getCenario(String id) {
        Cenario c = cenarios.get(id);
        if (c == null)
            throw new NoSuchElementException("Cenário '" + id + "' não encontrado.");
        return c;
    }

    public Collection<Cenario> getCenarios() {
        return Collections.unmodifiableCollection(cenarios.values());
    }

    // -------------------------------------------------------------------------
    // Tick do relógio — avalia todas as regras
    // -------------------------------------------------------------------------

    /**
     * Chamado pelo DomusControl a cada avanço do relógio interno.
     * Avalia automações e escalonamentos, executando os que se aplicam.
     *
     * @param agora Momento actual da simulação.
     */
    public void tick(LocalDateTime agora) {
        // Avaliar automações
        for (Automacao a : automacoes.values()) {
            boolean disparou = a.avaliarEExecutar();
            if (disparou) {
                System.out.println("[AUTO] " + a.getNome() + " disparada às " + agora);
            }
        }

        // Avaliar escalonamentos
        for (Escalonamento e : escalonamentos.values()) {
            boolean disparou = e.avaliarEExecutar(agora);
            if (disparou) {
                System.out.println("[ESCAL] " + e.getNome() + " executado às " + agora);
            }
        }
    }

    // -------------------------------------------------------------------------
    // toString
    // -------------------------------------------------------------------------

    @Override
    public String toString() {
        return String.format("GestorAutomacoes: %d automações, %d escalonamentos, %d cenários",
                automacoes.size(), escalonamentos.size(), cenarios.size());
    }
}
