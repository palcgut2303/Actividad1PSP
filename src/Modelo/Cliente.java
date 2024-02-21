/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Modelo;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;

/**
 *
 * @author ruben
 */
public class Cliente {

    //Método que se encarga de enviar mensaje al multicast del grupo.
    public void enviarMensaje(String message) throws SocketException, IOException {

        DatagramSocket socketCliente = new DatagramSocket();
        byte[] buffer = new byte[1000];

        InetAddress direccionServidor = InetAddress.getByName("25.50.57.40");
        int puertoServidor = 9876;

        String mensajeInicioSesion = "INICIAR SESION";
        buffer = mensajeInicioSesion.getBytes();
        DatagramPacket registrationPacket = new DatagramPacket(buffer, buffer.length, direccionServidor, puertoServidor);
        socketCliente.send(registrationPacket);

        buffer = message.getBytes();
        DatagramPacket sendPacket = new DatagramPacket(buffer, buffer.length, direccionServidor, puertoServidor);
        socketCliente.send(sendPacket);
    }

}
