package Estrutura;


public class ArvoreAVL implements EstruturaDados {

    private static class No {
        int valor;
        int altura;
        No esquerda;
        No direita;

        public No(int valor) {
            this.valor = valor;
            this.altura = 1;
            this.esquerda = null;
            this.direita = null;
        }
    }

    private No raiz;
    private int tamanho;

    public ArvoreAVL() {
        this.raiz = null;
        this.tamanho = 0;
    }

    // Retorna a altura de um nó
    private int altura(No no) {
        if (no == null) {
            return 0;
        }

        return no.altura;
    }

    // Calcula o fator de balanceamento
    private int fatorBalanceamento(No no) {
        if (no == null) {
            return 0;
        }

        return altura(no.esquerda) - altura(no.direita);
    }

    // Atualiza a altura de um nó
    private void atualizarAltura(No no) {
        no.altura = 1 + Math.max(
            altura(no.esquerda),
            altura(no.direita)
        );
    }

    // Rotação simples à direita (LL)
    private No rotacaoDireita(No y) {
        No x = y.esquerda;
        No t2 = x.direita;

        x.direita = y;
        y.esquerda = t2;

        atualizarAltura(y);
        atualizarAltura(x);

        return x;
    }

    // Rotação simples à esquerda (RR)
    private No rotacaoEsquerda(No x) {
        No y = x.direita;
        No t2 = y.esquerda;

        y.esquerda = x;
        x.direita = t2;

        atualizarAltura(x);
        atualizarAltura(y);

        return y;
    }

    // Verifica e corrige o balanceamento
    private No balancear(No no) {
        if (no == null) {
            return null;
        }

        atualizarAltura(no);

        int fator = fatorBalanceamento(no);

        // Caso LL
        if (fator > 1 && fatorBalanceamento(no.esquerda) >= 0) {
            return rotacaoDireita(no);
        }

        // Caso LR
        if (fator > 1 && fatorBalanceamento(no.esquerda) < 0) {
            no.esquerda = rotacaoEsquerda(no.esquerda);
            return rotacaoDireita(no);
        }

        // Caso RR
        if (fator < -1 && fatorBalanceamento(no.direita) <= 0) {
            return rotacaoEsquerda(no);
        }

        // Caso RL
        if (fator < -1 && fatorBalanceamento(no.direita) > 0) {
            no.direita = rotacaoDireita(no.direita);
            return rotacaoEsquerda(no);
        }

        return no;
    }

    // INSERÇÃO

    @Override
    public boolean inserir(int valor) {
        if (buscar(valor)) {
            return false;
        }

        raiz = inserir(raiz, valor);
        tamanho++;

        return true;
    }

    private No inserir(No no, int valor) {
        if (no == null) {
            return new No(valor);
        }

        if (valor < no.valor) {
            no.esquerda = inserir(no.esquerda, valor);
        } else {
            no.direita = inserir(no.direita, valor);
        }

        return balancear(no);
    }

    // BUSCA

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

    // REMOÇÃO

    @Override
    public boolean remover(int valor) {
        if (!buscar(valor)) {
            return false;
        }

        raiz = remover(raiz, valor);
        tamanho--;

        return true;
    }

    private No remover(No no, int valor) {
        if (no == null) {
            return null;
        }

        if (valor < no.valor) {
            no.esquerda = remover(no.esquerda, valor);

        } else if (valor > no.valor) {
            no.direita = remover(no.direita, valor);

        } else {

            // Nó sem filhos ou com apenas um filho
            if (no.esquerda == null) {
                return no.direita;
            }

            if (no.direita == null) {
                return no.esquerda;
            }

            // Nó com dois filhos:
            // encontra o menor valor da subárvore direita
            No sucessor = menorValor(no.direita);

            no.valor = sucessor.valor;

            no.direita = remover(no.direita, sucessor.valor);
        }

        return balancear(no);
    }

    // Encontra o menor nó de uma subárvore
    private No menorValor(No no) {
        No atual = no;

        while (atual.esquerda != null) {
            atual = atual.esquerda;
        }

        return atual;
    }

    // CONSULTA POR INTERVALO

    @Override
    public int consultarIntervalo(int minimo, int maximo) {
        if (minimo > maximo) {
            return 0;
        }

        return consultarIntervalo(raiz, minimo, maximo);
    }

    private int consultarIntervalo(No no, int minimo, int maximo) {
        if (no == null) {
            return 0;
        }

        int quantidade = 0;

        if (no.valor >= minimo && no.valor <= maximo) {
            quantidade++;
        }

        // Só percorre a esquerda se houver possibilidade
        // de encontrar valores dentro do intervalo
        if (minimo < no.valor) {
            quantidade += consultarIntervalo(
                no.esquerda, minimo, maximo
            );
        }

        // Só percorre a direita se houver possibilidade
        // de encontrar valores dentro do intervalo
        if (maximo > no.valor) {
            quantidade += consultarIntervalo(
                no.direita, minimo, maximo
            );
        }

        return quantidade;
    }

    // TAMANHO

    @Override
    public int tamanho() {
        return tamanho;
    }
}
