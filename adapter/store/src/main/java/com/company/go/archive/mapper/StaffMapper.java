package com.company.go.archive.mapper;

import com.company.go.StoreUtilities;
import com.company.go.Utilities;
import com.company.go.archive.staff.Constants;
import com.company.go.archive.staff.StaffEntity;
import com.company.go.domain.archive.staff.Staff;
import com.company.go.domain.inventory.Money;
import com.company.go.global.mapper.UserMapper;

import java.util.EnumMap;
import java.util.Map;

public class StaffMapper {
    private static final Map<Constants.StaffStatus, Staff.Constants.Status> statusMapping;

    static {
        statusMapping = new EnumMap<>(Constants.StaffStatus.class);

        statusMapping.put(Constants.StaffStatus.ACTIVE, Staff.Constants.Status.ACTIVE);
        statusMapping.put(Constants.StaffStatus.INACTIVE, Staff.Constants.Status.INACTIVE);
    }


    static public Staff mapStaffEntityToStaffDomain(StaffEntity entity){
        Staff staff = new Staff();
        staff.setId(entity.getId());
        staff.setPayment(new Money(Money.doubleToBigDecimal(entity.getPayment()), entity.getCurrency()));
        staff.setUser(UserMapper.mapUserEntityToUserDomain(entity.getUserType()));
        staff.setStatus(statusMapping.get(entity.getStatus()));
        return staff;
    }

    static public StaffEntity mapStaffDomainToStaffEntity(Staff staff){
        StaffEntity entity = new StaffEntity();
        entity.setId(staff.getId());
        Money payment = staff.getPayment();
        entity.setPayment(payment != null ? payment.getValue().doubleValue() : 0d);
        entity.setCurrency(payment != null ? staff.getPayment().getCurrency() : null);
        entity.setUserType(UserMapper.mapUserDomainToUserEntity(staff.getUser()));
        entity.setStatus(Utilities.getKeyByValue(statusMapping, staff.getStatus()));
        entity.setCompanyRegisterer(StoreUtilities.getUser());
        return entity;
    }
}
