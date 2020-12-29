package com.company.go.application.port.out.archive;

import com.company.go.Utilities;
import com.company.go.domain.archive.staff.Staff;
import com.company.go.domain.inventory.order.Order;

import java.util.List;
import java.util.Map;

public interface UpdateStaffPort {
    boolean storeStaff(Staff staff);

    boolean updateStaff(Long id, Staff currentStaff);

    Staff getStaff(Long id);

    boolean removeStaff(Long id);

    Long getStaffMaxId();

    List<Staff> getTotalStaff();

    List<Staff> getTotalFilteredStaff(Map<String, Object> parameters);

    List<Staff> getTotalFilteredStaff(List<Utilities.Filter> filterList, Utilities.FilterCondition condition);

}
