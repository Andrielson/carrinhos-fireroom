package tk.andrielson.carrinhos.androidapp.data.dao;

import android.arch.lifecycle.LiveData;

import java.util.List;

import tk.andrielson.carrinhos.androidapp.data.model.Produto;

/**
 * Created by Andrielson on 02/03/2018.
 */

public interface ProdutoDao<T extends Produto> {

    /**
     * Insere um produto no banco de dados e retorna o código gerado.
     *
     * @param produto o produto a ser inserido
     * @return o código gerado para o produto
     */
    long insert(T produto);

    /**
     * Atualiza as informações de um produto já existente no banco de dados.
     *
     * @param produto o produto a ser atualizado
     * @return o número de produtos atualizados
     */
    int update(T produto);

    /**
     * Remove um produto do banco de dados
     *
     * @param produto o produto a ser removido
     * @return o número de produto removidos
     */
    int delete(T produto);

    /**
     * Consulta todos os produtos do banco de dados e retorna uma lista
     * encapsulada numa LiveData observável, para manter a lista sempre atualizada.
     *
     * @return a lista de produtos encapsulada em uma LiveData
     */
    LiveData<List<T>> getAll();

    /**
     * Procura um produto especificado pelo código informado e o retorna encapsulado
     * numa LiveData para sempre manter a informação atualizada.
     *
     * @param codigo o código do produto a ser procurado/retornado
     * @return o produto encapsulado em uma LiveData
     */
    LiveData<T> getByCodigo(Long codigo);
}
