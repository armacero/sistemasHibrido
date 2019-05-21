package sistemaexpertodifuso;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Hashtable;
import java.util.List;

public class MaxMin {
	public static ArrayList<ResultadoDifuso> procesar(List<ReglaDifusa> reglas, List<ResultadoDifuso> resultados) {		
		Hashtable<Integer, ResultadoDifuso> hash = new Hashtable<Integer, ResultadoDifuso>();
		
		for (ReglaDifusa regla : reglas) {
			ResultadoDifuso resultadoNuevo = min(regla, resultados);
			System.out.println(resultadoNuevo.valor);
			ResultadoDifuso resultadoViejo = hash.get(regla.consecuente.getClave().hashCode());
			
			/* Verifico si ya procese ese consecuente antes */
			if (resultadoViejo != null) {
				System.out.println(resultadoNuevo.valor + ">" + resultadoViejo.valor);
				if (resultadoNuevo.valor > resultadoViejo.valor) {
					hash.put(regla.consecuente.getClave().hashCode(), resultadoNuevo);
				}
			} else {
				hash.put(regla.consecuente.getClave().hashCode(), resultadoNuevo);
			}
		}
		
		return new ArrayList<ResultadoDifuso>(hash.values());
	}
	
	private static ResultadoDifuso min(ReglaDifusa regla, List<ResultadoDifuso> resultados) {
		Double minValor = 1.0;
		
		for (VariableConjunto variable : regla.antecedentes) {
			Double nuevoValor = matchearResultados(variable, resultados);
			
			if (minValor > nuevoValor) {
				minValor = nuevoValor;
			}
		}
		
		return new ResultadoDifuso(minValor, regla.consecuente);
	}
	
	private static Double matchearResultados(VariableConjunto variable, List<ResultadoDifuso> resultados) {
		for (ResultadoDifuso resultadoDifuso : resultados) {
			if (resultadoDifuso.variableConjunto.getClave().equals(variable.getClave())) {
				return resultadoDifuso.valor;
			}
		}
		
		return 0.0;
	}
}
