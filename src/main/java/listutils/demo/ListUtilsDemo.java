package listutils.demo;

import java.util.ArrayList;
import java.util.List;
import listutils.ListUtils;

public class ListUtilsDemo {
    
    public static void main(String[] args) {
        
        List<Person> list = new ArrayList<>();
        list.add(new Person("Carl Johnson", 37));
        list.add(new Person("Big Smoke", 43));
        list.add(new Person("OG Loc", 35));
        
        ListUtils.sortByField(list ,"age").forEach((Person person) -> {
            System.out.printf("%s, %d years old\n", person.getName(), person.getAge());
        });
        
        System.out.println();
        System.out.println("The youngest is " + ListUtils.minByField(list, "age").getName());
        System.out.println("The oldest is " + ListUtils.maxByField(list, "age").getName());
        
    }
    
}
