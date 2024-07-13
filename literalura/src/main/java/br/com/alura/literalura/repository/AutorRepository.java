package br.com.alura.literalura.repository;

import br.com.alura.literalura.model.Autor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface AutorRepository extends JpaRepository<Autor, Long>{

    @Query("SELECT a FROM Autor a WHERE a.anoNascimento <= :ano AND a.anoMorte>:ano")
    List<Autor> AchaAutoresVivosNumAno(Integer ano);

    Autor findByNome(String nome);
}