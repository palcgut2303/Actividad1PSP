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
        byte[] buffer = new byte[4096];

        InetAddress direccionServidor = InetAddress.getLocalHost();
        int puertoServidor = 12347;

        login(direccionServidor, puertoServidor, socketCliente);

        buffer = message.getBytes();
        DatagramPacket sendPacket = new DatagramPacket(buffer, buffer.length, direccionServidor, puertoServidor);
        socketCliente.send(sendPacket);
    }

    private void login(InetAddress direccionServidor, int puertoServidor, DatagramSocket socketCliente) throws IOException {
        byte[] buffer;
        String mensajeInicioSesion = "INICIAR SESION";
        buffer = mensajeInicioSesion.getBytes();
        DatagramPacket registrationPacket = new DatagramPacket(buffer, buffer.length, direccionServidor, puertoServidor);
        socketCliente.send(registrationPacket);
    }

}
