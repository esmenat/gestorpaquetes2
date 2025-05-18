import java.util.*;

public class SistemaGestionPaquetes {
    // Configuración de bodegas
    private final int capacidadBodega;   // capacidad fija por lote (e.g., 100)
    private PilaPaquetes historial;

    // Colas FIFO de despacho
    private ColaPaquetes colaPremium;
    private ColaPaquetes colaRegular;
    private PilaPaquetes bodega;

    public SistemaGestionPaquetes(int capacidadBodega) {

        // Inicializa colas con capacidad total estática
        this.capacidadBodega = capacidadBodega;
        this.historial = new PilaPaquetes(capacidadBodega);
        this.colaPremium = new ColaPaquetes(capacidadBodega);
        this.colaRegular = new ColaPaquetes(capacidadBodega);
        this.bodega = new PilaPaquetes(capacidadBodega);
        
    }

    /** Registra un paquete: apila en bodega y encola según tipo. */
    public void registrarPaquete(Paquete p) {
        
        bodega.Apilar(p);
    
        if (p.esPremium()) colaPremium.Encolar(p);
        else colaRegular.Encolar(p);
    
        try { historial.Apilar(p); }
        catch (IllegalStateException ex) { /* pila llena: se ignora */ }
    }
    

    /** Despacha siguiente paquete: primero FIFO premium, luego regular. */
    public Paquete despacharSiguiente() {
        Paquete despachado;

        if (!colaPremium.Vacia()) {
            despachado = (Paquete) colaPremium.Desencolar();
        } else if (!colaRegular.Vacia()) {
            despachado = (Paquete) colaRegular.Desencolar();
        } else {
            throw new NoSuchElementException("No hay paquetes para despachar.");
        }

        // Copiar bodega actual (de cima a base)
        int n = bodega.getNumElementos();
        Object[] original = bodega.getArreglo();
        Paquete[] temporal = new Paquete[n];
        for (int i = 0; i < n; i++) {
            temporal[i] = (Paquete) original[i];
        }

        // Crear nueva bodega
        PilaPaquetes nuevaBodega = new PilaPaquetes(capacidadBodega);

        // Reconstruir bodega sin el paquete despachado (desde base a cima)
        for (int i = 0; i < n; i++) {
            if (!temporal[i].getId().equals(despachado.getId())) {
                nuevaBodega.Apilar(temporal[i]);
            }
        }

        this.bodega = nuevaBodega;

        return despachado;
    }

    /** Copia estática de cola FIFO sin alterarla. */
    private Paquete[] copiarCola (ColaPaquetes cola) {
        int n = cola.getNumeroElementos();
        Paquete[] copia = new Paquete[n];
        Object[] arr = cola.getArreglo();
        int inicio = cola.getInicio();
        for (int i = 0; i < n; i++) {
            copia[i] = (Paquete) arr[(inicio + i) % cola.getTamano()];
        }
        return copia;
    }

    /** @return arreglo de premium FIFO. */
    public Paquete[] getColaPremium() {
        return copiarCola(colaPremium);
    }

    /** @return arreglo de regulares FIFO. */
    public Paquete[] getColaRegular() {
        return copiarCola(colaRegular);
    }

    /** @return pendientes premium. */
    public int getPendientesPremium() {
        return colaPremium.getNumeroElementos();
    }

    /** @return pendientes regulares. */
    public int getPendientesRegular() {
        return colaRegular.getNumeroElementos();
    }

    public boolean actualizarTipo(String id, boolean nuevoPremium) {
        boolean encontrado = false;
    
        // 1) Extraer premium a un array temporal
        int nPrem = colaPremium.getNumeroElementos();
        Paquete[] tempPrem = new Paquete[nPrem];
        for (int i = 0; i < nPrem; i++) {
            tempPrem[i] = (Paquete) colaPremium.Desencolar();
        }
    
        // 2) Extraer normal a un array temporal
        int nNorm = colaRegular.getNumeroElementos();
        Paquete[] tempNorm = new Paquete[nNorm];
        for (int i = 0; i < nNorm; i++) {
            tempNorm[i] = (Paquete) colaRegular.Desencolar();
        }
    
        // 3) Reconstruir premium: si coincide, cambia tipo; luego reencola
        for (Paquete p : tempPrem) {
            if (p.getId().equals(id)) {
                p.setEsPremium(nuevoPremium);
                encontrado = true;
            }
            // reencolar según su (posible) tipo
            if (p.esPremium()) colaPremium.Encolar(p);
            else                colaRegular.Encolar(p);
        }
    
        // 4) Reconstruir normal
        for (Paquete p : tempNorm) {
            if (p.getId().equals(id)) {
                p.setEsPremium(nuevoPremium);
                encontrado = true;
            }
            if (p.esPremium()) colaPremium.Encolar(p);
            else                colaRegular.Encolar(p);
        }
    
        return encontrado;
    }

    public Paquete[] getHistoricoPila() {
        int n = historial.getNumElementos();
        Paquete[] arr = new Paquete[n];
        Object[] interno = historial.getArreglo();  // tu PilaPaquetes expone el array interno
        // extraer de cima a base
        for (int i = 0; i < n; i++) {
            arr[i] = (Paquete) interno[n - 1 - i];
        }
        return arr;
    }

   

    public Paquete[] getBodega() {
        int n = bodega.getNumElementos();
        Paquete[] arr = new Paquete[n];
        Object[] interno = bodega.getArreglo();
        for (int i = 0; i < n; i++) {
            arr[i] = (Paquete) interno[n - 1 - i]; // de cima a base
        }
        return arr;
    }
}
