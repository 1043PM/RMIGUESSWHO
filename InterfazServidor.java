/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
//package RMIAdivinaQuien;

import java.io.File;
import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 *
 * @author ale
 */
public interface InterfazServidor extends Remote {
    
    public String registrar (String login)  throws RemoteException;
     
    public void difundirMensaje (String mensaje) throws RemoteException;
    
    public void incrementarJugada() throws RemoteException;
    
    public int getPersonajeAdivinar() throws RemoteException;
     
    public boolean desconectar (String login) throws RemoteException;
}
