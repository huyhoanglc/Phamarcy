/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Data;

import AccountInfo.AccountInfoController;
import DrugList.DruglistController;
import AddDrug.AddDrugController;

/**
 *
 * @author Admin
 */
public class ControllerHolder {

    private static ControllerHolder instance = new ControllerHolder();

    private DruglistController druglistController;

    private AddDrugController addDrugController;
    
    private AccountInfoController accountinfoController;

    

    private ControllerHolder() {
    }

    public AccountInfoController getAccountinfoController() {
        return accountinfoController;
    }

    public void setAccountinfoController(AccountInfoController accountinfoController) {
        this.accountinfoController = accountinfoController;
    }
    
    public static ControllerHolder getInstance() {
        return instance;
    }

    public AddDrugController getAddDrugController() {
        return addDrugController;
    }

    public void setAddDrugController(AddDrugController addDrugController) {
        this.addDrugController = addDrugController;
    }

    public DruglistController getDruglistController() {
        return druglistController;
    }

    public void setDruglistController(DruglistController druglistController) {
        this.druglistController = druglistController;
    }
}
