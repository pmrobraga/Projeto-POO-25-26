package domuscontrol.utilizadores;

/**
 * Define o tipo de acesso que um utilizador tem numa determinada casa.
 */
public enum TipoUtilizador {
    ADMINISTRADOR,  // Pode criar divisões, adicionar dispositivos, etc.
    UTILIZADOR      // Apenas pode operar dispositivos e criar cenários/automações pessoais
}
