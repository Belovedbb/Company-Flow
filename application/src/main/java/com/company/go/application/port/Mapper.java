package com.company.go.application.port;

import com.company.go.application.port.in.archive.PerformanceUseCase;
import com.company.go.application.port.in.archive.StaffUseCase;
import com.company.go.application.port.in.billing.AccountUseCase;
import com.company.go.application.port.in.global.RegisterUserUseCase;
import com.company.go.domain.archive.performance.Performance;
import com.company.go.domain.archive.staff.Staff;
import com.company.go.domain.billing.AggregateAccount;
import com.company.go.domain.billing.PerformanceAccount;
import com.company.go.domain.billing.ProductAccount;
import com.company.go.domain.billing.PurchaseOrderAccount;
import com.company.go.domain.global.User;
import org.springframework.util.Base64Utils;

import java.io.IOException;
import java.sql.SQLException;

public class Mapper {

    static public RegisterUserUseCase.RegisterUserModel convert(User user)  {
        RegisterUserUseCase.RegisterUserModel registerUserModel = new RegisterUserUseCase.RegisterUserModel();
        registerUserModel.setId(user.getId());
        registerUserModel.setPhoneNumber(user.getPhoneNumber());
        registerUserModel.setPictureType(user.getPictureType());
        registerUserModel.setEmail(user.getEmail());
        registerUserModel.setLastName(user.getLastName());
        registerUserModel.setFirstName(user.getFirstName());
        registerUserModel.setRoles(user.getRoles());
        registerUserModel.setAlias(user.getAlias());
        registerUserModel.setPassword(user.getPassword());
        registerUserModel.setAddress(user.getAddress());
        try {
            //memory intensive, fix later
            String data = Base64Utils.encodeToString(user.getProfilePicture().getBinaryStream().readAllBytes());
            registerUserModel.setPictureData(data);
        }catch (SQLException | IOException e){
            e.printStackTrace();
            registerUserModel.setPictureData(null);
        }
        return registerUserModel;
    }

    static public StaffUseCase.StaffViewModel convert(Staff staff){
        StaffUseCase.StaffViewModel staffViewModel = new  StaffUseCase.StaffViewModel();
        staffViewModel.setId(staff.getId());
        staffViewModel.setUserModel(Mapper.convert(staff.getUser()));
        staffViewModel.setPayment(staff.getPayment().getValue().doubleValue());
        staffViewModel.setStatus(staff.getStatus().name());
        return staffViewModel;
    }

    static public PerformanceUseCase.PerformanceViewModel convert(Performance performance){
        PerformanceUseCase.PerformanceViewModel performanceViewModel = new  PerformanceUseCase.PerformanceViewModel();
        performanceViewModel.setId(performance.getId());
        performanceViewModel.setStaff(Mapper.convert(performance.getStaff()));
        performanceViewModel.setAverageMonthlyPerformance(performance.getAverageMonthlyPerformance());
        performanceViewModel.setBonusPoint(performance.getBonusPoint());
        performanceViewModel.setStatus(performance.getStatus().name());
        performanceViewModel.setDate(performance.getDate());
        return performanceViewModel;
    }

    static public AccountUseCase.AccountViewModel convert(PerformanceAccount performanceAccount){
        AccountUseCase.PerformanceAccountViewModel performanceAccountViewModel = new AccountUseCase.PerformanceAccountViewModel();
        performanceAccountViewModel.setId(performanceAccount.getId());
        performanceAccountViewModel.setDate(performanceAccount.getDate());
        performanceAccountViewModel.setAggregateAmount(performanceAccount.getAggregateAmount());
        performanceAccountViewModel.setKind(performanceAccount.getKind().name());
        performanceAccountViewModel.setType(performanceAccount.getType());
        performanceAccountViewModel.setTypeCount(performanceAccount.getTypeCount());
        return performanceAccountViewModel;
    }

    static public AccountUseCase.AccountViewModel convert(ProductAccount productAccount){
        AccountUseCase.ProductAccountViewModel productAccountViewModel = new AccountUseCase.ProductAccountViewModel();
        productAccountViewModel.setId(productAccount.getId());
        productAccountViewModel.setDate(productAccount.getDate());
        productAccountViewModel.setAggregateAmount(productAccount.getAggregateAmount());
        productAccountViewModel.setKind(productAccount.getKind().name());
        productAccountViewModel.setType(productAccount.getType());
        productAccountViewModel.setTypeCount(productAccount.getTypeCount());
        return productAccountViewModel;
    }

    static public AccountUseCase.AccountViewModel convert(PurchaseOrderAccount purchaseOrderAccount){
        AccountUseCase.PurchaseOrderAccountViewModel purchaseOrderAccountViewModel = new AccountUseCase.PurchaseOrderAccountViewModel();
        purchaseOrderAccountViewModel.setId(purchaseOrderAccount.getId());
        purchaseOrderAccountViewModel.setDate(purchaseOrderAccount.getDate());
        purchaseOrderAccountViewModel.setAggregateAmount(purchaseOrderAccount.getAggregateAmount());
        purchaseOrderAccountViewModel.setKind(purchaseOrderAccount.getKind().name());
        purchaseOrderAccountViewModel.setType(purchaseOrderAccount.getType());
        purchaseOrderAccountViewModel.setTypeCount(purchaseOrderAccount.getTypeCount());
        return purchaseOrderAccountViewModel;
    }

    static public AccountUseCase.AccountViewModel convert(AggregateAccount aggregateAccount){
        AccountUseCase.AggregateAccountViewModel aggregateAccountViewModel = new AccountUseCase.AggregateAccountViewModel();
        aggregateAccountViewModel.setId(aggregateAccount.getId());
        aggregateAccountViewModel.setDate(aggregateAccount.getDate());
        aggregateAccountViewModel.setAggregateAmount(aggregateAccount.getAggregateAmount());
        aggregateAccountViewModel.setKind(aggregateAccount.getKind().name());
        aggregateAccountViewModel.setType(aggregateAccount.getType());
        aggregateAccountViewModel.setTypeCount(aggregateAccount.getTypeCount());
        return aggregateAccountViewModel;
    }
}
