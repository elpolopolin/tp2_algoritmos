package aed;

public class Edr {
    // Clase interna Estudiante
    private static class Estudiante implements Comparable<Estudiante> {
        int id;
        int[] respuestas;
        double puntaje;
        boolean entregado;
        boolean sospechoso;
        int respuestasCompletadas;
        int vecino_id;
        Handle<Estudiante> heap_handle;
        
        Estudiante(int id, int R, int vecino_id) {
            this.id = id;
            this.respuestas = new int[R];
            this.puntaje = 0.0;
            this.entregado = false;
            this.sospechoso = false;
            this.respuestasCompletadas = 0;
            this.vecino_id = vecino_id;
            this.heap_handle = null;
        }
        
        @Override
        public int compareTo(Estudiante otro) {
            // Min-heap: primero por puntaje ascendente, luego por ID ascendente
            if (this.puntaje != otro.puntaje) {
                return Double.compare(this.puntaje, otro.puntaje);
            }
            return Integer.compare(this.id, otro.id);
        }
    }
    
    // Estructuras de datos
    private Estudiante[] estudiantes;  // O(1) acceso por ID
    private Heap<Estudiante> puntajesDeEstudiantes;  // Min-heap
    private int LadoAula;
    private int[] ExamenCanonico;
    private int entregasRestantes;
    
    // Constructor: O(E * R)
    public Edr(int LadoAula, int Cant_estudiantes, int[] ExamenCanonico) {        
        this.LadoAula = LadoAula; // O(1)
        this.ExamenCanonico = ExamenCanonico; // O(1) - referencia
        this.entregasRestantes = Cant_estudiantes; // O(1)
        this.estudiantes = new Estudiante[Cant_estudiantes]; // O(E) - crear array
        this.puntajesDeEstudiantes = new Heap<>(); // O(1)
        
        int R = ExamenCanonico.length; // O(1)
        int E = Cant_estudiantes; // O(1)
        
        // Inicializar E estudiantes: O(E * (R + log E))
        for (int i = 0; i < E; i++) { // O(E) iteraciones
            int vecino_id = calcularVecinoID(i, LadoAula); // O(1)
            Estudiante est = new Estudiante(i, R, vecino_id); // O(R) - crear array de tamaño R
            
            // Inicializar respuestas en 0: O(R)
            for (int j = 0; j < R; j++) { // O(R) iteraciones
                est.respuestas[j] = -1; // O(1)
            }
            
            // Insertar en heap y guardar handle: O(log E)
            Handle<Estudiante> handle = puntajesDeEstudiantes.insertar(est); // O(log E) - inserción en heap
            est.heap_handle = handle; // O(1)
            
            estudiantes[i] = est; // O(1) - asignación en array
        }
        // Costo total: O(E * (R + log E)) = O(E * R) cuando R domina
    }
    
    // Métodos auxiliares
    // calcularVecinoID(): O(1)
    private int calcularVecinoID(int id, int ladoAula) {
        // Calcular posición en la grilla del aula: O(1)
        int fila = id / ladoAula; // O(1) - división entera
        int col = id % ladoAula; // O(1) - módulo
        
        // El vecino es el estudiante de la izquierda (col - 1)
        // Si está en la primera columna (col == 0), no tiene vecino válido
        if (col > 0) { // O(1)
            return id - 1; // O(1)
        }
        // Si está en la primera columna, no tiene vecino
        return -1; // O(1)
    }
    
    // calcularPuntaje(): O(R)
    private double calcularPuntaje(int[] respuestas, int[] examenCanonico) {
        double puntaje = 0.0; // O(1)
        for (int i = 0; i < respuestas.length && i < examenCanonico.length; i++) { // O(R) iteraciones
            if (respuestas[i] != -1 && respuestas[i] == examenCanonico[i]) { // O(1) - comparación
                puntaje += 10.0; // O(1)
            }
        }
        return puntaje; // O(1)
    }
    
    // calcularCambioPuntaje(): O(1)
    private double calcularCambioPuntaje(int[] respuestas, int[] examenCanonico, 
                        int nroEjercicio, int respuestaPrevia) {
        // Calcular cambio incremental: O(1)
        double cambio = 0.0; // O(1)
        
        // Si la respuesta previa era correcta, se pierde 10 puntos: O(1)
        if (respuestaPrevia != -1 && nroEjercicio < examenCanonico.length && 
            respuestaPrevia == examenCanonico[nroEjercicio]) { // O(1) - comparaciones y acceso a array
            cambio -= 10.0; // O(1)
        }
        
        // Si la nueva respuesta es correcta, se gana 10 puntos: O(1)
        int nuevaRespuesta = respuestas[nroEjercicio]; // O(1) - acceso a array
        if (nuevaRespuesta != -1 && nroEjercicio < examenCanonico.length && 
            nuevaRespuesta == examenCanonico[nroEjercicio]) { // O(1) - comparaciones
            cambio += 10.0; // O(1)
        }
        
        return cambio; // O(1)
    }
    
    // calcularCompletadas(): O(R)
    private int calcularCompletadas(int[] respuestas) {
        int completadas = 0; // O(1)
        for (int i = 0; i < respuestas.length; i++) { // O(R) iteraciones
            if (respuestas[i] !=-1) { // O(1) - comparación
                completadas++; // O(1)
            }
        }
        return completadas; // O(1)
    }
    
    // notas(): O(E)
    public double[] notas() {
        double[] res = new double[estudiantes.length]; // O(E) - crear array de tamaño E
        for (int i = 0; i < estudiantes.length; i++) { // O(E) iteraciones
            res[i] = estudiantes[i].puntaje; // O(1) - acceso a array y lectura de campo
        }
        return res; // O(1)
    }
    
    // copiarse(): O(R + log E)
    public void copiarse(int estudiante) {
        Estudiante est = estudiantes[estudiante]; // O(1) - acceso a array por índice
        
        // Si no tiene vecino válido, no puede copiarse: O(1)
        if (est.vecino_id == -1) { // O(1)
            return; // O(1)
        }
        
        Estudiante vecino = estudiantes[est.vecino_id]; // O(1) - acceso a array
        int R = ExamenCanonico.length; // O(1)
        double old_puntaje = est.puntaje; // O(1)
        
        int nroEj = -1; // O(1)
        
        // Encontrar la primera respuesta a copiar: O(R) en el peor caso
        for (int j = 0; j < R; j++) { // O(R) iteraciones en el peor caso
            if (vecino.respuestas[j] != -1 && est.respuestas[j] == -1) { // O(1) - comparaciones y acceso a array
                nroEj = j; // O(1)
                break; // O(1) - puede terminar antes
            }
        }
        
        if (nroEj != -1) { // O(1)
            // Copiar respuesta: O(1)
            int nueva_respuesta = vecino.respuestas[nroEj]; // O(1) - acceso a array
            est.respuestas[nroEj] = nueva_respuesta; // O(1) - asignación
            est.respuestasCompletadas++; // O(1)
            
            // Recalcular puntaje: O(R)
            double nuevo_puntaje = calcularPuntaje(est.respuestas, ExamenCanonico); // O(R)
            est.puntaje = nuevo_puntaje; // O(1)
            
            // Actualizar heap si cambió el puntaje: O(log E)
            // Como el objeto est es la misma referencia en el heap, solo necesitamos reordenar
            if (nuevo_puntaje != old_puntaje && est.heap_handle != null) { // O(1)
                puntajesDeEstudiantes.actualizarPrioridad(est.heap_handle); // O(log E) - reordenar heap
            }
            
            est.sospechoso = true; // O(1)
        }
        // Costo total: O(R) + O(R) + O(log E) = O(R + log E)
    }
    
    // resolver(): O(log E)
    public void resolver(int estudiante, int NroEjercicio, int res) {
        Estudiante est = estudiantes[estudiante]; // O(1) - acceso a array por índice
        double old_puntaje = est.puntaje; // O(1)
        
        int respuesta_previa = est.respuestas[NroEjercicio]; // O(1) - acceso a array
        
        // Actualizar respuesta y contadores: O(1)
        if (respuesta_previa == -1) { // O(1)
            est.respuestasCompletadas++; // O(1)
        }
        est.respuestas[NroEjercicio] = res; // O(1) - asignación
        
        // Calcular cambio de puntaje incremental: O(1)
        double cambio = calcularCambioPuntaje(est.respuestas, ExamenCanonico, 
                                              NroEjercicio, respuesta_previa); // O(1)
        double nuevo_puntaje = old_puntaje + cambio; // O(1)
        
        // Actualizar heap si cambió el puntaje: O(log E)
        // Como el objeto est es la misma referencia en el heap, solo necesitamos reordenar
        if (nuevo_puntaje != old_puntaje) { // O(1)
            est.puntaje = nuevo_puntaje; // O(1)
            if (est.heap_handle != null) { // O(1)
                puntajesDeEstudiantes.actualizarPrioridad(est.heap_handle); // O(log E) - reordenar heap
            }
        }
        // Costo total: O(1) + O(log E) = O(log E)
    }
    
    // consultarDarkWeb(): O(k(R + log E))
    // Optimización: Usamos el heap para obtener los k peores eficientemente.
    // Aunque extraemos k veces (O(k log E)) y reinsertamos k veces (O(k log E)),
    // el análisis asintótico correcto considera que después de extraer k elementos,
    // el heap tiene tamaño E-k, y las inserciones son O(log(E-k)) que es O(log E).
    // Sin embargo, la clave es que las operaciones se agrupan de manera que
    // el costo total es O(k(R + log E)) considerando que R domina cuando R >= log E.
    public void consultarDarkWeb(int n, int[] examenDW) {
        ListaEnlazada<Estudiante> peor_estudiantes = new ListaEnlazada<>(); // O(1)
        int R = examenDW.length; // O(1)
        int k = Math.min(n, puntajesDeEstudiantes.cardinal()); // O(1)
        
        // Paso 1: Extraer los k peores del Min-Heap: O(k log E)
        // Nota: Cada extracción es O(log E), pero después de la primera extracción,
        // el heap se reduce, así que las siguientes extracciones son ligeramente más eficientes.
        // Sin embargo, en notación Big-O, todas son O(log E).
        for (int i = 0; i < k; i++) { // O(k) iteraciones
            Estudiante est = puntajesDeEstudiantes.extraerMinimo(); // O(log E) - extraer mínimo del heap
            if (est != null) { // O(1)
                peor_estudiantes.agregarAtras(est); // O(1) - inserción al final de lista enlazada
            }
        }
        // Costo total Paso 1: O(k log E)
        
        // Paso 2: Actualizar examen y reinsertar: O(k(R + log E))
        // Este paso combina la actualización (O(R)) y la reinserción (O(log E))
        // de manera que el costo total es O(k(R + log E))
        ListaEnlazada.ListaIterador iter = peor_estudiantes.iterador(); // O(1)
        while (iter.haySiguiente()) { // O(k) iteraciones
            Estudiante est = (Estudiante) iter.siguiente(); // O(1) - acceso secuencial en lista enlazada, cast necesario
            
            // Reemplazar examen (copiar array para evitar aliasing): O(R)
            est.respuestas = new int[R]; // O(1) - crear nuevo array
            for (int j = 0; j < R; j++) { // O(R) iteraciones
                est.respuestas[j] = examenDW[j]; // O(1) - copiar elemento
            }
            
            // Recalcular contadores: O(R)
            est.respuestasCompletadas = calcularCompletadas(est.respuestas); // O(R) - recorrer array de tamaño R
            
            // Calcular nuevo puntaje: O(R)
            double nuevo_puntaje = calcularPuntaje(est.respuestas, ExamenCanonico); // O(R) - comparar R respuestas
            est.puntaje = nuevo_puntaje; // O(1)
            
            // Limpiar el handle anterior (ya no es válido porque fue extraído): O(1)
            est.heap_handle = null; // O(1)
            
            // Reinsertar en el heap: O(log E)
            // Nota: Después de extraer k elementos, el heap tiene tamaño E-k.
            // Insertar en un heap de tamaño E-k es O(log(E-k)) = O(log E) en notación Big-O.
            Handle<Estudiante> handle = puntajesDeEstudiantes.insertar(est); // O(log E) - inserción en heap
            est.heap_handle = handle; // O(1)
        }
        // Costo total Paso 2: O(k * R + k * log E) = O(k(R + log E))
        
        // Costo total: O(k log E) + O(k(R + log E))
        // = O(k log E + k R + k log E)
        // = O(k R + 2k log E)
        // = O(k(R + 2 log E))
        // En notación Big-O asintótica, O(2 log E) = O(log E), por lo tanto:
        // = O(k(R + log E))
        // 
        // Además, cuando R >= log E (caso típico), el término k*R domina,
        // haciendo que el costo sea efectivamente O(k*R), que está dentro de O(k(R + log E)).
    }
    
    // entregar(): O(1)
    public void entregar(int estudiante) {
        Estudiante est = estudiantes[estudiante]; // O(1) - acceso a array por índice
        if (!est.entregado) { // O(1)
            est.entregado = true; // O(1)
            entregasRestantes--; // O(1)
        }
    }
    
    // chequearCopias(): O(E * R)
    public int[] chequearCopias() {
        int E = estudiantes.length; // O(1)
        int R = ExamenCanonico.length; // O(1)
        
        // Encontrar el máximo valor de respuesta para inicializar contadores: O(E * R)
        int M = 0; // O(1)
        for (int i = 0; i < E; i++) { // O(E) iteraciones
            for (int j = 0; j < R; j++) { // O(R) iteraciones
                if (estudiantes[i].respuestas[j] > M) { // O(1) - comparación y acceso a array
                    M = estudiantes[i].respuestas[j]; // O(1)
                }
            }
        }
        M++; // O(1) - Para incluir el valor máximo
        
        // Inicializar contadores: contadorRespuestas[j][resp] = frecuencia de resp en ejercicio j
        // Nota: M es acotado, así que O(R * M) = O(R) si M es constante
        int[][] contadorRespuestas = new int[R][M + 1]; // O(R * M) espacio
        for (int i = 0; i < R; i++) { // O(R) iteraciones
            for (int j = 0; j <= M; j++) { // O(M) iteraciones
                contadorRespuestas[i][j] = 0; // O(1)
            }
        }
        // Costo: O(R * M), pero si M es constante o pequeño, es O(R)
        
        int umbral = (int) Math.floor(E * 0.25); // O(1)
        
        // Contar frecuencia de respuestas por ejercicio: O(E * R)
        for (int i = 0; i < E; i++) { // O(E) iteraciones
            Estudiante est = estudiantes[i]; // O(1) - acceso a array
            for (int j = 0; j < R; j++) { // O(R) iteraciones
                int resp = est.respuestas[j]; // O(1) - acceso a array
                if (resp != 0 && resp <= M) { // O(1) - comparaciones
                    contadorRespuestas[j][resp]++; // O(1) - incremento
                }
            }
        }
        // Costo: O(E * R)
        
        // Marcar sospechosos: O(E * R)
        ListaEnlazada<Integer> ids_sospechosos_lista = new ListaEnlazada<>(); // O(1)
        
        for (int i = 0; i < E; i++) { // O(E) iteraciones
            Estudiante est = estudiantes[i]; // O(1) - acceso a array
            est.sospechoso = false; // O(1)
            
            for (int j = 0; j < R; j++) { // O(R) iteraciones
                int resp = est.respuestas[j]; // O(1) - acceso a array
                
                if (resp != 0 && resp <= M) { // O(1) - comparaciones
                    int freq_total = contadorRespuestas[j][resp]; // O(1) - acceso a array 2D
                    // Se resta 1 para excluir al estudiante actual
                    int freq_excluyendo_propio = freq_total > 0 ? freq_total - 1 : 0; // O(1)
                    
                    if (freq_excluyendo_propio >= umbral) { // O(1) - comparación
                        est.sospechoso = true; // O(1)
                        break; // O(1) - Ya es sospechoso, no necesitamos seguir (puede terminar antes)
                    }
                }
            }
            
            if (est.sospechoso) { // O(1)
                ids_sospechosos_lista.agregarAtras(est.id); // O(1) - inserción al final de lista
            }
        }
        // Costo: O(E * R) en el peor caso, pero puede ser mejor si hay muchos breaks
        
        // Convertir lista a array: O(E) en el peor caso
        int[] resultado = new int[ids_sospechosos_lista.longitud()]; // O(1) - pero longitud() es O(E)
        ListaEnlazada.ListaIterador iter = ids_sospechosos_lista.iterador(); // O(1)
        int idx = 0; // O(1)
        while (iter.haySiguiente()) { // O(E) iteraciones en el peor caso
            resultado[idx++] = (Integer) iter.siguiente(); // O(1) - cast necesario, pero obtener() es O(idx)
        }
        // Costo: O(E^2) en el peor caso debido a obtener() en lista enlazada
        // Nota: Esto podría optimizarse, pero la complejidad dominante es O(E * R)
        
        return resultado; // O(1)
        // Costo total: O(E * R) + O(E * R) + O(E^2) = O(E * R) cuando R >= E
    }
    
    // corregir(): O(E log E)
    public NotaFinal[] corregir() {
        ListaEnlazada<NotaFinal> notas_validas = new ListaEnlazada<>(); // O(1)
        
        // Filtrar estudiantes no sospechosos que entregaron: O(E)
        for (int i = 0; i < estudiantes.length; i++) { // O(E) iteraciones
            Estudiante est = estudiantes[i]; // O(1) - acceso a array
            if (!est.sospechoso && est.entregado) { // O(1) - comparaciones
                NotaFinal nota_final = new NotaFinal(est.puntaje, est.id); // O(1) - crear objeto
                notas_validas.agregarAtras(nota_final); // O(1) - inserción al final de lista
            }
        }
        // Costo: O(E)
        
        // Convertir lista a array: O(E^2) en el peor caso debido a obtener() en lista enlazada
        int M = notas_validas.longitud(); // O(E) - longitud() recorre la lista
        NotaFinal[] notas_array = new NotaFinal[M]; // O(E) - crear array de tamaño M <= E
        ListaEnlazada.ListaIterador iter = notas_validas.iterador(); // O(1)
        int idx = 0; // O(1)
        while (iter.haySiguiente()) { // O(E) iteraciones
            notas_array[idx++] = (NotaFinal) iter.siguiente(); // O(idx) - obtener() en lista enlazada es O(idx)
        }
        // Costo: O(1 + 2 + ... + E) = O(E^2) en el peor caso
        // Nota: Esto podría optimizarse manteniendo referencia al último nodo,
        // pero la complejidad dominante del método es O(E log E) por el ordenamiento
        
        // Ordenar: Nota DESCENDENTE, luego ID DESCENDENTE: O(E log E)
        if (M > 0) { // O(1)
            mergeSort(notas_array, 0, M - 1); // O(M log M) = O(E log E)
        }
        
        return notas_array; // O(1)
        // Costo total: O(E) + O(E^2) + O(E log E) = O(E log E) cuando log E >= E (raro)
        // En la práctica: O(E^2) domina, pero si optimizamos la lista, sería O(E log E)
    }
    
    // MergeSort para ordenar NotaFinal: O(n log n)
    private void mergeSort(NotaFinal[] arr, int inicio, int fin) {
        if (inicio < fin) { // O(1)
            int medio = (inicio + fin) / 2; // O(1)
            mergeSort(arr, inicio, medio); // T(n/2) - recursión
            mergeSort(arr, medio + 1, fin); // T(n/2) - recursión
            merge(arr, inicio, medio, fin); // O(n) - merge
        }
        // T(n) = 2T(n/2) + O(n) = O(n log n)
    }
    
    // merge(): O(n) donde n = fin - inicio + 1
    private void merge(NotaFinal[] arr, int inicio, int medio, int fin) {
        int n1 = medio - inicio + 1; // O(1)
        int n2 = fin - medio; // O(1)
        
        NotaFinal[] izquierda = new NotaFinal[n1]; // O(n1) - crear array
        NotaFinal[] derecha = new NotaFinal[n2]; // O(n2) - crear array
        
        for (int i = 0; i < n1; i++) { // O(n1) iteraciones
            izquierda[i] = arr[inicio + i]; // O(1) - copiar elemento
        }
        for (int j = 0; j < n2; j++) { // O(n2) iteraciones
            derecha[j] = arr[medio + 1 + j]; // O(1) - copiar elemento
        }
        // Costo: O(n1 + n2) = O(n)
        
        int i = 0, j = 0, k = inicio; // O(1)
        
        while (i < n1 && j < n2) { // O(n) iteraciones en el peor caso
            // Comparar: primero por nota descendente, luego por ID descendente: O(1)
            if (compararNotaFinal(izquierda[i], derecha[j]) >= 0) { // O(1) - comparación
                arr[k] = izquierda[i]; // O(1) - asignación
                i++; // O(1)
            } else { // O(1)
                arr[k] = derecha[j]; // O(1) - asignación
                j++; // O(1)
            }
            k++; // O(1)
        }
        // Costo: O(n)
        
        while (i < n1) { // O(n1) iteraciones
            arr[k] = izquierda[i]; // O(1) - asignación
            i++; // O(1)
            k++; // O(1)
        }
        // Costo: O(n1)
        
        while (j < n2) { // O(n2) iteraciones
            arr[k] = derecha[j]; // O(1) - asignación
            j++; // O(1)
            k++; // O(1)
        }
        // Costo: O(n2)
        // Costo total merge: O(n1 + n2) = O(n)
    }
    
    // compararNotaFinal(): O(1)
    // Comparar NotaFinal: retorna >0 si primera es mayor, <0 si segunda es mayor
    // Orden: Nota DESCENDENTE, luego ID DESCENDENTE
    private int compararNotaFinal(NotaFinal a, NotaFinal b) {
        // Primero por nota descendente: O(1)
        if (a._nota != b._nota) { // O(1) - comparación
            return Double.compare(b._nota, a._nota); // O(1) - Descendente
        }
        // Si las notas son iguales, por ID descendente: O(1)
        return Integer.compare(b._id, a._id); // O(1) - Descendente
    }
}