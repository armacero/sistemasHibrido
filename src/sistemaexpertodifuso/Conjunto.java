package sistemaexpertodifuso;
class Conjunto{
	VariableConjunto variableConjunto;
    String nombre;
    Punto[] puntosCriticos;

    public Conjunto(){
        puntosCriticos = new Punto[4];
    }
    
    public Conjunto(boolean vacio){
        int i;
        nombre = "                    ";
        puntosCriticos = new Punto[4];
        for(i = 0; i < 4; i++){
            puntosCriticos[i] = new Punto(true);
        }
    }
    
    public ResultadoDifuso evaluar(Double x) {
    	int limit = puntosCriticos.length - 1;
    	
    	for (int i = 0; i < limit; i++) {
    		Punto primer = puntosCriticos[i];
    		Punto segundo = puntosCriticos[i + 1];
    		
			if (!primer.esVacio() && !segundo.esVacio() && primer.x <= x && x <= segundo.x) {
				return new ResultadoDifuso(evaluarRecta(primer, segundo, x), variableConjunto);                 
			}        
		}
    	
    	return new ResultadoDifuso(0.0, variableConjunto);
    }
    
    private Double evaluarRecta(Punto primer, Punto segundo, double x) {
    	Double pendiente = (segundo.y - primer.y) / (segundo.x - primer.x);
    	
    	return primer.y + pendiente * (x - primer.x);
    }
}
