package com.example.richard_dt.visualisation.Helper;

/**
 * Created by Richard-DT on 10/08/2016.
 */
public class LocalisationItem {

    private int nbErrors;
    private String localisation;
    private String id;
    private boolean isSelected;
    private String informations;

    public LocalisationItem(String nLocalisation , String nId){
        localisation = nLocalisation;
        id = nId;
    }
    public void setLocalisation(String localisation) {
        this.localisation = localisation;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setInformations(String informations) {
        this.informations = informations;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    public void setNbErrors(int nbErrors) {
        this.nbErrors = nbErrors;
    }

    public boolean getSelected(){return isSelected;}
    public int getNbErrors() {
        return nbErrors;
    }
    public String getLocalisation() {return localisation;}

    public String getId() {return id;}
    public String getInformations() {return informations;}
}
