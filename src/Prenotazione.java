public class Prenotazione implements Comparable<Prenotazione>{

    private int id_Prenotazione;
    private Cittadino cittadino;
    private String tipoPrenotazione;
    private Date giorno;
    private String orario;
    private String sede;
    public Prenotazione(String tipoPrenotazione,Date giorno,String orario,String sede, int id_Prenotazione){
        this.id_Prenotazione = id_Prenotazione;
        this.tipoPrenotazione = tipoPrenotazione;
        this.giorno = giorno;
        this.orario = orario;
        this.sede = sede;
    }

    public Prenotazione(String tipoPrenotazione,Date giorno,String orario,String sede){
        this.tipoPrenotazione = tipoPrenotazione;
        this.giorno = giorno;
        this.orario = orario;
        this.sede = sede;
    }

    public Prenotazione(Cittadino citt, String date, String sede){
        this.cittadino = citt;
        this.giorno = new Date(date);
        this.orario = sede;

    }

    public Prenotazione(Cittadino citt, String tipoPrenotazione,Date giorno,String orario,String sede, int id_Prenotazione){
        this(tipoPrenotazione, giorno, orario, sede, id_Prenotazione);
        this.cittadino = citt;
    }

    public Date getGiorno(){
        return giorno;
    }


    public void setCittadino(Cittadino cit){
        this.cittadino = cit;
    }

    public Cittadino getCittadino(){
        return cittadino;
    }

    public String toString(){
        return "Id: " + id_Prenotazione +"\nTipo: " + tipoPrenotazione + "\nGiorno: " + giorno + "\nOrario: " + orario + "\nSede: " + sede;
    }


    public int compareTo(Prenotazione o) {
        int dif = this.giorno.compareTo(o.giorno);
        if(dif == 0){
            String[] split = this.orario.split(":");
            int hourThis = Integer.parseInt(split[0]);
            int minThis = Integer.parseInt(split[1]);
            split = o.orario.split(":");
            int hourOther = Integer.parseInt(split[0]);
            int minOther = Integer.parseInt(split[1]);
            dif = hourThis - hourOther;
            if(dif == 0){
                dif = minThis - minOther;
                if(dif == 0){
                    return 0;
                }
                return dif;
            }
            return dif;
        }
        return dif;
    }



    public int getId(){
        return id_Prenotazione;
    }

    public String getTipoPrenotazione(){
        return tipoPrenotazione;
    }

    public String getDate(){
        return giorno.toString();
    }

    public String getOrario(){
        return orario;
    }

    public String getSede(){
        return sede;
    }
}
