package Exercici1;

import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveTask;

/**
 *
 * @author Matias
 */
public class TestEx1 extends RecursiveTask<Integer> {

    private final ArrayList<Integer> array;
    private final int inicio, finale;

    /**
     * Constructor
     *
     * @param arr
     * @param inici
     * @param fin
     */
    @SuppressWarnings("unchecked")
    public TestEx1(ArrayList arr, int inici, int fin) {
        this.array = arr;
        this.inicio = inici;
        this.finale = fin;
    }

    /**
     * Parte el array en tareas más sencillas para comparar 2 resultados. Los parte por la mitad 
     * y si sigue siendo demasiado grande los vuelve a partir por la mitad, así succesivamente. Devuelve 
     * al padre el resultado más alto de los dos que compara.
     * @return
     */
    @Override
    protected Integer compute() {
        if (finale - inicio <= 1) {
            int sueldoMaximo = Math.max(array.get(inicio), array.get(finale));
            return sueldoMaximo;

        } else {
            int mitat = inicio + (finale - inicio) / 2;
            TestEx1 forkJoin1 = new TestEx1(array, inicio, mitat);
            TestEx1 forkJoin2 = new TestEx1(array, mitat + 1, finale);
            invokeAll(forkJoin1, forkJoin2);
            return Math.max(forkJoin1.join(), forkJoin2.join());
        }
    }

    /**
     * Llena el array de números aleatorios entre 0 y 50.000. También se crean los hilos 
     * dependiendo del número disponible de procesadores para posteriormente hacer el "invoke" (ejecutar el método "compute()"
     * y almacenando en una variable el resultado de esa tarea. Y por último, mostramos una pequeña frase
     * para que quede bonito seguido del contenido de la variable del resultado de la tarea anterior. Que és el
     * número más alto del array.
     * @param args
     */
    public static void main(String[] args) {
        Random aleatorio = new Random();
        ArrayList<Integer> sueldos = new ArrayList<>();

        for (int i = 0; i < 20000; i++) {
            int sueldoAleatorio = aleatorio.nextInt(50001);
            sueldos.add(sueldoAleatorio);
        }

        int NumberOfProcessors = Runtime.getRuntime().availableProcessors();
        ForkJoinPool pool = new ForkJoinPool(NumberOfProcessors);
        TestEx1 tarea = new TestEx1(sueldos, 0, sueldos.size() - 1);
        Integer resultado = pool.invoke(tarea);

        System.out.println("El número más alto del array és: " + resultado);
    }

}
