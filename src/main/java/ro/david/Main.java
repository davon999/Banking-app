package ro.david;

import java.math.BigDecimal;

public class Main {
    public static void main(String[] args) {
        Bank bank = new Bank();

        Account david = bank.openAccount("David Regele", Currency.RON);
        Account ana = bank.openAccount("Ana Popescu", Currency.RON);
        Account john = bank.openAccount("John Smith", Currency.EUR);

        david.deposit(new BigDecimal("100"));

        bank.transfer(david.getIban(), ana.getIban(), new BigDecimal("40"));
        System.out.println("Dupa transfer: David = " + david.getBalance() + ", Ana = " + ana.getBalance());

        try {
            bank.transfer(david.getIban(), ana.getIban(), new BigDecimal("500"));
        } catch (Exception e) {
            System.out.println("Eroare asteptata: " + e.getMessage());
        }
        System.out.println("Dupa transfer esuat: David = " + david.getBalance() + ", Ana = " + ana.getBalance());

        try {
            bank.transfer(david.getIban(), david.getIban(), new BigDecimal("10"));
        } catch (Exception e) {
            System.out.println("Eroare asteptata: " + e.getMessage());
        }

        try {
            bank.transfer(david.getIban(), john.getIban(), new BigDecimal("10"));
        } catch (Exception e) {
            System.out.println("Eroare asteptata: " + e.getMessage());
        }

        try {
            bank.transfer(david.getIban(), "RO49DAVB9999999999", new BigDecimal("10"));
        } catch (Exception e) {
            System.out.println("Eroare asteptata: " + e.getMessage());
        }

        System.out.println("Sold final: David = " + david.getBalance() + ", Ana = " + ana.getBalance());
    }
}