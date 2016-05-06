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
    private Context context;
    private String nombreuser;

    public AdaptadorUsuarios(Context contexto,List<MoldeUsuario> Usuariosfirebase){
        ListaUsuarios=Usuariosfirebase;
        context=contexto;
    }

    @Override
    public ViewHolderUsuarios onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolderUsuarios(context, LayoutInflater.from(parent.getContext()).inflate(R.layout.perfil,parent,false));
    }

    @Override
    public void onBindViewHolder(ViewHolderUsuarios holder, int position) {
        MoldeUsuario usuarioseleccionado=ListaUsuarios.get(position);
        //establecer nombre de usuario
        holder.getNombre().setText(usuarioseleccionado.getEnombre());
        holder.getEstadoConexion().setText(usuarioseleccionado.getConexion());
        //poner color al estado de conexion rojo o verde
        if(usuarioseleccionado.getConexion().equals(conexion.ESTADO_ONLINE)){
            holder.getEstadoConexion().setTextColor(Color.parseColor("#00FF00"));
        }else{
            holder.getEstadoConexion().setTextColor(Color.parseColor("#FF0000"));
        }
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
        private Context contexto;
        public ViewHolderUsuarios(Context context,View view){
            super(view);
            nombre=(TextView)view.findViewById(R.id.nombre);
            estadoConexion=(TextView)view.findViewById(R.id.estado);
            contexto=context;
            view.setOnClickListener(this);
        }
        public TextView getNombre(){
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
            Intent chat=new Intent(context,Chat.class);
            chat.putExtra("usersData",usuario);
            context.startActivity(chat);
        }
    }
}
