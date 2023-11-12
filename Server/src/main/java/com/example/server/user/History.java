package com.example.server.user;

public class History {
    private long id;
    private String name;
    private String rid;

    public Long getId() {
        return id;
    }

    public History() {
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRid() {
        return rid;
    }

    public void setRid(String rid) {
        this.rid = rid;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public  String getName() {
        return name;
    }

}
