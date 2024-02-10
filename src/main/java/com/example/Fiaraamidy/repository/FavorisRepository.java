package com.example.Fiaraamidy.repository;

import com.example.Fiaraamidy.entites.Favoris;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

public interface FavorisRepository extends JpaRepository<Favoris,Integer> {
    List<Favoris> findByIdUtilisateur(int id);
    Optional<Favoris> findByIdUtilisateurAndAndAnnonceIdAnnonce(int idu,int ida);
    @Transactional
    void deleteByIdUtilisateurAndAndAnnonceIdAnnonce(int idu,int ida);

}
