package dados;


import java.util.Random;

public class GeradorDados {

    // Gera os números pares de 2 até 2n
    public int[] gerarPares(int n) {
        int[] dados = new int[n];

        for (int i = 0; i < n; i++) {
            dados[i] = (i + 1) * 2;
        }

        return dados;
    }

    // Gera os números pares em ordem crescente
    public int[] gerarCrescente(int n) {
        return gerarPares(n);
    }

    // Gera os números pares em ordem decrescente
    public int[] gerarDecrescente(int n) {
        int[] dados = gerarPares(n);

        for (int i = 0; i < n / 2; i++) {
            int temp = dados[i];
            dados[i] = dados[n - 1 - i];
            dados[n - 1 - i] = temp;
        }

        return dados;
    }

    // Gera os números pares em ordem aleatória
    // Utiliza obrigatoriamente a semente 42
    public int[] gerarAleatorio(int n) {
        int[] dados = gerarPares(n);

        return embaralhar(dados, 42);
    }

    // Embaralha um conjunto utilizando uma semente específica
    // Retorna um novo array, preservando o original
    public int[] embaralhar(int[] dados, long semente) {
        int[] resultado = dados.clone();

        Random random = new Random(semente);

        // Algoritmo Fisher-Yates
        for (int i = resultado.length - 1; i > 0; i--) {
            int j = random.nextInt(i + 1);

            int temp = resultado[i];
            resultado[i] = resultado[j];
            resultado[j] = temp;
        }

        return resultado;
    }
}
