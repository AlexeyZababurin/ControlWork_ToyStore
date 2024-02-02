package GeekBrains.ControlWork_ToyStore;

import java.io.FileReader;
import java.io.FileWriter;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

public class ToysStore {
    private List<Toy> toys;
    private String fileNameToys;

    public ToysStore() {
        fileNameToys = "toys.csv";
    }

    public void add(Toy rec) {
        toys.add(rec);
    }

    public boolean deleteById(int id) {
        for (Toy toy : toys) {
            if (toy.getId() == id) {
                toys.remove(toy);
                System.out.println("Игрушка с id=" + id + " успешно удалена.");
                return true;
            }
        }
        System.out.println("Игрушка с id=" + id + " не найдена.");
        return false;
    }

    public boolean save() {
        try {
            FileWriter fileWriter = new FileWriter(fileNameToys);
            fileWriter.append("id|name|count|weight\n");
            for (Toy toy : toys) {
                fileWriter.append(toy.getId() + "|" +
                        toy.getName() + "|" +
                        toy.getCount() + "|" +
                        toy.getWeight() + "\n");
            }
            fileWriter.close();
            return true;
        } catch (Exception ex) {
            System.out.println(ex.toString());
            return false;
        }
    }

    public boolean load() {
        toys = new LinkedList<>();
        try (FileReader fileReader = new FileReader(fileNameToys)) {
            Scanner scanner = new Scanner(fileReader);
            int i = 0;
            while (scanner.hasNextLine()) {
                String row = scanner.nextLine();
                if (i > 0) {
                    String[] fields = row.split("\\|");
                    if (fields.length != 4) {
                        throw new Exception("В исходном файле ошибка в строке " + i
                                + ". Количество полей не равно 4.");
                    }
                    int id = Integer.parseInt(fields[0].trim());
                    String name = fields[1].trim();
                    int count = Integer.parseInt(fields[2].trim());
                    double weight = Double.parseDouble(fields[3].trim());
                    Toy toy = new Toy(id, name, count, weight);
                    toys.add(toy);
                }
                i++;
            }
            scanner.close();
        } catch (Exception ex) {
            System.out.println(ex.toString());
            return false;
        }
        return true;
    }

    public List<Toy> getToysAll() {
        return toys;
    }

    public int getNewId() {
        int maxId = -1;
        for (Toy toy : toys) {
            if (toy.getId() > maxId)
                maxId = toy.getId();
        }
        return maxId + 1;
    }

    public Toy getToyById(int toyId) {
        for (Toy toy : toys) {
            if (toy.getId() == toyId)
                return toy;
        }
        return null;
    }

    public Toy getRandomToyByWeight() {    
        if (toys.size() == 0) {
            System.out.println("Нет игрушек!");
            return null;
        }

        int SumWeight = 0;
        List<Toy> selectionToys = new LinkedList<>();
        for (Toy toy : toys) {
            if (toy.getCount() > 0) {
                selectionToys.add(toy);
                SumWeight += toy.getWeight();
            }
        }

        if (selectionToys.size() == 0) {
            System.out.println("Игрушки для выдачи призов закончились!");
            return null;
        }

        int RandomWeight = new Random().nextInt(SumWeight + 1);
        SumWeight=0;
        for (Toy toy : selectionToys) {
            SumWeight += toy.getWeight();
            if (SumWeight >= RandomWeight) {
                return toy;
            }
        }
        return null;
    }

    @Override
    public String toString() {
        String result = "";
        for (Toy toy : toys) {
            result += toy.toString();
        }
        return "Список игрушек\n---------------\n: " + result;
    }
}
