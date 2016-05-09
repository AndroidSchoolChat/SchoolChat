package com.schoolchat.schoolchat.Adaptadores;


import android.support.v7.widget.RecyclerView;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.schoolchat.schoolchat.R;
import com.schoolchat.schoolchat.moldes.MoldeMensajes;

import java.util.List;

public class AdaptadorConversacion extends RecyclerView.Adapter<RecyclerView.ViewHolder>{
    private List<MoldeMensajes> ListaMensajes;
    private static final int EMISOR=0;
    private static final int RECEPTOR=1;

    public AdaptadorConversacion(List<MoldeMensajes> conversacionFirebase){
        ListaMensajes=conversacionFirebase;
    }
    public int getItemViewType(int position){
        if(ListaMensajes.get(position).getReceptorOEmisorEstado()==EMISOR){
            return EMISOR;
        }else {
            return RECEPTOR;
        }
    }
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder viewHolder;
        LayoutInflater inflater=LayoutInflater.from(parent.getContext());
        switch (viewType){
            case EMISOR:
                View viewEmisor=inflater.inflate(R.layout.mensaje_enviado,parent,false);
                viewHolder=new ViewHolderEmisor(viewEmisor);
                break;
            case RECEPTOR:
                View viewReceptor=inflater.inflate(R.layout.mensaje_recibido,parent,false);
                viewHolder=new ViewHolderReceptor(viewReceptor);
                break;
            default:
                View viewEmisorDefecto=inflater.inflate(R.layout.mensaje_enviado,parent,false);
                viewHolder=new ViewHolderEmisor(viewEmisorDefecto);
                break;
        }
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        switch (holder.getItemViewType()){
            case EMISOR:
                ViewHolderEmisor viewHolderEmisor=(ViewHolderEmisor)holder;
                configurarEmisorView(viewHolderEmisor,position);
                break;
            case RECEPTOR:
                ViewHolderReceptor viewHolderReceptor=(ViewHolderReceptor) holder;
                configurarReceptorView(viewHolderReceptor,position);
                break;
        }
    }
    public void configurarEmisorView(ViewHolderEmisor viewHolderEmisor,int position){
        MoldeMensajes enviarmensje=ListaMensajes.get(position);
        viewHolderEmisor.getTextViewEviarMensaje().setText(enviarmensje.getMensaje());
    }
    public void configurarReceptorView(ViewHolderReceptor viewHolderReceptor,int position){
        MoldeMensajes mensajerecibido=ListaMensajes.get(position);
        viewHolderReceptor.getTextViewRecibirMensaje().setText(mensajerecibido.getMensaje());
    }
    @Override
    public int getItemCount() {
        return ListaMensajes.size();
    }
    public void refillAdapter(MoldeMensajes NuevoMensaje){
        //añadir nuevo mensaje
        ListaMensajes.add(NuevoMensaje);
        //actualizar view
        notifyItemInserted(getItemCount()-1);
    }
    public void refillPrimeraVezAdapter(List<MoldeMensajes> nuevomensaje){
        ListaMensajes.clear();
        ListaMensajes.addAll(nuevomensaje);
        notifyItemInserted(getItemCount()-1);
    }
    public void cleanUp(){ListaMensajes.clear();}
    //viewholder para emisor
    public class ViewHolderEmisor extends RecyclerView.ViewHolder{
        private TextView textViewEviarMensaje;
        public ViewHolderEmisor(View view){
            super(view);
            textViewEviarMensaje=(TextView)view.findViewById(R.id.mensajeenviado);
        }
        public TextView getTextViewEviarMensaje(){return textViewEviarMensaje;}
        public void setTextViewEviarMensaje(TextView mensajeenviado){
            textViewEviarMensaje=mensajeenviado;
        }
    }
    //viewholder para receptor
    public class ViewHolderReceptor extends RecyclerView.ViewHolder{
        private TextView textViewRecibirMensaje;
        public ViewHolderReceptor(View view){
            super(view);
            textViewRecibirMensaje=(TextView)view.findViewById(R.id.mensajerecibido);
        }
        public TextView getTextViewRecibirMensaje(){return textViewRecibirMensaje;}
        public void setTextViewRecibirMensaje(TextView mensajerecibido){
            textViewRecibirMensaje=mensajerecibido;
        }
    }
}
