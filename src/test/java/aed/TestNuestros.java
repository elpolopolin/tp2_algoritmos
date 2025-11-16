package aed;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class TestNuestros {
    Edr edr;
    int d_aula;
    int cant_alumnos;
    int[] solucion;

    @BeforeEach
    void setUp(){
        d_aula = 5;
        cant_alumnos = 4;
        solucion = new int[]{0,1,2,3,4,5,6,7,8,9};

        edr = new Edr(d_aula, cant_alumnos, solucion);
    }

    // crea un edr mas chico
    @Test
    void nuevo_edr_chico() {
        int[] solucion_chica = new int[]{0,1,2,3,4};
        Edr edr_chico = new Edr(3, 3, solucion_chica);
        double[] notas = edr_chico.notas();
        double[] notas_esperadas = new double[]{0.0, 0.0, 0.0};
        assertTrue(Arrays.equals(notas_esperadas, notas));
    }

    // cambiar la misma respuesta varias veces actualiza bien la nota
    @Test
    void resolver_cambia_respuesta() {
        double[] notas;
        double[] notas_esperadas;
        edr.resolver(0,0,5);
        notas = edr.notas();
        notas_esperadas = new double[]{0.0, 0.0, 0.0, 0.0};
        assertTrue(Arrays.equals(notas_esperadas, notas));
        edr.resolver(0, 0, 0);
        notas = edr.notas();
        notas_esperadas = new double[]{10.0, 0.0, 0.0, 0.0};
        assertTrue(Arrays.equals(notas_esperadas, notas));
        edr.resolver(0, 0, 3);
        notas = edr.notas();
        notas_esperadas = new double[]{0.0, 0.0, 0.0, 0.0};
        assertTrue(Arrays.equals(notas_esperadas, notas));
    }

    // resuelve varios ejercicios para el mismo alumno y despues pisa una respuesta
    @Test
    void resolver_muchos_ejercicios_mismo_estudiante() {
        edr.resolver(1, 0, 0);
        edr.resolver(1, 1, 1);
        edr.resolver(1, 2, 2);
        double[] notas = edr.notas();
        double[] notas_esperadas = new double[]{0.0, 30.0, 0.0, 0.0};
        assertTrue(Arrays.equals(notas_esperadas, notas));
        edr.resolver(1, 1, 5);
        notas = edr.notas();
        notas_esperadas = new double[]{0.0, 20.0, 0.0, 0.0};
        assertTrue(Arrays.equals(notas_esperadas, notas));
    }

    // responde siempre mal y la nota deberia seguir en 0
    @Test
    void resolver_todo_mal() {
        for (int i = 0; i < 5; i++) {
            edr.resolver(2, i, 9 - i);
        }
        double[] notas = edr.notas();
        double[] notas_esperadas = new double[]{0.0, 0.0, 0.0, 0.0};
        assertTrue(Arrays.equals(notas_esperadas, notas));
    }

    // copiarse sin vecinos no cambia nada
    @Test
    void copiarse_sin_vecinos_no_cambia_nota() {
        int[] examen = new int[]{0, 1, 2, 3};
        Edr edr_uno = new Edr(4, 1, examen);
        double[] notas = edr_uno.notas();
        double[] notas_esperadas = new double[]{0.0};
        assertTrue(Arrays.equals(notas_esperadas, notas));
        edr_uno.copiarse(0);
        notas = edr_uno.notas();
        assertTrue(Arrays.equals(notas_esperadas, notas));
    }

    // si el vecino no tiene respuestas nuevas para copiar la nota queda igual
    @Test
    void copiarse_sin_respuestas_nuevas() {
        int[] examen = new int[]{0, 1, 2, 3};
        Edr edr_dos = new Edr(4, 2, examen);
        edr_dos.resolver(0, 0, 0);
        edr_dos.resolver(1, 0, 0);
        double[] notas = edr_dos.notas();
        double[] notas_esperadas = new double[]{25.0, 25.0};
        assertTrue(Arrays.equals(notas_esperadas, notas));
        edr_dos.copiarse(1);
        notas = edr_dos.notas();
        assertTrue(Arrays.equals(notas_esperadas, notas));
    }

    // el alumno se copia del vecino con mas respuestas nuevas y desempata por id mas grande
    @Test
    void copiarse_desempata_por_id_mas_grande() {
        int[] examen = new int[]{0, 1, 2, 3};
        Edr edr_copia = new Edr(4, 4, examen);
        edr_copia.resolver(0, 0, 0);
        edr_copia.resolver(0, 1, 1);
        edr_copia.resolver(3, 2, 2);
        edr_copia.resolver(3, 3, 3);
        edr_copia.copiarse(2);
        double[] notas = edr_copia.notas();
        double[] notas_esperadas = new double[]{50.0, 0.0, 25.0, 50.0};
        assertTrue(Arrays.equals(notas_esperadas, notas));
    }

    // entregar dos veces al mismo alumno no rompe y no cambia su nota
    @Test
    void entregar_dos_veces_no_rompe() {
        edr.resolver(0, 0, 0);
        double[] notas = edr.notas();
        double[] notas_esperadas = new double[]{10.0, 0.0, 0.0, 0.0};
        assertTrue(Arrays.equals(notas_esperadas, notas));
        edr.entregar(0);
        edr.entregar(0);
        edr.consultarDarkWeb(1, solucion);
        notas = edr.notas();
        assertEquals(10.0, notas[0]);
    }

    // si todos entregan antes de consultar darkweb las notas no cambian
    @Test
    void entregar_todos_antes_darkweb_no_cambia() {
        int[] examen = new int[]{0, 1, 2};
        Edr edr_tres = new Edr(3, 3, examen);
        edr_tres.resolver(0, 0, 0);
        edr_tres.resolver(1, 1, 1);
        edr_tres.resolver(2, 2, 2);
        double[] notas_antes = edr_tres.notas();
        for (int i = 0; i < 3; i++) {
            edr_tres.entregar(i);
        }
        edr_tres.consultarDarkWeb(2, examen);
        double[] notas_despues = edr_tres.notas();
        assertTrue(Arrays.equals(notas_antes, notas_despues));
    }

    // probar que cuando k es mas grande que la cantidad de alumnos solo cambian los que pueden
    @Test
    void consultarDarkWeb_con_k_mas_grande() {
        edr.entregar(0);
        edr.consultarDarkWeb(10, solucion);
        double[] notas = edr.notas();
        double[] notas_esperadas = new double[]{0.0, 100.0, 100.0, 100.0};
        assertTrue(Arrays.equals(notas_esperadas, notas));
    }

    // todos sin entregar se reemplazan por el examen de la darkweb
    @Test
    void consultarDarkWeb_todos_sin_entregar() {
        edr.resolver(0, 0, 0);
        edr.resolver(1, 1, 1);
        int[] examen = new int[]{0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
        edr.consultarDarkWeb(4, examen);
        double[] notas = edr.notas();
        double[] notas_esperadas = new double[]{10.0, 10.0, 10.0, 10.0};
        assertTrue(Arrays.equals(notas_esperadas, notas));
    }

    // si nadie responde nada no debe haber copiones y todas las notas finales son 0
    @Test
    void chequearCopias_sin_respuestas() {
        for (int i = 0; i < cant_alumnos; i++) {
            edr.entregar(i);
        }
        int[] copiones = edr.chequearCopias();
        int[] copiones_esperados = new int[]{};
        assertTrue(Arrays.equals(copiones_esperados, copiones));
        NotaFinal[] notas_finales = edr.corregir();
        NotaFinal[] notas_finales_esperadas = new NotaFinal[]{
            new NotaFinal(0.0, 3),
            new NotaFinal(0.0, 2),
            new NotaFinal(0.0, 1),
            new NotaFinal(0.0, 0)
        };
        assertTrue(Arrays.equals(notas_finales_esperadas, notas_finales));
    }

    // dos estudiantes con exactamente el mismo examen quedan marcados como copiones
    @Test
    void chequearCopias_dos_estudiantes_iguales() {
        int[] examen = new int[]{0, 1};
        Edr edr_dos = new Edr(3, 2, examen);
        edr_dos.resolver(0, 0, 5);
        edr_dos.resolver(1, 0, 5);
        edr_dos.resolver(0, 1, 5);
        edr_dos.resolver(1, 1, 5);
        edr_dos.entregar(0);
        edr_dos.entregar(1);
        int[] copiones = edr_dos.chequearCopias();
        int[] copiones_esperados = new int[]{0, 1};
        assertTrue(Arrays.equals(copiones_esperados, copiones));
        NotaFinal[] notas_finales = edr_dos.corregir();
        NotaFinal[] notas_finales_esperadas = new NotaFinal[]{};
        assertTrue(Arrays.equals(notas_finales_esperadas, notas_finales));
    }
}
