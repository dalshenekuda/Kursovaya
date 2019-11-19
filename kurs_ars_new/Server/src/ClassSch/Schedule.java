package ClassSch;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.time.Period;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

import static java.nio.file.Files.newBufferedReader;

public class Schedule
{
    private HashMap<LocalDateTime, Lesson> schedule;
    private HashMap<String, Course> courses;
    private HashMap<String, Student> students;
    private HashMap<String, Tutor> tutors;

    private HashMap<String, Classroom> classrooms;

    private HashMap<Integer, String> groups;
    private HashMap<Integer, String> streams;

    private String exceptionCause;
    private String workingDirectory;
    public Schedule()
    {
        schedule = new HashMap<>();
        courses = new HashMap<>();
        students = new HashMap<>();
        tutors = new HashMap<>();
        classrooms = new HashMap<>();
        groups = new HashMap<>();
        streams = new HashMap<>();

    }
    public HashMap<String, Course> getCourses()
    {
        return courses;
    }
    public HashMap<String, Student> getStudents()
    {
        return students;
    }
    public HashMap<String, Tutor> getTutors()
    {
        return tutors;
    }
    public HashMap<String, Classroom> getClassrooms()
    {
        return classrooms;
    }
    public HashMap<Integer, String> getStreams()
    {
        return streams;
    }
    public HashMap<Integer,String> getGroups()
    {
        return groups;
    }

    public HashMap<LocalDateTime, Lesson> getSchedule()
    {
        return schedule;
    }
    public void setSchedule(HashMap<LocalDateTime, Lesson> schedule)
    {
        this.schedule = schedule;
    }

    public void parseFiles(String sourceFilesPath) throws IOException
    {
        Path inputFilePath;
        workingDirectory = sourceFilesPath;
        try
        {
            exceptionCause = "courses.txt";
            System.out.print("Загрузка информации о курсах... ");
            inputFilePath = FileSystems.getDefault().getPath(sourceFilesPath,"courses.txt");
            BufferedReader inputFileBuffer = newBufferedReader(inputFilePath, StandardCharsets.UTF_8);
            String inputString;
            while ((inputString = inputFileBuffer.readLine()) != null)
            {
                Course newCourse = new Course();
                newCourse.parseString(inputString);
                courses.put(newCourse.getName(), newCourse);
            }

            inputFileBuffer.close();
            System.out.print("готово!\nЗагрузка информации о преподавателях... ");
            exceptionCause = "tutors";
            inputFilePath = FileSystems.getDefault().getPath(sourceFilesPath, "tutors");
            inputFileBuffer = newBufferedReader(inputFilePath, StandardCharsets.UTF_8);
            while ((inputString = inputFileBuffer.readLine()) != null)
            {
                Tutor newTutor = new Tutor();
                newTutor.setContainer(this);
                newTutor.parseString(inputString);
                tutors.put(newTutor.getName(), newTutor);

            }
            inputFileBuffer.close();
            System.out.print("готово!\nЗагрузка информации о студентах... ");
            exceptionCause = "students";
            inputFilePath = FileSystems.getDefault().getPath(sourceFilesPath, "students");
            inputFileBuffer = newBufferedReader(inputFilePath, StandardCharsets.UTF_8);
            int i=0;
            while ((inputString = inputFileBuffer.readLine()) != null)
            {
                Student newStudent = new Student();
                newStudent.setContainer(this);
                newStudent.parseString(inputString,i);
                students.put(newStudent.getName(), newStudent);
                i++;
            }

            inputFileBuffer.close();
            System.out.print("готово!\nЗагрузка информации об аудиториях... ");
            exceptionCause = "classrooms";
            inputFilePath = FileSystems.getDefault().getPath(sourceFilesPath, "classrooms");
            inputFileBuffer = newBufferedReader(inputFilePath, StandardCharsets.UTF_8);
            while ((inputString = inputFileBuffer.readLine()) != null)
            {
                Classroom newClassroom = new Classroom();
                newClassroom.setContainer(this);
                newClassroom.parseString(inputString);
                classrooms.put(newClassroom.getName(), newClassroom);
            }


            inputFileBuffer.close();
            System.out.print("готово!\nЕсли файл schedule содержит расписание нажмите 1, если нет - 0: ");
            Scanner input = new Scanner(System.in);
            String decision = input.nextLine();
            switch (decision)
            {
                case "1":
                {
                    System.out.print("Загрузка информации о расписании... ");
                    exceptionCause = "schedule.txt";
                    inputFilePath = FileSystems.getDefault().getPath(sourceFilesPath, "schedule.txt");
                    inputFileBuffer = newBufferedReader(inputFilePath, StandardCharsets.UTF_8);
                    while ((inputString = inputFileBuffer.readLine()) != null)
                    {
                        Lesson newLesson = new Lesson();
                        newLesson.setContainer(this);
                        newLesson.parseString(inputString);
                        schedule.put(newLesson.getHostingDateTime(), newLesson);
                    }
                    inputFileBuffer.close();
                    System.out.print("готово!\n");
                    break;
                }
                default:
                {
                    System.out.print("Загрузка расписания из файла пропущена.\n");
                    break;
                }
            }
            System.out.print("Актуализация информации... ");
            System.out.print("готово!\n\n");
        }
        catch (IOException e)
        {
            throw new IOException("Произошла ошибка при открытии файла " + exceptionCause + ". Он не существует, имеет кодировку, отличную от UTF-8, или находится в другой директории. Попробуйте ещё раз.");
        }
    }
    public void dumpToFiles() throws IOException
    {
        Path outputFilePath;
        try
        {
            exceptionCause = "courses.txt";
            System.out.print("Выгрузка данных о курсах... ");
            outputFilePath = FileSystems.getDefault().getPath(workingDirectory, "courses.txt");
            BufferedWriter fileOutput = Files.newBufferedWriter(outputFilePath, StandardCharsets.UTF_8);
            HashMap<String, Course> outputCoursesList = new HashMap<>(courses);
            for (Map.Entry<String, Course> courseEntry : outputCoursesList.entrySet())
            {
                String courseEntryKey = courseEntry.getKey();
                fileOutput.write(outputCoursesList.get(courseEntryKey).returnWritableFull());
                fileOutput.newLine();
            }


            fileOutput.close();
            System.out.print("готово!\nВыгрузка данных о преподавателях ... ");
            exceptionCause = "tutors";
            outputFilePath = FileSystems.getDefault().getPath(workingDirectory, "tutors");
            fileOutput = Files.newBufferedWriter(outputFilePath, StandardCharsets.UTF_8);
            HashMap<String, Tutor> outputTutorsList = new HashMap<>(tutors);
            for (Map.Entry<String, Tutor> tutorEntry : outputTutorsList.entrySet())
            {
                String courseEntryKey = tutorEntry.getKey();
                fileOutput.write(outputTutorsList.get(courseEntryKey).returnWritableFull());
                fileOutput.newLine();
            }
            fileOutput.close();
            System.out.print("готово!\nВыгрузка данных о студентах... ");
            exceptionCause = "students";
            outputFilePath = FileSystems.getDefault().getPath(workingDirectory, "students");
            fileOutput = Files.newBufferedWriter(outputFilePath, StandardCharsets.UTF_8);
            HashMap<String, Student> outputStudentsList = new HashMap<>(students);
            for (Map.Entry<String, Student> studentEntry : outputStudentsList.entrySet())
            {
                String courseEntryKey = studentEntry.getKey();
                fileOutput.write(outputStudentsList.get(courseEntryKey).returnWritableFull());
                fileOutput.newLine();
            }


            fileOutput.close();
            System.out.print("готово!\nВыгрузка данных о расписании... ");
            exceptionCause = "schedule.txt";
            outputFilePath = FileSystems.getDefault().getPath(workingDirectory, "schedule.txt");
            fileOutput = Files.newBufferedWriter(outputFilePath, StandardCharsets.UTF_8);
            HashMap<LocalDateTime, Lesson> outputSchedule = new HashMap<>(schedule);
            for (Map.Entry<LocalDateTime, Lesson> lessonEntry : outputSchedule.entrySet())
            {
                LocalDateTime courseEntryKey = lessonEntry.getKey();
                fileOutput.write(outputSchedule.get(courseEntryKey).returnWritableFull());
                fileOutput.newLine();
            }
            fileOutput.close();
            System.out.print("готово!\n");
        }
        catch (IOException e)
        {
            throw new IOException("Произошла ошибка при записи в файл " + exceptionCause + ". Он не существует, имеет кодировку, отличную от UTF-8, или находится в другой директории. Попробуйте ещё раз.");
        }
    }
    public HashMap<LocalDateTime, Lesson> getScheduleFor(Object person, String personName, Period timeFrame, LocalDateTime startDate)
    {
        HashMap<LocalDateTime, Lesson> result = new HashMap<>();
        if (person.getClass().getName().contains("Student"))
        {
            startDate.plus(timeFrame);
        }
        else
        if (person.getClass().getName().contains("Tutor"))
        {

        }
        return result;
    }
}
