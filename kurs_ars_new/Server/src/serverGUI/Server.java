

package serverGUI;

import ClassSch.*;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Inet4Address;
import java.net.ServerSocket;
import java.net.Socket;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.util.Map;
import java.util.Scanner;

public class Server
{
    public Schedule coursesSchedule;
    private ServerSocket srvSocket;
    public Server(int srvPort)
    {
        coursesSchedule = new Schedule();
        boolean filesParsedSuccessfully = false; boolean socketCreated = false;
        Scanner input = new Scanner(System.in);
        do
        {
            System.out.print("Введите путь к папке с исходными файлами (нажмите Enter, если они находятся в одной папке с исполняемым файлом сервера): ");

            try
            {
                coursesSchedule.parseFiles(input.nextLine());
                filesParsedSuccessfully = true;
            }
            catch (IOException fileInputException)
            {
                System.out.println(fileInputException.getMessage());
            }
        }
        while (!filesParsedSuccessfully);
        System.out.println("Загрузка информации завершена");
        try
        {
            srvSocket = new ServerSocket(srvPort, 10, Inet4Address.getByName(Inet4Address.getLocalHost().getHostAddress()));
            socketCreated = true;
        }
        catch (Exception sockCreationException)
        {
            System.out.println("Создание сокета провалено!");
        }
        if (socketCreated)
        {
            System.out.println("Сервер готов к работе. Его адрес: " + srvSocket.getInetAddress().toString().replace("/", "") + ":" + srvSocket.getLocalPort());
            listen();
        }
    }
    public void listen()
    {
        while (true)
        {
            Socket client = null;
            try
            {
                client = srvSocket.accept();
                ServerConnectionProcessor scp = new ServerConnectionProcessor(client, this);
                System.out.println("\n" + scp.getTimestamp() + " Клиент с адресом " + client.getInetAddress().toString().replace("/", "") + " подключился.");
                scp.start();
            }
            catch (IOException e)
            {
                System.out.println("Клиент с адресом " + client.getInetAddress().toString().replace("/", "")  + " отключился.");
            }
        }
    }
}

class ServerConnectionProcessor extends Thread
{
    private Socket clientSocket;
    private Server container;
    private DataOutputStream clientOutput;
    private DataInputStream clientInput;
    public ServerConnectionProcessor(Socket s, Server c)
    {
        clientSocket = s;
        container = c;
    }
    public void run()
    {
        try
        {
            clientOutput = new DataOutputStream(clientSocket.getOutputStream());
            clientInput = new DataInputStream(clientSocket.getInputStream());
            String decision;
            clientOutput.writeUTF("Успешное подключение.\n");
            do
            {
                decision = clientInput.readUTF();
                System.out.println(getTimestamp() + " Клиент с адресом " + clientSocket.getInetAddress().toString().replace("/", "") + " запросил операцию " + decision);
                switch (decision.toUpperCase(Locale.getDefault()))
                {
                    case "1":
                    {
                        clientOutput.writeUTF("\nОбработка...\n\n");
                        clientOutput.writeUTF("Список преподавателей:\n|");
                        int i = 0;
                        for (Map.Entry<String, Tutor> tutorEntry : container.coursesSchedule.getTutors().entrySet())
                        {
                            String tutorEntryKey = tutorEntry.getKey();
                            clientOutput.writeUTF(container.coursesSchedule.getTutors().get(tutorEntryKey).getName() + "|");
                            i++;
                            if (i % 4 == 0) clientOutput.writeUTF("\n|");
                        }
                        clientOutput.writeUTF("\n");
                        break;

                    }
                    case "2":
                    {
                        clientOutput.writeUTF("\nОбработка...\n\n");
                        clientOutput.writeUTF("Список студентов:\n|");
                        int i = 0;
                        for (Map.Entry<String, Student> studentEntry : container.coursesSchedule.getStudents().entrySet())
                        {
                            String studentEntryKey = studentEntry.getKey();
                            clientOutput.writeUTF(container.coursesSchedule.getStudents().get(studentEntryKey).getName() + "|");
                            i++;
                            if (i % 4 == 0) clientOutput.writeUTF("\n|");
                        }
                        clientOutput.writeUTF("\n");
                        break;
                    }
                    case "3":
                    {
                        clientOutput.writeUTF("\nОбработка...\n\n");
                        clientOutput.writeUTF("Список курсов:\n|");
                        int i = 0;
                        for (Map.Entry<String, Course> courseEntry : container.coursesSchedule.getCourses().entrySet())
                        {
                            String courseEntryKey = courseEntry.getKey();
                            clientOutput.writeUTF(container.coursesSchedule.getCourses().get(courseEntryKey).getName() + "|");
                            i++;
                            if (i % 4 == 0) clientOutput.writeUTF("\n|");
                        }
                        clientOutput.writeUTF("\n");
                        break;
                    }


                    case "4":
                    {
                        clientOutput.writeUTF("\nОбработка...\n\n");
                        clientOutput.writeUTF("Список аудиторий:\n|");
                        int i = 0;
                        for (Map.Entry<String, Classroom> classroomEntry : container.coursesSchedule.getClassrooms().entrySet())
                        {
                            String classroomEntryKey = classroomEntry.getKey();
                            clientOutput.writeUTF(container.coursesSchedule.getClassrooms().get(classroomEntryKey).getName() + "|");
                            i++;
                            if (i % 4 == 0) clientOutput.writeUTF("\n");
                        }
                        clientOutput.writeUTF("\n");
                        break;
                    }




                    case "6":
                    {
                        String studName;
                            studName = clientInput.readUTF();
                            if (container.coursesSchedule.getStudents().get(studName) != null)
                            {
                                clientOutput.writeUTF("\nСписок курсов, проходимых студентов:\n|");
                                int i = 0;
                                for(Map.Entry<String, Course> courseEntry : container.coursesSchedule.getStudents().get(studName).getAttendedCoursesList().entrySet())
                                {
                                    String courseEntryKey = courseEntry.getKey();
                                    clientOutput.writeUTF(container.coursesSchedule.getCourses().get(courseEntryKey).getName() + "|");
                                    i++;
                                    if (i % 4 == 0) clientOutput.writeUTF("\n|");
                                }
                            }
                            else
                            {
                                clientOutput.writeUTF("Студент не обнаружен, возможно имя введено некорректно");
                            }
                            clientOutput.writeUTF("\n");
                        break;
                    }
                    case "7":
                    {
                        clientOutput.writeUTF("\nОбработка...\n\n");
                        for (Map.Entry<String, Course> courseEntry : container.coursesSchedule.getCourses().entrySet())
                        {
                            String courseEntryKey = courseEntry.getKey();
                            clientOutput.writeUTF("Название курса: " + container.coursesSchedule.getCourses().get(courseEntryKey).getName() + "\n");
                            if (container.coursesSchedule.getCourses().get(courseEntryKey).getHostingTutor() != null)
                                clientOutput.writeUTF("Преподаватель: " + container.coursesSchedule.getCourses().get(courseEntryKey).getHostingTutor().getName() + "\n\n");
                        }
                        break;
                    }


                    case "8":
                    {
                        String tutorName;
                            tutorName = clientInput.readUTF();
                            if (container.coursesSchedule.getTutors().get(tutorName) != null)
                            {
                                clientOutput.writeUTF("\n Информация :\n");
                                clientOutput.writeUTF(container.coursesSchedule.getTutors().get(tutorName).returnFullInfo());
                            }
                            else
                            {
                                clientOutput.writeUTF("Преподаватель не обнаружен, возможно имя введено некорректно");
                            }
                        break;
                    }

                    case "9":
                    {
                        String studName;
                            studName = clientInput.readUTF();
                            if (container.coursesSchedule.getStudents().get(studName) != null)
                            {
                                clientOutput.writeUTF("\n Информация :\n");
                                clientOutput.writeUTF(container.coursesSchedule.getStudents().get(studName).returnFullInfo());
                            }
                            else
                            {
                                clientOutput.writeUTF("Студент не обнаружен, возможно имя введено некорректно");
                            }
                        break;
                    }
                    case "10":
                    {
                        String courseName;
                            courseName = clientInput.readUTF();
                            if (container.coursesSchedule.getCourses().get(courseName) != null)
                            {
                                clientOutput.writeUTF("\n Информация :\n");
                                clientOutput.writeUTF(container.coursesSchedule.getCourses().get(courseName).returnFullInfo());
                            }
                            else
                            {
                                clientOutput.writeUTF("Курс не обнаружен, возможно название введено некорректно");
                            }
                            break;
                    }
                    case "11":
                    {
                        String classroomName;
                        classroomName = clientInput.readUTF();
                        if (container.coursesSchedule.getClassrooms().get(classroomName) != null)
                        {
                            clientOutput.writeUTF("\n Информация :\n");
                            clientOutput.writeUTF(container.coursesSchedule.getClassrooms().get(classroomName).returnFullInfo());
                        }
                        else
                        {
                            clientOutput.writeUTF("Аудитория не обнаружена, возможно название введено некорректно");
                        }
                        break;
                    }

                    case "14":
                    {
                        String tutorName;
                        tutorName = clientInput.readUTF();
                        if (container.coursesSchedule.getTutors().get(tutorName) != null)
                        {
                            String newInfo = clientInput.readUTF();
                            container.coursesSchedule.getTutors().get(tutorName).parseString(newInfo);
                            if (!container.coursesSchedule.getTutors().get(tutorName).getName().equals(tutorName))
                            {
                                container.coursesSchedule.getTutors().put(container.coursesSchedule.getTutors().get(tutorName).getName(), container.coursesSchedule.getTutors().get(tutorName));
                                container.coursesSchedule.getTutors().remove(tutorName);
                            }
                            clientOutput.writeUTF("Данные о преподавателе " + tutorName + " обновлены");
                        }
                        else
                        {
                            clientOutput.writeUTF("Преподаватель не обнаружен, возможно название введено некорректно");
                        }
                        break;
                    }
                    case "15":
                    {

                        String studName;
                        studName = clientInput.readUTF();
                       // int i = container.coursesSchedule.getStreams().size();
                        int i = container.coursesSchedule.getStreams().size();
                        if (container.coursesSchedule.getStudents().get(studName) != null)
                        {
                            String newInfo = clientInput.readUTF();
                            container.coursesSchedule.getStudents().get(studName).parseString(newInfo,i);
                            if (!container.coursesSchedule.getStudents().get(studName).getName().equals(studName))
                            {
                                container.coursesSchedule.getStudents().put(container.coursesSchedule.getStudents().get(studName).getName(), container.coursesSchedule.getStudents().get(studName));
                                container.coursesSchedule.getStudents().remove(studName);
                            }
                            clientOutput.writeUTF("Данные о студенте " + studName + " обновлены");
                        }
                        else
                        {
                            clientOutput.writeUTF("Студент не обнаружен, возможно имя введено некорректно");
                        }
                        break;
                    }
                    case "16":
                    {
                        String courseName;
                        courseName = clientInput.readUTF();
                        if (container.coursesSchedule.getCourses().get(courseName) != null)
                        {
                            String newInfo = clientInput.readUTF();
                            container.coursesSchedule.getCourses().get(courseName).parseString(newInfo);
                            if (!container.coursesSchedule.getCourses().get(courseName).getName().equals(courseName))
                            {
                                container.coursesSchedule.getCourses().put(container.coursesSchedule.getCourses().get(courseName).getName(), container.coursesSchedule.getCourses().get(courseName));
                                container.coursesSchedule.getCourses().remove(courseName);
                            }
                            clientOutput.writeUTF("Данные о курсе " + courseName + " обновлены");
                        }
                        else
                        {
                            clientOutput.writeUTF("Курс не обнаружен, возможно название введено некорректно");
                        }
                        break;
                    }

                    case "18":
                    {
                        String tutorName;
                        tutorName = clientInput.readUTF();
                        String newInfo = clientInput.readUTF();
                        Tutor newTutor = new Tutor();
                        newTutor.setContainer(container.coursesSchedule);
                        newTutor.parseString(newInfo);
                        container.coursesSchedule.getTutors().put(newTutor.getName(), newTutor);
                        clientOutput.writeUTF("Преподаватель " + tutorName + " добавлен");
                        break;
                    }
                    case "19":
                    {
                        int i=10;
                        String studName;
                        studName = clientInput.readUTF();
                        String newInfo = clientInput.readUTF();
                        Student newStudent = new Student();
                        newStudent.setContainer(container.coursesSchedule);
                        newStudent.parseString(newInfo,i);
                        container.coursesSchedule.getStudents().put(newStudent.getName(), newStudent);
                        clientOutput.writeUTF("Студент " + studName + " добавлен");
                        break;
                    }
                    case "20":
                    {
                        String courseName;
                        courseName = clientInput.readUTF();
                        String newInfo = clientInput.readUTF();
                        Course newCourse = new Course();
                        newCourse.parseString(newInfo);
                        container.coursesSchedule.getCourses().put(newCourse.getName(), newCourse);
                        clientOutput.writeUTF("Курс " + courseName + " добавлен");
                        break;
                    }

                    case "23":
                    {
                        clientOutput.writeUTF("\nОбработка...\n\n");
                        for (Map.Entry<Integer, String> groupEntry : container.coursesSchedule.getGroups().entrySet())
                        {
                            String groupEntryValue = groupEntry.getValue();
                            clientOutput.writeUTF("Номер группы : " + groupEntryValue + "\n");
                            clientOutput.writeUTF("Список студентов:\n|");
                            int i = 0;
                            for (Map.Entry<String, Student> studentEntry : container.coursesSchedule.getStudents().entrySet())
                            {
                                String studentEntryKey = studentEntry.getKey();
                                if (container.coursesSchedule.getStudents().get(studentEntryKey).getGroup().equals(groupEntryValue))
                                {
                                    clientOutput.writeUTF(container.coursesSchedule.getStudents().get(studentEntryKey).getName() + "|");
                                    i++;
                                    if (i % 4 == 0)
                                        clientOutput.writeUTF("|");
                                }
                            }

                            clientOutput.writeUTF("\n\n");
                        }
                        break;
                    }

                    case "24":
                    {
                        clientOutput.writeUTF("\nОбработка...\n\n");
                        for (Map.Entry<Integer, String> streamEntry : container.coursesSchedule.getStreams().entrySet())
                        {
                            String streamEntryValue = streamEntry.getValue();
                            clientOutput.writeUTF("Номер потока : " + streamEntryValue + "\n");
                            clientOutput.writeUTF("Список студентов:\n|");
                            int i = 0;
                            for (Map.Entry<String, Student> studentEntry : container.coursesSchedule.getStudents().entrySet())
                            {
                                String studentEntryKey = studentEntry.getKey();
                                if (container.coursesSchedule.getStudents().get(studentEntryKey).getStream().equals(streamEntryValue))
                                {

                                    clientOutput.writeUTF(container.coursesSchedule.getStudents().get(studentEntryKey).getName() + "|");
                                    i++;
                                    if (i % 4 == 0)
                                        clientOutput.writeUTF("|");
                                }
                            }

                            clientOutput.writeUTF("\n\n");
                        }
                        break;
                    }
                    case "22":
                    {
                        break;
                    }
                    case "X-IT":
                    {
                        break;
                    }
                    default:
                    {
                        break;
                    }
                }
            }
            while (!decision.toUpperCase(Locale.getDefault()).equals("X-IT"));
            clientOutput.close(); clientInput.close();
            System.out.println(getTimestamp() + " Клиент с адресом " + clientSocket.getInetAddress().toString().replace("/", "") + " отключился. ");
            clientSocket.close();
            container.coursesSchedule.dumpToFiles();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }
    public String getTimestamp()
    {
        LocalDateTime currentTime = LocalDateTime.now();
        return currentTime.format(DateTimeFormatter.ofPattern("HH:mm:ss"));
    }
}

