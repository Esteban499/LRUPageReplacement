import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.*;

public class LRUPageReplacement {
    private final int capacity;
    private final LinkedList<Integer> cache;
    private int pageFaults = 0;
    private int paso = 0;
    private final PrintWriter salida;

    // ───────────────────────────────────────────────────────────────
    // Constructor
    public LRUPageReplacement(int capacity, PrintWriter salida) {
        this.capacity = capacity;
        this.cache = new LinkedList<>();
        this.salida = salida;
    }

    // ───────────────────────────────────────────────────────────────
    // Procesamiento de una página solicitada
    public void accessPage(int page) {
        paso++;
        registrar("Paso " + paso + ": Se solicita la página " + page);

        if (cache.contains(page)) {
            manejarHit(page);
        } else {
            manejarFallo(page);
        }

        mostrarEstadoMemoria();
        registrar("----------------------------------");
    }

    // ───────────────────────────────────────────────────────────────
    // Acciones en caso de HIT
    private void manejarHit(int page) {
        cache.remove((Integer) page);
        cache.addLast(page);
        registrar(" ➤ HIT: la página " + page + " ya está en memoria.");
    }

    // ───────────────────────────────────────────────────────────────
    // Acciones en caso de FALLO
    private void manejarFallo(int page) {
        pageFaults++;
        if (cache.size() >= capacity) {
            int removed = cache.removeFirst();
            registrar(" ➤ FALLO: reemplazo de página - se elimina " + removed);
        } else {
            registrar(" ➤ FALLO: se carga la página " + page + " (hay espacio)");
        }
        cache.addLast(page);
    }

    // ───────────────────────────────────────────────────────────────
    // Mostrar estado actual de la memoria
    private void mostrarEstadoMemoria() {
        StringBuilder estado = new StringBuilder(" Estado actual de la memoria: ");
        for (int i = 0; i < capacity; i++) {
            if (i < cache.size()) {
                estado.append("[").append(cache.get(i)).append("] ");
            } else {
                estado.append("[ ] ");
            }
        }
        registrar(estado.toString());
    }

    // ───────────────────────────────────────────────────────────────
    // Mostrar resultados finales
    public void mostrarResultadosFinales() {
        registrar("\n======= RESULTADOS FINALES =======");
        registrar(" Memoria final: " + cache);
        registrar(" Total de fallos de página: " + pageFaults);
        registrar("==================================");
    }

    // ───────────────────────────────────────────────────────────────
    // Registrar salida tanto en archivo como en consola
    private void registrar(String mensaje) {
        System.out.println(mensaje);
        salida.println(mensaje);
    }

    // ───────────────────────────────────────────────────────────────
    // Método principal
    public static void main(String[] args) {
        try (Scanner scanner = new Scanner(System.in);
             PrintWriter writer = new PrintWriter(new FileWriter("resultado.txt"))) {

            int capacidad = pedirCapacidad(scanner);
            int[] secuencia = pedirSecuencia(scanner);

            LRUPageReplacement lru = new LRUPageReplacement(capacidad, writer);
            lru.registrar("Simulación del algoritmo de reemplazo LRU");
            lru.registrar("Capacidad de marcos de memoria: " + capacidad);
            lru.registrar("----------------------------------");

            for (int pagina : secuencia) {
                lru.accessPage(pagina);
            }

            lru.mostrarResultadosFinales();
            System.out.println("\n✅ Simulación finalizada. Resultado guardado en 'resultado.txt'.");

        } catch (IOException e) {
            System.err.println("❌ Error al escribir el archivo: " + e.getMessage());
        } catch (NumberFormatException e) {
            System.err.println("❌ Entrada inválida. Usa números separados por espacios.");
        }
    }

    // ───────────────────────────────────────────────────────────────
    // Métodos auxiliares para entrada del usuario
    private static int pedirCapacidad(Scanner scanner) {
        System.out.print("Ingrese la cantidad de marcos de memoria: ");
        return scanner.nextInt();
    }

    private static int[] pedirSecuencia(Scanner scanner) {
        scanner.nextLine(); // limpiar buffer
        System.out.print("Ingrese la secuencia de páginas (separadas por espacio): ");
        String[] entrada = scanner.nextLine().split(" ");
        return Arrays.stream(entrada).mapToInt(Integer::parseInt).toArray();
    }
}
