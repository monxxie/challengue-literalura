package br.com.alura.literalura.main;

import br.com.alura.literalura.model.*;
import br.com.alura.literalura.repository.AutorRepository;
import br.com.alura.literalura.repository.LivroRepository;
import br.com.alura.literalura.service.ConsumoApi;
//import br.com.alura.literalura.service.ConverteDados;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Comparator;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

public class Principal {
    private final Scanner leitura = new Scanner(System.in);
    private final LivroRepository livroRepository;
    private final AutorRepository autorRepository;
    private ConsumoApi consumo = new ConsumoApi();
//    private ConverteDados conversor = new ConverteDados();
    private String enderecoBuscaLivro = "https://gutendex.com/books/?search=";
    private final ObjectMapper objectMapper = new ObjectMapper();
    public Principal(LivroRepository livroRepository, AutorRepository autorRepository) {
        this.livroRepository = livroRepository;
        this.autorRepository = autorRepository;
    }

    public void executaMenu() throws JsonProcessingException {
        var opcao = -1;
        while (opcao != 0) {
            var menu = """
                    **************************************************
                    *           Bem vindo ao LiterAlura              *
                    **************************************************
                    |                                                |
                    | 1 - Buscar Livro pelo titulo                   |
                    | 2 - Listar livros registrados                  |
                    | 3 - Listar autores registrados                 |
                    | 4 - Listar autores vivos em um determinado ano |
                    | 5 - Listar livros em um determinado idioma     |
                    | 0 - Sair                                       |
                    __________________________________________________
                    """;
            System.out.println(menu);
            opcao = leitura.nextInt();
            leitura.nextLine();
            switch (opcao) {
                case 1:
                    buscarLivro();
                    break;
                case 2:
                    listarLivros();
                    break;
                case 3:
                    listarAutores();
                    break;
                case 4:
                    listarAutoresVivos();
                    break;
                case 5:
                    listarLivroIdioma();
                    break;
                case 0:
                    System.out.println("Saindo...");
                    break;
                default:
                    System.out.println("Opção inválida");
            }
        }
    }

    private void listarLivroIdioma() {
        List<Idiomas> idiomas = livroRepository.listarIdiomas();
        if (idiomas.isEmpty()) {
            System.out.println("Sua lista está vazia");
        } else {
            System.out.println("Lista de idiomas: ");
            System.out.println();
            idiomas.forEach(idioma -> System.out.println("  " + idioma.getCodigoIdioma().toUpperCase() + "   - " + idioma.getNomeIdioma()));
            System.out.println();
            System.out.print("Código do idioma: ");
            while (true) {
                try {
                    String idiomaEscolhido = leitura.nextLine();
                    List<Livro> livros = livroRepository.findByIdiomas(Idiomas.valueOf(idiomaEscolhido.toUpperCase()));
                    System.out.println();
                    System.out.println("Lista de livros em " + Idiomas.getNomeIdiomaByCodigoIdioma(idiomaEscolhido) + ":");
                    System.out.println();
                    livros.forEach(livro -> System.out.println(" Livro: " + livro.getTitulo()));
                    System.out.println();
                    break;
                } catch (IllegalArgumentException e) {
                    System.out.println();
                    System.out.println("Código inválido");
                    System.out.println();
                    System.out.print("Código idioma: ");
                }
            }
        }
    }

    private void listarAutoresVivos() {
        if (autorRepository.findAll().isEmpty()){
            System.out.println("Sua Lista está vazia!");
        } else {
            System.out.println("Buscar por ano: ");
            System.out.println();
            System.out.print("Ano: ");
            int ano = leitura.nextInt();
            leitura.nextLine();
            System.out.println();
            List<Autor> autores = autorRepository.AchaAutoresVivosNumAno(ano);
            if (autores.isEmpty()) {
                System.out.println("Nenhum autor vivo encontrado");
            } else {
                System.out.println("Lista de autores vivos no ano escolhido:");
                System.out.println();
                autores.stream()
                        .map(autor -> {
                            autor.setNome(autor.NomePadrao(autor.getNome()));
                            return autor;
                        })
                        .sorted(Comparator.comparing(Autor::getAnoNascimento))
                        .forEach(autor -> {
                            System.out.println("Nascimento: " + autor.getAnoNascimento() + " | Morte: " + autor.getAnoMorte() + " | Autor: " + autor.getNome());
                        });
            }
        }
    }

    private void listarAutores() {
        List<Autor> autores = autorRepository.findAll();
        if (autores.isEmpty()) {
            System.out.println("Sua lista está vazia");
        } else {
            System.out.println("Lista de autores: ");
            System.out.println();
            autores.stream()
                    .map(autor -> "Autor: " + autor.NomePadrao(autor.getNome()))
                    .sorted(String::compareTo)
                    .forEach(System.out::println);
        }
    }

    private void listarLivros() {
        List<Livro> livros = livroRepository.findAll();
        if (livros.isEmpty()) {
            System.out.println("Sua lista está vazia");
        } else {
            System.out.println("Lista de livros: ");
            livros.stream()
                    .sorted(Comparator.comparing(Livro::getTitulo))
                    .forEach(livro -> {
                        System.out.println();
                        System.out.println("Livro: " + livro.getTitulo());
                        System.out.println("Idioma: " + livro.getIdioma().getNomeIdioma());
                        String autores = livro.getAutores().stream()
                                .map(autor -> autor.NomePadrao(autor.getNome()))
                                .collect(Collectors.joining(", "));
                        System.out.println("Autores" + autores);
                    });
        }
    }

    private void previaBuscaLivro(DadosLivro dadosLivro){
        String titulo = dadosLivro.titulo();
        if (titulo.length() > 25) {
            titulo = titulo.substring(0, 25) + "...";
        }
        String tituloFormatado = String.format("%-28s", titulo);
        String autorFormatado = dadosLivro.autores().stream()
                .findFirst()
                .map(autor -> {
                    String nome = autor.nome();
                    if (nome.length() > 25) {
                        nome = nome.substring(0, 25) + "...";
                    }
                    return String.format("%-28s", nome);
                })
                .orElse(String.format("%-28s", "Desconhecido"));
        String idiomaFormatado = Idiomas.getNomeIdiomaByCodigoIdioma(dadosLivro.idiomas().get(0));
        System.out.printf("%s | %s | %s%n", tituloFormatado, autorFormatado, idiomaFormatado);
    }

    private void buscarLivro() throws JsonProcessingException {
        ConsumoApi consumoApi = new ConsumoApi();
        System.out.print("Buscar por livro: ");
        String nomeLivro = leitura.nextLine().replace(" ", "+");
        String json = consumoApi.obterDados(enderecoBuscaLivro + nomeLivro);
        Dados dados = objectMapper.readValue(json, Dados.class);
        if (dados.quantidadeDeResultadosEncontrados() == 0) {
            System.out.println("Livro não encontrado!");
        } else {
            System.out.println("Livros encontrados: ");
            System.out.println();
            for (DadosLivro dadosLivro : dados.resultados()) {
                Livro livro = livroRepository.findByTitulo(dadosLivro.titulo());
                previaBuscaLivro(dadosLivro);
                if (livro == null) {
                    livro = new Livro(dadosLivro);
                    for (DadosAutor dadosAutor : dadosLivro.autores()) {
                        Autor autor = autorRepository.findByNome(dadosAutor.nome());
                        if (autor == null) {
                            autor = new Autor(dadosAutor);
                            autorRepository.save(autor);
                        }
                        livro.getAutores().add(autor);
                    }
                    livroRepository.save(livro);
                }
            }
            System.out.println();
            System.out.println("Livro salvo no banco de dados!");
        }
    }
}
