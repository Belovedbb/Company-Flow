package com.company.go.domain.billing;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
public class AggregateAccount extends Account{

    public AggregateAccount(Long id, String type, Double aggregateAmount, LocalDate date, Long typeCount, Constants.Kind kind) {
        super(id, type, aggregateAmount, date, typeCount, kind);
    }

    @Override
    public Constants.Kind getKind(){
        return Constants.Kind.TOTAL;
    }

    @Override
    public String getType(){
        return Constants.Type.AGGREGATE.name();
    }
}
