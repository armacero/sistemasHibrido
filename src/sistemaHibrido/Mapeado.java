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
public class Mapeado {
    int id_VarLinguis;
    int p_patron[];

    public Mapeado(int id_VarLinguis, int[] p_patron) {
        this.id_VarLinguis = id_VarLinguis;
        this.p_patron = p_patron;
    }

    public Mapeado(int[] p_patron) {
        this.p_patron = p_patron;
    }

    
    
    public int getId_VarLinguis() {
        return id_VarLinguis;
    }

    public void setId_VarLinguis(int id_VarLinguis) {
        this.id_VarLinguis = id_VarLinguis;
    }

    public int[] getP_patron() {
        return p_patron;
    }

    public void setP_patron(int[] p_patron) {
        this.p_patron = p_patron;
    }
    
    
    
    
}
