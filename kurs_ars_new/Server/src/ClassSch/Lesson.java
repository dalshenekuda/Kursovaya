package ClassSch;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

import static ClassSch.ParserClass.purgeString;
import static ClassSch.ParserClass.returnSubString;

public class Lesson
{
    private Course hostedCourse;
    private Tutor hostingTutor;
    private String classroom;
    private LocalDateTime hostingDateTime;
    private HashMap<String, Student> enrolledStudents;
    private Schedule container;



    public Lesson()
    {
        enrolledStudents = new HashMap<>();
    }
    public Course getHostedCourse()
    {
        return hostedCourse;
    }
    public void setHostedCourse(Course hostedCourse)
    {
        this.hostedCourse = hostedCourse;
    }
    public Tutor getHostingTutor()
    {
        return hostingTutor;
    }
    public void setHostingTutor(Tutor hostingTutor)
    {
        this.hostingTutor = hostingTutor;
    }
    public String getClassroom()
    {
        return classroom;
    }
    public void setClassroom(String classroom)
    {
        this.classroom = classroom;
    }

    public LocalDateTime getHostingDateTime()
    {
        return hostingDateTime;
    }
    public void setHostingDateTime(LocalDateTime hostingDateTime)
    {
        this.hostingDateTime = hostingDateTime;
    }
    public HashMap<String, Student> getEnrolledStudents()
    {
        return enrolledStudents;
    }
    public void setEnrolledStudents(HashMap<String, Student> enrolledStudents)
    {
        this.enrolledStudents = enrolledStudents;
    }

    public void setContainer(Schedule container)
    {
        this.container = container;
    }


    public void parseString(String strToParse)
    {
        hostingDateTime = LocalDateTime.parse(returnSubString(strToParse, "#"), DateTimeFormatter.ofPattern("dd/MM/yy HH:mm"));
        strToParse = purgeString(strToParse, "#");
      //  classroom += returnSubString(strToParse, "#");
      //  strToParse = purgeString(strToParse, "#");
        hostedCourse = container.getCourses().get(returnSubString(strToParse, "#"));
        hostingTutor = hostedCourse.getHostingTutor();
        strToParse = purgeString(strToParse, "#");
        for(Map.Entry<String, Student> studentEntry : container.getStudents().entrySet())
        {
            String key = studentEntry.getKey();
            if (container.getStudents().get(key).getAttendedCoursesList().get(hostedCourse.getName()) != null)
            {
                enrolledStudents.put(key, container.getStudents().get(key));
            }
        }
    }


    public String returnWritableFull()
    {
        return hostingDateTime.format(DateTimeFormatter.ofPattern("dd/MM/yy HH:mm")) + "#" + hostedCourse.getName() + "#";
    }
}
