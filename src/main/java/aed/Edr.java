package aed;
import java.util.Arrays;
// La complejidad de las operaciones se expresa en función de:
// E: cantidad total de estudiantes
// R: cantidad total de respuestas del examen
// k: parámetro para consultarDarkWeb
// S: cantidad de sospechosos
// V: cantidad de notas válidas
public class Edr {
    private class Estudiante implements Comparable<Estudiante> {
        int id; // O(1)
        int[] respuestas; // O(1)
        double puntaje; // O(1)
        boolean entregado; // O(1)
        int respuestasCorrectas; // O(1)
        boolean esCopion; // O(1)
        Handle<Estudiante> heap_handle; // O(1)

        Estudiante(int id, int R) { // O(R)
            this.id = id; // O(1)
            this.respuestas = new int[R]; // O(R)
            for (int i = 0; i < R; i++) { // Bucle O(R)
                this.respuestas[i] = -1; // O(1)
            }
            this.puntaje = 0.0; // O(1)
            this.entregado = false; // O(1)
            this.respuestasCorrectas = 0; // O(1)
            this.esCopion = false; // O(1)
            this.heap_handle = null; // O(1)
        }

        @Override
        public int compareTo(Estudiante otro) { // O(1)
            if (this.puntaje != otro.puntaje) { // O(1)
                return Double.compare(this.puntaje, otro.puntaje); // O(1)
            }
            return Integer.compare(this.id, otro.id); // O(1)
        }
    }

    private Estudiante[] estudiantes; // O(1)
    private Heap<Estudiante> puntajesDeEstudiantes; // O(1)
    private int[] ExamenCanonico; // O(1)
    private int LadoAula; // O(1)
    private boolean[] _esCopion = null; // O(1)

    // Complejidad: O(E*R)
    public Edr(int LadoAula, int Cant_estudiantes, int[] ExamenCanonico) { // O(E*R)
        this.LadoAula = LadoAula; // O(1)
        this.ExamenCanonico = new int[ExamenCanonico.length]; // O(R)
        System.arraycopy(ExamenCanonico, 0, this.ExamenCanonico, 0, ExamenCanonico.length); // O(R)
        this.estudiantes = new Estudiante[Cant_estudiantes]; // O(E)
        this.puntajesDeEstudiantes = new Heap<>(); // O(1)
        int R = this.ExamenCanonico.length; // O(1)
        for (int i = 0; i < Cant_estudiantes; i++) { // Bucle O(E)
            Estudiante est = new Estudiante(i, R); // O(R)
            est.heap_handle = puntajesDeEstudiantes.insertar(est); // O(log E)
            estudiantes[i] = est; // O(1)
        }
    }

    // Complejidad: O(E)
    public double[] notas() { // O(E)
        double[] res = new double[estudiantes.length]; // O(E)
        for (int i = 0; i < estudiantes.length; i++) { // Bucle O(E)
            res[i] = estudiantes[i].puntaje; // O(1)
        }
        return res; // O(1)
    }

    // Complejidad: O(log E)
    public void resolver(int estudiante, int NroEjercicio, int res) { // O(log E)
        Estudiante est = estudiantes[estudiante]; // O(1)
        int respuesta_previa = est.respuestas[NroEjercicio]; // O(1)
        est.respuestas[NroEjercicio] = res;

        boolean eraCorrecta = (respuesta_previa != -1 && respuesta_previa == ExamenCanonico[NroEjercicio]); // O(1)
        boolean esCorrecta = (res != -1 && res == ExamenCanonico[NroEjercicio]); // O(1)

        if (eraCorrecta && !esCorrecta) { // O(1)
            est.respuestasCorrectas--; // O(1)
        } else if (!eraCorrecta && esCorrecta) { // O(1)
            est.respuestasCorrectas++; // O(1)
        }

        double nuevo_puntaje = ((double) est.respuestasCorrectas / ExamenCanonico.length) * 100.0; // O(1)

        if (nuevo_puntaje != est.puntaje) { // O(1)
            est.puntaje = nuevo_puntaje; // O(1)
            if (est.heap_handle != null) { // O(1)
                puntajesDeEstudiantes.actualizarPrioridad(est.heap_handle); // O(log E)
            }
        }
    }

    // Complejidad: O(R + log E)
    public void copiarse(int estudiante_id) { // O(R + log E)
        Estudiante est = estudiantes[estudiante_id]; // O(1)
        
        // 1. Identificar estudiantes cercanos
        int[] vecinos_ids = new int[3]; // O(1)
        int num_vecinos = 0; // O(1)

        // Izquierda de por medio: [estudianteCercano, vacio, mi estudiante]
        if (estudiante_id % LadoAula >= 2) { // O(1)
            vecinos_ids[num_vecinos++] = estudiante_id - 2; // O(1)
        }
        // Derecha de por medio: [mi estudiante, vacio, estudianteCercano]
        if (estudiante_id % LadoAula < LadoAula - 2 && (estudiante_id + 2) < estudiantes.length) { // O(1)
            vecinos_ids[num_vecinos++] = estudiante_id + 2; // O(1)
        }
        // Arriba (inmediatamente arriba)
        if (estudiante_id >= LadoAula) { // O(1)
            vecinos_ids[num_vecinos++] = estudiante_id - LadoAula; // O(1)
        }

        int mejor_vecino_id = -1; // O(1)
        int max_respuestas_nuevas = -1; // O(1)

        // 2. Encontrar el mejor vecino (3 iteraciones max)
        for (int i = 0; i < num_vecinos; i++) { // Bucle O(1) (max 3 veces)
            int vecino_id = vecinos_ids[i]; // O(1)
            Estudiante potencial_vecino = estudiantes[vecino_id]; // O(1)
            
            int respuestas_nuevas = 0; // O(1)
            for (int j = 0; j < ExamenCanonico.length; j++) { // Bucle O(R)
                if (potencial_vecino.respuestas[j] != -1 && est.respuestas[j] == -1) { // O(1)
                    respuestas_nuevas++; // O(1)
                }
            }

            if (respuestas_nuevas > max_respuestas_nuevas) { // O(1)
                max_respuestas_nuevas = respuestas_nuevas; // O(1)
                mejor_vecino_id = vecino_id; // O(1)
            } else if (respuestas_nuevas == max_respuestas_nuevas) { // O(1)
                if (vecino_id > mejor_vecino_id) { // O(1)
                    mejor_vecino_id = vecino_id; // O(1)
                }
            }
        }

        if (mejor_vecino_id == -1 || max_respuestas_nuevas == 0) return; // O(1)

        est.esCopion = true;

        // 3. Copiar la primera respuesta y recalcular puntaje
        Estudiante vecino = estudiantes[mejor_vecino_id]; // O(1)
        for (int j = 0; j < ExamenCanonico.length; j++) { // Bucle O(R)
            if (vecino.respuestas[j] != -1 && est.respuestas[j] == -1) { // O(1)
                
                int respuesta_a_copiar = vecino.respuestas[j]; // O(1)
                
                boolean esCorrecta = (respuesta_a_copiar == ExamenCanonico[j]); // O(1)

                if (esCorrecta) { // O(1)
                    est.respuestasCorrectas++; // O(1)
                }

                est.respuestas[j] = respuesta_a_copiar; // O(1)
                double nuevo_puntaje = ((double) est.respuestasCorrectas / ExamenCanonico.length) * 100.0; // O(1)

                if (nuevo_puntaje != est.puntaje) { // O(1)
                    est.puntaje = nuevo_puntaje; // O(1)
                    if (est.heap_handle != null) { // O(1)
                        puntajesDeEstudiantes.actualizarPrioridad(est.heap_handle); // O(log E)
                    }
                }
                
                return; // O(1)
            }
        }
    }

    // Complejidad: O(log E)
    public void entregar(int estudiante) { // O(log E)
        Estudiante est = estudiantes[estudiante]; // O(1)
        if (!est.entregado) { // O(1)
            est.entregado = true; // O(1)
            if (est.heap_handle != null) { // O(1)
                est.heap_handle.eliminar(); // O(log E)
                est.heap_handle = null; // O(1)
            }
        }
    }

    // Complejidad: O(k * (R + log E))
    public void consultarDarkWeb(int k, int[] examenDW) { // O(k * (R + log E))
        ListaEnlazada<Estudiante> peores = new ListaEnlazada<>(); // O(1)
        int count = (k < puntajesDeEstudiantes.cardinal()) ? k : puntajesDeEstudiantes.cardinal(); // O(1)
        for (int i = 0; i < count; i++) { // Bucle O(k)
            peores.agregarAtras(puntajesDeEstudiantes.extraerMinimo()); // O(log E)
        }

        ListaEnlazada.ListaIterador iter = peores.iterador(); // O(1)
        while (iter.haySiguiente()) { // Bucle O(k)
            Estudiante est = (Estudiante) iter.siguiente(); // O(1)
            est.respuestas = examenDW.clone(); // O(R)
            
            est.respuestasCorrectas = 0; // O(1)
            for(int i=0; i < est.respuestas.length; i++){ // Bucle O(R)
                if(est.respuestas[i] != -1 && est.respuestas[i] == ExamenCanonico[i]){ // O(1)
                    est.respuestasCorrectas++; // O(1)
                }
            }
            est.puntaje = ((double) est.respuestasCorrectas / ExamenCanonico.length) * 100.0; // O(1)
            est.heap_handle = puntajesDeEstudiantes.insertar(est); // O(log E)
        }
    }

    // Complejidad: O(E*R) asumiendo que el valor de las respuestas es acotado.
        public int[] chequearCopias() {
            boolean alguienSeCopio = false;
            for (int i = 0; i < estudiantes.length; i++) {
                if (estudiantes[i].esCopion) {
                    alguienSeCopio = true;
                    break;
                }
            }
    
            if (alguienSeCopio) {
                int count = 0;
                for (int i = 0; i < estudiantes.length; i++) {
                    if (estudiantes[i].esCopion) {
                        count++;
                    }
                }
                int[] res = new int[count];
                int index = 0;
                for (int i = 0; i < estudiantes.length; i++) {
                    if (estudiantes[i].esCopion) {
                        res[index++] = i;
                    }
                }
                this._esCopion = new boolean[estudiantes.length];
                for(int i=0; i<res.length; i++){
                    this._esCopion[res[i]] = true;
                }
                return res;
    
            } else {
                // Modo Estadístico
                int E = estudiantes.length;
                if (E <= 1) {
                    this._esCopion = new boolean[E];
                    return new int[0];
                }
    
                int maxRespuesta = 0;
                for (int i = 0; i < E; i++) {
                    for (int j = 0; j < ExamenCanonico.length; j++) {
                        if (estudiantes[i].respuestas[j] > maxRespuesta) {
                            maxRespuesta = estudiantes[i].respuestas[j];
                        }
                    }
                }
    
                int umbral = (E - 1 + 3) / 4;
                int R = ExamenCanonico.length;
    
                System.out.println("E: " + E);
                System.out.println("umbral: " + umbral);
                System.out.println("maxRespuesta: " + maxRespuesta);

                boolean[][] esRespuestaSospechosa = new boolean[E][R];
                for (int j = 0; j < R; j++) {
                    int[] counts = new int[maxRespuesta + 1];
                    for (int i = 0; i < E; i++) {
                        int respuesta = estudiantes[i].respuestas[j];
                        if (respuesta != -1) {
                            counts[respuesta]++;
                        }
                    }
                    System.out.println("Pregunta " + j + ", counts: " + Arrays.toString(counts));
                    for (int i = 0; i < E; i++) {
                        int respuesta = estudiantes[i].respuestas[j];
                        if (respuesta != -1) {
                            int count = counts[respuesta];
                            if ((count - 1) >= umbral) {
                                esRespuestaSospechosa[i][j] = true;
                            }
                        }
                    }
                }
    
                System.out.println("esRespuestaSospechosa: " + Arrays.deepToString(esRespuestaSospechosa));
    
                boolean[] esCopion = new boolean[E];
                for (int i = 0; i < E; i++) {
                    boolean todasSospechosas = true;
                    int respuestasContestadas = 0;
                    for (int j = 0; j < R; j++) {
                        if (estudiantes[i].respuestas[j] == -1) continue;
                        respuestasContestadas++;
                        if (!esRespuestaSospechosa[i][j]) {
                            todasSospechosas = false;
                            break;
                        }
                    }
                    if (respuestasContestadas > 0 && todasSospechosas) {
                        esCopion[i] = true;
                    }
                }
                
                this._esCopion = esCopion;
    
                int countSospechosos = 0;
                for (int i = 0; i < E; i++) {
                    if (esCopion[i]) {
                        countSospechosos++;
                    }
                }
    
                int[] res = new int[countSospechosos];
                int index = 0;
                for (int i = 0; i < E; i++) {
                    if (esCopion[i]) {
                        res[index++] = i;
                    }
                }
                return res;
            }
        }

    // Complejidad: O(E*log E)
    public NotaFinal[] corregir() { // O(E*log E)
        if (this._esCopion == null) { // O(1)
            throw new IllegalStateException("chequearCopias must be called before corregir"); // O(1)
        }

        ListaEnlazada<NotaFinal> notas_validas = new ListaEnlazada<>(); // O(1)
        for (int i = 0; i < estudiantes.length; i++) { // Bucle O(E)
            if (estudiantes[i].entregado && !_esCopion[i]) { // O(1)
                notas_validas.agregarAtras(new NotaFinal(estudiantes[i].puntaje, estudiantes[i].id)); // O(1)
            }
        }

        NotaFinal[] notas_array = new NotaFinal[notas_validas.longitud()]; // O(V)
        ListaEnlazada.ListaIterador iter = notas_validas.iterador(); // O(1)
        int idx = 0; // O(1)
        while (iter.haySiguiente()) { // Bucle O(V)
            notas_array[idx++] = (NotaFinal) iter.siguiente(); // O(1)
        }

        if (notas_array.length > 0) { // O(1)
            mergeSort(notas_array, 0, notas_array.length - 1); // O(V log V)
        }
        return notas_array; // O(1)
    }

    private void mergeSort(NotaFinal[] arr, int inicio, int fin) { // O(N log N) donde N = fin - inicio
        if (inicio < fin) { // O(1)
            int medio = inicio + (fin - inicio) / 2; // O(1)
            // Cada llamada recursiva tiene un costo de O(N/2 * log(N/2))
            mergeSort(arr, inicio, medio);
            mergeSort(arr, medio + 1, fin);
            // La combinación de los resultados toma O(N)
            merge(arr, inicio, medio, fin);
        }
    }

    private void merge(NotaFinal[] arr, int inicio, int medio, int fin) { // O(N) donde N = fin - inicio
        int n1 = medio - inicio + 1; // O(1)
        int n2 = fin - medio; // O(1)
        NotaFinal[] izq = new NotaFinal[n1]; // O(n1)
        NotaFinal[] der = new NotaFinal[n2]; // O(n2)
        for (int i = 0; i < n1; i++) izq[i] = arr[inicio + i]; // Bucle O(n1)
        for (int j = 0; j < n2; j++) der[j] = arr[medio + 1 + j]; // Bucle O(n2)

        int i = 0, j = 0, k = inicio; // O(1)
        while (i < n1 && j < n2) { // Bucle O(n1+n2) = O(N)
            if (compararNotaFinal(izq[i], der[j]) >= 0) { // O(1)
                arr[k++] = izq[i++]; // O(1)
            } else {
                arr[k++] = der[j++]; // O(1)
            }
        }
        while (i < n1) arr[k++] = izq[i++]; // Bucle O(n1)
        while (j < n2) arr[k++] = der[j++]; // Bucle O(n2)
    }

    private int compararNotaFinal(NotaFinal a, NotaFinal b) { // O(1)
        if (a._nota != b._nota) { // O(1)
            return Double.compare(b._nota, a._nota); // O(1)
        }
        return Integer.compare(b._id, a._id); // O(1)
    }
}
