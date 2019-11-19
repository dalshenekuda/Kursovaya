package ClassSch;

import java.util.HashMap;
import java.util.Map;

import static ClassSch.ParserClass.purgeString;
import static ClassSch.ParserClass.returnSubString;

public class Tutor
{
    private String name;
    private HashMap<String, Course> hostedCourses;
    private Schedule container;
    public Tutor()
    {
        hostedCourses = new HashMap<>();
    }
    public String getName()
    {
        return name;
    }
    public void setName(String name)
    {
        this.name = name;
    }

    public HashMap<String, Course> getHostedCourses()
    {
        return hostedCourses;
    }
    public void setHostedCourses(HashMap<String, Course> hostedCourses)
    {
        this.hostedCourses = hostedCourses;
    }
    public void setContainer(Schedule container)
    {
        this.container = container;
    }

    public void parseString(String strToParse)
    {
        name = returnSubString(strToParse, "#").equals("") ? name : returnSubString(strToParse, "#");
        strToParse = purgeString(strToParse, "#");
        while (!returnSubString(strToParse, "*").equals(""))
        {
            if (container.getCourses().get(returnSubString(strToParse, "*")) == null)
            {
                Course newCourse = new Course();
                newCourse.setName(returnSubString(strToParse, "*"));
                newCourse.setHostingTutor(this);
                hostedCourses.put(newCourse.getName(), newCourse);
                container.getCourses().put(newCourse.getName(), newCourse);
            }
            else
            {
                hostedCourses.put(returnSubString(strToParse, "*"), container.getCourses().get(returnSubString(strToParse, "*")));
                container.getCourses().get(returnSubString(strToParse, "*")).setHostingTutor(this);
            }
            strToParse = purgeString(strToParse, "*");
        }
    }
    public String returnFullInfo()
    {
        String result;
        result = "Имя: " + name;
        result += (hostedCourses.size() <= 0 ? "" : "\nЧитаемые курсы:\n|");
        int i = 0;
        for (Map.Entry<String, Course> courseEntry : hostedCourses.entrySet())
        {
            String courseEntryKey = courseEntry.getKey();
            result += hostedCourses.get(courseEntryKey).getName() + "|";
            i++;
            if (i % 4 == 0) result += "\n|";
        }
        result += "\n";
        return result;
    }

    public String returnWritableFull()
    {
        String result;
        result = name + "#";
        for (Map.Entry<String, Course> courseEntry : hostedCourses.entrySet())
        {
            String courseEntryKey = courseEntry.getKey();
            result += hostedCourses.get(courseEntryKey).getName() + "*";
        }
        result += "#";
        return result;
    }


}
