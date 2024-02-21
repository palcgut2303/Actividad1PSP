/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Modelo;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.MulticastSocket;

/**
 *
 * @author ruben
 */
public class Servidor {

    public static void main(String args[]) throws Exception {
        DatagramSocket serverSocket = new DatagramSocket(12347);
        int tamañoBuffer = 1000;
        byte[] buffer = new byte[tamañoBuffer];

        while (true) {
            DatagramPacket receivePacket = new DatagramPacket(buffer, buffer.length);
            serverSocket.receive(receivePacket);

            InetAddress clientAddress = receivePacket.getAddress();
            int clientPort = receivePacket.getPort();

            String receivedMessage = new String(receivePacket.getData(), 0, receivePacket.getLength());

            if (receivedMessage.equalsIgnoreCase("INICIAR SESION")) {

                String registrationAck = "Registration successful";
                buffer = registrationAck.getBytes();
                DatagramPacket registrationAckPacket = new DatagramPacket(buffer, buffer.length, clientAddress, clientPort);
                serverSocket.send(registrationAckPacket);

            } else {

                MulticastSocket ms = new MulticastSocket();
                // Se escoge un puerto para el server
                int puerto = 12346;
                // Se escoge una dirección para el grupo
                InetAddress grupoMulticast = InetAddress.getByName("225.0.0.1");

                String cadena = receivedMessage;
                System.out.print("Mensaje a enviar: "+cadena);
                // Enviamos el mensaje a todos los clientes que se hayan unido al grupo
                DatagramPacket paquete = new DatagramPacket(cadena.getBytes(), cadena.length(), grupoMulticast, puerto);
                ms.send(paquete);

            }

        }
    }

}
