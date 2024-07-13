package br.com.alura.literalura.service;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class ConsumoApi {
    public String obterDados(String endereco) {
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(endereco))
                .build();
        HttpResponse<String> response = null;
        try {
            response = client
                    .send(request, HttpResponse.BodyHandlers.ofString());
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        String json = response.body();
        return json;
    }
}
//public class ConsumoApi {
//    private final String endereco = "https://gutendex.com/books/";
//    private final String search = "?search=%20";
//    public String obterDados(String nome) {
//
//        HttpClient client = HttpClient.newHttpClient();
//        HttpRequest request = HttpRequest.newBuilder()
//                .uri(URI.create(endereco + search + nome))
//                .build();
//        HttpResponse<String> response = null;
//        try {
//            response = client
//                    .send(request, HttpResponse.BodyHandlers.ofString());
//        } catch (IOException e) {
//            throw new RuntimeException(e);
//        } catch (InterruptedException e) {
//            throw new RuntimeException(e);
//        }
//        String json = response.body();
//        return json;
//    }
//
//    public String obterDadosLivro(String id) {
//        HttpClient client2 = HttpClient.newHttpClient();
//        HttpRequest request2 = HttpRequest.newBuilder()
//                .uri(URI.create(endereco + id + "/"))
//                .build();
//        HttpResponse<String> response = null;
//        try {
//            response = client2
//                    .send(request2, HttpResponse.BodyHandlers.ofString());
//        } catch (IOException e) {
//            throw new RuntimeException(e);
//        } catch (InterruptedException e) {
//            throw new RuntimeException(e);
//        }
//        String json = response.body();
//        return json;
//    }
//}

