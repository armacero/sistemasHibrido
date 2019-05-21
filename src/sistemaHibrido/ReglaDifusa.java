package sistemaHibrido;
import sistemaexpertodifuso.*;
import java.util.ArrayList;
public class ReglaDifusa {
    int llave;
    ArrayList<VariableConjunto> antecedentes;
    VariableConjunto consecuente;

    public ReglaDifusa() {
    }
    public ReglaDifusa(int llave){
        this.llave = llave;
        antecedentes = new ArrayList<>();
    } 

    public int getLlave() {
        return llave;
    }

    public void setLlave(int llave) {
        this.llave = llave;
    }

    public ArrayList<VariableConjunto> getAntecedentes() {
        return antecedentes;
    }

    public void setAntecedentes(ArrayList<VariableConjunto> antecedentes) {
        this.antecedentes = antecedentes;
    }

    public VariableConjunto getConsecuente() {
        return consecuente;
    }

    public void setConsecuente(VariableConjunto consecuente) {
        this.consecuente = consecuente;
    }
    
    
    
    
    
    
    
}
