package domuscontrol.automacoes;

import domuscontrol.dispositivos.Sensor;

/**
 * Condição: valor de um sensor abaixo de um limiar.
 * Ex: luminosidade < 100 lux → ligar lâmpadas.
 */
class CondicaoSensorAbaixo implements Condicao {
    private final Sensor sensor;
    private final double limiar;

    public CondicaoSensorAbaixo(Sensor sensor, double limiar) {
        this.sensor = sensor;
        this.limiar = limiar;
    }

    @Override
    public boolean avaliar() {
        return sensor.estaLigado() && sensor.getValor() < limiar;
    }

    @Override
    public String descrever() {
        return sensor.getTipoSensor() + " < " + limiar + " " + sensor.getUnidade();
    }
}

/**
 * Condição: valor de um sensor acima de um limiar.
 * Ex: pluviosidade > 5 mm/h → fechar cortinas.
 */
class CondicaoSensorAcima implements Condicao {
    private final Sensor sensor;
    private final double limiar;

    public CondicaoSensorAcima(Sensor sensor, double limiar) {
        this.sensor = sensor;
        this.limiar = limiar;
    }

    @Override
    public boolean avaliar() {
        return sensor.estaLigado() && sensor.getValor() > limiar;
    }

    @Override
    public String descrever() {
        return sensor.getTipoSensor() + " > " + limiar + " " + sensor.getUnidade();
    }
}

/**
 * Condição composta: AND de duas condições.
 */
class CondicaoE implements Condicao {
    private final Condicao esquerda;
    private final Condicao direita;

    public CondicaoE(Condicao esquerda, Condicao direita) {
        this.esquerda = esquerda;
        this.direita = direita;
    }

    @Override
    public boolean avaliar() {
        return esquerda.avaliar() && direita.avaliar();
    }

    @Override
    public String descrever() {
        return "(" + esquerda.descrever() + ") E (" + direita.descrever() + ")";
    }
}

/**
 * Condição composta: OR de duas condições.
 */
class CondicaoOu implements Condicao {
    private final Condicao esquerda;
    private final Condicao direita;

    public CondicaoOu(Condicao esquerda, Condicao direita) {
        this.esquerda = esquerda;
        this.direita = direita;
    }

    @Override
    public boolean avaliar() {
        return esquerda.avaliar() || direita.avaliar();
    }

    @Override
    public String descrever() {
        return "(" + esquerda.descrever() + ") OU (" + direita.descrever() + ")";
    }
}

/**
 * Fábrica de condições.
 */
public class FabricaCondicoes {

    private FabricaCondicoes() {}

    public static Condicao sensorAbaixoDe(Sensor s, double limiar) {
        return new CondicaoSensorAbaixo(s, limiar);
    }

    public static Condicao sensorAcimaDe(Sensor s, double limiar) {
        return new CondicaoSensorAcima(s, limiar);
    }

    public static Condicao e(Condicao a, Condicao b) {
        return new CondicaoE(a, b);
    }

    public static Condicao ou(Condicao a, Condicao b) {
        return new CondicaoOu(a, b);
    }
}
