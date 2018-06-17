/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
//package RMIAdivinaQuien;

import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import javax.swing.ImageIcon;
import javax.swing.JButton;

/**
 *
 * @author ale
 */
public class Boton extends JButton implements ActionListener{
    private final String RUTA_X="./personajes/x.png";    
    private final String RUTA_PERSONAJES = "./personajes";
    
    public Boton(int posX, int posY, int ancho, int alto){       
        setBounds(posX, posY, ancho, alto);
        addActionListener(this);        
    }
    
    @Override
    public void actionPerformed(ActionEvent e) {        
        ImageIcon personaje = new ImageIcon(RUTA_X);
        //Image imagen = personaje.getImage().getScaledInstance(this.getWidth(),this.getHeight(), 0);
        Image imagen = personaje.getImage().getScaledInstance(74,71, 0);
        this.setIcon(new ImageIcon(imagen));           
        this.setEnabled(false);        
    }
    
    public void setPersonaje(File f, boolean seleccionable){
        ImageIcon personaje = new ImageIcon(f.getPath());
        //Image imagen = personaje.getImage().getScaledInstance(this.getWidth(),this.getHeight(), 0);
        Image imagen = personaje.getImage().getScaledInstance(74,71, 0);
        this.setIcon(new ImageIcon(imagen));           
        this.setEnabled(seleccionable);        
    }
    
    public void setPersonajeRan(int rand){
        String sDirectorio = RUTA_PERSONAJES;
        File f = new File(sDirectorio);        
        
        if(f.exists()){ 
            File[] ficheros = f.listFiles();
            //List<File> list = Arrays.asList(ficheros);            
            ImageIcon personaje = new ImageIcon(ficheros[rand].getPath());
            Image imagen = personaje.getImage().getScaledInstance(this.getWidth(),this.getHeight(), 0);
            this.setIcon(new ImageIcon(imagen));           
            this.setEnabled(false); 
            
        }else{
            System.err.println("Cliente: Error Boton setPersonajeRan, directorio no encontrado");
        }
    }
    
}
