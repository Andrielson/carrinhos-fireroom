package tk.andrielson.carrinhos.androidapp;

import android.support.annotation.NonNull;

import tk.andrielson.carrinhos.androidapp.data.dao.ProdutoDao;
import tk.andrielson.carrinhos.androidapp.data.dao.VendaDao;
import tk.andrielson.carrinhos.androidapp.data.dao.VendedorDao;
import tk.andrielson.carrinhos.androidapp.data.model.ItemVenda;
import tk.andrielson.carrinhos.androidapp.data.model.Produto;
import tk.andrielson.carrinhos.androidapp.data.model.Venda;
import tk.andrielson.carrinhos.androidapp.data.model.Vendedor;
import tk.andrielson.carrinhos.androidapp.data.repository.ProdutoRepository;
import tk.andrielson.carrinhos.androidapp.data.repository.VendaRepository;
import tk.andrielson.carrinhos.androidapp.data.repository.VendedorRepository;
import tk.andrielson.carrinhos.androidapp.fireroom.firestore.dao.ProdutoDaoImpl;
import tk.andrielson.carrinhos.androidapp.fireroom.firestore.dao.VendaDaoImpl;
import tk.andrielson.carrinhos.androidapp.fireroom.firestore.dao.VendedorDaoImpl;
import tk.andrielson.carrinhos.androidapp.fireroom.model.ItemVendaImpl;
import tk.andrielson.carrinhos.androidapp.fireroom.model.ProdutoImpl;
import tk.andrielson.carrinhos.androidapp.fireroom.model.VendaImpl;
import tk.andrielson.carrinhos.androidapp.fireroom.model.VendedorImpl;

public final class DI {
    private DI() {
        //construtor privado para impedir o instanciamento da classe.
    }

    @NonNull
    public static Produto newProduto() {
        return new ProdutoImpl();
    }

    @NonNull
    public static ProdutoDao newProdutoDao() {
        return new ProdutoDaoImpl();
    }

    @NonNull
    public static Venda newVenda() {
        return new VendaImpl();
    }

    @NonNull
    public static ItemVenda newItemVenda() {
        return new ItemVendaImpl();
    }

    @NonNull
    public static ItemVenda newItemVenda(Produto produto) {
        return new ItemVendaImpl((ProdutoImpl) produto);
    }

    @NonNull
    public static VendaDao newVendaDao() {
        return new VendaDaoImpl();
    }

    @NonNull
    public static Vendedor newVendedor() {
        return new VendedorImpl();
    }

    @NonNull
    public static VendedorDao newVendedorDao() {
        return new VendedorDaoImpl();
    }

    public static ProdutoRepository newProdutoRepository() {
        return null;
    }

    public static VendedorRepository newVendedorRepository() {
        return null;
    }

    public static VendaRepository newVendaRepository() {
        return null;
    }
}
