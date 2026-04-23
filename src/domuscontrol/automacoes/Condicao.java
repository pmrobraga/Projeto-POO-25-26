package domuscontrol.automacoes;

import java.io.Serializable;

/**
 * Representa uma condição que pode ser avaliada (verdadeiro/falso).
 * Usada pelas automações para decidir quando atuar.
 */
public interface Condicao extends Serializable {
    /**
     * Avalia a condição.
     * @return true se a condição se verificar, false caso contrário.
     */
    boolean avaliar();

    /**
     * Descrição legível da condição.
     */
    String descrever();
}
