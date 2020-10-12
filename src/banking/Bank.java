package banking;

import java.util.ArrayList;

class Bank {

    private ArrayList<Account> currentSessionAccounts;

    public Bank() {
        this.currentSessionAccounts = new ArrayList<>();
    }

    public void addAccount(Account a) {
        currentSessionAccounts.add(a);
    }

}
