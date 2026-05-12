package domuscontrol.automacoes;

import java.io.Serializable;

/**
 * Representa uma condiçao que pode ser avaliada (verdadeiro/falso)
 * Usada pelas automaçoes para decidir quando atuar
 */
public interface Condicao extends Serializable {
    /**
     * Avalia a condiçao
     * @return true se a condição se verificar, false caso contrário
     */
    boolean avaliar();

    /**
     * Descriçao legível da condiçao
     */
    String descrever();
}
