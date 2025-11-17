package src;

import jade.core.Agent;
import jade.core.behaviours.Behaviour;
import jade.core.behaviours.CyclicBehaviour;
import jade.core.behaviours.OneShotBehaviour;
import jade.lang.acl.ACLMessage;

import java.io.FileWriter;   // Import the FileWriter class
import java.io.IOException;  // Import the IOException class

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import com.google.gson.*;


public class AgenteProcesamiento extends Agent {
    
    public static int x,y,z;
    public static int[][][] matriz;
    public static int[][][] matrizActualizada;
    public static int cajaX = 4;
    public static int cajaY = 3;
    public static int cajaZ = 4;
    public static List<int[]> mejorLista = new ArrayList<>();
    //public static List<int[]> listaAct = new ArrayList<>();
    public static int puntuacionMejorLista;

    private static int[][][] leerMatriz() throws IOException {
        String contenido = new String(Files.readAllBytes(Paths.get("matrix.txt")));
        Gson gson = new Gson();
        int[][][] matriz = gson.fromJson(contenido, int[][][].class);
        y = matriz.length;
        x = matriz[0].length;
        z = matriz[0][0].length;
        return matriz;
    }

    private static int[][] leerPosTam() throws IOException {
        String contenido = new String(Files.readAllBytes(Paths.get("pos.txt")));
        Gson gson = new Gson();
        int[][] posTam = gson.fromJson(contenido, int[][].class);
        y = posTam.length;
        x = posTam[0].length;
        return posTam;
    }



    private static int[][] obtenerPosValidas(int cajaX, int cajaY, int cajaZ){
        int[][] lista;
        List<int[]> listaArr = new ArrayList<>();
        boolean noValido = false;
        for (int i = 0; i < y; i++){
            for (int j = 0; j < x; j++){
                for (int k = 0; k < z; k++){
                    noValido = false;
                    if(matrizActualizada[i][j][k] == 1){ // bloque seleccionado es aire?
                        for (int i2 = 0; i2 < cajaY; i2++){
                            if(noValido){
                                break;
                            }
                            for (int j2 = 0; j2 < cajaX; j2++){
                                if(noValido){
                                    break;
                                }
                                for (int k2 = 0; k2 < cajaZ; k2++){
                                    if(noValido){
                                        break;
                                    }
                                    try{
                                        if(matrizActualizada[i+i2][j+j2][k+k2] != 1){
                                            noValido = true;
                                        }
                                    }catch(Exception e){
                                        noValido = true;
                                    }
                                    
                                }
                            
                            }
                        }
                        if(!noValido){
                            listaArr.add(new int[]{i,j,k});
                            
                        }
                    }
                }

            }
        }
        lista = new int[listaArr.size()][3];
        for (int i=0;i < listaArr.size(); i++){
            lista[i][0] = listaArr.get(i)[0];
            lista[i][1] = listaArr.get(i)[1];
            lista[i][2] = listaArr.get(i)[2];
        }
        return lista;
    }

    private static int[] seleccionarSalaRandom(int[][] listaSalas){
        Random random = new Random();
        return listaSalas[random.nextInt(listaSalas.length)];
    }



    private static List<int[]> MCTS(int[][] listaSalas) throws IOException{
        LocalTime ini = LocalTime.now();
        //for(int i = 0; i<listaSalas.length;i++){ // cambiar a listaSalas.length
        while(listaSalas.length > 0){   

            int[] pos = seleccionarSalaRandom(listaSalas); // seleccionar sala
            int[][] listaSalasAux = new int[listaSalas.length-1][3];
            int a = 0;
            for(int[] id : listaSalas){
                if(!(id[0] == pos[0] && id[1] == pos[1] && id[2] == pos[2]) && a< listaSalasAux.length){
                    listaSalasAux[a] = id;
                    a++;
                }
                
            }
            listaSalas = new int[listaSalas.length-1][3];
            listaSalas = listaSalasAux;
            
            // System.out.println("Estoy en la it de la pos " + pos[0] + " "+ pos[1] + " "+ pos[2]);
            matrizActualizada = leerMatriz();
            for(int y1 = 0;y1<cajaY;y1++){
                for(int x1 = 0;x1<cajaX;x1++){
                    for(int z1 = 0;z1<cajaZ;z1++){
                        matrizActualizada[pos[0]+y1][pos[1]+x1][pos[2]+z1] = 0;
                    }
                }
            }
            List<int[]> listaAct = new ArrayList<>();
            listaAct.add(pos);
            int[][] posCajasNuevas = obtenerPosValidas(cajaX, cajaY, cajaZ);
            // System.out.println(listaAct.size());
            // for (int[] elem : listaAct){
            //     System.out.println("elem act pos " + elem[0] + " " + elem[1] + " " + elem[2]);
            // }
            MCTSAux(posCajasNuevas, listaAct);
            if(mejorLista.size() < listaAct.size()){
                mejorLista = new ArrayList<>();
                for (int[] elem : listaAct){
                    mejorLista.add(elem);
                }
            }
            // System.out.println(listaAct.size());
            // for (int[] elem : listaAct){
            //     System.out.println("elem " + elem[0] + " " + elem[1] + " " + elem[2]);
            // }

        }

        // mejorLista.remove(mejorLista.size()-1);
        LocalTime fin = LocalTime.now();
        System.out.println("Posiciones obtenidas en " + Duration.between(ini, fin).toMillis() + "ms.");
        return mejorLista;
    } 

    private static List<int[]> MCTSAux(int[][] listaSalas, List<int[]> listaAct){
        // System.out.println("ListaSalas tiene " + listaSalas.length + " salas:");
        // for(int i = 0; i< listaSalas.length; i++){
        //     System.out.println(listaSalas[i][0] +" "
        //         +listaSalas[i][1] +" "+listaSalas[i][2]);
        // }
        // System.out.println("matriz antes de actualizarse:");
        // for (int l = 0; l < matrizActualizada.length; l++) { // primera dimensión
        //     for (int j = 0; j < matrizActualizada[l].length; j++) { // segunda dimensión
        //         for (int k = 0; k < matrizActualizada[l][j].length; k++) { // tercera dimensión
        //             System.out.print(matrizActualizada[l][j][k] + " ");
        //         }
        //         System.out.print(" , ");
        //     }
        //     System.out.println("");
        // }
        if(listaSalas.length == 0){
            if(mejorLista.size() < listaAct.size()){
                mejorLista = listaAct;
            }
            // System.out.println("He llegado al final de una rama");
            // for(int i= 0; i< listaAct.size();i++){
            //     System.out.println(listaAct.get(i)[0] + " " + listaAct.get(i)[1]+ " " + listaAct.get(i)[2]);
            // }
            return listaAct;
        }
        //for(int i = 0; i<listaSalas.length;i++){
        while(listaSalas.length > 0){
            //int[] pos = listaSalas[i];

            int[] pos = seleccionarSalaRandom(listaSalas);
            int[][] listaSalasAux = new int[listaSalas.length-1][3];
            int b = 0;
            for(int[] id : listaSalas){
                if(!id.equals(pos)){
                    listaSalasAux[b] = id;
                    b++;
                }
                
            }
            listaSalas = listaSalasAux;

            List<int[]> aux = new ArrayList<>(); // clonar listaAct
            for (int[] a : listaAct){
                aux.add(a);
            }
            for(int y1 = 0;y1<cajaY;y1++){
                for(int x1 = 0;x1<cajaX;x1++){
                    for(int z1 = 0;z1<cajaZ;z1++){
                        matrizActualizada[pos[0]+y1][pos[1]+x1][pos[2]+z1] = 0;
                    }
                }
            }
            // System.out.println("matriz despues de actualizarse:");
            // for (int l = 0; l < matrizActualizada.length; l++) { // primera dimensión
            //     for (int j = 0; j < matrizActualizada[l].length; j++) { // segunda dimensión
            //         for (int k = 0; k < matrizActualizada[l][j].length; k++) { // tercera dimensión
            //             System.out.print(matrizActualizada[l][j][k] + " ");
            //         }
            //         System.out.print(" , ");
            //     }
            //     System.out.println("");
            // }
            listaAct.add(pos);
            // System.out.println("meto " + pos[0] + " " + pos[1] + " " + pos[2] + " a listaAct");
            // System.out.println("tamaño: " + listaAct.size());
            // System.out.println("He metido: "+pos[0] + " " + pos[1]+ " " + pos[2]);
            int[][] posCajasNuevas = obtenerPosValidas(cajaX, cajaY, cajaZ);
            // System.out.println("salas nuevas en posCajasNuevas: " + posCajasNuevas.length);
            listaAct = MCTSAux(posCajasNuevas, listaAct);
            
            for(int y1 = 0;y1<cajaY;y1++){
                for(int x1 = 0;x1<cajaX;x1++){
                    for(int z1 = 0;z1<cajaZ;z1++){
                        matrizActualizada[pos[0]+y1][pos[1]+x1][pos[2]+z1] = 1;
                    }
                }
            }

            // System.out.println("matriz tras recursion:");
            // for (int l = 0; l < matrizActualizada.length; l++) { // primera dimensión
            //     for (int j = 0; j < matrizActualizada[l].length; j++) { // segunda dimensión
            //         for (int k = 0; k < matrizActualizada[l][j].length; k++) { // tercera dimensión
            //             System.out.print(matrizActualizada[l][j][k] + " ");
            //         }
            //         System.out.print(" , ");
            //     }
            //     System.out.println("");
            // }
            listaAct = aux;
        }
        return listaAct;
    }



    

    // protected void setup() {
    //     System.out.println("Agente "+getLocalName()+" ha empezado");
    //     // matriz = new int[y][x][z];
    //     // int[][] posCajas;
    //     try {
    //         // matriz = leerMatriz(); // de texto a matriz
    //         // matrizActualizada = leerMatriz();
    //         // posCajas = obtenerPosValidas(cajaX,cajaY,cajaZ);
    //         // System.out.println(posCajas.length);
    //         // for(int i = 0; i< posCajas.length; i++){
    //         //     System.out.println(posCajas[i][0] +" "
    //         //         +posCajas[i][1] +" "+posCajas[i][2]);
    //         // }

    //         // List<int[]> salasAGenerar = MCTS(posCajas);
    //         // System.out.println("Salas a generar: "+salasAGenerar.size());
    //         // FileWriter myWriter = new FileWriter("listaSalasAGenerar.txt");
    //         // String listaSalas = "";
    //         // for(int i= 0; i< salasAGenerar.size();i++){
    //         //     System.out.println(salasAGenerar.get(i)[0] + " " + salasAGenerar.get(i)[1]+ " " + salasAGenerar.get(i)[2]);
    //         //     listaSalas += salasAGenerar.get(i)[0] + " " + salasAGenerar.get(i)[1]+ " " + salasAGenerar.get(i)[2] + "\n";
    //         // }
    //         // myWriter.write(listaSalas);
    //         // myWriter.close();
    //         // ACLMessage reply = new ACLMessage(ACLMessage.INFORM);
	// 	    // reply.addReceiver(null); // cambiar
	// 	    // reply.setContent("exiting");
	// 	    // send(reply);
    //     } catch (IOException e) {
    //         System.out.println("Error al importar la matriz");
    //     }
    // } 

    // Salas: (X Y Z)
    static int[] sala1 = new int[]{7,3,7}; // puerta
    static int[] sala2 = new int[]{17,7,17}; // sala cuadrada 1
    static int[] sala3 = new int[]{10,4,10}; // sala cuadrada 2
    static int[] sala4 = new int[]{14,6,18}; // sala rectangular 1
    static int[] sala5 = new int[]{18,6,14}; // sala rectangular 2
    static int[] sala6 = new int[]{20,7,20}; // sala final
    static int[] sala7 = new int[]{15,12,15}; // sala torre
    static int[][] salas = new int[][]{sala1,sala2,sala3,sala4,sala5,sala6,sala7};
    static int[] valorSala = new int[]{10,10,4,5,5,10,8};
    static List<Integer> salasVisitadas = new ArrayList<>();
    static int[][] area;

    private static int[] selecSalaRnd(){
        List<Integer> indicesMayores = new ArrayList<>();
        int mayorValor = 0;
        for(int i=0;i<valorSala.length;i++){
            if(valorSala[i] >= mayorValor && !salasVisitadas.contains(i)){
                mayorValor = valorSala[i];
            }
        }
        for(int i=0;i<valorSala.length;i++){
            if(valorSala[i] == mayorValor && !salasVisitadas.contains(i)){
                indicesMayores.add(i);
            }
        }
        if(indicesMayores.size() == 0){
            return null;
        }
        Random random = new Random();
        int index = random.nextInt(indicesMayores.size());
        salasVisitadas.add(indicesMayores.get(index));
        int[] aux = new int[3];
        aux[0] = salas[indicesMayores.get(index)][0];
        aux[1] = salas[indicesMayores.get(index)][2];
        aux[2] = indicesMayores.get(index);
        return aux;
    }


    private static void mctsV2(){
        int[] sala= selecSalaRnd();
        List<int[]> posicionesSalas = new ArrayList<>(); // [X, Z, indiceSala]
        do{
            // System.out.println(sala[0] + " " + sala[1] + " " + sala[2]);
            //obtener posibles posiciones
            posicionesSalas = obtenerPosValidasV2(sala[0], sala[1], sala[2]);
            if(posicionesSalas.size() == 0){
                break;
            }
            // System.out.println(posicionesSalas.size());
            //elegir una pos random
            Random random = new Random();
            int index = random.nextInt(posicionesSalas.size());
            int[] pos = posicionesSalas.get(index);
            // System.out.println(pos[0] + " " + pos[1] + " ");
            //ocupar area
            for(int i=pos[0]; i<pos[0]+sala[0];i++){
                for(int j=pos[1]; j<pos[1]+sala[1];j++){
                    area[i][j] = sala[2]+1;
                }
            }
            // for (int i = 0; i<40; i++){
            //     for (int j = 0; j<40; j++){
            //         System.out.print(area[i][j] + " ");
            //     }
            //     System.out.println("");
            // }
            sala = selecSalaRnd(); 
        }while(sala!=null);
    }

    private List<int[]> mctsV2Aux(){
        int[] sala;
        List<int[]> posicionesSalas = new ArrayList<>(); // [X, Z, indiceSala]
        do{
            sala = selecSalaRnd();


        }while(sala!=null);

        return posicionesSalas;
    }

    private static List<int[]> obtenerPosValidasV2(int salaX, int salaZ, int id){
        List<int[]> pos = new ArrayList<>();
        boolean esValido = true;
        for(int i=0;i+salaX<area.length;i++){
            for(int j=0;j+salaZ<area[i].length;j++){
                esValido = true;
                if(id==0){
                    if(j==0 || j+salaZ==area[i].length-1){
                        if(area[i][j] == 0){
                            for(int k=0;i+k<i+salaX;k++){
                                for(int l=0;j+l<j+salaZ;l++){
                                    if(area[i+k][j+l] != 0){
                                        esValido = false;
                                    }
                                }
                            }
                            if(esValido){
                                int[] sala = new int[]{i,j};
                                pos.add(sala);
                            }
                        }
                    }
                }else{
                    if(area[i][j] == 0){
                        for(int k=0;i+k<i+salaX;k++){
                            for(int l=0;j+l<j+salaZ;l++){
                                if(area[i+k][j+l] != 0){
                                    esValido = false;
                                }
                            }
                        }
                        if(esValido){
                            int[] sala = new int[]{i,j};
                            pos.add(sala);
                        }
                    }
                }
            }
        }
        return pos;
    }







    protected void setup(){
        System.out.println("Agente "+getLocalName()+" ha empezado");
        int[] pos = new int[3], tam = new int[3]; // pos: bloque en el que generar
        List<int[]> posId = new ArrayList<>(); //[X, Z, indiceSala]
        try {
            int[][] posTam = leerPosTam();
            pos = posTam[0];
            tam = posTam[1];
        } catch (IOException e) {
            System.out.println("Error al leer el archivo pos.txt");
        }
        area = new int[tam[0]][tam[2]];
        // for (int i = 0; i<tam[0]; i++){
        //     for (int j = 0; j<tam[2]; j++){
        //         area[i][j] = 0;
        //     }
        // }
        while(salasVisitadas.size() < 6){
            for (int i = 0; i<tam[0]; i++){
                for (int j = 0; j<tam[2]; j++){
                    area[i][j] = 0;
                }
            }
            salasVisitadas = new ArrayList<>();
            mctsV2();
            // System.out.println(salasVisitadas.size());
        }
        for (int i = 0; i<40; i++){
            for (int j = 0; j<40; j++){
                System.out.print(area[i][j] + " ");
            }
            System.out.println("");
        }
        
        // System.out.println(tam[0] + " " + tam[1] + " " + tam[2] + " ");

    }

}
