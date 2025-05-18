public class Paquete {
    private String NombreCliente;
    private String Ciudad;
    private int numeroLlegada;
    private String id;
    private boolean esPremium;

    public Paquete(String nombreCliente, String ciudad, int numeroLlegada, boolean esPremium) {
        if (nombreCliente == null || nombreCliente.isEmpty() || ciudad == null || ciudad.isEmpty()) {
            throw new IllegalArgumentException("Nombre y ciudad no pueden estar vacíos.");
        }
        this.NombreCliente = nombreCliente.trim();
        this.Ciudad = ciudad.trim();
        this.numeroLlegada = numeroLlegada;
        this.esPremium = esPremium;

        String inicialCiudad = ciudad.substring(0, 1).toUpperCase();
        String inicialNombre = nombreCliente.substring(0, 1).toUpperCase();
        String numeroFormateado = String.format("%03d", numeroLlegada);
        this.id = inicialCiudad + numeroFormateado + inicialNombre;
    }

    public String getId() { 
        return id; 
    }

    public boolean esPremium() {
         return esPremium; 
    }

    public void setEsPremium(boolean esPremium) {
         this.esPremium = esPremium; 
    }

    public String getCiudad() {
         return Ciudad; 
    }

    public String getNombreCliente() {
         return NombreCliente; 
    }

    public int getNumeroLlegada() {
        return numeroLlegada;
    }

    @Override
    public String toString() {
        return "ID: " + id + "  Ciudad: "+Ciudad+" Cliente: "+NombreCliente+" Tipo"+" (" + (esPremium ? "Premium" : "Normal") + ")";
    }
}