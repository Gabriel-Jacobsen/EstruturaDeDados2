package Estrutura;


public class ArvoreBinariaBusca implements EstruturaDados {

    private static class No {
        int valor;
        No esquerda;
        No direita;

        public No(int valor) {
            this.valor = valor;
        }
    }

    private No raiz;
    private int tamanho;

    public ArvoreBinariaBusca() {
        this.raiz = null;
        this.tamanho = 0;
    }

    // INSERIR
    @Override
    public boolean inserir(int valor) {

        No novo = new No(valor);

        if (raiz == null) {
            raiz = novo;
            tamanho++;
            return true;
        }

        No atual = raiz;
        No pai = null;

        while (atual != null) {

            pai = atual;

            if (valor == atual.valor) {
                return false;
            }

            if (valor < atual.valor) {
                atual = atual.esquerda;
            } else {
                atual = atual.direita;
            }
        }

        if (valor < pai.valor) {
            pai.esquerda = novo;
        } else {
            pai.direita = novo;
        }

        tamanho++;
        return true;
    }

    // BUSCAR
    @Override
    public boolean buscar(int valor) {

        No atual = raiz;

        while (atual != null) {

            if (valor == atual.valor) {
                return true;
            }

            if (valor < atual.valor) {
                atual = atual.esquerda;
            } else {
                atual = atual.direita;
            }
        }

        return false;
    }

    // REMOVER
    @Override
    public boolean remover(int valor) {

        No atual = raiz;
        No pai = null;

        // Localiza o nó
        while (atual != null && atual.valor != valor) {

            pai = atual;

            if (valor < atual.valor) {
                atual = atual.esquerda;
            } else {
                atual = atual.direita;
            }
        }

        // Valor não encontrado
        if (atual == null) {
            return false;
        }

        // CASO 1: Nó com dois filhos
        if (atual.esquerda != null && atual.direita != null) {

            // Encontra o sucessor (menor valor da subárvore direita)
            No paiSucessor = atual;
            No sucessor = atual.direita;

            while (sucessor.esquerda != null) {
                paiSucessor = sucessor;
                sucessor = sucessor.esquerda;
            }

            // Substitui o valor pelo sucessor
            atual.valor = sucessor.valor;

            // O sucessor não possui filho à esquerda
            // Portanto, basta removê-lo considerando seu possível filho direito
            if (paiSucessor.esquerda == sucessor) {
                paiSucessor.esquerda = sucessor.direita;
            } else {
                paiSucessor.direita = sucessor.direita;
            }

        } else {

            // CASO 2: Nó folha (sem filhos)
            // CASO 3: Nó com apenas um filho

            No filho;

            if (atual.esquerda != null) {
                filho = atual.esquerda;
            } else {
                filho = atual.direita;
            }

            // Atualização da raiz
            if (pai == null) {
                raiz = filho;
            }

            // Atualização do filho esquerdo ou direito do pai
            else if (pai.esquerda == atual) {
                pai.esquerda = filho;
            } else {
                pai.direita = filho;
            }
        }

        tamanho--;
        return true;
    }

    // CONSULTAR INTERVALO
    @Override
    public int consultarIntervalo(int minimo, int maximo) {

        if (minimo > maximo) {
            return 0;
        }

        int quantidade = 0;

        // Pilha própria, sem utilizar estruturas proibidas
        No[] pilha = new No[tamanho + 1];
        int topo = -1;

        No atual = raiz;

        while (atual != null || topo >= 0) {

            // Percorre para a esquerda somente quando necessário
            while (atual != null) {

                if (atual.valor >= minimo) {
                    pilha[++topo] = atual;
                    atual = atual.esquerda;
                } else {
                    atual = atual.direita;
                }
            }

            if (topo >= 0) {

                atual = pilha[topo--];

                // Como a árvore é BST, os próximos valores
                // serão maiores ou iguais ao atual.
                if (atual.valor > maximo) {
                    break;
                }

                if (atual.valor >= minimo) {
                    quantidade++;
                }

                atual = atual.direita;
            }
        }

        return quantidade;
    }

    // TAMANHO
    @Override
    public int tamanho() {
        return tamanho;
    }
}
