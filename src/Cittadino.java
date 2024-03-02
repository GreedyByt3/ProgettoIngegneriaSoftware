
public class Cittadino implements Comparable<Cittadino>{
    private String cd_Fiscale, numTesseraSanitaria;
    private String nome, cognome;
    private String luogoNascita;
    private Date dataNascita;
    private String category;
    private String mail, password;

    public Cittadino(String cd_Fisc, String numTessera,
                     String nome, String cognome,
                     String luogoNascita,
                     Date dataN){

        this.cd_Fiscale = cd_Fisc;
        this.numTesseraSanitaria = numTessera;
        this.nome = nome;
        this.cognome = cognome;
        this.luogoNascita = luogoNascita;
        this.dataNascita = dataN;
    }

    public String get_Tessera_Sanitaria(int option) {
        return (option == 1) ? cd_Fiscale:numTesseraSanitaria;
    }

    public String getInfo(int option){
        return (option == 1) ? nome:cognome;
    }

    public String getNascita(int option){
        return (option == 1) ? dataNascita.toString():luogoNascita;
    }

    public String getCategory(){
        return category;
    }

    @Override
    public int compareTo(Cittadino o) {
        return cd_Fiscale.compareTo(o.cd_Fiscale);
    }
    public String toString(){
        return "Nome: " + nome + ";\nCognome: " + cognome + ";\nCodice Fiscale: " + cd_Fiscale + ";\nNumero Tessera: " + numTesseraSanitaria + ";\nLuogo di Nascita: " + luogoNascita + ";\nData di Nascita: " + dataNascita.toString();
    }
}
