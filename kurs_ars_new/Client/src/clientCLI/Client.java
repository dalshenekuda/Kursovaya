package clientCLI;

import java.io.*;
import java.net.Inet4Address;
import java.net.Socket;
import java.util.Scanner;

public class Client
{
    public Socket clientSocket;
    private Scanner input;
    private String serverName;
    public boolean operationTerminated;
    public Client(Inet4Address srvAddr, int srvPort, String srvName) throws IOException
    {
        try
        {
            clientSocket = new Socket(srvAddr, srvPort);
            serverName = srvName;
            input = new Scanner(System.in);
        }
        catch (IOException connException)
        {
            throw connException;
        }
    }
    public void run()
    {
        System.out.println("Соединение с сервером " + serverName + ":" + clientSocket.getPort() + "...");
        DataInputStream srvOutput = null; DataOutputStream srvInput = null;
        try
        {
            srvOutput = new DataInputStream(clientSocket.getInputStream()); //response from server
            srvInput = new DataOutputStream(clientSocket.getOutputStream()); //query to server
            System.out.println("Соединение с сервером " + serverName + ":" + clientSocket.getPort() + " установлено.");
            String query; operationTerminated = false;
            ServerOutputProcessor sop = new ServerOutputProcessor(srvOutput, this);
            sop.start();
            while (!operationTerminated)
            {
                query = input.nextLine();
                srvInput.writeUTF(query);
            }
            srvOutput.close();
            srvOutput.close();
            clientSocket.close();
        }
        catch (IOException connException)
        {
            System.out.println("Проблема при попытке установления соединения с сервером. Перезапустите клиент и попробуйте ещё раз.");
        }
    }

}

class ServerOutputProcessor extends Thread
{
    public Client container;
    public DataInputStream srvOutput;
    public ServerOutputProcessor(DataInputStream o, Client c)
    {
        container = c;
        srvOutput = o;
    }
    public void run()
    {
        String response;
        try
        {
            while (!container.operationTerminated)
            {
                while (srvOutput.available() > 0)
                {
                    response = srvOutput.readUTF();
                    if (response.contains("terminate"))
                    {
                        container.operationTerminated = true;
                    }
                    else
                    {
                        System.out.print(response);
                    }
                }
            }
        }
        catch (IOException exception)
        {
            System.out.println("Ошибка при выводе сообщения от сервера");
        }
    }
}