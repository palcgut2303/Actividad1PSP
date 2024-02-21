/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controlador;

import Vista.VistaCliente;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.net.SocketAddress;
import java.net.UnknownHostException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Pablo Alcudia
 */
//Clase controladora para recibir mensajes ya que estara en un hilo aparte, y estara recibiendo mensajes constantemente.
public class HiloControlador extends Thread {

    VistaCliente vistaCliente;

    public HiloControlador(VistaCliente i) {
        this.vistaCliente = i;
    }

    @Override
    public void run() {
        try {
            receiveMessage();
        } catch (IOException ex) {
            Logger.getLogger(HiloControlador.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public String receiveMessage() throws IOException {

        //Inicia el socket del multicast, se conecta al grupo del servidor, donde debe de recibir los mensajes.
        int puerto = 12345;//Puerto multicast
        MulticastSocket ms = new MulticastSocket(puerto);
        //Nos unimos al grupo multicast
        InetAddress grupo = InetAddress.getByName("225.0.0.1");
        ms.joinGroup(grupo);

        String msg = "";
        while (msg != null) {
            // El buffer se crea dentro del bucle para que se sobrescriba
            // con cada nuevo mensaje
            byte[] buf = new byte[4096];
            DatagramPacket paquete = new DatagramPacket(buf, buf.length);

            //Recibe el paquete del servidor multicast
            ms.receive(paquete);
            msg = new String(paquete.getData());
            System.out.println("Recibo del servidor: " + msg.trim());

            //Metodo para sacar la posicion del caracter ":", para poder sacar el mensaje solo. 
            //Ya que mi formato de mensaje es NombreUsuario: Mensaje.
            int indicePunto = sacarNombreUsuario(msg);
            String cadena = msg.substring(0, indicePunto);
            if (!cadena.contains(vistaCliente.getUsuario())) {
                vistaCliente.appendTextArea("", msg);
            }
        }
        ms.leaveGroup(grupo);
        ms.close();

        return null;
    }

    private int sacarNombreUsuario(String msg) {
        //Bucle para añadir en el textArea del cliente solo el usuario del que recibe, no su propio usuario.
        int indicePunto = 0;
        for (int i = 0; i < msg.length(); i++) {
            if ((msg.charAt(i) + "").equals(":")) {
                indicePunto = i;
            }
        }
        return indicePunto;
    }
}
