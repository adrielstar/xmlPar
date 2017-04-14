package Test;

/**
 * Created by walterad on 11-4-2017.
 */
public class test1 {
    public static void main(String[] args) {
        String myName = "1234567891012345678910";
//        String one = myName.substring(0,9);
//        String twoo = myName.substring(13);
//        int x = 0;
//        String formatValue = String.format("%04d", x);
//        String newName = myName.substring(0)+String.format("%04d", 1);
//        System.out.println(newName);
//        System.out.println(twoo);
//        System.out.println(one + formatValue + twoo);

        StringBuilder stringBuilder = new StringBuilder(myName);
        stringBuilder.replace(5, 9, "0001");
        String newMyName = stringBuilder.toString();

        System.out.println(newMyName);
    }
}
