package com.smartosc.training.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ReportDTO extends AbstractDTO{

    private Long id;
    private String name;
    private String product;
    private String price;
    private int quantity;
    private String total;


}
