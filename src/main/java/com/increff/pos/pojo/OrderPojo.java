package com.increff.pos.pojo;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.sql.Timestamp;

@Getter
@Setter
@Entity
@Table(name = "order_tables")
public class OrderPojo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @CreationTimestamp
    private Timestamp createdAt;

    private Timestamp invoicedCreatedAt;
    
    private String path;

    private Boolean isInvoiceCreated = false;
}