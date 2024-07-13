package br.com.alura.literalura.model;

public enum Idiomas {
    EN("en","Inglês"),
    ES("es","Espanhol"),
    PT("pt","Português"),
    Outra("desconhecida","Desconhecida");

    private final String codigoIdioma;
    private final String nomeIdioma;

    Idiomas(String codigoIdioma, String nomeIdioma) {
        this.codigoIdioma = codigoIdioma;
        this.nomeIdioma = nomeIdioma;
    }

    public String getCodigoIdioma() {
        return codigoIdioma;
    }

    public String getNomeIdioma() {
        return nomeIdioma;
    }

    public static Idiomas setCodigoIdioma(String codigoIdioma) {
        for (Idiomas idiomas : values()) {
            if (idiomas.getCodigoIdioma().equalsIgnoreCase(codigoIdioma)) {
                return idiomas;
            }
        }
        return Outra;
    }

    public static String getNomeIdiomaByCodigoIdioma(String codigoIdioma) {
        return setCodigoIdioma(codigoIdioma).getNomeIdioma();
    }
}