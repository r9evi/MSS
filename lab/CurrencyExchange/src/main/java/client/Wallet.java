package client;

import currency.Currency;

import java.util.ArrayList;
import java.util.List;

public class Wallet {
    private final List<Double> balances;

    public Wallet() {
        this.balances = new ArrayList<>();
        fillWalletWithCurrencies();
    }

    public void deposit(Currency currency, double amount) {
        if (amount <= 0) {
            System.out.println("Сумма пополнения должна быть положительной");
        }
        balances.set(getIndex(currency), getBalance(currency) + amount);
        System.out.printf("Пополнение: %.2f %s\nНовый баланс: %.2f\n", amount, currency, getBalance(currency));
    }

    public double withdraw(Currency currency, double amount) {
        if (amount <= 0) {
            System.out.println("Сумма снятия должна быть положительной");
        }
        if (amount > getBalance(currency)) {
            System.out.printf("Недостаточно средств для снятия в валюте %s %n", currency);
        }
        balances.set(getIndex(currency), getBalance(currency) - amount);
        System.out.printf("Снятие: %.2f %s\nНовый баланс: %.2f\n", amount, currency, getBalance(currency));
        return amount;
    }

    private int getIndex(Currency currency) {
        return currency.ordinal();
    }

    public Double getBalance(Currency currency) {
        return balances.get(getIndex(currency));
    }

    private void fillWalletWithCurrencies() {
        int walletAmount = Currency.values().length;
        for (int i = 0; i < walletAmount; i++) {
            balances.add(0.0);
        }
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Кошелек {\n");
        for(int i = 0; i < balances.size(); i++) {
            Currency currency = Currency.values()[i];
            double balance = balances.get(i);
            sb.append(String.format(" %s: %.2f\n", currency, balance));
        }
        sb.append("}");
        return sb.toString();
    }
}
