/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sistemaHibrido;

/**
 *
 * @author joseh
 */
public class TDA_TAM_VAR {

    int id_varLin;
    int numConjuntos;

    public TDA_TAM_VAR(int id_varLin, int numConjuntos) {
        this.id_varLin = id_varLin;
        this.numConjuntos = numConjuntos;
    }
    
    public int getId_varLin() {
        return id_varLin;
    }

    public void setId_varLin(int id_varLin) {
        this.id_varLin = id_varLin;
    }

    public int getNumConjuntos() {
        return numConjuntos;
    }

    public void setNumConjuntos(int numConjuntos) {
        this.numConjuntos = numConjuntos;
    }

}
