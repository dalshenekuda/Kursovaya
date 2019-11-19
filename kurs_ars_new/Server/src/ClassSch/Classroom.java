package ClassSch;

import static ClassSch.ParserClass.purgeString;
import static ClassSch.ParserClass.returnSubString;

public class Classroom {

    private String name;
    private int capacity;
    private Schedule container;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getCapacity() {
        return capacity;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    public void setContainer(Schedule container) {
        this.container = container;
    }

    public void parseString(String strToParse) {
        name = returnSubString(strToParse, "#").equals("") ? name : returnSubString(strToParse, "#");
        strToParse = purgeString(strToParse, "#");
        capacity = Integer.parseInt(returnSubString(strToParse, "#"));
    }

    public String returnFullInfo() {
        return ("Название :" + name + (", вместимость : " + capacity));
    }

}