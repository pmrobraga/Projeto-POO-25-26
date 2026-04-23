package domuscontrol.cenarios;

import domuscontrol.automacoes.Acao;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Um Cenário agrupa um conjunto de acções a executar em simultâneo
 * sobre vários dispositivos da casa.
 *
 * Exemplos predefinidos:
 *   - "Fora de Casa": desliga tudo, fecha cortinas
 *   - "Ver Cinema": baixa luzes, aumenta volume
 *   - "Deitar": desliga tudo, fecha cortinas
 *   - "Acordar": abre cortinas, liga luzes suaves
 */
public class Cenario implements Serializable {

    private static final long serialVersionUID = 1L;

    private final String id;
    private String nome;
    private String descricao;

    private final List<Acao> acoes;

    private LocalDateTime ultimaActivacao;
    private int numeroActivacoes;

    // -------------------------------------------------------------------------
    // Construtor
    // -------------------------------------------------------------------------

    public Cenario(String id, String nome, String descricao) {
        if (id == null || id.isBlank())
            throw new IllegalArgumentException("ID do cenário não pode ser vazio.");
        if (nome == null || nome.isBlank())
            throw new IllegalArgumentException("Nome do cenário não pode ser vazio.");

        this.id = id;
        this.nome = nome;
        this.descricao = descricao;
        this.acoes = new ArrayList<>();
        this.numeroActivacoes = 0;
    }

    // -------------------------------------------------------------------------
    // Gestão de acções
    // -------------------------------------------------------------------------

    public void adicionarAcao(Acao acao) {
        if (acao == null) throw new IllegalArgumentException("Acção não pode ser nula.");
        acoes.add(acao);
    }

    public List<Acao> getAcoes() {
        return Collections.unmodifiableList(acoes);
    }

    // -------------------------------------------------------------------------
    // Activação
    // -------------------------------------------------------------------------

    /**
     * Executa todas as acções do cenário pela ordem em que foram adicionadas.
     */
    public void activar() {
        acoes.forEach(Acao::executar);
        ultimaActivacao = LocalDateTime.now();
        numeroActivacoes++;
        System.out.println("Cenário '" + nome + "' activado (" + acoes.size() + " acções executadas).");
    }

    // -------------------------------------------------------------------------
    // Getters / Setters
    // -------------------------------------------------------------------------

    public String getId()                    { return id; }
    public String getNome()                  { return nome; }
    public String getDescricao()             { return descricao; }
    public int getNumeroActivacoes()         { return numeroActivacoes; }
    public LocalDateTime getUltimaActivacao(){ return ultimaActivacao; }

    public void setNome(String nome)         { this.nome = nome; }
    public void setDescricao(String desc)    { this.descricao = desc; }

    // -------------------------------------------------------------------------
    // toString
    // -------------------------------------------------------------------------

    @Override
    public String toString() {
        return String.format("Cenário [%s] '%s' — %s | Acções: %d | Activações: %d",
                id, nome, descricao, acoes.size(), numeroActivacoes);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Cenario)) return false;
        return id.equals(((Cenario) o).id);
    }

    @Override
    public int hashCode() { return id.hashCode(); }
}
