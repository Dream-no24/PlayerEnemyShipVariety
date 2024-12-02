import entity.EnemyShip;

import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CodeTest {

    @Test
    void test() {
        EnemyShip e = mock(EnemyShip.class);
        when(e.getX()).thenReturn(10);
        assertEquals(10, e.getX());
    }
    
    @Test
    void test2() {

    }
}
