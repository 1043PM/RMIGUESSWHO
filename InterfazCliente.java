/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
//package RMIAdivinaQuien;

import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 *
 * @author ale
 */
public interface InterfazCliente extends Remote {
    
    public void mostrarMensaje(String mensaje) throws RemoteException;
    
    public void empezarJuego(boolean adivinador) throws RemoteException;
    
    public boolean getStatus() throws RemoteException;
    
    public void setGanador(boolean ganador) throws RemoteException;
}
