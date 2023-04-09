import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class CarSetTest {

    private CarSet carSet;

    @Before
    public void setUp() throws Exception {
        carSet = new CarHashSet();
        for (int i = 0; i < 100; i++) {
            carSet.add(new Car("Brand - " + i, i));
        }
    }


    @Test
    public void whenAdd3SimilarObjectsThenSizeIncreaseBy1() {
        assertEquals(100, carSet.size());
        assertTrue(carSet.add(new Car("Brand88", 0)));
        assertFalse(carSet.add(new Car("Brand88", 0)));
        assertFalse(carSet.add(new Car("Brand88", 0)));

        assertEquals(101, carSet.size());
    }

    @Test
    public void whenSetClearedThenSizeMustBe0() {
        carSet.clear();
        assertEquals(0, carSet.size());
    }

    @Test
    public void whenElementRemovedThenSizeDecreased() {
        assertTrue(carSet.remove(new Car("Brand - " + 0, 0)));
        assertEquals(99, carSet.size());
        assertFalse(carSet.remove(new Car("Brand - " + 0, 0)));
        assertEquals(99, carSet.size());
    }
}