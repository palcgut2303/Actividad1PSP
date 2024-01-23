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
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Pablo Alcudia
 */
public class HiloControlador extends Thread {

    VistaCliente i;

    public HiloControlador(VistaCliente i) {
        this.i = i;
    }

    @Override
    public void run() {
        try {
            recibirMensaje();
        } catch (IOException ex) {
            Logger.getLogger(HiloControlador.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public String recibirMensaje() throws IOException {

        int puerto = 12346;//Puerto multicast
        MulticastSocket ms = new MulticastSocket(puerto);
        //Nos unimos al grupo multicast
        InetAddress grupo = InetAddress.getByName("225.0.0.1");
        ms.joinGroup(grupo);
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

            int indiceSegundoCorchete = 0;
            for (int j = 0; j < msg.length(); j++) {
                if ((msg.charAt(j) + "").equals("]")) {
                    indiceSegundoCorchete = j;
                }
            }
            String cadenaCorchetes = msg.substring(0, indiceSegundoCorchete);
            if (!cadenaCorchetes.contains(i.getUsuario())) {
                i.escribirText("", msg);
            }
        }
    }
}
