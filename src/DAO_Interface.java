import java.util.Set;

public interface DAO_Interface {
    public Set<Prenotazione> getPrenotazioniOggi(String giorno, Filtro filtro);

    public Set<Prenotazione> getPrenotazioniNonFiltrate();

    public Set<Prenotazione> getPrenotazioniPerCittadino(Cittadino cittadino);

    public Cittadino getCittadino(String mail, String psw);

    public Cittadino getCittadinoByCF(String cf);

    public boolean checkPerson(Cittadino cittadino);

    public void addRegistrazione(String mail, String password, String cd_fiscale);

    public void addPrenotazione(String sede, String giorno, String orario, String tipo) throws Exception;

    public boolean checkLogin(String mail, String password) throws Exception;

    public boolean confirmPrenotazioneCittadino(Cittadino cittadino, Prenotazione temp) throws Exception;
    public boolean checkPrenotazionePresente(Prenotazione temp) throws Exception;
}
