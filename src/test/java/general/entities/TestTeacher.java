package general.entities;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

public class TestTeacher {
    Teacher teacher = new Teacher();
    @Test
    void addSchoolClass(){
        teacher.addSchoolClass(new SchoolClass());
        assertEquals(teacher.getSchoolClassSet().iterator().next(), new SchoolClass());
    }
}