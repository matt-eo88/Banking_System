package banking;

import banking.cardmaker.CardFactory;

import java.util.Scanner;

class ATM {

    private final Scanner scan = new Scanner(System.in);
    private Bank bank;
    private boolean systemOn;
    private Account currentAccount;
    private String filename;

    public ATM(String filename) {
        this.bank = new Bank();
        this.systemOn = true;
        this.currentAccount = null;
        this.filename = filename;
    }

    public void startSystem() {
        while (systemOn) {
            DataManager.connectToDatabase(this.filename);
            System.out.println("1. Create an account\n" + "2. Log into account\n" + "0. Exit");
            String input = scan.nextLine();
            switch (input) {
                case "1":
                    createAccount();
                    break;
                case "2":
                    logIn();
                    break;
                case "0":
                    System.out.println();
                    System.out.println("Bye!");
                    systemOn = false;
                    DataManager.disconnect();
                    break;
            }
        }
    }

    private void createAccount() {
        currentAccount = new Account();
        DataManager.insertData(currentAccount);
        bank.addAccount(currentAccount);
        System.out.println();
        System.out.println("Your card has been created");
        System.out.println("Your card number:");
        System.out.println(currentAccount.getCard().getCardNumber());
        System.out.println("Your card PIN:");
        System.out.println(currentAccount.getCard().getPin());
        System.out.println();
    }

    private void logIn() {
        System.out.println();
        System.out.println("Enter your card number:");
        String num = scan.nextLine();
        System.out.println("Enter your PIN:");
        String pin = scan.nextLine();
        if (DataManager.isCorrect(num, pin)) {
            setCurrentAccount(num, pin);
            System.out.println();
            System.out.println("You have successfully logged in!");
            System.out.println();
            logInMenu();
        } else {
            System.out.println();
            System.out.println("Wrong card number or PIN!");
            System.out.println();
        }
    }

    private void logInMenu() {
        boolean menuOn = true;
        while (menuOn) {
            DataManager.connectToDatabase(this.filename);
            System.out.println("1. Balance\n" + "2. Add income\n" + "3. Do transfer\n"
                    + "4. Close account\n" + "5. Log out\n" + "0. Exit");
            String input = scan.nextLine();
            switch (input) {
                case "1":
                    System.out.println();
                    int balance = DataManager.readBalance(currentAccount.getCard().getCardNumber());
                    System.out.println("Balance: " + balance);
                    System.out.println();
                    break;
                case "2":
                    addIncome();
                    break;
                case "3":
                    startTransfer();
                    break;
                case "4":
                    System.out.println();
                    DataManager.closeAccount(currentAccount.getCard().getCardNumber());
                    System.out.println("The account has been closed!");
                    System.out.println();
                    resetCurrentAccount();
                    menuOn = false;
                    break;
                case "5":
                    System.out.println();
                    System.out.println("You have successfully logged out!");
                    System.out.println();
                    resetCurrentAccount();
                    menuOn = false;
                    break;
                case "0":
                    System.out.println();
                    System.out.println("Bye!");
                    systemOn = false;
                    menuOn = false;
                    DataManager.disconnect();
                    break;
            }
        }
    }

    private void addIncome() {
        System.out.println();
        System.out.println("Enter income:");
        int income = Integer.parseInt(scan.nextLine());
        DataManager.addIncome(income, currentAccount.getCard().getCardNumber());
        System.out.println("Income was added!");
        System.out.println();
    }

    private void startTransfer() {
        System.out.println();
        System.out.println("Transfer");
        System.out.println("Enter card number:");
        String receiver = scan.nextLine();
        if (!CardFactory.isLuhn(receiver)) {
            System.out.println("Probably you made a mistake in the card number. Please try again!");
            System.out.println();
            return;
        }
        if (!DataManager.isPresent(receiver)) {
            System.out.println("Such card does not exist.");
            System.out.println();
            return;
        }
        if (receiver.equals(currentAccount.getCard().getCardNumber())) {
            System.out.println("You can't transfer money to the same account!");
            System.out.println();
            return;
        }
        System.out.println("Enter how much money you want to transfer:");
        int amount = Integer.parseInt(scan.nextLine());
        if (!DataManager.isAvailable(amount, currentAccount.getCard().getCardNumber())) {
            System.out.println("Not enough money!");
            System.out.println();
            return;
        }
        DataManager.doTransfer(amount, receiver);
        DataManager.reduceBalance(amount, currentAccount.getCard().getCardNumber());
        System.out.println("Success!");
        System.out.println();
    }

    private void setCurrentAccount(String number, String pin) {
        if (currentAccount == null) {
            currentAccount = new Account();
        }
        currentAccount.getCard().setCardNumber(number);
        currentAccount.getCard().setPin(pin);
    }

    private void resetCurrentAccount() {
        this.currentAccount = null;
    }

}
