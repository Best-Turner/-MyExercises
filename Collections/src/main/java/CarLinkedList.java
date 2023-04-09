import java.util.Iterator;

public class CarLinkedList implements CarList, CarQueue {

    private Node first;
    private Node last;
    private int size;

    @Override
    public Car get(int index) {
        Car car = getNode(index).value;
        return car;
    }

    @Override
    public boolean add(Car car) {
        if (size == 0) {
            first = new Node(null, car, null);
            last = first;
        } else {
            Node secondNode = last;
            last = new Node(secondNode, car, null);
            secondNode.next = last;
        }
        size++;
        return true;
    }

    @Override
    public Car peek() {
        return size > 0 ? get(0) : null;
    }

    @Override
    public Car poll() {
        Car car = get(0);
        removeAt(0);
        return car;
    }

    @Override
    public boolean add(Car car, int index) {
        if (index < 0 || index > size) {
            throw new IndexOutOfBoundsException();
        }
        if (index == size) {
            add(car);
            return true;
        }
        Node nextNode = getNode(index);
        Node previousNode = nextNode.previous;
        Node newNode = new Node(previousNode, car, nextNode);
        nextNode.previous = newNode;
        if (previousNode != null) {
            previousNode.next = newNode;
        } else {
            first = newNode;
        }
        size++;
        return true;
    }

    @Override
    public boolean remove(Car car) {
        int index = findElement(car);
        if (index >= 0) {
            return removeAt(index);
        }
        return false;
    }

    @Override
    public Iterator<Car> iterator() {
        return new Iterator<Car>() {
            private Node node = first;

            @Override
            public boolean hasNext() {
                return node != null;
            }

            @Override
            public Car next() {
                Car car = node.value;
                node = node.next;
                return car;
            }
        };
    }

    @Override
    public boolean contains(Car car) {
        return findElement(car) != -1;
    }

    private int findElement(Car car) {
        Node deletedNode = first;
        for (int i = 0; i < size; i++) {
            if (deletedNode.value.equals(car)) {
                return i;
            }
            deletedNode = deletedNode.next;
        }
        return -1;
    }

    @Override
    public boolean removeAt(int index) {
        Node deletedNode = getNode(index);
        Node nextNode = deletedNode.next;
        Node previousNode = deletedNode.previous;
        if (nextNode != null) {
            nextNode.previous = previousNode;
        } else {
            last = previousNode;
        }
        if (previousNode != null) {
            previousNode.next = nextNode;
        } else {
            first = nextNode;
        }
        size--;
        return true;
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public void clear() {
        first = null;
        last = null;
        size = 0;
    }


    private Node getNode(int index) {
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException();
        }
        Node node = first;
        for (int i = 0; i < index; i++) {
            node = node.next;
        }
        return node;
    }


    private static class Node {
        private Node previous;
        private Car value;
        private Node next;

        public Node(Node previous, Car value, Node next) {
            this.previous = previous;
            this.value = value;
            this.next = next;
        }
    }
}
