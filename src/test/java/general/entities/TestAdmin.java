package general.entities;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

public class TestAdmin {
    Admin admin = new Admin("Admin", "password", new ArrayList<>());

    @Test
    void constructorTest() {

        assertEquals("Admin", admin.getName());
    }
}