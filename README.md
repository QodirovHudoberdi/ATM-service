# ATM -service

### Automation of ATM operation systems

This system includes:

* Reformed  a cardHolder details
* Chance to perform actions on Banknote
* Chance to perform actions on card
* The ability to withdraw cash from the card through an ATM
* Possibility to top up the card through ATM
* The ability to make transfers between cards
* Chance to perform actions on card type
* The ability to get all transactions performed through the card in a sorted order




```java
@SpringBootApplication
public class AtmCardHolderApplication {

    public static void main(String[] args) {
        SpringApplication.run(AtmCardHolderApplication.class, args);
    }

}
```



***You can get the services listed above in this table through the links***



| No |                                                                       Services                                                                        | Status |
|:--:|:-----------------------------------------------------------------------------------------------------------------------------------------------------:|:------:|
| 1  | [__Card Holder Service__](https://github.com/QodirovHudoberdi/ATM-service/blob/master/src/main/java/com/company/controller/CardHolderController.java) |   ✅    |
| 2  |   [__BankNote  Service__](https://github.com/QodirovHudoberdi/ATM-service/blob/master/src/main/java/com/company/controller/BankNoteController.java)   |   ✅    |
| 3  |       [__Card Service__](https://github.com/QodirovHudoberdi/ATM-service/blob/master/src/main/java/com/company/controller/CardController.java)        |   ✅    |
| 4  |    [__Cashing Service__](https://github.com/QodirovHudoberdi/ATM-service/blob/master/src/main/java/com/company/controller/CashingController.java)     |   ✅    |
| 5  |  [__Fill Out  Card Service__](https://github.com/QodirovHudoberdi/ATM-service/blob/master/src/main/java/com/company/controller/CardController.java)   |   ✅    |
| 6  |     [__Transfer Service__ ](https://github.com/QodirovHudoberdi/ATM-service/blob/master/src/main/java/com/company/controller/CardController.java)     |   ✅    |
| 7  |     [__Card Type Service__](https://github.com/QodirovHudoberdi/ATM-service/blob/master/src/main/java/com/company/controller/CardController.java)     |   ✅    |
| 8  |     [__Get Card History__](https://github.com/QodirovHudoberdi/ATM-service/blob/master/src/main/java/com/company/controller/CardController.java)      |   ✅    |




