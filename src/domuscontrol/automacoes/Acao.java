package domuscontrol.automacoes;

import java.io.Serializable;

/**
 * Representa uma açao a executar sobre um dispositivo.
 * Todas as açoes devem implementar esta interface.
 */
public interface Acao extends Serializable {
    /**
     * Executa a açao.
     */
    void executar();

    /**
     * Descrição legível da açao.
     */
    String descrever();
}
