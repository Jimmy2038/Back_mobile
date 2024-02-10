package com.example.Fiaraamidy.service;

import com.example.Fiaraamidy.entites.Favoris;
import com.example.Fiaraamidy.entites.PhotoAnnonce;
import com.example.Fiaraamidy.repository.FavorisRepository;
import com.example.Fiaraamidy.repository.MarqueRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@AllArgsConstructor
@Service
public class FavorisService {
    private FavorisRepository favorisRepository;
    private UtilisateurService utilisateurService;

    private PhotoAnnonceService photoAnnonceService;

    public void insert(Favoris favoris){
        utilisateurService.getById(favoris.getIdUtilisateur());
        this.favorisRepository.save(favoris);
    }

    public List<Favoris> getFavoris(int id){
        List<Favoris> favoris =  this.favorisRepository.findByIdUtilisateur(id);
        for(int i=0;i<favoris.size();i++){
            List<PhotoAnnonce> photos =  this.photoAnnonceService.getPhotoByIdAnnonce(favoris.get(i).getAnnonce().getIdAnnonce());
            favoris.get(i).getAnnonce().setPhotos(photos);
        }
        return favoris;
    }

    public void delete(int idu,int ida){
        this.favorisRepository.deleteByIdUtilisateurAndAndAnnonceIdAnnonce(idu,ida);
    }

    public boolean check(int idu,int ida){
        Optional<Favoris> favoris = this.favorisRepository.findByIdUtilisateurAndAndAnnonceIdAnnonce(idu,ida);
        if (favoris.isPresent()){
            return true;
        }else{
            return false;
        }
    }
}
