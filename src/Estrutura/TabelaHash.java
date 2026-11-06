package Estrutura;


public class TabelaHash implements EstruturaDados {

    private static class No {
        int valor;
        No proximo;

        public No(int valor) {
            this.valor = valor;
            this.proximo = null;
        }
    }

    private No[] tabela;
    private int tamanho;
    private int capacidade;
    private double fatorCarga;

    private static final int CAPACIDADE_INICIAL = 16;
    private static final double FATOR_CARGA_MAXIMO = 0.75;

    // Construtor
    public TabelaHash() {
        this.capacidade = CAPACIDADE_INICIAL;
        this.tamanho = 0;
        this.fatorCarga = FATOR_CARGA_MAXIMO;
        this.tabela = new No[capacidade];
    }

    // Calcula o índice da tabela
    private int hash(int valor) {
        return Math.floorMod(valor, capacidade);
    }

    // Insere um elemento
    @Override
    public boolean inserir(int valor) {

        // Verifica se o elemento já existe
        if (buscar(valor)) {
            return false;
        }

        // Verifica se precisa redimensionar
        if ((double) (tamanho + 1) / capacidade > fatorCarga) {
            redimensionar();
        }

        int indice = hash(valor);

        No novo = new No(valor);

        // Insere no início da lista
        novo.proximo = tabela[indice];
        tabela[indice] = novo;

        tamanho++;

        return true;
    }

    // Busca um elemento
    @Override
    public boolean buscar(int valor) {

        int indice = hash(valor);

        No atual = tabela[indice];

        while (atual != null) {

            if (atual.valor == valor) {
                return true;
            }

            atual = atual.proximo;
        }

        return false;
    }

    // Remove um elemento
    @Override
    public boolean remover(int valor) {

        int indice = hash(valor);

        No atual = tabela[indice];
        No anterior = null;

        while (atual != null) {

            if (atual.valor == valor) {

                // Elemento está no início da lista
                if (anterior == null) {
                    tabela[indice] = atual.proximo;
                } 
                // Elemento está no meio ou final
                else {
                    anterior.proximo = atual.proximo;
                }

                tamanho--;

                return true;
            }

            anterior = atual;
            atual = atual.proximo;
        }

        return false;
    }

    // Conta elementos dentro de um intervalo
    @Override
    public int consultarIntervalo(int minimo, int maximo) {

        if (minimo > maximo) {
            return 0;
        }

        int quantidade = 0;

        for (int i = 0; i < capacidade; i++) {

            No atual = tabela[i];

            while (atual != null) {

                if (atual.valor >= minimo && atual.valor <= maximo) {
                    quantidade++;
                }

                atual = atual.proximo;
            }
        }

        return quantidade;
    }

    // Retorna a quantidade de elementos
    @Override
    public int tamanho() {
        return tamanho;
    }

    // Aumenta a capacidade e redistribui os elementos
    private void redimensionar() {

        int novaCapacidade = capacidade * 2;

        No[] novaTabela = new No[novaCapacidade];

        // Redistribui todos os elementos
        for (int i = 0; i < capacidade; i++) {

            No atual = tabela[i];

            while (atual != null) {

                No proximo = atual.proximo;

                // Calcula o novo índice
                int novoIndice = Math.floorMod(
                    atual.valor, novaCapacidade
                );

                // Insere o nó na nova tabela
                atual.proximo = novaTabela[novoIndice];
                novaTabela[novoIndice] = atual;

                atual = proximo;
            }
        }

        tabela = novaTabela;
        capacidade = novaCapacidade;
    }
}
