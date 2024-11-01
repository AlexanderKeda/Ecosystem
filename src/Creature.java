import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.util.Objects;

public abstract class LivingOrganism {
    private final String nameOfSpecies;
    private int count;      //число представителей
    private double reproductionRate;        //число новых представителей на пару в год
    private int lifeTime;                   //время жизни в годах
    private double weight;      //вес взрослого представителя в кг
    private int humidityComf;       //комфортная влажность в %
    private int temperatureComf;        //комфортная температура в град цельсия
    private int waterForDay;            //потребление воды в день в литрах
    private int foodForDay;             //потребление питательных веществ в день в кг
    private Ecosystem ecosystem;

    public static boolean checkDate(String nameOfSpecies, int count, double reproductionRate, int lifeTime, double weight,
                                    int humidityComf, int temperatureComf, int waterForDay, int foodForDay) {
        return !nameOfSpecies.matches(".*[^A-Za-z0-9 -].*") &&
                count > 0 &&
                reproductionRate > 0 &&
                lifeTime > 0 && lifeTime < 150 &&
                weight > 0 &&
                humidityComf >= 0 && humidityComf <=100 &&
                temperatureComf > -100 && temperatureComf < 100 &&
                waterForDay > 0 && waterForDay < weight * 0.4 &&
                foodForDay > 0 && foodForDay < weight * 0.4;
    }

    public LivingOrganism(Ecosystem ecosystem, String nameOfSpecies, int count, double reproductionRate, int lifeTime, double weight,
                          int humidityComf, int temperatureComf, int waterForDay, int foodForDay) {
        this.ecosystem = ecosystem;
        this.nameOfSpecies = nameOfSpecies;
        this.count = count;
        this.reproductionRate = reproductionRate;
        this.lifeTime = lifeTime;
        this.weight = weight;
        this.humidityComf = humidityComf;
        this.temperatureComf = temperatureComf;
        this.waterForDay = waterForDay;
        this.foodForDay = foodForDay;
    }

    public abstract void saveToFile();

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        LivingOrganism that = (LivingOrganism) o;
        return nameOfSpecies.equals(that.nameOfSpecies);
    }

    @Override
    public int hashCode() {
        return nameOfSpecies.hashCode();
    }

    @Override
    public String toString() {
        return "nameOfSpecies='" + nameOfSpecies + '\'' +
                ", count=" + count +
                ", reproductionRate=" + reproductionRate +
                ", lifeTime=" + lifeTime +
                ", weight=" + weight +
                ", humidityComf=" + humidityComf +
                ", temperatureComf=" + temperatureComf +
                ", waterForDay=" + waterForDay +
                ", foodForDay=" + foodForDay;
    }

    public Ecosystem getEcosystem() {
        return ecosystem;
    }

    public String getNameOfSpecies() {
        return nameOfSpecies;
    }

    public int getCount() {
        return count;
    }

    public double getReproductionRate() {
        return reproductionRate;
    }

    public int getLifeTime() {
        return lifeTime;
    }

    public double getWeight() {
        return weight;
    }

    public int getHumidityComf() {
        return humidityComf;
    }

    public int getTemperatureComf() {
        return temperatureComf;
    }

    public int getWaterForDay() {
        return waterForDay;
    }

    public int getFoodForDay() {
        return foodForDay;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public void setReproductionRate(double reproductionRate) {
        this.reproductionRate = reproductionRate;
    }

    public void setLifeTime(int lifeTime) {
        this.lifeTime = lifeTime;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }

    public void setHumidityComf(int humidityComf) {
        this.humidityComf = humidityComf;
    }

    public void setTemperatureComf(int temperatureComf) {
        this.temperatureComf = temperatureComf;
    }

    public void setWaterForDay(int waterForDay) {
        this.waterForDay = waterForDay;
    }

    public void setFoodForDay(int foodForDay) {
        this.foodForDay = foodForDay;
    }
}
