public interface CarList extends CarCollection {
    Car get(int index);

    boolean add(Car car, int index);

    boolean removeAt(int index);

}
