import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Stream;

public class Plant extends Creature {
    private double eatablePart;     //доля съедобной части в весе растения

    public Plant(Ecosystem ecosystem, String nameOfSpecies, int count,
                 double reproductionRate, int lifeTime, double weight,
                 int humidityComf, int temperatureComf, int waterForDay, int foodForDay, double eatablePart) {
        super(ecosystem, nameOfSpecies, count, reproductionRate, lifeTime,
                weight, humidityComf, temperatureComf, waterForDay, foodForDay);
        this.eatablePart = eatablePart;
        saveToFile();
    }

    public static boolean checkDate(String nameOfSpecies, int count, double reproductionRate, int lifeTime, double weight,
                                    int humidityComf, int temperatureComf, int waterForDay, int foodForDay, double eatablePart) {
        return eatablePart >= 0 && eatablePart <= 1 &&
                Creature.checkDate(nameOfSpecies, count, reproductionRate, lifeTime, weight,
                        humidityComf, temperatureComf, waterForDay, foodForDay);
    }

    public static Plant getPlantFromFile(String fileName, Ecosystem ecosystem) {
        Path path = Paths.get(fileName);
        try (Stream<String> lines = Files.lines(path)) {
            List<String> data = lines
                    .map(str -> str.replaceAll("^.*:", ""))
                    .toList();
            if (data.get(0).equals("Plant")) {
                String nameOfSpecies = data.get(1);
                int count = Integer.parseInt(data.get(2));
                double reproductionRate = Double.parseDouble(data.get(3));
                int lifeTime = Integer.parseInt(data.get(4));
                double weight = Double.parseDouble(data.get(5));
                int humidityComf = Integer.parseInt(data.get(6));
                int temperatureComf = Integer.parseInt(data.get(7));
                int waterForDay = Integer.parseInt(data.get(8));
                int foodForDay = Integer.parseInt(data.get(9));
                double eatablePart = Double.parseDouble(data.get(10));
                if (checkDate(nameOfSpecies, count, reproductionRate, lifeTime, weight,
                        humidityComf, temperatureComf, waterForDay, foodForDay, eatablePart)) {
                    return new Plant(ecosystem, nameOfSpecies, count, reproductionRate, lifeTime, weight,
                            humidityComf, temperatureComf, waterForDay, foodForDay, eatablePart);
                }
            }
            System.out.println("Некорректные данные в файле: " + fileName);
            return null;
        } catch (IOException e) {
            System.out.println("Ошибка при попытке чтения данных из файла: " + fileName);
            e.printStackTrace();
            return null;
        } catch (RuntimeException e) {
            System.out.println("Некорректные данные в файле: " + fileName);
            e.printStackTrace();
            return null;
        }

    }

    @Override
    public void deleteFile() {
        String fileName = this.getEcosystem().getDirectory() +
                "\\" + "Plants" + "\\" + this.getNameOfSpecies() + ".txt";
        try {
            Path path = Paths.get(fileName);
            Files.delete(path);
        } catch (IOException e) {
            System.out.println("Ошибка при удалении файла: " + fileName);
            e.printStackTrace();
        }
    }

    public void saveToFile() {
        String fileName = this.getEcosystem().getDirectory() +
                "\\" + "Plants" + "\\" + this.getNameOfSpecies() + ".txt";
        StringBuilder data = new StringBuilder();
        data.append("Class:Plant" + "\n")
                .append("nameOfSpecies:").append(this.getNameOfSpecies()).append("\n")
                .append("count:").append(this.getCount()).append("\n")
                .append("reproductionRate:").append(this.getReproductionRate()).append("\n")
                .append("lifeTime:").append(this.getLifeTime()).append("\n")
                .append("weight:").append(this.getWeight()).append("\n")
                .append("humidityComf:").append(this.getHumidityComf()).append("\n")
                .append("temperatureComf:").append(this.getTemperatureComf()).append("\n")
                .append("waterForDay:").append(this.getWaterForDay()).append("\n")
                .append("foodForDay:").append(this.getFoodForDay()).append("\n")
                .append("eatablePart:").append(eatablePart).append("\n");
        try {
            Path path = Paths.get(fileName);
            if (!Files.exists(path.getParent())) {
                Files.createDirectory(path.getParent());
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

    public void update(String nameOfSpecies, int count, double reproductionRate, int lifeTime, double weight,
                       int humidityComf, int temperatureComf, int waterForDay, int foodForDay, double eatablePart) {
        if (!checkDate(nameOfSpecies, count, reproductionRate, lifeTime, weight,
                humidityComf, temperatureComf, waterForDay, foodForDay, eatablePart)) {
            System.out.println("Переданы некорректные данные для обновления вида " + nameOfSpecies);
            System.out.println("Обновление не было произведено");
            return;
        }
        setCount(count);
        setReproductionRate(reproductionRate);
        setLifeTime(lifeTime);
        setWeight(weight);
        setHumidityComf(humidityComf);
        setTemperatureComf(temperatureComf);
        setWaterForDay(waterForDay);
        setFoodForDay(foodForDay);
        this.eatablePart = eatablePart;
        saveToFile();
    }

    @Override
    public String toString() {
        return "Plant{" +
                super.toString() +
                ", eatablePart=" + eatablePart +
                '}';
    }
}
