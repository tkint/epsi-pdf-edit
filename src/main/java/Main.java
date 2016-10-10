import model.Test;

/**
 * Created by Thomas on 10/10/2016.
 */
public class Main {
    public static void main(String[] args) {
        Test test = new Test();

        test.setId(5);
        test.setFirstname("Toto");
        test.setLastname("Jo");
        System.out.println(test.toString());
    }
}
