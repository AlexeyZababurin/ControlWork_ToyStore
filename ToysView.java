package GeekBrains.ControlWork_ToyStore;

import java.util.List;

public class ToysView {
        List<Toy> toys;
    
    public ToysView(List<Toy> toys) {
        this.toys = toys;
    }

    public void ShowTable() {
        System.out.println("\nСписок игрушек\n---------------");
        for (Toy toy : toys) {
            System.out.println(toy.toString());
        }
    }
}
