package GeekBrains.ControlWork_ToyStore;

import java.io.FileReader;
import java.io.FileWriter;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;

public class LotteryModel {
    private String fileNamePrizesToAward;
    private Lottery lottery;

    public LotteryModel() {
        fileNamePrizesToAward = "prizes.csv";
        lottery = new Lottery();
    }

    public boolean loadPrizesToAward() {
        List<Prize> prizesToAward = new LinkedList<>();
        try (FileReader fileReader = new FileReader(fileNamePrizesToAward)) {
            Scanner scanner = new Scanner(fileReader);
            int i = 0;
            while (scanner.hasNextLine()) {
                String row = scanner.nextLine();
                if (i > 0) {
                    String[] fields = row.split("\\|");
                    if (fields.length != 2) {
                        throw new Exception("В исходном файле ошибка в строке " + i + ". Количество полей не равно 2.");
                    }
                    int id = Integer.parseInt(fields[0].trim());
                    String[] toFields = fields[1].trim().split(";");
                    Toy toy = new Toy(Integer.parseInt(toFields[0].trim()),toFields[1].trim(), 0, 0);
                    Prize prize = new Prize(id, toy);
                    prizesToAward.add(prize);
                }
                i++;
            }
            lottery.setPrizesToAward(prizesToAward);
            scanner.close(); 
        } catch (Exception ex) {
            System.out.println("Ошибка при загрузке списка призов.\n" + ex.toString());
            return false;
        }
        return true;
    }

    public void ShowTablePrizesToAward() {
        String s1 = "Список-Разыгранные призы";
        System.out.println("\n" + s1 + "\n" + "-".repeat(s1.length()));
        for (Prize item : lottery.getPrizesToAward()) {
            System.out.println(item.toString());
        }
    }

    public boolean PrizesToAwardAddNew() {
        loadPrizesToAward();
        ToysStore toysStore= new ToysStore();
        if (!toysStore.load()) {
            return false;
        }
        Toy RandomToy = toysStore.getRandomToyByWeight();
        if (RandomToy == null) {
            System.out.println("Ошибка. Игрушка для приза не выбрана!");
            return false;
        }
        int NewId = getPrizesToAwardNewId();
        Prize newPrize = new Prize(NewId, RandomToy);
        lottery.getPrizesToAward().add(newPrize);
        if (!savePrizesToAward()) {
            System.out.println("Ошибка при сохранении списка призов!");
            return false;
        }
        RandomToy.setCount(RandomToy.getCount()-1);
        toysStore.save();
        System.out.println("Новый приз успешно разыгран! id=" + NewId + ". Смотрите таблицу разыгранных призов.");
        return true;
    }

    public boolean savePrizesToAward() {
        try {
            FileWriter fileWriter = new FileWriter(fileNamePrizesToAward);
            fileWriter.append("id|Toy=id;name\n");
            for (Prize prize : lottery.getPrizesToAward()) {
                fileWriter.append(prize.getId() + "|" + prize.getToy().toSavePrize() + "\n");
            }
            fileWriter.close();
            return true;
        } catch (Exception ex) {
            System.out.println(ex.toString());
            return false;
        }
    }

    public int getPrizesToAwardNewId() {
        int maxId = -1;
        for (Prize prize : lottery.getPrizesToAward()) {
            if (prize.getId() > maxId)
                maxId = prize.getId();
        }
        return maxId + 1;
    }

    public Prize getPrizeToAwardById(int prizeId) {
        for (Prize prize : lottery.getPrizesToAward()) {
            if (prize.getId() == prizeId)
                return prize;
        }
        return null;
    }
}
