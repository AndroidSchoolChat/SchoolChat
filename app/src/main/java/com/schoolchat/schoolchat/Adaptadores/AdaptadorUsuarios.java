package com.schoolchat.schoolchat.Adaptadores;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.schoolchat.schoolchat.Firebase.conexion;
import com.schoolchat.schoolchat.R;
import com.schoolchat.schoolchat.UserInterface.Chat;
import com.schoolchat.schoolchat.moldes.MoldeUsuario;

import java.util.List;


public class AdaptadorUsuarios extends RecyclerView.Adapter<AdaptadorUsuarios.ViewHolderUsuarios>{
    private List<MoldeUsuario> ListaUsuarios;
    private Context scontext;
    private String nombreuser;

    public AdaptadorUsuarios(Context context,List<MoldeUsuario> Usuariosfirebase){
        ListaUsuarios=Usuariosfirebase;
        scontext=context;
    }

    @Override
    public ViewHolderUsuarios onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolderUsuarios(scontext, LayoutInflater.from(parent.getContext()).inflate(R.layout.perfil,parent,false));
    }

    public void onBindViewHolder(ViewHolderUsuarios holder, int position) {
        MoldeUsuario usuarioseleccionado=ListaUsuarios.get(position);
        System.out.println(position);
        //establecer nombre de usuario
        holder.getUserName().setText(usuarioseleccionado.getNombre());
        holder.getEstadoConexion().setText(usuarioseleccionado.getConexion());

    }

    @Override
    public int getItemCount() {
        return ListaUsuarios.size();
    }

    public void refill(MoldeUsuario usuarios){
        ListaUsuarios.add(usuarios);
        notifyDataSetChanged();
    }

    public void setNombreuser(String nombreUser){
        nombreuser=nombreUser;
    }

    public void cambioUsuario(int index,MoldeUsuario usuario){
        ListaUsuarios.set(index,usuario);
        notifyDataSetChanged();
    }
    public class ViewHolderUsuarios extends RecyclerView.ViewHolder implements View.OnClickListener{
        private TextView nombre;
        private TextView estadoConexion;
        private Context contextHolder;
        public ViewHolderUsuarios(Context context,View itemView){
            super(itemView);
            nombre=(TextView)itemView.findViewById(R.id.nombre);
            estadoConexion=(TextView)itemView.findViewById(R.id.estado);
            contextHolder=context;
            itemView.setOnClickListener(this);
        }
        public TextView getUserName(){
            return nombre;
        }
        public TextView getEstadoConexion(){
            return estadoConexion;
        }

        @Override
        public void onClick(View v) {
            int posicion=getLayoutPosition();//obtine la posicion de la fila seleccionada
            MoldeUsuario usuario=ListaUsuarios.get(posicion);
            usuario.setEnombre(nombreuser);
            Intent chat=new Intent(contextHolder,Chat.class);
            chat.putExtra(conexion.INFO_USER,usuario);
            scontext.startActivity(chat);
        }
    }
}
