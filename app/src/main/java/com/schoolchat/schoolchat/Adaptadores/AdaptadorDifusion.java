package com.schoolchat.schoolchat.Adaptadores;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import com.schoolchat.schoolchat.Firebase.conexion;
import com.schoolchat.schoolchat.R;
import com.schoolchat.schoolchat.UserInterface.Chat;
import com.schoolchat.schoolchat.moldes.MoldeUsuario;

import java.util.List;


public class AdaptadorDifusion extends RecyclerView.Adapter<AdaptadorDifusion.ViewHolderDifusion>{
    private List<MoldeUsuario> ListaUsuarios;
    private Context scontext;
    private String nombreuser;
    private String fechacreacion;

    public AdaptadorDifusion(Context context,List<MoldeUsuario> Usuariosfirebase){
        ListaUsuarios=Usuariosfirebase;
        scontext=context;
    }

    @Override
    public ViewHolderDifusion onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolderDifusion(scontext, LayoutInflater.from(parent.getContext()).inflate(R.layout.difusion,parent,false));
    }
    @Override
    public void onBindViewHolder(ViewHolderDifusion holder, int position) {
        MoldeUsuario usuarioseleccionado=ListaUsuarios.get(position);
        //establecer nombre de usuario
        holder.getUserName().setText(usuarioseleccionado.getnombre());
        holder.getEstadoConexion().setText(usuarioseleccionado.getconexion());
        if(usuarioseleccionado.getconexion().equals(conexion.ESTADO_ONLINE)){
            holder.getEstadoConexion().setTextColor(Color.parseColor("#00FF00"));
        }else{
            holder.getEstadoConexion().setTextColor(Color.parseColor("#FF0000"));
        }
        //esto comprueba que usuarios estan seleccionados ¡¡EN DESARROLLO!!
        if(holder.getSeleccionar().isChecked()){
            Toast toast1 =
                    Toast.makeText(scontext,
                            usuarioseleccionado.getemail(), Toast.LENGTH_SHORT);

            toast1.show();
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

    public void setNombre_Fechauser(String nombreUser,String fecha){
        nombreuser=nombreUser;
        fechacreacion=fecha;
    }

    public class ViewHolderDifusion extends RecyclerView.ViewHolder implements View.OnClickListener{
        private TextView nombre;
        private TextView estadoConexion;
        private CheckBox seleccionar;
        private Context contextHolder;
        public ViewHolderDifusion(Context context,View itemView){
            super(itemView);
            nombre=(TextView)itemView.findViewById(R.id.nombre);
            estadoConexion=(TextView)itemView.findViewById(R.id.estado);
            seleccionar=(CheckBox)itemView.findViewById(R.id.seleccion);
            contextHolder=context;
        }
        public TextView getUserName(){
            return nombre;
        }
        public TextView getEstadoConexion(){
            return estadoConexion;
        }
        public CheckBox getSeleccionar(){return seleccionar;}

        @Override
        public void onClick(View v) {
            int posicion=getLayoutPosition();//obtine la posicion de la fila seleccionada
            MoldeUsuario usuario=ListaUsuarios.get(posicion);
            usuario.setEnombre(nombreuser);
            usuario.seteCreado(fechacreacion);
        }
    }
}
