package ClassSch;

import java.util.regex.Pattern;

public class ParserClass
{
    public static String purgeString(String strToPurge, String separator)
    {
        if (strToPurge.contains(separator)) return strToPurge.replaceFirst(Pattern.quote(strToPurge.substring(0, strToPurge.indexOf(separator) + 1)), "");
        else return "";
    }
    public static String returnSubString(String strToSearch, String separator)
    {
        if (strToSearch.contains(separator)) return strToSearch.substring(0, strToSearch.indexOf(separator));
        else return "";
    }
}
