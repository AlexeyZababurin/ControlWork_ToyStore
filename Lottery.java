package GeekBrains.ControlWork_ToyStore;

import java.util.LinkedList;
import java.util.List;

public class Lottery {
    List<Prize> prizesToAward;
    
    public Lottery() {
        this.prizesToAward = new LinkedList<>();
    }

    public List<Prize> getPrizesToAward() {
        return prizesToAward;
    }

    public void setPrizesToAward(List<Prize> prizesToAward) {
        this.prizesToAward = prizesToAward;
    }
}
