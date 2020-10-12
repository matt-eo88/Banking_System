package banking;

public class Main {
    public static void main(String[] args) {
        if (!args[0].toLowerCase().equals("-filename")) {
            System.out.println(args[0] + " not valid");
            System.out.println("usage: banking -filename filename");
            return;
        }

        ATM atm = new ATM(args[1]);
        atm.startSystem();
    }
}
