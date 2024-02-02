package GeekBrains.ControlWork_ToyStore;


import java.util.NoSuchElementException;
import java.util.Scanner;

public class Menu {
    private String prevPos;                      
    private String choice;
    private String newPos;
    private boolean ShowNewChoice;
    private Scanner scanner;

    public void run() {
        showProgramGreeting();

        scanner = new Scanner(System.in);
        while (true) {
            ShowNewChoice = true;
            if (getChoice() != "") {
                if (getPrevPos() != "") {
                    setNewPos((getPrevPos() + "," +
                            getChoice()));
                } else {
                    setNewPos(getChoice());
                }
            } else {
                if (getPrevPos() != "") {
                    setNewPos(getPrevPos());
                } else {
                    setNewPos("");
                }
            }
            if (getChoice().equals("0")) {
                showMainMenu();
            } else if (getChoice().equals("q")) {
                showProgramExit();
                return;
            } else {
                switch (getNewPos()) {
                    case (""):
                        showMainMenu();
                        break;

                    case ("1"):
                        showToysMenu();
                        break;

                    case ("1,1"): 
                        ToysShowTableAll();
                        break;

                    case ("1,2"):
                        if (ToyAddNew() == true) {
                            ToysShowTableAll();
                        } else {
                            setChoice("");
                            ShowNewChoice = false;
                        }
                        break;

                    case ("1,3"):
                        if (ToyEdit()) {
                            ToysShowTableAll();
                        } else {
                            setChoice("");
                            ShowNewChoice = false;
                        }
                        break;

                    case ("1,4"):
                        if (ToyDeleteById()) {
                            ToysShowTableAll();
                        } else {
                            setChoice("");
                            ShowNewChoice = false;
                        }
                        break;

                    case ("2"):
                        showLotteryMenu();
                        break;

                    case ("2,1"):
                        PrizesToAwardShowAll();
                        showLotteryMenu();
                        break;

                    case ("2,2"):
                        if (PrizeAddNew()) {
                            PrizesToAwardShowAll();
                            showLotteryMenu();
                        } else {
                            setChoice("");
                            ShowNewChoice = false;
                        }
                        break;

                    default:
                        showMenuItemNotFound();
                        setChoice("");
                        ShowNewChoice = false;
                        break;
                }
            }

            if (ShowNewChoice) {
                System.out.printf("Укажите пункт меню: ");
                try {
                    setChoice(scanner.nextLine().trim());
                } catch (NoSuchElementException exception) {
                    System.out.println("Пункт меню не выбран");
                    setChoice("");
                }
            }
        }
    }

    public void showProgramGreeting() {
        System.out.println();
        String s1 = "Программа - Проведение розыгрыша призов магазина игрушек";
        System.out.println(s1);
        System.out.println("-".repeat(s1.length()));
    }

    public void showMainMenu() {
        String s1 = "Главное меню";
        System.out.println("\n" + s1 + "\n" + "-".repeat(s1.length()));
        System.out.println("1. Игрушки.");
        System.out.println("2. Розыгрыш призов.");
        System.out.println("q. Выход из программы.");
        ResetMenuPos();
    }

    public void showToysMenu() {
        String s1 = "Меню-Список игрушек";
        System.out.println("\n" + s1 + "\n" + "-".repeat(s1.length()));
        System.out.println("1. Показать список.");
        System.out.println("2. Добавить игрушку.");
        System.out.println("3. Редактировать игрушку.");
        System.out.println("4. Удалить игрушку.");
        System.out.println("0. Назад в Главное меню.");
        System.out.println("q. Выход.");
        setPrevPos(getNewPos());
    }

    public void showLotteryMenu() {
        System.out.println("\nМеню-Розыгрыш призов\n----------------");
        System.out.println("1. Показать список-Разыгранные призы.");
        System.out.println("2. Разыграть следующий приз.");
        System.out.println("0. Назад в Главное меню.");
        System.out.println("q. Выход.");
        setPrevPos(getNewPos());
    }

    public void showProgramExit() {
        System.out.println();
        System.out.println("Завершение работы программы");
    }

    public void showMenuItemNotFound() {
        System.out.println("Не найден обработчик для указанного пункта меню.");
    }

    public void ToysShowTableAll() {
        ToysStore ToysStore = new ToysStore();
        if (ToysStore.load()) {
            ToysView toysView = new ToysView(ToysStore.getToysAll());
            toysView.ShowTable();
        }
        ReturnToPrevPos();
        showToysMenu();
    }

    public boolean ToyAddNew() {
        ToysStore ToysStore = new ToysStore();
        if (!ToysStore.load()) {
            System.out.println("\nФункция добавления игрушки прервана.");
            return false;
        }

        int id = ToysStore.getNewId();
        System.out.println("\nДобавление игрушки. Введите значения полей.");
        System.out.print("Название: ");
        try {
            String name = scanner.nextLine();
            System.out.print("Количество: ");
            int count = Integer.parseInt(scanner.nextLine());
            System.out.print("Вес: ");
            int weight = Integer.parseInt(scanner.nextLine());
            Toy toy = new Toy(id, name, count, weight);
            ToysStore.add(toy);
        } catch (Exception ex) {
            System.out.println("Ошибка при вводе данных.\n" + ex.toString());
            return false;
        }

        if (ToysStore.save()) {
            System.out.println("Новая игрушка успешно добавлена!");
        } else {
            System.out.println("Ошибка при добавлении новой игрушки.");
            return false;
        }
        return true;
    }

    public boolean ToyDeleteById() {
        ToysStore ToysStore = new ToysStore();
        if (ToysStore.load()) {
            ToysView toysView = new ToysView(ToysStore.getToysAll());
            toysView.ShowTable();
        }
        System.out.print("\nВведите id игрушки для удаление: ");
        try {
            int id = Integer.parseInt(scanner.nextLine());
            if (ToysStore.deleteById(id)) {
                ToysStore.save();
                return true;
            }
        } catch (Exception ex) {
            System.out.println("Ошибка при удалении игрушки.\n" + ex.toString());
            return false;
        }
        return false;
    }

    public boolean ToyEdit() {
        Toy editedToy;
        ToysStore ToysStore = new ToysStore();
        if (ToysStore.load()) {
            ToysView toysView = new ToysView(ToysStore.getToysAll());
            toysView.ShowTable();
        }
        System.out.print("\nВведите id игрушки для редактирования: ");
        try {
            int id = Integer.parseInt(scanner.nextLine());
            editedToy = ToysStore.getToyById(id);
            String value;
            System.out.println("Введите новые значения полей (Enter - оставить прежнее значение).");
            System.out.print("Название (прежнее значение): " + editedToy.getName() + "\nНовое значение: ");
            value = scanner.nextLine();
            if (!value.equals(""))
                editedToy.setName(value);

            System.out.print("Количество (прежнее значение): " + editedToy.getCount() + "\nНовое значение: ");
            value = scanner.nextLine();
            if (!value.equals(""))
                editedToy.setCount(Integer.parseInt(value));

            System.out.print("Вес (прежнее значение): " + editedToy.getWeight() + "%\nНовое значение: ");
            value = scanner.nextLine();
            if (!value.equals(""))
                editedToy.setWeight(Integer.parseInt(value));

            if (ToysStore.save()) {
                System.out.println("Игрушка с id=" + id + " успешно отредактирована.");
                return true;
            }

        } catch (Exception ex) {
            System.out.println("Ошибка при редактировании игрушки.\n" + ex.toString());
            return false;
        }
        return false;
    }

    public void PrizesToAwardShowAll() {
        LotteryModel lotteryModel = new LotteryModel();
        if (lotteryModel.loadPrizesToAward()) {
            lotteryModel.ShowTablePrizesToAward();
        }
        ReturnToPrevPos(); 
    }

    public boolean PrizeAddNew() {
        LotteryModel lotteryModel = new LotteryModel();
        if (lotteryModel.PrizesToAwardAddNew())
            return true;
        return false;
    }

    public String getPrevPos() {
        return prevPos;
    }

    public String getChoice() {
        return choice;
    }

    public String getNewPos() {
        return newPos;
    }

    public void setPrevPos(String prevPos) {
        this.prevPos = prevPos;
    }

    public void setChoice(String choice) {
        this.choice = choice;
    }

    public void setNewPos(String newPos) {
        this.newPos = newPos;
    }

    public void ResetMenuPos() {
        prevPos = "";
        choice = "";
        newPos = "";
    }

    public void ReturnToPrevPos() {
        newPos = prevPos;
        choice = "";
    }

    public Menu() {
        prevPos = "";
        choice = "";
        newPos = "";
    }
}
