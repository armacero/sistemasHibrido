package sistemaHibrido;

import sistemaexpertodifuso.*;

class Arbol {
    Nodo raiz = null;
    boolean borrar(int llave){
        Nodo recorre, anterior, origen, aEliminar;
        if(raiz != null){
            if(raiz.llave == llave){
                if(raiz.derecha == null && raiz.izquierda == null){
                    raiz = null;
                }
                else{
                    if(raiz.derecha != null && raiz.izquierda != null){
                        //recorre a la izquierda
                        anterior = raiz;
                        recorre = raiz.derecha;
                        while(recorre.izquierda != null){
                            anterior = recorre;
                            recorre = recorre.izquierda;
                        }
                        if(anterior == raiz){
                            anterior.derecha = null;
                        }
                        else{
                            anterior.izquierda = null;
                        }
                        if(recorre.derecha != null){
                            if(anterior == raiz){
                                anterior.derecha = recorre.derecha;
                            }
                            else{
                                anterior.izquierda = recorre.derecha;
                            }
                        }
                        recorre.izquierda = raiz.izquierda;
                        recorre.derecha = raiz.derecha;
                        raiz = recorre;
                    }
                    else{
                        if(raiz.derecha != null){
                            raiz = raiz.derecha;
                        }
                        if(raiz.izquierda != null){
                            raiz = raiz.izquierda;
                        }
                    }
                }
            }
            else{
                recorre = raiz;
                anterior = raiz;
                while(recorre.llave != llave && recorre != null){
                    anterior = recorre;
                    if(recorre.llave > llave){
                        recorre = recorre.izquierda;
                    }
                    else{
                        recorre = recorre.derecha;
                    }
                }
                if(recorre == null){
                    return false;
                }
                else{
                    origen = anterior;
                    aEliminar = recorre;
                    if(aEliminar.derecha == null && aEliminar.izquierda == null){
                        if(origen.llave > llave){
                            origen.izquierda = null;
                        }
                        else{
                            origen.derecha = null;
                        }
                    }
                    else{
                        if(aEliminar.derecha != null && aEliminar.izquierda != null){
                            //recorre a la izquierda
                            anterior = aEliminar;
                            recorre = aEliminar.derecha;
                            while(recorre.izquierda != null){
                                anterior = recorre;
                                recorre = recorre.izquierda;
                            }
                            if(anterior == aEliminar){
                                anterior.derecha = null;
                            }
                            else{
                                anterior.izquierda = null;
                            }
                            if(recorre.derecha != null){
                                if(anterior == aEliminar){
                                    anterior.derecha = recorre.derecha;
                                }
                                else{
                                    anterior.izquierda = recorre.derecha;
                                }
                            }
                            recorre.izquierda = aEliminar.izquierda;
                            recorre.derecha = aEliminar.derecha;
                            if(origen.llave > llave){
                                origen.izquierda = recorre;
                            }
                            else{
                                origen.derecha = recorre;
                            }
                        }
                        else{
                            if(aEliminar.derecha != null){
                                if(origen.llave > llave){
                                    origen.izquierda = aEliminar.derecha;
                                }
                                else{
                                    origen.derecha = aEliminar.derecha;
                                }
                            }
                            if(aEliminar.izquierda != null){
                                if(origen.llave > llave){
                                    origen.izquierda = aEliminar.izquierda;
                                }
                                else{
                                    origen.derecha = aEliminar.izquierda;
                                }
                            }
                        }
                    }
                }
            }
        }
        else{
            return false;
        }
        return true;
    }
    int buscar(int llave){
        int direccion = -1;
        Nodo recorre, anterior;
        if(raiz != null){
            recorre = raiz;
            anterior = raiz;
            while(anterior.llave != llave && recorre != null){
                anterior = recorre;
                if(recorre.llave > llave){
                    recorre = recorre.izquierda;
                }
                else{
                    recorre = recorre.derecha;
                }
            }
            if(anterior.llave == llave){
                direccion = anterior.direccion;
            }
        }
        return direccion;
    }
    void insertar(int llave, int direccion){
        Nodo nuevo, anterior = null, recorre;
        if(raiz == null){
            raiz = new Nodo(llave, direccion);
        }
        else{
            nuevo = new Nodo(llave, direccion);
            recorre = raiz;
            while(recorre != null){
                anterior = recorre;
                if(recorre.llave > nuevo.llave){
                    recorre = recorre.izquierda;
                }
                else{
                    recorre = recorre.derecha;
                }
            }
            if(anterior.llave > nuevo.llave){
                anterior.izquierda = nuevo;
            }
            else{
                anterior.derecha = nuevo;
            }
        }
    }
}
