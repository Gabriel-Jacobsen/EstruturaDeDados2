package App;

import Estrutura.ArrayLinear;
import Estrutura.ArrayOrdenado;
import Estrutura.ArvoreAVL;
import Estrutura.ArvoreBinariaBusca;
import Estrutura.ArvoreRubroNegra;
import Estrutura.EstruturaDados;
import Estrutura.ListaDuplamenteEncadeada;
import Estrutura.TabelaHash;

public class FabricaEstruturas {

    public static EstruturaDados criar(String tipo, int capacidade) {

        switch (tipo) {

            case "ArrayLinear":
                return new ArrayLinear(capacidade);

            case "ArrayOrdenado":
                return new ArrayOrdenado(capacidade);

            case "Lista":
                return new ListaDuplamenteEncadeada();

            case "Hash":
                return new TabelaHash();

            case "BST":
                return new ArvoreBinariaBusca();

            case "AVL":
                return new ArvoreAVL();

            case "RubroNegra":
                return new ArvoreRubroNegra();

            default:
                throw new IllegalArgumentException(
                    "Estrutura desconhecida: " + tipo
                );
        }
    }

    public static String[] getTipos() {
        return new String[] {
            "ArrayLinear",
            "ArrayOrdenado",
            "Lista",
            "Hash",
            "BST",
            "AVL",
            "RubroNegra"
        };
    }
}
