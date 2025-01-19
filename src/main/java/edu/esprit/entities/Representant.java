package edu.esprit.entities;

public class Representant extends Utilisateur{
    private int ROLE=1;

    public  int getROLE() {
        return ROLE;
    }


    public Representant() {
    }

    @Override
    public boolean equals(Object o) {
        return super.equals(o);
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }
    public String toString() {
        return "Representant{" +

                "cin=" + this.getCin() +
                ", nom='" + this.getNom() + '\'' +
                ", prenom='" + this.getPrenom() + '\'' +
                ", adresse='" + this.getAdresse() + '\'' +

                ", role='" + this.getROLE() + '\'' +
                '}'+'\n';
    }

   /* public Representant(int id_utilisateur, int cin, String nom , String prenom, String adresse, String mdp)
    {
        super(id_utilisateur, cin, nom, prenom, adresse, mdp);
        this.ROLE=0;
    }*/
    public Representant(int cin, String nom , String prenom, String adresse, String mdp)
    {
        super(cin,nom,prenom, adresse, mdp);
        this.ROLE=1;
    }
    public Representant(int cin, String nom , String prenom, String adresse, String mdp,String image)
    {
        super(cin,nom,prenom, adresse, mdp,image);
        this.ROLE=1;
    }




}
