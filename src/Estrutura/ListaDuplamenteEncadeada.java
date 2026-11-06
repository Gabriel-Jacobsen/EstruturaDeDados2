package Estrutura;


public class ListaDuplamenteEncadeada implements EstruturaDados {

    // Classe interna para representar cada nó
    private static class No {
        int valor;
        No anterior;
        No proximo;

        public No(int valor) {
            this.valor = valor;
            this.anterior = null;
            this.proximo = null;
        }
    }

    // Referências para o início e o fim da lista
    private No inicio;
    private No fim;
    private int tamanho;

    // Construtor
    public ListaDuplamenteEncadeada() {
        inicio = null;
        fim = null;
        tamanho = 0;
    }

    // Insere um valor na lista, caso não exista
    @Override
    public boolean inserir(int valor) {

        // Verifica se o valor já existe
        if (buscar(valor)) {
            return false;
        }

        No novo = new No(valor);

        // Caso a lista esteja vazia
        if (inicio == null) {
            inicio = novo;
            fim = novo;
        } else {
            // Insere no final da lista
            fim.proximo = novo;
            novo.anterior = fim;
            fim = novo;
        }

        tamanho++;
        return true;
    }

    // Busca um valor na lista
    @Override
    public boolean buscar(int valor) {

        No atual = inicio;

        while (atual != null) {

            if (atual.valor == valor) {
                return true;
            }

            atual = atual.proximo;
        }

        return false;
    }

    // Remove um valor da lista
    @Override
    public boolean remover(int valor) {

        No atual = inicio;

        // Procura o nó que contém o valor
        while (atual != null && atual.valor != valor) {
            atual = atual.proximo;
        }

        // Valor não encontrado
        if (atual == null) {
            return false;
        }

        // Caso seja o primeiro nó
        if (atual.anterior == null) {
            inicio = atual.proximo;
        } else {
            atual.anterior.proximo = atual.proximo;
        }

        // Caso seja o último nó
        if (atual.proximo == null) {
            fim = atual.anterior;
        } else {
            atual.proximo.anterior = atual.anterior;
        }

        tamanho--;
        return true;
    }

    // Conta os valores dentro do intervalo informado
    @Override
    public int consultarIntervalo(int minimo, int maximo) {

        int quantidade = 0;
        No atual = inicio;

        while (atual != null) {

            if (atual.valor >= minimo && atual.valor <= maximo) {
                quantidade++;
            }

            atual = atual.proximo;
        }

        return quantidade;
    }

    // Retorna a quantidade de elementos
    @Override
    public int tamanho() {
        return tamanho;
    }
}
