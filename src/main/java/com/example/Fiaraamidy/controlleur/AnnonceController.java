package com.example.Fiaraamidy.controlleur;

import com.example.Fiaraamidy.config.Data;
import com.example.Fiaraamidy.entites.Annonce;
import com.example.Fiaraamidy.entites.PhotoAnnonce;
import com.example.Fiaraamidy.entites.Transmission;
import com.example.Fiaraamidy.model.UploadPhoto;
import com.example.Fiaraamidy.recherche.Recherche;
import com.example.Fiaraamidy.service.AnnonceService;
import com.example.Fiaraamidy.service.PhotoAnnonceService;
import com.google.firebase.database.util.JsonMapper;
import com.google.gson.Gson;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;


import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping(path = "annonce")
public class AnnonceController {
    private AnnonceService annonceService;
    private PhotoAnnonceService photoAnnonceService;
    private Gson gson;

    public AnnonceController(AnnonceService annonceService, PhotoAnnonceService photoAnnonceService,Gson gson) {
        this.annonceService = annonceService;
        this.photoAnnonceService = photoAnnonceService;
        this.gson = gson;
    }

    @GetMapping(path = "getAllPhoto")
    public List<Annonce> all()
    {
        return this.annonceService.getAll();
    }

    @GetMapping(path = "getNonValide")
    public List<Annonce> allNV()
    {
        return this.annonceService.getNonValide();
    }

    @GetMapping(path = "getAll")
    public List<Annonce> alll()
    {
        return this.annonceService.getAllSansPhoto();
    }

    @ResponseStatus(value = HttpStatus.CREATED)
    @PostMapping(path = "insert")
    public String cree(@RequestBody Annonce annonce)
    {
        try{
            LocalDateTime currentDateTime = LocalDateTime.now();
            annonce.setDaty(currentDateTime);
            this.annonceService.create(annonce);
            return "Creation annonce reussie";
        } catch (Exception e){
            return e.getMessage();
        }

    }

    @GetMapping(path = "getAllByUser/{idUser}")
    public List<Annonce> allByUser(@PathVariable int idUser)
    {
        return this.annonceService.getAllByUser(idUser);
    }

    @GetMapping(path = "getByIdAnnonce/{id}")
    public Annonce getByIdAnnonce(@PathVariable int id)
    {
        return this.annonceService.getById(id);
    }

    @GetMapping(path = "recherche")
    public List<Annonce> recherche(@RequestBody Recherche recherche)
    {
        return this.annonceService.searchAnnonces(recherche);
    }

    @GetMapping(path = "recherche/{idma}/{idmo}/{idt}/{ide}/{pmin}/{pmax}/{kmin}/{kmax}/{amin}/{amax}")
    public List<Annonce> recherche(@PathVariable int idma,@PathVariable int idmo,@PathVariable int idt,@PathVariable int ide,@PathVariable Long pmin,@PathVariable Long pmax,@PathVariable Long kmin,@PathVariable Long kmax,@PathVariable int amin,@PathVariable int amax)
    {
        Recherche recherche = new Recherche(idma,idmo,idt,ide,pmin,pmax,kmin,kmax,amin,amax);
        return this.annonceService.searchAnnonces(recherche);
    }

    @GetMapping(path = "All")
    public List<Annonce> nonValide()
    {
        return this.annonceService.getByEtat(0);
    }

    @GetMapping(path = "valide")
    public List<Annonce> Valide()
    {
        return this.annonceService.getByEtat(5);
    }

    @GetMapping(path = "valideNotUser/{idu}")
    public List<Annonce> Validewithlog(@PathVariable int idu)
    {
        return this.annonceService.getAnnonceByNotUser(idu);
    }

    @GetMapping(path = "nonValide")
    public List<Annonce> nValide()
    {
        return this.annonceService.getByEtat(0);
    }

    @GetMapping(path = "valide/{idU}")
    public List<Annonce> Valide(@PathVariable int idU)
    {

            return this.annonceService.getAllByEtatByUser(idU);

    }


    @PutMapping(path = "valide/{id}")
    public String validation(@PathVariable int id)
    {
        try{
            this.annonceService.valide(id);
            return "Validation reussie";
        } catch (Exception e){
            return e.getMessage();
        }
    }

    @PutMapping(path = "vendu/{id}")
    public String vendu(@PathVariable int id)
    {
        try{
            this.annonceService.vendu(id);
            return "Status change";
        } catch (RuntimeException e) {
            return e.getMessage();
        }
    }


    @GetMapping(path = "photo/{id}")
    public List<PhotoAnnonce> photo(@PathVariable int id){
        try{
            return this.photoAnnonceService.getPhotoByIdAnnonce(id);
        }catch (Exception e){
            e.getMessage();
        }
        return null;
    }

    @GetMapping(path = "photo/getAll")
    public List<PhotoAnnonce> photo(){
        return this.photoAnnonceService.getAllPhoto();
    }

    @PostMapping(path = "insertPhoto")
    @ResponseStatus(value = HttpStatus.CREATED)
    public String insertPhoto(@RequestParam("idAnnonce") String idAnnonce, @RequestPart("files") List<MultipartFile> files){

        try{

            for (MultipartFile file : files){
                Path tempPath = Files.createTempFile("temp", ".jpg");

                // Copier le contenu du MultipartFile vers le chemin temporaire
                Files.copy(file.getInputStream(), tempPath, StandardCopyOption.REPLACE_EXISTING);

                // Vous pouvez maintenant utiliser tempPath comme chemin absolu du fichier
                String absolutePath = tempPath.toAbsolutePath().toString();

                File uploadPhoto = new File(absolutePath);
                int id = Integer.parseInt(idAnnonce);
                this.photoAnnonceService.ajouterImage(id,uploadPhoto);
            }

            return "mety";
        }catch (Exception e){
            return e.getMessage();
        }
    }

    @PostMapping(path = "insertAnnonce")
    @ResponseStatus(value = HttpStatus.CREATED)
    @Transactional(rollbackFor = Exception.class)
    public ResponseEntity<Data> insertAnnonceP(@RequestPart("files") List<MultipartFile> files,@RequestParam("annonce") String s){
        try{
            Annonce annonce = gson.fromJson(s,Annonce.class);
            LocalDateTime currentDateTime = LocalDateTime.now();
            annonce.setDaty(currentDateTime);
            System.out.println(annonce.getModel().getIdModel());
            this.annonceService.create(annonce);

            for (MultipartFile file : files){
                Path tempPath = Files.createTempFile("temp", ".jpg");

                // Copier le contenu du MultipartFile vers le chemin temporaire
                Files.copy(file.getInputStream(), tempPath, StandardCopyOption.REPLACE_EXISTING);

                // Vous pouvez maintenant utiliser tempPath comme chemin absolu du fichier
                String absolutePath = tempPath.toAbsolutePath().toString();

                File uploadPhoto = new File(absolutePath);
                int id = annonce.getIdAnnonce();
                this.photoAnnonceService.ajouterImage(id,uploadPhoto);
            }

            return ResponseEntity.ok(new Data(null,null,"Annonce creer"));
        }catch (Exception e){
            return ResponseEntity.ok(new Data(null,null,e.getMessage()));
//            throw new RuntimeException(e.getMessage());
        }
    }


    @PostMapping(path = "insertAnnonceMobile")
    @ResponseStatus(value = HttpStatus.CREATED)
    public ResponseEntity<Data> insertAnnonceM(@RequestParam("dataUrl") List<String> dataUrls,@RequestParam("annonce") String s){
        try{
            Annonce annonce = gson.fromJson(s,Annonce.class);
            LocalDateTime currentDateTime = LocalDateTime.now();
            annonce.setDaty(currentDateTime);
            this.annonceService.create(annonce);

            for (String dataUrl : dataUrls){
                this.photoAnnonceService.insertImage(annonce.getIdAnnonce(),dataUrl);
            }

            return ResponseEntity.ok(new Data(null,null,"Annonce creer"));
        }catch (Exception e){
            return ResponseEntity.ok(new Data(null,null,e.getMessage()));
//            throw new RuntimeException(e.getMessage());
        }
    }

}

