package com.increff.employee.model.data;

import lombok.Getter;
import lombok.Setter;

import java.util.List;
@Getter
@Setter
public class PdfData {
	private Integer orderId;
	private String invoiceDate;
	private String invoiceTime;
	private List<PdfListData> itemList;
	private Double total;

}