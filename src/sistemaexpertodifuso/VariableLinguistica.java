package sistemaexpertodifuso;

class VariableLinguistica {
    int llave;
    String nombre;
    Conjunto[] conjuntos;

    public VariableLinguistica() {
        conjuntos = new Conjunto[8];
    }

    public String muestraVariableLinguistica() {
        int i, j;
        String variable = "";
        variable += this.llave;
        variable += "  Nombre: " + nombre + "\n";
        variable += "Conjuntos:";
        for (i = 0; i < 8; i++) {
            if (conjuntos[i] != null) {
                variable += "\nNombre: " + conjuntos[i].nombre;
                variable += " Puntos críticos:";
                for (j = 0; j < 4; j++) {
                    if (conjuntos[i].puntosCriticos[j] != null && conjuntos[i].puntosCriticos[j].y != -1) {
                        variable += " (" + conjuntos[i].puntosCriticos[j].x + "," + conjuntos[i].puntosCriticos[j].y + ")";
                    }
                }
            }
        }
        return variable;
    }

    public int obtenLLaveVariable() {
        return this.llave;
    }

    public String obtenNombreVar() {
        return this.nombre;
    }
}