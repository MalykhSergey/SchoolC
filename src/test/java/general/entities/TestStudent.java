package general.entities;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;


public class TestStudent {
    Student student = new Student("Student_name","password", new ArrayList<>(), new School(), new SchoolClass());

    @Test
    void testConstructor(){
        assertEquals("Student_name", student.getName());
    }
    @Test
    void testAddAnswer(){
        List<Answer> answers  = new ArrayList<>();
        Answer answer = new Answer();
        answers.add(answer);
        student.addAnswer(answer);
        assertEquals(student.getAnswers(), answers);
    }
    
}