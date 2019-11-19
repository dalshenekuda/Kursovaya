package clientCLI;

import java.io.IOException;
import java.net.Inet4Address;
import java.net.UnknownHostException;
import java.util.Scanner;

public class ClientMainApp
{
    public static void main(String[] args)
    {
        Scanner input = new Scanner(System.in);
        Client clientBackend = null;
        Inet4Address srvAddr = null; int srvPort; String srvName = "";
        boolean ipIsCorrect = false; boolean connectionIsEstablished = false;
        while (!ipIsCorrect)
        {
            System.out.print("Введите IP сервера: ");
            try
            {
                srvName = input.nextLine();
                srvAddr = (Inet4Address) Inet4Address.getByName(srvName);
                ipIsCorrect = true;
            }
            catch (UnknownHostException addrIncorrect)
            {
                System.out.println("Адрес введён с ошибкой, попробуйте ещё раз");
            }
        }
        while (!connectionIsEstablished)
        {
            System.out.print("Введите порт сервера: ");
            srvPort = input.nextInt();
            try
            {
                clientBackend = new Client(srvAddr, srvPort, srvName);
                connectionIsEstablished = true;
            }
            catch (IOException connException)
            {
                System.out.println("Сервер не запущен или не существует, попробуйте ещё раз");
            }
        }
        clientBackend.run();
        input.close();
    }
}
