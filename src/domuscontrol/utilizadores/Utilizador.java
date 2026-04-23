package domuscontrol.utilizadores;

import domuscontrol.casa.Casa;
import domuscontrol.excecoes.CasaNaoEncontradaException;

import java.io.Serializable;
import java.util.*;

/**
 * Representa um utilizador do DomusControl.
 *
 * Um utilizador pode ter acesso a várias casas, podendo ser
 * ADMINISTRADOR numa e apenas UTILIZADOR noutra.
 */
public class Utilizador implements Serializable {

    private static final long serialVersionUID = 1L;

    private final String id;
    private String nome;
    private String email;
    private String password; // Em produção deveria estar hasheada

    /**
     * Mapa: Casa -> tipo de acesso do utilizador nessa casa.
     */
    private final Map<Casa, TipoUtilizador> casas;

    // -------------------------------------------------------------------------
    // Construtor
    // -------------------------------------------------------------------------

    public Utilizador(String id, String nome, String email, String password) {
        if (id == null || id.isBlank())
            throw new IllegalArgumentException("ID do utilizador não pode ser vazio.");
        if (nome == null || nome.isBlank())
            throw new IllegalArgumentException("Nome do utilizador não pode ser vazio.");
        if (email == null || !email.contains("@"))
            throw new IllegalArgumentException("Email inválido.");
        if (password == null || password.length() < 4)
            throw new IllegalArgumentException("Password deve ter pelo menos 4 caracteres.");

        this.id = id;
        this.nome = nome;
        this.email = email;
        this.password = password;
        this.casas = new LinkedHashMap<>();
    }

    // -------------------------------------------------------------------------
    // Gestão de casas
    // -------------------------------------------------------------------------

    public void adicionarCasa(Casa casa, TipoUtilizador tipo) {
        if (casa == null) throw new IllegalArgumentException("Casa não pode ser nula.");
        casas.put(casa, tipo);
    }

    public void removerCasa(Casa casa) throws CasaNaoEncontradaException {
        if (!casas.containsKey(casa))
            throw new CasaNaoEncontradaException("O utilizador não tem acesso a esta casa.");
        casas.remove(casa);
    }

    public TipoUtilizador getTipoNaCasa(Casa casa) throws CasaNaoEncontradaException {
        if (!casas.containsKey(casa))
            throw new CasaNaoEncontradaException("O utilizador não tem acesso à casa '" + casa.getNome() + "'.");
        return casas.get(casa);
    }

    public boolean isAdministradorDe(Casa casa) {
        return TipoUtilizador.ADMINISTRADOR.equals(casas.get(casa));
    }

    public boolean temAcessoA(Casa casa) {
        return casas.containsKey(casa);
    }

    public Set<Casa> getCasas() {
        return Collections.unmodifiableSet(casas.keySet());
    }

    // -------------------------------------------------------------------------
    // Autenticação
    // -------------------------------------------------------------------------

    public boolean autenticar(String passwordFornecida) {
        return this.password.equals(passwordFornecida);
    }

    // -------------------------------------------------------------------------
    // Getters / Setters
    // -------------------------------------------------------------------------

    public String getId()     { return id; }
    public String getNome()   { return nome; }
    public String getEmail()  { return email; }

    public void setNome(String nome) {
        if (nome == null || nome.isBlank())
            throw new IllegalArgumentException("Nome não pode ser vazio.");
        this.nome = nome;
    }

    public void setEmail(String email) {
        if (email == null || !email.contains("@"))
            throw new IllegalArgumentException("Email inválido.");
        this.email = email;
    }

    public void setPassword(String password) {
        if (password == null || password.length() < 4)
            throw new IllegalArgumentException("Password deve ter pelo menos 4 caracteres.");
        this.password = password;
    }

    // -------------------------------------------------------------------------
    // toString / equals / hashCode
    // -------------------------------------------------------------------------

    @Override
    public String toString() {
        return String.format("Utilizador [%s] %s <%s> | Casas: %d",
                id, nome, email, casas.size());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Utilizador)) return false;
        return id.equals(((Utilizador) o).id);
    }

    @Override
    public int hashCode() { return id.hashCode(); }
}
