package sistemaexpertodifuso;

import java.io.IOException;

public class VariableConjunto {
    int llaveVariableLiguistica, llaveConjunto;
    Conjunto conjunto;

    public VariableConjunto() {
    }

    public VariableConjunto(int llaveVariableLiguistica, int llaveConjunto) {
        this.llaveVariableLiguistica = llaveVariableLiguistica;
        this.llaveConjunto = llaveConjunto;
    }

    public int getLlaveVariableLiguistica() {
        return llaveVariableLiguistica;
    }

    public void setLlaveVariableLiguistica(int llaveVariableLiguistica) {
        this.llaveVariableLiguistica = llaveVariableLiguistica;
    }

    public int getLlaveConjunto() {
        return llaveConjunto;
    }

    public void setLlaveConjunto(int llaveConjunto) {
        this.llaveConjunto = llaveConjunto;
    }
    
    public String getClave() {
    	return llaveVariableLiguistica + "," + llaveConjunto;
    }
    
    public Conjunto getConjunto(VariablesLinguisticas variablesLinguisticas) throws IOException {
    	if(conjunto == null){
            conjunto = variablesLinguisticas.recuperarAleatorio(llaveVariableLiguistica).conjuntos[llaveConjunto];
    	}
    	return conjunto; 
    }
}
