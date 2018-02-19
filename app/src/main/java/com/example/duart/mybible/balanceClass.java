package com.example.duart.mybible;

import java.sql.Timestamp;

/**
 * Created by duart on 15/02/2018.
 */

public class balanceClass {

    private Integer id;
    private Integer status;
    private String date;
    private String description;
    private Double value;
    private String source_destination;
    private String repay;
    private String repayment;
    private String type;


    public balanceClass(Integer idParam, String dateParam, String descriptionParam, Double valueParam, String source_destinationParam, String repayParam, String repaymentParam, String typeParam, Integer statusParam){

        this.id = idParam;
        this.date = dateParam;
        this.description = descriptionParam;
        this.value = valueParam;
        this.source_destination = source_destinationParam;
        this.repay = repayParam;
        this.repayment = repaymentParam;
        this.type = typeParam;
        this.status = statusParam;

    }

    public String getDate() {
        return date;
    }
    public void setDate(String date) {
        this.date = date;
    }

    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }

    public Double getValue() {
        return value;
    }
    public void setValue(Double value) {
        this.value = value;
    }

    public String getSourceDestination() {
        return source_destination;
    }
    public void setSourceDestination(String source_destination) {
        this.source_destination = source_destination;
    }

    public String getRepay() {
        return repay;
    }
    public void setRepay(String repay) {
        this.repay = repay;
    }

    public String getRepayment() {
        return repayment;
    }
    public void setRepayment(String repayment) {
        this.repayment = repayment;
    }

    public String getType() {
        return type;
    }
    public void setType(String type) {
        this.type = type;
    }

    public Integer getStatus(){
        return status;
    }
    public void setStatus(Integer status){
        this.status = status;
    }

    public Integer getId(){
        return id;
    }
    public void setId(Integer id){
        this.id = id;
    }

}
