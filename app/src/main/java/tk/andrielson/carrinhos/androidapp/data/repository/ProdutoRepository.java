package tk.andrielson.carrinhos.androidapp.data.repository;

import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;
import android.support.v4.util.SimpleArrayMap;

import java.util.List;

import tk.andrielson.carrinhos.androidapp.data.model.Produto;
import tk.andrielson.carrinhos.androidapp.fireroom.model.ProdutoImpl;

public interface ProdutoRepository<T extends Produto> {
    /**
     * Insere um produto no banco de dados e retorna o código gerado.
     *
     * @param produto o produto a ser inserido
     */
    void insert(Produto produto);

    /**
     * Atualiza as informações de um produto já existente no banco de dados.
     *
     * @param produto o produto a ser atualizado
     */
    void update(Produto produto);

    /**
     * Remove um produto do banco de dados
     *
     * @param produto o produto a ser removido
     */
    void delete(Produto produto);

    /**
     * Consulta todos os produtos do banco de dados e retorna uma lista
     * encapsulada numa LiveData observável, para manter a lista sempre atualizada.
     *
     * @return a lista de produtos encapsulada em uma LiveData
     */
    @NonNull
    LiveData<List<T>> getAll();

    /**
     * Procura um produto especificado pelo código informado e o retorna encapsulado
     * numa LiveData para sempre manter a informação atualizada.
     *
     * @param codigo o código do produto a ser procurado/retornado
     * @return o produto encapsulado em uma LiveData
     */
    @NonNull
    LiveData<T> getByCodigo(Long codigo);

    /**
     * Consulta todos os produtos do banco de dados, ordenando pelos parâmetros de ordenação,
     * encapsulada numa LiveData observável, para manter a lista sempre atualizada.
     *
     * @param ordenacao um mapa no qual a chave indica por qual atributo deve ser ordenado e o valor
     *                  indica a direção (ascendente ou descendente) da ordenação.
     * @return a lista de produtos encapsulada em uma LiveData
     */
    @NonNull
    LiveData<List<T>> getAll(SimpleArrayMap<String, String> ordenacao);
}
