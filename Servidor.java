/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
//package RMIAdivinaQuien;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author ale
 */
public class Servidor implements InterfazServidor {
    static ArrayList<String> usuariosRegistrados;
    static String hostRegistry;
    static int personajeAdivinar;
    private static volatile int numeroJugada;   
    private final int MAX_JUGADAS = 10;
    private Thread hiloEsperar;
    private int numeroAleatorio;
    private int complementoNumeroAleatorio;
         
    public Servidor(){        
        usuariosRegistrados = new ArrayList();        
        personajeAdivinar = elegirAleatoriamentePersonaje();
        numeroJugada = 0;
    }
        
    public static void main(String args[]) {
                
        Servidor server = new Servidor();
        server.inicializarObjetoRemoto();        
        server.esperarContrincantes(server);
        
    }
    
    private void esperarContrincantes(Servidor server){       
        hiloEsperar = new Thread(new Runnable() {
            @Override
            public void run() {
                boolean buscandoContrincante = true;
                while(buscandoContrincante){   
                System.out.println("buscando");            
                    if(server.contrincantesEncontrados()){
                        try {
                            System.out.println("ya hay dos usuarios!");
                            server.empezarJuego();                
                            buscandoContrincante = false;
                        } catch (RemoteException | NotBoundException ex) {
                            Logger.getLogger(Servidor.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }
                }
            }
        });
        hiloEsperar.start();
    }
    
    
    public void inicializarObjetoRemoto() {                      
        try {            
            InterfazServidor interfaz = new Servidor();
            InterfazServidor stub = (InterfazServidor) UnicastRemoteObject.exportObject(interfaz, 0);
            
            Registry registry = LocateRegistry.getRegistry();
            String nombre_objeto_remoto = "servidor";
            registry.rebind(nombre_objeto_remoto, stub);
            System.out.println("Servidor...");
            
        } catch (RemoteException ex) {

            Logger.getLogger(Servidor.class.getName()).log(Level.SEVERE, null, ex);
        }        
    } 

    @Override
    public String registrar(String login) throws RemoteException {
        String respuesta;
        
        if(usuariosRegistrados.contains(login)) {
            respuesta = "usuarioDuplicado";
        }else {
            usuariosRegistrados.add(login);
            respuesta = "usuarioAnadido";
            System.out.println("Usuario " + login +" registrado.");
        }        
        return respuesta;
    }

    @Override
    public void difundirMensaje(String mensaje) throws RemoteException {
        for (int i = 0 ; i<usuariosRegistrados.size() ; i++) {
            mostrarMensaje(usuariosRegistrados.get(i), mensaje);            
        }
    }

    @Override
    public void incrementarJugada() throws RemoteException {
        numeroJugada++;
    }

    @Override
    public int getPersonajeAdivinar() throws RemoteException {
        return personajeAdivinar;
    }

    @Override
    public boolean desconectar(String login) throws RemoteException {        
        boolean desconectado = false;
        
        //Quitamos al usuario del mapa de usuarios registrados
        desconectado = usuariosRegistrados.remove(login);
        
        if(desconectado) { 
            System.out.println("Usuario " + login + " desconectado");
        }        
        return desconectado;
    }
   
    
    public void mostrarMensaje(String nombreObjetoRemoto, String mensaje) {
                
        try {
            Registry registry = LocateRegistry.getRegistry(hostRegistry);
            System.out.print("Buscando el objeto remoto de "+nombreObjetoRemoto + "...");
            String nombre_objeto_remoto = nombreObjetoRemoto;
            InterfazCliente instancia_local = (InterfazCliente) registry.lookup(nombre_objeto_remoto);
            System.out.println(" Objeto remoto encontrado");

            //Envia el mensaje al usuario            
            instancia_local.mostrarMensaje(mensaje);
            System.out.println("Mensaje enviado");  
        } catch (RemoteException | NotBoundException ex) {
            Logger.getLogger(Servidor.class.getName()).log(Level.SEVERE, null, ex);
        }   

    }
    
    private int elegirAleatoriamentePersonaje(){
        return  Math.abs((int) (Math.random() * 21));
                              
    }
    
    private boolean contrincantesEncontrados(){        
        return usuariosRegistrados.size() == 2;
    }
    
    private void empezarJuego() throws RemoteException, NotBoundException{        
        enviarEmpezarJuego();
       
        while(true){
            if(numeroJugada==MAX_JUGADAS){
                enviarSetGanador();
                System.out.println("Termino el juego!");
                System.exit(0);
            }
        }
                
    }
        
    private int complemento(){
        if(numeroAleatorio == 1){
            return 0;
        }else{
            return 1;
        }       
    }    
    
    private void enviarEmpezarJuego(){        
        numeroAleatorio =(int) (Math.random() * 2);       
        complementoNumeroAleatorio = complemento();
        
        for (int i = 0 ; i<usuariosRegistrados.size() ; i++) {
            try {
                if(i == numeroAleatorio){
                    System.out.println("se intancio con true "+ usuariosRegistrados.get(numeroAleatorio));
                    instanciarObjeto((usuariosRegistrados.get(i))).empezarJuego(true);
                }else if(i == complementoNumeroAleatorio){
                    System.out.println("se intancio con false "+ usuariosRegistrados.get(complementoNumeroAleatorio));
                    instanciarObjeto((usuariosRegistrados.get(i))).empezarJuego(false);
                }
            } catch (RemoteException | NotBoundException ex) {
                Logger.getLogger(Servidor.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    
    private void enviarSetGanador() throws RemoteException, NotBoundException{
        int contador = 0;
        for(String nombreObjetoRemoto:usuariosRegistrados){
            if(contador == numeroAleatorio){
                instanciarObjeto(nombreObjetoRemoto).setGanador(false);
            }else if(contador == complementoNumeroAleatorio){
                instanciarObjeto(nombreObjetoRemoto).setGanador(true);
            }
        }
    }
    
    private InterfazCliente instanciarObjeto(String nombreObjetoRemoto) throws RemoteException, NotBoundException{
        Registry registry = LocateRegistry.getRegistry(hostRegistry);
        System.out.print("Buscando el objeto remoto de "+nombreObjetoRemoto + "...");
        String nombre_objeto_remoto = nombreObjetoRemoto;
        InterfazCliente instancia_local = (InterfazCliente) registry.lookup(nombre_objeto_remoto);
        
        return instancia_local;
        
    }
}