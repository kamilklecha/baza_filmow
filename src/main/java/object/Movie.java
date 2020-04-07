package object;

public class Movie {

    protected String Tytul;
    protected String Komentarz;
    protected Integer Ocena; // 0 - 10 trzeba jeszcze dorobić ten warunek
//    protected Integer Id;

//    public Integer getId() {
//        return Id;
//    }

//    public void setId(Integer id) {
//        Id = id;
//    }

    public String getTytul() {
        return Tytul;
    }

    public void setTytul(String tytul) {
        Tytul = tytul;
    }

    public String getKomentarz() {
        return Komentarz;
    }

    public void setKomentarz(String komentarz) {
        Komentarz = komentarz;
    }

    public Integer getOcena() {
        return Ocena;
    }

    public void setOcena(Integer ocena) {
        Ocena = ocena;
    }

    public static Movie create(String tytul, Integer ocena, String komentarz) {
        Movie movie = new Movie();
        movie.setTytul(tytul);
        movie.setOcena(ocena);
        movie.setKomentarz(komentarz);
//        movie.setId(id);
        return movie;
    }

}
