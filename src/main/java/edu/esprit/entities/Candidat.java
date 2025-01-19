package edu.esprit.entities;

public class Candidat extends Utilisateur{
    private  int ROLE=2;

    public  int getROLE() {
        return ROLE;
    }

    public  void setROLE(int ROLE) {
        this.ROLE = ROLE;
    }
    public Candidat() {
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
        return "Candidat{" +

                "cin=" + this.getCin() +
                ", nom='" + this.getNom() + '\'' +
                ", prenom='" + this.getPrenom() + '\'' +
                ", adresse='" + this.getAdresse() + '\'' +

                ", role='" + this.getROLE() + '\'' +
                '}'+'\n';
    }

    /*public Candidat(int id_utilisateur,int cin,String nom ,String prenom,String adresse, String mdp)
    {
        super(id_utilisateur, cin, nom, prenom, adresse, mdp);
        this.ROLE=1;
    }*/
    public Candidat(int cin,String nom ,String prenom,String adresse, String mdp)
    {
        super( cin, nom, prenom, adresse, mdp);
        this.ROLE=2;
    }
    public Candidat(int cin,String nom ,String prenom,String adresse, String mdp,String image)
    {
        super( cin, nom, prenom, adresse, mdp,image);
        this.ROLE=2;
    }



}
