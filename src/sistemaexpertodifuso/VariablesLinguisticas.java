package sistemaexpertodifuso;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import javax.swing.JOptionPane;

public class VariablesLinguisticas{
    private final int registerLength;
    private Arbol arbol;
    private int direccionSiguiente, direccionActual, borrados, desbordados, ordenados, direccionReorganizados;
    private Indice indice;
    VariablesLinguisticas() throws IOException, FileNotFoundException, ClassNotFoundException {
        registerLength = 936;
        indice = new Indice();
        recuperarArbol();
        recuperarControl();
    }
    public void actualizar(VariableLinguistica variableLinguistica) throws FileNotFoundException, IOException{
        RandomAccessFile escritor;
        variableLinguistica.llave = indice.llave;
        indice.direccion = arbol.buscar(indice.llave);
        if(indice.direccion == -1){
            JOptionPane.showMessageDialog(null, "No hay una variable linguistica con la llave "+indice.llave);
        }
        else{
            escritor = new RandomAccessFile("maestroVariableLinguistica", "rw");
            escritor.seek(indice.direccion*registerLength);
            escribeVariableLinguistica(variableLinguistica, escritor);
            escritor.close();
        }
    }
    public void borrar(int llave) throws FileNotFoundException, IOException, ClassNotFoundException{
        int i;
        RandomAccessFile escritor;
        VariableLinguistica variableLinguistica = new VariableLinguistica();
        indice.llave = variableLinguistica.llave = llave;
        indice.direccion = arbol.buscar(indice.llave);
        if(indice.direccion == -1){
            JOptionPane.showMessageDialog(null, "No hay una variable linguistica con la llave "+indice.llave);
        }
        else{
            variableLinguistica.llave = 0;
            variableLinguistica.nombre = null;
            for(i = 0; i < 8; i++){
                variableLinguistica.conjuntos[i] = null;
            }
            escritor = new RandomAccessFile("maestroVariableLinguistica", "rw");
            escritor.seek(indice.direccion*registerLength);
            escribeVariableLinguistica(variableLinguistica, escritor);
            escritor.close();
            marcarIndice(indice.llave);
            arbol.borrar(llave);
            borrados++;
            reescribirControl();
        }
    }
    private void escribeIndice(Indice aEscribir, RandomAccessFile raf) throws IOException{
        raf.writeInt(aEscribir.llave);
        raf.writeInt(aEscribir.direccion);
    }
    private void escribeVariableLinguistica(VariableLinguistica aEscribir, RandomAccessFile raf) throws IOException{
        int i, j;
        String nombre;
        raf.writeInt(aEscribir.llave);
        if(aEscribir.nombre == null){
            nombre = "";
        }
        else{
            nombre = aEscribir.nombre;
        }
        while(nombre.length() < 50){
            nombre += " ";
        }
        raf.writeChars(nombre);
        for(i = 0; i < 8; i++){
            if(aEscribir.conjuntos[i] == null){
                aEscribir.conjuntos[i] = new Conjunto(true);
                aEscribir.conjuntos[i].variableConjunto = new VariableConjunto(aEscribir.llave, i);
            }
            
            nombre = aEscribir.conjuntos[i].nombre;
            while(nombre.length() < 20){
                nombre += " ";
            }
            raf.writeChars(nombre);
            for(j = 0; j < 4; j++){
                if(aEscribir.conjuntos[i].puntosCriticos[j] == null){
                    aEscribir.conjuntos[i].puntosCriticos[j] = new Punto(true);
                }
                raf.writeDouble(aEscribir.conjuntos[i].puntosCriticos[j].x);
                raf.writeDouble(aEscribir.conjuntos[i].puntosCriticos[j].y);
            }
        }
    }
    public void insertar(VariableLinguistica variableLinguistica) throws IOException{
        boolean existe = true;
        RandomAccessFile lector = null, escritorIndice, escritor;
        indice.llave = variableLinguistica.llave;
        indice.direccion = direccionSiguiente;
        if(arbol.buscar(variableLinguistica.llave) != -1){
            JOptionPane.showMessageDialog(null, "La clave ya existe");
        }
        else{
            try{
                lector = new RandomAccessFile("maestroVariableLinguistica", "r");
            }
            catch(FileNotFoundException e){
                existe = false;
            }
            if(existe){
                lector.close();
                escritor = new RandomAccessFile("maestroVariableLinguistica", "rw");
                escritorIndice = new RandomAccessFile("indiceVariableLinguistica", "rw");
                escritor.seek(escritor.length());
                escribeVariableLinguistica(variableLinguistica, escritor);
                escritorIndice.seek(escritorIndice.length());
                escribeIndice(indice, escritorIndice);
                //System.out.println(escritor.getFilePointer());
                escritor.close();
                escritorIndice.close();
                desbordados++;
            }
            else{
                escritor = new RandomAccessFile("maestroVariableLinguistica", "rw");
                escritorIndice = new RandomAccessFile("indiceVariableLinguistica", "rw");
                escribeVariableLinguistica(variableLinguistica, escritor);
                //System.out.println(escritor.getFilePointer());
                escribeIndice(indice, escritorIndice);
                escritor.close();
                escritorIndice.close();
                ordenados++;
            }
            arbol.insertar(variableLinguistica.llave, direccionSiguiente);
            direccionSiguiente++;
            reescribirControl();
        }
    }
    private void marcarIndice(int llave) throws IOException, ClassNotFoundException{
        boolean existe = true;
        RandomAccessFile lector = null;
        long apuntadorFinal, ultimoApuntador;
        boolean marcado = false;
        try{
            lector = new RandomAccessFile("indiceVariableLinguistica", "rw");
        }
        catch(FileNotFoundException e){
            existe = false;
        }
        if(existe){
            apuntadorFinal = lector.length();
            while((ultimoApuntador = lector.getFilePointer()) != apuntadorFinal && !marcado){
                indice = recuperaIndice(lector);
                if(indice.llave == llave){
                    lector.seek(ultimoApuntador);
                    indice.direccion = -1;
                    escribeIndice(indice, lector);
                }
            }
            lector.close();
        }
    }
    private void recorreArbol(Nodo nodo, RandomAccessFile raf, ArrayList<VariableLinguistica> arreglo) throws IOException{
        VariableLinguistica variableLinguistica;
        if(nodo.izquierda != null){
            recorreArbol(nodo.izquierda, raf, arreglo);
        }
        if(direccionActual != nodo.direccion){
            raf.seek(nodo.direccion*registerLength);
            direccionActual = nodo.direccion;
        }
        variableLinguistica = recuperaVariableLinguistica(raf);
        arreglo.add(variableLinguistica);
        direccionActual++;
        if(nodo.derecha != null){
            recorreArbol(nodo.derecha, raf, arreglo);
        }
    }
    private void recorreArbolReestructurar(Nodo nodo, RandomAccessFile lector, RandomAccessFile escritor) throws IOException{
        VariableLinguistica variableLinguistica;
        if(nodo.izquierda != null){
            recorreArbolReestructurar(nodo.izquierda, lector, escritor);
        }
        if(direccionActual != nodo.direccion){
            lector.seek(nodo.direccion*registerLength);
            direccionActual = nodo.direccion;
        }
        variableLinguistica = recuperaVariableLinguistica(lector);
        escribeVariableLinguistica(variableLinguistica, escritor);
        direccionActual++;
        nodo.direccion = direccionReorganizados;
        direccionReorganizados++;
        if(nodo.derecha != null){
            recorreArbolReestructurar(nodo.derecha, lector, escritor);
        }
    }
    public VariableLinguistica recuperarAleatorio(int llave) throws FileNotFoundException, IOException{
        VariableLinguistica variableLinguistica = null;
        RandomAccessFile lector = null;
        indice.llave = llave;
        indice.direccion = arbol.buscar(indice.llave);
        if(indice.direccion == -1){
            //JOptionPane.showMessageDialog(null, "No hay una variableLinguistica con la llave "+indice.llave);
        }
        else{
            lector = new RandomAccessFile("maestroVariableLinguistica", "r");
            lector.seek(indice.direccion*registerLength);
            variableLinguistica = recuperaVariableLinguistica(lector);
            lector.close();
        }
        return variableLinguistica;
    }
    private void recuperarArbol() throws IOException{
        boolean existe = true;
        RandomAccessFile lector = null;
        arbol = new Arbol();
        try{
            lector = new RandomAccessFile("indiceVariableLinguistica", "r");
        }
        catch(FileNotFoundException e){
            existe = false;
        }
        if(existe){
            while(lector.getFilePointer() != lector.length()){
                indice = recuperaIndice(lector);
                if(indice.direccion != -1){
                    arbol.insertar(indice.llave, indice.direccion);
                }
            }
            lector.close();
        }
    }
    private void recuperarControl() throws IOException{
        boolean existe = true;
        RandomAccessFile lector = null;
        try{
            lector = new RandomAccessFile("controlVariableLinguistica", "r");
        }
        catch(FileNotFoundException e){
            existe = false;
        }
        if(existe){
            direccionSiguiente = lector.readInt();
            borrados = lector.readInt();
            desbordados = lector.readInt();
            ordenados = lector.readInt();
            lector.close();
        }
        else{
            direccionSiguiente = borrados = desbordados = ordenados = 0;
        }
    }
    private Indice recuperaIndice(RandomAccessFile raf) throws IOException{
        Indice i = new Indice();
        i.llave = raf.readInt();
        i.direccion = raf.readInt();
        return i;
    }
    private VariableLinguistica recuperaVariableLinguistica(RandomAccessFile lector) throws IOException{
        int i, j, c;
        char nombreVL[] = new char[50];
        char nombreConjunto[] = new char[20];
        VariableLinguistica l = new VariableLinguistica();
        
        l.llave = lector.readInt();
        
        for(c = 0; c < nombreVL.length; c++){
            nombreVL[c] = lector.readChar();
        }
        l.nombre = (new String(nombreVL).replace('\0', ' ')).trim();
        
        for(i = 0; i < 8; i++){
            l.conjuntos[i] = new Conjunto();
            l.conjuntos[i].variableConjunto = new VariableConjunto(l.llave, i);
            
            for(c = 0; c < nombreConjunto.length; c++){
                nombreConjunto[c] = lector.readChar();
            }
            l.conjuntos[i].nombre = (new String(nombreConjunto).replace('\0', ' ')).trim();
            
            for(j = 0; j < 4; j++){
                l.conjuntos[i].puntosCriticos[j] = new Punto();
                l.conjuntos[i].puntosCriticos[j].x = lector.readDouble();
                l.conjuntos[i].puntosCriticos[j].y = lector.readDouble();
            }
            
            if(l.conjuntos[i].nombre.length() == 0){
                l.conjuntos[i] = null;
            }
        }
        return l;
    }
    public VariableLinguistica[] recuperarSecuencial() throws FileNotFoundException, IOException{
        VariableLinguistica[] variableLinguisticas;
        boolean existe = true;
        RandomAccessFile lector = null;
        direccionActual = 0;
        ArrayList<VariableLinguistica> arreglo = new ArrayList<VariableLinguistica>();
        try{
            lector = new RandomAccessFile("maestroVariableLinguistica", "r");
        }
        catch(FileNotFoundException e){
            existe = false;
        }
        if(existe){
            if(arbol.raiz != null){
                recorreArbol(arbol.raiz, lector, arreglo);
            }
            lector.close();
        }
        variableLinguisticas = new VariableLinguistica[arreglo.size()];
        for(int i = 0; i < arreglo.size(); i++){
            variableLinguisticas[i] = arreglo.get(i);
        }
        return variableLinguisticas;
    }
    private void reescribirControl() throws IOException{
        boolean existe = true;
        RandomAccessFile lector = null;
        try{
            lector = new RandomAccessFile("controlVariableLinguistica", "rw");
        }
        catch(FileNotFoundException e){
            existe = false;
        }
        if(existe){
            lector.writeInt(direccionSiguiente);
            lector.writeInt(borrados);
            lector.writeInt(desbordados);
            lector.writeInt(ordenados);
            lector.close();
        }
        if(0.4*ordenados < borrados+desbordados){
            reestructurar();
        }
    }
    private void reestructurar() throws IOException{
        boolean existe = true;
        RandomAccessFile lector = null, escritor;
        File file, newName;
        direccionReorganizados = direccionActual = 0;
        try{
            lector = new RandomAccessFile("maestroVariableLinguistica", "r");
        }
        catch(FileNotFoundException e){
            existe = false;
        }
        if(existe){
            escritor = new RandomAccessFile("tmp", "rw");
            if(arbol.raiz != null){
                recorreArbolReestructurar(arbol.raiz, lector, escritor);
            }
            lector.close();
            escritor.close();
            file = new File("tmp");
            newName = new File("maestroVariableLinguistica");
            newName.delete();
            file.renameTo(newName);
            
            lector = new RandomAccessFile("indiceVariableLinguistica", "rw");
            escritor = new RandomAccessFile("tmp", "rw");
            while(lector.getFilePointer() != lector.length()){
                indice = recuperaIndice(lector);
                indice.direccion = arbol.buscar(indice.llave);
                if(indice.direccion != -1){
                    escribeIndice(indice, escritor);
                }
            }
            lector.close();
            escritor.close();
            file = new File("tmp");
            newName = new File("indiceVariableLinguistica");
            newName.delete();
            file.renameTo(newName);
        }
        ordenados = ordenados+desbordados-borrados;
        direccionSiguiente = ordenados;
        desbordados = borrados = 0;
        reescribirControl();
    }
}
