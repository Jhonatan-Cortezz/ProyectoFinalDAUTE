package com.daute.proyectofinaldaute.ui.notifications;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.daute.proyectofinaldaute.MySingleton;
import com.daute.proyectofinaldaute.R;
import com.daute.proyectofinaldaute.Setting_VAR;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class NotificationsFragment extends Fragment {
    private Button btnNew;
    ArrayList<String> lista = null;
    private LinearLayoutCompat resultado;
    private ListView lst;
    ArrayList<DtoProductos> listaProductos;

    private NotificationsViewModel notificationsViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        notificationsViewModel =
                ViewModelProviders.of(this).get(NotificationsViewModel.class);
        View root = inflater.inflate(R.layout.fragment_notifications, container, false);
        btnNew = root.findViewById(R.id.btnNuevoProducto);
        resultado = root.findViewById(R.id.fm2P);
        lst = root.findViewById(R.id.lstProducto);

        recibirAllProd();
        btnNew.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (btnNew.isClickable() == true) {
                    Intent intent = new Intent(getContext(), GuardarProductos.class);
                    startActivity(intent);
                }
            }
        });

        return root;
    }

    private void recibirAllProd(){
        listaProductos = new ArrayList<DtoProductos>();
        lista = new ArrayList<String>();
        String urlConsultaCategoria = Setting_VAR.URL_LISTA_PRODUCTOS;
        StringRequest request = new StringRequest(Request.Method.POST, urlConsultaCategoria, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONArray respuestaJSOn = new JSONArray(response);
                    int totalEnct = respuestaJSOn.length();

                    DtoProductos objCategorias = null;
                    for (int i = 0; i < respuestaJSOn.length(); i++){
                        JSONObject categoriaObj = respuestaJSOn.getJSONObject(i);
                        int idC = categoriaObj.getInt("id_producto");
                        String name = categoriaObj.getString("nom_producto");
                        String apelli = categoriaObj.getString("des_producto");
                        double email = categoriaObj.getDouble("stock");
                        double use = categoriaObj.getDouble("precio");
                        String pass = categoriaObj.getString("unidad_medida");
                        int tip = categoriaObj.getInt("estado_producto");
                        int stado = categoriaObj.getInt("categoria");
                        String nombreCat = categoriaObj.getString("nom_categoria");
                        String fecha = categoriaObj.getString("nom_categoria");

                        objCategorias = new DtoProductos(idC, name, apelli, email, use, pass, tip, stado, fecha);

                        listaProductos.add(objCategorias);
                        lista.add(listaProductos.get(i).getIdProd() + " - " + listaProductos.get(i).getNombreProd() + " - " + listaProductos.get(i).getCategoria());

                        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_list_item_1, lista);
                        lst.setAdapter(adapter);

                        lst.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                                String codigo = "" + listaProductos.get(i).getIdProd();
                                String nombre = "" + listaProductos.get(i).getNombreProd();
                                String apellido = "" + listaProductos.get(i).getDescripcion();
                                String correo = "" + listaProductos.get(i).getStock();
                                String usuario = "" + listaProductos.get(i).getPrecio();
                                String clave = "" + listaProductos.get(i).getUnidadMedida();
                                String tipo = "" + listaProductos.get(i).getEstadoProducto();
                                String estadou = "" + listaProductos.get(i).getCategoria();

                                Activity activity = new Activity();
                                Intent intent = new Intent(getActivity(), DetalleProducto.class);
                                intent.putExtra("codigo", codigo);
                                intent.putExtra("nombre", nombre);
                                intent.putExtra("apellido", apellido);
                                intent.putExtra("correo", correo);
                                intent.putExtra("usuario", usuario);
                                intent.putExtra("clave", clave);
                                intent.putExtra("tipo", tipo);
                                intent.putExtra("estado", estadou);

                                startActivity(intent);
                            }
                        });
                    }

                } catch (JSONException ex){
                    String none = ex.toString();
                    Log.i("NO consulta ***** ", none);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                String err = volleyError.toString();
                Log.i("No se pudo **********", err);
            }
        });

        //tiempo de respuesta, establece politica de reintentos
        request.setRetryPolicy(new DefaultRetryPolicy(
                10000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        MySingleton.getInstance(getContext()).addToRequestQueue(request);
    }
}