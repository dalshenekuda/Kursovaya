package ClassSch;

import java.util.HashMap;
import java.util.Map;

import static ClassSch.ParserClass.purgeString;
import static ClassSch.ParserClass.returnSubString;

public class Student
{
    private String name;
    private HashMap<String, Course> attendedCoursesList;
    private Schedule container;

    private int year;
    private String stream;
    private String group;


    public Student()
    {
        attendedCoursesList = new HashMap<>();
    }
    public HashMap<String, Course> getAttendedCoursesList()
    {
        return attendedCoursesList;
    }
    public void setAttendedCoursesList(HashMap<String, Course> attendedCoursesList)
    {
        this.attendedCoursesList = attendedCoursesList;
    }
    public String getName()
    {
        return name;
    }
    public void setName(String name)
    {
        this.name = name;
    }

    public String getStream()
    {
        return stream;
    }
    public void setStream(String stream)
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

    public void setContainer(Schedule container)
    {
        this.container = container;
    }


    public void parseString(String strToParse,int i)
    {
        name = returnSubString(strToParse, "#").equals("") ? name : returnSubString(strToParse, "#");
        strToParse = purgeString(strToParse, "#");
        stream = returnSubString(strToParse, "#").equals("") ? stream : returnSubString(strToParse, "#");
        if (!container.getStreams().containsValue(stream))
            container.getStreams().put(i,stream);
        strToParse = purgeString(strToParse, "#");
        group = returnSubString(strToParse, "#").equals("") ?  group : returnSubString(strToParse, "#");
        if (!container.getGroups().containsValue(group))
            container.getGroups().put(i,group);

        strToParse = purgeString(strToParse, "#");
        while (!returnSubString(strToParse, "*").equals(""))
        {
            if (container.getCourses().get(returnSubString(strToParse, "*")) == null)
            {
                Course newCourse = new Course();
                newCourse.setName(returnSubString(strToParse, "*"));
                newCourse.getEnrolledStudents().put(name, this);
                container.getCourses().put(newCourse.getName(), newCourse);
                attendedCoursesList.put(newCourse.getName(), newCourse);
            }
            else
            {
                attendedCoursesList.put(returnSubString(strToParse, "*"), container.getCourses().get(returnSubString(strToParse, "*")));
                container.getCourses().get(returnSubString(strToParse, "*")).getEnrolledStudents().put(name, this);
            }
            strToParse = purgeString(strToParse, "*");
        }
    }




    public String returnFullInfo()
    {
        String result;
        result = "Имя: " + name + "\n";
        result+= "Поток: " + stream + "\n";
        result+= "Группа: " + group;
        result += (attendedCoursesList.size() <= 0 ? "" : "\nСписок прослушиваемых курсов:\n|");
        int i = 0;
        for (Map.Entry<String, Course> courseEntry : attendedCoursesList.entrySet())
        {
            String courseEntryKey = courseEntry.getKey();
            result += attendedCoursesList.get(courseEntryKey).getName() + "|";
            i++;
            if (i % 4 == 0) result += "\n|";
        }
        result += "\n";
        return result;
    }


    public String returnWritableFull()
    {
        String result;
        result = name + "#" + stream + "#" + group + "#";
        for (Map.Entry<String, Course> courseEntry : attendedCoursesList.entrySet())
        {
            String courseEntryKey = courseEntry.getKey();
            result += attendedCoursesList.get(courseEntryKey).getName() + "*";
        }
        result += "#";
        return result;
    }
}
