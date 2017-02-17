package com.dk.mp.core.entity;

/**
 * Created by dongqs on 16/7/21.
 */
public class App {
    private String url;
    private String idCat;
    private String nameCat;
    private String name;
    private String id;
    private String packageName;
    private String icon;
    private String action;

    private int x;
    private int y;

    private boolean selected = false;//被选中添加

    public App(){}

    public App(String url, String idCat, String nameCat, String name, String id, String packageName, String icon, String action){
        this.url = url;
        this.idCat=idCat;
        this.nameCat=nameCat;
        this.name=name;
        this.id=id;
        this.packageName=packageName;
        this.icon=icon;
        this.action=action;
    }

    public App(String url, String idCat, String nameCat, String name, String id, String packageName, String icon, String action, int x, int y){
        this.url = url;
        this.idCat=idCat;
        this.nameCat=nameCat;
        this.name=name;
        this.id=id;
        this.packageName=packageName;
        this.icon=icon;
        this.action=action;
        this.x=x;
        this.y=y;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public void setIdCat(String idCat) {
        this.idCat = idCat;
    }

    public void setNameCat(String nameCat) {
        this.nameCat = nameCat;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public String getUrl() {
        return url;
    }

    public String getIdCat() {
        return idCat;
    }

    public String getNameCat() {
        return nameCat;
    }

    public String getName() {
        return name;
    }

    public String getId() {
        return id;
    }

    public String getPackageName() {
        return packageName;
    }

    public String getIcon() {
        return icon;
    }

    public String getAction() {
        return action;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

}
