package Estrutura;


public class ArvoreRubroNegra implements EstruturaDados {

    private static final boolean VERMELHO = true;
    private static final boolean PRETO = false;

    private static class No {
        int valor;
        boolean vermelho;

        No pai;
        No esquerda;
        No direita;

        No(int valor, boolean vermelho) {
            this.valor = valor;
            this.vermelho = vermelho;
        }
    }

    private No raiz;
    private final No sentinela;
    private int tamanho;

    public ArvoreRubroNegra() {
        sentinela = new No(0, PRETO);

        sentinela.pai = sentinela;
        sentinela.esquerda = sentinela;
        sentinela.direita = sentinela;

        raiz = sentinela;
        tamanho = 0;
    }

    // =====================================================
    // BUSCAR
    // =====================================================

    @Override
    public boolean buscar(int valor) {
        return buscarNo(valor) != sentinela;
    }

    private No buscarNo(int valor) {
        No atual = raiz;

        while (atual != sentinela) {

            if (valor == atual.valor) {
                return atual;
            }

            if (valor < atual.valor) {
                atual = atual.esquerda;
            } else {
                atual = atual.direita;
            }
        }

        return sentinela;
    }

    // =====================================================
    // INSERIR
    // =====================================================

    @Override
    public boolean inserir(int valor) {

        No pai = sentinela;
        No atual = raiz;

        // Localiza a posição de inserção
        while (atual != sentinela) {

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

        // Cria o novo nó vermelho
        No novo = new No(valor, VERMELHO);

        novo.pai = pai;
        novo.esquerda = sentinela;
        novo.direita = sentinela;

        // Insere na posição encontrada
        if (pai == sentinela) {
            raiz = novo;
        } else if (valor < pai.valor) {
            pai.esquerda = novo;
        } else {
            pai.direita = novo;
        }

        tamanho++;

        corrigirInsercao(novo);

        return true;
    }

    private void corrigirInsercao(No atual) {

        while (atual.pai.vermelho == VERMELHO) {

            if (atual.pai == atual.pai.pai.esquerda) {

                No tio = atual.pai.pai.direita;

                // Caso 1: tio vermelho
                if (tio.vermelho == VERMELHO) {

                    atual.pai.vermelho = PRETO;
                    tio.vermelho = PRETO;
                    atual.pai.pai.vermelho = VERMELHO;

                    atual = atual.pai.pai;

                } else {

                    // Caso 2: formato de triângulo
                    if (atual == atual.pai.direita) {
                        atual = atual.pai;
                        rotacaoEsquerda(atual);
                    }

                    // Caso 3: formato de linha
                    atual.pai.vermelho = PRETO;
                    atual.pai.pai.vermelho = VERMELHO;

                    rotacaoDireita(atual.pai.pai);
                }

            } else {

                No tio = atual.pai.pai.esquerda;

                // Caso 1: tio vermelho
                if (tio.vermelho == VERMELHO) {

                    atual.pai.vermelho = PRETO;
                    tio.vermelho = PRETO;
                    atual.pai.pai.vermelho = VERMELHO;

                    atual = atual.pai.pai;

                } else {

                    // Caso 2: formato de triângulo
                    if (atual == atual.pai.esquerda) {
                        atual = atual.pai;
                        rotacaoDireita(atual);
                    }

                    // Caso 3: formato de linha
                    atual.pai.vermelho = PRETO;
                    atual.pai.pai.vermelho = VERMELHO;

                    rotacaoEsquerda(atual.pai.pai);
                }
            }
        }

        raiz.vermelho = PRETO;
        raiz.pai = sentinela;
    }

    // =====================================================
    // REMOVER
    // =====================================================

    @Override
    public boolean remover(int valor) {

        No removido = buscarNo(valor);

        if (removido == sentinela) {
            return false;
        }

        No substituto = removido;
        boolean corOriginal = substituto.vermelho;

        No auxiliar;

        if (removido.esquerda == sentinela) {

            auxiliar = removido.direita;
            transplantar(removido, removido.direita);

        } else if (removido.direita == sentinela) {

            auxiliar = removido.esquerda;
            transplantar(removido, removido.esquerda);

        } else {

            substituto = minimo(removido.direita);
            corOriginal = substituto.vermelho;

            auxiliar = substituto.direita;

            if (substituto.pai == removido) {

                auxiliar.pai = substituto;

            } else {

                transplantar(substituto, substituto.direita);

                substituto.direita = removido.direita;
                substituto.direita.pai = substituto;
            }

            transplantar(removido, substituto);

            substituto.esquerda = removido.esquerda;
            substituto.esquerda.pai = substituto;

            substituto.vermelho = removido.vermelho;
        }

        tamanho--;

        if (corOriginal == PRETO) {
            corrigirRemocao(auxiliar);
        }

        return true;
    }

    private void transplantar(No antigo, No novo) {

        if (antigo.pai == sentinela) {
            raiz = novo;
        } else if (antigo == antigo.pai.esquerda) {
            antigo.pai.esquerda = novo;
        } else {
            antigo.pai.direita = novo;
        }

        novo.pai = antigo.pai;
    }

    private No minimo(No atual) {

        while (atual.esquerda != sentinela) {
            atual = atual.esquerda;
        }

        return atual;
    }

    private void corrigirRemocao(No atual) {

        while (atual != raiz && atual.vermelho == PRETO) {

            if (atual == atual.pai.esquerda) {

                No irmao = atual.pai.direita;

                // Caso 1: irmão vermelho
                if (irmao.vermelho == VERMELHO) {

                    irmao.vermelho = PRETO;
                    atual.pai.vermelho = VERMELHO;

                    rotacaoEsquerda(atual.pai);

                    irmao = atual.pai.direita;
                }

                // Caso 2: irmão preto com filhos pretos
                if (irmao.esquerda.vermelho == PRETO &&
                    irmao.direita.vermelho == PRETO) {

                    irmao.vermelho = VERMELHO;
                    atual = atual.pai;

                } else {

                    // Caso 3: filho direito preto
                    if (irmao.direita.vermelho == PRETO) {

                        irmao.esquerda.vermelho = PRETO;
                        irmao.vermelho = VERMELHO;

                        rotacaoDireita(irmao);

                        irmao = atual.pai.direita;
                    }

                    // Caso 4: filho direito vermelho
                    irmao.vermelho = atual.pai.vermelho;
                    atual.pai.vermelho = PRETO;
                    irmao.direita.vermelho = PRETO;

                    rotacaoEsquerda(atual.pai);

                    atual = raiz;
                }

            } else {

                No irmao = atual.pai.esquerda;

                // Caso 1: irmão vermelho
                if (irmao.vermelho == VERMELHO) {

                    irmao.vermelho = PRETO;
                    atual.pai.vermelho = VERMELHO;

                    rotacaoDireita(atual.pai);

                    irmao = atual.pai.esquerda;
                }

                // Caso 2: irmão preto com filhos pretos
                if (irmao.direita.vermelho == PRETO &&
                    irmao.esquerda.vermelho == PRETO) {

                    irmao.vermelho = VERMELHO;
                    atual = atual.pai;

                } else {

                    // Caso 3: filho esquerdo preto
                    if (irmao.esquerda.vermelho == PRETO) {

                        irmao.direita.vermelho = PRETO;
                        irmao.vermelho = VERMELHO;

                        rotacaoEsquerda(irmao);

                        irmao = atual.pai.esquerda;
                    }

                    // Caso 4: filho esquerdo vermelho
                    irmao.vermelho = atual.pai.vermelho;
                    atual.pai.vermelho = PRETO;
                    irmao.esquerda.vermelho = PRETO;

                    rotacaoDireita(atual.pai);

                    atual = raiz;
                }
            }
        }

        atual.vermelho = PRETO;
        sentinela.vermelho = PRETO;
    }

    // =====================================================
    // ROTAÇÃO À ESQUERDA
    // =====================================================

    private void rotacaoEsquerda(No atual) {

        No direito = atual.direita;

        atual.direita = direito.esquerda;

        if (direito.esquerda != sentinela) {
            direito.esquerda.pai = atual;
        }

        direito.pai = atual.pai;

        if (atual.pai == sentinela) {
            raiz = direito;
        } else if (atual == atual.pai.esquerda) {
            atual.pai.esquerda = direito;
        } else {
            atual.pai.direita = direito;
        }

        direito.esquerda = atual;
        atual.pai = direito;
    }

    // =====================================================
    // ROTAÇÃO À DIREITA
    // =====================================================

    private void rotacaoDireita(No atual) {

        No esquerdo = atual.esquerda;

        atual.esquerda = esquerdo.direita;

        if (esquerdo.direita != sentinela) {
            esquerdo.direita.pai = atual;
        }

        esquerdo.pai = atual.pai;

        if (atual.pai == sentinela) {
            raiz = esquerdo;
        } else if (atual == atual.pai.direita) {
            atual.pai.direita = esquerdo;
        } else {
            atual.pai.esquerda = esquerdo;
        }

        esquerdo.direita = atual;
        atual.pai = esquerdo;
    }

    // =====================================================
    // CONSULTA POR INTERVALO
    // =====================================================

    @Override
    public int consultarIntervalo(int minimo, int maximo) {

        if (minimo > maximo) {
            return 0;
        }

        return consultarIntervaloRecursivo(raiz, minimo, maximo);
    }

    private int consultarIntervaloRecursivo(
            No atual, int minimo, int maximo) {

        if (atual == sentinela) {
            return 0;
        }

        int quantidade = 0;

        if (atual.valor >= minimo && atual.valor <= maximo) {
            quantidade++;
        }

        // Só percorre a esquerda se puder haver valores menores
        if (atual.valor > minimo) {
            quantidade += consultarIntervaloRecursivo(
                    atual.esquerda, minimo, maximo);
        }

        // Só percorre a direita se puder haver valores maiores
        if (atual.valor < maximo) {
            quantidade += consultarIntervaloRecursivo(
                    atual.direita, minimo, maximo);
        }

        return quantidade;
    }

    // =====================================================
    // TAMANHO
    // =====================================================

    @Override
    public int tamanho() {
        return tamanho;
    }
}
