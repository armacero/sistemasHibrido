package sistemaexpertodifuso;

import java.io.FileNotFoundException;
import java.io.IOException;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

public class SistemaExpertoDifuso {
    public static void main(String[] args) throws IOException, FileNotFoundException, ClassNotFoundException, InstantiationException, IllegalAccessException, UnsupportedLookAndFeelException {
        UIManager.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel");
        VariablesLinguisticas variablesLinguisticas = new VariablesLinguisticas();
        ArchivoReglas archivoReglas = new ArchivoReglas();
        new GUI(variablesLinguisticas, archivoReglas);
    }
}
