package com.company.go.application.port.in.archive;

import com.company.go.Utilities;
import com.company.go.application.port.in.global.RegisterUserUseCase;
import com.company.go.application.port.in.inventory.PurchaseOrderUseCase;
import com.company.go.domain.archive.staff.Staff;
import com.company.go.domain.inventory.Money;
import com.company.go.domain.inventory.product.Product;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public interface StaffUseCase {

    boolean storeStaff(StaffViewModel model);

    boolean editStaff(Long id, StaffViewModel currentModel);

    StaffViewModel viewStaff(Long id);

    List<StaffViewModel> getAllStaffs();

    List<StaffViewModel> getFilteredStaffs(StaffViewModel criteriaModel);

    List<StaffViewModel> getFilteredStaffs(StaffViewModel criteriaModel, RegisterUserUseCase.RegisterUserModel userCriteriaModel, Utilities.FilterCondition condition) throws IOException, SQLException;

    List<RegisterUserUseCase.RegisterUserModel> getAvailableRegisteredUsers();

    boolean deleteStaff(Long id);

    Long getStaffMaxId();

    @Getter
    @Setter
    class StaffViewModel{
        private Long id;

        @NotNull
        private Double payment;

        @NotNull
        private RegisterUserUseCase.RegisterUserModel userModel;

        private String status = Staff.Constants.Status.ACTIVE.name();

        public List<String> getLoadedStatus(){
            ArrayList<String> statuses = new ArrayList<>();
            statuses.add(Staff.Constants.Status.ACTIVE.name());
            statuses.add(Staff.Constants.Status.INACTIVE.name());
            return statuses;
        }

        public StaffViewModel createStaff(RegisterUserUseCase.RegisterUserModel user){
            this.payment = null;
            this.userModel = user;
            return  this;
        }

        public Staff toStaff() throws IOException, SQLException {
            return new Staff(
                    id,
                    userModel  == null ? null : userModel.toUser(),
                    new Money(Money.doubleToBigDecimal((payment == null ? 0d : payment))),
                    status == null ? null : Staff.Constants.Status.valueOf(status)
            );
        }
    }
}
