package app.controller;

import app.exception.NotEnoughtMoneyException;
import app.exception.UnknownAccountException;
import app.service.AccountService;
import app.store.StoreService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
public class RestAccountController {

    @Autowired
    StoreService storeService;

    @Autowired
    AccountService fileaccountservice;

    @GetMapping ("/balance") //Готовая ссылка для проверки: http://localhost:8080/balance?acc_id=1
    public ResponseEntity<String> balance(@RequestParam("acc_id") int acc_id) throws UnknownAccountException {
        storeService.startofwork();
        return new ResponseEntity<>("Баланс счета: " + fileaccountservice.balance(acc_id), HttpStatus.OK);
    }

    @GetMapping ("/deposite/{acc_id}/{amount}")//Готовая ссылка для проверки: http://localhost:8080/deposite/1/100
    public  ResponseEntity<String> deposite(@PathVariable("acc_id")int acc_id, @PathVariable("amount")int amount) throws UnknownAccountException {
        storeService.startofwork();
        fileaccountservice.deposit(acc_id, amount);
        return new ResponseEntity<>("Операция пополнения счета выполнена. Сумма на счете: " + fileaccountservice.balance(acc_id), HttpStatus.OK);
    }

    @PostMapping("/withdraw")
    public  ResponseEntity<String> withdraw(@RequestParam("acc_id") int acc_id, @RequestParam("amount") int amount) throws UnknownAccountException, NotEnoughtMoneyException {
        storeService.startofwork();
        fileaccountservice.withdraw(acc_id, amount);
        return new ResponseEntity<>("Операция списания выполнена. Сумма на счете: " + fileaccountservice.balance(acc_id), HttpStatus.OK);
    }

    @PostMapping ("/transfer")
    public  ResponseEntity<String> transfer(@RequestBody Values values) throws UnknownAccountException, NotEnoughtMoneyException {
        storeService.startofwork();
        fileaccountservice.transfer(values.getAcc_from(), values.getAcc_to(), values.getAmount());
        return new ResponseEntity<>("Операция выполнена. Сумма на счете получателя: " + fileaccountservice.balance(values.getAcc_to()) +
                ". Сумма на счете отправителя: " + fileaccountservice.balance(values.getAcc_from()), HttpStatus.OK);
    }

    static class Values {
        private int acc_from;
        private int acc_to;
        private int amount;

        public Values(){
        }

        public Values(int acc_from, int acc_to, int amount){
            this.acc_from = acc_from;
            this.acc_to = acc_to;
            this.amount = amount;
        }

        public int getAcc_from() {
            return acc_from;
        }

        public int getAcc_to() {
            return acc_to;
        }

        public int getAmount() {
            return amount;
        }

        public void setAcc_from(int acc_from) {
            this.acc_from = acc_from;
        }

        public void setAcc_to(int acc_to) {
            this.acc_to = acc_to;
        }

        public void setAmount(int amount) {
            this.amount = amount;
        }
    }
}
