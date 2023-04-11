import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

public class Main {
    public static void main(String[] args) throws IOException {
        BufferedReader reader =
                new BufferedReader(new FileReader("res/Задача ВС Java Сбер.csv"));
        String line;
        Scanner scanner;

        List<City> cityList = new ArrayList<>();

        while ((line = reader.readLine()) != null) {
            int index = 0;
            scanner = new Scanner(line);
            scanner.useDelimiter(";");
            City city = new City();
            while (scanner.hasNext()) {
                String next = scanner.next();
                switch (index) {
                    case 0 -> {
                        index++;
                        continue;
                    }
                    case 1 -> city.setName(next);
                    case 2 -> city.setRegion(next);
                    case 3 -> city.setDistrict(next);
                    case 4 -> city.setPopulation(Integer.parseInt(next));
                    case 5 -> city.setFoundation(next);
                }
                index++;
            }
            cityList.add(city);
            scanner.close();
        }

    }

    private static void reverseSortingByName(List<City> cityList) {
        cityList.sort((city1, city2) -> city2.getName().compareToIgnoreCase(city1.getName()));
    }

    private static void reverseSortingByDistrictAndName(List<City> cityList) {
        cityList.sort(Comparator.comparing(City::getDistrict).thenComparing(City::getName).reversed());
    }

    private static void findMaxCountPopulation(List<City> cityList) {
        if (!cityList.isEmpty()) {
            City[] cities = cityList.toArray(new City[cityList.size()]);
            int index = 0;
            int population = 0;
            for (int i = 0; i < cities.length; i++) {
                if (cities[i].getPopulation() > population) {
                    population = cities[i].getPopulation();
                    index = i;
                }
            }
            System.out.println("[" + index + "] = " + population);
        } else {
            System.out.println("Список пустой");
        }
    }

    private static void findCountCitiesInRegion(List<City> cityList) {
        if (!cityList.isEmpty()) {
            Map<String, List<City>> collect =
                    cityList.stream()
                            .collect(Collectors.groupingBy(City::getRegion));
            for (Map.Entry<String, List<City>> map : collect.entrySet()) {
                System.out.println(map.getKey() + " - " + map.getValue().size());
            }
        } else {
            System.out.println("Список пустой");
        }
    }
}
