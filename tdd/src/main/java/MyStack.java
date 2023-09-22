import java.util.ArrayList;
import java.util.List;

public class MyStack {

    private List<Integer> list = new ArrayList<>();

    public boolean isEmpty() {
        return list.isEmpty();
    }

    public void push(final int i) {
        list.add(i);
    }

    public int size() {
        return list.size();
    }
}
