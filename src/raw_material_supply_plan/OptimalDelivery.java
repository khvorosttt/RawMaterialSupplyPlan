/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package raw_material_supply_plan;

/**
 *
 * @author Lenovo
 */
public class OptimalDelivery {
    private int _i;//запасы
    private int _x;//поставки
    private int _f;//затраты
    
    public OptimalDelivery(int _i, int _x, int _f){
        this._i=_i;
        this._x=_x;
        this._f=_f;
    }
    
    public int I(){
        return _i;
    }
    public void I(int _i){
        this._i=_i;
    }
    
    public int X(){
        return _x;
    }
    public void X(int _x){
        this._x=_x;
    }
    
    public int F(){
        return _f;
    }
    public void F(int _f){
        this._f=_f;
    }
    
}
