package com.example.Fiaraamidy.entites;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@Entity
@Table( name = "annonce")
public class Annonce {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int idAnnonce;


    private int idUtilisateur;

    private LocalDateTime daty;

    @ManyToOne
    @JoinColumn(name = "id_model")
    private Model model;

    @ManyToOne
    @JoinColumn(name = "id_energie")
    private Energie energie;

    @ManyToOne
    @JoinColumn(name = "id_transmission")
    private Transmission transmission;

    private Long kilometrage;

    private int annee;

    private String descri;

    private Long prix;

    private int etat;

    private LocalDateTime dateValidation;

    private LocalDateTime dateVente;

    @Transient
    private String etatString;

    @Transient
    private List<PhotoAnnonce> photos;

    @Transient
    private boolean favoris;


    public String getEtatString()
    {
        if(etat==0){
            this.setEtatString("Non valide");
        }else if(etat==5){
            this.setEtatString("Valide");
        }else if(etat==10) {
            this.setEtatString("Vendu");
        }
        return etatString;
    }


    public Annonce() {
    }


}
