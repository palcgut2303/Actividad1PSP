/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Modelo;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.util.Scanner;

/**
 *
 * @author ruben
 */
public class Cliente {

    private static final int puertoServidor = 12346;
    private static final String direccionGrupo = "225.0.0.1";
    private static String user;

    public String getUser() {
        return user;
    }

    public  void setUser(String user) {
        Cliente.user = user;
    }

    public Cliente() {
    }
    
    
    public static void main(String[] args) {
        try {
            MulticastSocket multicastSocket = new MulticastSocket(puertoServidor);
             InetAddress group = InetAddress.getByName(direccionGrupo);
            multicastSocket.joinGroup(group);
            Scanner sc = new Scanner(System.in);
            
            
            System.out.println("Introduce tu usuario: ");
            user = sc.nextLine();
            
            login(multicastSocket, user);
            while (true) {
                // Enviar mensajes al grupo multicast
                System.out.print("Enter message: ");
                String sendMessage = sc.nextLine();
               /* byte[] sendData = sendMessage.getBytes();
                DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, group, 12346);
                multicastSocket.send(sendPacket);*/
                sendMessage(multicastSocket, sendMessage);
                
               // Recibir mensajes del servidor
                byte[] receiveData = new byte[1024];
                DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
                multicastSocket.receive(receivePacket);

                String message = new String(receivePacket.getData(), 0, receivePacket.getLength());
                System.out.println("Received message: " + message);
            }
            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    //Método que sirve para enviar al servidor el usuario que se ha registrado.
    private static void login(MulticastSocket socket, String username) {
        String loginMessage = "USUARIO:" + username;
        sendRequest(socket, loginMessage);
    }
    
    
    //Este método toma un mensaje como parámetro y un datagramaSocket. Se introduce un string con el mensaje, que se va a enviar al servidor
    //Se instancia el metodo sendRequest para enviar la peticion al sevidor
    private static void sendMessage(MulticastSocket socket, String message) {
    String sendMessage = user+" ha enviado un mensaje: " + message;
    sendRequest(socket, sendMessage);
    }

    private static void sendRequest(MulticastSocket socket, String request) {
        try {
            byte[] sendData = request.getBytes();
            DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, java.net.InetAddress.getByName("localhost"), puertoServidor);
            socket.send(sendPacket);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    
    //Este método se ejecuta en un bucle infinito y se encarga de recibir mensajes del servidor constantemente.
    //Se implementa en un hilo aparte, para que tambien pueda el cliente a parte de recibir, enviar mensajes.
    private static void receiveMessages(DatagramSocket socket) {
    try {
        while (true) {
            byte[] receiveData = new byte[1024];
            DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
            socket.receive(receivePacket);

            String message = new String(receivePacket.getData(), 0, receivePacket.getLength());
            System.out.println("\nMensaje del servidor:"+message);
             // Comprobación para cerrar la sesión si el mensaje contiene un "*"
            if (message.trim().equals("*")) {
                System.out.println("Sesión cerrada por el servidor.");
                break; // Salir del bucle y cerrar la aplicación
            }
        }
    } catch (Exception e) {
        e.printStackTrace();
    }
}
}
