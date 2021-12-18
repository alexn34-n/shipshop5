package com.ship.shipshop5.entity;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.PrePersist;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.UUID;

@Entity
public class Product implements Serializable {

    @Id
    @GenericGenerator(name="UUID",strategy = "org.hibernate.id.UUIDGenerator")
    private UUID id;

    @Column(name="name",nullable = false)
    private String name;

//    @Column(name="category",nullable = false)
//    private String category;


    @Column(name="count")
    private int count;

    @Column(name="price")
    private BigDecimal price;

//    @Column(name="port_Delivery")
//    private String port_Delivery;


//    @Column(name="next_date")
//    private Date next_date;

    @PrePersist
    private  void init(){
        if(this.id==null){
            this.id=UUID.randomUUID();
        }
    }

    public UUID getId() {
        return id;
    }

    public Product setId(UUID id) {
        this.id = id;
        return this;
    }

    public String getName() {
        return name;
    }

    public Product setName(String name) {
        this.name = name;
        return this;
    }

    public int getCount() {
        return count;
    }

    public Product setCount(int count) {
        this.count = count;
        return this;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public Product setPrice(BigDecimal price) {
        this.price = price;
        return this;
    }
    public void incrementCount(){
        this.count++;
    }
    public  void  decreaseCount(){
        this.count--;
    }
}

//



