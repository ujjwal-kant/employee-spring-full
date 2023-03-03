package com.increff.pos.pojo;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "inventory")
public class InventoryPojo{
	
	@Id
	@Column(name = "product_id")
    private Integer productId;
	@Column(nullable = false)
	private Integer quantity;
}