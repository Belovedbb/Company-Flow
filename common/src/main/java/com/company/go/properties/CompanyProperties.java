package com.company.go.properties;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;


@ConfigurationProperties("company")
@Getter
@Setter
@Validated
public class CompanyProperties {

    @NotEmpty
    private List<String> productTypes;

    @NotEmpty
    private String currency;


}
