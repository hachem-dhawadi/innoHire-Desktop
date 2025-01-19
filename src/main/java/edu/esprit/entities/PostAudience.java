package edu.esprit.entities;

/**
 * Created by Mahmoud Hamwi on 17-Feb-21.
 */
public enum PostAudience {
    PUBLIC(0,"PUBLIC","/img/ic_public.png"),
    FRIENDS(1,"FRIENDS","/img/ic_friend.png");

    private int id;
    private String nom;
    private String imgSrc;

    PostAudience(int id, String nom, String imgSrc) {
        this.id = id;
        this.nom = nom;
        this.imgSrc = imgSrc;
    }

    public int getId() {
        return id;
    }

    public String getNom() {
        return nom;
    }

    public String getImgSrc() {
        return imgSrc;
    }
}
