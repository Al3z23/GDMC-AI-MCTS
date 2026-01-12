package src;

import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.Behaviour;
import jade.core.behaviours.CyclicBehaviour;
import jade.core.behaviours.OneShotBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import javax.swing.*;
import java.awt.*;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class AgenteInterfaz extends Agent {


    @Override
    protected void setup() {
        System.out.println("Agente AgenteInterfaz iniciado");
        addBehaviour(new Behaviour() {
            boolean finished = false;

            @Override
            public void action() {
                ACLMessage msg = blockingReceive(); // bloquea hasta recibir
            
                if (msg.getContent().equals("UNLOCK")) {
                    System.out.println("Desbloqueado");
                    SwingUtilities.invokeLater(() -> new SelectorSalasGUI());
                    
                    finished = true;
                }
            }
        
            @Override
            public boolean done() {
                return finished;
            }
        });
        
    }

    class SelectorSalasGUI extends JFrame {

        public SelectorSalasGUI() {
            setTitle("Selector de Salas");
            setSize(900, 600);
            setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            setLocationRelativeTo(null);

            JPanel panelPrincipal = new JPanel();
            panelPrincipal.setBackground(new Color(168, 196, 159));
            panelPrincipal.setLayout(new BorderLayout());

            JLabel titulo = new JLabel("Elige una sala");
            titulo.setHorizontalAlignment(SwingConstants.CENTER);
            titulo.setFont(new Font("Arial", Font.BOLD, 28));
            titulo.setBorder(BorderFactory.createEmptyBorder(30, 0, 30, 0));

            panelPrincipal.add(titulo, BorderLayout.NORTH);

            int[][] salas = new int[][]{new int[]{1,2}, new int[]{2,3},
                new int[]{3,4}, new int[]{4,5}, new int[]{5,7} };

            Random random = new Random();
            int a = random.nextInt(salas.length);
            int [] sala1 = salas[a];
            int b = random.nextInt(salas.length);
            while(b == a){
                b = random.nextInt(salas.length);
            }
            int [] sala2 = salas[b];
            String str1 = "sala" + sala1[0] + ".png";
            String str2 = "sala" + sala2[0] + ".png";

            JPanel panelSalas = new JPanel();
            panelSalas.setOpaque(false);
            panelSalas.setLayout(new GridLayout(1, 2, 50, 0));
            panelSalas.add(crearPanelSala(str1, sala1[1]));
            panelSalas.add(crearPanelSala(str2, sala2[1]));
            panelPrincipal.add(panelSalas, BorderLayout.CENTER);

            add(panelPrincipal);
            setVisible(true);
        }

        private JPanel crearPanelSala(String rutaImagen, int numero) {
            JPanel panel = new JPanel();
            panel.setOpaque(false);
            panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

            JLabel textoSala = new JLabel("Sala " + numero);
            textoSala.setFont(new Font("Arial", Font.BOLD, 20));
            textoSala.setAlignmentX(Component.CENTER_ALIGNMENT);

            JLabel imagen = new JLabel();
            ImageIcon icon = new ImageIcon(rutaImagen);
            Image img = icon.getImage().getScaledInstance(240, 135, Image.SCALE_SMOOTH);
            imagen.setIcon(new ImageIcon(img));
            imagen.setAlignmentX(Component.CENTER_ALIGNMENT);

            JButton boton = new JButton("Elegir");
            boton.setAlignmentX(Component.CENTER_ALIGNMENT);
            boton.addActionListener(e -> {
                try {
                    escribirFichero(numero);
                } catch (IOException e1) {}
            });

            panel.add(Box.createVerticalGlue());
            panel.add(textoSala);                 // TEXTO DE LA SALA
            panel.add(Box.createRigidArea(new Dimension(0, 10)));
            panel.add(imagen);                    // IMAGEN
            panel.add(Box.createRigidArea(new Dimension(0, 20)));
            panel.add(boton);                     // BOTÓN
            panel.add(Box.createVerticalGlue());
                
            return panel;
        }

        private void escribirFichero(int numero) throws IOException {
            List<Integer> lista = new ArrayList<>();
            BufferedReader br = new BufferedReader(new FileReader("pesos.txt"));
            String linea;
            while ((linea = br.readLine()) != null) {
                lista.add(Integer.parseInt(linea.trim()));
            }
            br.close();
            int[] numeros = lista.stream().mapToInt(Integer::intValue).toArray();
            numeros[numero-1]++;

                 BufferedWriter bw = new BufferedWriter(new FileWriter("pesos.txt"));
            for (int valor : numeros) {
                bw.write(Integer.toString(valor));
                bw.newLine();
            }
            bw.close();
            

            ACLMessage msg2 = new ACLMessage(ACLMessage.INFORM);
            msg2.addReceiver(new AID("agenteProc", AID.ISLOCALNAME));
            msg2.setContent(Integer.toString(numero));
            send(msg2);
        }
    }
}
