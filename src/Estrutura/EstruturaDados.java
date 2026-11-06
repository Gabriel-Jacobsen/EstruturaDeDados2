package Estrutura;

public interface EstruturaDados {

    boolean inserir(int valor);

    boolean buscar(int valor);

    boolean remover(int valor);

    int consultarIntervalo(int minimo, int maximo);

    int tamanho();
}