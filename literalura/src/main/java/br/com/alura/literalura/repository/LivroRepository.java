package br.com.alura.literalura.repository;

import br.com.alura.literalura.model.Idiomas;
import br.com.alura.literalura.model.Livro;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface LivroRepository extends JpaRepository<Livro, Long> {

    Livro findByTitulo(String titulo);

    @Query("SELECT DISTINCT idiomas FROM Livro")
    List<Idiomas> listarIdiomas();

    List<Livro> findByIdiomas(Idiomas idiomas);
}
