package banking;

import banking.cardmaker.Card;
import banking.cardmaker.CardFactory;

class Account {

    private String accHolderName;
    private Card card;

    public Account() {
        this("");
    }

    public Account(String accHolderName) {
        this.card = CardFactory.issueCard();
        this.accHolderName = accHolderName;
    }

    //getAccHolderName - never used;
    public String getAccHolderName() {
        return this.accHolderName;
    }

    //setAccHolderName - never used;
    public void setAccHolderName(String name) {
        this.accHolderName = name;
    }

    public Card getCard() {
        return this.card;
    }
}
