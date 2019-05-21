package sistemaexpertodifuso;

import java.io.IOException;
import java.util.List;

public class Centroide {
    static final Double error = 0.05;
    VariablesLinguisticas variablesLinguisticas;

    Centroide(VariablesLinguisticas variablesLinguisticas) {
        this.variablesLinguisticas = variablesLinguisticas;
    }
	
    public Double procesar(List<ResultadoDifuso> resultados) throws IOException {
        Double numerador = 0.0;
        Double denominador = 0.0;

        for (Double i = 0.0; i <= 100; i += error) {
            Double valor = maximoValor(resultados, i);

            numerador += i * valor;
            denominador += valor;
            System.out.println(i+" "+valor);
        }

        return numerador / denominador;
    }

    private Double maximoValor(List<ResultadoDifuso> resultados, Double valor) throws IOException {
        /* Maximo valor entre todos los conjuntos */
        Double maximo = 0.0;

        for (ResultadoDifuso resultado: resultados) {
            ResultadoDifuso conjuntoResultado = resultado.variableConjunto.getConjunto(variablesLinguisticas).evaluar(valor);
            Double valorConjunto = resultado.valor*conjuntoResultado.valor;
            
            maximo = Math.max(maximo, valorConjunto);
        }

        return maximo;
    }
}
