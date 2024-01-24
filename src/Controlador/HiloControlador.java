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
import java.net.UnknownHostException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Pablo Alcudia
 */
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

        MulticastSocket ms = iniciar();
        //ms.joinGroup(mcastaddr, netIf);
        String msg = "";
        while (true) {
            // El buffer se crea dentro del bucle para que se sobrescriba
            // con cada nuevo mensaje
            byte[] buf = new byte[1000];
            DatagramPacket paquete = new DatagramPacket(buf, buf.length);
            //Recibe el paquete del servidor multicast
            ms.receive(paquete);
            msg = new String(paquete.getData());
            System.out.println("Recibo: " + msg.trim());

            int indicePunto = sacarNombreUsuario(msg);
            String cadena = msg.substring(0, indicePunto);
            if (!cadena.contains(vistaCliente.getUsuario())) {
                vistaCliente.appendTextArea("", msg);
            }
        }
    }

    private MulticastSocket iniciar() throws UnknownHostException, IOException {
        int puerto = 12346;//Puerto multicast
        MulticastSocket ms = new MulticastSocket(puerto);
        //Nos unimos al grupo multicast
        InetAddress grupo = InetAddress.getByName("225.0.0.1");
        ms.joinGroup(grupo);
        return ms;
    }

    private int sacarNombreUsuario(String msg) {
        //Bucle para añadir en el textArea del cliente solo el usuario del que recibe, no su propio usuario.
        int indicePunto = 0;
        for (int i = 0; i < msg.length(); i++) {
            if ((msg.charAt(i)+"").equals(":")) {
                indicePunto = i;
            }
        }
        return indicePunto;
    }
}
