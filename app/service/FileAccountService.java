package app.service;

import app.domain.Account;
import app.exception.UnknownAccountException;
import app.exception.NotEnoughtMoneyException;
import app.repository.Repository;
import app.store.StoreAccounts;
import org.springframework.beans.factory.annotation.Autowired;

public class FileAccountService implements AccountService {


    @Autowired
    private Repository filerepository;

    public void setFr(Repository filerepository) {
        this.filerepository = filerepository;
    }

    @Override //снять указанную сумму
    public void withdraw(int accountId, int amount) throws NotEnoughtMoneyException, UnknownAccountException {
        Account account = null;
        for (Account acc: StoreAccounts.accounts) {
            if (acc.getId() == accountId) {
                account = acc;
                break;
            }
        }
        if (account == null) {
            throw new UnknownAccountException();
        }
        if (account.getAmount() < amount) {
            throw new NotEnoughtMoneyException();
        }
        account.setAmount((int) (account.getAmount() - amount));
        filerepository.write(account);
    }


    @Override //вывести баланс
    public int balance(int accountId) throws UnknownAccountException {
        Account account = null;
        for (Account acc: StoreAccounts.accounts) {
            if (acc.getId() == accountId) {
                account = acc;
                break;
            }
        }
        if (account == null) {
            throw new UnknownAccountException();
        }
        return (int) account.getAmount();
    }

    @Override //внести сумму на указанный счет
    public void deposit(int accountId, int amount) throws UnknownAccountException {
        Account account = null;
        for (Account acc: StoreAccounts.accounts) {
            if (acc.getId() == accountId) {
                account = acc;
                break;
            }
        }
        if (account == null) {
            throw new UnknownAccountException();
        }
        account.setAmount((int) (account.getAmount() + amount));
        filerepository.write(account);
    }

    @Override //перевести сумму с одного на другой
    public void transfer(int accountFrom, int accountTo, int amount) throws NotEnoughtMoneyException, UnknownAccountException {
        Account account_from = null;
        Account account_to = null;
        for (Account acc: StoreAccounts.accounts) {
            if (acc.getId() == accountFrom) {
                account_from = acc;
            }
            if (acc.getId() == accountTo) {
                account_to = acc;
            }
        }
        if ((account_from == null) & (account_to == null)) {
            throw new UnknownAccountException();
        }
        if (account_from.getAmount() < amount) {
            throw new NotEnoughtMoneyException();
        }
        account_from.setAmount((int) (account_from.getAmount() - amount));
        account_to.setAmount((int) (account_to.getAmount() + amount));
        filerepository.write(account_from);
        filerepository.write(account_to);
    }
}
