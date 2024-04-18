package com.company.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
public class CardHolder {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JsonProperty("first_name")
    @Size(min = 2)
    private String firstName;

    @JsonProperty("last_name")
    @Size(min = 4)
    private String lastName;

    @JsonProperty("middle_name")
    @Size(min = 4)
    private String middleName;

    @JsonProperty("series_passport")
    @Size(min = 2, max = 2)
    private String passport;

    @JsonProperty("passport_number")
    @Size(min = 7, max = 7)
    private String number;

    @JsonProperty("series_number")
    private String seriesNum = passport + number;

    @Column(unique = true)
    @JsonProperty("pinfl")
    @Size(min = 14, max = 14)
    private String pinfl;

    @JsonProperty("passport_number")
    private Boolean isActive = true;

    @DateTimeFormat
    @JsonFormat(pattern = "yyyy-MM-dd")
    private String birthDay;


}
