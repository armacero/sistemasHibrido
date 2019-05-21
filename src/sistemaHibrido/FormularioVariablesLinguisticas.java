package sistemaHibrido;

import sistemaexpertodifuso.*;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class FormularioVariablesLinguisticas extends JDialog{
    JButton button;
    JPanel panelesConjuntos[];
    JTextField inputNombreVL, nombresConjuntos[], puntos[][][];
    public FormularioVariablesLinguisticas(VariableLinguistica variableLinguistica, VariablesLinguisticas variablesLinguisticas, boolean actualiza){
        int i, j;
        Container cp = getContentPane();
        String nombreVL = "", nombreConjunto, x, y;
        cp.setLayout(new BoxLayout(cp, BoxLayout.PAGE_AXIS));
        panelesConjuntos = new JPanel[8];
        nombresConjuntos = new JTextField[8];
        puntos = new JTextField[8][4][2];
        if(actualiza){
            nombreVL = variableLinguistica.nombre;
        }
        inputNombreVL = new JTextField(nombreVL);
        inputNombreVL.setPreferredSize(new Dimension(100, 30));
        cp.add(new JLabel("Nombre de variable lingüistica:"));
        cp.add(inputNombreVL);
        for(i = 0; i < 8; i++){
            panelesConjuntos[i] = new JPanel();
            panelesConjuntos[i].setLayout(new BoxLayout(panelesConjuntos[i], BoxLayout.LINE_AXIS));
            panelesConjuntos[i].add(new JLabel("Nombre del conjunto "+i+":"));
            nombreConjunto = "";
            if(actualiza && variableLinguistica.conjuntos[i] != null){
                nombreConjunto = variableLinguistica.conjuntos[i].nombre.trim();
            }
            nombresConjuntos[i] = new JTextField(nombreConjunto);
            nombresConjuntos[i].setPreferredSize(new Dimension(300, 30));
            panelesConjuntos[i].add(nombresConjuntos[i]);
            
            panelesConjuntos[i].add(new JLabel("Puntos críticos:"));
            for(j = 0; j < 4; j++){
                panelesConjuntos[i].add(new JLabel(" ("));
                x = "";
                if(actualiza && variableLinguistica.conjuntos[i] != null && variableLinguistica.conjuntos[i].puntosCriticos[j] != null && variableLinguistica.conjuntos[i].puntosCriticos[j].y != -1){
                    x = variableLinguistica.conjuntos[i].puntosCriticos[j].x+"";
                }
                puntos[i][j][0] = new JTextField(x);
                puntos[i][j][0].setPreferredSize(new Dimension(100, 30));
                panelesConjuntos[i].add(puntos[i][j][0]);
                panelesConjuntos[i].add(new JLabel(","));
                y = "";
                if(actualiza && variableLinguistica.conjuntos[i] != null && variableLinguistica.conjuntos[i].puntosCriticos[j] != null && variableLinguistica.conjuntos[i].puntosCriticos[j].y != -1){
                    y = variableLinguistica.conjuntos[i].puntosCriticos[j].y+"";
                }
                puntos[i][j][1] = new JTextField(y);
                puntos[i][j][1].setPreferredSize(new Dimension(100, 30));
                panelesConjuntos[i].add(puntos[i][j][1]);
                panelesConjuntos[i].add(new JLabel(")"));
            }
            
            cp.add(panelesConjuntos[i]);
        }
        
        button = new JButton("Terminar");
        button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                try {
                    StringBuffer buffer;
                    int i, j;
                    String nombreConjunto;
                    buffer = new StringBuffer(inputNombreVL.getText());
                    buffer.setLength(50);
                    variableLinguistica.nombre = buffer.toString();
                    
                    for(i = 0; i < 8; i++){
                        buffer = new StringBuffer(nombresConjuntos[i].getText());
                        buffer.setLength(20);
                        nombreConjunto = buffer.toString();
                        if(nombreConjunto.trim().length() > 0){
                            variableLinguistica.conjuntos[i] = new Conjunto();
                            variableLinguistica.conjuntos[i].nombre = nombreConjunto;
                            variableLinguistica.conjuntos[i].variableConjunto = new VariableConjunto(variableLinguistica.llave, i);
                            
                            for(j = 0; j < 4; j++){
                                if(puntos[i][j][0].getText().length() > 0 && puntos[i][j][1].getText().length() > 0){
                                    variableLinguistica.conjuntos[i].puntosCriticos[j] = new Punto();
                                    variableLinguistica.conjuntos[i].puntosCriticos[j].x = Double.parseDouble(puntos[i][j][0].getText());
                                    variableLinguistica.conjuntos[i].puntosCriticos[j].y = Double.parseDouble(puntos[i][j][1].getText());
                                }
                                else{
                                    variableLinguistica.conjuntos[i].puntosCriticos[j] = null;
                                }
                            }
                        }
                        else{
                            variableLinguistica.conjuntos[i] = null;
                        }
                    }
                    
                    if(actualiza){
                        variablesLinguisticas.actualizar(variableLinguistica);
                    }
                    else{
                        variablesLinguisticas.insertar(variableLinguistica);
                    }
                    dispose();
                } catch (IOException ex) {
                    Logger.getLogger(FormularioVariablesLinguisticas.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
        cp.add(button);
        
        setPreferredSize(new Dimension(600, 600));
        setTitle((actualiza ? "Actualizar "+variableLinguistica.llave : "Crear "+variableLinguistica.llave));
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        pack();
        setVisible(true);
    }
}