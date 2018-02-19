package com.example.duart.mybible;

/**
 * Created by duart on 16/02/2018.
 */

public class columnsClass {

    private String name;
    private String status;
    private Integer id;

    public columnsClass (String nameParam, String statusParam ){

        this.name = nameParam;
        this.status = statusParam;

    }

    public String getName(){
        return name;
    }
    public void setName( String name ){
        this.name = name;
    }

    public String getStatus(){
        return status;
    }
    public void setStatus(String status) {
        this.status = status;
    }

    public Integer getId(){
        return id;
    }
    public void setId( Integer id){
        this.id = id;
    }
}
