package edu.esprit.services;

import edu.esprit.entities.Etablissement;


public interface MyListener {
    public void onClickListener(Etablissement etablissement);

    void onDeleteListener(Etablissement etablissement);
}
