package sistemaHibrido;

import sistemaexpertodifuso.*;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.io.RandomAccessFile;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JButton;
import javax.swing.event.ChangeListener;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSlider;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.event.ChangeEvent;

public class GUI extends JFrame {

    VariablesLinguisticas variablesLinguisticas;
    ArchivoReglas reglasDifusas;
    JTextArea messages;
    JPanel panelVariables, pnlSliders;
    static JLabel valors;
    JTextField[] valores;
    JLabel[] llaves;
    JLabel[] nombres;
    JSlider[] sliders;
    int variableLinguisticaResultado;
    int variableLinguisticaResultadoRedNeuronal;
    double ortogonalidad = 0;

    public ArrayList<int[][]> arrayCombinaciones = new ArrayList<int[][]>();
    public int[] patronGenerado;
    public int neurona = 0, cont = 0, tamañoVector = 0;
    public ArrayList<int[]> patronesDeEntrada = new ArrayList<>();
    public ArrayList<Integer> PatronEntrada = new ArrayList<>();
    private int[][] matrizIdentidad;
    ArrayList<int[][]> restapixpiT_I = new ArrayList<>();
    public int[][] matrizPesos;
    private int[] vectorSalida;
    private sistemaHibrido.VariableConjunto var_conjunto;

    ReglaDifusa reglaDifusaResul;


    /*
      ____ _   _ ___ 
     / ___| | | |_ _|
    | |  _| | | || | 
    | |_| | |_| || | 
     \____|\___/|___|*/
    GUI(VariablesLinguisticas variablesLinguisticas, ArchivoReglas reglasDifusas) throws IOException {
        Container cp = getContentPane();
        recuperaVariableLinguisticaResultado(cp);
        this.variablesLinguisticas = variablesLinguisticas;
        this.reglasDifusas = reglasDifusas;
        setSize(900, 650);
        setTitle("Sistema experto neuro-difuso");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        cp.add(createMenu(cp), BorderLayout.NORTH);

        messages = new JTextArea("Mensajes");
        messages.setEnabled(false);
        messages.setBackground(Color.black);
        JScrollPane messagesScrollPane = new JScrollPane(messages);
        messagesScrollPane.setMinimumSize(new Dimension(100, 200));
        messagesScrollPane.setPreferredSize(new Dimension(100, 200));
        messagesScrollPane.setAlignmentX(LEFT_ALIGNMENT);
        cp.add(messagesScrollPane, BorderLayout.SOUTH);

        setVisible(true);
    }

    /*
     __  __              __  
    |  \/  | ___ _ __  _/_/_ 
    | |\/| |/ _ \ '_ \| | | |
    | |  | |  __/ | | | |_| |
    |_|  |_|\___|_| |_|\__,_|*/
    JMenuBar createMenu(Container cp) throws IOException {
        JMenuBar menuBar;
        JMenu menuVariablesLinguisticas, menuFuzzy, menuReglas, menuRedNeuronal;
        JMenuItem menuItem;
        menuBar = new JMenuBar();
        menuVariablesLinguisticas = new JMenu("Variables lingüisticas");
        menuFuzzy = new JMenu("Inferencia Difusa");
        menuReglas = new JMenu("Reglas difusas");

        menuBar.add(menuVariablesLinguisticas);
        menuBar.add(menuFuzzy);
        menuBar.add(menuReglas);

        //Variables lingüisticas-------------------------------------------------------------------------------------------------------------------------------------------------------------------
        menuItem = new JMenuItem("Mostrar las variables lingüisticas");
        menuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                VariableLinguistica variables[] = null;
                int i;
                String reglas = "";
                try {
                    variables = variablesLinguisticas.recuperarSecuencial();
                } catch (IOException ex) {
                    Logger.getLogger(GUI.class.getName()).log(Level.SEVERE, null, ex);
                }
                if (variables != null) {
                    for (i = 0; i < variables.length; i++) {
                        reglas += variables[i].muestraVariableLinguistica() + "\n";
                    }
                } else {
                    reglas = "Hubo un error al recuperar las variables lingüisticas.";
                }
                JTextArea textArea = new JTextArea(reglas);
                JScrollPane scrollPane = new JScrollPane(textArea);
                textArea.setLineWrap(true);
                textArea.setWrapStyleWord(true);
                scrollPane.setPreferredSize(new Dimension(500, 500));
                JOptionPane.showMessageDialog(null, scrollPane, "Variables Linguisticas",
                        JOptionPane.YES_NO_OPTION);

                //JOptionPane.showMessageDialog(cp, reglas);
            }
        });
        menuVariablesLinguisticas.add(menuItem);

        menuItem = new JMenuItem("Actualizar variable lingüistica");
        menuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                try {
                    int llave = Integer.parseInt(JOptionPane.showInputDialog(cp, "Llave de la variable lingüistica a actualizar:", JOptionPane.INPUT_VALUE_PROPERTY));
                    VariableLinguistica variable = variablesLinguisticas.recuperarAleatorio(llave);
                    if (variable != null) {
                        new FormularioVariablesLinguisticas(variable, variablesLinguisticas, true);
                    } else {
                        JOptionPane.showMessageDialog(cp, "No existe una las variable lingüistica con esa llave.");
                    }
                } catch (IOException ex) {
                    Logger.getLogger(GUI.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
        menuVariablesLinguisticas.add(menuItem);

        menuItem = new JMenuItem("Nueva variable lingüistica");
        menuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                try {
                    int llave = Integer.parseInt(JOptionPane.showInputDialog(cp, "Llave de la nueva variable lingüistica", JOptionPane.INPUT_VALUE_PROPERTY));
                    VariableLinguistica variable = variablesLinguisticas.recuperarAleatorio(llave);
                    if (variable == null) {
                        variable = new VariableLinguistica();
                        variable.llave = llave;
                        new FormularioVariablesLinguisticas(variable, variablesLinguisticas, false);
                    } else {
                        JOptionPane.showMessageDialog(cp, "Ya existe una variable lingüistica con esa llave.");
                    }
                } catch (IOException ex) {
                    Logger.getLogger(GUI.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
        menuVariablesLinguisticas.add(menuItem);

        menuItem = new JMenuItem("Borrar variable lingüistica");
        menuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                try {
                    int llave = Integer.parseInt(JOptionPane.showInputDialog(cp, "Llave de la variable lingüistica que se va a borrar", JOptionPane.INPUT_VALUE_PROPERTY));
                    VariableLinguistica variable = variablesLinguisticas.recuperarAleatorio(llave);
                    if (variable != null) {
                        variablesLinguisticas.borrar(llave);
                    } else {
                        JOptionPane.showMessageDialog(cp, "No existe una variable lingüistica con esa llave.");
                    }
                } catch (IOException ex) {
                    Logger.getLogger(GUI.class.getName()).log(Level.SEVERE, null, ex);
                } catch (ClassNotFoundException ex) {
                    Logger.getLogger(GUI.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
        menuVariablesLinguisticas.add(menuItem);

        menuItem = new JMenuItem("Especificar VL del resultado");
        menuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                try {
                    RandomAccessFile escritor;
                    escritor = new RandomAccessFile("variableResultado", "rw");
                    variableLinguisticaResultado = Integer.parseInt(JOptionPane.showInputDialog(cp, "Llave de la variable difusa que indica el resultado", JOptionPane.INPUT_VALUE_PROPERTY));
                    escritor.writeInt(variableLinguisticaResultado);
                    escritor.close();
                } catch (FileNotFoundException ex) {
                    Logger.getLogger(GUI.class.getName()).log(Level.SEVERE, null, ex);
                } catch (IOException ex) {
                    Logger.getLogger(GUI.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
        );
        menuVariablesLinguisticas.add(menuItem);

        //Evaluación-------------------------------------------------------------------------------------------------------------------------------------------------------------------
        menuItem = new JMenuItem("Evaluar variable lingüistica");
        menuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    valors = new JLabel("");
                    int valor_de_entrada = 0;
                    int llave = Integer.parseInt(JOptionPane.showInputDialog(cp, "Llave de la variable linguistica a evaluar:", JOptionPane.INPUT_VALUE_PROPERTY));
                    VariableLinguistica variable = variablesLinguisticas.recuperarAleatorio(llave);
                    if (variable != null) {
                        JOptionPane optionPane = new JOptionPane();

                        JSlider slider = getSlider(optionPane);
                        optionPane.setMessage(new Object[]{"Ingresa el valor a evaluar:", slider});
                        optionPane.setMessageType(JOptionPane.QUESTION_MESSAGE);
                        optionPane.setOptionType(JOptionPane.OK_CANCEL_OPTION);
                        optionPane.add(valors);
                        JDialog dialog = optionPane.createDialog(cp, "Difuzificar:" + variable.nombre);
                        dialog.setVisible(true);
                        valor_de_entrada = slider.getValue();

                        messages.append("\nVariable linguistica a evaluar:" + variable.nombre + "\n");
                        messages.append("Valor de entrada:" + valor_de_entrada + "\n");
                        messages.append("-----------------------------------------\n");

                        for (int i = 0; i < 8; i++) {
                            if (variable.conjuntos[i] != null) {

                                messages.append("Nombre: " + variable.conjuntos[i].nombre + "\n");
                                messages.append("Puntos Criticos:\n");
                                for (int j = 0; j < 4; j++) {
                                    if (variable.conjuntos[i].puntosCriticos[j] != null && variable.conjuntos[i].puntosCriticos[j].y != -1) {
                                        messages.append(variable.conjuntos[i].puntosCriticos[j].x + "," + variable.conjuntos[i].puntosCriticos[j].y + "\n");
                                    }
                                }
                                messages.append("Grado de membresia: " + variable.conjuntos[i].evaluar((double) (valor_de_entrada)).valor + "\n");
                                messages.append("\n");
                                messages.append("------------------------------------------------------\n");
                            }
                        }
                    } else {
                        JOptionPane.showMessageDialog(cp, "No existe una variable lingüistica con esa llave.");
                    }
                } catch (IOException ex) {
                    Logger.getLogger(GUI.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
        menuFuzzy.add(menuItem);

        menuItem = new JMenuItem("Inferir");
        menuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                //arrayResultadosDifusos = new ArrayList<>();
                //Obtener datos de las variables linguisticas
                VariableLinguistica variables[] = null;
                VariablesLinguisticas var = null;
                int num_var;
                JButton btnDifuzificar = null;
                try {
                    variables = variablesLinguisticas.recuperarSecuencial();
                } catch (IOException ex) {
                    Logger.getLogger(GUI.class.getName()).log(Level.SEVERE, null, ex);
                }
                num_var = variables.length;

                for (int i = 0; i < variables.length; i++) {

                    System.out.println(variables[i].obtenLLaveVariable());
                    System.out.println(variables[i].obtenNombreVar());

                }

                JLabel[] llaves = new JLabel[num_var];
                JLabel[] nombres = new JLabel[num_var];
                JSlider[] sliders = new JSlider[num_var];

                valores = new JTextField[num_var];

                Map<JSlider, JTextField> fieldMap;
                fieldMap = new HashMap<>();

                JPanel pnlVarLin = new JPanel(new GridLayout(num_var, 4));

                for (int i = 0; i <= variables.length - 1; i++) {//ciclo para crear, añadir, establecer propiedades a los botones
                    llaves[i] = new JLabel("" + variables[i].obtenLLaveVariable());
                    nombres[i] = new JLabel(variables[i].obtenNombreVar());

                    sliders[i] = new JSlider(0, 100);
                    sliders[i].setMajorTickSpacing(10);
                    sliders[i].setPaintTicks(true);
                    sliders[i].setPaintLabels(true);

                    valores[i] = new JTextField("50");
                    fieldMap.put(sliders[i], valores[i]);
                    sliders[i].addChangeListener(new ChangeListener() {
                        @Override
                        public void stateChanged(ChangeEvent e) {
                            JSlider slider = (JSlider) e.getSource();
                            JTextField field = fieldMap.get(slider);
                            field.setText(Integer.toString(slider.getValue()));
                        }
                    });

                    if (Integer.parseInt(llaves[i].getText()) == variableLinguisticaResultado) {

                    } else {
                        pnlVarLin.add(llaves[i]);
                        pnlVarLin.add(nombres[i]);
                        pnlVarLin.add(sliders[i]);
                        pnlVarLin.add(valores[i]);
                    }

                }
                Container cp = getContentPane();
                btnDifuzificar = new JButton("Inferir");

                panelVariables = new JPanel();
                JScrollPane codeScrollPane = new JScrollPane(pnlVarLin);
                cp.add(codeScrollPane, BorderLayout.CENTER);
                cp.add(btnDifuzificar, BorderLayout.EAST);

                btnDifuzificar.addActionListener(new ActionListener() {

                    VariableLinguistica vls = new VariableLinguistica();

                    @Override
                    public void actionPerformed(ActionEvent e) {
                        //eliminarArchivoReglasDifusas();

                        try {
                            ArrayList<ResultadoDifuso> arrayResultadosDifusos = new ArrayList<>();
                            VariableLinguistica variable = null;
                            for (int i = 0; i < valores.length; i++) {
                                variable = variablesLinguisticas.recuperarAleatorio(Integer.parseInt(llaves[i].getText()));
                                double valor_entrada = (double) Integer.parseInt(valores[i].getText());
                                messages.append("\nVariable linguistica a evaluar:" + variable.nombre + "\n");
                                messages.append("Llave de la variable linguistica:" + variable.llave + "\n");
                                messages.append("Valor de entrada:" + valor_entrada + "\n");
                                messages.append("-----------------------------------------\n");

                                for (int j = 0; j < 8; j++) {
                                    if (variable.conjuntos[j] != null) {

                                        messages.append("Nombre: " + variable.conjuntos[j].nombre + "\n");
                                        messages.append("Puntos Criticos:\n");
                                        for (int k = 0; k < 4; k++) {
                                            if (variable.conjuntos[j].puntosCriticos[k] != null && variable.conjuntos[j].puntosCriticos[k].y != -1) {
                                                messages.append(variable.conjuntos[j].puntosCriticos[k].x + "," + variable.conjuntos[j].puntosCriticos[k].y + "\n");
                                            }
                                        }

                                        ResultadoDifuso objResultadoDifuso = new ResultadoDifuso();
                                        objResultadoDifuso = variable.conjuntos[j].evaluar((double) (valor_entrada));

                                        objResultadoDifuso.variableConjunto = new VariableConjunto(variable.llave, j);

                                        arrayResultadosDifusos.add(objResultadoDifuso);

                                        if (variable.conjuntos[j].evaluar((double) (valor_entrada)).valor > 0) {
                                            PatronEntrada.add(1);
                                        } else {
                                            PatronEntrada.add(-1);
                                        }

                                        messages.append("Grado de membresia: " + variable.conjuntos[j].evaluar((double) (valor_entrada)).valor + "\n");
                                        messages.append("\n");
                                        messages.append("------------------------------------------------------\n");
                                    }

                                }
                            }
                            //ArchivoReglas archivo = new ArchivoReglas();
                            // Metodos del modelo neuronal 
                            m_procesoHopfield();

                            List<ResultadoDifuso> resultados = MaxMin.procesar(Arrays.asList(reglasDifusas.recuperarTodo()), arrayResultadosDifusos);

                            resultados.stream().map((resultadoDifuso) -> {
                                messages.append("Difusificación: Llave variable linguistica: " + resultadoDifuso.variableConjunto.llaveVariableLiguistica + "\n");
                                return resultadoDifuso;
                            }).map((resultadoDifuso) -> {
                                messages.append("Difusificación: Llave del conjunto: " + resultadoDifuso.variableConjunto.llaveConjunto + "\n");
                                return resultadoDifuso;
                            }).forEachOrdered((resultadoDifuso) -> {
                                messages.append("Difusificación: Resultado difuso: " + resultadoDifuso.valor + "\n\n");
                            });
                            messages.append("Resultados difusos \n");
                            m_muestraResultadosDifusos(arrayResultadosDifusos);
                            Centroide centroide = new Centroide(variablesLinguisticas);
                            messages.append("Resultado:" + centroide.procesar(resultados));
                            //m_muestraResultadosDifusos(arrayResultadosDifusos);

                        } catch (IOException ex) {
                            Logger.getLogger(GUI.class.getName()).log(Level.SEVERE, null, ex);
                        }

                    }
                });

            }
        }
        );
        menuFuzzy.add(menuItem);

        //Reglas difusas-------------------------------------------------------------------------------------------------------------------------------------------------------------------
        menuItem = new JMenuItem("Nueva regla difusa");

        menuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae
            ) {
                int llave = Integer.parseInt(JOptionPane.showInputDialog(cp, "Llave de la nueva regla difusa", JOptionPane.INPUT_VALUE_PROPERTY));
                ReglaDifusa regla;
                try {
                    if (reglasDifusas.existe(llave)) {
                        JOptionPane.showMessageDialog(cp, "Ya existe una regla difusa con esa llave.");
                    } else {
                        regla = new ReglaDifusa(llave);

                    }
                } catch (IOException ex) {
                    Logger.getLogger(GUI.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
        );
        menuReglas.add(menuItem);

        menuItem = new JMenuItem("Editar reglas difusas");

        menuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae
            ) {
                int llave = Integer.parseInt(JOptionPane.showInputDialog(cp, "Llave de la regla difusa a editar", JOptionPane.INPUT_VALUE_PROPERTY));
                ReglaDifusa regla = null, reglas[];
                try {
                    if (!reglasDifusas.existe(llave)) {
                        JOptionPane.showMessageDialog(cp, "No existe una regla difusa con esa llave.");
                    } else {
                        reglas = reglasDifusas.recuperarTodo();
                        for (int i = 0; i < reglas.length; i++) {
                            if (reglas[i].llave == llave) {
                                regla = reglas[i];
                            }
                        }
                        new FormularioReglasDifusas(regla, reglasDifusas, true, variablesLinguisticas);
                    }
                } catch (IOException ex) {
                    Logger.getLogger(GUI.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
        );
        menuReglas.add(menuItem);

        menuItem = new JMenuItem("Borrar regla difusa");

        menuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                int llave = Integer.parseInt(JOptionPane.showInputDialog(cp, "Llave de la regla difusa a eliminar", JOptionPane.INPUT_VALUE_PROPERTY));
                ReglaDifusa regla;
                try {
                    if (!reglasDifusas.existe(llave)) {
                        JOptionPane.showMessageDialog(cp, "No existe una regla difusa con esa llave.");
                    } else {
                        reglasDifusas.elimina(llave);
                    }
                } catch (IOException ex) {
                    Logger.getLogger(GUI.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
        menuReglas.add(menuItem);

        menuItem = new JMenuItem("Listar reglas difusas");

        menuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae
            ) {
                try {
                    messages.append("\n--------------------------------\n");
                    messages.append("\n" + reglasDifusas.mostrarTodo());

                } catch (IOException ex) {
                    Logger.getLogger(GUI.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
        );
        menuReglas.add(menuItem);

        menuItem = new JMenuItem("Importar CSV");

        menuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                int result;
                File file, workingDirectory;
                JFileChooser chooser = new JFileChooser();
                chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
                workingDirectory = new File(System.getProperty("user.dir"));
                chooser.setCurrentDirectory(workingDirectory);
                result = chooser.showOpenDialog(cp);
                int llave = Integer.parseInt(JOptionPane.showInputDialog(cp, "Primera llave consecutiva", JOptionPane.INPUT_VALUE_PROPERTY));
                if (result == 0) {
                    try {
                        file = chooser.getSelectedFile();
                        reglasDifusas.importar(file, llave);
                    } catch (FileNotFoundException ex) {
                        Logger.getLogger(GUI.class.getName()).log(Level.SEVERE, null, ex);
                    } catch (IOException ex) {
                        Logger.getLogger(GUI.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }
        });
        menuReglas.add(menuItem);

        return menuBar;
    }

    static JSlider getSlider(final JOptionPane optionPane) {
        JSlider slider = new JSlider();
        slider.setMajorTickSpacing(10);
        slider.setPaintTicks(true);
        slider.setPaintLabels(true);
        ChangeListener changeListener = new ChangeListener() {
            public void stateChanged(ChangeEvent changeEvent) {
                JSlider theSlider = (JSlider) changeEvent.getSource();
                if (!theSlider.getValueIsAdjusting()) {
                    optionPane.setInputValue(new Integer(theSlider.getValue()));
                    valors.setText("" + theSlider.getValue());
                }
            }
        };
        slider.addChangeListener((javax.swing.event.ChangeListener) changeListener);
        return slider;
    }

    public void m_muestraResultadosDifusos(ArrayList<ResultadoDifuso> p_array) {
        for (int i = 0; i < p_array.size(); i++) {

            System.out.println("Llave variable linguistica: " + p_array.get(i).variableConjunto.llaveVariableLiguistica);
            System.out.println("Llave del conjunto: " + p_array.get(i).variableConjunto.llaveConjunto);
            System.out.println("Resultado difuso: " + p_array.get(i).valor + "\n");
            //System.out.println(p_array.get(i).variableConjunto);

        }

        ArchivoReglas archivo = new ArchivoReglas();

        try {
            List<ResultadoDifuso> resultados = MaxMin.procesar(Arrays.asList(archivo.recuperarTodo()), p_array);

            for (ResultadoDifuso resultadoDifuso : resultados) {
                System.out.println("Difusificación: Llave variable linguistica: " + resultadoDifuso.variableConjunto.llaveVariableLiguistica);
                System.out.println("Difusificación: Llave del conjunto: " + resultadoDifuso.variableConjunto.llaveConjunto);
                System.out.println("Difusificación: Resultado difuso: " + resultadoDifuso.valor);
            }
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    private void recuperaVariableLinguisticaResultado(Container cp) throws IOException {
        boolean existeArchivo = true;
        RandomAccessFile lector = null, escritor;
        try {
            lector = new RandomAccessFile("variableResultado", "r");
        } catch (FileNotFoundException e) {
            existeArchivo = false;
        }
        if (existeArchivo) {
            variableLinguisticaResultado = lector.readInt();
            lector.close();
        } else {
            escritor = new RandomAccessFile("variableResultado", "rw");
            variableLinguisticaResultado = Integer.parseInt(JOptionPane.showInputDialog(cp, "Llave de la variable difusa que indica el resultado", JOptionPane.INPUT_VALUE_PROPERTY));
            escritor.writeInt(variableLinguisticaResultado);
            escritor.close();
        }
    }

    /*Metodos referentes al modelo neuronal de HOPFIELD  */
 /*
    Metodo que obtiene el numero de competencias almacenadas en el archivo, 
    asi como el numero de etiquetas que tiene cada uno
     */
    public VariableConjunto m_hopfield(ArrayList<Integer> p_patronE, ArrayList<int[]> p_PatronesGeneradosShow) {

        //Calculo de la matrizPesos
//      System.out.println("Muestra las matrices generadas de aplicar la operacion (PiT * Pi)-I");
        m_calculaMatrizPesos(p_PatronesGeneradosShow);

        return var_conjunto;

    }

    //Metodo que obtiene todos los patrones generados, y realiza la sumatoria de
    //W = Sumatoria(PiT * Pi)-I
    public void m_calculaMatrizPesos(ArrayList<int[]> p_PatronesGeneradosShow) {
//        System.out.println("PATRONES QUE ENTRAN AL METODO " + p_PatronesGeneradosShow.size());
        m_generaMatrizIdentidad(p_PatronesGeneradosShow.get(0), p_PatronesGeneradosShow.get(0));

        restapixpiT_I = new ArrayList<>();
        for (int i = 0; i < p_PatronesGeneradosShow.size(); i++) {
            int[] pi = p_PatronesGeneradosShow.get(i);
            int[] piT = pi;
            int[][] pixpiT = new int[pi.length][pi.length];
            int[][] pixpiT_I = new int[pi.length][pi.length];

            for (int j = 0; j < pi.length; j++) {
                for (int k = 0; k < pi.length; k++) {
                    pixpiT[j][k] = piT[j] * pi[k];
                    pixpiT_I[j][k] = pixpiT[j][k] - matrizIdentidad[j][k];
                    //A las matricez resutlante se le resta la matriz identidad

                }
            }
            restapixpiT_I.add(pixpiT_I);//Almaceno la matriz resultante de cada patron para despues sumarla
        }

        metodoMatrizPesos(restapixpiT_I);//Metodo que suma todas las matrices.

        for (int i = 0; i < restapixpiT_I.size(); i++) {
            System.out.println("MATRIZ GENERADA NUMERO :" + i);
            m_muestraMatriz(restapixpiT_I.get(i));
        }
        System.out.println("\n\n SUMATORIA DE LAS MATRICES, MATRIZ DE PESOS W");
        m_muestraMatriz(matrizPesos);
        GeneraPatronSalida(PatronEntrada, matrizPesos);//Metodo que realiza la multiplicacion del patron entrada por la matriz de pesos

//        System.out.println("PATRON DE SALIDAAA");
//        for (int i = 0; i < vectorSalida.length; i++) {
//            System.out.print(" " + vectorSalida[i]);
//        }
        //vectorSalida = funcionEscalon(vectorSalida);//Aplica la funcion escalon al vector salida
//        System.out.println("\nPATRON DE SALIDAAA con la funcion escalon");
//        for (int i = 0; i < vectorSalida.length; i++) {
//            System.out.print(" " + vectorSalida[i]);
//        }
        System.out.println("\n Patron de entrada: ");
        for (int i = 0; i < PatronEntrada.size(); i++) {
            System.out.print(" " + PatronEntrada.get(i));
        }

        System.out.println("\n Patron de salida: ");
        for (int i = 0; i < vectorSalida.length; i++) {
            System.out.print(" " + vectorSalida[i]);
        }

        m_MapearRegla(funcionEscalon(funcionEntrada(matrizPesos, vectorSalida)));

    }

    public void m_mapearPatronesaReglas(int llave, int[] p_patron) {
        ArrayList<TDA_TAM_VAR> arrayTDA = new ArrayList<>();
        ArrayList<Mapeado> arrayMapeado = new ArrayList<>();
        arrayTDA = m_obtenNumCompetencias();

        int[] Conju = null;
        int controla = 0;
        for (int i = 0; i < arrayTDA.size(); i++) {
            Conju = new int[arrayTDA.get(i).numConjuntos];
            for (int j = 0; j < arrayTDA.get(i).numConjuntos; j++) {
                Conju[j] = p_patron[controla];
                controla++;
            }
            Mapeado mapeado = new Mapeado(Conju);
            arrayMapeado.add(mapeado);
        }

        int[] p_patron1 = {-1, -1, 1};//Bueno*
        int[] p_patron2 = {-1, 1, -1};//Regular*
        int[] p_patron3 = {1, -1, -1};//Malo*
        int[] p_patron4 = {-1, 1, 1};//Bueno/Regular*
        int[] p_patron5 = {1, 1, -1};//Malo,Regular*

        int[] p_patron7 = {1, -1, -1, -1};//Excelente
        int[] p_patron8 = {-1, 1, -1, -1};//Bueno
        int[] p_patron9 = {-1, -1, 1, -1};//Regular
        int[] p_patron10 = {-1, -1, -1, 1};//Malo
        int[] p_patron11 = {1, 1, -1, -1};//Excelente/Bueno
        int[] p_patron12 = {-1, 1, 1, -1};//Bueno/Regular
        int[] p_patron13 = {-1, -1, 1, 1};//Regular/Malo

        int llave_conjunto = 0;
        int suma_malos = 0;
        int suma_regulares = 0;
        int suma_buenos = 0;
        int suma_excelentes = 0;
        VariableConjunto consecuente = null;
        VariableConjunto antecedente;
        ArrayList<VariableConjunto> antecedentess = new ArrayList<>();
        ArrayList<VariableConjunto> antecedentess2 = new ArrayList<>();

        for (int i = 0; i < (arrayMapeado.size() - 1); i++) {
            suma_malos = 0;
            suma_regulares = 0;
            suma_buenos = 0;
            suma_excelentes = 0;
            //Si el array del mapeo es igual a [-1 -1 1]-->Bueno (i,2)
            if (m_comparaVectores(arrayMapeado.get(i).getP_patron(), p_patron1)) {
                // p_cadenaReglaDifusa = p_cadenaReglaDifusa + "(" + i + "," + "2" + ")^";
                llave_conjunto = 2;
                suma_buenos++;
                antecedente = new VariableConjunto(i, 2);
                antecedentess.add(antecedente);
            } //Si el  array del mapeo es igual a [-1 1 -1]-->Regular (i,1)
            else if (m_comparaVectores(arrayMapeado.get(i).getP_patron(), p_patron2)) {
                //p_cadenaReglaDifusa = p_cadenaReglaDifusa + "(" + i + "," + "1" + ")^";
                llave_conjunto = 1;
                suma_regulares++;
                antecedente = new VariableConjunto(i, 1);
                antecedentess.add(antecedente);
            }////Si el array del mapeo es igual a [1 -1 -1]-->Malo (i,0)
            else if (m_comparaVectores(arrayMapeado.get(i).getP_patron(), p_patron3)) {
                //p_cadenaReglaDifusa = p_cadenaReglaDifusa + "(" + i + "," + "0" + ")^";
                llave_conjunto = 0;
                suma_malos++;
                antecedente = new VariableConjunto(i, 0);
                antecedentess.add(antecedente);
            }//Si el  array del mapeo es igual a [-1, 1, 1]-->Bueno y regular (i,2) y (i,1)
            else if (m_comparaVectores(arrayMapeado.get(i).getP_patron(), p_patron4)) {
                //p_cadenaReglaDifusa = p_cadenaReglaDifusa + "(" + i + "," + "2" + ")^";
                llave_conjunto = 1;
                suma_buenos++;
                //p_cadenaReglaDifusa2 = p_cadenaReglaDifusa + "(" + i + "," + "1" + ")^";
                antecedente = new VariableConjunto(i, 1);
                antecedentess.add(antecedente);
            }//Si el  array del mapeo es igual a [1, 1, -1]-->Malo y regular (i,2) y (i,1)
            else if (m_comparaVectores(arrayMapeado.get(i).getP_patron(), p_patron5)) {
                //p_cadenaReglaDifusa = p_cadenaReglaDifusa + "(" + i + "," + "1" + ")^";
                llave_conjunto = 0;
                suma_regulares++;
                //p_cadenaReglaDifusa2 = p_cadenaReglaDifusa + "(" + i + "," + "0" + ")^";
                antecedente = new VariableConjunto(i, 0);
                antecedentess.add(antecedente);
            } //Patrones de 4 entradas
            //Si el array del mapeo es igual a [-1 -1 -1 1]-->Excelente (i,3)
            else if (m_comparaVectores(arrayMapeado.get(i).getP_patron(), p_patron7)) {
                //p_cadenaReglaDifusa = p_cadenaReglaDifusa + "(" + i + "," + "3" + ")^";
                llave_conjunto = 3;
                suma_excelentes++;
                antecedente = new VariableConjunto(i, 3);
                antecedentess.add(antecedente);
            }//Si el array del mapeo es igual a [-1 -1 1 -1]-->Bueno (i,2)
            else if (m_comparaVectores(arrayMapeado.get(i).getP_patron(), p_patron8)) {
                llave_conjunto = 2;
                suma_buenos++;
                //p_cadenaReglaDifusa = p_cadenaReglaDifusa + "(" + i + "," + "2" + ")^";
                antecedente = new VariableConjunto(i, 2);
                antecedentess.add(antecedente);
            }//Si el array del mapeo es igual a [-1 1 -1 -1]-->Regular (i,1)
            else if (m_comparaVectores(arrayMapeado.get(i).getP_patron(), p_patron9)) {
                llave_conjunto = 1;
                suma_regulares++;
                //p_cadenaReglaDifusa = p_cadenaReglaDifusa + "(" + i + "," + "1" + ")^";
                antecedente = new VariableConjunto(i, 1);
                antecedentess.add(antecedente);
            }//Si el array del mapeo es igual a [1 -1 -1 -1]-->Malo (i,0)
            else if (m_comparaVectores(arrayMapeado.get(i).getP_patron(), p_patron10)) {
                llave_conjunto = 0;
                suma_malos++;
                //p_cadenaReglaDifusa = p_cadenaReglaDifusa + "(" + i + "," + "0" + ")^";
                antecedente = new VariableConjunto(i, 0);
                antecedentess.add(antecedente);
            }//Si el array del mapeo es igual a [1 -1 1 1]-->Excelente y Bueno (i,3) (i,2)
            else if (m_comparaVectores(arrayMapeado.get(i).getP_patron(), p_patron11)) {
                llave_conjunto = 3;
                suma_excelentes++;
                //p_cadenaReglaDifusa = p_cadenaReglaDifusa + "(" + i + "," + "3" + ")^";
                //p_cadenaReglaDifusa2 = p_cadenaReglaDifusa + "(" + i + "," + "2" + ")^";
                antecedente = new VariableConjunto(i, 3);
                antecedentess.add(antecedente);
            }//Si el array del mapeo es igual a [1 1 1 -1]--> Bueno y Regular (i,2)(i,1)
            else if (m_comparaVectores(arrayMapeado.get(i).getP_patron(), p_patron12)) {
                llave_conjunto = 1;
                suma_buenos++;
                //p_cadenaReglaDifusa = p_cadenaReglaDifusa + "(" + i + "," + "2" + ")^";
                //p_cadenaReglaDifusa2 = p_cadenaReglaDifusa + "(" + i + "," + "1" + ")^";
                antecedente = new VariableConjunto(i, 1);
                antecedentess.add(antecedente);
            }//Si el array del mapeo es igual a [1 1 1 -1]--> Regular y Malo (i,1)(i,0)
            else if (m_comparaVectores(arrayMapeado.get(i).getP_patron(), p_patron13)) {
                llave_conjunto = 0;
                suma_regulares++;
                //p_cadenaReglaDifusa = p_cadenaReglaDifusa + "(" + i + "," + "1" + ")^";
                //p_cadenaReglaDifusa2 = p_cadenaReglaDifusa + "(" + i + "," + "0" + ")^";
                antecedente = new VariableConjunto(i, 0);
                antecedentess.add(antecedente);
            }
            

            if (suma_buenos > suma_excelentes && suma_buenos > suma_malos && suma_buenos > suma_regulares) {
                consecuente = new VariableConjunto(9, 2);
            }
            if (suma_excelentes > suma_buenos && suma_excelentes > suma_malos && suma_excelentes > suma_regulares) {
                consecuente = new VariableConjunto(9, 3);
            }
            if (suma_regulares > suma_excelentes && suma_regulares > suma_malos && suma_regulares > suma_buenos) {
                consecuente = new VariableConjunto(9, 1);
            }
            if (suma_malos > suma_excelentes && suma_malos > suma_buenos && suma_malos > suma_regulares) {
                consecuente = new VariableConjunto(9, 0);
            }

            //Aqui agregar todas las reglas generadas al archivo
        }
        sistemaHibrido.VariableConjunto antecedente0 = new VariableConjunto(0, 2);
       sistemaHibrido.VariableConjunto antecedente1 = new VariableConjunto(1, 2);
        
        sistemaHibrido.VariableConjunto antecedente2 = new VariableConjunto(2, 2);
        sistemaHibrido.VariableConjunto antecedente3 = new VariableConjunto(3, 2);
        
        sistemaHibrido.VariableConjunto antecedente4 = new VariableConjunto(4, 2);
        sistemaHibrido.VariableConjunto antecedente5 = new VariableConjunto(5, 2);
        
        sistemaHibrido.VariableConjunto antecedente6 = new VariableConjunto(6, 2);
        sistemaHibrido.VariableConjunto antecedente7 = new VariableConjunto(7, 2);
        
        antecedentess2 = new ArrayList<>();
        antecedentess2.add(antecedente0);
        antecedentess2.add(antecedente1);
        antecedentess2.add(antecedente2);
        antecedentess2.add(antecedente3);
        antecedentess2.add(antecedente4);
        antecedentess2.add(antecedente5);
        antecedentess2.add(antecedente6);
        antecedentess2.add(antecedente7);
        sistemaHibrido.VariableConjunto consecuentex = new VariableConjunto(9, 2);
        
        agregarReglaDifusa(llave, antecedentess, consecuente);
        //agregarReglaDifusa(405, antecedentess2, consecuentex);
        
    }

    public void m_MapearRegla(int[] p_pastronSalida) {
        //Quitar los ultimos 3 elementos del array
//        System.out.println("\nPATRON DE SALIDA SIN LA VARIABLE DE SALIDA");
//        for (int i = 0; i < (p_pastronSalida.length - 3); i++) {
//            System.out.print(" " + p_pastronSalida[i]);
//        }
        ArrayList<TDA_TAM_VAR> arrayTDA = new ArrayList<>();
        ArrayList<Mapeado> arrayMapeado = new ArrayList<>();
        arrayTDA = m_obtenNumCompetencias();

        System.out.println("\nCOMPETENCIAS Y SUS ETIQUETAS");
        for (int i = 0; i < (arrayTDA.size() - 1); i++) {
            System.out.println(arrayTDA.get(i).id_varLin + " " + arrayTDA.get(i).numConjuntos);
        }
        int[] Conju = null;
        int controla = 0;
        for (int i = 0; i < arrayTDA.size(); i++) {
            Conju = new int[arrayTDA.get(i).numConjuntos];
            for (int j = 0; j < arrayTDA.get(i).numConjuntos; j++) {
                Conju[j] = p_pastronSalida[controla];
                controla++;
            }
            Mapeado mapeado = new Mapeado(Conju);
            arrayMapeado.add(mapeado);
        }

         System.out.println("MAPEADO JALANDO:  " + arrayMapeado.size());
        for (int i = 0; i < (arrayMapeado.size() - 1); i++) {
            System.out.println("\nId var linguistica " + arrayMapeado.get(i).id_VarLinguis);
            for (int j = 0; j < arrayMapeado.get(i).p_patron.length; j++) {

                System.out.print(" " + arrayMapeado.get(i).p_patron[j]);
            }
        }
//
        for (int i = 0; i < (arrayMapeado.size() - 1); i++) {
            System.out.println("\nId var linguistica " + arrayMapeado.get(i).id_VarLinguis);
            for (int j = 0; j < arrayMapeado.get(i).p_patron.length; j++) {
                System.out.print(" " + arrayMapeado.get(i).p_patron[j]);
            }
        }
        String p_cadenaReglaDifusa = "";
        String p_cadenaReglaDifusa2 = "";

        int[] p_patron1 = {-1, -1, 1};//Bueno*
        int[] p_patron2 = {-1, 1, -1};//Regular*
        int[] p_patron3 = {1, -1, -1};//Malo*
        int[] p_patron4 = {-1, 1, 1};//Bueno/Regular*
        int[] p_patron5 = {1, 1, -1};//Malo,Regular*

        int[] p_patron7 = {1, -1, -1, -1};//Excelente
        int[] p_patron8 = {-1, 1, -1, -1};//Bueno
        int[] p_patron9 = {-1, -1, 1, -1};//Regular
        int[] p_patron10 = {-1, -1, -1, 1};//Malo
        int[] p_patron11 = {1, 1, -1, -1};//Excelente/Bueno
        int[] p_patron12 = {-1, 1, 1, -1};//Bueno/Regular
        int[] p_patron13 = {-1, -1, 1, 1};//Regular/Malo

        //Patrones de 3 entradas
        int llave_conjunto = 0;
        int suma_malos = 0;
        int suma_regulares = 0;
        int suma_buenos = 0;
        int suma_excelentes = 0;
        VariableConjunto consecuente = null;
        VariableConjunto antecedente;
        ArrayList<VariableConjunto> antecedentess = new ArrayList<>();
        for (int i = 0; i < (arrayMapeado.size() - 1); i++) {
            //Si el array del mapeo es igual a [-1 -1 1]-->Bueno (i,2)
            if (m_comparaVectores(arrayMapeado.get(i).getP_patron(), p_patron1)) {
                p_cadenaReglaDifusa = p_cadenaReglaDifusa + "(" + i + "," + "2" + ")^";
                llave_conjunto = 2;
                suma_buenos++;
                antecedente = new VariableConjunto(i, 2);
                antecedentess.add(antecedente);
            } //Si el  array del mapeo es igual a [-1 1 -1]-->Regular (i,1)
            else if (m_comparaVectores(arrayMapeado.get(i).getP_patron(), p_patron2)) {
                p_cadenaReglaDifusa = p_cadenaReglaDifusa + "(" + i + "," + "1" + ")^";
                llave_conjunto = 1;
                suma_regulares++;
                antecedente = new VariableConjunto(i, 1);
                antecedentess.add(antecedente);
            }////Si el array del mapeo es igual a [1 -1 -1]-->Malo (i,0)
            else if (m_comparaVectores(arrayMapeado.get(i).getP_patron(), p_patron3)) {
                p_cadenaReglaDifusa = p_cadenaReglaDifusa + "(" + i + "," + "0" + ")^";
                llave_conjunto = 0;
                suma_malos++;
                antecedente = new VariableConjunto(i, 0);
                antecedentess.add(antecedente);
            }//Si el  array del mapeo es igual a [-1, 1, 1]-->Bueno y regular (i,2) y (i,1)
            else if (m_comparaVectores(arrayMapeado.get(i).getP_patron(), p_patron4)) {
                p_cadenaReglaDifusa = p_cadenaReglaDifusa + "(" + i + "," + "2" + ")^";
                llave_conjunto = 1;
                suma_buenos++;
                p_cadenaReglaDifusa2 = p_cadenaReglaDifusa + "(" + i + "," + "1" + ")^";
                antecedente = new VariableConjunto(i, 2);
                antecedentess.add(antecedente);
            }//Si el  array del mapeo es igual a [1, 1, -1]-->Malo y regular (i,2) y (i,1)
            else if (m_comparaVectores(arrayMapeado.get(i).getP_patron(), p_patron5)) {
                p_cadenaReglaDifusa = p_cadenaReglaDifusa + "(" + i + "," + "1" + ")^";
                llave_conjunto = 0;
                suma_regulares++;
                p_cadenaReglaDifusa2 = p_cadenaReglaDifusa + "(" + i + "," + "0" + ")^";
                antecedente = new VariableConjunto(i, 1);
                antecedentess.add(antecedente);
            } //Patrones de 4 entradas
            //Si el array del mapeo es igual a [-1 -1 -1 1]-->Excelente (i,3)
            else if (m_comparaVectores(arrayMapeado.get(i).getP_patron(), p_patron7)) {
                p_cadenaReglaDifusa = p_cadenaReglaDifusa + "(" + i + "," + "3" + ")^";
                llave_conjunto = 3;
                suma_excelentes++;
                antecedente = new VariableConjunto(i, 3);
                antecedentess.add(antecedente);
            }//Si el array del mapeo es igual a [-1 -1 1 -1]-->Bueno (i,2)
            else if (m_comparaVectores(arrayMapeado.get(i).getP_patron(), p_patron8)) {
                llave_conjunto = 2;
                suma_buenos++;
                p_cadenaReglaDifusa = p_cadenaReglaDifusa + "(" + i + "," + "2" + ")^";
                antecedente = new VariableConjunto(i, 2);
                antecedentess.add(antecedente);
            }//Si el array del mapeo es igual a [-1 1 -1 -1]-->Regular (i,1)
            else if (m_comparaVectores(arrayMapeado.get(i).getP_patron(), p_patron9)) {
                llave_conjunto = 1;
                suma_regulares++;
                p_cadenaReglaDifusa = p_cadenaReglaDifusa + "(" + i + "," + "1" + ")^";
                antecedente = new VariableConjunto(i, 1);
                antecedentess.add(antecedente);
            }//Si el array del mapeo es igual a [1 -1 -1 -1]-->Malo (i,0)
            else if (m_comparaVectores(arrayMapeado.get(i).getP_patron(), p_patron10)) {
                llave_conjunto = 0;
                suma_malos++;
                p_cadenaReglaDifusa = p_cadenaReglaDifusa + "(" + i + "," + "0" + ")^";
                antecedente = new VariableConjunto(i, 0);
                antecedentess.add(antecedente);
            }//Si el array del mapeo es igual a [1 -1 1 1]-->Excelente y Bueno (i,3) (i,2)
            else if (m_comparaVectores(arrayMapeado.get(i).getP_patron(), p_patron11)) {
                llave_conjunto = 3;
                suma_excelentes++;
                p_cadenaReglaDifusa = p_cadenaReglaDifusa + "(" + i + "," + "3" + ")^";
                p_cadenaReglaDifusa2 = p_cadenaReglaDifusa + "(" + i + "," + "2" + ")^";
                antecedente = new VariableConjunto(i, 3);
                antecedentess.add(antecedente);
            }//Si el array del mapeo es igual a [1 1 1 -1]--> Bueno y Regular (i,2)(i,1)
            else if (m_comparaVectores(arrayMapeado.get(i).getP_patron(), p_patron12)) {
                llave_conjunto = 1;
                suma_buenos++;
                p_cadenaReglaDifusa = p_cadenaReglaDifusa + "(" + i + "," + "2" + ")^";
                p_cadenaReglaDifusa2 = p_cadenaReglaDifusa + "(" + i + "," + "1" + ")^";
                antecedente = new VariableConjunto(i, 2);
                antecedentess.add(antecedente);
            }//Si el array del mapeo es igual a [1 1 1 -1]--> Regular y Malo (i,1)(i,0)
            else if (m_comparaVectores(arrayMapeado.get(i).getP_patron(), p_patron13)) {
                llave_conjunto = 0;
                suma_regulares++;
                p_cadenaReglaDifusa = p_cadenaReglaDifusa + "(" + i + "," + "1" + ")^";
                p_cadenaReglaDifusa2 = p_cadenaReglaDifusa + "(" + i + "," + "0" + ")^";
                antecedente = new VariableConjunto(i, 1);
                antecedentess.add(antecedente);
            }

            if (suma_buenos > suma_excelentes && suma_buenos > suma_malos && suma_buenos > suma_regulares) {
                consecuente = new VariableConjunto(9, 2);
            }
            if (suma_excelentes > suma_buenos && suma_excelentes > suma_malos && suma_excelentes > suma_regulares) {
                consecuente = new VariableConjunto(9, 3);
            }
            if (suma_regulares > suma_excelentes && suma_regulares > suma_malos && suma_regulares > suma_buenos) {
                consecuente = new VariableConjunto(9, 1);
            }
            if (suma_malos > suma_excelentes && suma_malos > suma_buenos && suma_malos > suma_regulares) {
                consecuente = new VariableConjunto(9, 0);
            }

        }
        //AQUI NO DEBO AGREGAR ESTA REGLA AL ARCHIVO
        //agregarReglaDifusa(1, antecedentess, consecuente);

        var_conjunto = consecuente;

        //System.out.println(p_cadenaReglaDifusa2);
    }

    public void m_mapeaPatronesGenerados(ArrayList<int[]> p_PatronesGeneradosShow) {
        messages.append("\nREGLAS AGREGADAS AL ARCHIVO");
        for (int i = 0; i < p_PatronesGeneradosShow.size(); i++) {
            m_mapearPatronesaReglas(i, p_PatronesGeneradosShow.get(i));
        }
        

    }

    public void agregarReglaDifusa(int id_llave, ArrayList<VariableConjunto> p_antecedentes, VariableConjunto consecuente) {
        //Eliminar archivo reglas difusas
        //eliminarArchivoReglasDifusas();
        //System.out.println("REEGLAAAAA DIFUSAAAAAAAAAAAAAAAA AGREGADAAAAAAAAAAAAAAAAAAAAAA");
        int llave = id_llave;
        ReglaDifusa regla;
        try {
            if (reglasDifusas.existe(llave)) {
                System.out.println("Ya existe una regla difusa con esa llave.");
            } else {
                regla = new ReglaDifusa(llave, p_antecedentes, consecuente);
                reglasDifusas.insertar(regla);
            }
        } catch (IOException ex) {
            Logger.getLogger(GUI.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static void eliminarArchivoReglasDifusas() {
        File fichero = new File("C:\\Users\\joseh\\Desktop\\sistemasHibrido\\reglasDifusas");
        if (!fichero.exists()) {
            System.out.println("El archivo reglas difusas no existe.");
        } else {
            fichero.delete();
            System.out.println("El archivo reglas difusas fue eliminado.");
        }

    }

    public boolean m_comparaVectores(int[] p_1, int[] p_2) {
        return java.util.Arrays.equals(p_1, p_2);
    }

    public void GeneraPatronSalida(ArrayList<Integer> p_patronE, int[][] matrizPesos) {
        //Funciona de entrada
        int suma = 0;
        vectorSalida = new int[p_patronE.size()];
        for (int i = 0; i < p_patronE.size(); i++) {
            for (int j = 0; j < p_patronE.size(); j++) {
                suma += matrizPesos[j][i] * p_patronE.get(j);
            }
            vectorSalida[i] = suma;
            suma = 0;
        }
    }

    public void m_muestraMatriz(int[][] p_matriz) {
        for (int x = 0; x < p_matriz.length; x++) {
            for (int y = 0; y < p_matriz[x].length; y++) {
                System.out.print(" | " + p_matriz[x][y] + " | ");
            }
            System.out.println();
        }
    }

    public void m_generaMatrizIdentidad(int[] pi, int[] piT) {
        //int num_ne = pi.length * piT.length;
        matrizIdentidad = new int[pi.length][pi.length];
        for (int i = 0; i < pi.length; i++) {
            for (int j = 0; j < pi.length; j++) {
                if (i == j) {
                    matrizIdentidad[i][i] = 1;
                } else {
                    matrizIdentidad[i][i] = 0;
                }
                System.out.print(matrizIdentidad[i][j]);

            }
            System.out.print("\n");
        }
        System.out.println();

    }

    public ArrayList<TDA_TAM_VAR> m_obtenNumCompetencias() {
        sistemaHibrido.VariableLinguistica variables[] = null;
        ArrayList<TDA_TAM_VAR> arrayTDA = new ArrayList<>();
        int i;
        ArrayList<Integer> numVarLing = new ArrayList<>();
        ArrayList<Integer> numConjuntos = new ArrayList<>();
        try {
            variables = variablesLinguisticas.recuperarSecuencial();
        } catch (IOException ex) {
            Logger.getLogger(sistemaexpertodifuso.GUI.class.getName()).log(Level.SEVERE, null, ex);
        }
        if (variables != null) {
            for (i = 0; i < variables.length; i++) {
                numVarLing.add(variables[i].obtenLLaveVariable());
                numConjuntos.add(variables[i].m_obtenNumConjuntosxVar());
            }
        } else {
            System.out.println("Hubo un error al recuperar las variables lingüisticas.");
        }
        //System.out.println("Numero de variables lingusticas en el archivo");
        for (int j = 0; j < (numVarLing.size()); j++) {
            //System.out.println("Var Lingu: " + numVarLing.get(j) + " Conjuntos: " + numConjuntos.get(j));
            arrayTDA.add(new TDA_TAM_VAR(numVarLing.get(j), numConjuntos.get(j)));
        }
        return arrayTDA;
    }

    public void generar_combinaciones(ArrayList<TDA_TAM_VAR> num_competencias) {
        String imprimeR = "";
        int renglon1, renglon2, columna, seguir = 0, k2 = 0;
        int competencias = 8, etiqueta = 3;

        //int[] V_etiquetas = new int[tamaño];
        //por competencias
        for (int i = 0; i < num_competencias.size(); i++) {
            int[][] combinaciones = new int[num_competencias.get(i).numConjuntos + (num_competencias.get(i).numConjuntos - 1)][num_competencias.get(i).numConjuntos];
            renglon1 = num_competencias.get(i).numConjuntos;
            //renglon2 = num_competencias.get(i).numConjuntos+(num_competencias.get(i).numConjuntos-1);
            renglon2 = (num_competencias.get(i).numConjuntos - 1);
            columna = num_competencias.get(i).numConjuntos;
            //System.out.println("Competencia " + i);
            for (int j = 0; j < renglon1; j++) {
                for (int k = 0; k < columna; k++) {
                    if (k == j) {
                        combinaciones[j][k] = 1;
                        imprimeR += combinaciones[j][k] + " ";

                    } else {
                        combinaciones[j][k] = (-1);
                        imprimeR += combinaciones[j][k] + " ";
                    }

                }
                //System.out.println(imprimeR);
                imprimeR = "";
                seguir++;
            }

            for (int j = 0; j < renglon2; j++) {
                for (int k = 0; k < columna - 1; k++) {
                    if (k == j) {
                        combinaciones[j + seguir][k] = 1;
                        imprimeR += combinaciones[j + seguir][k] + " ";
                        combinaciones[j + seguir][k + 1] = 1;
                        imprimeR += combinaciones[j + seguir][k + 1] + " ";
                        k2 = k + 2;
                    } else {
                        combinaciones[j + seguir][k2] = (-1);
                        imprimeR += combinaciones[j + seguir][k2] + " ";
                        k2++;
                    }
                }
                k2 = 0;
                //System.out.println(imprimeR);
                imprimeR = "";
            }
            arrayCombinaciones.add(combinaciones);
            seguir = 0;
        }
    }

    //Metodo para la suma de los conjuntos
    //se creo una variable tamañoVector publica que guarda el tamaño de los patrones 1xtamañoVector
    public int neuronas(ArrayList<TDA_TAM_VAR> num_competencias) {
        int suma = 0;
        for (int i = 0; i < num_competencias.size(); i++) {
            suma = suma + num_competencias.get(i).numConjuntos + (num_competencias.get(i).numConjuntos - 1);
            //tamañoVector = tamañoVector + num_competencias.get(i).numConjuntos;
        }
        return suma;
    }

    public void generar_PatronesEntrada(ArrayList<TDA_TAM_VAR> num_competencias) {
        //Aqui esta el error dice algo de static y ocupo ese arraylist
        boolean banderaSalida = true;
        int detener = 0;
        int valor = neuronas(m_obtenNumCompetencias());
        int noPatrones = arrayCombinaciones.size() * valor;
        sumaTamaño(num_competencias);

        String patron = "";
        //Este while es para generar los patrones aleatoriamente
        while (banderaSalida) {
            patronGenerado = new int[tamañoVector];
            //recorrer patrones e insercion

            for (int m = 0; m < num_competencias.size(); m++) {
                int[][] combinaciones = arrayCombinaciones.get(m);
                int rango = num_competencias.get(m).numConjuntos + (num_competencias.get(m).numConjuntos - 1);
                int posicion = (int) (Math.random() * rango);
                for (int l = 0; l < (num_competencias.get(m).numConjuntos); l++) {
                    patronGenerado[neurona] = combinaciones[posicion][l];
                    neurona++;
                    patron += combinaciones[posicion][l] + " ";
                }//for de columnas

            }//for de competencias
            boolean entrar = compararPatrones(patronGenerado);
            //Si la variable entrar es Verdadera ingresa el patron generado
            //Si es falso no ingresa el patron
            if (entrar) {
                patronesDeEntrada.add(patronGenerado);
                detener++;
                patron = "";
            }
            //aqui si ya se compretaron en numero de combinaciones camabia la bandera a falso y se detiene el While 
            if (detener == noPatrones) {
                banderaSalida = false;
            }
            neurona = 0;
        }//terminal el while
    }

    //Esta clase es llamada para comprar el patron generado con los patrones que ya fueron guardados
    //Si encuentra un patron igual a uno de los guardados en patronesDeEntrada lo ignora y genera otro nuevo
    // Regresa verdadero o falso
    public boolean compararPatrones(int[] vector) {
        boolean existe = false;
        if (patronesDeEntrada.isEmpty()) {
            existe = true;
        } else {

            for (int k = 0; k < patronesDeEntrada.size(); k++) {
                int[] linea = patronesDeEntrada.get(k);
                for (int n = 0; n < vector.length; n++) {
                    if (linea[n] != vector[n]) {
                        existe = true;
                        break;
                    }
                }

            }

        }

        return existe;
    }

    public void sumaTamaño(ArrayList<TDA_TAM_VAR> num_competencias) {
        for (int i = 0; i < num_competencias.size(); i++) {
            tamañoVector = tamañoVector + num_competencias.get(i).numConjuntos;
        }
    }

    public int[] funcionEscalon(int[] patronE) {
        int[] patronResultado = new int[patronE.length];
        //recorremos el patron
        for (int i = 0; i < patronE.length; i++) {
            //si es mayor o igual a 0 se ingresa 1 al nuevo patron, sino ingresa -1
            if (patronE[i] >= 0) {
                patronResultado[i] = 1;
            } else {
                patronResultado[i] = -1;
            }
        }
        return patronResultado;
    }

    public void metodoMatrizPesos(ArrayList<int[][]> todasMatrices) {
        try {
            matrizPesos = new int[todasMatrices.get(0).length][todasMatrices.get(0).length];

            for (int i = 0; i < todasMatrices.size(); i++) {
                int[][] matriz = todasMatrices.get(i);
                int[][] siguienteMatriz = todasMatrices.get(i + 1);
                for (int j = 0; j < matriz.length; j++) {
                    for (int k = 0; k < matriz.length; k++) {
                        matrizPesos[j][k] = matrizPesos[j][k] + matriz[j][k] + siguienteMatriz[j][k];
                    }

                }

            }
        } catch (Exception e) {
        }
    }

    public int[] funcionEntrada(int[][] matrizdePesos, int[] entrada) {
        int suma = 0;
        int[] patronSalida = new int[matrizdePesos.length];
        for (int i = 0; i < patronSalida.length; i++) {
            for (int j = 0; j < patronSalida.length; j++) {
                suma += matrizdePesos[j][i] * entrada[j];
                System.out.println("" + suma);
            }
            patronSalida[i] = suma;
        }
        return patronSalida;
    }

    public void m_procesoHopfield() {
        generar_combinaciones(m_obtenNumCompetencias());//Genera las combinaciones que puede haber
        generar_PatronesEntrada(m_obtenNumCompetencias());//Genera los patrones de entrada
        m_mapeaPatronesGenerados(patronesDeEntrada);//Comvierte los patrones generados a reglas y las agrega al archivo
        m_hopfield(PatronEntrada, patronesDeEntrada);
    }

}
