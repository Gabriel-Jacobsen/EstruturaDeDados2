package Estrutura;


public class ArrayOrdenado implements EstruturaDados {

    private int[] dados;
    private int tamanho;
    private int capacidade;

    // Construtor: reserva N + 10% de N posições
    public ArrayOrdenado(int n) {
        this.capacidade = n + (int) Math.ceil(n * 0.10);
        this.dados = new int[capacidade];
        this.tamanho = 0;
    }

    // Encontra a primeira posição cujo valor é maior ou igual ao procurado
    private int buscaBinaria(int valor) {
        int inicio = 0;
        int fim = tamanho;

        while (inicio < fim) {
            int meio = inicio + (fim - inicio) / 2;

            if (dados[meio] < valor) {
                inicio = meio + 1;
            } else {
                fim = meio;
            }
        }

        return inicio;
    }

    // Insere mantendo a ordem crescente
    @Override
    public boolean inserir(int valor) {

        int posicao = buscaBinaria(valor);

        // Verifica se o valor já existe
        if (posicao < tamanho && dados[posicao] == valor) {
            return false;
        }

        // Verifica se existe espaço disponível
        if (tamanho == capacidade) {
            throw new IllegalStateException("Capacidade máxima atingida.");
        }

        // Desloca os elementos para abrir espaço
        for (int i = tamanho; i > posicao; i--) {
            dados[i] = dados[i - 1];
        }

        // Insere o novo elemento
        dados[posicao] = valor;
        tamanho++;

        return true;
    }

    // Busca um elemento utilizando busca binária
    @Override
    public boolean buscar(int valor) {

        int posicao = buscaBinaria(valor);

        return posicao < tamanho && dados[posicao] == valor;
    }

    // Remove um elemento e reorganiza o array
    @Override
    public boolean remover(int valor) {

        int posicao = buscaBinaria(valor);

        // Verifica se o elemento existe
        if (posicao == tamanho || dados[posicao] != valor) {
            return false;
        }

        // Desloca os elementos para preencher o espaço
        for (int i = posicao; i < tamanho - 1; i++) {
            dados[i] = dados[i + 1];
        }

        tamanho--;

        return true;
    }

    // Conta quantos elementos estão dentro do intervalo
    @Override
    public int consultarIntervalo(int minimo, int maximo) {

        if (minimo > maximo) {
            return 0;
        }

        // Primeira posição com valor >= minimo
        int inicio = buscaBinaria(minimo);

        // Primeira posição com valor >= maximo + 1
        int fim;

        if (maximo == Integer.MAX_VALUE) {
            fim = tamanho;
        } else {
            fim = buscaBinaria(maximo + 1);
        }

        return fim - inicio;
    }

    // Retorna a quantidade de elementos armazenados
    @Override
    public int tamanho() {
        return tamanho;
    }
}