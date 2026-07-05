package Server;

import java.io.*;
import java.net.*;
import java.util.*;

public class Servidor extends Thread
{
    public static Vector usuarios = new Vector();
    private static boolean running = true;
    private static ServerSocket sfd;

    public static void main (String args[])
    {
        try
        {
            sfd = new ServerSocket(7000);
        }
        catch (IOException ioe)
        {
            System.out.println("Comunicacion rechazada."+ioe);
            System.exit(1);
        }

        while (running)
        {
            try
            {
                Socket nsfd = sfd.accept();
                System.out.println("Conexion aceptada de: "+nsfd.getInetAddress());
                DataInputStream oos = new DataInputStream(nsfd.getInputStream());
                DataOutputStream escritor =  new DataOutputStream(new FileOutputStream(new File("clinica_respaldo.dat")));

                int unByte;
                try {
                    while((unByte = oos.read())!=-1) 
                        escritor.write(unByte);
                }
                catch (SocketException se) {
                    System.out.println("Cliente desconectado: " + se.getMessage());
                }
                catch (IOException e) {
                    e.printStackTrace();
                } 
                finally {
                    try {
                        oos.close();
                        escritor.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

            }
            catch (SocketException se) {
                if (!running) {
                    System.out.println("Servidor detenido correctamente");
                } else {
                    System.out.println("Error: "+se);
                }
            }
            catch(IOException ioe)
            {
                System.out.println("Error: "+ioe);
            }
        }
    }

    public static void detenerServidor() {
        running = false;
        try {
            if (sfd != null) sfd.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
