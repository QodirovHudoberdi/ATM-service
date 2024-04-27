# ATM -service

### Automation of ATM operation systems

This system includes:

* Save a cardHolder details
* Add cards , card types And banknotes to system
* Possibility to top up the card through ATM
* The ability to withdraw cash from the card through an ATM
* The ability to make transfers between cards
* The ability to get all transactions performed through the card in a sorted order
*  For the programmer : the ability to record the device of the client who used the program and the actions performed in an archive file




Add this code  for run a project
```java
@SpringBootApplication
public class AtmCardHolderApplication {

    public static void main(String[] args) {
        SpringApplication.run(AtmCardHolderApplication.class, args);
    }

}
```
# ATM v3 Service

## Description



***This is a simple ATM service that allows you to perform the following operations:***



| No |                                                                       Services                                                                        | Status |
|:--:|:-----------------------------------------------------------------------------------------------------------------------------------------------------:|:------:|
| 1  | [__Card Holder Service__](https://github.com/QodirovHudoberdi/ATM-service/blob/master/src/main/java/com/company/controller/CardHolderController.java) |   ✅    |
| 2  |   [__BankNote  Service__](https://github.com/QodirovHudoberdi/ATM-service/blob/master/src/main/java/com/company/controller/BankNoteController.java)   |   ✅    |
| 3  |       [__Card Service__](https://github.com/QodirovHudoberdi/ATM-service/blob/master/src/main/java/com/company/controller/CardController.java)        |   ✅    |
| 4  |    [__Cashing Service__](https://github.com/QodirovHudoberdi/ATM-service/blob/master/src/main/java/com/company/controller/CashingController.java)     |   ✅    |
| 5  |  [__Fill Out  Card Service__](https://github.com/QodirovHudoberdi/ATM-service/blob/master/src/main/java/com/company/controller/CardController.java)   |   ✅    |
| 6  |     [__Transfer Service__ ](https://github.com/QodirovHudoberdi/ATM-service/blob/master/src/main/java/com/company/controller/CardController.java)     |   ✅    |
| 7  |     [__Card Type Service__](https://github.com/QodirovHudoberdi/ATM-service/blob/master/src/main/java/com/company/controller/CardController.java)     |   ✅    |




