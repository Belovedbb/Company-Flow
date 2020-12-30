package com.company.go.domain.billing;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
public class PerformanceAccount extends Account{

    public PerformanceAccount(Long id, String type, Double aggregateAmount, LocalDate date, Long typeCount, Constants.Kind kind) {
        super(id, type, aggregateAmount, date, typeCount, kind);
    }

    @Override
    public Constants.Kind getKind(){
        return Constants.Kind.DEBIT;
    }

    @Override
    public String getType(){
        return Constants.Type.PERFORMANCE.name();
    }

}
