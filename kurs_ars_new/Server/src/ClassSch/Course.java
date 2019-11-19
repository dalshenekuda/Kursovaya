package ClassSch;

import java.util.HashMap;
import java.util.Map;

import static ClassSch.ParserClass.purgeString;
import static ClassSch.ParserClass.returnSubString;

public class Course
{
    private String name;

    private int intensity;
    private int stream;
    private String group;

    private Tutor hostingTutor;
    private HashMap<String, Student> enrolledStudents;


    public void setEnrolledStudents(HashMap<String, Student> enrolledStudents)
    {
        this.enrolledStudents = enrolledStudents;
    }
    public Course()
    {
        enrolledStudents = new HashMap<>();
    }
    public Integer getStream()
    {
        return stream;
    }
    public void setStream(Integer stream)
    {
        this.stream = stream;
    }

    public String getGroup()
    {
        return group;
    }
    public void setGroup(String group)
    {
        this.group = group;
    }

    public String getName()
    {
        return name;
    }
    public void setName(String name)
    {
        this.name = name;
    }


    public int getIntensity()
    {
        return intensity;
    }
    public void setIntensity(int intensity)
    {
        this.intensity = intensity;
    }


    public Tutor getHostingTutor()
    {
        return hostingTutor;
    }
    public void setHostingTutor(Tutor hostingTutor)
    {
        this.hostingTutor = hostingTutor;
    }
    public HashMap<String, Student> getEnrolledStudents()
    {
        return enrolledStudents;
    }


    public void parseString(String strToParse)
    {
        name = returnSubString(strToParse, "#").equals("") ? name : returnSubString(strToParse, "#");
        strToParse = purgeString(strToParse, "#");
        intensity = returnSubString(strToParse, "#").equals("") ? intensity : Integer.parseInt(returnSubString(strToParse, "#"));
        strToParse = purgeString(strToParse, "#");
        stream = returnSubString(strToParse, "#").equals("") ?  stream  : Integer.parseInt(returnSubString(strToParse, "#"));
        strToParse = purgeString(strToParse, "#");
        group = returnSubString(strToParse, "#").equals("") ? group : returnSubString(strToParse, "#");
    }
    public String returnFullInfo()
    {
        String result;
        result = "Название курса: " + name;
        result += (intensity == 0 ? "\n" : "\nИнтенсивность: " + intensity + " часов в день");
        result += (stream == 0 ? "\n" : "\nПоток: " + stream);
        result += (group == null ? "\n" : "\nГруппа: " + group);

        result += returnEnrolledStudents();
        return result;
    }
    public String returnEnrolledStudents()
    {
        String result;
        result = (enrolledStudents.size() <= 0 ? "" : "Студенты:\n|");
        int i = 0;
        for (Map.Entry<String, Student> studentEntry : enrolledStudents.entrySet())
        {
            String studentKey = studentEntry.getKey();
            result += enrolledStudents.get(studentKey).getName() + "|";
            i++;
            if (i % 4 == 0) result += "|\n";
        }
        result +=("\n");
        return result;
    }
    public String returnWritableFull()
    {
        return ((name == null) ? "" : name) + "#" +((intensity == 0) ? "" : intensity) + "#" + ((stream == 0) ? "" : stream) + "#" +((group== null) ? "" : group + "#");
    }

}
