package domuscontrol.dispositivos;

/**
 * Interface que define o contrato para qualquer dispositivo operável.
 * Todos os dispositivos devem saber ligar, desligar e reportar o seu estado.
 */
public interface Operavel {
    void ligar();
    void desligar();
    boolean estaLigado();
    String getEstadoDetalhado();
}
