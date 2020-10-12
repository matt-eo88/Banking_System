package banking.cardmaker;

public class Card {
    private String cardNumber;
    private String pin;

    Card(String cardNumber, String pin) {
        this.cardNumber = cardNumber;
        this.pin = pin;
    }

    public String getCardNumber() {
        return this.cardNumber;
    }

    public String getPin() {
        return this.pin;
    }

    public void setCardNumber(String number) {
        this.cardNumber = number;
    }

    public void setPin(String pin) {
        this.pin = pin;
    }
}
