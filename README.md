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

### Reference Documentation

For further reference, please consider the following sections:

* [Spring Boot Maven Plugin Reference Guide](https://docs.spring.io/spring-boot/docs/3.2.4/maven-plugin/reference/html/)
* [Spring Data JPA](https://docs.spring.io/spring-boot/docs/3.2.4/reference/htmlsingle/index.html#data.sql.jpa-and-spring-data)
* [Spring Web](https://docs.spring.io/spring-boot/docs/3.2.4/reference/htmlsingle/index.html#web)



Add this code  for run project
```java
@SpringBootApplication
public class AtmCardHolderApplication {

    public static void main(String[] args) {
        SpringApplication.run(AtmCardHolderApplication.class, args);
    }

}
```


