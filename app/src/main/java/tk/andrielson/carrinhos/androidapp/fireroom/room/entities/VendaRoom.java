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
import java.text.SimpleDateFormat;
import java.util.Locale;

import tk.andrielson.carrinhos.androidapp.fireroom.firestore.collections.VendaFirestore;

@Entity(tableName = "tb_venda",
        foreignKeys = @ForeignKey(entity = VendedorRoom.class, parentColumns = "codigo", childColumns = "cod_vendedor"),
        indices = {@Index(name = "idx_venda_vendedor", value = {"cod_vendedor"})})
public final class VendaRoom {

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

    public VendaRoom(@NonNull VendaFirestore venda) {
        this.codigo = venda.codigo;
        this.data = formato.format(venda.data);
        this.comissao = venda.comissao;
        this.status = venda.status;
        this.vendedor = Long.valueOf(venda.vendedor.getId());
    }

    public static class VendaTeste {
        @Embedded
        public VendaRoom venda;
        @Embedded
        public VendedorRoom vendedor;
    }
}
