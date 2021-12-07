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
public class Month {

    private int _stok;
    private int _add;

    public Month(int _stok, int _add) {
        this._stok = _stok;
        this._add = _add;
    }
    
    public int stok(){
        return _stok;
    }
    public int add(){
        return _add;
    }
}
