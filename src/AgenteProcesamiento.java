package src;

import jade.core.Agent;
import jade.core.behaviours.Behaviour;
import jade.core.behaviours.CyclicBehaviour;
import jade.core.behaviours.OneShotBehaviour;
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
    public static int cajaX = 7;
    public static int cajaY = 3;
    public static int cajaZ = 7;
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



    private static List<int[]> MCTS(int[][] listaSalas) throws IOException{
        for(int i = 0; i<listaSalas.length;i++){ // cambiar a listaSalas.length
            int[] pos = listaSalas[i]; // seleccionar sala
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
        for(int i = 0; i<listaSalas.length;i++){
            int[] pos = listaSalas[i];
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

    

    protected void setup() {
        System.out.println("Agente "+getLocalName()+" ha empezado");
        matriz = new int[y][x][z];
        int[][] posCajas;
        try {
            matriz = leerMatriz(); // de texto a matriz
            matrizActualizada = leerMatriz();
            posCajas = obtenerPosValidas(cajaX,cajaY,cajaZ);
            // System.out.println(posCajas.length);
            // for(int i = 0; i< posCajas.length; i++){
            //     System.out.println(posCajas[i][0] +" "
            //         +posCajas[i][1] +" "+posCajas[i][2]);
            // }

            List<int[]> memuero = MCTS(posCajas);
            System.out.println(memuero.size());
            for(int i= 0; i< memuero.size();i++){
                System.out.println(memuero.get(i)[0] + " " + memuero.get(i)[1]+ " " + memuero.get(i)[2]);
            }
        } catch (IOException e) {
            System.out.println("Error al importar la matriz");
        }
    } 

}
