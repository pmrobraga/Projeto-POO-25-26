package domuscontrol.ui;

import domuscontrol.casa.Casa;
import domuscontrol.utilizadores.TipoUtilizador;
import domuscontrol.utilizadores.Utilizador;

/**
 * Representa a sessão do utilizador autenticado.
 * Guarda quem está logado e fornece métodos de verificação de permissões.
 */
public class Sessao {

    private Utilizador utilizadorActual;

    public Sessao() {
        this.utilizadorActual = null;
    }

    // Login / Logout

    public void login(Utilizador u) {
        this.utilizadorActual = u;
    }

    public void logout() {
        this.utilizadorActual = null;
    }

    public boolean estaAutenticado() {
        return utilizadorActual != null;
    }

    // Permissões

    public boolean isAdminDe(Casa casa) {
        if (!estaAutenticado()) return false;
        return utilizadorActual.isAdministradorDe(casa);
    }

    public boolean temAcessoA(Casa casa) {
        if (!estaAutenticado()) return false;
        return utilizadorActual.temAcessoA(casa);
    }

    public boolean isAdminEmAlgumaCasa() {
        if (!estaAutenticado()) return false;
        return utilizadorActual.getCasas().stream()
                .anyMatch(c -> {
                    try { return utilizadorActual.getTipoNaCasa(c) == TipoUtilizador.ADMINISTRADOR; }
                    catch (Exception e) { return false; }
                });
    }

    // Getter

    public Utilizador getUtilizador() {
        return utilizadorActual;
    }

    public String getNome() {
        return utilizadorActual != null ? utilizadorActual.getNome() : "Nao autenticado";
    }
}
