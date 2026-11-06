package experimentos;


import java.util.Map;
import java.util.function.Supplier;
import Estrutura.EstruturaDados;

public class ValidadorResultados {

    private final Map<String, Supplier<EstruturaDados>> estruturas;

    private final int[] dadosIniciais;
    private final int[] buscasPresentes;
    private final int[] buscasAusentes;
    private final int[] insercoes;
    private final int[] remocoes;
    private final int[][] intervalos;

    public ValidadorResultados(
            Map<String, Supplier<EstruturaDados>> estruturas,
            int[] dadosIniciais,
            int[] buscasPresentes,
            int[] buscasAusentes,
            int[] insercoes,
            int[] remocoes,
            int[][] intervalos) {

        this.estruturas = estruturas;

        this.dadosIniciais = dadosIniciais.clone();
        this.buscasPresentes = buscasPresentes.clone();
        this.buscasAusentes = buscasAusentes.clone();
        this.insercoes = insercoes.clone();
        this.remocoes = remocoes.clone();

        this.intervalos = new int[intervalos.length][2];

        for (int i = 0; i < intervalos.length; i++) {
            this.intervalos[i] = intervalos[i].clone();
        }
    }

    // Cria uma estrutura e insere os dados iniciais.
    private EstruturaDados criarEstruturaInicial(
            Supplier<EstruturaDados> fabrica) {

        EstruturaDados estrutura = fabrica.get();

        for (int valor : dadosIniciais) {
            estrutura.inserir(valor);
        }

        if (estrutura.tamanho() != dadosIniciais.length) {
            throw new IllegalStateException(
                "Quantidade inicial incorreta."
            );
        }

        return estrutura;
    }

    // 1. VALIDAÇÃO DE BUSCAS PRESENTES

    public void validarBuscaPresente() {

        for (Map.Entry<String, Supplier<EstruturaDados>> entrada
                : estruturas.entrySet()) {

            String nome = entrada.getKey();

            EstruturaDados estrutura =
                criarEstruturaInicial(entrada.getValue());

            int encontrados = 0;

            for (int valor : buscasPresentes) {

                if (estrutura.buscar(valor)) {
                    encontrados++;
                }
            }

            if (encontrados != buscasPresentes.length) {

                throw new IllegalStateException(
                    nome + ": quantidade incorreta de buscas presentes."
                );
            }
        }

        System.out.println("Buscas presentes: OK");
    }

    // 2. VALIDAÇÃO DE BUSCAS AUSENTES

    public void validarBuscaAusente() {

        for (Map.Entry<String, Supplier<EstruturaDados>> entrada
                : estruturas.entrySet()) {

            String nome = entrada.getKey();

            EstruturaDados estrutura =
                criarEstruturaInicial(entrada.getValue());

            for (int valor : buscasAusentes) {

                if (estrutura.buscar(valor)) {

                    throw new IllegalStateException(
                        nome + ": encontrou um valor que deveria estar ausente: "
                        + valor
                    );
                }
            }
        }

        System.out.println("Buscas ausentes: OK");
    }

    // 3. VALIDAÇÃO DE INSERÇÕES

    public void validarInsercao() {

        for (Map.Entry<String, Supplier<EstruturaDados>> entrada
                : estruturas.entrySet()) {

            String nome = entrada.getKey();

            EstruturaDados estrutura =
                criarEstruturaInicial(entrada.getValue());

            int inseridos = 0;

            for (int valor : insercoes) {

                if (estrutura.inserir(valor)) {
                    inseridos++;
                }
            }

            int tamanhoEsperado =
                dadosIniciais.length + insercoes.length;

            if (inseridos != insercoes.length) {

                throw new IllegalStateException(
                    nome + ": quantidade incorreta de inserções."
                );
            }

            if (estrutura.tamanho() != tamanhoEsperado) {

                throw new IllegalStateException(
                    nome + ": tamanho incorreto após inserções."
                );
            }

            // Verifica se todos os valores iniciais continuam presentes.
            for (int valor : dadosIniciais) {

                if (!estrutura.buscar(valor)) {

                    throw new IllegalStateException(
                        nome + ": valor inicial desapareceu: " + valor
                    );
                }
            }

            // Verifica se todos os novos valores foram inseridos.
            for (int valor : insercoes) {

                if (!estrutura.buscar(valor)) {

                    throw new IllegalStateException(
                        nome + ": valor inserido não encontrado: " + valor
                    );
                }
            }
        }

        System.out.println("Inserções: OK");
    }

    // 4. VALIDAÇÃO DE REMOÇÕES

    public void validarRemocao() {

        for (Map.Entry<String, Supplier<EstruturaDados>> entrada
                : estruturas.entrySet()) {

            String nome = entrada.getKey();

            EstruturaDados estrutura =
                criarEstruturaInicial(entrada.getValue());

            int removidos = 0;

            for (int valor : remocoes) {

                if (estrutura.remover(valor)) {
                    removidos++;
                }
            }

            int tamanhoEsperado =
                dadosIniciais.length - remocoes.length;

            if (removidos != remocoes.length) {

                throw new IllegalStateException(
                    nome + ": quantidade incorreta de remoções."
                );
            }

            if (estrutura.tamanho() != tamanhoEsperado) {

                throw new IllegalStateException(
                    nome + ": tamanho incorreto após remoções."
                );
            }

            // Os valores removidos não podem mais existir.
            for (int valor : remocoes) {

                if (estrutura.buscar(valor)) {

                    throw new IllegalStateException(
                        nome + ": valor removido ainda existe: " + valor
                    );
                }
            }

            // Os demais valores iniciais devem continuar presentes.
            for (int valor : dadosIniciais) {

                boolean foiRemovido = false;

                for (int removido : remocoes) {
                    if (valor == removido) {
                        foiRemovido = true;
                        break;
                    }
                }

                if (!foiRemovido && !estrutura.buscar(valor)) {

                    throw new IllegalStateException(
                        nome + ": valor que deveria permanecer não foi encontrado: "
                        + valor
                    );
                }
            }
        }

        System.out.println("Remoções: OK");
    }

    // 5. VALIDAÇÃO DE CONSULTAS POR INTERVALO

    public void validarIntervalo() {

        for (Map.Entry<String, Supplier<EstruturaDados>> entrada
                : estruturas.entrySet()) {

            String nome = entrada.getKey();

            EstruturaDados estrutura =
                criarEstruturaInicial(entrada.getValue());

            for (int[] intervalo : intervalos) {

                int minimo = intervalo[0];
                int maximo = intervalo[1];

                int esperado = 0;

                // Calcula o resultado esperado usando os dados originais.
                for (int valor : dadosIniciais) {

                    if (valor >= minimo && valor <= maximo) {
                        esperado++;
                    }
                }

                int resultado = estrutura.consultarIntervalo(
                    minimo,
                    maximo
                );

                if (resultado != esperado) {

                    throw new IllegalStateException(
                        nome + ": resultado incorreto no intervalo ["
                        + minimo + ", " + maximo + "]. Esperado: "
                        + esperado + ", obtido: " + resultado
                    );
                }
            }
        }

        System.out.println("Consultas por intervalo: OK");
    }

    // Executa todas as validações.
    public void validarTodos() {

        validarBuscaPresente();
        validarBuscaAusente();
        validarInsercao();
        validarRemocao();
        validarIntervalo();

        System.out.println(
            "\nTodas as estruturas passaram nas validações!"
        );
    }
}
