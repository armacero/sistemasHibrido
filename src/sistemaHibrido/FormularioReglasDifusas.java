package sistemaHibrido;

import sistemaexpertodifuso.*;
import java.awt.BorderLayout;
import static java.awt.Component.LEFT_ALIGNMENT;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;

public class FormularioReglasDifusas extends JDialog{
    ArchivoReglas reglasDifusas;
    VariablesLinguisticas variablesLinguisticas;
    ReglaDifusa regla;
    JPanel panelRegla, panelBotones, panelAntecedentes, panelConsecuente;
    boolean actualiza;
    ArrayList<JTextField> inputsVariables, inputsConjuntos;
    ArrayList<JPanel> panelesAntecedentes;
    JTextField inputConjuntoConsecuente, inputVariableConsecuente;
    private void creaAntecedente(boolean primero){
        JPanel panel = new JPanel();
        panelesAntecedentes.add(panel);
        if(primero){
            panel.add(new JLabel("(Variable:"));
        }
        else{
            panel.add(new JLabel("^(Variable:"));
        }
        JTextField text = new JTextField();
        text.setPreferredSize(new Dimension(100, 30));
        inputsVariables.add(text);
        panel.add(text);
        panel.add(new JLabel(" Conjunto:"));
        text = new JTextField();
        text.setPreferredSize(new Dimension(100, 30));
        inputsConjuntos.add(text);
        panel.add(text);
        panel.add(new JLabel(")"));
        panelAntecedentes.add(panel);
    }
    private void crearPanelBotones(){
        JButton agregaAntecedente;
        agregaAntecedente = new JButton("Agrega antecedente");
        agregaAntecedente.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                creaAntecedente(false);
                panelAntecedentes.revalidate();
                panelAntecedentes.repaint();
            }
        });
        panelBotones.add(agregaAntecedente);
        JButton eliminaAntecedente;
        eliminaAntecedente = new JButton("Elimina antecedente");
        eliminaAntecedente.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                panelAntecedentes.remove(panelesAntecedentes.get(panelesAntecedentes.size()-1));
                panelesAntecedentes.remove(panelesAntecedentes.size()-1);
                inputsVariables.remove(inputsVariables.size()-1);
                inputsConjuntos.remove(inputsConjuntos.size()-1);
                panelAntecedentes.revalidate();
                panelAntecedentes.repaint();
            }
        });
        panelBotones.add(eliminaAntecedente);
        if(!actualiza){
            JButton agregaRegla;
            agregaRegla = new JButton("Agregar regla");
            agregaRegla.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent ae){
                    try {
                        llenaRegla();
                        reglasDifusas.insertar(regla);
                        dispose();
                    } catch (IOException ex) {
                        Logger.getLogger(FormularioReglasDifusas.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            });
            panelBotones.add(agregaRegla);
        }
        else{
            JButton agregaRegla;
            agregaRegla = new JButton("Actualizar regla");
            agregaRegla.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent ae){
                    try {
                        reglasDifusas.elimina(regla.llave);
                        llenaRegla();
                        reglasDifusas.insertar(regla);
                        dispose();
                    } catch (IOException ex) {
                        Logger.getLogger(FormularioReglasDifusas.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            });
            panelBotones.add(agregaRegla);
        }
    }
    private void crearPanelRegla() throws IOException{
        int i;
        panelRegla.setLayout(new BorderLayout());
        panelAntecedentes = new JPanel();
        panelRegla.add(panelAntecedentes, BorderLayout.CENTER);
        panelConsecuente = new JPanel();
        
        panelRegla.add(panelConsecuente, BorderLayout.SOUTH);
        panelConsecuente.add(new JLabel("->(Variable:"));
        JTextField text = new JTextField();
        text.setPreferredSize(new Dimension(100, 30));
        inputVariableConsecuente = text;
        panelConsecuente.add(text);
        panelConsecuente.add(new JLabel(" Conjunto:"));
        text = new JTextField();
        text.setPreferredSize(new Dimension(100, 30));
        inputConjuntoConsecuente = text;
        panelConsecuente.add(text);
        panelConsecuente.add(new JLabel(")"));
        if(!actualiza){
            for(i = 0; i < variablesLinguisticas.recuperarSecuencial().length-1; i++){
                creaAntecedente(true);
            }
        }
        else{
            for(i = 0; i < regla.antecedentes.size(); i++){
                creaAntecedente(true);
            }
            for(i = 0; i < regla.antecedentes.size(); i++){
                inputsVariables.get(i).setText(regla.antecedentes.get(i).llaveVariableLiguistica+"");
                inputsConjuntos.get(i).setText(regla.antecedentes.get(i).llaveConjunto+"");
            }
            inputVariableConsecuente.setText(regla.consecuente.llaveVariableLiguistica+"");
            inputConjuntoConsecuente.setText(regla.consecuente.llaveConjunto+"");
        }
    }
    
    private void llenaRegla(){
        regla.antecedentes.clear();
        for(int i = 0; i < panelesAntecedentes.size(); i++){
            regla.antecedentes.add(new VariableConjunto(Integer.parseInt(inputsVariables.get(i).getText()), Integer.parseInt(inputsConjuntos.get(i).getText())));
        }
        regla.consecuente = new VariableConjunto(Integer.parseInt(inputVariableConsecuente.getText()), Integer.parseInt(inputConjuntoConsecuente.getText()));
    }
    
    public FormularioReglasDifusas(ReglaDifusa regla, ArchivoReglas reglasDifusas, boolean actualiza, VariablesLinguisticas variablesLinguisticas) throws IOException{
        this.reglasDifusas = reglasDifusas;
        this.actualiza = actualiza;
        this.regla = regla;
        this.variablesLinguisticas = variablesLinguisticas;
        inputsVariables = new ArrayList<>();
        inputsConjuntos = new ArrayList<>();
        panelesAntecedentes = new ArrayList<>();
        Container cp = getContentPane();
        setSize(1350, 600);
        setTitle("Regla difusa "+regla.llave);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());

        panelRegla = new JPanel();
        crearPanelRegla();
        JScrollPane codeScrollPane = new JScrollPane(panelRegla);
        cp.add(codeScrollPane, BorderLayout.CENTER);

        panelBotones = new JPanel();
        crearPanelBotones();
        JScrollPane messagesScrollPane = new JScrollPane(panelBotones);
        messagesScrollPane.setMinimumSize(new Dimension(100, 200));
        messagesScrollPane.setPreferredSize(new Dimension(100, 200));
        messagesScrollPane.setAlignmentX(LEFT_ALIGNMENT);
        cp.add(messagesScrollPane, BorderLayout.SOUTH);

        setVisible(true);
    }
}
