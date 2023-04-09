import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class CarMapTest {

    private CarMap map;

    @Before
    public void setUp() {
        map = new CarHashMap();
    }

    @Test
    public void whenPut100ElementsThenSizeBecome100() {
        for (int i = 0; i < 100; i++) {
            map.put(new CarOwner(i, "Aleksandr", "Ivanov"), new Car("KIA " + i, i));
        }
        assertEquals(100, map.size());
    }

    @Test
    public void whenPut100ElementsWith10DifferentKeysThenSize10() {
        for (int i = 0; i < 100; i++) {
            int index = i % 10;
            CarOwner carOwner = new CarOwner(index, "Name " + index, "LastName " + index);
            Car car = new Car("Brand " + index, index);
            map.put(carOwner, car);
        }
        assertEquals(10, map.size());
    }


    @Test
    public void removeReturnTrueOnlyOnce() {
        for (int i = 0; i < 10; i++) {
            map.put(new CarOwner(i, "Aleksandr", "Ivanov"), new Car("KIA " + i, i));
        }
        assertEquals(10, map.size());
        CarOwner elementForDeleting = new CarOwner(2, "Aleksandr", "Ivanov");
        assertTrue(map.remove(elementForDeleting));
        assertEquals(9, map.size());
        assertFalse(map.remove(elementForDeleting));

    }

    @Test
    public void countOfKeysMustToEqualsCountOfValue() {
        for (int i = 0; i < 100; i++) {
            map.put(new CarOwner(i, "Aleksandr", "Ivanov"), new Car("KIA " + i, i));
        }
        assertEquals(100, map.size());
        assertEquals(100, map.values().size());
        assertEquals(100, map.keySet().size());
    }

    @Test
    public void methodGetMustReturnRightValue() {
        for (int i = 0; i < 100; i++) {
            map.put(new CarOwner(i, "Aleksandr", "Ivanov"), new Car("KIA " + i, i));
        }
        CarOwner key = new CarOwner(50, "Aleksandr", "Ivanov");
        Car value = map.get(key);
        String expectedCarBrand = "KIA " + 50;
        assertEquals(expectedCarBrand, value.getBrand());
    }
}