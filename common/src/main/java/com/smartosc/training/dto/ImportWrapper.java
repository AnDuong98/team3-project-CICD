package com.smartosc.training.dto;

import java.util.ArrayList;
import java.util.List;

public class ImportWrapper {

    private List<OrderDetailDTO> orderDetailDTOS;

    public ImportWrapper()
    {
        orderDetailDTOS = new ArrayList<>();
    }

    public ImportWrapper(List<OrderDetailDTO> orderDetailDTOS)
    {
        this.orderDetailDTOS = orderDetailDTOS;
    }

    public void setOrderDetailDTOS(List<OrderDetailDTO> employees) {
        this.orderDetailDTOS = employees;
    }

    public List<OrderDetailDTO> getOrderDetailDTOS()
    {
        return orderDetailDTOS;
    }
}
