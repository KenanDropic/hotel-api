package com.hotel.utils.projections;

import java.util.List;

public interface EmployeeView {
    Long getId();
    String getFirstName();
    String getLastName();
    String getEmail();
    List<String> getRoles();
}
