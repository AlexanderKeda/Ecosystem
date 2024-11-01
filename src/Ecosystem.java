import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Stream;

public class Ecosystem {
    private final String name;
    private int temperature;
    private int humidity;
    private int volumeOfWater;
    private final String directory;
    private final HashMap<String, Plant> plants;
    private final HashMap<String, Animal> animals;

    public static void main(String[] args) {
        System.out.println("Введите параметры экосистемы:");
        System.out.println("1) Для открытия существующей: open [путь до файла с данными экосистемы] [путь до места хранения файлов]");
        System.out.println("2) Для создания новой: new [путь до места хранения файлов] название_экосистемы температура влажность объём_воды");
        System.out.println("В названиях (экосистемы и видов) можно использовать только латинские буквы, цифры, нижнее подчеркивание и дефис");
        System.out.println("Для завершения работы введите: exit");
        String[] input;
        Ecosystem ecosystem;
        try (Scanner scanner = new Scanner(System.in)) {
            input = scanner.nextLine().split(" ");
            switch (input[0]) {
                case "open":
                    ecosystem = getEcosystemFromFile(input[1], input[2]);
                    if (ecosystem == null) {
                        System.out.println("Работа программы завершена");
                        return;
                    }
                    break;
                case "new":
                    if (Files.exists(Paths.get(input[1]))) {
                        if (!input[2].matches(".*[^A-Za-z0-9 -].*")) {
                            ecosystem = new Ecosystem(input[2], Integer.parseInt(input[3]),
                                    Integer.parseInt(input[4]), Integer.parseInt(input[5]), input[1]);
                        } else {
                            System.out.println("Недопустимые символы в имени, работа программы завершена");
                            return;
                        }
                    } else {
                        System.out.println("Указана не существующая папка, работа программы завершена");
                        return;
                    }
                    break;
                case "exit":
                    return;
                default:
                    System.out.println("Некорректные данные, работа программы завершена");
                    return;
            }
            System.out.println("Экосистема готова, далее доступны следующие дейсвия:");
            System.out.println("1) Добавление новых видов, формат: -c [animal или plant] [все параметры выбранного типа через пробел];");
            System.out.println("2) Чтение данных, формат: -r [ecosystem, animal или plant] [название вида, если выбрана не экосистема];");
            System.out.println("3) Обновление данных, формат: -u [ecosystem, animal или plant] [все параметры выбранного типа через пробел];");
            System.out.println("4) Удаление видов, формат: -d [animal или plant] [название вида].");
            System.out.println("Название видов в экосистеме можно получить прочитав данные экосистемы.");
            System.out.println("Для завершения работы введите: exit.");
            while (true) {
                input = scanner.nextLine().split(" ");
                switch (input[0]) {
                    case "-c":
                        ecosystem.create(input);
                        break;
                    case "-r":
                        ecosystem.read(input);
                        break;
                    case "-u":
                        ecosystem.update(input);
                        break;
                    case "-d":
                        ecosystem.delete(input);
                        break;
                    case "exit":
                        System.out.println("Работа программы завершена");
                        return;
                    default:
                        System.out.println("Неправильный формат входных данных");
                }
            }

        } catch (Exception e) {
            System.out.println("Ошибка при работе программы");
            e.printStackTrace();
        }

    }

    public Ecosystem(String name, int temperature, int humidity, int volumeOfWater, String directory) {
        this.name = name;
        this.temperature = temperature;
        this.humidity = humidity;
        this.volumeOfWater = volumeOfWater;
        if (directory.endsWith("\\")) {
            this.directory = directory + name;
        } else {
            this.directory = directory + "\\" + name;
        }
        plants = new HashMap<>();
        animals = new HashMap<>();
        saveToFile();
    }

    public static Ecosystem getEcosystemFromFile(String fileName, String folder) {
        Path path = Paths.get(fileName);
        Ecosystem ecosystem;
        try (Stream<String> lines = Files.lines(path)) {
            List<String> data = lines
                    .map(str -> str.replaceAll("^.*:", ""))
                    .toList();
            if (data.get(0).equals("Ecosystem")) {
                String name = data.get(1);
                int temperature = Integer.parseInt(data.get(2));
                int humidity = Integer.parseInt(data.get(3));
                int volumeOfWater = Integer.parseInt(data.get(4));
                String directory = folder;
                ecosystem = new Ecosystem(name, temperature, humidity, volumeOfWater, directory);
            } else {
                System.out.println("Некорректные данные в файле: " + fileName);
                return null;
            }
        } catch (IOException e) {
            System.out.println("Ошибка при попытке чтения данных из файла: " + fileName);
            e.printStackTrace();
            return null;
        } catch (RuntimeException e) {
            System.out.println("Некорректные данные в файле: " + fileName);
            e.printStackTrace();
            return null;
        }
        Path pathPlants = Paths.get(path.getParent() + "\\Plants");
        if (Files.exists(pathPlants)) {
            try (Stream<Path> pathStream = Files.list(pathPlants)) {
                List<Path> plants = pathStream.filter(p -> !p.endsWith(".txt")).toList();
                for (Path p : plants) {
                    Plant plant = Plant.getPlantFromFile(p.toString(), ecosystem);
                    if (plant != null) {
                        ecosystem.plants.put(plant.getNameOfSpecies(), plant);
                    }
                }
            } catch (IOException e) {
                System.out.println("Ошибка при попытке чтения данных из файлов сохраненных растений");
                e.printStackTrace();
                return null;
            }
        }
            Path pathAnimals = Paths.get(path.getParent().toString() + "\\Animals");
            if (Files.exists(pathAnimals)) {
                try (Stream<Path> pathStream = Files.list(pathAnimals)) {
                    List<Path> animals = pathStream.filter(p -> !p.endsWith(".txt")).toList();
                    for (Path p : animals) {
                        Animal animal = Animal.getAnimalFromFile(p.toString(), ecosystem);
                        if (animal != null) {
                            ecosystem.animals.put(animal.getNameOfSpecies(), animal);
                        }
                    }
                } catch (IOException e) {
                    System.out.println("Ошибка при попытке чтения данных из файлов сохраненных животных");
                    e.printStackTrace();
                    return null;
                }
            }
        return ecosystem;
    }

    private void create(String[] input) {
        switch (input[1]) {
            case "animal":
                try {
                    if (animals.containsKey(input[2])) {
                        System.out.println("Вид уже присутствует в экосистеме");
                        return;
                    }
                    Animal animal;
                    String nameOfSpecies = input[2];
                    int count = Integer.parseInt(input[3]);
                    double reproductionRate = Double.parseDouble(input[4]);
                    int lifeTime = Integer.parseInt(input[5]);
                    double weight = Double.parseDouble(input[6]);
                    int humidityComf = Integer.parseInt(input[7]);
                    int temperatureComf = Integer.parseInt(input[8]);
                    int waterForDay = Integer.parseInt(input[9]);
                    int foodForDay = Integer.parseInt(input[10]);
                    int speed = Integer.parseInt(input[11]);
                    boolean carnivorous = Boolean.parseBoolean(input[12]);
                    boolean herbivorous = Boolean.parseBoolean(input[13]);
                    if (Animal.checkDate(nameOfSpecies, count, reproductionRate, lifeTime, weight,
                            humidityComf, temperatureComf, waterForDay, foodForDay, speed, carnivorous, herbivorous)) {
                        animal = new Animal(this, nameOfSpecies, count, reproductionRate, lifeTime, weight,
                                humidityComf, temperatureComf, waterForDay, foodForDay, speed, carnivorous, herbivorous);
                        animals.put(animal.getNameOfSpecies(), animal);
                        System.out.println("Новое животное добавлено");
                        return;
                    } else {
                        System.out.println("Переданы некорректные данные. Новое животное не добавлено");
                    }
                } catch (RuntimeException e) {
                    System.out.println("Неправильный формат входных данных");
                    e.printStackTrace();
                }
                break;
            case "plant":
                try {
                    if (plants.containsKey(input[2])) {
                        System.out.println("Вид уже присутствует в экосистеме");
                        return;
                    }
                    Plant plant;
                    String nameOfSpecies = input[2];
                    int count = Integer.parseInt(input[3]);
                    double reproductionRate = Double.parseDouble(input[4]);
                    int lifeTime = Integer.parseInt(input[5]);
                    double weight = Double.parseDouble(input[6]);
                    int humidityComf = Integer.parseInt(input[7]);
                    int temperatureComf = Integer.parseInt(input[8]);
                    int waterForDay = Integer.parseInt(input[9]);
                    int foodForDay = Integer.parseInt(input[10]);
                    double eatablePart = Double.parseDouble(input[11]);
                    if (Plant.checkDate(nameOfSpecies, count, reproductionRate, lifeTime, weight,
                            humidityComf, temperatureComf, waterForDay, foodForDay, eatablePart)) {
                        plant = new Plant(this, nameOfSpecies, count, reproductionRate, lifeTime, weight,
                                humidityComf, temperatureComf, waterForDay, foodForDay, eatablePart);
                        plants.put(plant.getNameOfSpecies(), plant);
                        System.out.println("Новое растение добавлено");
                        return;
                    } else {
                        System.out.println("Переданы некорректные данные. Новое растение не добавлено");
                    }
                } catch (RuntimeException e) {
                    System.out.println("Неправильный формат входных данных");
                    e.printStackTrace();
                }
                break;
            default:
                System.out.println("Неправильный формат входных данных");
        }
    }

    private void read(String[] input) {
        switch (input[1]) {
            case "ecosystem":
                System.out.println(this);
                break;
            case "animal":
                if (animals.containsKey(input[2])) {
                    System.out.println(animals.get(input[2]));
                } else {
                    System.out.println("Указанный вид не найден");
                }
                break;
            case "plant":
                if (plants.containsKey(input[2])) {
                    System.out.println(plants.get(input[2]));
                } else {
                    System.out.println("Указанный вид не найден");
                }
                break;
            default:
                System.out.println("Неправильный формат входных данных");
        }
    }

    private void update(String[] input) {
        switch (input[1]) {
            case "ecosystem":
                try {
                    int temperature = Integer.parseInt(input[2]);
                    int humidity = Integer.parseInt(input[3]);
                    int volumeOfWater = Integer.parseInt(input[4]);
                    this.temperature = temperature;
                    this.humidity = humidity;
                    this.volumeOfWater = volumeOfWater;
                    System.out.println("Данные обновлены");
                } catch (RuntimeException e) {
                    System.out.println("Неправильный формат входных данных");
                    e.printStackTrace();
                }
                break;
            case "animal":
                try {
                    String nameOfSpecies = input[2];
                    if (animals.containsKey(nameOfSpecies)) {
                        int count = Integer.parseInt(input[3]);
                        double reproductionRate = Double.parseDouble(input[4]);
                        int lifeTime = Integer.parseInt(input[5]);
                        double weight = Double.parseDouble(input[6]);
                        int humidityComf = Integer.parseInt(input[7]);
                        int temperatureComf = Integer.parseInt(input[8]);
                        int waterForDay = Integer.parseInt(input[9]);
                        int foodForDay = Integer.parseInt(input[10]);
                        int speed = Integer.parseInt(input[11]);
                        boolean carnivorous = Boolean.parseBoolean(input[12]);
                        boolean herbivorous = Boolean.parseBoolean(input[13]);
                        if (Animal.checkDate(nameOfSpecies, count, reproductionRate, lifeTime, weight,
                                humidityComf, temperatureComf, waterForDay, foodForDay, speed, carnivorous, herbivorous)) {
                            animals.get(nameOfSpecies).update(nameOfSpecies, count, reproductionRate, lifeTime, weight,
                                    humidityComf, temperatureComf, waterForDay, foodForDay, speed, carnivorous, herbivorous);
                            System.out.println("Данные обновлены");
                            return;
                        }
                    }
                    System.out.println("Переданы некорректные данные. Данные не обновлены");
                } catch (RuntimeException e) {
                    System.out.println("Неправильный формат входных данных");
                    e.printStackTrace();
                }
                break;
            case "plant":
                try {
                    String nameOfSpecies = input[2];
                    if (plants.containsKey(nameOfSpecies)) {
                        int count = Integer.parseInt(input[3]);
                        double reproductionRate = Double.parseDouble(input[4]);
                        int lifeTime = Integer.parseInt(input[5]);
                        double weight = Double.parseDouble(input[6]);
                        int humidityComf = Integer.parseInt(input[7]);
                        int temperatureComf = Integer.parseInt(input[8]);
                        int waterForDay = Integer.parseInt(input[9]);
                        int foodForDay = Integer.parseInt(input[10]);
                        double eatablePart = Double.parseDouble(input[11]);
                        if (Plant.checkDate(nameOfSpecies, count, reproductionRate, lifeTime, weight,
                                humidityComf, temperatureComf, waterForDay, foodForDay, eatablePart)) {
                            plants.get(nameOfSpecies).update(nameOfSpecies, count, reproductionRate, lifeTime, weight,
                                    humidityComf, temperatureComf, waterForDay, foodForDay, eatablePart);
                            System.out.println("Данные обновлены");
                            return;
                        }
                    }
                    System.out.println("Переданы некорректные данные. Данные не обновлены");
                } catch (RuntimeException e) {
                    System.out.println("Неправильный формат входных данных");
                    e.printStackTrace();
                }
                break;
            default:
                System.out.println("Неправильный формат входных данных");
        }
    }

    private void delete(String[] input) {
        switch (input[1]) {
            case "animal":
                if (animals.containsKey(input[2])) {
                    animals.get(input[2]).deleteFile();
                    animals.remove(input[2]);
                    System.out.println("Вид удалён");
                } else {
                    System.out.println("Указанный вид не найден");
                }
                break;
            case "plant":
                if (plants.containsKey(input[2])) {
                    plants.get(input[2]).deleteFile();
                    plants.remove(input[2]);
                    System.out.println("Вид удалён");
                } else {
                    System.out.println("Указанный вид не найден");
                }
                break;
            default:
                System.out.println("Неправильный формат входных данных");
        }

    }

    public void saveToFile() {
        String fileName = directory + "\\" + name + ".txt";
        StringBuilder data = new StringBuilder();
        data.append("Class:Ecosystem" + "\n")
                .append("name:").append(name).append("\n")
                .append("temperature:").append(temperature).append("\n")
                .append("humidity:").append(humidity).append("\n")
                .append("volumeOfWater:").append(volumeOfWater).append("\n");
        try {
            Path path = Paths.get(fileName);
            if (!Files.exists(path.getParent())) {
                Files.createDirectories(path.getParent());
            }
            if (!Files.exists(path)) {
                Files.createFile(path);
            }
            Files.writeString(path, data.toString());
        } catch (IOException e) {
            System.out.println("Ошибка при попытке сохранить данные в файл: " + fileName);
            e.printStackTrace();
        }
    }

    public String getDirectory() {
        return directory;
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();
        result.append("Ecosystem{" +
                "name='" + name + '\'' +
                ", temperature=" + temperature +
                ", humidity=" + humidity +
                ", volumeOfWater=" + volumeOfWater +
                ", directory='" + directory + '\'' + ",\n" +
                "plants={");
        for (String plant : plants.keySet()) {
            result.append(plant)
                    .append(", ");
        }
        if (result.charAt(result.length() - 1) == ' ') {
            result.delete(result.length() - 2, result.length());
        }
        result.append("},\n");
        result.append("animals={");
        for (String animal : animals.keySet()) {
            result.append(animal)
                    .append(", ");
        }
        if (result.charAt(result.length() - 1) == ' ') {
            result.delete(result.length() - 2, result.length());
        }
        result.append("}\n}");
        return result.toString();
    }
}
