package Exercici2;

import java.util.concurrent.ForkJoinPool;
import static java.util.concurrent.ForkJoinTask.invokeAll;
import java.util.concurrent.RecursiveTask;

//Amb un termòmetre es pren la temperatura des de les 00:00h fins les 15:45h cada 15 minuts (4
//mostres per hora). Es demana calcular la temperatura mitja de cada hora i mostrar la temperatura
//mitja més baixa de totes.
public class TestEx2 extends RecursiveTask<Double> {

    private final int inicio, finale;
    private final double[] array;

    /**
     * Constructor
     * @param array
     * @param inicio
     * @param finale 
     */
    public TestEx2(double[] array, int inicio, int finale) {
        this.inicio = inicio;
        this.finale = finale;
        this.array = array;
    }


    /**
     * Se crea el array de las temperaturas registradas. Se obtienen los procesadores disponibles 
     * para crear los hilos e invocar el método "compute()" donde se divide la tarea en tareas más simples
     * para realizar la suma de los 4 primeros valores con un bucle del array y diviendo el resultado de esa suma
     * entre 4 para saber la temperatura media de esa hora (Ya que las temperaturas se registran cada 15 minutos). 
     * De esta se realiza la temperatura media de cada hora y posteriormentese realiza la temperatura media de todos 
     * los resultados anteriores.
     * 
     * @param args 
     */
    public static void main(String[] args) {

        double[] temperaturas = {
            13.0, 13.2, 13.3, 13.4, //00:00 h.
            13.5, 13.7, 13.8, 13.9, //01:00 h.
            14.1, 14.2, 14.3, 14.4, //02:00 h.
            14.6, 14.7, 14.8, 14.9, //03:00 h.
            15.0, 15.2, 15.3, 15.4, //04:00 h.
            15.5, 15.7, 15.8, 15.9, //05:00 h.
            16.1, 16.2, 16.3, 16.4, //06:00 h.
            16.6, 16.7, 16.8, 16.9, //07:00 h.
            17.0, 17.2, 17.3, 17.4, //08:00 h.
            17.5, 17.7, 17.8, 17.9, //09:00 h.
            18.1, 18.2, 18.3, 18.4, //10:00 h.
            18.6, 18.7, 18.8, 18.9, //11:00 h.
            18.0, 18.2, 18.3, 18.4, //12:00 h.
            18.5, 18.7, 18.8, 18.9, //13:00 h.
            17.1, 17.2, 17.3, 17.4, //14:00 h.
            17.6, 17.7, 17.8, 17.9, //15:00 h.
        };

        int NumberOfProcessors = Runtime.getRuntime().availableProcessors();
        ForkJoinPool pool = new ForkJoinPool(NumberOfProcessors);
        TestEx2 test = new TestEx2(temperaturas, 0, temperaturas.length - 1);
        Double resultado = pool.invoke(test);

        System.out.println("Temperatura media más alta: " + resultado + " ºC");
    }

    /**
     * Se dividen las tareas en otras más sencillas para hacer la media de las temperaturas almacenadas en el array
     * de temperaturas. 
     * @return 
     */
    @Override
    protected Double compute() {

        if (finale - inicio <= 4) {
            double contador = 0.0;
            for (int i = 0; i < 4; i++) {
                contador += array[inicio+i];
            }          
            return contador/4;
        } else {
            int mitat = inicio + (finale - inicio) / 2;
            TestEx2 forkJoin1 = new TestEx2(array, inicio, mitat);
            TestEx2 forkJoin2 = new TestEx2(array, mitat + 1, finale);
            invokeAll(forkJoin1, forkJoin2);
            return Math.max(forkJoin1.join(), forkJoin2.join());
        }
    }
}


