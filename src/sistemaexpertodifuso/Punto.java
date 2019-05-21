package sistemaexpertodifuso;
class Punto{
    Double x,y;

    public Punto() {}
    
    Punto(boolean vacio){
        x = -1.0;
        y = -1.0;
    }
    
    public boolean esVacio() {
    	return x == -1.0 || y == -1.0;
    }
}
