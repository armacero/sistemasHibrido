/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sistemaHibrido;

import java.util.ArrayList;
import java.util.*;

/**
 *
 * @author Armando BG
 */
public class principal {
public ArrayList<int[][]> arrayCombinaciones = new ArrayList<int[][]>();
public int[][] patronesEntrada;
public int[] valores = new int[5];
public int neurona=0, cont=0;
public static void main(String[] args)
{ 
    
    
    
    
}

public void generar_combinaciones(int num_competencias[])
{
    
    int renglon1,renglon2, columna,seguir=0,k2=0;
    int competencias=8, etiqueta=3 ;
    int tamaño= competencias*etiqueta;
    int[] V_etiquetas = new int[tamaño];
   
    //por competencias
    for (int i = 0; i < num_competencias.length; i++) {
        int[][] combinaciones = new int[tamaño][tamaño];
        renglon1 = num_competencias[i];
        //renglon2 = num_competencias[i]+(num_competencias[i]-1);
        renglon2=num_competencias[i]-1;
        columna = num_competencias[i];
        for (int j = 0; j < renglon1; j++) {
            for (int k = 0; k < columna; k++) {
                if (k==j) {
                    combinaciones[j][k]=1;
                    
                }
                else
                {
                    combinaciones[j][k]=(-1);
                }
                
            }
            seguir++;
        }
        seguir++;
         for (int j = 0; j < renglon2; j++) {
            for (int k = 0; k < columna; k++) {
                if (k==j) {
                    combinaciones[j+seguir][k]=1;
                    combinaciones[j+seguir][k+1]=1;
                    k2=k+2;
                }
                else
                {
                    combinaciones[j+seguir][k2]=(-1);
                }
                k2++;
            }
            k2=0;
        }
        arrayCombinaciones.add(combinaciones);
    }
}
public int neuronas(int num_competencias[])   
{
    int suma=0;
    for (int i = 0; i < num_competencias.length; i++) {
        suma=suma+num_competencias[i]+(num_competencias[i]-1);
    }
    return suma;
}


public void generar_PatronesEntrada()
{
    int valor=neuronas(valores);
    int noPatrones = arrayCombinaciones.size()*valor;
    patronesEntrada = new int[noPatrones][noPatrones];
    
    for (int i = 0; i < 10; i++) {
         int[][] competencia = arrayCombinaciones.get(i);
         //recorrer patrones e insercion
        for (int j = 0; j < noPatrones; j++) {
            for (int k = 0; k < competencia.length; k++) {
                for (int l = 0; l < competencia[k].length; l++) {
                    patronesEntrada[i][neurona]=competencia[cont][l];
                    neurona++;
                }
                //neurona=0;
               competencia = arrayCombinaciones.get(k);
            }
            cont=0;
        }
        
        
    }
}
    
}