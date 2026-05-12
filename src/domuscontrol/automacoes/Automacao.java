package domuscontrol.automacoes;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Uma Automaçao associa uma Condiçao a uma lista de Acoes.
 *
 * Quando a condiçao se verifica, todas as acoes são executadas.
 *
 * Exemplo:
 *   Condição: luminosidade < 100 lux
 *   Açoes:   ligar lâmpada sala, abrir cortinas
 */
public class Automacao implements Serializable {

    private static final long serialVersionUID = 1L;

    private final String id;
    private String nome;
    private String descricao;
    private boolean ativa;

    private final Condicao condicao;
    private final List<Acao> acoes;

    private LocalDateTime ultimaExecucao;
    private int numeroExecucoes;

    // Construtor

    public Automacao(String id, String nome, String descricao, Condicao condicao) {
        if (id == null || id.isBlank())
            throw new IllegalArgumentException("ID da automação não pode ser vazio.");
        if (nome == null || nome.isBlank())
            throw new IllegalArgumentException("Nome da automação não pode ser vazio.");
        if (condicao == null)
            throw new IllegalArgumentException("A condição não pode ser nula.");

        this.id = id;
        this.nome = nome;
        this.descricao = descricao;
        this.condicao = condicao;
        this.acoes = new ArrayList<>();
        this.ativa = true;
        this.numeroExecucoes = 0;
    }

    // Gestão de açoes

    public void adicionarAcao(Acao acao) {
        if (acao == null) throw new IllegalArgumentException("Acção não pode ser nula.");
        acoes.add(acao);
    }

    public List<Acao> getAcoes() {
        return Collections.unmodifiableList(acoes);
    }

    // Avaliação e execução

    /**
     * Verifica a condiçao e, se verdadeira, executa todas as açoes.
     * @return true se a automaçao foi disparada.
     */
    public boolean avaliarEExecutar() {
        if (!ativa) return false;
        if (condicao.avaliar()) {
            acoes.forEach(Acao::executar);
            ultimaExecucao = LocalDateTime.now();
            numeroExecucoes++;
            return true;
        }
        return false;
    }

    // Getters / Setters

    public String getId()               { return id; }
    public String getNome()             { return nome; }
    public String getDescricao()        { return descricao; }
    public boolean isAtiva()           { return ativa; }
    public Condicao getCondicao()       { return condicao; }
    public int getNumeroExecucoes()     { return numeroExecucoes; }
    public LocalDateTime getUltimaExecucao() { return ultimaExecucao; }

    public void setAtiva(boolean ativa)    { this.ativa = ativa; }
    public void setNome(String nome)         { this.nome = nome; }
    public void setDescricao(String desc)    { this.descricao = desc; }


    // toString

    @Override
    public String toString() {
        return String.format("Automação [%s] '%s' | Condição: %s | Ações: %d | %s",
                id, nome, condicao.descrever(), acoes.size(),
                ativa ? "ATIVA" : "INATIVA");
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Automacao)) return false;
        return id.equals(((Automacao) o).id);
    }

    @Override
    public int hashCode() { return id.hashCode(); }
}
