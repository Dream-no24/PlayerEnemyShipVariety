import entity.EnemyShip;
import ctrlS.*;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CodeTest {

    @Test
    void test() {
        EnemyShip e = mock(EnemyShip.class);
        when(e.getHp()).thenReturn(10);
        assertEquals(10, e.getHp());
    }
    
    @Test
    void test2() {
        CurrencyManager c = mock(CurrencyManager.class);
        
    }
}
