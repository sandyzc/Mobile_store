package store.mobile_kfil.freaklab.sandyz.com.mobile_store;

/**
 * Created by santosh on 18-08-2017.
 */

public class Beans {

    private String code;
    private String descp;
    private String equip;
    private String pip_stock;

    public String getPip_stock_uom() {
        return pip_stock_uom;
    }

    public void setPip_stock_uom(String pip_stock_uom) {
        this.pip_stock_uom = pip_stock_uom;
    }

    public String getFdy_stock_uom() {
        return fdy_stock_uom;
    }

    public void setFdy_stock_uom(String fdy_stock_uom) {
        this.fdy_stock_uom = fdy_stock_uom;
    }

    private String pip_stock_uom;

    public String getPip_stock() {
        return pip_stock;
    }

    public void setPip_stock(String pip_stock) {
        this.pip_stock = pip_stock;
    }

    public String getFdy_stock() {
        return fdy_stock;
    }

    public void setFdy_stock(String fdy_stock) {
        this.fdy_stock = fdy_stock;
    }

    public String getPip_location() {
        return pip_location;
    }

    public void setPip_location(String pip_location) {
        this.pip_location = pip_location;
    }

    public String getFdy_location() {
        return fdy_location;
    }

    public void setFdy_location(String fdy_location) {
        this.fdy_location = fdy_location;
    }

    private String fdy_stock ;
    private String fdy_stock_uom ;
    private String pip_location;
    private String fdy_location;

    private int id;

    public Beans() {
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getDescp() {
        return descp;
    }

    public void setDescp(String descp) {
        this.descp = descp;
    }

    public String getEquip() {
        return equip;
    }

    public void setEquip(String equip) {
        this.equip = equip;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
