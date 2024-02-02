package GeekBrains.ControlWork_ToyStore;

public class Prize {
    private int id;
    private Toy toy;
    
    public Prize(int id, Toy toy) {
        this.id = id;
        this.toy = toy;
    }

    public int getId() {
        return id;
    }

    public Toy getToy() {
        return toy;
    }

    public void setToy(Toy toy) {
        this.toy = toy;
    }

    @Override
    public String toString() {
        return "Приз: [id=" + id + "]\n" +
                "Игрушка: " + toy.toStringAsPrize() + "\n***************";
    }
}
