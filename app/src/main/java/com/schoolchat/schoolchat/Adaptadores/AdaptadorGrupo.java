package com.schoolchat.schoolchat.Adaptadores;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.schoolchat.schoolchat.Firebase.conexion;
import com.schoolchat.schoolchat.R;
import com.schoolchat.schoolchat.UserInterface.Chat;
import com.schoolchat.schoolchat.moldes.MoldeUsuario;

import java.util.List;


public class AdaptadorGrupo extends RecyclerView.Adapter<AdaptadorGrupo.ViewHolderGrupo>{
    private List<MoldeUsuario> ListaUsuarios;
    private Context scontext;
    private String nombreuser;
    private String fechacreacion;

    public AdaptadorGrupo(Context context, List<MoldeUsuario> Usuariosfirebase){
        ListaUsuarios=Usuariosfirebase;
        scontext=context;
    }

    @Override
    public ViewHolderGrupo onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolderGrupo(scontext, LayoutInflater.from(parent.getContext()).inflate(R.layout.grupo,parent,false));
    }
    @Override
    public void onBindViewHolder(ViewHolderGrupo holder, int position) {
        MoldeUsuario usuarioseleccionado=ListaUsuarios.get(position);
        //establecer nombre de usuario
        holder.getUserName().setText(usuarioseleccionado.getnombre());

    }

    @Override
    public int getItemCount() {
        return ListaUsuarios.size();
    }

    public void refill(MoldeUsuario usuarios){
        ListaUsuarios.add(usuarios);
        notifyDataSetChanged();
    }

    public void setNombre_Fechauser(String nombreUser,String fecha){
        nombreuser=nombreUser;
        fechacreacion=fecha;
    }

    public void cambioUsuario(int index,MoldeUsuario usuario){
        ListaUsuarios.set(index,usuario);
        notifyDataSetChanged();
    }
    public class ViewHolderGrupo extends RecyclerView.ViewHolder implements View.OnClickListener{
        private TextView nombre;
        private ImageView imagenConexion;
        private Context contextHolder;
        public ViewHolderGrupo(Context context,View itemView){
            super(itemView);
            nombre=(TextView)itemView.findViewById(R.id.nombre);
            imagenConexion=(ImageView)itemView.findViewById(R.id.imagenEstado);
            contextHolder=context;
            itemView.setOnClickListener(this);
        }
        public TextView getUserName(){
            return nombre;
        }

        @Override
        public void onClick(View v) {
            int posicion=getLayoutPosition();//obtine la posicion de la fila seleccionada
            MoldeUsuario moldeUsuario=ListaUsuarios.get(posicion);
            moldeUsuario.setEmisorNombre(nombreuser);
            moldeUsuario.setEmisorCreado(fechacreacion);
            Intent chat=new Intent(contextHolder,Chat.class);
            chat.putExtra(conexion.INFO_USUARIO,moldeUsuario);
            contextHolder.startActivity(chat);
        }
    }
}
