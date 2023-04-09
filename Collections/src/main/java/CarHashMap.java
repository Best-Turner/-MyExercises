import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class CarHashMap implements CarMap {
    private int size;
    private final static int INITIAL_CAPACITY = 16;
    private Entry[] array = new Entry[INITIAL_CAPACITY];

    private static final double LOAD_FACTOR = 0.75;

    @Override
    public void put(CarOwner key, Car value) {
        if (size >= (array.length * LOAD_FACTOR)) {
            increaseArray();
        }
        if (put(key, value, array)) {
            size++;
        }
    }

    private boolean put(CarOwner key, Car value, Entry[] dst) {
        int index = getHashCode(key, dst);
        if (dst[index] == null) {
            dst[index] = new Entry(key, value, null);
            return true;
        } else {
            Entry existedElement = dst[index];
            while (true) {
                if (existedElement.key.equals(key)) {
                    existedElement.value = value;
                    return false;
                } else if (existedElement.next == null) {
                    existedElement.next = new Entry(key, value, null);
                    return true;
                } else {
                    existedElement = existedElement.next;
                }
            }
        }
    }

    @Override
    public Car get(CarOwner key) {
        int index = getHashCode(key, array);
        Entry existedElement = array[index];
        while (existedElement != null) {
            if (existedElement.key.equals(key)) {
                return existedElement.value;
            }
            existedElement = existedElement.next;
        }
        return null;
    }

    @Override
    public Set<CarOwner> keySet() {
        Set<CarOwner> result = new HashSet<>();
        for (Entry entry : array) {
            Entry existedElement = entry;
            while (existedElement != null) {
                result.add(existedElement.key);
                existedElement = existedElement.next;
            }
        }
        return result;
    }

    @Override
    public List<Car> values() {
        List<Car> cars = new ArrayList<>();
        for (Entry entry : array) {
            Entry entryElement = entry;
            while (entryElement != null) {
                cars.add(entryElement.value);
                entryElement = entryElement.next;
            }
        }
        return cars;
    }

    @Override
    public boolean remove(CarOwner key) {
        int position = getHashCode(key, array);
        Entry existedElement = array[position];
        if (existedElement != null && existedElement.key.equals(key)) {
            array[position] = existedElement.next;
            size--;
            return true;
        } else {
            while (existedElement != null) {
                Entry next = existedElement.next;
                if (next == null) {
                    return false;
                }
                if (next.key.equals(key)) {
                    existedElement.next = next.next;
                    size--;
                    return true;
                }
                existedElement = existedElement.next;
            }
        }
        return false;
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public void clear() {
        array = new Entry[INITIAL_CAPACITY];
        size = 0;
    }

    private int getHashCode(CarOwner key, Entry[] array) {
        int index = Math.abs(key.hashCode() % array.length);
        return index;
    }

    private void increaseArray() {
        Entry[] newArray = new Entry[array.length * 2];
        for (Entry entry : array) {
            Entry entryElement = entry;
            while (entryElement != null) {
                put(entryElement.key, entryElement.value, newArray);
                entryElement = entryElement.next;
            }
        }
        array = newArray;
    }

    private static class Entry {
        private CarOwner key;
        private Car value;
        private Entry next;

        public Entry(CarOwner key, Car value, Entry next) {
            this.key = key;
            this.value = value;
            this.next = next;
        }
    }
}
