package tk.andrielson.carrinhos.androidapp.fireroom.room.entities;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Embedded;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.Index;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Locale;

import tk.andrielson.carrinhos.androidapp.fireroom.firestore.collections.VendaFire;
import tk.andrielson.carrinhos.androidapp.fireroom.model.VendaImpl;

import static android.arch.persistence.room.ForeignKey.CASCADE;

@Entity(tableName = "tb_venda",
        foreignKeys = @ForeignKey(entity = VendedorRoom.class, parentColumns = "vendedor_codigo", childColumns = "vendedor_codigo", onDelete = CASCADE, onUpdate = CASCADE, deferred = true),
        indices = {@Index(name = "idx_venda_vendedor", value = {"vendedor_codigo"})})
public final class VendaRoom {

    @Ignore
    private static final DateFormat formato = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
    @PrimaryKey
    @ColumnInfo(name = "venda_codigo")
    public Long codigo;
    @ColumnInfo(name = "venda_data")
    public String data;
    @ColumnInfo(name = "venda_comissao")
    public Integer comissao;
    @ColumnInfo(name = "venda_status")
    public String status;
    @ColumnInfo(name = "vendedor_codigo")
    public Long vendedor;

    public VendaRoom() {

    }

    public VendaRoom(@NonNull VendaFire venda) {
        this.codigo = venda.codigo;
        this.data = formato.format(venda.data);
        this.comissao = venda.comissao;
        this.status = venda.status;
        this.vendedor = Long.valueOf(venda.vendedor.getId());
    }

    public static VendaImpl getModel(@NonNull VendaComVendedorTotal vcvt) {
        VendaImpl venda = new VendaImpl();
        venda.setCodigo(vcvt.codigo);
        try {
            venda.setData(formato.parse(vcvt.data));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        venda.setComissao(vcvt.comissao);
        venda.setStatus(vcvt.status);
        venda.setVendedor(vcvt.vendedor.getModel());
        venda.setTotal(vcvt.total);
        return venda;
    }

    public static class VendaComVendedorTotal {
        @ColumnInfo(name = "venda_codigo")
        public Long codigo;
        @ColumnInfo(name = "venda_data")
        public String data;
        @ColumnInfo(name = "venda_comissao")
        public Integer comissao;
        @ColumnInfo(name = "venda_status")
        public String status;
        @ColumnInfo(name = "venda_total")
        public Long total;
        @Embedded
        public VendedorRoom vendedor;
    }
}
