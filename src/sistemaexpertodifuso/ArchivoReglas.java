package sistemaexpertodifuso;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.StringTokenizer;
import javax.swing.JOptionPane;

public class ArchivoReglas {

    ArrayList<ReglaDifusa> arrayReglasDifusas = new ArrayList<ReglaDifusa>();

    private void escribeReglaDifusa(ReglaDifusa aEscribir, RandomAccessFile raf) throws IOException {
        VariableConjunto vc;
        raf.writeInt(aEscribir.llave);
        for (int i = 0; i < aEscribir.antecedentes.size(); i++) {
            vc = aEscribir.antecedentes.get(i);
            raf.writeInt(vc.llaveVariableLiguistica);
            raf.writeInt(vc.llaveConjunto);
        }
        raf.writeInt(aEscribir.consecuente.llaveVariableLiguistica);
        raf.writeInt(aEscribir.consecuente.llaveConjunto);
        raf.writeInt(-1);
    }

    public boolean existe(int llave) throws IOException {
        boolean existeArchivo = true, ultimoEraVacio = true, encontrado = false;
        int actual;
        RandomAccessFile lector = null;
        try {
            lector = new RandomAccessFile("reglasDifusas", "r");
        } catch (FileNotFoundException e) {
            existeArchivo = false;
        }
        if (existeArchivo) {
            while (lector.getFilePointer() != lector.length() && !encontrado) {
                actual = lector.readInt();
                if (ultimoEraVacio && actual == llave) {
                    encontrado = true;
                }
                if (actual == -1) {
                    ultimoEraVacio = true;
                } else {
                    ultimoEraVacio = false;
                }
            }
            lector.close();
            return encontrado;
        } else {
            return false;
        }
    }

    public void insertar(ReglaDifusa regla) throws IOException {
        boolean existe = true;
        RandomAccessFile lector = null, escritorIndice, escritor;
        try {
            lector = new RandomAccessFile("reglasDifusas", "r");
        } catch (FileNotFoundException e) {
            existe = false;
        }
        if (existe) {
            lector.close();
            escritor = new RandomAccessFile("reglasDifusas", "rw");
            escritor.seek(escritor.length());
            escribeReglaDifusa(regla, escritor);
            escritor.close();
        } else {
            escritor = new RandomAccessFile("reglasDifusas", "rw");
            escribeReglaDifusa(regla, escritor);
            escritor.close();
        }
    }

    public String muestra_reglasDifusas() throws IOException {
        long ap_actual, ap_final;
        int elemento, elemento2;
        String salida = "";
        boolean existeArchivo = true;
        RandomAccessFile lector = null;
        try {
            lector = new RandomAccessFile("reglasDifusas", "r");
        } catch (FileNotFoundException w) {
            existeArchivo = false;
        }
        if (existeArchivo) {
            //String salida = "\nReglas difusas\n---------------------------\n";
            RandomAccessFile leer_archi = new RandomAccessFile("reglasDifusas", "r");
            while ((ap_actual = leer_archi.getFilePointer()) != (ap_final = leer_archi.length())) {
                elemento = leer_archi.readInt();
                if (elemento == -1) {
                    salida += elemento + "\n";
                } else {
                    salida += elemento + "";
                }
            }//Fin while
        } else {
            JOptionPane.showMessageDialog(null, "No existe el archivo, agregue reglas difusas para crearlo");
        }

        m_ProcCadena(salida);

        return (salida);
    }

    public String m_remplaza(String p_cadena) {

        p_cadena = p_cadena.trim().replace("-1", "*");
        p_cadena = p_cadena.trim().replace(" ", "");
        System.out.println(p_cadena);

        return p_cadena;
    }

    public void m_ProcCadena(String p_cadena) {

        StringTokenizer st = new StringTokenizer(m_remplaza(p_cadena), "*");
        int cont = 0;
        while (st.hasMoreTokens()) {
            ReglaDifusa objRegla = new ReglaDifusa();
            ArrayList<VariableConjunto> arrayVariableConjuntos = new ArrayList<>();
            VariableConjunto consecuente = new VariableConjunto();

            String regla = null;
            String conse;   
            regla = st.nextToken();
            int cons1, cons2, llave;
            conse = regla.trim().substring(regla.trim().length() - 2, regla.trim().length());
            char[] consecue = conse.toCharArray();
            String c1 = Character.toString(consecue[0]);
            String c2 = Character.toString(consecue[1]);

            cons1 = Integer.valueOf(c1);
            cons2 = Integer.valueOf(c2);

            regla = regla.trim().replaceFirst("[\\s\\S]{0,2}$", "");

            //Obtener llave
            if (cont < 9) {
                char key = regla.trim().charAt(0);
                String key2 = Character.toString(key);
                llave = Integer.parseInt(key2);
                //System.out.println("LLave: " + llave);
                regla = regla.trim().substring(1);
                objRegla.setLlave(llave);
            } else if (cont >= 9 && cont <= 99) {
                String key2 = regla.trim().substring(0, 2);
                llave = Integer.parseInt(key2);
                // System.out.println("LLave: " + llave);
                regla = regla.trim().substring(2);
                objRegla.setLlave(llave);
            } else if (cont >= 100 && cont <= 999) {
                String key2 = regla.trim().substring(0, 3);
                llave = Integer.parseInt(key2);
                //System.out.println("LLave: " + llave);
                regla = regla.trim().substring(3);
                objRegla.setLlave(llave);
            } else if (cont >= 1000 && cont < 10000) {
                String key2 = regla.trim().substring(0, 4);
                llave = Integer.parseInt(key2);
                //System.out.println("LLave: " + llave);
                regla = regla.trim().substring(4);
                objRegla.setLlave(llave);
            }
            regla = regla.trim().replaceAll("(?s).{" + 2 + "}(?!$)", "$0" + ",");
            String[] parts = regla.trim().split(",");

            for (String part : parts) {
                char keyVL = part.charAt(0);
                char keyCon = part.charAt(1);
                String KeyVL = Character.toString(keyVL);
                String KeyCon = Character.toString(keyCon);
                int VL = Integer.parseInt(KeyVL);
                int CON = Integer.parseInt(KeyCon);
                VariableConjunto variableConjunto = new VariableConjunto(VL, CON);

                arrayVariableConjuntos.add(variableConjunto);
            }

            consecuente.setLlaveVariableLiguistica(cons1);
            consecuente.setLlaveConjunto(cons2);

            objRegla.setConsecuente(consecuente);
            objRegla.setAntecedentes(arrayVariableConjuntos);
            arrayReglasDifusas.add(objRegla);

            cont++;
        }

        m_muestraArrayReglasDifusas(arrayReglasDifusas);

    }

    public void m_muestraArrayReglasDifusas(ArrayList<ReglaDifusa> p_arraDifusas) {
        String rule = "";
        for (int i = 0; i < p_arraDifusas.size(); i++) {
            rule="";
            ReglaDifusa rd = p_arraDifusas.get(i);
            System.out.println("\n--------------------------");
            System.out.println("Llave: " + rd.getLlave());
            rule+=rd.getLlave()+ " ";
            VariableConjunto vc, vc2;
            System.out.println("Antecedentes:");
            for (int j = 0; j < rd.antecedentes.size(); j++) {
                vc = rd.antecedentes.get(j);
                System.out.print(vc.llaveVariableLiguistica);
                System.out.print("" + vc.llaveConjunto + " ");
                rule+=vc.llaveVariableLiguistica + "" + vc.llaveConjunto + " ";
            }
            vc2 = rd.consecuente;
            System.out.print("\nConsecuente de la regla\n");
            System.out.print( vc2.llaveVariableLiguistica + ""+  vc2.llaveConjunto);
            System.out.println("\nRegla completa");
            rule+=vc2.llaveVariableLiguistica + ""+  vc2.llaveConjunto;
            
            System.out.println(rule);
        }

    }

    ReglaDifusa[] recuperarTodo() throws FileNotFoundException, IOException {
        ArrayList<ReglaDifusa> reglas = new ArrayList<>();
        ReglaDifusa regla;
        boolean existe = true;
        RandomAccessFile lector = null;
        int ultimo;
        try {
            lector = new RandomAccessFile("reglasDifusas", "r");
        } catch (FileNotFoundException e) {
            existe = false;
        }
        if (existe) {
            while(lector.getFilePointer() != lector.length()){
                ultimo = lector.readInt();
                if(ultimo != -1){
                    regla = new ReglaDifusa(ultimo);
                    reglas.add(regla);
                    leeReglaDifusa(regla, lector);
                }
            }
            lector.close();
        }
        else{
            return new ReglaDifusa[0];
        }
        ReglaDifusa[] reglasDifusas = new ReglaDifusa[reglas.size()];
        for(int i = 0; i < reglas.size(); i++){
            reglasDifusas[i] = reglas.get(i);
        }
        return reglasDifusas;
    }

    private void leeReglaDifusa(ReglaDifusa regla, RandomAccessFile lector) throws IOException {
        boolean sigueLeyendo = true;
        int llaveVariable;
        while(sigueLeyendo){
            llaveVariable = lector.readInt();
            if(llaveVariable != -1){
                regla.antecedentes.add(new VariableConjunto(llaveVariable, lector.readInt()));
            }
            else{
                sigueLeyendo = false;
            }
        }
        regla.consecuente = regla.antecedentes.remove(regla.antecedentes.size()-1);
    }

    String mostrarTodo() throws IOException {
        ReglaDifusa[] todasLasReglas = recuperarTodo();
        String respuesta = "";
        for(int i = 0; i < todasLasReglas.length; i++){
            respuesta += "\nLlave: "+todasLasReglas[i].llave+", regla: ";
            for(int j = 0; j < todasLasReglas[i].antecedentes.size(); j++){
                if(j > 0){
                    respuesta += "^";
                }
                respuesta += "("+todasLasReglas[i].antecedentes.get(j).llaveVariableLiguistica+","+todasLasReglas[i].antecedentes.get(j).llaveConjunto+")";
            }
            respuesta += "->("+todasLasReglas[i].consecuente.llaveVariableLiguistica+","+todasLasReglas[i].consecuente.llaveConjunto+")";
        }
        return respuesta;
    }

    void elimina(int llave) throws IOException {
        boolean ultimoEraVacio = true, eliminado = false;
        long ultimo;
        int actual;
        RandomAccessFile lector = null;
        lector = new RandomAccessFile("reglasDifusas", "rw");
        while(!eliminado){
            ultimo = lector.getFilePointer();
            actual = lector.readInt();
            if(ultimoEraVacio && actual == llave){
                while(actual != -1){
                    lector.seek(ultimo);
                    lector.writeInt(-1);
                    ultimo = lector.getFilePointer();
                    actual = lector.readInt();
                }
                eliminado = true;
            }
            if(actual == -1){
                ultimoEraVacio = true;
            }
            else {
                ultimoEraVacio = false;
            }
        }
        lector.close();
    }

    void importar(File file, int llave) throws FileNotFoundException, IOException {
        Scanner sc;
        int i;
        String[] elementos = null;
        ReglaDifusa regla;
        sc = new Scanner(file, "UTF-8");
        while(sc.hasNext()){
            elementos = sc.nextLine().split(",");
            regla = new ReglaDifusa(llave);
            llave++;
            for(i = 1; i < elementos.length; i++){
                regla.antecedentes.add(new VariableConjunto(i, Integer.parseInt(elementos[i-1])));
            }
            regla.consecuente = new VariableConjunto(i, Integer.parseInt(elementos[i-1]));
            insertar(regla);
        }
    }
}
