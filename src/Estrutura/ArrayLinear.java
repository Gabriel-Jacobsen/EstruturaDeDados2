package Estrutura;


public class ArrayLinear implements EstruturaDados {

    private int[] dados;
    private int tamanho;
    private int capacidade;

    // Construtor
    public ArrayLinear(int capacidade) {
        this.capacidade = capacidade;
        this.dados = new int[capacidade];
        this.tamanho = 0;
    }

    // Insere um elemento no final do array
    @Override
    public boolean inserir(int valor) {

        // Verifica se o elemento já existe
        if (buscar(valor)) {
            return false;
        }

        // Verifica se existe espaço disponível
        if (tamanho >= capacidade) {
            return false;
        }

        dados[tamanho] = valor;
        tamanho++;

        return true;
    }

    // Busca sequencialmente um elemento
    @Override
    public boolean buscar(int valor) {

        for (int i = 0; i < tamanho; i++) {
            if (dados[i] == valor) {
                return true;
            }
        }

        return false;
    }

    // Remove um elemento do array
    @Override
    public boolean remover(int valor) {

        for (int i = 0; i < tamanho; i++) {

            if (dados[i] == valor) {

                // Substitui pelo último elemento
                dados[i] = dados[tamanho - 1];

                tamanho--;

                return true;
            }
        }

        return false;
    }

    // Conta os elementos dentro do intervalo
    @Override
    public int consultarIntervalo(int minimo, int maximo) {

        int quantidade = 0;

        for (int i = 0; i < tamanho; i++) {

            if (dados[i] >= minimo && dados[i] <= maximo) {
                quantidade++;
            }
        }

        return quantidade;
    }

    // Retorna a quantidade de elementos armazenados
    @Override
    public int tamanho() {
        return tamanho;
    }
}
