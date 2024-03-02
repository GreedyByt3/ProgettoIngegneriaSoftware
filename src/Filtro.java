public class Filtro extends HomeController{

    private String sede;
    private String tipo;
    public Filtro(String sede, String tipo){
            this.sede = sede;
            this.tipo = tipo;
    }

    public String getFiltro(){
        return this.sede + ";" + this.tipo;
    }

    public String getSede(){
        return sede;
    }
    public String getTipo(){
        return tipo;
    }

}
