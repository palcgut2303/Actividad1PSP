/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Modelo;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.net.SocketException;
import java.util.ArrayList;

/**
 *
 * @author ruben
 */
public class Servidor {
    
    public static void main(String[] args) throws SocketException, IOException {
        
        //Hacemos un array de clientes
       ArrayList<String> listaDeNombres = new ArrayList<>();
        
        //Puerto por el que escucha el servidor a los clientes 
         DatagramSocket serverSocket = new DatagramSocket(12347);
         
          //Recibir los mensajes
         MulticastSocket ms = new MulticastSocket(12346);
         
         //Asignamos la ip para que la gente se una. 
         InetAddress grupo = InetAddress.getByName("225.0.0.1");
         ms.joinGroup(grupo);
         String msg = "";
         String mensajeUsuario = "";
         String usuario= "";
         
         // El buffer se crea dentro del bucle para que se sobrescriba
         // con cada nuevo mensaje
         byte[] buf = new byte[1000];
        DatagramPacket paquete = new DatagramPacket(buf, buf.length);
        
        //Recibe el paquete de los clientes
        while (!msg.equals("*")) {
            
            ms.receive(paquete);
            msg = new String(paquete.getData());
            String idUsuario = extractUsername(msg);
            if (msg.startsWith("USUARIO:")) {
                usuario = msg.substring("USUARIO:".length());
                listaDeNombres.add(usuario);
            }
            else{
                mensajeUsuario = msg;  
                //Enviar el meensaje para todos los que están conectacdos al grupo
                 DatagramPacket paquetEnviar = new DatagramPacket(mensajeUsuario.getBytes(), mensajeUsuario.length(), grupo, 12346);
                 for (String u : listaDeNombres) {
                     if (!idUsuario.equals(u.trim())) {
                       ms.send(paquetEnviar);
                       System.out.println( usuario + msg.trim());
                     }
                }
              
            }
        }
        
        
        ms.close();
         
         
    }
    
    private static String extractUsername(String message) {
        // Encontrar la posición del primer espacio
        int spaceIndex = message.indexOf(' ');

        // Verificar si se encontró un espacio
        if (spaceIndex != -1) {
            // Extraer la subcadena desde el inicio hasta el primer espacio
            return message.substring(0, spaceIndex);
        } else {
            // Si no hay espacio, manejar según tus necesidades
            System.out.println("No se encontró espacio en el mensaje: " + message);
            return null;
        }
    }
    
     
   
}
