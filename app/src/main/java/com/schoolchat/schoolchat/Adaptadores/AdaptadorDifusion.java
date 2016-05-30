package com.schoolchat.schoolchat.Adaptadores;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.schoolchat.schoolchat.Firebase.conexion;
import com.schoolchat.schoolchat.R;
import com.schoolchat.schoolchat.UserInterface.Chat;
import com.schoolchat.schoolchat.moldes.MoldeUsuario;

import java.util.ArrayList;
import java.util.List;


public class AdaptadorDifusion extends RecyclerView.Adapter<AdaptadorDifusion.ViewHolderUsuarios>{
    private List<MoldeUsuario> ListaUsuarios;
    private Context scontext;
    private String nombreuser;
    private String fechacreacion;
    //variable para almacenar los usuarios seleccionados
    public static final ArrayList<MoldeUsuario> DifusionUsuarios=new ArrayList<MoldeUsuario>();

    public AdaptadorDifusion(Context context,List<MoldeUsuario> Usuariosfirebase){
        ListaUsuarios=Usuariosfirebase;
        scontext=context;
        DifusionUsuarios.removeAll(DifusionUsuarios);
    }

    @Override
    public ViewHolderUsuarios onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolderUsuarios(scontext, LayoutInflater.from(parent.getContext()).inflate(R.layout.difusion,parent,false));
    }
    @Override
    public void onBindViewHolder(ViewHolderUsuarios holder, int position) {
        MoldeUsuario usuarioseleccionado=ListaUsuarios.get(position);
        //establecer nombre de usuario
        holder.getUserName().setText(usuarioseleccionado.getnombre());
        holder.getCurso().setText(usuarioseleccionado.getcurso());
        if(usuarioseleccionado.getconexion().equals(conexion.ESTADO_ONLINE)){
            holder.getImagenConexion().setImageResource(R.drawable.circle_green);
        }else{
            holder.getImagenConexion().setImageResource(R.drawable.circle_red);
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


    public class ViewHolderUsuarios extends RecyclerView.ViewHolder implements View.OnClickListener{
        private TextView nombre;
        private TextView curso;
        private ImageView imagenConexion;
        private Context contextHolder;
        public ViewHolderUsuarios(Context context,View itemView){
            super(itemView);
            nombre=(TextView)itemView.findViewById(R.id.nombre);
            curso=(TextView)itemView.findViewById(R.id.curso);
            imagenConexion=(ImageView)itemView.findViewById(R.id.imagenEstado);
            contextHolder=context;
            itemView.setOnClickListener(this);
        }
        public TextView getUserName(){
            return nombre;
        }
        public TextView getCurso(){return curso;}
        public ImageView getImagenConexion(){return imagenConexion;}
        //este evento se encargara de almacenar los usuarios y mostrar a traves de su checkbox si han sido seleccionados o no
        @Override
        public void onClick(View v) {
            int posicion=getLayoutPosition();//obtine la posicion de la fila seleccionada
            MoldeUsuario usuario=ListaUsuarios.get(posicion);
            usuario.setEnombre(nombreuser);
            usuario.seteCreado(fechacreacion);
            CheckBox chb=(CheckBox)v.findViewById(R.id.elegir);
            //si el usuario seleccionado no ha sido seleccionado antes se introduce y su chexbox se pone en true
            if(!DifusionUsuarios.contains(usuario)) {
                DifusionUsuarios.add(usuario);
                chb.setChecked(true);
            }else{
                //para deseleccionar un usuario se pulsa de nuevo sobre él para no volver ha introducirlo como ya esta se borra y su checkbox a false
                DifusionUsuarios.remove(usuario);
                chb.setChecked(false);
            }
        }
    }
}
