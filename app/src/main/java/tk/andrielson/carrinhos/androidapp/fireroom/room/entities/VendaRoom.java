package tk.andrielson.carrinhos.androidapp.fireroom.room.entities;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Locale;

import tk.andrielson.carrinhos.androidapp.fireroom.model.VendaImpl;

@Entity(tableName = "tb_venda", foreignKeys = @ForeignKey(entity = VendedorRoom.class, parentColumns = "codigo", childColumns = "cod_vendedor"))
public class VendaRoom {

    @Ignore
    private static final DateFormat formato = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
    @PrimaryKey
    public Long codigo;
    public String data;
    public Integer comissao;
    public String status;
    @ColumnInfo(name = "cod_vendedor")
    public Long vendedor;

    public VendaRoom() {

    }

    public VendaRoom(VendaImpl venda) {
        this.codigo = venda.getCodigo();
        this.data = formato.format(venda.getData());
        this.comissao = venda.getComissao();
        this.status = venda.getStatus();
        this.vendedor = venda.getVendedor().getCodigo();
    }
}
