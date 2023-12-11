
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
public class DuomenuBaze {
    private String name;

    public DuomenuBaze(String name)
    {
        this.name=name;
    }
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    public String toString() {
        return "Duomenu bazes pavadinimas: " + name;
    }

}
