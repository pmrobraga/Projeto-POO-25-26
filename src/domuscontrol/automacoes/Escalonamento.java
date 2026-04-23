package domuscontrol.automacoes;

import java.io.Serializable;
import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;

/**
 * Um Escalonamento define ações a executar com base no tempo.

 * Pode ser:
 *  - Diário: executa todos os dias a uma determinada hora.
 *  - Semanal: executa em dias específicos da semana.
 *  - Num intervalo: ativo entre horaInicio e horaFim.

 * Exemplos:
 *   - Todos os dias às 07:00, abrir cortinas.
 *   - De segunda a sexta, entre as 18:00 e as 23:00, ligar luzes da sala.
 */
public class Escalonamento implements Serializable {

    private static final long serialVersionUID = 1L;

    public enum TipoEscalonamento {
        DIARIO,   // Executa a uma hora fixa, todos os dias
        SEMANAL,  // Executa em dias da semana específicos
        INTERVALO // Executa enquanto o relógio estiver entre horaInicio e horaFim
    }

    private final String id;
    private String nome;
    private boolean ativo;

    private final TipoEscalonamento tipo;
    private final LocalTime horaExecucao;  // Para DIARIO e SEMANAL
    private final LocalTime horaInicio;    // Para INTERVALO
    private final LocalTime horaFim;       // Para INTERVALO
    private final Set<DayOfWeek> diasDaSemana; // Para SEMANAL

    private final List<Acao> acoes;
    private LocalDateTime ultimaExecucao;

    // -------------------------------------------------------------------------
    // Construtores
    // -------------------------------------------------------------------------

    /** Escalonamento diário — executa à mesma hora todos os dias. */
    public Escalonamento(String id, String nome, LocalTime horaExecucao) {
        this(id, nome, TipoEscalonamento.DIARIO, horaExecucao, null, null, null);
    }

    /** Escalonamento semanal — executa em dias específicos. */
    public Escalonamento(String id, String nome, LocalTime horaExecucao, Set<DayOfWeek> dias) {
        this(id, nome, TipoEscalonamento.SEMANAL, horaExecucao, null, null, dias);
    }

    /** Escalonamento por intervalo de horas — activo enquanto dentro do intervalo. */
    public Escalonamento(String id, String nome, LocalTime horaInicio, LocalTime horaFim) {
        this(id, nome, TipoEscalonamento.INTERVALO, null, horaInicio, horaFim, null);
    }

    private Escalonamento(String id, String nome, TipoEscalonamento tipo,
                          LocalTime horaExecucao, LocalTime horaInicio,
                          LocalTime horaFim, Set<DayOfWeek> diasDaSemana) {
        if (id == null || id.isBlank())
            throw new IllegalArgumentException("ID do escalonamento não pode ser vazio.");
        if (nome == null || nome.isBlank())
            throw new IllegalArgumentException("Nome do escalonamento não pode ser vazio.");

        this.id = id;
        this.nome = nome;
        this.tipo = tipo;
        this.horaExecucao = horaExecucao;
        this.horaInicio = horaInicio;
        this.horaFim = horaFim;
        this.diasDaSemana = diasDaSemana != null
                ? Collections.unmodifiableSet(new HashSet<>(diasDaSemana))
                : Collections.emptySet();
        this.acoes = new ArrayList<>();
        this.ativo = true;
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
    // Avaliação
    // -------------------------------------------------------------------------

    /**
     * Verifica se o escalonamento deve ser disparado neste momento.
     * @param agora Data/hora actual (do relógio interno da simulação).
     * @return true se as acções devem ser executadas.
     */
    public boolean deveExecutar(LocalDateTime agora) {
        if (!ativo) return false;

        LocalTime horaAgora = agora.toLocalTime();

        switch (tipo) {
            case DIARIO:
                return horaAgora.equals(horaExecucao);

            case SEMANAL:
                return diasDaSemana.contains(agora.getDayOfWeek())
                        && horaAgora.equals(horaExecucao);

            case INTERVALO:
                if (horaInicio.isBefore(horaFim)) {
                    return !horaAgora.isBefore(horaInicio) && !horaAgora.isAfter(horaFim);
                } else {
                    // Intervalo que passa meia-noite (ex: 23:00 - 06:00)
                    return !horaAgora.isBefore(horaInicio) || !horaAgora.isAfter(horaFim);
                }

            default:
                return false;
        }
    }

    /**
     * Avalia e executa as acções se for o momento certo.
     * @return true se foi executado.
     */
    public boolean avaliarEExecutar(LocalDateTime agora) {
        if (deveExecutar(agora)) {
            acoes.forEach(Acao::executar);
            ultimaExecucao = agora;
            return true;
        }
        return false;
    }

    // -------------------------------------------------------------------------
    // Getters / Setters
    // -------------------------------------------------------------------------

    public String getId()                    { return id; }
    public String getNome()                  { return nome; }
    public boolean isActivo()                { return ativo; }
    public TipoEscalonamento getTipo()       { return tipo; }
    public LocalTime getHoraExecucao()       { return horaExecucao; }
    public LocalTime getHoraInicio()         { return horaInicio; }
    public LocalTime getHoraFim()            { return horaFim; }
    public Set<DayOfWeek> getDiasDaSemana()  { return diasDaSemana; }
    public LocalDateTime getUltimaExecucao() { return ultimaExecucao; }

    public void setActivo(boolean activo) { this.ativo = activo; }
    public void setNome(String nome)      { this.nome = nome; }

    // -------------------------------------------------------------------------
    // toString
    // -------------------------------------------------------------------------

    @Override
    public String toString() {
        String detalhes;
        switch (tipo) {
            case DIARIO:
                detalhes = "Diário às " + horaExecucao;
                break;
            case SEMANAL:
                detalhes = "Semanal (" + diasDaSemana + ") às " + horaExecucao;
                break;
            case INTERVALO:
                detalhes = "Intervalo " + horaInicio + " - " + horaFim;
                break;
            default:
                detalhes = "";
        }
        return String.format("Escalonamento [%s] '%s' | %s | Acções: %d | %s",
                id, nome, detalhes, acoes.size(), ativo ? "ATIVO" : "INATIVO");
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Escalonamento)) return false;
        return id.equals(((Escalonamento) o).id);
    }

    @Override
    public int hashCode() { return id.hashCode(); }
}
